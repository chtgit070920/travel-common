package com.travel.common.mybatis;

import java.util.List;

public abstract class Page<T> {
	
	private final static String DEFAULT_ATTRIBUTE_NAME="page"; 
	
	//for mybatis：该字段用于从“查询方法的参数”中取出“分页信息”
	//当page作为po字段时的字段名，或作为map时的key值，
	//已方便mybatis取出page对象进行解析。
	private static String attributeName=DEFAULT_ATTRIBUTE_NAME;
	
	private int pageNo ; // 当前页码
	private int pageSize ;//每页记录数
	private long total;// 总记录数
	private List<T> rows;//数据集
	
	//for mybatis：该字段用于从“查询方法的参数”中取出“分页信息”
	public static String getAttributeName() {
		return attributeName;
	}
	//for mybatis：该字段用于从“查询方法的参数”中取出“分页信息”
	public static void setAttributeName(String attributeName) {
		Page.attributeName = attributeName;
	}


	//for mybatis 分页查询：第一个条数据
	//子类可扩展
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getTotal()) {
			firstResult = 0;
		}
		return firstResult;
	}
		
	//for mybatis 分页查询：取出条数
	//子类可扩展
	public int getMaxResults(){
		return getPageSize();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	

	
}
