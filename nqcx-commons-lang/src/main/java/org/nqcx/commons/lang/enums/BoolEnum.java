/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public enum BoolEnum {

	FALSE(0, "否"), TRUE(1, "是");

	private int value;
	private String text;

	private BoolEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static BoolEnum get(int value) {
		for (BoolEnum p : BoolEnum.values()) {
			if (p.getValue() == value)
				return p;
		}
		throw new IllegalArgumentException("unknown value:" + value);
	}
}
