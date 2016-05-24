package com.dm.fileManage.finalFile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dm.entity.BaseEntity;

/**
 * 管理目录根节点
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "SP_FILE_ROOT")
public class SpFileRoot extends BaseEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer rootId;
	/** 根路径 */
	private String rootPath;
	/** 跟名 */
	private String rootName;
	/** 是否为绝对路径,1:是,0:不是 */
	private Integer rootAbs;
	/** 根目录下匹配文件后缀的正则,也就是可上传的文件类型 */
	private String rootReg;
	@Id
	@Column(name = "ROOT_ID", nullable = false)
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	public Integer getRootId() {
		return rootId;
	}

	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}
	@Column(name = "ROOT_PATH")
	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	@Column(name = "ROOT_NAME")
	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	@Column(name = "ROOT_ABS")
	public Integer getRootAbs() {
		return rootAbs;
	}

	public void setRootAbs(Integer rootAbs) {
		this.rootAbs = rootAbs;
	}
	@Column(name = "ROOT_REG")
	public String getRootReg() {
		return rootReg;
	}

	public void setRootReg(String rootReg) {
		this.rootReg = rootReg;
	}

}
