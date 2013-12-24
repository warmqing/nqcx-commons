/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author naqichuan Sep 23, 2013 4:41:48 PM
 * 
 */
public interface MapperInterface {

	/**
	 * 插入记录
	 * 
	 * @author naqichuan Sep 24, 2013 10:01:05 AM
	 * @param t
	 * @return
	 */
	public <T> int insert(T t);

	/**
	 * 根据 id 更新记录
	 * 
	 * @author naqichuan Sep 24, 2013 10:00:37 AM
	 * @param t
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
	 * 根据 id 取单条记录
	 * 
	 * @author naqichuan Sep 24, 2013 9:59:17 AM
	 * @param id
	 * @return
	 */
	public <T> T getById(long id);

	/**
	 * 查询总数
	 * 
	 * @author naqichuan Sep 24, 2013 9:58:57 AM
	 * @param map
	 * @return
	 */
	public long queryCount(Map<String, Object> map);

	/**
	 * 查询列表
	 * 
	 * @author naqichuan Sep 24, 2013 9:58:43 AM
	 * @param map
	 * @return
	 */
	public <T> List<T> query(Map<String, Object> map);
}
