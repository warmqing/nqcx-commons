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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nqcx.commons.web.cookie.CookieUtils;
import org.nqcx.commons.web.cookie.NqcxCookie;
import org.nqcx.commons.web.login.LoginTicket;
import org.nqcx.commons.web.login.LoginTicketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author nqcx Apr 10, 2013
 * 
 */
public class LoginTicketInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = Logger
			.getLogger(LoginTicketInterceptor.class);

	@Autowired
	private NqcxCookie ticketCookie;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		try {
			parseTicket(request);
		} catch (Exception e) {
			logger.warn("parse login ticket error!", e);
		}

		return true;
	}

	protected LoginTicket parseTicket(HttpServletRequest request) {
		if (ticketCookie == null) {
			logger.info("ticketCookie is null!");
			return null;
		}

		String cookieValue = CookieUtils.getCookieValue(request,
				ticketCookie.getName());
		if (StringUtils.isNotBlank(cookieValue)) { // 先check passport的cookie有没有
			LoginTicket ticket = null;

			try {
				ticket = LoginTicketUtils.getLoginTicket(cookieValue,
						ticketCookie.getKey());
			} catch (Exception e) {
				logger.error("decrypt ticket cookie error!", e);
			}

			if (ticket != null && StringUtils.isNotBlank(ticket.getAccount())) {
				LoginTicket.setTicket(ticket);
				return ticket;
			} else {
				logger.info("tick error or ticket expired!");
			}
		} else {
			LoginTicket.setTicket(null);
			logger.info("ticketCookie [" + ticketCookie.getName()
					+ "] is null!");
		}
		return null;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
