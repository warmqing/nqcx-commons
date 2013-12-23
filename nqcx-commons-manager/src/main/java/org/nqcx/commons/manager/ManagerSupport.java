/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nqcx.commons.dao.DaoInterface;
import org.nqcx.commons.lang.DTO;
import org.nqcx.commons.mapper.MapperInterface;

/**
 * 
 * @author naqichuan Dec 23, 2013 10:28:43 PM
 * 
 */
public abstract class ManagerSupport implements ManagerInterface {

	protected DaoInterface getDao() {
		return null;
	}

	protected abstract MapperInterface getMapper();

	/**
	 * 添加一条数据到数据库中，如果需要处理业务逻辑，子类中重写该方法
	 */
	@Override
	public <T> long add(T t) {
		return this.insert(t);
	}

	/**
	 * 从数据库中删除一条数据，如果需要处理业务逻辑，子类中重写该方法
	 * 
	 * @param id
	 * @return
	 */
	protected int del(long id) {
		return this.delete(id);
	}

	/**
	 * 从数据库中删除多条数据，如果需要处理业务逻辑，子类中重写该方法
	 * 
	 * @param ids
	 * @return
	 */
	protected int del(long[] ids) {
		if (ids != null) {
			int returns = 0;
			for (long id : ids) {
				returns += del(id);
			}
			return returns;
		}
		return 0;
	}

	/**
	 * 根据ID修改一条数据，如果需要添加业务逻辑，子类中重写该方法
	 */
	@Override
	public <T> int modify(T t) {
		return this.update(t);
	}

	/**
	 * 根据ID（取dto中的id属性）从数据库中删除一条数据，如果需要处理业务逻辑，子类中重写del方法
	 */
	@Override
	public final int delOne(DTO dto) {
		dto.setSuccess(true);
		return this.del(dto.getId());
	}

	/**
	 * 根据IDS（取dto中的ids属性）从数据库中删除多条数据，如果需要处理业务逻辑，子类中重写del方法
	 */
	@Override
	public final int delAll(DTO dto) {
		dto.setSuccess(true);
		return this.del(dto.getIds());
	}

	@Override
	public final <T> T getById(long id) {
		if (getDao() != null)
			return getDao().getById(id);
		else
			return getMapper().getById(id);
	}

	@Override
	public final <T> List<T> query(DTO dto) {
		dto.setSuccess(true);

		if (dto.getPage() != null)
			dto.getPage().setTotalCount(this.queryCount(dto));

		dto.setList(getDao() != null ? getDao().query(getParams(dto))
				: getMapper().query(getParams(dto)));

		return dto.getList();
	}

	@Override
	public final long queryCount(DTO dto) {
		dto.setSuccess(true);
		return getDao() != null ? getDao().queryCount(getParams(dto))
				: getMapper().queryCount(getParams(dto));
	}

	/**
	 * 解析参数，返回 mapper 需要的参数
	 * 
	 * @param dto
	 * @return
	 */
	protected <T> Map<String, Object> getParams(DTO dto) {
		if (dto == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		if (dto.getParamsMap() != null)
			map.putAll(dto.getParamsMap());
		if (dto.getSort() != null)
			map.put("order", dto.getSort().getOrder());
		else
			map.put("order", "");

		if (dto.getPage() != null) {
			map.put("pageTag", 1);
			map.put("position", dto.getPage().getPosition());
		} else {
			map.put("pageTag", 0);
			map.put("position", "");
		}
		return map;
	}

	/**
	 * 插入一条数据到表中，这个方法直接操作Dao，子类里不可覆盖
	 * 
	 * @param t
	 * @return
	 */
	private <T> long insert(T t) {
		return getDao() != null ? getDao().insert(t) : getMapper().insert(t);
	}

	/**
	 * 根据ID从数据库中删除一条数据，这个方法直接操作Dao，子类里不可覆盖
	 * 
	 * @param id
	 * @return
	 */
	private int delete(long id) {
		return getDao() != null ? getDao().deleteById(id) : getMapper()
				.deleteById(id);
	}

	/**
	 * 根据ID修改数据表中的一条数据，这个方法直接操作Dao，子类里不可覆盖
	 * 
	 * @param t
	 * @return
	 */
	private <T> int update(T t) {
		return getDao() != null ? getDao().update(t) : getMapper().update(t);
	}
}
