package com.qcjkjg.trafficrules.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;


public class NetworkUtils {
//	private static final String TAG = "NetworkUtils";

	/**
	 * @description: 用来判断手机是否联网
	 * @param context
	 * @return boolean
	 * @author mengguoqiang
	 */
	public static boolean isNetworkAvailable(final Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}


}
