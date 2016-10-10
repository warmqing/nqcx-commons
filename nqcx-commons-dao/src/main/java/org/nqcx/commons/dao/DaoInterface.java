/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.dao;

import java.util.List;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午10:41:33
 */
public interface DaoInterface {

    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    <T> int insert(T t);

    /**
     * 更新数据
     *
     * @param t
     * @return
     */
    <T> int update(T t);

    /**
     * 根据 id 删除单条记录
     *
     * @param id
     * @return
     */
    int deleteById(long id);

    /**
     * 根据 ids 删除多条记录
     *
     * @param ids
     * @return
     */
    int deleteByIds(long[] ids);

    /**
     * 根据ID取得详情
     *
     * @param id
     * @return
     */
    <T> T getById(long id);

    /**
     * 执行查询
     *
     * @param map
     * @return
     */
    <T> List<T> query(Map<String, Object> map);

    /**
     * 查询总数
     *
     * @param map
     * @return
     */
    long queryCount(Map<String, Object> map);
}
