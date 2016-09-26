/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
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

import org.nqcx.commons.util.NqcxStringUtils;
import org.nqcx.commons.web.WebContext;
import org.nqcx.commons.web.WebSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContextInterceptor extends WebSupport implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
    protected static final String HEADER_REQUEST_WITH = "x-requested-with";
    protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        WebContext webContext = getWebContext();
        // 判断是否 ajax 访问
        webContext.setAjax(this.isAjax(request));
        // 取IP
        webContext.setRemoteAddr(this.getRemoteAddr(request));

        return true;
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
        if (NqcxStringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Real-IP");

        if (NqcxStringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        if (NqcxStringUtils.isNotBlank(ip) && ip.indexOf(",") != -1)
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
                                                                                                                       throws Exception {
    }
}
