/*
 * Copyright 2017 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.mq;

import org.nqcx.commons.lang.consts.LoggerConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author naqichuan 17/1/6 09:42
 */
public class MqSenderBlank {

    private final static Logger logger = LoggerFactory.getLogger(LoggerConst.LOGGER_MQ_NAME);

    public void send(MqDestination destination, final Serializable object) {

        logger.info("MqSenderBlank -> OPERATE: SEND, object: {}", object);

    }
}
