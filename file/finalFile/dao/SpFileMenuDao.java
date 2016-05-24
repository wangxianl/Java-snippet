package com.dm.fileManage.finalFile.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dm.dao.BasicDao;
import com.dm.fileManage.finalFile.entity.SpFileMenu;
/**
 * 文件目录 dao层
 * @author Administrator
 *
 */
@Repository
public class SpFileMenuDao extends BasicDao<SpFileMenu> {

	public SpFileMenuDao() {
		super(SpFileMenu.class);
	}
	
	/**
	 * 根据条件查询目录
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuDisplayName 目录显示名
	 * @param menuLabel 关键字,多个关键字以空格隔开
	 * @return 目录列表
	 */
	public List<SpFileMenu> getMenusByCondition(Integer rootId,Integer menuStatus,String menuDisplayName,String menuLabel){
		StringBuilder sb=new StringBuilder("from SpFileMenu where menuRootId=").append(rootId);
		//目录名
		if(menuDisplayName!=null&&menuDisplayName.trim().length()>0){
			sb.append(" and menuDisplayName like '%").append(menuDisplayName).append("%'");
		}
		//根据关键字
		if(menuLabel!=null&&menuLabel.trim().length()>0){
			//根据空格拆分,多个关键字查询
			String[] labels= menuLabel.split(" ");
			sb.append(" and ( 1=1");
			for(String label:labels){
				sb.append(" and menuLabel like '%").append(label).append("%'");
			}
			sb.append(")");
		}
		if(menuStatus!=null){
			sb.append(" and menuStatus=").append(menuStatus);
		}
		sb.append(" order by menuIndex");
		return this.queryForList(sb.toString());
	}
	
	/**
	 * 根据条件查询
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuStatus 状态
	 * @param query 显示名或标签,多个可用空格分隔
	 */
	public List<SpFileMenu> getMenus(Integer rootId, Integer menuStatus,String query) {
		StringBuilder sb=new StringBuilder("from SpFileMenu where menuRootId=").append(rootId);
		//状态
		if(menuStatus!=null){
			sb.append(" and menuStatus=").append(menuStatus);
		}
		//查询
		if(query!=null&&query.trim().length()>0){
			String[] labels= query.split(" ");
			for(String label:labels){
				sb.append("and (menuDisplayName like '%").append(label).append("%'").append(" or menuLabel like '%").append(label).append("%'").append(")");
			}
		}
		sb.append(" order by menuIndex");
		return this.queryForList(sb.toString());
	}
	
	/**
	 * 根据id查询
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuId id
	 */
	public List<SpFileMenu> getThisMenus(Integer menuId, Integer rootId ) {
		StringBuilder sb=new StringBuilder("from SpFileMenu where menuRootId=").append(rootId);
		sb.append(" and menuId=").append(menuId);
		return this.queryForList(sb.toString());
	}
	/**
	 * 根据父节点查询
	 * @param menuParentId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @return
	 */
	public List<SpFileMenu> getMenusByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		StringBuilder sb=new StringBuilder("from SpFileMenu where menuParentId=").append(menuParentId);
		//选择模块,只有parentId=0时需要
		if(rootId!=null){
			sb.append(" and menuRootId=").append(rootId);
		}
		if(menuStatus!=null){
			sb.append(" and menuStatus=").append(menuStatus);
		}
		sb.append(" order by menuIndex");
		return this.queryForList(sb.toString());
	}
	
	/**
	 * 删除目录
	 */
	public void deleteMenus(String ids){
		String hql="delete from SpFileMenu where menuId in ("+ids+")";
		this.executeUpdate(hql);
	}

}
