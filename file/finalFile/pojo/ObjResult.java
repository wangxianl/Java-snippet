package com.dm.fileManage.finalFile.pojo;

import java.io.Serializable;

import com.dm.common.ResResult;

public class ObjResult extends ResResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Object obj;
	
	public ObjResult() {
		super();
	}
	
	public ObjResult(boolean success,String msg) {
		super(success, msg);
	}
	
	public ObjResult(boolean success,String msg,Object obj) {
		super(success, msg);
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
	
}
