package com.dm.fileManage.finalFile.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dm.common.ResResult;
import com.dm.fileManage.finalFile.dao.SpFileRootDao;
import com.dm.fileManage.finalFile.entity.SpFileInfo;
import com.dm.fileManage.finalFile.entity.SpFileRoot;
import com.dm.fileManage.finalFile.service.SpFileInfoService;
import com.dm.fileManage.util.FileManager;

@Controller
@RequestMapping("/public/finalFile")
public class SpFileInfoController {
	@Autowired
	private SpFileInfoService fileInfoService;
	@Autowired
	private SpFileRootDao fileRootDao;
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileDisplayName 文件名
	 * @param fileLabel 关键字,多个关键字以空格隔开
	 * @return 
	 */
	@RequestMapping("/getFiles")
	@ResponseBody
	public List<SpFileInfo> getFiles(Integer menuId,Integer fileStatus,String fileDisplayName,String fileLabel){
		return fileInfoService.getFiles(menuId, fileStatus, fileDisplayName, fileLabel);
	}
	
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileStatus 状态
	 * @param query 文件名或关键字,多个用空格分开
	 */
	@RequestMapping("/getFilesByCondition")
	@ResponseBody
	public List<SpFileInfo> getFilesByCondition(Integer menuId,Integer fileStatus,String query){
		return fileInfoService.getFilesByCondition(menuId,fileStatus,query);
	}
	
	/**
	 * 根据目录查询
	 * @param menuId 目录id
	 * @param fileStatus 状态
	 * @param query 文件名或关键字,多个用空格分开
	 */
	@RequestMapping("/getAllFiles")
	@ResponseBody
	public Map<String, Object> getAllFiles(Integer menuId,Integer fileStatus,Integer rootId){
		
		Map<String, Object> result = new HashMap<String, Object>();
		//根节点
		SpFileRoot fileRoot=fileRootDao.queryById(rootId);
		String rootPath = fileRoot.getRootPath();
		Integer rootABs = fileRoot.getRootAbs();
		List<SpFileInfo> filelist = fileInfoService.getAllFiles(menuId,fileStatus,rootId);
		result.put("filelist", filelist);
		result.put("rootPath", rootPath);
		result.put("rootABs", rootABs == 1);
		
		
		return result;
	}
	
	/**
	 * 文件上传,未加任何过滤
	 * @param path 要上传到的目录的路径
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param fileName 上传后文件的名称,为空时使用默认文件名  fileName不包含后缀
	 */
	@RequestMapping("/uploadFileInfo")
	@ResponseBody
	public ResResult uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,SpFileInfo info, HttpServletRequest request) {
		ResResult result=new ResResult(false,"上传失败");
		//上传路径
		String path= info.getRootPath()+info.getFilePath();
		if(!(info.isRootAbs())){
			path= request.getSession().getServletContext().getRealPath("/")+path;
		}
		try {
			String fileName= info.getFileName();
			String displayName= info.getFileDisplayName();
			//后缀
			int extraIndex= fileName.lastIndexOf(".");
			String extra= "";
			if(extraIndex!=-1){
				extra= fileName.substring(extraIndex+1);
				//显示名
				if(!(displayName!=null&&displayName.trim().length()>0)){
					info.setFileDisplayName(fileName.substring(0, extraIndex));
				}
			}else{
				//显示名
				if(!(displayName!=null&&displayName.trim().length()>0)){
					info.setFileDisplayName(fileName);
				}
			}
			//设置后缀
			info.setFileExtra(extra);
			//大小
			info.setFileSize(FileManager.formatSize(file.getSize()));
			//存到数据库
			fileInfoService.saveFileInfo(info);
			info.setFileExtra("");
			//存到本地
			File newFile=new File(path);
			File folder=newFile.getParentFile();
			if(!folder.exists()){
				folder.mkdirs();
			}
			file.transferTo(newFile);
			//返回结果设置
			result.setSuccess(true);
			result.setMsg("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 添加文件
	 */
	@RequestMapping("/saveFileHTMLInfo")
	@ResponseBody
	public ResResult saveFileHTMLInfo(SpFileInfo info,String html, HttpServletRequest request) {
		ResResult result=new ResResult(false,"上传失败");
		//上传路径
		String path= info.getRootPath()+info.getFilePath();
		if(!(info.isRootAbs())){
			path= request.getSession().getServletContext().getRealPath("/")+path;
		}
		try {
			//文件
			File file=new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			//写入文件
			PrintWriter out=new PrintWriter(file,"utf-8");
			out.println(html);
			out.close();
			//大小
			info.setFileSize(FileManager.formatSize(file.length()));
			//存到数据库
			fileInfoService.saveFileInfo(info);
			info.setFileExtra("");
			//返回结果设置
			result.setSuccess(true);
			result.setMsg("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/updateFileInfo")
	@ResponseBody
	public ResResult updateFileInfo(SpFileInfo fileInfo){
		ResResult result=new ResResult(false,"保存失败");
		try {
			fileInfoService.saveFileInfo(fileInfo);
			result.setSuccess(true);
			result.setMsg("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 保存排序
	 * @param datas
	 * @return
	 */
	@RequestMapping("/saveFileIndex")
	@ResponseBody
	public ResResult saveFileIndex(String datas){
		ResResult result=new ResResult(false,"保存失败");
		try {
			fileInfoService.saveFileIndex(datas);
			result.setSuccess(true);
			result.setMsg("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/deleteFileInfos")
	@ResponseBody
	public ResResult deleteFileInfos(String ids,String paths,String rootPath,boolean rootAbs,HttpServletRequest request){
		ResResult result=new ResResult(false,"删除失败");
		try {
			//删除数据库
			fileInfoService.deleteFileInfos(ids);
			//删除本地
			String adding=rootPath;
			if(!rootAbs){
				adding= request.getSession().getServletContext().getRealPath("/")+adding;
			}
			String[] pathArr= paths.split(",");
			for(String path:pathArr){
				File file=new File(adding+path);
				if(file.exists()){
					file.delete();
				}
			}
			result.setSuccess(true);
			result.setMsg("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
