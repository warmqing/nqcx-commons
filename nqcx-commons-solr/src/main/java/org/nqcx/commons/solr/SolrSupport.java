/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 * @author naqichuan 15/1/6 14:34
 */
public abstract class SolrSupport {

    /**
     * 格式化查询字符串正则表达式
     */
    public final static String QUERY_STRING_FORMAT_REGEX = "[`~!@#$%^&*()-+=|{}\':;\',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 取得 core
     *
     * @return
     */
    public abstract SolrServerCore getServerCore();

    /**
     * 设置 factory
     *
     * @return
     */
    public abstract SolrServerFactory getSolrServerFactory();

    /**
     * 取得 server
     *
     * @return
     */
    public HttpSolrServer getServer() {
        return this.getSolrServerFactory() == null ? null : this.getSolrServerFactory().getHttpSolrServer(getServerCore());
    }
}
