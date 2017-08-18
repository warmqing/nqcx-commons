/*
 * Copyright 2017 ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */

package org.nqcx.commons.solrcloud;

/**
 * @Author Jiangsiqi on 2017/8/17 14:44.
 */
public class SolrNull {

    private boolean isNull = true;

    public SolrNull(boolean value) {
        isNull = value;
    }

    public boolean isTrue(){
        return isNull;
    }


}
