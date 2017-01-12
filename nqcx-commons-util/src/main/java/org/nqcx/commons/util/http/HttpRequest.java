/*
 * Copyright 2015 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.util.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.nqcx.commons.lang.consts.LoggerConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author nqcx 2013-10-22 下午2:13:57
 */
public class HttpRequest {

    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final static Logger http_logger = LoggerFactory.getLogger(LoggerConst.LOGGER_HTTP_NAME);

    /**
     * 创建 http client
     *
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author nqcx 2013-10-22 下午2:13:57
     */
    private static HttpClient newHttpClient(int connectionTimeout,
                                            int socketTimeout) {
        HttpClient httpClient = new DefaultHttpClient();

        if (connectionTimeout > 0) {
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        }
        if (socketTimeout > 0)
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, socketTimeout);

        return httpClient;
    }

    /**
     * get
     *
     * @param uri
     * @param httpMap
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String get(String uri, HttpMap httpMap) {
        return get(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri
     * @param map
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String get(String uri, Map<String, Object> map) {
        return get(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri
     * @param map
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author 黄保光 Nov 6, 2013 3:40:06 PM
     */
    public static String get(String uri, Map<String, Object> map,
                             String chareset, int connectionTimeout, int socketTimeout) {
        return get(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * @param uri
     * @param httpMap
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author nqcx 2013-10-22 下午2:13:57
     * @author 黄保光 Nov 6, 2013 3:40:09 PM
     */
    public static String get(String uri, HttpMap httpMap, String chareset,
                             int connectionTimeout, int socketTimeout) {
        HttpClient httpClient = newHttpClient(connectionTimeout, socketTimeout);
        String responseBody = null;
        try {
            String paramString = null;
            if (httpMap != null)
                paramString = httpMap.buildString();

            http_logger.info("reqeust: get, uri: " + uri + ", params: ["
                    + paramString + "]");

            if (paramString != null && paramString.length() > 0)
                uri += "?" + paramString;

            HttpGet httpGet = new HttpGet(uri);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {
                responseBody = EntityUtils.toString(entity, chareset);
                EntityUtils.consume(entity);
                return responseBody;
            }

            EntityUtils.consume(entity);
            responseBody = "HttpStatus "
                    + response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            responseBody = e.toString();
            logger.error("response: get, error: ", e);
        } catch (IOException e) {
            responseBody = e.toString();
            logger.error("response: get, error: ", e);
        } finally {
            http_logger.info("response: get, content: " + responseBody);
            httpClient.getConnectionManager().shutdown();
        }

        return responseBody;
    }

    /**
     * post
     *
     * @param uri
     * @param map
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String post(String uri, Map<String, Object> map) {
        return post(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri
     * @param httpMap
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String post(String uri, HttpMap httpMap) {
        return post(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri
     * @param map
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     */
    public static String post(String uri, Map<String, Object> map,
                              String chareset, int connectionTimeout, int socketTimeout) {
        return post(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * post
     *
     * @param uri
     * @param httpMap
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author 黄保光 Nov 6, 2013 3:40:06 PM
     */
    public static String post(String uri, HttpMap httpMap,
                              String chareset, int connectionTimeout, int socketTimeout) {
        List<NameValuePair> nvps = null;
        if (httpMap != null)
            nvps = httpMap.buildList();

        try {
            http_logger.info("reqeust: post, uri:" + uri + ", params: " + nvps);

            return post(uri, chareset, new UrlEncodedFormEntity(nvps, chareset),
                    connectionTimeout, socketTimeout);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * post
     *
     * @param uri
     * @param map
     * @return
     */
    public static String postJson(String uri, Map<String, Object> map) {
        return postJson(uri, HttpMap.newInstance().put(map));
    }

    /**
     * post
     *
     * @param uri
     * @param httpMap
     * @return
     * @author 黄保光 Nov 6, 2013 3:40:06 PM
     */
    public static String postJson(String uri, HttpMap httpMap) {

        String params = httpMap.buildJson();
        http_logger.info("reqeust: post, uri:" + uri + ", params: " + params);

        try {
            StringEntity requestEntity = new StringEntity(params, Consts.UTF_8.toString());
            requestEntity.setContentEncoding(Consts.UTF_8.toString());
            requestEntity.setContentType("application/json");

            return post(uri, Consts.UTF_8.toString(), requestEntity,
                    HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * post
     *
     * @param uri
     * @param chareset
     * @param requestEntity
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     */
    private static String post(String uri, String chareset, HttpEntity requestEntity,
                               int connectionTimeout, int socketTimeout) {
        HttpClient httpClient = newHttpClient(connectionTimeout, socketTimeout);
        String responseBody = null;
        try {
            HttpPost httpost = new HttpPost(uri);
            httpost.setEntity(requestEntity);

            HttpResponse response = httpClient.execute(httpost);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {
                responseBody = EntityUtils.toString(entity, chareset);
                EntityUtils.consume(entity);
                return responseBody;
            }

            EntityUtils.consume(entity);
            responseBody = "HttpStatus "
                    + response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            responseBody = e.toString();
            logger.error("response: post, error: ", e);
        } catch (IOException e) {
            responseBody = e.toString();
            logger.error("response: post, error: ", e);
        } catch (Exception e) {
            responseBody = e.toString();
            logger.error("response: post, error: ", e);
        } finally {
            logger.info("response: post, content: " + responseBody);
            httpClient.getConnectionManager().shutdown();
        }

        return responseBody;
    }

//    public String postJson(String uri, HttpMap httpMap, String chareset,
//                           int connectionTimeout, int socketTimeout) {
//
//    }
}
