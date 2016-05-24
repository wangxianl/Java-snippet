package com.dm.fileManage.finalFile.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.dm.entity.BaseEntity;

/**
 * 目录
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "SP_FILE_MENU")
public class SpFileMenu extends BaseEntity {

	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer menuId;
	/** 目录名 */
	private String menuName;
	/** 目录显示名 */
	private String menuDisplayName;
	/** 父节点 */
	private Integer menuParentId;
	/** 目录路径 */
	private String menuPath;
	/** 目录下文件的展现方式,,比如下载列表,树列表文件展示,单位名录等 */
	private Integer menuDisplayType;
	/** 目录显示状态,1:显示,0:隐藏 */
	private Integer menuStatus;
	/** 目录关键字 */
	private String menuLabel;
	/** 排序字段 */
	private Integer menuIndex;
	/** 根目录选择 */
	private Integer menuRootId;
	/** 目录下支持的上传文件类型,后缀正则匹配 */
	private String menuReg;
	/** 根路径 */
	private String rootPath;
	/** 跟路径是否为绝对路径 */
	private boolean rootAbs;
	/** 子节点 */
	private List<SpFileMenu> children;

	@Id
	@Column(name = "MENU_ID", nullable = false)
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@Column(name = "MENU_NAME")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = "MENU_DISPLAY_NAME")
	public String getMenuDisplayName() {
		return menuDisplayName;
	}

	public void setMenuDisplayName(String menuDisplayName) {
		this.menuDisplayName = menuDisplayName;
	}

	@Column(name = "MENU_PARENT_ID")
	public Integer getMenuParentId() {
		return menuParentId;
	}

	public void setMenuParentId(Integer menuParentId) {
		this.menuParentId = menuParentId;
	}

	@Column(name = "MENU_PATH")
	public String getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

	@Column(name = "MENU_DISPLAY_TYPE")
	public Integer getMenuDisplayType() {
		return menuDisplayType;
	}

	public void setMenuDisplayType(Integer menuDisplayType) {
		this.menuDisplayType = menuDisplayType;
	}

	@Column(name = "MENU_STATUS")
	public Integer getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(Integer menuStatus) {
		this.menuStatus = menuStatus;
	}

	@Column(name = "MENU_LABEL")
	public String getMenuLabel() {
		return menuLabel;
	}

	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}

	@Column(name = "MENU_INDEX")
	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	@Column(name = "MENU_ROOT_ID")
	public Integer getMenuRootId() {
		return menuRootId;
	}

	public void setMenuRootId(Integer menuRootId) {
		this.menuRootId = menuRootId;
	}

	@Column(name = "MENU_REG")
	public String getMenuReg() {
		return menuReg;
	}

	public void setMenuReg(String menuReg) {
		this.menuReg = menuReg;
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

	@Transient
	public List<SpFileMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SpFileMenu> children) {
		this.children = children;
	}

}
