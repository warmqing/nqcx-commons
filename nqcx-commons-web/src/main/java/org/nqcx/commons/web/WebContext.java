/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContext {

    private final static ThreadLocal<WebContext> holder = new ThreadLocal<WebContext>() {

        @Override
        protected WebContext initialValue() {
            return new WebContext();
        }
    };

    private boolean ajax = false;
    private String remoteAddr;

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public static void setWebContext(WebContext webContext) {
        holder.set(webContext);
    }

    public static WebContext getWebContext() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }

}
