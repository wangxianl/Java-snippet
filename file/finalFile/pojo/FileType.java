package com.dm.fileManage.finalFile.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件展示类型
 * 
 * @author Administrator
 */
public class FileType {
	/** 根据文件类型自动判断 */
	public static final int TYPE_AUTO = 1;
	/** pageoffice展现 */
	public static final int TYPE_PAGEOFFICE = 2;
	/** html展现 */
	public static final int TYPE_HTML = 3;
	/**图片展现*/
	public static final int TYPE_PIC = 4;

	/** 获取文件类型列表 */
	public static List<Map<String, Object>> getFileTypeList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		Map<String, Object> fileTypeStr = new HashMap<String, Object>();
		fileTypeStr.put("text", "根据文件类型自动判断");
		fileTypeStr.put("value", TYPE_AUTO);
		result.add(fileTypeStr);

		fileTypeStr = new HashMap<String, Object>();
		fileTypeStr.put("text", "pageoffice展现");
		fileTypeStr.put("value", TYPE_PAGEOFFICE);
		result.add(fileTypeStr);

		fileTypeStr = new HashMap<String, Object>();
		fileTypeStr.put("text", "html展现");
		fileTypeStr.put("value", TYPE_HTML);
		result.add(fileTypeStr);
		
		fileTypeStr = new HashMap<String, Object>();
		fileTypeStr.put("text", "图片展现");
		fileTypeStr.put("value", TYPE_PIC);
		result.add(fileTypeStr);

		return result;
	}
}
