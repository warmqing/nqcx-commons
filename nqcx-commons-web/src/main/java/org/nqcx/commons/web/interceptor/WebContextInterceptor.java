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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import static org.nqcx.commons.util.StringUtils.*;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContextInterceptor extends WebSupport implements HandlerInterceptor {

    private final static Logger ACCESS_LOGGER = LoggerFactory.getLogger(LoggerConst.LOGGER_ACCESS_NAME);
    private final static Logger LOGGER = LoggerFactory.getLogger(WebContextInterceptor.class);

    protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
    protected static final String HEADER_REQUEST_WITH = "x-requested-with";
    protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    protected static final String HEADER_REAL_IP = "X-Real-IP";

    protected LocaleResolver localeResolver;


    /*
     * ========================================================================
     * ===================          以下是拦截器方法       ======================
     * ========================================================================
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.remove();
        WebContext wc = getWebContext().start();

        // 取 scheme
        wc.setScheme(request.getScheme());
        // 取 secure
        wc.setSecure(request.isSecure());
        // 取 server name
        wc.setServerName(request.getServerName());
        // 取 port
        wc.setPort(request.getServerPort());
        // 取 contextPath
        wc.setContextPath(request.getContextPath());
        // 取 servletPath
        wc.setServletPath(request.getServletPath());
        // 取 requestURI
        wc.setRequestURI(request.getRequestURI());
        // 取 params
        wc.setParams(getParamsFromRequest(request));
        // 取 requestURL
        wc.setRequestURL(request.getRequestURL());

        // 取 realPath
        wc.setRealPath(request.getSession().getServletContext().getRealPath("/"));

        // 取 remoteAddr
        wc.setRemoteAddr(this.getRemoteAddrFromRequest(request));
        // 取 method
        wc.setMethod(request.getMethod());
        // 判断是否 ajax 访问
        wc.setAjax(this.isAjaxFromRequest(request));
        // 取 locale
        wc.setLocale(localeResolver == null ? null : localeResolver.resolveLocale(request));
        // 取 sessionId
        wc.setSessionId(request.getRequestedSessionId());
        // 取 url
        StringBuffer url = new StringBuffer(request.getRequestURL());
        if (request.getQueryString() != null)
            url.append("?").append(request.getQueryString());
        wc.setUrl(url.toString());
        // 取 referer
        wc.setReferer(request.getHeader("referer"));
        // 取 userAgent
        wc.setUserAgent(request.getHeader("User-Agent"));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        getWebContext().post();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        WebContext wc = getWebContext().end();
        if (requireAccessLog(wc)) {
            ACCESS_LOGGER.info("\"start\": \"{}\", \"post\": \"{}\",  \"end\": \"{}\", \"remoteAddr\": \"{}\"," +
                            " \"serverName\": \"{}\", \"method\": \"{}\", \"scheme\": \"{}\", \"secure\": \"{}\"," +
                            " \"isAjax\": \"{}\", \"uri\": \"{}\", \"locale\": \"{}\", \"sessionId\": \"{}\"," +
                            " \"url\": \"{}\", \"referer\": \"{}\", \"params\": \"{}\"," +
                            " \"data\": \"{}\", \"User-Agent\": \"{}\"",
                    wc.getStart(), wc.getPost(), wc.getEnd(), wc.getRemoteAddr(),
                    wc.getServerName(), wc.getMethod(), wc.getScheme(), wc.isSecure(),
                    wc.isAjax(), wc.getRequestURI(), wc.getLocale(), trimToEmpty(wc.getSessionId()),
                    wc.getUrl(), trimToEmpty(wc.getReferer()), trimToEmpty(wc.getParams()),
                    trimToEmpty(wc.getData()), trimToEmpty(wc.getUserAgent()));
        }
    }

    /**
     * 是否需要输出 access log，如果应用中需要特殊处理访问日志，覆盖该方法
     *
     * @param webContext WebContext
     * @return 返回 true 为需要输出日志
     */
    protected boolean requireAccessLog(WebContext webContext) {
        return true;
    }

    /*
     * ========================================================================
     * ===================       以下是 request 处理方法       ==================
     * ========================================================================
     */

    /**
     * @param request request
     * @return boolean
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

    /**
     * 取参数表
     *
     * @param request request
     * @return string
     */
    protected String getParamsFromRequest(HttpServletRequest request) {
        StringBuilder query = new StringBuilder();

        appendParamsString(query, parseParamsFromRequest(request), false, false);

        if (query.length() > 0 && query.indexOf("&") == 0)
            query.replace(0, 1, "");

        return query.toString();
    }

    /**
     * 生成参数字符串
     *
     * @param query       query
     * @param map         map
     * @param ignoreEmpty 忽略空值（""）
     * @param isEncode    是否进行编码
     */
    protected void appendParamsString(StringBuilder query, Map<String, String[]> map, boolean ignoreEmpty, boolean isEncode) {
        if (map == null || query == null)
            return;

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            final String key = entry.getKey();
            String[] value = entry.getValue();
            if (value == null)
                continue;

            for (final String v : value) {
                appendParamsString(query, key, v, ignoreEmpty, isEncode);
            }
        }
    }

    /**
     * 生成参数字符串
     *
     * @param query       query
     * @param key         key
     * @param value       value
     * @param ignoreEmpty 忽略空值（""）
     * @param isEncode    是否进行编码
     */
    protected void appendParamsString(StringBuilder query, String key, String value, boolean ignoreEmpty, boolean isEncode) {
        if (value == null)
            return;

        String v = String.valueOf(value);
        if (ignoreEmpty && StringUtils.isBlank(v))
            return;

        query.append("&").append(key).append("=").append(isEncode ? encodeValue(v, Charset.forName(DEFAULT_CHARSET_NAME)) : v);
    }

    /**
     * 字符串进行 url 编码
     *
     * @param value   value
     * @param charset charset
     * @return string
     */
    protected String encodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLEncoder.encode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
            LOGGER.warn(e.getMessage());
        }
        return value;
    }

    /*
     * ========================================================================
     * ===================        以下是注入处理方法       ======================
     * ========================================================================
     */

    /**
     * 用于配置文件中配置注入
     *
     * @param localeResolver localeResolver
     */
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}
