/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.page;


import org.nqcx.commons.lang.o.EntityBO;

/**
 * Page interface object
 *
 * @author naqichuan 2014年8月14日 上午11:03:13
 */
public class PageBO extends EntityBO implements PageIO {

    // 记录总数
    private long totalCount = 0L;
    // 当前页
    private long page = 1L;
    // 每页显示记录数（默认值20）
    private long pageSize = 20L;
    // 偏移值（默认值0）
    private long offset = 0L;
    private long showPage = 10L; //每页显示页数
    private long[][] showArray; //二维长度为2  [*][1] 数值 [*][2]页类型 －1记录总数 0当前页 1上一页 2首页 3普通页 4末页 5下一页

    public PageBO() {
    }

    public PageBO(long _page) {
        calculate(_page, 0, 0, -1);
    }

    public PageBO(long _page, long _pageSize) {
        calculate(_page, _pageSize, 0, -1);
    }

    /**
     * calculate
     */
    private void calculate(long _page, long _pageSize, long _totalCount, long _offset) {
        if (_totalCount >= 0)
            totalCount = _totalCount;
        if (_page > 0)
            this.page = _page;
        if (_pageSize > 0)
            pageSize = _pageSize;
        if (_offset >= 0)
            offset = _offset;
        long totalPages = this.getTotalPage();
        // 当显示页为0时显示第一页
        if (this.page <= 0)
            this.page = 1;
            // 当显示页的值大于总页数并且总页数>0时显示最后一页
        else if (this.page > totalPages && totalPages > 0)
            this.page = totalPages;

        //生成每页显示页数数组
        long first = 0;
        long previous = 0;
        long commonBegin = 0;
        long commonEnd = 0;
        long last = 0;
        long next = 0;

        if (totalPages < this.showPage) {
            commonBegin = 1;
            commonEnd = totalPages;
        } else {
            commonBegin = ((this.page - 2) < 1) ? 1 : (this.page - 2);
            commonEnd = (commonBegin + this.showPage - 1) > totalPages ? totalPages : (commonBegin + this.showPage - 1);

            if ((commonEnd - commonBegin) < (this.showPage - 1)) {
                commonBegin = commonEnd - this.showPage + 1;
            }
        }

        if (totalPages > 1) {
            if (this.page == 1) {
                next = 1;
                if ((totalPages - commonEnd) > 0)
                    last = 1;
            } else if (this.page == totalPages) {
                previous = 1;
                if ((commonBegin - 1) > 0)
                    first = 1;
            } else {
                next = 1;
                previous = 1;
                if ((totalPages - commonEnd) > 0)
                    last = 1;
                if ((commonBegin - 1) > 0)
                    first = 1;
            }
        }

        int len = (int) (first + previous + (commonEnd - commonBegin + 1) + last + next);
        if (this.totalCount > 0)
            len = len + 1;
        showArray = new long[len][2];
        int arrayindex = 0;
        //－1记录总数 0当前页 1 首页  2上一页 3普通页 4末页 5下一页
        if (this.totalCount > 0) {
            showArray[arrayindex][0] = this.totalCount;
            showArray[arrayindex][1] = -1;
            arrayindex++;
        }

        if (previous == 1) {
            showArray[arrayindex][0] = this.page - 1;
            showArray[arrayindex][1] = 1;
            arrayindex++;
        }
        if (first == 1) {
            showArray[arrayindex][0] = 1;
            showArray[arrayindex][1] = 2;
            arrayindex++;
        }
        if (last == 1) {
            showArray[showArray.length - 2][0] = totalPages;
            showArray[showArray.length - 2][1] = 4;
        }
        if (next == 1) {
            showArray[showArray.length - 1][0] = this.page + 1;
            showArray[showArray.length - 1][1] = 5;
        }

        for (long i = commonBegin; i <= commonEnd; i++, arrayindex++) {
            showArray[arrayindex][0] = i;
            showArray[arrayindex][1] = 3;
            if (this.page == i)
                showArray[arrayindex][1] = 0;
        }
    }

    @Override
    public PageIO setPage(long page) {
        calculate(page, 0, -1, -1);
        return this;
    }

    @Override
    public PageIO setPageSize(long pageSize) {
        calculate(0, pageSize, -1, -1);
        return this;
    }

    @Override
    public PageIO setOffset(long offset) {
        calculate(page, 0, -1, offset);
        return this;
    }

    @Override
    public PageIO setTotalCount(long totalCount) {
        calculate(0, 0, totalCount, -1);
        return this;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public long getTotalPage() {
        return ((totalCount - this.getOffset() > 0 ? totalCount - this.getOffset() : 0) + this.getPageSize() - 1) / this.getPageSize();
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
    public long getOffset() {
        return offset;
    }

    @Override
    public long getStartIndex() {
        return (this.getPage() - 1) * this.getPageSize() + this.getOffset();
    }

    @Override
    public long getEndIndex() {
        return this.getPage() * this.getPageSize() + this.getOffset();
    }

    @Override
    public String getPosition() {
        return "limit " + this.getStartIndex() + "," + this.getPageSize();
    }

    @Override
    public long getShowPage() {
        return showPage;
    }

    @Override
    public long[][] getShowArray() {
        return showArray;
    }

    @Override
    public long getPrevPage() {
        return this.getPage() - 1 < 1 ? 1 : this.getPage() - 1;
    }

    @Override
    public long getNextPage() {
        return this.getPage() + 1 > this.getTotalPage() ? this.getTotalPage() : this.getPage() + 1;
    }

    public static void main(String[] args) {
        PageIO pb = new PageBO();
        pb.setTotalCount(188);
        pb.setPage(10);
        pb.setPageSize(55);
        pb.setOffset(6);
        System.out.println(pb.getTotalPage());
        System.out.println(pb.getPageSize());
        System.out.println(pb.getPage());
        System.out.println(pb.getPosition());
        System.out.println(pb.getShowArray());
        System.out.println(pb.getPrevPage());
        System.out.println(pb.getNextPage());
    }
}
