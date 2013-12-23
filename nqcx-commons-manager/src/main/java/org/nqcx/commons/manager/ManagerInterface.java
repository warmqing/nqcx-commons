/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import java.util.List;

import org.nqcx.commons.lang.DTO;

/**
 * 
 * @author naqichuan Dec 23, 2013 10:26:26 PM
 * 
 */
public interface ManagerInterface {

	/**
	 * 插入数据
	 * 
	 * @param o
	 * @return
	 */
	public <T> long add(T t);

	/**
	 * 更新数据
	 * 
	 * @param o
	 * @return
	 */
	public <T> int modify(T t);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public int delOne(DTO dto);

	/**
	 * 删除数据
	 * 
	 * @param dto
	 * @return
	 */
	public int delAll(DTO dto);

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
