package com.dm.fileManage.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤器,过滤掉所有文件夹
 */
public class OnlyFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.isFile();
	}

}
