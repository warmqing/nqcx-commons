/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.mq;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author naqichuan 15/1/3 12:42
 */
public class NqcxJmsTemplate extends JmsTemplate {

    public void send(NqcxDestination destination, MessageCreator messageCreator) throws JmsException {
        super.send(destination.getDestination(), messageCreator);
    }
}
