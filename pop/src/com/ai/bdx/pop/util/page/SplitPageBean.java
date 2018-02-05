package com.ai.bdx.pop.util.page;

public class SplitPageBean {



	private int pageIndex = 1; // 当前页 
	private int pageSize = 5; // 每页记录个数 
	private int totalCount = 0; // 总记录数
    private int totalPageCount;//总页数
	public SplitPageBean(){
		
	}
	/**
	 * 取分页记录
	 * 
	 * @param pageIndex 当前页面序号(以1开始)
	 * @param pageSize 每页记录数
	 */
	public SplitPageBean(int pageIndex, int pageSize) {
		if (pageIndex < 1) {
			this.pageIndex = 1;
		} else {
			this.pageIndex = pageIndex;
		}

		if (pageSize < 1) {
			this.pageSize = 10;
		} else {
			this.pageSize = pageSize;
		}
	}

	public SplitPageBean(int pageIndex, int pageSize, int totalCount) {
		this(pageIndex, pageSize);
		this.totalCount = totalCount;
	}

	/**
	 * 初始化bean
	 * @param pageIndex 当前页索引
	 */
	public SplitPageBean(int pageIndex) {
		if (pageIndex < 1) {
			this.pageIndex = 1;
		} else {
			this.pageIndex = pageIndex;
		}
	}

	/**
	 * 获取起始记录索引；后台使用(first record)
	 * 
	 * @return
	 */
	public int getFirstRecord() {
		return (pageIndex - 1) * pageSize;
	}

	/**
	 * 获取记录数量；后台使用 (max record)
	 * 
	 * @return
	 */
	public int getMaxRecord() {
		//		if(totalCount == 0){
		//			throw new RuntimeException("请在获取最大记录数前，首先为 totalCount属性赋值.");
		//		}
		if (getFirstRecord() > totalCount) {
			// 由程序处理
			return -1;
		} else {
			if ((getFirstRecord() + pageSize) > totalCount) {
				return totalCount - getFirstRecord() + 1;
			} else {
				return pageSize;
			}
		}
	}

	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * 设置每页记录数
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 得到总记录数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数
	 * @param totalRecordCount
	 */
	public void setTotalCount(int totalRecordCount) {
		this.totalCount = totalRecordCount;
	}

	/**
	 * 得到总页面数
	 * 
	 * @return
	 */
	public int getTotalPageCount() {
		totalPageCount = totalCount / pageSize;
		if (totalCount % pageSize == 0) {
			return totalPageCount;
		} else {
			return totalPageCount + 1;
		}
	}

	/**
	 * 设置当前页索引
	 * @param currentPage
	 */
	public void setPageIndex(int currentPage) {
		this.pageIndex = currentPage;
	}
		


}
