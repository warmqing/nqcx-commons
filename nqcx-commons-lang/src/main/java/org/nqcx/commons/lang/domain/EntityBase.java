/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public class EntityBase implements Serializable {

	private static final long serialVersionUID = -7339324118507199449L;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
