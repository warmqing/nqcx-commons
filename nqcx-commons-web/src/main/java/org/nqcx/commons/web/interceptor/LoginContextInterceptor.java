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
import org.nqcx.commons.web.login.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginContextInterceptor extends WebContextInterceptor {

    @Autowired(required = false)
    private NqcxCookie loginCookie;

    private long sessionTimeout = 30L;
    protected int rate = 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginContext.setLoginContext(null);

        if (loginCookie == null)
            return true;

        LoginTicket loginTicket = LoginTicket.getTicket();
        LoginContext loginContext = getLoginContext(CookieUtils.getCookieValue(request, loginCookie.getName()));

        boolean needNewCookie = false;

        if (loginContext == null || loginContext.getAccount() == 0) {
            if (loginTicket == null || loginTicket.isExpired()) {
                // 没有 login cookie，并且没有 ticket 或 ticket 已过期，需要重新登录的
                removeLoginCookie(request, response);
                LoginContext.remove();
                return true;
            }
            // 没有 login cookie，并且 ticket 没过期，从ticket中创建一个loginContext
            loginContext = getLoginContextFromTicket(loginTicket);
            needNewCookie = true;
        } else {
            if (loginTicket == null) {
                // 有 login cookie，但没有 ticket，需要重新登录的
                removeLoginCookie(request, response);
                LoginContext.remove();
                return true;
            } else if (loginTicket.isExpired()) {
                // 有 login cookie，但 ticket 已过期，不做处理
            } else if (loginTicket.getAccount() != loginContext.getAccount()) {
                // login cookie 切换了用户，但 ticket 没过期，从ticket中创建一个loginContext
                loginContext = getLoginContextFromTicket(loginTicket);
                needNewCookie = true;
            } else {
                // 有 login cookie，ticket 存在且 ticket 没过期，不做处理
            }
        }

        long current = System.currentTimeMillis(); //当前时间戳
        long created = loginContext.getCreated(); //创建时间戳
        long expires = loginContext.getExpires(); //过期时间

        // 如果没有设置过期时间，则使用默认的
        long timeout = expires == 0 ? sessionTimeout * 60 * 1000 : expires - created;

        // 如果已过期 login cookie，并且 ticket 已过期，重新登录
        if (current - created >= timeout && loginTicket.isExpired()) {
            removeLoginCookie(request, response);
            LoginContext.remove();
            return true;
        }

        // 如果新cookie或是更改了登录或剩下的时间只有2/3，就需要重新派发cookie
        if (needNewCookie || ((current - created) * 3 / rate > timeout)) {
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
     * 从 LoginTicket 中重构出 LoginContext
     *
     * @param ticket
     * @return
     */
    protected LoginContext getLoginContextFromTicket(LoginTicket ticket) {
        LoginContext loginContext = null;
        if (ticket != null && ticket.getAccount() > 0) {
            loginContext = new LoginContext();
            loginContext.setAccount(ticket.getAccount());
        }
        return loginContext;
    }

    /**
     * 用于应用中往cookie里添加内容
     *
     * @param loginContext
     */
    protected void fillLoginContext(LoginContext loginContext) {
        // 填充 id
        // 填充 nick
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
}
