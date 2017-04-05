/*
 * Copyright 2017 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * @author naqichuan 17/4/4 13:48
 */
public enum StatusEO implements EnumIO {

    NONE(0, "无"), AVAILABLE(1, "可用"), DISABLE(2, "不可用"), DELETED(3, "已删除");

    private int value;
    private String text;

    StatusEO(int value, String text) {
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

    /**
     * 判断自身是否与参数里的枚举相等
     *
     * @param statusEO statusEO
     * @return boolean
     */
    public boolean is(StatusEO statusEO) {
        return this == statusEO;
    }

    /**
     * 通过 value 取得枚举实例
     *
     * @param value
     * @return
     */
    public static StatusEO get(int value) {
        for (StatusEO p : StatusEO.values()) {
            if (p.getValue() == value)
                return p;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}
