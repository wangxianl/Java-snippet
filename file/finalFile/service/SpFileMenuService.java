package com.dm.fileManage.finalFile.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dm.fileManage.finalFile.dao.SpFileInfoDao;
import com.dm.fileManage.finalFile.dao.SpFileMenuDao;
import com.dm.fileManage.finalFile.dao.SpFileRootDao;
import com.dm.fileManage.finalFile.entity.SpFileInfo;
import com.dm.fileManage.finalFile.entity.SpFileMenu;
import com.dm.fileManage.finalFile.entity.SpFileRoot;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SpFileMenuService {
	@Autowired
	private SpFileMenuDao fileMenuDao;
	@Autowired
	private SpFileRootDao fileRootDao;
	@Autowired
	private SpFileInfoDao fileInfoDao;
	
	/**
	 * 根据条件查询目录
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuDisplayName 目录显示名
	 * @param menuLabel 关键字,多个关键字以空格隔开
	 * @return 目录列表
	 */
	public List<SpFileMenu> getMenusByCondition(Integer rootId,Integer menuStatus,String menuDisplayName,String menuLabel){
		List<SpFileMenu> fileMenus= fileMenuDao.getMenusByCondition(rootId, menuStatus, menuDisplayName, menuLabel);
		if(fileMenus!=null&&fileMenus.size()>0){
			//根
			SpFileRoot fileRoot=fileRootDao.queryById(rootId);
			//将跟节点的内容放入
			for(SpFileMenu fileMenu:fileMenus){
				fileMenu.setRootPath(fileRoot.getRootPath());
				fileMenu.setRootAbs(fileRoot.getRootAbs()==1);
			}
		}
		return fileMenus;
	}
	
	/**
	 * 根据条件查询
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuStatus 状态
	 * @param query 显示名或标签,多个可用空格分隔
	 */
	public List<SpFileMenu> getMenus(Integer rootId, Integer menuStatus,String query) {
		List<SpFileMenu> fileMenus= fileMenuDao.getMenus(rootId, menuStatus, query);
		if(fileMenus!=null&&fileMenus.size()>0){
			//根
			SpFileRoot fileRoot=fileRootDao.queryById(rootId);
			//将跟节点的内容放入
			for(SpFileMenu fileMenu:fileMenus){
				fileMenu.setRootPath(fileRoot.getRootPath());
				fileMenu.setRootAbs(fileRoot.getRootAbs()==1);
			}
		}
		return fileMenus;
	}
	
	/**
	 * 根据父节点查询,只查询一级
	 * @param menuParentId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @param menuStatus 状态,1:可见,0:不可见,null则为全部
	 * @return
	 */
	public List<SpFileMenu> getMenusByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		//查询的结果
		List<SpFileMenu> fileMenus=fileMenuDao.getMenusByPid(menuParentId, menuStatus, rootId);
		if(fileMenus!=null&&fileMenus.size()>0){
			//根
			SpFileRoot fileRoot=fileRootDao.queryById(rootId);
			//将跟节点的内容放入
			for(SpFileMenu fileMenu:fileMenus){
				fileMenu.setRootPath(fileRoot.getRootPath());
				fileMenu.setRootAbs(fileRoot.getRootAbs()==1);
			}
		}
		return fileMenus;
	}
	
	/**
	 * 根据父节点查询所有内容
	 * @param menuParentId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @param menuStatus 状态,1:可见,0:不可见,null则为全部
	 */
	public List<SpFileMenu> getAllMenusByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		//查询的结果
		List<SpFileMenu> fileMenus=fileMenuDao.getMenusByPid(menuParentId, menuStatus, rootId);
		if(fileMenus!=null&&fileMenus.size()>0){
			//根
			SpFileRoot fileRoot=fileRootDao.queryById(rootId);
			//将跟节点的内容放入
			for(SpFileMenu fileMenu:fileMenus){
				fileMenu.setRootPath(fileRoot.getRootPath());
				fileMenu.setRootAbs(fileRoot.getRootAbs()==1);
				//查询子节点
				List<SpFileMenu> subMenus= getAllMenusByPid(fileMenu.getMenuId(), menuStatus, rootId);
				if(subMenus!=null&&subMenus.size()>0){
					fileMenu.setChildren(subMenus);
				}
			}
		}
		return fileMenus;
	}
	
	/**
	 * 根据父节点查询所有内容,包括文件
	 */
	public List<Map<String, Object>> getAllByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		List<Map<String,Object>> result=new ArrayList<Map<String, Object>>();
		Map<String,Object> fileStr=null;
		//添加目录
		List<SpFileMenu> fileMenus=fileMenuDao.getMenusByPid(menuParentId, menuStatus, rootId);
		if(fileMenus!=null&&fileMenus.size()>0){
			for (SpFileMenu fileMenu : fileMenus) {
				fileStr= new HashMap<String, Object>();
				fileStr.put("menuId", fileMenu.getMenuId());
				fileStr.put("path", fileMenu.getMenuPath());
				fileStr.put("name", fileMenu.getMenuDisplayName());
				fileStr.put("displayType", fileMenu.getMenuDisplayType());
				fileStr.put("isMenu", true);
				//子节点
				List<Map<String, Object>> children= getAllByPid(fileMenu.getMenuId(), menuStatus, rootId);
				if(children.size()>0){
					fileStr.put("children", children);
				}
				result.add(fileStr);
			}
		}
		//添加文件
		List<SpFileInfo> fileInfos=fileInfoDao.getFilesByCondition(menuParentId, menuStatus, null);
		if(fileInfos!=null&&fileInfos.size()>0){
			for (SpFileInfo fileInfo : fileInfos) {
				fileStr= new HashMap<String, Object>();
				fileStr.put("fileId", fileInfo.getFileId());
				fileStr.put("path", fileInfo.getFilePath());
				fileStr.put("name", fileInfo.getFileDisplayName());
				fileStr.put("displayType", fileInfo.getFileDisplayType());
				fileStr.put("extra", fileInfo.getFileExtra());
				fileStr.put("isMenu", false);
				result.add(fileStr);
			}
		}
		return result;
	}
	
	/**
	 * 根据ID查询目录
	 * @param menuId 当前目录id
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @return 目录列表
	 */
	public List<SpFileMenu> getThisMenus(Integer menuId,Integer rootId){
		
		return fileMenuDao.getThisMenus(menuId,rootId);
	}
	
	/**
	 * 保存
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveMenu(SpFileMenu menu){
		if(menu.getMenuId()!=null){//修改
			fileMenuDao.update(menu);
		}else{//添加
			fileMenuDao.insert(menu);
		}
	}
	
	/**
	 * 删除目录
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteMenus(String ids){
		fileMenuDao.deleteMenus(ids);
	}

}
