/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.cookie;


import org.nqcx.commons.util.NqcxStringUtils;

import javax.servlet.http.Cookie;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class NqcxCookie {

    /**
     * Cookie 加解密工具
     */
    private CookieCipherTools cookieCipherTools;
    /**
     * cookie名称
     */
    private String name;
    /**
     * cookie域名
     */
    private String domain;
    /**
     * cookie路径
     */
    private String path;
    /**
     * cookie默认时限
     */
    private int expiry;
    /**
     * cookie键
     * 
     * @see #encrypt
     */
    private String key;
    /**
     * 是否加密cookie
     * 
     * @see #key
     */
    private boolean encrypt;

    public Cookie newCookie(String value) {
        String newValue;
        if (!NqcxStringUtils.isEmpty(value)) {
            newValue = isEncrypt() ? cookieCipherTools.encrypt(value, getKey()) : value;
        } else {
            newValue = value;
        }
        Cookie cookie = new Cookie(name, newValue);
        if (!NqcxStringUtils.isBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (!NqcxStringUtils.isBlank(path)) {
            cookie.setPath(path);
        }
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        return cookie;
    }

    public Cookie newCookie(String value, int expiry) {
        String newValue;
        if (!NqcxStringUtils.isEmpty(value)) {
            newValue = isEncrypt() ? cookieCipherTools.encrypt(value, getKey()) : value;
        } else {
            newValue = value;
        }
        Cookie cookie = new Cookie(name, newValue);
        if (!NqcxStringUtils.isBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (!NqcxStringUtils.isBlank(path)) {
            cookie.setPath(path);
        }
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public Cookie newCookie(String name, String value, int expiry) {
        String newValue;
        if (!NqcxStringUtils.isEmpty(value)) {
            newValue = isEncrypt() ? cookieCipherTools.encrypt(value, getKey()) : value;
        } else {
            newValue = value;
        }
        Cookie cookie = new Cookie(name, newValue);
        if (!NqcxStringUtils.isBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (!NqcxStringUtils.isBlank(path)) {
            cookie.setPath(path);
        }
        cookie.setMaxAge(expiry);
        return cookie;
    }

    public Cookie newCookie(String name, String value) {
        String newValue;
        if (!NqcxStringUtils.isEmpty(value)) {
            newValue = isEncrypt() ? cookieCipherTools.encrypt(value, getKey()) : value;
        } else {
            newValue = value;
        }
        Cookie cookie = new Cookie(name, newValue);
        if (!NqcxStringUtils.isBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (!NqcxStringUtils.isBlank(path)) {
            cookie.setPath(path);
        }
        cookie.setMaxAge(-1);
        return cookie;
    }

    public String getValue(String value) {
        if (!NqcxStringUtils.isEmpty(value)) {
            return isEncrypt() ? cookieCipherTools.decrypt(value, getKey()) : value;
        } else {
            return value;
        }
    }

    public String getValue(String value, boolean isEncrypt) {
        if (!NqcxStringUtils.isEmpty(value)) {
            return isEncrypt ? cookieCipherTools.decrypt(value, getKey()) : value;
        } else {
            return value;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public void setCookieCipherTools(CookieCipherTools cookieCipherTools) {
        this.cookieCipherTools = cookieCipherTools;
    }
}
