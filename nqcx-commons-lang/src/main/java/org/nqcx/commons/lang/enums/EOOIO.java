/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.enums;

/**
 * Enum object operate interface object
 *
 * @author naqichuan 16/10/9 15:05
 */
public interface EOOIO {

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos
     * @return EOOUtils.contain(eos, this);
     */
    <T> boolean in(T[] eos);
}
