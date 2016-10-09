/*
 * Copyright 2014 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * Gender enum object
 *
 * @author naqichuan 14-10-11 9:23
 */
public enum GenderEO implements EnumIO {

    NONE(0, ""), MALE(1, "男"), FEMALE(2, "女");

    private int value;
    private String text;

    GenderEO(int value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    public static GenderEO get(int value) {
        for (GenderEO p : GenderEO.values()) {
            if (p.getValue() == value)
                return p;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}
