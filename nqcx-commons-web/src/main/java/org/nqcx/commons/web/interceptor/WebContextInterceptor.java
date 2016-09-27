/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import org.nqcx.commons.util.StringUtils;
import org.nqcx.commons.web.WebContext;
import org.nqcx.commons.web.WebSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContextInterceptor extends WebSupport implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(WebContextInterceptor.class);
    private final static Logger access_logger = LoggerFactory.getLogger("_ACCESS_LOGGER");

    protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
    protected static final String HEADER_REQUEST_WITH = "x-requested-with";
    protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    protected static final String HEADER_REAL_IP = "X-Real-IP";

    @Autowired(required = false)
    private LocaleResolver localeResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.remove();

        StringBuffer url = new StringBuffer(request.getRequestURL());
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        Locale locale = localeResolver == null ? null : localeResolver.resolveLocale(request);

        access_logger.info("{}, method: {}, remoteAddr: {}, referer: {}, isAjax: {}, url: {}, locale: {}, User-Agent: {}",
                request.getRequestURI(),
                request.getMethod(),
                this.getRemoteAddr(request),
                request.getHeader("referer"),
                this.isAjax(request),
                url.toString(),
                locale,
                request.getHeader("User-Agent"));

        WebContext webContext = getWebContext();
        // 判断是否 ajax 访问
        webContext.setAjax(this.isAjax(request));
        // 取IP
        webContext.setRemoteAddr(this.getRemoteAddr(request));
        // 取 server name
        webContext.setServerName(request.getServerName());
        // 取 locale
        webContext.setLocale(locale);

        return true;
    }

    /**
     * 通过 response 直接返回 ContentType 为 application/json 格式字符串
     *
     * @param response
     * @param result
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */

    protected void responseJsonResult(HttpServletResponse response, String result) {
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
     * @param request
     * @return
     * @author naqichuan Oct 14, 2013 4:04:03 PM
     */
    protected boolean isAjax(HttpServletRequest request) {
        return XHR_OBJECT_NAME.equals(request.getHeader(HEADER_REQUEST_WITH));
    }

    /**
     * 取得 IP
     *
     * @param request
     * @return
     * @author naqichuan Oct 14, 2013 3:54:51 PM
     */
    protected String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader(HEADER_REAL_IP);

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        if (StringUtils.isNotBlank(ip) && ip.indexOf(",") != -1)
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();

        return ip;
    }

    /**
     * @return
     * @author naqichuan Oct 12, 2013 3:41:44 PM
     */
    protected boolean clearAndReturn() {
        WebContext.remove();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
    }
}
