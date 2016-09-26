/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nqcx.commons.web.login.LoginContext;
import org.nqcx.commons.web.url.UrlBuilder;
import org.nqcx.commons.web.url.UrlBuilder.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginRequiredInterceptor extends WebContextInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String NEED_LOGIN_JSON = "{\"success\": false, \"needLogin\": true}";

//    @Autowired(required = false)
//    @Qualifier("_homeUrl")
    protected UrlBuilder homeUrl;

//    @Autowired(required = false)
//    @Qualifier("_loginUrl")
    protected UrlBuilder loginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginContext loginContext = getLoginContext();

        // 取得用户请求方式 request with
        if (!checkLogin(response, loginContext)) {
            if (isAjax(request)) {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from ajax check login false!");

                responseJsonResult(response, NEED_LOGIN_JSON);
            } else {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from normal way check login false!");

                response.sendRedirect(getLoginUrl(request));
            }

            return false;
        }

        logger.info("Account [" + loginContext.getAccount() + "] check login true!");

        return true;
    }

    /**
     * 通过验证 LoginContext，判断用户是否登录，如果需要更新数据库，要重写该方法
     * 
     * @param context
     * @return
     */
    protected boolean checkLogin(HttpServletResponse response, LoginContext context) {
        if (context == null || !context.isLogin()) {
            // 没登录
            return false;
        }

        return true;
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
    protected String getLoginUrl(HttpServletRequest request) throws MalformedURLException {
        if (homeUrl == null)
            return "";
        Builder currentUrlBuilder = homeUrl.forPath(request.getServletPath());
        currentUrlBuilder.put(request.getParameterMap());

        if (loginUrl == null)
            return "";
        Builder loginUrlBuilder = loginUrl.forPath();

        loginUrlBuilder.put("returnUrl", currentUrlBuilder.build());

        return loginUrlBuilder.build();
    }

    public void setHomeUrl(UrlBuilder homeUrl) {
        this.homeUrl = homeUrl;
    }

    public void setLoginUrl(UrlBuilder loginUrl) {
        this.loginUrl = loginUrl;
    }
}
