package com.qcjkjg.trafficrules.vo;

/**
 * Created by zongshuo on 2017/7/25.
 */
public class Image {
    /* 上传文件的数据 */
    private byte[] data;
    /* 文件名称 */
    private String fileName;
    /* 表单字段名称*/
    private String formName;
    /* 内容类型 */
    private String contentType = "application/octet-stream"; //需要查阅相关的资料

    public Image(String filname,  String formname) {
        this.data = data;
        this.fileName = filname;
        this.formName = formname;
        if(contentType!=null) this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
