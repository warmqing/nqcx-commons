/*
 * Copyright 2017 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.solrcloud;

/**
 * @author wangqing 17/8/18 上午11:05
 */
public enum SolrCollection {
     BOOK("book");

    private String colletion;

    private SolrCollection(String colletion) {
        this.colletion = colletion;
    }

    public String getColletion() {
        return colletion;
    }
}
