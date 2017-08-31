/*
 * Copyright 2017 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.solrcloud;

import org.nqcx.commons.lang.o.EntityBO;

import java.util.Date;

/**
 * @Author Jiangsiqi on 2017/8/17 15:35.
 */
public class SolrDate extends EntityBO {

    private Date begintime;

    private Date endtime;

    public SolrDate(Date begintime, Date endtime) {
        this.begintime = begintime;
        this.endtime = endtime;
    }

    public Date getBegintime() {
        return begintime;
    }

    public Date getEndtime() {
        return endtime;
    }
}
