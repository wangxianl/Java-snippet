package com.dm.fileManage.finalFile.dao;

import org.springframework.stereotype.Repository;

import com.dm.dao.BasicDao;
import com.dm.fileManage.finalFile.entity.SpFileRoot;

@Repository
public class SpFileRootDao extends BasicDao<SpFileRoot> {

	public SpFileRootDao() {
		super(SpFileRoot.class);
	}

}
