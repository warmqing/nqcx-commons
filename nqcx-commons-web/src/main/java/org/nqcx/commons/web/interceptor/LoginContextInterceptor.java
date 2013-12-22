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
import org.nqcx.commons.web.cookie.CookieUtils;
import org.nqcx.commons.web.cookie.NqcxCookie;
import org.nqcx.commons.web.login.LoginContext;
import org.nqcx.commons.web.login.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author nqcx Apr 10, 2013
 * 
 */
public class LoginContextInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private NqcxCookie loginCookie;

	private long sessionTimeout = 30L;
	protected int rate = 2;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (loginCookie == null)
			return true;

		String loginCookieValue = CookieUtils.getCookieValue(request,
				loginCookie.getName());
		if (loginCookieValue == null)
			LoginContext.setLoginContext(null);

		LoginTicket loginTicket = LoginTicket.getTicket();
		LoginContext loginContext = getLoginContext(loginCookieValue);

		boolean needNewCookie = false;

		if (loginContext == null
				|| StringUtils.isNotBlank(loginContext.getAccount())) {
			if (loginTicket.isExpired()) {
				// 如果没有login cookie，并且ticket已过期，需要重新登录的
				LoginContext.setLoginContext(null);
				return true;
			}
			// 从ticket中创建一个loginContext
			loginContext = getLoginContextFromTicket(loginTicket);
			needNewCookie = true;
		} else if (!loginTicket.getAccount().equals(loginContext.getAccount())) {
			if (loginTicket.isExpired()) {
				// 如果login cookie切换了用户，并且ticket已过期，需要重新登录的
				LoginContext.setLoginContext(null);
				return true;
			}
			// 从ticket中创建一个loginContext
			loginContext = getLoginContextFromTicket(loginTicket);
			needNewCookie = true;
		}

		long current = System.currentTimeMillis();
		long created = loginContext.getCreated();
		long expires = loginContext.getExpires();

		// 如果没有设置过期时间，则使用默认的
		long timeout = expires == 0 ? sessionTimeout * 1000 : expires - created;

		// 如果已过期
		if (current - created >= timeout)
			return true;

		// 如果新cookie或是更改了登录或剩下的时间只有2/3，就需要重新派发cookie
		if (needNewCookie || ((current - created) * rate > timeout)) {
			// 写最后一次访问的cookie
			loginContext.setCreated(current);
			if (expires != 0)
				loginContext.setTimeout(timeout);

			CookieUtils.setCookie(response, loginCookie.getName(),
					loginContext.toCookieValue());
		}
		setLoginContext(loginContext);

		return true;
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

	/**
	 * 从dotnet 的 ticket中重构出logincontext
	 * 
	 * @param ticket
	 * @return
	 */
	protected LoginContext getLoginContextFromTicket(LoginTicket ticket) {
		LoginContext loginContext = null;
		if (ticket != null && StringUtils.isNotBlank(ticket.getAccount())) {
			loginContext = new LoginContext();
			loginContext.setAccount(ticket.getAccount());
		}
		return loginContext;
	}

	/**
	 * 解析登录cookie的值
	 * 
	 * @param value
	 * @return
	 */
	protected LoginContext getLoginContext(String value) {
		return StringUtils.isBlank(value) ? null : LoginContext.parse(value);
	}

	/**
	 * 更新 LoginContext
	 * 
	 * @param loginContext
	 */
	protected void setLoginContext(LoginContext loginContext) {
		LoginContext.setLoginContext(loginContext);
	}
}
