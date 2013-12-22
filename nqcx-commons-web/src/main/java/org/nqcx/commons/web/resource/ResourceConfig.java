/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.resource;

import java.util.Map;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public class ResourceConfig {

	private Map<String, String> configValues;

	public Map<String, String> getConfigValues() {
		return configValues;
	}

	public void setConfigValues(Map<String, String> configValues) {
		this.configValues = configValues;
	}

	public String getConfigValue(String key) {
		return configValues == null ? null : configValues.get(key);
	}
}
