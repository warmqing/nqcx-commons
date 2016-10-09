/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

import org.nqcx.commons.lang.DTO;

import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午10:52:21
 */
public interface ServiceInterface {

	/**
	 * 插入数据
	 * <p>
	 * 取ID可以从对象中取，需要从对象中取
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
	 * @param id
	 * @return
	 */
	public DTO deleteOne(long id);

	/**
	 * 删除多条数据
	 * 
	 * @param ids
	 * @return
	 */
	public DTO deleteMultiple(long[] ids);

	/**
	 * 执行查询
	 * 
	 * @param dto
	 * @return
	 */
	public <T> List<T> query(DTO dto);

	/**
	 * 执行查询，用于远程调用执行查询，解决远程调时 dto 取不到分页信息的问题
	 *
	 * @param dto
	 * @return
	 */
	DTO rmiQuery(DTO dto);

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
