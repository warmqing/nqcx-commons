/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web.url;

import org.nqcx.commons.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class UrlBuilder {

    private final URL base;
    private final boolean ignoreEmpty;
    private final Charset charset;
    private final Map<String, Object> queryMap;

    public UrlBuilder(final String base) throws MalformedURLException {
        this(base, "utf-8", true);
    }

    public UrlBuilder(final String base, final String charsetName) throws MalformedURLException {
        this(base, charsetName, true);
    }

    public UrlBuilder(final String base, final String charsetName, final boolean ignoreEmpty)
            throws MalformedURLException {
        this.base = new URL(base);
        this.charset = Charset.forName(charsetName);
        this.ignoreEmpty = ignoreEmpty;
        String queryString = this.base.getQuery();
        if (!StringUtils.isEmpty(queryString)) {
            queryMap = new LinkedHashMap<String, Object>(parseQuery(queryString));
        } else {
            queryMap = Collections.emptyMap();
        }
    }

    private Map<String, Object> parseQuery(String query) {
        String[] params = query.split("&");
        Map<String, Object> map = new LinkedHashMap<String, Object>(params.length);
        for (String param : params) {
            String[] strings = param.split("=");
            String name = strings[0];
            String value = null;
            if (strings.length > 1) {
                value = strings[1];
            }
            map.put(name, value);
        }
        return map;
    }

    public Builder forPath() {
        return forPath(null);
    }

    public Builder forPath(String path) {
        return new Builder(base, path, charset.name(), ignoreEmpty, queryMap);
    }

    public static class Builder {

        final URL base;
        String path;
        String charsetName;
        boolean ignoreEmpty;
        final Map<String, Object> urlParameters;

        Builder(URL base, String path, String charsetName, boolean ignoreEmpty, Map<String, Object> queryMap) {
            this.base = base;
            this.path = path;
            this.charsetName = charsetName;
            this.ignoreEmpty = ignoreEmpty;
            this.urlParameters = new LinkedHashMap<String, Object>(queryMap);
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

        public String build() {

            String path = prefixPath(base.getPath(), this.path);
            int port = base.getPort();
            if (base.getPort() == base.getDefaultPort()) {
                port = -1;
            }

            final StringBuilder builder;
            try {
                builder = new StringBuilder(
                        new URL(base.getProtocol(), base.getHost(), port, path).toString());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            StringBuilder query = new StringBuilder();
            for (Entry<String, Object> entry : urlParameters.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Object[]) {
                    for (final Object v : (Object[]) value) {
                        appendQueryString(key, v, query);
                    }
                } else if (value instanceof Collection) {
                    for (final Object v : (Collection<?>) value) {
                        appendQueryString(key, v, query);
                    }
                } else {
                    appendQueryString(key, value, query);
                }
            }
            if (query.length() > 0) {
                query.replace(0, 1, "?");
            }

            return builder.append(query).toString();
        }

        void appendQueryString(String key, Object v, StringBuilder sb) {
            if (v == null) {
                return;
            }
            String value = String.valueOf(v);
            if (ignoreEmpty && value.trim().length() == 0) {
                return;
            }
            sb.append("&").append(key).append("=").append(encodeUrl(value));
        }

        String encodeUrl(String value) {
            String result;
            try {
                result = URLEncoder.encode(value,
                        StringUtils.isNotBlank(charsetName) ? charsetName : Charset.defaultCharset().name());
            } catch (UnsupportedEncodingException e) {
                result = value;
            }
            return result;
        }

        String prefixPath(String contextPath, String path) {
            String returnPath;
            if (path == null || contextPath == null) {
                if (path == null && contextPath == null) {
                    returnPath = "/";
                } else if (contextPath == null) {
                    returnPath = path;
                } else {
                    returnPath = contextPath;
                }
            } else {
                if (contextPath.endsWith("/") && path.startsWith("/")) {
                    returnPath = contextPath + path.substring(1);
                } else {
                    returnPath = contextPath + path;
                }
            }
            return returnPath;
        }

        public Builder add(final String key, final Object value) {
            Object newValue;
            if (urlParameters.containsKey(key)) {
                Object o = urlParameters.get(key);
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
            urlParameters.put(key, newValue);
            return this;
        }

        void append(List<Object> container, Object o) {
            if (o instanceof Object[]) {
                for (Object e : (Object[]) o) {
                    container.add(e);
                }
            } else if (o instanceof Collection) {
                container.addAll((Collection<?>) o);
            } else {
                container.add(o);
            }
        }

        public Builder addAll(final Map<String, Object> values) {
            if (values != null && values.size() > 0) {
                for (Entry<String, Object> entry : values.entrySet()) {
                    add(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder put(final String key, final Object value) {
            urlParameters.put(key, value);
            return this;
        }

        public Builder put(Map<String, Object> values) {
            urlParameters.putAll(values);
            return this;
        }

    }
}
