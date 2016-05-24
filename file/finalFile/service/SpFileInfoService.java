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
public class SpFileInfoService {
	@Autowired
	private SpFileInfoDao fileInfoDao;
	@Autowired
	private SpFileMenuDao fileMenuDao;

	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileDisplayName 文件名
	 * @param fileLabel 关键字,多个关键字以空格隔开
	 * @return 
	 */
	public List<SpFileInfo> getFiles(Integer menuId,Integer fileStatus,String fileDisplayName,String fileLabel){
		return fileInfoDao.getFiles(menuId, fileStatus, fileDisplayName, fileLabel);
	}
	/**
	 * 根据目录查询所有内容
	 * @param menuId 目录id
	 * @param rootId 根节点,也就是对应的模块
	 * @param menuStatus 状态,1:可见,0:不可见,null则为全部
	 */
	public List<SpFileInfo> getAllFiles(Integer menuId,Integer fileStatus,Integer rootId) {
		
		List<SpFileInfo> result = new ArrayList<SpFileInfo>();
		//查询当前目录下的文件
		List<SpFileInfo> fileInfos = fileInfoDao.getFilesByCondition(menuId,fileStatus,null);
		for(SpFileInfo fileInfo:fileInfos){
			result.add(fileInfo);
		}
		// 查询当前目录下所有子目录
		List<SpFileMenu> fileMenus=fileMenuDao.getMenusByPid(menuId, null, rootId);
		if(fileMenus!=null&&fileMenus.size()>0){
			for(SpFileMenu fileMenu:fileMenus){
				//子节点下的文件
				List<SpFileInfo> children = getAllFiles(fileMenu.getMenuId(),fileStatus,rootId);
				for(SpFileInfo fileInfo:children){
					result.add(fileInfo);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileStatus 状态
	 * @param query 文件名或关键字,多个用空格分开
	 */
	public List<SpFileInfo> getFilesByCondition(Integer menuId,Integer fileStatus, String query) {
		return fileInfoDao.getFilesByCondition(menuId,fileStatus,query);
	}
	
	/**
	 * 保存文件信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveFileInfo(SpFileInfo fileInfo){
		if(fileInfo.getFileId()!=null){//修改
			fileInfoDao.update(fileInfo);
		}else{//添加
			fileInfoDao.insert(fileInfo);
		}
	}

	/**
	 * 删除
	 * @param ids
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteFileInfos(String ids) {
		fileInfoDao.deleteFileInfos(ids);
	}

	/**
	 * 保存排序
	 * @param datas
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveFileIndex(String datas) {
		String[] infos= datas.split(",");
		for(String infoStr:infos){
			String[] infoList= infoStr.split(":");
			fileInfoDao.updateFileIndex(infoList[0],infoList[1]);
		}
	}

}
