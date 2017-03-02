/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import org.nqcx.commons.util.json.JsonUtils;
import org.nqcx.commons.web.login.LoginContext;
import org.nqcx.commons.web.url.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginRequiredInterceptor extends WebContextInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginRequiredInterceptor.class);

    protected static final String NEED_LOGIN_JSON;

    static {
        Map<String, Object> needLoginMap = new HashMap<String, Object>();
        needLoginMap.put("success", false);
        needLoginMap.put("needLogin", true);
        NEED_LOGIN_JSON = JsonUtils.mapToJson(needLoginMap);
    }

    protected UrlBuilder homeUrl;
    protected UrlBuilder loginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginContext loginContext = getLoginContext();

        // 取得用户请求方式 request with
        if (!checkLogin(response, loginContext)) {
            if (isAjax()) {
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

    /**
     * @param request
     * @return
     */
    protected String getLoginUrl(HttpServletRequest request) {
        return getLoginUrl(request, null);
    }

    /**
     * @param request
     * @return
     */
    protected String getLoginUrl(HttpServletRequest request, Map<String, String> params) {
        if (homeUrl == null)
            return "";
        UrlBuilder.Builder currentUrlBuilder = homeUrl.forPath(request.getServletPath());
        currentUrlBuilder.put(request.getParameterMap());

        if (loginUrl == null)
            return "";
        UrlBuilder.Builder loginUrlBuilder = loginUrl.forPath();

        if (params != null && params.size() > 0)
            loginUrlBuilder.put(params);
        loginUrlBuilder.put("redirectUrl", generateRedirectUrl(currentUrlBuilder));

        return loginUrlBuilder.build();
    }

    /**
     * 对当前的 url 进行处理，生成符合预期的 redirectUrl
     * <p/>
     * 该方法用于子类扩展和重构 redirectUrl
     *
     * @param currentUrlBuilder
     * @return
     */
    protected String generateRedirectUrl(UrlBuilder.Builder currentUrlBuilder) {
        if (currentUrlBuilder == null)
            return null;
        return currentUrlBuilder.build();
    }

    public void setHomeUrl(UrlBuilder homeUrl) {
        this.homeUrl = homeUrl;
    }

    public void setLoginUrl(UrlBuilder loginUrl) {
        this.loginUrl = loginUrl;
    }
}
