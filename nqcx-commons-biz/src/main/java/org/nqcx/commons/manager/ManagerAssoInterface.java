/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import java.util.List;

/**
 * 
 * @author nqcx 2013-4-8 上午11:40:34
 * 
 */
public interface ManagerAssoInterface extends ManagerInterface {

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
