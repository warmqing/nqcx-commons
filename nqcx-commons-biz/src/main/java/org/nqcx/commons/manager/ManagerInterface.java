/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import org.nqcx.commons.lang.Transfer;

/**
 * 
 * @author nqcx 2013-4-8 上午11:41:08
 * 
 */
public interface ManagerInterface {

	/**
	 * 插入数据
	 * 
	 * @param o
	 * @return
	 */
	public <O> long add(O o);

	/**
	 * 更新数据
	 * 
	 * @param o
	 * @return
	 */
	public <O> int modify(O o);

	/**
	 * 删除一条记录
	 * 
	 * @param id
	 * @return
	 */
	public <O> int delOne(long id);

	/**
	 * 删除多条记录
	 * 
	 * @param ids
	 * @return
	 */
	public <O> long delAll(long[] ids);

	/**
	 * 执行查询
	 * 
	 * @param transfer
	 */
	public <O> void query(Transfer<O> transfer);

	/**
	 * 查询总数
	 * 
	 * @param transfer
	 * @return
	 */
	public <O> long queryCount(Transfer<O> transfer);

	/**
	 * 根据ID取得详情
	 * 
	 * @param id
	 * @return
	 */
	public <O> O getById(long id);
}
