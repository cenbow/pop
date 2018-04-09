package com.ailk.bdx.pop.adapter.common.model;

public class BaseModel {
	private Integer page=1; // 含期望的页码
	private Integer rows=10; // 每页的记录数
	private String sort; // 排序的字段
	private String order; // 排序的顺序
	private Integer totalRows; //结果集总记录数
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
    
    public Integer getTotalRows() {
        return totalRows;
    }
    
    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }
}
