package com.dm.fileManage.finalFile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dm.fileManage.finalFile.dao.SpFileRootDao;
import com.dm.fileManage.finalFile.entity.SpFileRoot;

@Service
public class SpFileRootService {
	@Autowired
	private SpFileRootDao fileRootDao;

	/**
	 * 查询全部
	 * 
	 * @return
	 */
	public List<SpFileRoot> getAll() {
		return fileRootDao.queryAll();
	}
	
	/**
	 * 根据主键查询
	 */
	public SpFileRoot getById(Integer rootId){
		return fileRootDao.queryById(rootId);
	}
}
