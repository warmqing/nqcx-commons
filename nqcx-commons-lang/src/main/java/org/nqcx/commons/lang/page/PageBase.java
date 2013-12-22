/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.page;

import org.nqcx.commons.lang.domain.EntityBase;

/**
 * 
 * @author nqcx Apr 7, 2013
 * 
 */
public class PageBase extends EntityBase implements PageBuilder {

	private static final long serialVersionUID = -7792407794436647838L;

	// 记录总数
	private long totalCount = 0L;
	// 当前页
	private long page = 1L;
	// 每页显示记录数（默认值20）
	private long pageSize = 20L;

	public PageBase() {

	}

	public PageBase(long _totalCount) {
		init(_totalCount, 0, 0);
	}

	public PageBase(long _totalCount, long _page) {
		init(_totalCount, _page, 0);
	}

	public PageBase(long _totalCount, long _page, long _pageSize) {
		init(_totalCount, _page, _pageSize);
	}

	/**
	 * check page range
	 */
	private void init(long _totalCount, long _page, long _pageSize) {
		if (_totalCount >= 0)
			totalCount = _totalCount;
		if (_page > 0)
			this.page = _page;
		if (_pageSize > 0)
			pageSize = _pageSize;

		long totalPage = this.getTotalPage();
		// 当显示页为0时显示第一页
		if (this.page <= 0)
			this.page = 1;
		// 当显示页的值大于总页数并且总页数>0时显示最后一页
		else if (this.page > totalPage && totalPage > 0)
			this.page = totalPage;
	}

	@Override
	public void setTotalCount(long totalCount) {
		init(totalCount, 0, 0);
	}

	@Override
	public void setPage(long page) {
		init(-1, page, 0);
	}

	@Override
	public void setPageSize(long pageSize) {
		init(-1, 0, pageSize);
	}

	@Override
	public long getTotalCount() {
		return totalCount;
	}

	@Override
	public long getTotalPage() {
		return (totalCount + this.getPageSize() - 1) / this.getPageSize();
	}

	@Override
	public long getPage() {
		return page;
	}

	@Override
	public long getPageSize() {
		return pageSize;
	}

	@Override
	public long getStartIndex() {
		return (this.getPage() - 1) * this.getPageSize();
	}

	@Override
	public long getEndIndex() {
		return this.getPage() * this.getPageSize();
	}

	@Override
	public String getPosition() {
		return "limit " + this.getStartIndex() + "," + this.getPageSize();
	}

	public static void main(String[] args) {
		PageBuilder pb = new PageBase();
		pb.setTotalCount(188);
		pb.setPage(10);
		pb.setPageSize(55);

		System.out.println(pb.getTotalPage());
		System.out.println(pb.getPageSize());
		System.out.println(pb.getPage());
		System.out.println(pb.getPosition());

	}

}
