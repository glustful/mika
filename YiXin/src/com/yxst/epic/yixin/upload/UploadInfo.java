package com.yxst.epic.yixin.upload;

import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
public class UploadInfo {

    public UploadInfo() {
    }

    private long id;

    @Transient
    private HttpHandler<String> handler;

    private HttpHandler.State state;

    private String uploadUrl;

    private String fileName;

    private String fileSavePath;

    private long progress;

    private long fileLength;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HttpHandler<String> getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler<String> handler) {
        this.handler = handler;
    }

    public HttpHandler.State getState() {
        return state;
    }

    public void setState(HttpHandler.State state) {
        this.state = state;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String downloadUrl) {
        this.uploadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadInfo)) return false;

        UploadInfo that = (UploadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
    
    private boolean isUploading;

	public boolean isUploading() {
		return isUploading;
	}

	public void setUploading(boolean isUploading) {
		this.isUploading = isUploading;
	}
    
}
