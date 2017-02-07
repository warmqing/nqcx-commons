/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.url;

import org.nqcx.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class UrlBuilder {

    // 生成 url 时根据 index 对应的值替换占位符
    private final static ThreadLocal<List<String>> values = new ThreadLocal<List<String>>() {
        @Override
        protected List<String> initialValue() {
            return new ArrayList<String>(0);
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(UrlBuilder.class);

    private final URL baseUrl; // url 可以有用占位符，如: http://{0}.{1}.nqcx.org
    private final boolean ignoreEmpty;
    private final Charset charset;
    private final Map<String, Object> queryMap;

    public UrlBuilder(final String baseUrl) {
        this(baseUrl, Charset.defaultCharset().name(), true);
    }

    public UrlBuilder(final String baseUrl, final String charsetName) {
        this(baseUrl, charsetName, true);
    }

    public UrlBuilder(final String baseUrl, final String charsetName, final boolean ignoreEmpty) {
        try {
            this.baseUrl = new URL(baseUrl);
            this.ignoreEmpty = ignoreEmpty;
            this.charset = Charset.forName(charsetName);
            String queryString = this.baseUrl.getQuery();
            if (StringUtils.isNotEmpty(queryString))
                queryMap = new LinkedHashMap<String, Object>(parseQuery(queryString));
            else
                queryMap = Collections.emptyMap();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param query
     * @return
     */
    private Map<String, Object> parseQuery(String query) {
        String[] params = query.split("&");
        Map<String, Object> map = new LinkedHashMap<String, Object>(params.length);
        for (String param : params) {
            String[] strings = param.split("=");
            String name = strings[0];
            String value = null;
            if (strings.length > 1) {
                // 需要对数值进行 decode
                value = decodeValue(strings[1]);
            }
            map.put(name, value);
        }
        return map;
    }

    /**
     * @param value
     * @return
     */
    private String decodeValue(String value) {
        try {
            if (value != null && value.length() > 0)
                return URLEncoder.encode(value, this.charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
            logger.warn(e.getMessage());
        }
        return value;
    }

    /**
     * @return
     */
    public Builder forPath() {
        return forPath(null);
    }

    /**
     * @param path
     * @return
     */
    public Builder forPath(String path) {
        return new Builder(baseUrl, path, charset.name(), ignoreEmpty, queryMap);
    }

    /**
     * @param _values
     */
    public static void setValues(List<String> _values) {
        if (values == null || _values.size() == 0)
            values.get().clear();
        else
            values.set(_values);
    }

    /**
     * @param _values
     */
    public static void addValues(List<String> _values) {
        if (values != null && _values.size() > 0)
            values.get().addAll(_values);
    }

    /**
     * @param value
     */
    public static void setValue(String value) {
        values.get().clear();
        if (value != null)
            values.get().add(value);
    }

    /**
     * @param value
     */
    public static void addValue(String value) {
        if (value != null)
            values.get().add(value);
    }

    /**
     */
    public static class Builder {

        final URL baseUrl;
        String path;
        String charsetName;
        boolean ignoreEmpty;
        final Map<String, Object> urlParams;

        Builder(URL baseUrl, String path, String charsetName, boolean ignoreEmpty, Map<String, Object> queryMap) {
            this.baseUrl = baseUrl;
            this.path = path;
            this.charsetName = charsetName;
            this.ignoreEmpty = ignoreEmpty;
            this.urlParams = new LinkedHashMap<String, Object>(queryMap);
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setCharsetName(String charsetName) {
            this.charsetName = charsetName;
            return this;
        }

        public Builder setIgnoreEmpty(boolean ignoreEmpty) {
            this.ignoreEmpty = ignoreEmpty;
            return this;
        }

        /**
         * 取得参数表
         *
         * @return
         */
        public Map<String, Object> getParamMap() {
            return new HashMap<String, Object>(this.urlParams);
        }

        /**
         * @return
         */
        public String build() {
            String path = prefixPath(baseUrl.getPath(), this.path);
            int port = baseUrl.getPort();
            if (baseUrl.getPort() == baseUrl.getDefaultPort())
                port = -1;

            String host = baseUrl.getHost();
            if (StringUtils.isNotBlank(host) && host.startsWith("."))
                host = host.substring(1);

            final StringBuilder builder;
            try {
                builder = new StringBuilder(new URL(baseUrl.getProtocol(), host, port, path).toString());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            StringBuilder query = new StringBuilder();
            for (Entry<String, Object> entry : urlParams.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null)
                    continue;

                if (value instanceof Object[]) {
                    for (final Object v : (Object[]) value) {
                        appendQueryString(key, v, query);
                    }
                } else if (value instanceof Collection) {
                    for (final Object v : (Collection<?>) value) {
                        appendQueryString(key, v, query);
                    }
                } else
                    appendQueryString(key, value, query);
            }
            if (query.length() > 0)
                query.replace(0, 1, "?");
            builder.append(query);

            if (values.get() != null && values.get().size() > 0)
                return MessageFormat.format(builder.toString(), values.get().toArray());
            return builder.toString();
        }

        /**
         * @param key
         * @param v
         * @param sb
         */
        void appendQueryString(String key, Object v, StringBuilder sb) {
            if (v == null)
                return;

            String value = String.valueOf(v);
            if (ignoreEmpty && StringUtils.isBlank(value))
                return;

            sb.append("&").append(key).append("=").append(encodeUrl(value));
        }

        /**
         * @param value
         * @return
         */
        String encodeUrl(String value) {
            try {
                return URLEncoder.encode(value, StringUtils.isNotBlank(charsetName) ? charsetName : Charset.defaultCharset().name());
            } catch (UnsupportedEncodingException e) {
                // Nothing to do
                logger.warn(e.getMessage());
            }
            return value;
        }

        /**
         * @param contextPath
         * @param path
         * @return
         */
        String prefixPath(String contextPath, String path) {
            if (path == null && contextPath == null)
                return "/";
            else if (path == null)
                return contextPath;
            else if (contextPath == null)
                return path;
            else if (path.startsWith("/") && contextPath.endsWith("/"))
                return contextPath + path.substring(1);
            else
                return contextPath + path;
        }

        /**
         * @param container
         * @param o
         */
        void append(List<Object> container, Object o) {
            if (o instanceof Object[]) {
                for (Object e : (Object[]) o) {
                    container.add(e);
                }
            } else if (o instanceof Collection)
                container.addAll((Collection<?>) o);
            else
                container.add(o);
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder add(final String key, final Object value) {
            Object newValue;
            if (urlParams.containsKey(key)) {
                Object o = urlParams.get(key);
                if (o == null) {
                    newValue = value;
                } else {
                    List<Object> container = new LinkedList<Object>();
                    append(container, o);
                    append(container, value);
                    newValue = container;
                }
            } else {
                newValue = value;
            }
            urlParams.put(key, newValue);
            return this;
        }

        /**
         * @param values
         * @return
         */
        public Builder add(Map<String, Object> values) {
            for (Entry<String, Object> entry : values.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
            return this;
        }

        /**
         * @param key
         * @param value
         * @return
         */
        public Builder put(final String key, final Object value) {
            urlParams.put(key, value);
            return this;
        }

        /**
         * @param values
         * @return
         */
        public Builder put(Map<String, ?> values) {
            urlParams.putAll(values);
            return this;
        }
    }

    public static void main(String[] args) {
        UrlBuilder ub = new UrlBuilder("http://www.naqichuan.com?cc=2&abcd=3");
        System.out.println(ub.forPath("/n").build());
        ub = new UrlBuilder("http://passport.{0}.{1}.naqichuan.com/{2}?cc=2&abcd=3&ccaa={1}");
        ub.addValue("c");
        ub.addValue("123");
        ub.addValue("bbbb");
        System.out.println(ub.forPath("/n{2}").build());
    }
}
