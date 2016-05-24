package com.dm.fileManage.finalFile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.dm.entity.BaseEntity;

/**
 * 文件
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "SP_FILE_INFO")
public class SpFileInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer fileId;
	/** 文件名 */
	private String fileName;
	/** 文件显示名 */
	private String fileDisplayName;
	/** 所属目录id */
	private Integer menuId;
	/** 文件路径 */
	private String filePath;
	/** 文件展示类型,比如pageoffice展现,html展现 */
	private Integer fileDisplayType;
	/** 文件后缀,也就是文件类型 */
	private String fileExtra;
	/** 文件显示状态,1:显示,0:隐藏 */
	private Integer fileStatus;
	/** 文件大小 */
	private String fileSize;
	/** 文件关键字,用来检索文件 */
	private String fileLabel;
	/** 文件排序字段 */
	private Integer fileIndex;
	/** 根路径 */
	private String rootPath;
	/** 跟路径是否为绝对路径 */
	private boolean rootAbs;

	@Id
	@Column(name = "FILE_ID", nullable = false)
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_DISPLAY_NAME")
	public String getFileDisplayName() {
		return fileDisplayName;
	}

	public void setFileDisplayName(String fileDisplayName) {
		this.fileDisplayName = fileDisplayName;
	}

	@Column(name = "MENU_ID")
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "FILE_DISPLAY_TYPE")
	public Integer getFileDisplayType() {
		return fileDisplayType;
	}

	public void setFileDisplayType(Integer fileDisplayType) {
		this.fileDisplayType = fileDisplayType;
	}

	@Column(name = "FILE_EXTRA")
	public String getFileExtra() {
		return fileExtra;
	}

	public void setFileExtra(String fileExtra) {
		this.fileExtra = fileExtra;
	}

	@Column(name = "FILE_STATUS")
	public Integer getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(Integer fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Column(name = "FILE_SIZE")
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "FILE_LABEL")
	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	@Column(name = "FILE_INDEX")
	public Integer getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	@Transient
	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Transient
	public boolean isRootAbs() {
		return rootAbs;
	}

	public void setRootAbs(boolean rootAbs) {
		this.rootAbs = rootAbs;
	}
}
