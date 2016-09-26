/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author naqichuan 15/1/6 14:10
 */
public abstract class SolrIndexSupport extends SolrSupport {

    @Override
    public abstract SolrServerCore getServerCore();

    /**
     * @param doc
     */
    public void add(SolrInputDocument doc) {
        try {
            HttpSolrServer server = this.getServer();
            server.add(doc);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport add error", e);
        }
    }

    /**
     * @param docs
     */
    public void addMulti(SolrInputDocument... docs) {
        addMulti(Arrays.asList(docs));
    }

    /**
     * @param docs
     */
    public void addMulti(Collection<SolrInputDocument> docs) {
        try {
            HttpSolrServer server = this.getServer();
            server.add(docs);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport addMulti error", e);
        }
    }

    /**
     * @param bean
     * @param <T>
     */
    public <T> void addBean(T bean) {
        try {
            HttpSolrServer server = this.getServer();
            server.addBean(bean);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport addBean error", e);
        }
    }

    /**
     * @param beans
     * @param <T>
     */
    public <T> void addBeans(T... beans) {
        addBeans(Arrays.asList(beans));
    }

    /**
     * @param beans
     * @param <T>
     */
    public <T> void addBeans(Collection<T> beans) {
        try {
            HttpSolrServer server = this.getServer();
            server.addBeans(beans);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport addBeans error", e);
        }
    }

    /**
     * @param id
     */
    public void deleteOne(String id) {
        try {
            HttpSolrServer server = this.getServer();
            server.deleteById(id);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport deleteOne error", e);
        }
    }

    /**
     * @param ids
     */
    public void deleteMulti(List<String> ids) {
        try {
            HttpSolrServer server = this.getServer();
            server.deleteById(ids);
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport deleteMulti error", e);
        }
    }

    /**
     * 删除全部
     */
    public void deleteAll() {
        try {
            HttpSolrServer server = this.getServer();
            server.deleteByQuery("*:*");
            server.optimize();
        } catch (Exception e) {
            throw new SolrSupportException("SolrIndexSupport deleteAll error", e);
        }
    }
}
