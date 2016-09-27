/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nqcx.commons.util.StringUtils;
import org.nqcx.commons.web.cookie.CookieUtils;
import org.nqcx.commons.web.cookie.NqcxCookie;
import org.nqcx.commons.web.login.LoginTicket;
import org.nqcx.commons.web.login.LoginTicketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginTicketInterceptor extends WebContextInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);

    @Autowired(required = false)
    protected NqcxCookie ticketCookie;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            parseTicketCookie(request, response);
        } catch (Exception e) {
            logger.warn("parse login ticket cookie error! " + e.getMessage());
        }

        return true;
    }

    /**
     * 解析 ticket cookie
     *
     * @param request
     * @param response
     */
    protected void parseTicketCookie(HttpServletRequest request, HttpServletResponse response) {
        LoginTicket.setTicket(null);

        if (ticketCookie == null)
            return;

        String cookieValue = CookieUtils.getCookieValue(request, ticketCookie.getName());
        if (StringUtils.isNotBlank(cookieValue)) {
            try {
                LoginTicket.setTicket(LoginTicketUtils.getLoginTicket(cookieValue, ticketCookie.getKey()));
                if (LoginTicket.getTicket() != null)
                    return;
                logger.info("ticket error or ticket expired!");
            } catch (Exception e) {
                logger.warn("decrypt ticket cookie error!" + e.getMessage());
            }

            LoginTicket.remove();
            removeTicketCookie(request, response);
        }
    }

    /**
     * 删除 ticket cookie
     *
     * @param request
     * @param response
     */
    protected void removeTicketCookie(HttpServletRequest request, HttpServletResponse response) {
        if (ticketCookie == null)
            return;

        CookieUtils.removeCookie(request, response, ticketCookie.getName(), true);
    }
}
