/*
 * Copyright 2014 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import org.nqcx.commons.lang.consts.LoggerConst;
import org.nqcx.commons.util.StringUtils;
import org.nqcx.commons.web.WebContext;
import org.nqcx.commons.web.WebSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.nqcx.commons.util.StringUtils.*;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContextInterceptor extends WebSupport implements HandlerInterceptor {

    //    private final static Logger logger = LoggerFactory.getLogger(WebContextInterceptor.class);
    private final static Logger access_logger = LoggerFactory.getLogger(LoggerConst.LOGGER_ACCESS_NAME);

    protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
    protected static final String HEADER_REQUEST_WITH = "x-requested-with";
    protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    protected static final String HEADER_REAL_IP = "X-Real-IP";

    protected LocaleResolver localeResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.remove();
        WebContext webContext = getWebContext();

        // 取 scheme
        webContext.setScheme(request.getScheme());
        // 取 secure
        webContext.setSecure(request.isSecure());
        // 取 server name
        webContext.setServerName(request.getServerName());
        // 取 port
        webContext.setPort(request.getServerPort());
        // 取 contextPath
        webContext.setContextPath(request.getContextPath());
        // 取 servletPath
        webContext.setServletPath(request.getServletPath());
        // 取 requestURI
        webContext.setRequestURI(request.getRequestURI());
        // 取 requestURL
        webContext.setRequestURL(request.getRequestURL());

        // 取 realPath
        webContext.setRealPath(request.getSession().getServletContext().getRealPath("/"));

        // 取 remoteAddr
        webContext.setRemoteAddr(this.getRemoteAddrFromRequest(request));
        // 取 method
        webContext.setMethod(request.getMethod());
        // 判断是否 ajax 访问
        webContext.setAjax(this.isAjaxFromRequest(request));
        // 取 locale
        webContext.setLocale(localeResolver == null ? null : localeResolver.resolveLocale(request));
        // 取 sessionId
        webContext.setSessionId(request.getRequestedSessionId());
        // 取 url
        StringBuffer url = new StringBuffer(request.getRequestURL());
        if (request.getQueryString() != null)
            url.append("?").append(request.getQueryString());
        webContext.setUrl(url.toString());
        // 取 referer
        webContext.setReferer(request.getHeader("referer"));
        // 取 userAgent
        webContext.setUserAgent(request.getHeader("User-Agent"));

        access_logger.info("remoteAddr: \"{}\", method: \"{}\", scheme: \"{}\", secure: \"{}\", isAjax: \"{}\"," +
                        " uri: \"{}\", locale: \"{}\", sessionId: \"{}\", url: \"{}\"," +
                        " referer: \"{}\", User-Agent: \"{}\"",
                webContext.getRemoteAddr(), webContext.getMethod(), webContext.getScheme(), webContext.isSecure(), webContext.isAjax(),
                webContext.getRequestURI(), webContext.getLocale(), trimToEmpty(webContext.getSessionId()), webContext.getUrl(),
                trimToEmpty(webContext.getReferer()), trimToEmpty(webContext.getUserAgent()));
        return true;
    }

    /**
     * @param request
     * @return
     * @author naqichuan Oct 14, 2013 4:04:03 PM
     */
    protected boolean isAjaxFromRequest(HttpServletRequest request) {
        return XHR_OBJECT_NAME.equals(request.getHeader(HEADER_REQUEST_WITH));
    }

    /**
     * 取得 IP
     *
     * @param request
     * @return
     * @author naqichuan Oct 14, 2013 3:54:51 PM
     */
    protected String getRemoteAddrFromRequest(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader(HEADER_REAL_IP);

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        if (StringUtils.isNotBlank(ip) && ip.contains(","))
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();

        return ip;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
    }

    /**
     * 用于配置文件中配置注入
     *
     * @param localeResolver localeResolver
     */
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}
