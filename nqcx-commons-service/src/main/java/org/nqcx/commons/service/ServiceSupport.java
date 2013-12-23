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
import org.nqcx.commons.manager.ManagerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author naqichuan Dec 23, 2013 10:39:11 PM
 * 
 */
public abstract class ServiceSupport implements ServiceInterface {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected abstract ManagerInterface getManager();

	/**
	 * 添加一条数据到数据库中
	 */
	@Override
	public <T> DTO add(T t) {
		DTO dto = new DTO();
		try {
			dto.setSuccess(getManager().add(t) > 0);
		} catch (Exception e) {
			logger.error("ServiceSupport add error", e);
		}
		return dto;
	}

	/**
	 * 根据ID修改一条数据
	 */
	@Override
	public <T> DTO modify(T t) {
		DTO dto = new DTO();
		try {
			int rs = getManager().modify(t);
			dto.setSuccess(true);
			dto.putResult("rs", rs); // 如果返回0，表示数据未被修改
		} catch (Exception e) {
			logger.error("ServiceSupport modify error", e);
		}
		return dto;
	}

	/**
	 * 根据ID（取dto中的id属性）从数据库中删除一条数据
	 */
	@Override
	public void delOne(DTO dto) {
		try {
			int rs = getManager().delOne(dto);
			dto.setSuccess(true);
			dto.putResult("rs", rs); // 如果返回0，表示数据未被删除
		} catch (Exception e) {
			logger.error("ServiceSupport delOne error", e);
		}
	}

	/**
	 * 根据IDS（取dto中的ids属性）从数据库中删除多条数据
	 */
	@Override
	public void delAll(DTO dto) {
		try {
			long rs = getManager().delAll(dto);
			dto.setSuccess(true);
			dto.putResult("rs", rs); // 如果返回0，表示数据未被删除
		} catch (Exception e) {
			logger.error("ServiceSupport delAll error", e);
		}
	}

	/**
	 * 根据ID查询一条数据
	 */
	@Override
	public <T> T getById(long id) {
		try {
			return getManager().getById(id);
		} catch (Exception e) {
			logger.error("ServiceSupport getById error", e);
		}
		return null;
	}

	/**
	 * 根据条件查询数据列表
	 */
	@Override
	public <T> List<T> query(DTO dto) {
		try {
			return getManager().query(dto);
		} catch (Exception e) {
			logger.error("ServiceSupport query error", e);
		}
		return null;
	}

	/**
	 * 根据条件查询数据总数
	 */
	@Override
	public long queryCount(DTO dto) {
		try {
			return getManager().queryCount(dto);
		} catch (Exception e) {
			logger.error("ServiceSupport queryCount error", e);
		}
		return 0;
	}
}
