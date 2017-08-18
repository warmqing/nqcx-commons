/*
 * Copyright 2017  ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */
package org.nqcx.commons.solrcloud;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

import java.util.Collection;
import java.util.List;

/**
 * @author wangqing 17/8/17 下午3:36
 */
public abstract class SolrCloudSupport {

    public abstract SolrCollection getSolrCollection();

    public abstract SolrCloudClientFactory getSolrCloudClientFactory();

    public CloudSolrClient getClient() {
        return this.getSolrCloudClientFactory() == null ? null : this.getSolrCloudClientFactory().getCloudSolrClient(getSolrCollection(), false);
    }


    public <T> void addBean(T bean) {
        try {
            getClient().addBean(bean);
            getClient().optimize();
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport addBean error", e);
        }
    }


    /**
     * @param beans
     * @param <T>
     */
    public <T> void addBeans(Collection<T> beans) {
        try {
            getClient().addBeans(beans);
            getClient().optimize();
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrIndexSupport addBeans error", e);
        }
    }

    /**
     * @param id
     */
    public void deleteOne(String id) {
        try {
            getClient().deleteById(id);
            getClient().optimize();
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrIndexSupport deleteOne error", e);
        }
    }

    /**
     * @param ids
     */
    public void deleteMulti(List<String> ids) {
        try {
            getClient().deleteById(ids);
            getClient().optimize();
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrIndexSupport deleteMulti error", e);
        }
    }

    /**
     * 删除全部
     */
    public void deleteAll() {
        try {
            getClient().deleteByQuery("*:*");
            getClient().optimize();
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrIndexSupport deleteAll error", e);
        }
    }


}
