/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import java.util.List;

import org.nqcx.commons.mapper.MapperAssoSupport;

/**
 * 
 * @author nqcx Apr 7, 2013
 * 
 */
public abstract class ManagerAssoSupport extends ManagerSupport {

	@Override
	protected abstract MapperAssoSupport getMapper();

	public int deleteByLeftId(long leftId) {
		return getMapper().deleteByLeftId(leftId);
	}

	public int deleteByRightId(long rightId) {
		return getMapper().deleteByRightId(rightId);
	}

	public <O> O getByRightId(long rightId) {
		return getMapper().getByRightId(rightId);
	}

	public <O> List<O> queryByLeftId(long leftId) {
		return getMapper().queryByLeftId(leftId);
	}
}