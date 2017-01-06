/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.mq;

import org.nqcx.commons.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * @author naqichuan 15/1/3 12:43
 */
public class MqSender extends MqSenderBlank {

    private final static Logger logger = LoggerFactory.getLogger(MqConst.MQ_LOG_NAME);

    protected MqJmsTemplate mqJmsTemplate;

    public MqSender(MqJmsTemplate mqJmsTemplate) {
        this.mqJmsTemplate = mqJmsTemplate;
    }

    @Override
    public void send(MqDestination destination, final Serializable object) {

        logger.info("MqSender -> OPERATE: SEND, object: {}", object);

        mqJmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createObjectMessage(JsonUtils.objectToJson(object));
                } catch (Exception e) {
                    logger.error("Message send failed", e);
                }
                return null;
            }
        });
    }
}
