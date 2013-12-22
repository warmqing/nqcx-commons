/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.mapper;

import java.util.List;

/**
 * association table data manipulation
 * 
 * @author nqcx 2013-4-7 下午3:18:11
 * 
 */
public interface MapperAssoSupport extends MapperSupport {

	/**
	 * delete by left id
	 * 
	 * @param leftId
	 * @return
	 */
	public int deleteByLeftId(long leftId);

	/**
	 * delte by right id
	 * 
	 * @param rightId
	 * @return
	 */
	public int deleteByRightId(long rightId);

	/**
	 * query by left id
	 * 
	 * @param leftId
	 * @return
	 */
	public <O> List<O> queryByLeftId(long leftId);

	/**
	 * get by right id
	 * 
	 * @param rightId
	 * @return
	 */
	public <O> O getByRightId(long rightId);
}
