package com.dm.fileManage.finalFile.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dm.dao.BasicDao;
import com.dm.fileManage.finalFile.entity.SpFileInfo;

@Repository
public class SpFileInfoDao extends BasicDao<SpFileInfo> {

	public SpFileInfoDao() {
		super(SpFileInfo.class);
	}
	
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileDisplayName 文件名
	 * @param fileLabel 关键字,多个关键字以空格隔开
	 */
	public List<SpFileInfo> getFiles(Integer menuId,Integer fileStatus,String fileDisplayName,String fileLabel){
		StringBuilder sb=new StringBuilder("from SpFileInfo where menuId=").append(menuId);
		//目录名
		if(fileDisplayName!=null&&fileDisplayName.trim().length()>0){
			sb.append(" and fileDisplayName like '%").append(fileDisplayName).append("%'");
		}
		//状态
		if(fileStatus!=null){
			sb.append(" and fileStatus=").append(fileStatus);
		}
		//根据关键字
		if(fileLabel!=null&&fileLabel.trim().length()>0){
			//根据空格拆分,多个关键字查询
			String[] labels= fileLabel.split(" ");
			sb.append(" and ( 1=1");
			for(String label:labels){
				sb.append(" and fileLabel like '%").append(label).append("%'");
			}
			sb.append(")");
		}
		sb.append(" order by fileIndex");
		return this.queryForList(sb.toString());
	}
	
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileStatus 状态
	 * @param query 文件名或关键字,多个用空格分开
	 */
	public List<SpFileInfo> getFilesByCondition(Integer menuId,Integer fileStatus, String query) {
		StringBuilder sb=new StringBuilder("from SpFileInfo where menuId=").append(menuId);
		//状态
		if(fileStatus!=null){
			sb.append(" and fileStatus=").append(fileStatus);
		}
		if(query!=null&&query.trim().length()>0){
			String[] labels= query.split(" ");
			for(String label:labels){
				sb.append("and (fileDisplayName like '%").append(label).append("%'").append(" or fileLabel like '%").append(label).append("%'").append(")");
			}
		}
		sb.append(" order by fileIndex");
		return this.queryForList(sb.toString());
	}

	/**
	 * 删除
	 * @param ids
	 */
	public void deleteFileInfos(String ids) {
		String hql="delete from SpFileInfo where fileId in ("+ids+")";
		this.executeUpdate(hql);
	}

	/**
	 * 保存排序
	 * @param fileId
	 * @param fileIndex
	 */
	public void updateFileIndex(String fileId, String fileIndex) {
		String hql="update SpFileInfo set fileIndex="+fileIndex+" where fileId="+fileId;
		this.executeUpdate(hql);
	}

}
