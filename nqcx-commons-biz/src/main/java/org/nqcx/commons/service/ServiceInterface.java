/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

import org.nqcx.commons.lang.Transfer;

/**
 * 
 * @author nqcx 2013-4-3 下午5:08:36
 * 
 */
public interface ServiceInterface {

	/**
	 * 插入数据
	 * 
	 * @param o
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> add(O o) throws ServiceException;

	/**
	 * 更新数据
	 * 
	 * @param o
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> modify(O o) throws ServiceException;

	/**
	 * 删除数据
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> delOne(Transfer<O> transfer) throws ServiceException;

	/**
	 * 删除多条数据
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> delAll(Transfer<O> transfer) throws ServiceException;

	/**
	 * 执行查询
	 * 
	 * @param transfer
	 * @throws ServiceException
	 */
	public <O> void query(Transfer<O> transfer) throws ServiceException;

	/**
	 * 查询总数
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> long queryCount(Transfer<O> transfer) throws ServiceException;

	/**
	 * 根据ID取得详情
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public <O> O getById(long id) throws ServiceException;
}
