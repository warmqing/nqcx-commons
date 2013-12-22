/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.controller;

import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.nqcx.commons.web.login.LoginContext;
import org.nqcx.commons.web.login.LoginTicket;
import org.nqcx.commons.web.resource.ResourceConfig;
import org.nqcx.commons.web.result.Result;
import org.nqcx.commons.web.result.ResultConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * 
 * @author nqcx 2013-4-8 下午4:07:04
 * 
 */
public abstract class MainSupport {

	protected final static String SUCCESS = "success";
	protected final static String ERROR_CODE_PREFIX = "errorCode";
	protected final static String CHARSETNAME = "UTF-8";

	@Autowired
	private ResourceConfig resourceConfig;
	@Autowired
	private ResultConfig resultConfig;
	@Autowired
	private MessageSource messageSource;

	// @ModelAttribute("configValues")
	// protected Map<String, String> getConfigValues() {
	// return resourceConfig.getConfigValues();
	// }

	protected String getConfig(String key) {
		if (resourceConfig == null || resourceConfig.getConfigValues() == null)
			return null;
		return resourceConfig.getConfigValues().get(key);
	}

	protected LoginContext getLoginContext() {
		return LoginContext.getLoginContext();
	}

	protected LoginTicket getLoginTicket() {
		return LoginTicket.getTicket();
	}

	protected String m(String code) {
		Result result = getResult(ResultConfig.M, code);
		return result == null ? "" : getValue(result.getSubject());
	}

	protected String e(String code) {
		Result result = getResult(ResultConfig.E, code);
		return result == null ? "" : getValue(result.getSubject());
	}

	protected String s(String code) {
		Result result = getResult(ResultConfig.S, code);
		return result == null ? "" : getValue(result.getSubject());
	}

	/**
	 * 取得配置文件中的Result
	 * 
	 * @param type
	 * @param code
	 * @return
	 */
	protected Result getResult(String type, String code) {
		return resultConfig.getResult(type, code);
	}

	/**
	 * 从 properties 中取值
	 * 
	 * @param code
	 * @return
	 */
	protected String getValue(String code) {
		return getValue(code, null);
	}

	/**
	 * 从 properties 中取值
	 * 
	 * @param code
	 * @param arguments
	 * @return
	 */
	protected String getValue(String code, Object[] arguments) {
		String rv = null;
		try {
			rv = messageSource.getMessage(code, arguments, null);
		} catch (NoSuchMessageException e) {
			// nothing to do.
		}
		return rv == null ? code : rv;
	}

	/**
	 * 将 map 中以 errorCode 开头的 key 对应的 value 转换成实际的说明，并放入到 JSONObject 对象中
	 * 
	 * @param jsonObject
	 * @param map
	 */
	protected JSONObject putErrorToJson(JSONObject jsonObject,
			Map<String, Object> map) {
		if (map != null) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				if (key == null || !key.startsWith(ERROR_CODE_PREFIX))
					continue;

				Object o = map.get(key);
				putErrorToJson(jsonObject, key, (String) o);
			}
		}
		return jsonObject;
	}

	/**
	 * 将 map 中以 errorCode 开头的 key 对应的 value 转换成实际的说明，并放入到 JSONObject 对象中
	 * 
	 * @param map
	 * @return
	 */
	protected JSONObject putErrorToJson(Map<String, Object> map) {
		return putErrorToJson(new JSONObject(), map);
	}

	/**
	 * 向 JSON 中添加错误信息，同时转换错误代码为说明
	 * 
	 * @param jsonObject
	 * @param key
	 * @param value
	 * @return
	 */
	protected JSONObject putErrorToJson(JSONObject jsonObject, String key,
			String value) {
		if (jsonObject != null && key != null) {
			putValueToJson(jsonObject, key, value);
			putValueToJson(jsonObject, key + "Text", e(value));
		}
		return jsonObject;
	}

	/**
	 * 向 JSON 中添加错误信息，同时转换错误代码为说明
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected JSONObject putErrorToJson(String key, String value) {
		return putErrorToJson(new JSONObject(), key, value);
	}

	/**
	 * 向 JSON 中添加信息
	 * 
	 * @param jsonObject
	 * @param key
	 * @param value
	 * @return
	 */
	protected JSONObject putValueToJson(JSONObject jsonObject, String key,
			Object value) {
		if (jsonObject != null && key != null) {
			jsonObject.put(key, value);
		}
		return jsonObject;
	}

	/**
	 * 向 JSON 中添加信息
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected JSONObject putValueToJson(String key, Object value) {
		return putValueToJson(new JSONObject(), key, value);
	}
}
