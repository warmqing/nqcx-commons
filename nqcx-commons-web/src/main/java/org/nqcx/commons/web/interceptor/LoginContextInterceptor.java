/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import org.nqcx.commons.util.StringUtils;
import org.nqcx.commons.web.cookie.CookieUtils;
import org.nqcx.commons.web.cookie.NqcxCookie;
import org.nqcx.commons.web.login.LoginContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginContextInterceptor extends WebContextInterceptor {

    /**
     * 需要注入
     */
    protected NqcxCookie loginCookie;

    protected long sessionTimeout = 30L;
    protected int rate = 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginContext.remove();

        if (loginCookie == null)
            return true;

        LoginContext loginContext = getLoginContext(CookieUtils.getCookieValue(request, loginCookie.getName()));
        if (loginContext == null || StringUtils.isBlank(loginContext.getAccount())) {
            // 没有 login cookie，直接返回
            removeLoginCookie(request, response);
            LoginContext.remove();
            return true;
        }

        long current = System.currentTimeMillis(); //当前时间戳
        long created = loginContext.getCreated(); //创建时间戳
        long expires = loginContext.getExpires(); //过期时间

        // 如果没有设置过期时间，则使用默认的
        long timeout = expires == 0 ? sessionTimeout * 60 * 1000 : expires - created;

        // 如果已过期 login cookie，直接返回
        if (current - created >= timeout) {
            removeLoginCookie(request, response);
            LoginContext.remove();
            return true;
        }

        // 如果新cookie或是更改了登录或剩下的时间只有2/3，就需要重新派发cookie
        if ((current - created) * 3 / rate > timeout) {
            // 写最后一次访问的cookie
            loginContext.setCreated(current);
            if (expires != 0)
                loginContext.setTimeout(timeout);

            // 这里添加一个步骤用于应用中填充 cookie
            this.fillLoginContext(loginContext);

            CookieUtils.setCookie(response, loginCookie.getName(), loginContext.toCookieValue());
        }

        LoginContext.setLoginContext(loginContext);
        return true;
    }

    /**
     * 用于应用中修改或添加 cookie 属性，非必须
     *
     * @param loginContext
     */
    protected void fillLoginContext(LoginContext loginContext) {
    }

    /**
     * 删除 login cookie
     *
     * @param request
     * @param response
     */
    protected void removeLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        if (loginCookie == null)
            return;

        CookieUtils.removeCookie(request, response, loginCookie.getName());
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
     * 用于配置文件中配置注入
     *
     * @param loginCookie
     */
    public void setLoginCookie(NqcxCookie loginCookie) {
        this.loginCookie = loginCookie;
    }
}
