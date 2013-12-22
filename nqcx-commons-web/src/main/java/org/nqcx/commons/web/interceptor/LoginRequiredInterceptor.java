/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nqcx.commons.web.login.LoginContext;
import org.nqcx.commons.web.url.UrlBuilder;
import org.nqcx.commons.web.url.UrlBuilder.Builder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author nqcx Apr 10, 2013
 * 
 */
public class LoginRequiredInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = Logger.getLogger(this.getClass());

	protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
	protected static final String HEADER_REQUEST_WITH = "x-requested-with";
	protected static final String NEED_LOGIN_JSON = "{\"success\": false, \"needLogin\": true}";

	protected UrlBuilder homeUrl;
	protected UrlBuilder loginUrl;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		LoginContext loginContext = getLoginContext();

		// 取得用户请求方式 request with
		if (!checkLogin(response, loginContext)) {
			if (isAjax(request)) {
				logger.info("RemoteAddr [" + request.getRemoteAddr()
						+ "] from ajax check login false!");

				responseOutWithJson(response, NEED_LOGIN_JSON);
			} else {
				logger.info("RemoteAddr [" + request.getRemoteAddr()
						+ "] from normal way check login false!");

				response.sendRedirect(getLoginUrl(request));
			}

			return false;
		}

		logger.info("Account [" + loginContext.getAccount()
				+ "] check login true!");

		return true;
	}

	/**
	 * 通过验证 LoginContext，判断用户是否登录，如果需要更新数据库，要重写该方法
	 * 
	 * @param context
	 * @return
	 */
	protected boolean checkLogin(HttpServletResponse response,
			LoginContext context) {
		if (context == null || !context.isLogin()) {
			// 没登录
			return false;
		}

		return true;
	}

	/**
	 * 检查是否通过ajax访问
	 * 
	 * @param request
	 * @return
	 */
	protected boolean isAjax(HttpServletRequest request) {
		return XHR_OBJECT_NAME.equals(request.getHeader(HEADER_REQUEST_WITH));
	}

	/**
	 * 取出登录的信息
	 * 
	 * @return
	 */
	protected LoginContext getLoginContext() {
		return LoginContext.getLoginContext();
	}

	@SuppressWarnings("unchecked")
	protected String getLoginUrl(HttpServletRequest request)
			throws MalformedURLException {
		Builder currentUrlBuilder = homeUrl.forPath(request.getServletPath());
		currentUrlBuilder.put(request.getParameterMap());

		Builder loginUrlBuilder = loginUrl.forPath();

		loginUrlBuilder.put("returnUrl", currentUrlBuilder.build());

		return loginUrlBuilder.build();
	}

	/**
	 * 以JSON格式输出
	 * 
	 * @param response
	 */
	protected void responseOutWithJson(HttpServletResponse response,
			String needLogin) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(needLogin);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	public void setHomeUrl(UrlBuilder homeUrl) {
		this.homeUrl = homeUrl;
	}

	public void setLoginUrl(UrlBuilder loginUrl) {
		this.loginUrl = loginUrl;
	}
}
