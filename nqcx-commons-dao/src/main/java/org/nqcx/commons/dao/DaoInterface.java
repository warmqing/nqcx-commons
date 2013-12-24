/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author naqichuan Oct 18, 2013 3:58:24 PM
 * 
 */
public interface DaoInterface {

	/**
	 * 插入数据
	 * 
	 * @param o
	 * @return
	 */
	public <T> long insert(T t);

	/**
	 * 更新数据
	 * 
	 * @param o
	 * @return
	 */
	public <T> int update(T t);

	/**
	 * 根据 id 删除单条记录
	 * 
	 * @author naqichuan Sep 24, 2013 9:59:57 AM
	 * @param id
	 * @return
	 */
	public int deleteById(long id);

	/**
	 * 根据ID取得详情
	 * 
	 * @param id
	 * @return
	 */
	public <T> T getById(long id);

	/**
	 * 执行查询
	 * 
	 * @author naqichuan Sep 24, 2013 2:44:04 PM
	 * @param map
	 * @return
	 */
	public <T> List<T> query(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 * @return
	 */
	public long queryCount(Map<String, Object> map);
}
