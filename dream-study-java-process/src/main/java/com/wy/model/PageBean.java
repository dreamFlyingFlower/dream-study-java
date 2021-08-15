package com.wy.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageBean<T> {

	/* 数据集 */
	private List<T> result;

	/* 总记录数 */
	private Long totalResult;

	/* 总页数 */
	private int totalPage;

	/* 当前页 */
	private int currentPage;

	/* 每页显示的记录数 */
	private int pageSize;

	/* 上一页 */
	private int previousPage;

	/* 下一页 */
	private int nextPage;

	/* 页码数量 */
	@Builder.Default
	private int barSum = 10;

	/* 页码条 */
	private int[] pageBar;

	public PageBean(List<T> result, Long totalResult) {
		this(result, totalResult, 0, 0);
	}

	public PageBean(List<T> result, Long totalResult, int currentPage, int pageSize) {
		this(result, totalResult, currentPage, pageSize, 10);
	}

	public PageBean(List<T> result, Long totalResult, int currentPage, int pageSize, int barSum) {
		super();
		this.setResult(result);
		this.setTotalResult(totalResult);
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
		this.setBarSum(barSum);
	}

	public int getTotalPage() {
		if (this.totalResult % this.pageSize == 0) {
			totalPage = (this.totalResult.intValue() / this.pageSize);
		} else {
			totalPage = (this.totalResult.intValue() / this.pageSize) + 1;
		}
		return totalPage;
	}

	public int getPreviousPage() {
		this.previousPage = this.currentPage - 1;
		if (previousPage < 1) {
			previousPage = 1;
		}
		return previousPage;
	}

	public int getNextPage() {
		this.nextPage = this.currentPage + 1;
		if (nextPage > this.totalPage) {
			nextPage = this.totalPage;
		}
		return nextPage;
	}

	public int[] getPageBar() {
		int startIndex = 0;
		int endIndex = 0;
		if (this.currentPage % barSum == 0) {
			startIndex = this.currentPage - (barSum - 1);
		} else {
			startIndex = (this.currentPage - (currentPage % barSum)) + 1;
		}
		// 20 11 20
		endIndex = startIndex + barSum - 1;
		if (endIndex > totalPage) {
			endIndex = totalPage;
		}
		this.pageBar = new int[endIndex - startIndex + 1];
		for (int i = startIndex; i <= endIndex; i++) {
			this.pageBar[i - startIndex] = i;
		}
		return pageBar;
	}
}