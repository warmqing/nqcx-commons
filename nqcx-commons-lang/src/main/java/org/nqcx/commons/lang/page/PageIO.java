/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.page;

/**
 * Page interface object
 *
 * @author naqichuan 2014年8月14日 上午11:03:21
 */
public interface PageIO {

    /**
     * @param totalCount 总记录数
     * @return 自身
     */
    PageIO setTotalCount(long totalCount);

    /**
     * @param page 页数
     * @return 自身
     */
    PageIO setPage(long page);

    /**
     * @param pageSize 每页数据条数
     * @return 自身
     */
    PageIO setPageSize(long pageSize);

    /**
     * 取得记录总数
     *
     * @return 返回 long 型的记录总数
     */
    long getTotalCount();

    /**
     * 取得记录分页后的总页数
     *
     * @return 总页数
     */
    long getTotalPage();

    /**
     * 取得当前页
     *
     * @return 当前页
     */
    long getPage();

    /**
     * 取得每页显示记录条数
     *
     * @return 每页记录数
     */
    long getPageSize();

    /**
     * 取得记录起始位置，在 SQL 中调用 从0开始
     *
     * @return 开始位置
     */
    long getStartIndex();

    /**
     * 取得记录的结束位置，要 SQL 中调用
     *
     * @return 结束位置
     */
    long getEndIndex();

    /**
     * 取得 mysql limit 字符串
     *
     * @return mysql Limit 字符串
     */
    String getPosition();

    /**
     * 取得每页显示分页页数
     *
     * @return
     */
    long getShowPage();

    /**
     * 取得分页数组
     *
     * @return
     */
    long[][] getShowArray();

    /**
     * 获取上一页页码
     *
     * @return
     */
    public long getPrevPage();

    /**
     * 获取下一页页码
     *
     * @return
     */
    long getNextPage();
}