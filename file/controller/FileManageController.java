package com.dm.fileManage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dm.common.ResResult;
import com.dm.fileManage.filters.OnlyFileFilter;
import com.dm.fileManage.filters.OnlyFolderFilter;
import com.dm.fileManage.pojo.FilePojo;
import com.dm.fileManage.util.FileManager;
import com.dm.util.ExportUtils;

@Controller
@RequestMapping("/public/file")
public class FileManageController {
	
	/**
	 * 进入文件管理界面
	 */
	@RequestMapping("/fileManageJsp")
	public String censusConfigJsp(){
		return "fileManage/fileManage";
	}
	
	/**
	 * 列出目录下的文件和目录
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param isContainsNullMenu 是否包含空目录;不传参数时只列出一级,传参数时显示所有
	 * @param path 文件路径,可为相对于webroot的路径,或文件绝对路径
	 * @return
	 */
	@RequestMapping("/listFilesByParent")
	@ResponseBody
	public List<FilePojo> listFilesByParent(HttpServletRequest request,Boolean isAbsolute,Boolean isContainsNullMenu){
		//文件路径
		String path= request.getParameter("path");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+ path;
		}
		File folder=new File(path);
		//不传参数,只显示一级
		if(isContainsNullMenu==null){
			return FileManager.listFilesByParent(folder,null);
		}
		//传参数,显示所有
		return FileManager.listAllFilesByParent(folder,null,isContainsNullMenu);
	}
	/**
	 * 只获取文件类型类型,一层
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param path 文件路径,可为相对于webroot的路径,或文件绝对路径
	 */
	@RequestMapping("/listOnlyFilesByParent")
	@ResponseBody
	public List<FilePojo> listOnlyFilesByParent(HttpServletRequest request,Boolean isAbsolute){
		//文件路径
		String path= request.getParameter("path");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+ path;
		}
		File folder=new File(path);
		FileFilter filter=new OnlyFileFilter();
		return FileManager.listFilesByParent(folder, filter);
	}
	
	/**
	 * 只获取所有文件夹
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param path 文件路径,可为相对于webroot的路径,或文件绝对路径
	 */
	@RequestMapping("/listOnlyAllFoldersByParent")
	@ResponseBody
	public List<FilePojo> listOnlyAllFoldersByParent(HttpServletRequest request,Boolean isAbsolute){
		//文件路径
		String path= request.getParameter("path");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+ path;
		}
		File folder=new File(path);
		return FileManager.listOnlyAllFoldersByParent(folder, true);
	}
	
	/**
	 * 获取配置文件,以List形式返回
	 * @param configPath 配置文件路径
	 * @return
	 */
	@RequestMapping("/getConfigJson")
	@ResponseBody
	public List getConfigJson(String configPath){
		return FileManager.getConfigJsonObj(configPath);
	}
	
	/**
	 * 添加下级目录
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param parentPath 文件路径,可为相对于webroot的路径,或文件绝对路径
	 * @param fileName 要添加的目录名
	 * @return
	 */
	@RequestMapping("/addFolder")
	@ResponseBody
	public ResResult addFolder(HttpServletRequest request,Boolean isAbsolute){
		//文件路径
		String path= request.getParameter("path");
		String fileName= request.getParameter("fileName");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+ path;
		}
		File folder=new File(path,fileName);
		ResResult result=new ResResult(false,"添加失败");
		try {
			if(!folder.exists()){
				folder.mkdirs();
			}
			result.setMsg("添加成功");
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 删除文件(若是目录,也包括其下的文件)
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param path 文件路径,可为相对于webroot的路径,或文件绝对路径
	 * @return
	 */
	@RequestMapping("/deleteFiles")
	@ResponseBody
	public ResResult deleteFiles(HttpServletRequest request,Boolean isAbsolute){
		//文件路径
		String paths= request.getParameter("paths");
		String preStr="";
		if(!(isAbsolute!=null&&isAbsolute)){
			preStr= request.getSession().getServletContext().getRealPath("/");
		}
		ResResult result=new ResResult(false,"添加失败");
		try {
			String[] pathStrs= paths.split(";");
			for(String path:pathStrs){
				File file=new File(preStr+path);
				FileManager.deleteFile(file);
			}
			result.setMsg("添加成功");
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 文件上传,未加任何过滤
	 * @param path 要上传到的目录的路径
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param fileName 上传后文件的名称,为空时使用默认文件名  fileName不包含后缀
	 */
	@RequestMapping("/uploadFile")
	@ResponseBody
	public ResResult uploadFile(@RequestParam(value = "file", required = false)
	MultipartFile file,Boolean isAbsolute, HttpServletRequest request) {
		ResResult result=new ResResult(false,"上传失败");
		//上传路径
		String path= request.getParameter("path");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+path;
		}
		String fileName= request.getParameter("fileName");
		String name=file.getOriginalFilename();
		try {
			if(fileName!=null&&fileName.length()>0){
				int index=name.lastIndexOf(".");
				String adding=index!=-1?name.substring(index):"";
				name=fileName+adding;
			}
			file.transferTo(new File(path,name));
			result.setSuccess(true);
			result.setMsg("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

	/**
	 * 文件下载
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param path 文件路径,可为相对于webroot的路径,或文件绝对路径
	 * @param name 文件下载时的显示名
	 */
	@RequestMapping(value = "/downloadFile")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response,Boolean isAbsolute) throws Exception {
		String path= request.getParameter("path");
		if(!(isAbsolute!=null&&isAbsolute)){
			path= request.getSession().getServletContext().getRealPath("/")+ path;
		}
		System.out.println("下载路径:"+path);
		//在浏览器端显示的下载名,所以要编码为iso-8859-1
		String downName=new String(request.getParameter("name").getBytes("utf-8"),"iso-8859-1");
		long fileLength = new File(path).length();
		response.setHeader("Content-disposition", "attachment; filename="
				+downName);
		response.setHeader("Content-Length", String.valueOf(fileLength));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[1024 * 1024];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.flush();
		bos.close();
	}
	
	/**
	 * 打包下载压缩文件
	 * @param isAbsolute 是否为文件的绝对路径,为空时默认为非绝对路径
	 * @param paths 要下载的路径，以分号隔开
	 */
	@RequestMapping(value = "/downloadArchivedFile")
	public void downloadArchivedFile(HttpServletRequest request,HttpServletResponse response,Boolean isAbsolute){
		//下载路径
		String paths=request.getParameter("paths");
		String basePath= request.getSession().getServletContext().getRealPath("/");
		//下载名
		String name=request.getParameter("name");
		//打包插件路径
		String exportPath = basePath+"download";
		//获取下载的路径的List
		List<String> pathList=new ArrayList<String>();
		String preStr="";
		if(!(isAbsolute!=null&&isAbsolute)){
			preStr=basePath;
		}
		String[] pathStrs= paths.split(";");
		for(String path:pathStrs){
			pathList.add(preStr+path);
		}
		//打包下载
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String outFileName;
		if(name!=null&&name.trim().length()>0){//传名字则用文件名
			outFileName= name;
		}else{//不传则用随机生成的
			outFileName=sdf.format(new Date())+"_size"+pathList.size();
		}
		ExportUtils.export(response, pathList, "multi", exportPath,outFileName);
	}
	
}
