/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

/**
 * 
 * @author nqcx 2013-4-3 下午5:07:12
 * 
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 2985737702436895518L;

	public ServiceException() {
		super();
	}

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Exception e) {
		super(e);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
