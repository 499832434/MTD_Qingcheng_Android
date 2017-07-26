package com.qcjkjg.trafficrules.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author wuxif_000 上传表单数据(多文件和多文本)
 * 
 */
public class MultiUpload {

	/**
	 * 请求的服务器地址
	 */
	private String url;
	private Context context;
	private Handler handler;
	private DefaultHttpClient defaultHttpClient;
	/**
	 * 上传的文本集合...........
	 */
	private List<BasicNameValuePair> texts;
	/**
	 * 上传的文件集合..........
	 */
	private HashMap<File, String> files;

	public MultiUpload(String url,Context context){
		this.url = url;
		this.context=context;
	}

	/**
	 * @param texts
	 *            文本数据集合
	 * @param files
	 *            文件数据集合   因为上传文件的key有可能相同（name="files[]）,所以HashMap<File, String>
	 * 
	 * 
	 *            <form method="post"
	 *            action="http://192.168.2.145/wuxifu/test/uploadMulti.php"
	 *            enctype="multipart/form-data"> <input type="file"
	 *            name="files[]"><br/>
	 *            <input type="file" name="files[]"><br/>
	 *            <input type="file" name="files[]"><br/>
	 *            <input type="text" name="name"></br/> <input type="text"
	 *            name="age"></br/> <input type="submit" value="submit"> </form>
	 */
	public void upload(List<BasicNameValuePair> texts,
			HashMap<File, String> files) {
		this.texts = texts;
		this.files = files;
		((Activity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				iniClient();
				HttpPost httpPost = iniHttpPost();
				try {
					HttpResponse httpResponse = defaultHttpClient
							.execute(httpPost);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// 上传成功
						closeConnect();
						//由于我这边服务器编码为gbk，所以编码设置gbk，如果乱码就改为utf-8
						String result = EntityUtils.toString(
								httpResponse.getEntity(), "gbk");
						Log.e("上传成功........", result);

					}
				} catch (ClientProtocolException e) {
					// e.printStackTrace();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 初始化post的内容
	 * 
	 * @return  HttpPost
	 */
	protected HttpPost iniHttpPost() {
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder create = MultipartEntityBuilder.create();
		// 普通文本的发送，用户名&密码等
		if (texts != null && texts.size() > 0) {
			for (BasicNameValuePair iterable_element : texts) {
				create.addTextBody(iterable_element.getName(),
						iterable_element.getValue());
			}
		}
		// 二进制的发送，文件
		if (files != null && files.size() > 0) {
			Set<Entry<File, String>> entrySet = files.entrySet();
			for (Entry<File, String> iterable_element : entrySet) {
				create.addBinaryBody(iterable_element.getValue(),
						iterable_element.getKey());

			}
		}
		HttpEntity httpEntity = create.build();
		// post内容的设置............
		httpPost.setEntity(httpEntity);
		return httpPost;

	}

	/**
	 * 初始化httpClient
	 */
	private void iniClient() {
		if (defaultHttpClient == null) {
			defaultHttpClient = new DefaultHttpClient();
			// HTTP协议版本1.1
			defaultHttpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// 连接超时
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		}

	};
	/**
	 * 关闭连接
	 */
	private void closeConnect(){
		if(defaultHttpClient!=null){
			ClientConnectionManager connectionManager = defaultHttpClient.getConnectionManager();
			if(connectionManager!=null)
			connectionManager.shutdown();
			defaultHttpClient=null;
		}
	}


}
