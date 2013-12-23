/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

import java.util.List;

import org.nqcx.commons.lang.DTO;

/**
 * 
 * @author naqichuan Dec 23, 2013 11:06:25 PM
 *
 */
public interface ServiceInterface {

	/**
	 * 插入数据
	 * 
	 * <p>
	 * 该方法不能返回插入的自增 ID，需要 ID 可以从对象中取
	 * 
	 * @param t
	 * @return
	 */
	public <T> DTO add(T t);

	/**
	 * 更新数据
	 * 
	 * @param t
	 * @return
	 */
	public <T> DTO modify(T t);

	/**
	 * 删除数据
	 * 
	 * @param dto
	 * @return
	 */
	public void delOne(DTO dto);

	/**
	 * 删除多条数据
	 * 
	 * @param dto
	 */
	public void delAll(DTO dto);

	/**
	 * 执行查询
	 * 
	 * @param dto
	 * @return
	 */
	public <T> List<T> query(DTO dto);

	/**
	 * 查询总数
	 * 
	 * @param dto
	 * @return
	 */
	public long queryCount(DTO dto);

	/**
	 * 根据ID取得详情
	 * 
	 * @param id
	 * @return
	 */
	public <T> T getById(long id);

}
