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
    private final static Map<String, HttpSolrServer> httpSolrServerMap = new HashMap<String, HttpSolrServer>();

    public static String SOLR_URL = "";
    public static int CONNECTION_TIMEOUT = 100;
    public static int SO_TIMEOUT = 15000;
    public static int DEFAULT_MAX_CONNECTIONS_PER_HOST = 100;
    public static int MAX_TOTAL_CONNECTION = 100;
    public static boolean FOLLOW_REDIRECTS = false;
    public static boolean ALLOW_COMPRESSION = true;

    public void setSolrUrl(String solrUrl) {
        SOLR_URL = solrUrl;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        CONNECTION_TIMEOUT = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        SO_TIMEOUT = soTimeout;
    }

    public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
        DEFAULT_MAX_CONNECTIONS_PER_HOST = defaultMaxConnectionsPerHost;
    }

    public void setMaxTotalConnection(int maxTotalConnection) {
        MAX_TOTAL_CONNECTION = maxTotalConnection;
    }

    public void setFollowRedirects(boolean followRedirects) {
        FOLLOW_REDIRECTS = followRedirects;
    }

    public void setAllowCompression(boolean allowCompression) {
        ALLOW_COMPRESSION = allowCompression;
    }

    /**
     * 取得无 core server
     *
     * @return
     */
    public static HttpSolrServer getHttpSolrServer() {
        if (!httpSolrServerMap.containsKey(DEFAULT_NO_CORE_KEY))
            httpSolrServerMap.put(DEFAULT_NO_CORE_KEY, fillServerParams((new HttpSolrServer(SOLR_URL))));

        return httpSolrServerMap.get(DEFAULT_NO_CORE_KEY);
    }

    /**
     * 取得有 core server
     *
     * @param core
     * @return
     */
    public static HttpSolrServer getHttpSolrServer(SolrServerCore core) {
        if (core == null)
            return getHttpSolrServer();

        if (!httpSolrServerMap.containsKey(core.getCore()))
            httpSolrServerMap.put(core.getCore(), fillServerParams(new HttpSolrServer(SOLR_URL + "/" + core.getCore())));

        return httpSolrServerMap.get(core.getCore());
    }

    /**
     * 填充其它参数
     *
     * @param server
     * @return
     */
    private static HttpSolrServer fillServerParams(HttpSolrServer server) {
        server.setConnectionTimeout(CONNECTION_TIMEOUT);
        server.setSoTimeout(SO_TIMEOUT);
        server.setDefaultMaxConnectionsPerHost(DEFAULT_MAX_CONNECTIONS_PER_HOST);
        server.setMaxTotalConnections(MAX_TOTAL_CONNECTION);
        server.setFollowRedirects(FOLLOW_REDIRECTS);
        server.setAllowCompression(ALLOW_COMPRESSION);

        return server;
    }
}
