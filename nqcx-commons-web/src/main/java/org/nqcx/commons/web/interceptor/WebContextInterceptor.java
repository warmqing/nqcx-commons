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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.nqcx.commons.web.WebContext;
import org.nqcx.commons.web.WebSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author naqichuan Sep 26, 2013 11:40:19 AM
 * 
 */
public class WebContextInterceptor extends WebSupport implements
		HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
	protected static final String HEADER_REQUEST_WITH = "x-requested-with";
	protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		WebContext webContext = getWebContext();
		// 判断是否 ajax 访问
		webContext.setAjax(this.isAjax(request));
		// 取IP
		webContext.setRemoteAddr(this.getRemoteAddr(request));

		return true;
	}

	/**
	 * 通过 response 直接返回 ContentType 为 application/json 格式字符串
	 * 
	 * @author naqichuan Sep 26, 2013 3:02:32 PM
	 * @param response
	 * @param result
	 */

	protected void responseJsonResult(HttpServletResponse response,
			String result) {
		response.setCharacterEncoding(DEFAULT_CHARSET_NAME);
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(result);
		} catch (IOException e) {
			logger.error("WebContextInterceptor.responseResult", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * @author naqichuan Oct 14, 2013 4:04:03 PM
	 * @param request
	 * @return
	 */
	protected boolean isAjax(HttpServletRequest request) {
		return XHR_OBJECT_NAME.equals(request.getHeader(HEADER_REQUEST_WITH));
	}

	/**
	 * 取得 IP
	 * 
	 * @author naqichuan Oct 14, 2013 3:54:51 PM
	 * @param request
	 * @return
	 */
	protected String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("X-Real-IP");

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();

		if (StringUtils.isNotBlank(ip) && ip.indexOf(",") != -1)
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();

		return ip;
	}

	/**
	 * @author naqichuan Oct 12, 2013 3:41:44 PM
	 * @return
	 */
	protected boolean clearAndReturn() {
		WebContext.remove();
		return false;
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
