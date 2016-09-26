/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.page;

import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午11:03:21
 */
public interface PageBuilder {

    /**
     * @param totalCount 总记录数
     * @return 自身
     */
    public PageBuilder setTotalCount(long totalCount);

    /**
     * @param page 页数
     * @return 自身
     */
    public PageBuilder setPage(long page);

    /**
     * @param pageSize 每页数据条数
     * @return 自身
     */
    public PageBuilder setPageSize(long pageSize);

    /**
     * 取得记录总数
     *
     * @return 返回 long 型的记录总数
     */
    public long getTotalCount();

    /**
     * 取得记录分页后的总页数
     *
     * @return 总页数
     */
    public long getTotalPage();

    /**
     * 取得当前页
     *
     * @return 当前页
     */
    public long getPage();

    /**
     * 取得每页显示记录条数
     *
     * @return 每页记录数
     */
    public long getPageSize();

    /**
     * 取得记录起始位置，在 SQL 中调用 从0开始
     *
     * @return 开始位置
     */
    public long getStartIndex();

    /**
     * 取得记录的结束位置，要 SQL 中调用
     *
     * @return 结束位置
     */
    public long getEndIndex();

    /**
     * 取得 mysql limit 字符串
     *
     * @return mysql Limit 字符串
     */
    public String getPosition();

    /**
     * 取得每页显示分页页数
     *
     * @return
     */
    public long getShowPage();

    /**
     * 取得分页数组
     *
     * @return
     */
    public long[][] getShowArray();
}