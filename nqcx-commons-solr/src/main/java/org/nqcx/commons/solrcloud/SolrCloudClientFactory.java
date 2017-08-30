/*
 * Copyright 2017 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
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
    private String defaultCollection = "_default";
    private final Map<String, CloudSolrClient> clientMap = new HashMap<String, CloudSolrClient>();

    /**
     * 获取solrcoud客户端
     *
     * @param collection
     * @param newInstance
     * @return
     */
    public CloudSolrClient getCloudSolrClient(SolrCollection collection, boolean newInstance) {

        if (newInstance)
            return fillClientParams(new CloudSolrClient(zkHost), collection);

        String key = (collection == null ? defaultCollection : collection.getColletion());
        if (!clientMap.containsKey(key))
            clientMap.put(key, fillClientParams(new CloudSolrClient(zkHost), collection));

        return clientMap.get(key);
    }

    /**
     * 设置client参数
     *
     * @param client
     * @param collection
     * @return
     */
    private CloudSolrClient fillClientParams(CloudSolrClient client, SolrCollection collection) {
        if (client == null)
            return null;
        client.setZkConnectTimeout(zkConnectTimeout);
        client.setZkClientTimeout(zkClientTimeout);
        client.setDefaultCollection(collection == null ? defaultCollection : collection.getColletion());
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
