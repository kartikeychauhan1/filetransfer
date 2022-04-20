package com.converter.fileTransfer;

public class DownloadDTO {
private String fileName;
private String fileBytes;
private String workItem;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileBytes() {
		return fileBytes;
	}
	
	public void setFileBytes(String fileBytes) {
		this.fileBytes = fileBytes;
	}
	
	public String getWorkItem() {
		return workItem;
	}
	
	public void setWorkItem(String workItem) {
		this.workItem = workItem;
	}
}
