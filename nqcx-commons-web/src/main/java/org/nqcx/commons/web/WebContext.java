/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web;

/**
 * 
 * @author naqichuan Sep 26, 2013 11:42:42 AM
 * 
 */
public class WebContext {

	private final static ThreadLocal<WebContext> holder = new ThreadLocal<WebContext>() {

		@Override
		protected WebContext initialValue() {
			return new WebContext();
		}
	};

	private boolean ajax = false;
	private String remoteAddr;

	public boolean isAjax() {
		return ajax;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public static void setWebContext(WebContext webContext) {
		holder.set(webContext);
	}

	public static WebContext getWebContext() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}

}
