/*
 * Copyright 1998-2013 jd.com All right reserved. This software is the
 * confidential and proprietary information of jd.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with jd.com.
 */

package org.nqcx.commons.lang;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * @author naqichuan Dec 23, 2013 10:12:15 PM
 *
 */
public abstract class EntityBase {

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
