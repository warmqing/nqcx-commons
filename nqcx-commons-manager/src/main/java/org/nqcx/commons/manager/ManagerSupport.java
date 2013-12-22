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

import org.nqcx.commons.lang.Transfer;
import org.nqcx.commons.mapper.MapperSupport;

/**
 * ManagerInterface接口的基本实现
 * 
 * @author nqcx 2013-4-3 下午5:07:34
 * 
 */
public abstract class ManagerSupport extends ManagerBase {

	protected abstract MapperSupport getMapper();

	/**
	 * 添加一条数据到数据库中，如果需要处理业务逻辑，子类中重写该方法
	 * 
	 * @param o
	 * @return
	 */
	public <O> long add(O o) {
		return this.insert(o);
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
	 * 根据ID修改数据一条数据，如果需要添加业务逻辑，子类中重写该方法
	 * 
	 * @param o
	 * @return
	 */
	public <O> int modify(O o) {
		return this.update(o);
	}

	/**
	 * 根据ID（取transfer中的id属性）从数据库中删除一条数据，如果需要处理业务逻辑，子类中重写del方法
	 * 
	 * @param id
	 * @return
	 */
	public final <O> int delOne(long id) {
		return this.del(id);
	}

	/**
	 * 根据IDS（取transfer中的ids属性）从数据库中删除多条数据，如果需要处理业务逻辑，子类中重写del方法
	 * 
	 * @param ids
	 * @return
	 */
	public final <O> long delAll(long[] ids) {
		if (ids != null) {
			long returns = 0;
			for (long id : ids) {
				returns += del(id);
			}
			return returns;
		}
		return 0;
	}

	/**
	 * 根据ID查询一条数据
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <O> O getById(long id) {
		return (O) getMapper().getById(id);
	}

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param transfer
	 */
	@SuppressWarnings("unchecked")
	public final <O> void query(Transfer<O> transfer) {
		if (transfer.getPage() != null) {
			transfer.getPage().setTotalCount(this.queryCount(transfer));
		}
		transfer.setList((List<O>) getMapper().query(getParams(transfer)));
	}

	/**
	 * 根据条件查询数据总数
	 * 
	 * @param transfer
	 * @return
	 */
	public final <O> long queryCount(Transfer<O> transfer) {
		return getMapper().queryCount(getParams(transfer));
	}

	private <O> Map<String, Object> getParams(Transfer<O> transfer) {
		if (transfer == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		if (transfer.getParamsMap() != null)
			map.putAll(transfer.getParamsMap());
		if (transfer.getSort() != null)
			map.put("order", transfer.getSort().getOrder());
		else
			map.put("order", "");

		if (transfer.getPage() != null) {
			map.put("pageTag", 1);
			map.put("position", transfer.getPage().getPosition());
		} else {
			map.put("pageTag", 0);
			map.put("position", "");
		}
		return map;
	}

	/**
	 * 插入一条数据到表中，这个方法直接操作Mapper，子类里不可覆盖
	 * 
	 * @param o
	 * @return
	 */
	private <O> long insert(O o) {
		return getMapper().insert(o);
	}

	/**
	 * 根据ID从数据库中删除一条数据，这个方法直接操作Mapper，子类里不可覆盖
	 * 
	 * @param id
	 * @return
	 */
	private int delete(long id) {
		return getMapper().delete(id);
	}

	/**
	 * 根据ID修改数据表中的一条数据，这个方法直接操作Mapper，子类里不可覆盖
	 * 
	 * @param o
	 * @return
	 */
	private <O> int update(O o) {
		return getMapper().update(o);
	}
}
