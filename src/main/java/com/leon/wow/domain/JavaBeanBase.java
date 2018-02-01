package com.leon.wow.domain;

/**
 * 所有javaBean的超类,包含分页信息
 * 
 * @author 孙旭
 * 
 */
public class JavaBeanBase {
	protected int pageSize = 10;// 每页显示的记录数
	protected int currentPage = 0;// 当前页
	protected long totalResult = 0;// 总记录数
	protected long totalPageNum = (totalResult + pageSize - 1) / pageSize;// 总页数
	protected int recordStart = currentPage * pageSize;// 当前记录起始索引
	protected int recordEnd = recordStart + pageSize;// 记录结束位置

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(long totalResult) {
		this.totalResult = totalResult;
	}

	public long getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(long totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getRecordStart() {
		return recordStart;
	}

	public void setRecordStart(int recordStart) {
		this.recordStart = recordStart;
	}

	public int getRecordEnd() {
		return recordEnd;
	}

	public void setRecordEnd(int recordEnd) {
		this.recordEnd = recordEnd;
	}

}
