/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nqcx.commons.web.login.LoginTicket;

/**
 * 
 * @author nqcx 2013-4-10 下午5:41:47
 * 
 */
public class LoginTicketRequiredInterceptor extends LoginRequiredInterceptor {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		LoginTicket ticket = getLoginTicket();

		if (ticket == null || ticket.getAccount() == null) {
			if (isAjax(request)) {
				logger.info("RemoteAddr [" + request.getRemoteAddr()
						+ "] from ajax check ticket false!");

				responseOutWithJson(response, NEED_LOGIN_JSON);
			} else {
				logger.info("RemoteAddr [" + request.getRemoteAddr()
						+ "] from normal way check ticket false!");

				response.sendRedirect(getLoginUrl(request));
			}

			return false;
		}

		logger.info("Account [" + ticket.getAccount() + "] check ticket true!");

		return true;

	}

	/**
	 * 取出Ticket的信息
	 * 
	 * @return
	 */
	protected LoginTicket getLoginTicket() {
		return LoginTicket.getTicket();
	}
}
