package com.dm.fileManage.finalFile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dm.fileManage.finalFile.entity.SpFileMenu;
import com.dm.fileManage.finalFile.entity.SpFileRoot;
import com.dm.fileManage.finalFile.pojo.MenuType;
import com.dm.fileManage.finalFile.service.SpFileMenuService;
import com.dm.fileManage.finalFile.service.SpFileRootService;
import com.google.gson.Gson;

/**
 * 文件管理界面
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/public/finalFile")
public class FinalFileHandleController {
	@Autowired
	private SpFileMenuService fileMenuService;
	@Autowired
	private SpFileRootService fileRootService;
	/**
	 * 根据父节点查询下级
	 * @param menuId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @param modelId 展示类型
	 */
	@RequestMapping("/loadModel")
	public ModelAndView getMenusByPid(Integer menuId,Integer modelId,Integer rootId,HttpServletRequest request){
		ModelAndView mv = null;
		SpFileRoot fileRoot=fileRootService.getById(rootId);
		switch(modelId){
		case MenuType.TYPE_SHOW_SELECT://下拉展示
			mv = new ModelAndView("/census/frontpage/modelPage/normal");
			mv.addObject("menuDisplayName",fileMenuService.getThisMenus(menuId, rootId).get(0).getMenuDisplayName());
			mv.addObject("root", fileRoot);
			break;
		case MenuType.TYPE_SHOW_GRID://表格展示
			mv = new ModelAndView("/census/frontpage/modelPage/grid");
			break;	
		case MenuType.TYPE_DOWNLOAD_LIST://列表下载
			mv = new ModelAndView("/census/frontpage/modelPage/downloadlist");
			break;		
		case MenuType.TYPE_SHOW_YEARBOOK://年鉴展示
			mv = new ModelAndView("/census/frontpage/modelPage/show_yearbook");
			//其下的文件夹
			List<SpFileMenu> menus= fileMenuService.getMenusByPid(menuId, 0, rootId);
			mv.addObject("menus", menus);
			mv.addObject("menuObjs", new Gson().toJson(menus));
			//每页个数
//			int lineSize= 8;
//			int pageSize= lineSize*3;
//			mv.addObject("pageSize",pageSize);
//			mv.addObject("lineSize",lineSize);
//			if(menus!=null&&menus.size()>pageSize){//需要分页
//				mv.addObject("size", menus.size());
//				mv.addObject("menuObjs", new Gson().toJson(menus));
//			}else{//不需要分页时设为0
//				mv.addObject("size", 0);
//			}
			mv.addObject("root", fileRoot);
			break;
		case MenuType.TYPE_SHOW_TREE://树形展示
			mv = new ModelAndView("/census/frontpage/modelPage/show_tree");
			mv.addObject("root", fileRoot);
			break;
		}
		mv.addObject("menuId",menuId);
		mv.addObject("rootId", rootId);
		return mv;
	}
}
