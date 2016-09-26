/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

/**
 * @author naqichuan 15/1/6 23:41
 */
public class SolrSupportException extends RuntimeException {

    public SolrSupportException() {
        super();
    }

    public SolrSupportException(String msg) {
        super(msg);
    }

    public SolrSupportException(Exception e) {
        super(e);
    }

    public SolrSupportException(String msg, Exception e) {
        super(msg, e);
    }

    public SolrSupportException(Throwable cause) {
        super(cause);
    }
}
