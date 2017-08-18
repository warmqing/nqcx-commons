/*
 * Copyright 2017  ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */
package org.nqcx.commons.solrcloud;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangqing 17/8/18 下午1:55
 */
public class SolrCloudClientFactory {
    private String zkHost; // the zk server connect string
    private int zkConnectTimeout = 10000;
    private int zkClientTimeout = 10000;
    private String defaultCollection;
    private final Map<String, CloudSolrClient> clientMap = new HashMap<String, CloudSolrClient>();

    public CloudSolrClient getCloudSolrClient(SolrCollection collection, boolean newInstance) {

        if (newInstance)
            return fillClientParams(new CloudSolrClient(zkHost), collection);

        if (!clientMap.containsKey(collection.getColletion()))
            clientMap.put(collection.getColletion(), fillClientParams(new CloudSolrClient(zkHost), collection));

        return clientMap.get(collection.getColletion());
    }

    private CloudSolrClient fillClientParams(CloudSolrClient client, SolrCollection collection) {
        if(client == null)
            return null;
        client.setZkConnectTimeout(zkConnectTimeout);
        client.setZkClientTimeout(zkClientTimeout);
        client.setDefaultCollection(collection == null ? null : collection.getColletion());
        return client;
    }

    public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    public int getZkConnectTimeout() {
        return zkConnectTimeout;
    }

    public void setZkConnectTimeout(int zkConnectTimeout) {
        this.zkConnectTimeout = zkConnectTimeout;
    }

    public int getZkClientTimeout() {
        return zkClientTimeout;
    }

    public void setZkClientTimeout(int zkClientTimeout) {
        this.zkClientTimeout = zkClientTimeout;
    }

    public String getDefaultCollection() {
        return defaultCollection;
    }

    public void setDefaultCollection(String defaultCollection) {
        this.defaultCollection = defaultCollection;
    }
}
