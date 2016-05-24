package com.dm.fileManage.finalFile.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dm.common.ResResult;
import com.dm.fileManage.finalFile.entity.SpFileMenu;
import com.dm.fileManage.finalFile.entity.SpFileRoot;
import com.dm.fileManage.finalFile.pojo.FileType;
import com.dm.fileManage.finalFile.pojo.MenuType;
import com.dm.fileManage.finalFile.pojo.ObjResult;
import com.dm.fileManage.finalFile.service.PackageMenuListToTreeService;
import com.dm.fileManage.finalFile.service.SpFileMenuService;
import com.dm.fileManage.finalFile.service.SpFileRootService;
import com.dm.fileManage.util.FileManager;
import com.google.gson.Gson;

/**
 * 文件管理界面
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/public/finalFile")
public class SpFileConfigController {
	@Autowired
	private SpFileMenuService fileMenuService;
	@Autowired
	private SpFileRootService fileRootService;
	@Autowired
	private PackageMenuListToTreeService packageMenuListToTreeService;
	/**
	 * 进入文件管理的jsp页面
	 * @return 
	 */
	@RequestMapping("/fileConfigJsp")
	public ModelAndView fileConfigJsp(){
		List<SpFileRoot> fileRoots= fileRootService.getAll();
		ModelAndView mv=new ModelAndView("fileManage/finalFile/backpage/fileConfig");
		Gson gson=new Gson();
		mv.addObject("fileRoots", gson.toJson(fileRoots));
		mv.addObject("menuTypes", gson.toJson(MenuType.getMenuTypeList()));
		mv.addObject("fileTypes", gson.toJson(FileType.getFileTypeList()));
		return mv;
	}
	
	/**
	 * 根据父节点查询下级
	 * @param menuParentId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @param menuStatus 状态,1:可见,0:不可见,null则为全部
	 * @return
	 */
	@RequestMapping("/getMenusByPid")
	@ResponseBody
	public List<SpFileMenu> getMenusByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		return fileMenuService.getMenusByPid(menuParentId, menuStatus, rootId);
	}
	
	/**
	 * 根据条件查询目录
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuDisplayName 目录显示名
	 * @param menuLabel 关键字,多个关键字以空格隔开
	 * @return
	 */
	@RequestMapping("/getMenusByCondition")
	@ResponseBody
	public List<Map<String, Object>> getMenusByCondition(Integer rootId,Integer menuStatus,String menuDisplayName,String menuLabel){
		List<SpFileMenu> fileMenus= fileMenuService.getMenusByCondition(rootId, menuStatus, menuDisplayName, menuLabel);
		boolean isNeedColor= menuDisplayName!=null&&menuDisplayName.length()>0;
		return packageMenuListToTreeService.packageDepListToTree(fileMenus, isNeedColor);
	}
	
	/**
	 * 根据条件查询
	 * @param rootId 属于哪个根节点,也就是哪个模块
	 * @param menuStatus 状态
	 * @param query 显示名或标签,多个可用空格分隔
	 */
	@RequestMapping("/getMenus")
	@ResponseBody
	public List<Map<String, Object>> getMenus(Integer rootId,Integer menuStatus,String query){
		List<SpFileMenu> fileMenus= fileMenuService.getMenus(rootId, menuStatus,query);
		boolean isNeedColor= query!=null&&query.length()>0;
		return packageMenuListToTreeService.packageDepListToTree(fileMenus, isNeedColor);
	}
	
	/**
	 * 根据父节点查询所有内容
	 * @param menuParentId 父节点
	 * @param rootId 根节点,也就是对应的模块
	 * @param menuStatus 状态,1:可见,0:不可见,不传则为全部
	 */
	@RequestMapping("/getAllMenusByPid")
	@ResponseBody
	public List<SpFileMenu> getAllMenusByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		return fileMenuService.getAllMenusByPid(menuParentId, menuStatus, rootId);
	}
	
	/**
	 * 根据父节点查询所有内容,包括文件
	 */
	@RequestMapping("/getAllByPid")
	@ResponseBody
	public List<Map<String, Object>> getAllByPid(Integer menuParentId,Integer menuStatus,Integer rootId){
		return fileMenuService.getAllByPid(menuParentId, menuStatus, rootId);
	}
	
	/**
	 * 保存目录
	 */
	@RequestMapping("/saveMenu")
	@ResponseBody
	public ResResult saveMenu(SpFileMenu menu,HttpServletRequest request){
		ObjResult result=new ObjResult(false,"保存失败");
		try {
			//存到数据库
			fileMenuService.saveMenu(menu);
			//存到本地
			String path= menu.getRootPath()+"/"+menu.getMenuPath();
			if(!menu.isRootAbs()){//不是绝对路径时添加根路径
				path= request.getSession().getServletContext().getRealPath("/")+ path;
			}
			File file=new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			result.setSuccess(true);
			result.setMsg("保存成功");
			result.setObj(menu.getMenuId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 将数据库的目录同步到本地
	 * @return 
	 */
	@RequestMapping("/synchronizeFolders")
	@ResponseBody
	public ResResult synchronizeFolders(String paths,String rootPath,boolean rootAbs,HttpServletRequest request){
		ResResult result=new ResResult(false,"同步失败");
		try {
			String[] pathList= paths.split(",");
			String adding;
			if(!rootAbs){
				adding= request.getSession().getServletContext().getRealPath("/")+ rootPath;
			}else{
				adding= rootPath;
			}
			for(String path:pathList){
				//删除本地
				File file=new File(adding+path);
				if(!file.exists()){
					file.mkdirs();
				}
				result.setSuccess(true);
			}
			result.setMsg("同步成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除目录
	 */
	@RequestMapping("/deleteMenus")
	@ResponseBody
	public ResResult deleteMenus(String ids,String path,boolean rootAbs,HttpServletRequest request){
		ResResult result=new ResResult(false,"删除失败");
		try {
			//删除数据库
			fileMenuService.deleteMenus(ids);
			//删除本地
			if(!rootAbs){
				path= request.getSession().getServletContext().getRealPath("/")+ path;
			}
			File file=new File(path);
			FileManager.deleteFile(file);
			result.setSuccess(true);
			result.setMsg("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
