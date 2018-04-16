/*
 * Copyright 2014 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.interceptor;

import org.nqcx.commons.web.login.LoginTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginTicketRequiredInterceptor extends LoginRequiredInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginTicketRequiredInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginTicket ticket = getLoginTicket();

        if (ticket == null || ticket.getAccount() == null || ticket.getAccount().length() == 0) {
            if (isAjax()) {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from ajax check ticket false!");

                responseJsonResult(response, NEED_LOGIN_JSON);
            } else {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from normal way check ticket false!");

                response.sendRedirect(getLoginUrl(request));
            }

            return false;
        }

        logger.info("Account [" + ticket.getAccount() + "] check ticket true!");

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // ignore
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // ignore
    }

    /**
     * 取出Ticket的信息
     *
     * @return
     */
    protected LoginTicket getLoginTicket() {
        return LoginTicket.getTicket();
    }
}
