package com.dm.fileManage.pojo;

import java.io.File;
import java.io.Serializable;
import java.util.List;
/**文件传输实体类*/
public class FilePojo implements Serializable,Comparable<FilePojo>{
	/**文件类型*/
	public static final Integer TYPE_FILE=1;
	/**文件夹类型*/
	public static final Integer TYPE_FOLDER=2;
	private static final long serialVersionUID = 1L;
	/**文件名*/
	private String fileName;
	/**文件路径*/
	private String filePath;
	/**文件大小*/
	private Long fileSize;
	/**文件大小,文字描述*/
	private String fileSizeStr;
	/**文件类型,1:文件,2: 文件夹*/
	private Integer fileType;
	/**父路径*/
	private String parentPath;
	/**文件后缀*/
	private String fileExtra;
	
	private Long modifiedTime;
	/**子目录*/
	private List<FilePojo> children;
	
	public FilePojo(File file) {
		this.fileName=file.getName();
		this.filePath=file.getPath();
		this.formatSize(file.length());
		this.fileType=file.isFile()?TYPE_FILE:TYPE_FOLDER;
		this.parentPath= file.getParent();
		this.setExtra(file);
		modifiedTime= file.lastModified();
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileSizeStr() {
		return fileSizeStr;
	}
	public void setFileSizeStr(String fileSizeStr) {
		this.fileSizeStr = fileSizeStr;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	public String getFileExtra() {
		return fileExtra;
	}

	public void setFileExtra(String fileExtra) {
		this.fileExtra = fileExtra;
	}

	public List<FilePojo> getChildren() {
		return children;
	}

	public void setChildren(List<FilePojo> children) {
		this.children = children;
	}

	/**初始化文件大小*/
	private void formatSize(long size){
		this.fileSize =size;
		String fileSizeStr=null;
		if (size < 1024 * 1024) {
			fileSizeStr= String.format("%.2f", (double) (size / 1024)) + "KB";
		} else {
			fileSizeStr= String.format("%.2f", (double) (size / 1024 / 1024)) + "MB";
		}
		this.fileSizeStr= fileSizeStr;
	}
	
	/**获取文件后缀名*/
	private void setExtra(File file){
		String extra="";
		if(file.isFile()){
			String fileName=file.getName();
			int index= fileName.lastIndexOf(".");
			if(index!=-1&&index<fileName.length()-1){
				extra= fileName.substring(index+1);
			}
		}
		this.fileExtra =extra;
	}

	@Override
	public int compareTo(FilePojo o) {
		//return fileName.compareTo(o.fileName);
		return (int)(this.modifiedTime - o.modifiedTime);
		
	}
}
