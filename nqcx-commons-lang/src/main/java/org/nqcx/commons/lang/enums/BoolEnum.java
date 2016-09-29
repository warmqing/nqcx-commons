/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * @author naqichuan 2014年8月14日 上午11:05:32
 */
public enum BoolEnum implements EnumInterface {

    FALSE(0, false, "否"), TRUE(1, true, "是");

    private int value;
    private boolean bool;
    private String text;

    BoolEnum(int value, boolean bool, String text) {
        this.value = value;
        this.bool = bool;
        this.text = text;
    }

    @Override
    public int getValue() {
        return value;
    }

    public boolean isTrue() {
        return bool;
    }

    @Override
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
