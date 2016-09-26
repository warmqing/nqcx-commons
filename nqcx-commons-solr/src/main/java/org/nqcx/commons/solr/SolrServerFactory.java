/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naqichuan 15/1/5 22:55
 */
public class SolrServerFactory {

    private final static String DEFAULT_NO_CORE_KEY = "_default";
    private final Map<String, HttpSolrServer> httpSolrServerMap = new HashMap<String, HttpSolrServer>();

    public String solrUrl = "";
    public int connectionTimeout = 100;
    public int soTimeout = 15000;
    public int defaultMaxConnectionsPerHost = 100;
    public int maxTotalConnection = 100;
    public boolean followRedirects = false;
    public boolean allowCompression = true;

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
        this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
    }

    public void setMaxTotalConnection(int maxTotalConnection) {
        this.maxTotalConnection = maxTotalConnection;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public void setAllowCompression(boolean allowCompression) {
        this.allowCompression = allowCompression;
    }

    /**
     * 取得无 core server
     *
     * @return
     */
    public HttpSolrServer getHttpSolrServer() {
        if (!httpSolrServerMap.containsKey(DEFAULT_NO_CORE_KEY))
            httpSolrServerMap.put(DEFAULT_NO_CORE_KEY, fillServerParams((new HttpSolrServer(solrUrl))));

        return httpSolrServerMap.get(DEFAULT_NO_CORE_KEY);
    }

    /**
     * 取得有 core server
     *
     * @param core
     * @return
     */
    public HttpSolrServer getHttpSolrServer(SolrServerCore core) {

        return getHttpSolrServer(core, false);
    }

    /**
     * 取得 core server
     *
     * @param core
     * @param newInstance
     * @return
     */
    public HttpSolrServer getHttpSolrServer(SolrServerCore core, boolean newInstance) {
        if (core == null)
            return getHttpSolrServer();

        if (newInstance)
            return fillServerParams(new HttpSolrServer(solrUrl + "/" + core.getCore()));

        if (!httpSolrServerMap.containsKey(core.getCore()))
            httpSolrServerMap.put(core.getCore(), fillServerParams(new HttpSolrServer(solrUrl + "/" + core.getCore())));

        return httpSolrServerMap.get(core.getCore());
    }

    /**
     * 填充其它参数
     *
     * @param server
     * @return
     */
    private HttpSolrServer fillServerParams(HttpSolrServer server) {
        server.setConnectionTimeout(connectionTimeout);
        server.setSoTimeout(soTimeout);
        server.setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
        server.setMaxTotalConnections(maxTotalConnection);
        server.setFollowRedirects(followRedirects);
        server.setAllowCompression(allowCompression);

        return server;
    }
}
