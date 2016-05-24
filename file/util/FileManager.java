package com.dm.fileManage.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dm.common.Constant;
import com.dm.fileManage.filters.OnlyFileFilter;
import com.dm.fileManage.pojo.FilePojo;
import com.google.gson.Gson;

public class FileManager {
	
	/**
	 * 根据文件夹列出此文件夹下的文件(一级)
	 * @param file 目录
	 * @param filter 文件过滤器,可为空
	 * @return 
	 */
	public static List<FilePojo> listFilesByParent(File file,FileFilter filter){
		List<FilePojo> result=null;
		if(file.exists()&&file.isDirectory()){
			File[] files=file.listFiles(filter);
			if(files!=null&&files.length>0){
				result=new ArrayList<FilePojo>();
				for(File f:files){
					FilePojo filePojo=new FilePojo(f);
					result.add(filePojo);
				}
				Collections.sort(result);
			}
		}
		return result;
	}
	
	/**
	 * 根据文件夹列出此文件夹下所有的文件
	 * @param file 目录
	 * @param filter 文件过滤器,可为空
	 * @param isContainsNullMenu 是否需要包含空目录
	 * @return 
	 */
	public static List<FilePojo> listAllFilesByParent(File file,FileFilter filter,boolean isContainsNullMenu){
		List<FilePojo> result=null;
		if(file.exists()&&file.isDirectory()){
			File[] files=file.listFiles(filter);
			if(files!=null&&files.length>0){
				result=new ArrayList<FilePojo>();
				for(File f:files){
					if(f.isFile()){//文件
						FilePojo filePojo=new FilePojo(f);
						result.add(filePojo);
					}else{//目录
						List<FilePojo> subFiles= listAllFilesByParent(f,filter,isContainsNullMenu);
						if(subFiles!=null&&subFiles.size()>0){
							FilePojo filePojo=new FilePojo(f);
							filePojo.setChildren(subFiles);
							result.add(filePojo);
						}else{
							if(isContainsNullMenu){
								FilePojo filePojo=new FilePojo(f);
								result.add(filePojo);
							}
						}
					}
				}
				Collections.sort(result);
			}
		}
		return result;
	}
	
	/**
	 * 根据文件夹列出此文件夹下所有的文件夹
	 * @param file 目录
	 * @param isContainsNullMenu 是否需要包含空目录
	 * @return 
	 */
	public static List<FilePojo> listOnlyAllFoldersByParent(File file,boolean isContainsNullMenu){
		List<FilePojo> result=null;
		if(file.exists()&&file.isDirectory()){
			File[] files=file.listFiles();
			if(files!=null&&files.length>0){
				result=new ArrayList<FilePojo>();
				for(File f:files){
					if(f.isDirectory()){//目录
						List<FilePojo> subFilePojos= listOnlyAllFoldersByParent(f,isContainsNullMenu);
						if(subFilePojos!=null&&subFilePojos.size()>0){
							FilePojo filePojo=new FilePojo(f);
							filePojo.setChildren(subFilePojos);
							result.add(filePojo);
						}else{
							//过滤掉文件夹
							File[] subFiles=f.listFiles(new OnlyFileFilter());
							boolean hasFiles= subFiles!=null&&subFiles.length>0;
							if(isContainsNullMenu||hasFiles){
								FilePojo filePojo=new FilePojo(f);
								result.add(filePojo);
							}
						}
					}
				}
				Collections.sort(result);
			}
		}
		return result;
	}
	
	
	
	/**
	 * 删除文件
	 */
	public static void deleteFile(File file){
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			}else{
				File[] files=file.listFiles();
				if(files!=null&&files.length>0){
					for(File f:files){
						deleteFile(f);
					}
				}
				file.delete();
			}
		}
	}
	
	/**
	 * 格式化文件大小
	 */
	public static String formatSize(long size) {
		if(size < 1024){
			return String.format("%.2f", (double)size) + "B";
		}
		if (size < 1024 * 1024) {
			return String.format("%.2f", (double) (size / 1024)) + "KB";
		}
		if(size<1024*1024*1024){
			return String.format("%.2f", (double) (size / 1024 / 1024)) + "MB";
		}
		return String.format("%.2f", (double) (size / 1024 / 1024/1024)) + "GB";
	}
	/**
	 * 读取配置文件,返回List
	 * @param configPath 配置文件名
	 * @return
	 */
	public static List getConfigJsonObj(String configPath){
    	String path=null;
    	String tmpStr = null;
    	BufferedReader bf = null;
    	StringBuilder jsonStrBuilder = new StringBuilder();
    	try {
    		path = Constant.class.getResource("/").toURI().getPath()+configPath;
    		path = URLDecoder.decode(path, "UTF-8");
			File mapConfigFile=new File(path);
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(mapConfigFile), "utf-8"));
			Gson g = new Gson();
			while ((tmpStr = bf.readLine()) != null) {
				jsonStrBuilder.append(tmpStr);
			}
			List res = g.fromJson(jsonStrBuilder.toString(), List.class);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bf!=null){
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return null;
    }
	/**
	 * 读取配置文件,返回 字符串
	 * @param configPath
	 * @return
	 */
    public static String getConfigJsonStr(String configPath){
    	String path=null;
    	String tmpStr = null;
    	BufferedReader bf = null;
    	StringBuilder jsonStrBuilder = new StringBuilder();
    	try {
    		path = Constant.class.getResource("/").toURI().getPath()+configPath;
    		path = URLDecoder.decode(path, "UTF-8");
			File mapConfigFile=new File(path);
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(mapConfigFile), "utf-8"));
			while ((tmpStr = bf.readLine()) != null) {
				jsonStrBuilder.append(tmpStr);
			}
			return jsonStrBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bf!=null){
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return "[]";
    }

}
