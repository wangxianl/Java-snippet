package com.dm.fileManage.finalFile.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目录展示类型
 * @author Administrator
 */
public class MenuType {
	/**下拉展示*/
	public static final int TYPE_SHOW_SELECT=1;
	/**树形展示*/
	public static final int TYPE_SHOW_TREE=2;
	/**表格展示*/
	public static final int TYPE_SHOW_GRID=3;
	/**列表下载*/
	public static final int TYPE_DOWNLOAD_LIST=4;
	/**年鉴*/
	public static final int TYPE_SHOW_YEARBOOK=5;
	
	
	/** 获取目录类型列表 */
	public static List<Map<String, Object>> getMenuTypeList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> menuTypeStr = new HashMap<String, Object>();
		menuTypeStr.put("text", "下拉展示");
		menuTypeStr.put("value", TYPE_SHOW_SELECT);
		result.add(menuTypeStr);

		menuTypeStr=new HashMap<String, Object>();
		menuTypeStr.put("text", "年鉴展示");
		menuTypeStr.put("value", TYPE_SHOW_YEARBOOK);
		result.add(menuTypeStr);
		
		menuTypeStr=new HashMap<String, Object>();
		menuTypeStr.put("text", "树形展示");
		menuTypeStr.put("value", TYPE_SHOW_TREE);
		result.add(menuTypeStr);
		
		menuTypeStr=new HashMap<String, Object>();
		menuTypeStr.put("text", "表格展示");
		menuTypeStr.put("value", TYPE_SHOW_GRID);
		result.add(menuTypeStr);
		
		menuTypeStr=new HashMap<String, Object>();
		menuTypeStr.put("text", "列表下载");
		menuTypeStr.put("value", TYPE_DOWNLOAD_LIST);
		result.add(menuTypeStr);
		
		return result;
	}
}
