/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

import org.nqcx.commons.lang.Transfer;
import org.nqcx.commons.manager.ManagerInterface;

/**
 * 
 * @author nqcx 2013-4-3 下午5:08:57
 * 
 */
public abstract class ServiceSupport {

	protected abstract ManagerInterface getManager();

	/**
	 * 添加一条数据到数据库中
	 * 
	 * @param o
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> add(O o) throws ServiceException {
		Transfer<O> transfer = new Transfer<O>();
		try {
			long id = getManager().add(o);
			if (id > 0) {
				transfer.setSuccess(true);
				transfer.setId(id);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return transfer;
	}

	/**
	 * 根据ID修改数据一条数据
	 * 
	 * @param o
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> modify(O o) throws ServiceException {
		Transfer<O> transfer = new Transfer<O>();
		try {
			int rs = getManager().modify(o);
			transfer.setSuccess(true);
			transfer.putResult("rs", rs);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return transfer;
	}

	/**
	 * 根据ID（取transfer中的id属性）从数据库中删除一条数据
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> delOne(Transfer<O> transfer) throws ServiceException {
		try {
			if (transfer != null) {
				int rs = getManager().delOne(transfer.getId());
				transfer.setSuccess(true);
				transfer.putResult("rs", rs);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return transfer;
	}

	/**
	 * 根据IDS（取transfer中的ids属性）从数据库中删除多条数据
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> Transfer<O> delAll(Transfer<O> transfer) throws ServiceException {
		try {
			if (transfer != null) {
				long rs = getManager().delAll(transfer.getIds());
				transfer.setSuccess(true);
				transfer.putResult("rs", rs);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return transfer;
	}

	/**
	 * 根据ID查询一条数据
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <O> O getById(long id) throws ServiceException {
		try {
			return (O) getManager().getById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param transfer
	 * @throws ServiceException
	 */
	public <O> void query(Transfer<O> transfer) throws ServiceException {
		try {
			getManager().query(transfer);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 根据条件查询数据总数
	 * 
	 * @param transfer
	 * @return
	 * @throws ServiceException
	 */
	public <O> long queryCount(Transfer<O> transfer) throws ServiceException {
		try {
			return getManager().queryCount(transfer);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
