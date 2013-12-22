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
 * @author nqcx 2013-4-3 下午5:21:32
 * 
 */
public interface MapperSupport {

	/**
	 * 插入数据
	 * 
	 * @param <O>
	 * @param o
	 * @return
	 */
	public <O> int insert(O o);

	/**
	 * 更新数据
	 * 
	 * @param <O>
	 * @param o
	 * @return
	 */
	public <O> int update(O o);

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @return
	 */
	public int delete(long id);

	/**
	 * 执行查询
	 * 
	 * @param map
	 * @return
	 */
	public <O> List<O> query(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 * @return
	 */
	public long queryCount(Map<String, Object> map);

	/**
	 * 根据ID取得详情
	 * 
	 * @param id
	 * @return
	 */
	public <O> O getById(long id);

}
