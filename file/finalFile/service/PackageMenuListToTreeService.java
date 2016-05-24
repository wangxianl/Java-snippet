package com.dm.fileManage.finalFile.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dm.fileManage.finalFile.dao.SpFileMenuDao;
import com.dm.fileManage.finalFile.entity.SpFileMenu;
import com.dm.subscibe.abst.PackageListToTree;

@Service
public class PackageMenuListToTreeService extends PackageListToTree<SpFileMenu>{
	@Autowired
	private SpFileMenuDao fileMenuDao;

	@Override
	protected SpFileMenu getEntityById(Integer entityKey) {
		return fileMenuDao.queryById(entityKey);
	}

	@Override
	protected Integer getEntityKey(SpFileMenu t) {
		return t.getMenuId();
	}

	@Override
	protected Integer getParentId(SpFileMenu t) {
		return t.getMenuParentId();
	}

	@Override
	protected String getEntityName(SpFileMenu t) {
		return t.getMenuDisplayName();
	}
	
	@Override
	protected Map<String, Object> getEntityMap(SpFileMenu t) {
		Map<String, Object> menuMap= super.getEntityMap(t);
		menuMap.remove("checked");//取消复选框
		menuMap.put("menuId", t.getMenuId());
		menuMap.put("menuLabel", t.getMenuLabel());
		menuMap.put("menuReg", t.getMenuReg());
		menuMap.put("menuIndex", t.getMenuIndex());
		menuMap.put("menuPath", t.getMenuPath());
		menuMap.put("menuName", t.getMenuName());//文件名
		menuMap.put("menuDisplayType", t.getMenuDisplayType());//展示类型
		menuMap.put("menuDisplayName", t.getMenuDisplayName());//展示类型
		menuMap.put("menuParentId", t.getMenuParentId());//展示类型
		menuMap.put("menuStatus", t.getMenuStatus());
		menuMap.put("rootPath", t.getRootPath());
		menuMap.put("rootAbs", t.isRootAbs());
		menuMap.put("menuRootId", t.getMenuRootId());
		return menuMap;
	}
	
	
}
