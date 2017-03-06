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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支持泛协议，该特性用于http 和 https 自适应，如：//nqcx.org，forPath() 操作之前需要填充协议
 * <p/>
 * 支持 baseUrl 占位符，该特性用于泛域名的应用，如：http://$baseUrl$ or //$baseUrl$ or http://$baseUrl$，forPath() 操作之前需要填充 baseUrl
 * <p/>
 * 支持 ajax 匿名函数，如：http://nqcx.org?xx=xx&callback=?
 *
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class UrlBuilder {

    private final static Logger logger = LoggerFactory.getLogger(UrlBuilder.class);

    private final static Pattern URL_PROTOCOL_PATTERN = Pattern.compile("^(file|gopher|news|nntp|telnet|http|ftp|https|ftps|sftp){0,1}:{0,1}//{0,1}(.*)");
    private final static Pattern URL_BASE_PATTERN = Pattern.compile("(\\$\\s*baseUrl\\s*\\$)");
    private final static Pattern PARAM_PLACEHOLDER_PATTERN = Pattern.compile("\\{\\s*\\d+\\s*\\}");

    // 泛协议，默认 http 协议，如果调用 setProtocol() 进行个赋值，设置该属性不空，再调用 setBaseUrl() 时不对 protocol 进行更改
    private final ThreadLocal<String> protocol = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    // 支持泛协议，支持 baseUrl 占位符，//$baseUrl$
    private final ThreadLocal<String> baseUrl = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new String();
        }
    };

    // 生成 url 时根据 index 对应的值替换占位符
    private final ThreadLocal<List<String>> values = new ThreadLocal<List<String>>() {
        @Override
        protected List<String> initialValue() {
            return new ArrayList<String>(50);
        }
    };

    // new UrlBuilder 时指定的 url，不包含 protocol
    private final String originalUrl;
    // new UrlBuilder 时指定 url 的 protocol
    private final String originalProtocol;
    private final Charset charset;
    private final boolean ignoreEmpty;
    private final Map<String, Object> queryMap;


    /**
     * 默认构造
     */
    public UrlBuilder() {
        this("//$baseUrl$");
    }

    /**
     * @param _originalUrl
     */
    public UrlBuilder(final String _originalUrl) {
        this(_originalUrl, Charset.defaultCharset().name());
    }

    /**
     * @param _originalUrl
     * @param _charsetName
     */
    public UrlBuilder(final String _originalUrl, final String _charsetName) {
        this(_originalUrl, _charsetName, true);
    }

    /**
     * @param _originalUrl
     * @param _charsetName
     * @param _ignoreEmpty
     */
    public UrlBuilder(final String _originalUrl, final String _charsetName, final boolean _ignoreEmpty) {
        this.clean();

        // 检查 originalUrl 是否符合要求
        if (_originalUrl == null || _originalUrl.length() == 0)
            throw new RuntimeException("originalUrl 不允许空!");

        Matcher matcher = URL_PROTOCOL_PATTERN.matcher(_originalUrl);
        if (!matcher.matches())
            throw new RuntimeException("originalUrl 格式不匹配!");

        if (matcher.groupCount() >= 1 && matcher.group(1) != null)
            this.originalProtocol = matcher.group(1);
        else
            this.originalProtocol = "http";

        if (matcher.groupCount() >= 2 && matcher.group(2) != null)
            this.originalUrl = matcher.group(2);
        else
            this.originalUrl = _originalUrl;

        // charsetName
        if (_charsetName == null || _charsetName.length() == 0)
            this.charset = Charset.defaultCharset();
        else
            this.charset = Charset.forName(_charsetName);

        // ignoreEmpty
        this.ignoreEmpty = _ignoreEmpty;

        // queryMap
        try {
            String queryString = new URL(this.originalProtocol + "://" + this.originalUrl).getQuery();
            if (StringUtils.isNotEmpty(queryString))
                queryMap = new LinkedHashMap<String, Object>(parseQuery(queryString, charset));
            else
                queryMap = Collections.emptyMap();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 清除原来的变量
     */
    private void clean() {
        protocol.remove();
        baseUrl.remove();
        values.remove();
    }

    // ========================================================================

    /**
     * 为 protocol 赋值
     *
     * @param _protocol
     */
    public UrlBuilder setProtocol(final String _protocol) {
        if (_protocol == null || _protocol.length() == 0)
            return this;
        this.protocol.set(_protocol);

        return this;
    }

    /**
     * 为 baseUrl 赋值
     *
     * @param _baseUr
     */
    public UrlBuilder setBaseUrl(final String _baseUr) {
        if (_baseUr == null || _baseUr.length() == 0)
            return this;

        // 检查 baseUr 是否符合要求
        if (_baseUr == null || _baseUr.length() == 0)
            return this;

        // protocol 未设置，并且 baseUr 包含 protocol，更新 protocol
        Matcher matcher = URL_PROTOCOL_PATTERN.matcher(_baseUr);
        if (matcher.matches() && (protocol.get() == null || protocol.get().length() == 0)
                && matcher.groupCount() > 0
                && matcher.group(1) != null && matcher.group(1).length() > 0)
            protocol.set(matcher.group(1));

        if (matcher.matches() && matcher.groupCount() >= 2 && matcher.group(2) != null)
            this.baseUrl.set(matcher.group(2));
        else
            this.baseUrl.set(_baseUr);

        return this;
    }


    /**
     * 最多允许 50 个占位符
     *
     * @param value
     */
    public UrlBuilder setValue(int index, String value) {
        if (index > 50 || index < 0)
            throw new RuntimeException("占位符个数不允许超过50");

        if (values.get().size() > index)
            values.get().set(index, value);
        else {
            for (int i = values.get().size(); i <= index; i++) {
                values.get().add("{" + i + "}");
                if (i == index)
                    values.get().set(i, value);
            }
        }

        return this;
    }

    /**
     * 最多允许 50 个占位符
     *
     * @param map
     */
    public UrlBuilder setValues(Map<String, String> map) {
        if (map == null || map.size() == 0)
            return this;

        for (Entry<String, String> entry : map.entrySet()) {
            if (entry == null)
                continue;
            try {
                setValue(Integer.parseInt(entry.getKey()), entry.getValue());
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    // ========================================================================

    /**
     * 检查 url 是否包含 protocol
     *
     * @param originalUrl
     * @return
     */
    public static boolean containProtocol(String originalUrl) {
        Matcher matcher = URL_PROTOCOL_PATTERN.matcher(originalUrl);

        return matcher.matches() && matcher.groupCount() >= 1 && matcher.group(1) != null;
    }

    /**
     * @param query
     * @return
     */
    public static Map<String, Object> parseQuery(String query, Charset charset) {
        String[] params = query.split("&");
        Map<String, Object> map = new LinkedHashMap<String, Object>(params.length);
        for (String param : params) {
            String[] strings = param.split("=");
            String name = strings[0];
            String value = null;
            if (strings.length > 1) {
                // 需要对数值进行 decode
                value = decodeValue(strings[1], charset);
            }
            map.put(name, value);
        }
        return map;
    }

    /**
     * 处理 originalUrl 中的占位符
     *
     * @param originalUrl
     * @return
     */
    public static String replaceBaseUrl(String originalUrl, String baseUrl) {
        if (originalUrl == null || originalUrl.length() == 0
                || baseUrl == null || baseUrl.length() == 0)
            return originalUrl;

        return URL_BASE_PATTERN.matcher(originalUrl).replaceAll(baseUrl);
    }

    /**
     * @param value
     * @return
     */
    public static String decodeValue(String value) {
        return decodeValue(value, Charset.defaultCharset());
    }

    /**
     * @param value
     * @return
     */
    public static String decodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLDecoder.decode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
            logger.warn(e.getMessage());
        }
        return value;
    }

    /**
     * @param value
     * @return
     */
    public static String encodeValue(String value) {
        return encodeValue(value, Charset.defaultCharset());
    }

    /**
     * @param value
     * @return
     */
    public static String encodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLEncoder.encode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
            logger.warn(e.getMessage());
        }
        return value;
    }

    /**
     * 检查值是否含有占位符
     *
     * @param value
     * @return
     */
    public static boolean hasPlaceholder(String value) {
        return (value == null || value.length() == 0) ? false : PARAM_PLACEHOLDER_PATTERN.matcher(value).matches();
    }

    // ========================================================================

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
        try {
            return new Builder(new URL(((protocol.get() != null && protocol.get().length() > 0) ? protocol.get() : this.originalProtocol)
                    + "://" + replaceBaseUrl(this.originalUrl, this.baseUrl.get())),
                    path, charset, ignoreEmpty, queryMap, values.get());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================================================================

    /**
     * Builder
     */
    public static class Builder {

        final URL baseUrl;
        String path;
        Charset charset;
        boolean ignoreEmpty;
        final Map<String, Object> queryMap;
        final Map<String, Object> urlParams = new LinkedHashMap<String, Object>();
        final List<String> values;

        Builder(URL _baseUrl, String _path, Charset _charset, boolean _ignoreEmpty, Map<String, Object> _queryMap, List<String> _values) {
            this.baseUrl = _baseUrl;
            this.path = _path;
            this.charset = _charset;
            this.ignoreEmpty = _ignoreEmpty;
            this.queryMap = _queryMap;
            this.values = _values;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setCharsetName(String _charsetName) {
            if (_charsetName != null && _charsetName.length() > 0)
                this.charset = Charset.forName(_charsetName);
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
            // 添加原始参数表
            appendQueryString(query, this.queryMap, false);
            // 添加参数表
            appendQueryString(query, this.urlParams, true);
            if (query.length() > 0)
                query.replace(0, 1, "?");
            builder.append(query);

            // 进行占位符替换
            if (values != null && values.size() > 0)
                return MessageFormat.format(builder.toString(), values.toArray());

            return builder.toString();
        }

        /**
         * @param query
         * @param map
         * @param isEncode 是否进行编码
         */
        private void appendQueryString(StringBuilder query, Map<String, Object> map, boolean isEncode) {
            if (map == null || query == null)
                return;

            for (Entry<String, Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null)
                    continue;

                if (value instanceof Object[]) {
                    for (final Object v : (Object[]) value) {
                        appendQueryString(query, key, v, isEncode);
                    }
                } else if (value instanceof Collection) {
                    for (final Object v : (Collection<?>) value) {
                        appendQueryString(query, key, v, isEncode);
                    }
                } else
                    appendQueryString(query, key, value, isEncode);
            }
        }

        /**
         * @param query
         * @param key
         * @param value
         */
        private void appendQueryString(StringBuilder query, String key, Object value, boolean isEncode) {
            if (value == null)
                return;

            String v = String.valueOf(value);
            if (ignoreEmpty && StringUtils.isBlank(v))
                return;

            query.append("&").append(key).append("=").append(isEncode && !hasPlaceholder(v) ? encodeValue(v, charset) : v);
        }


        /**
         * @param contextPath
         * @param path
         * @return
         */
        private String prefixPath(String contextPath, String path) {
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
        private void append(List<Object> container, Object o) {
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
        public Builder add(final Map<String, Object> values) {
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
        public Builder put(final Map<String, ?> values) {
            urlParams.putAll(values);
            return this;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UrlBuilder ub = new UrlBuilder("//yun.$baseUrl$/{0}?param1={1}&param2={2}&callback=?");
        ub.setProtocol("https");
        ub.setBaseUrl("nqcx.org");
        ub.setValue(0, "i/x");
        ub.setValue(1, "0");
        ub.setValue(2, UrlBuilder.encodeValue("黄保光"));
        ub.setValue(3, "nqcx");
        ub.setValue(4, "wq");

        System.out.println(ub.forPath("/{3}").add("account", "{4}").build());

        System.out.println(containProtocol("https://a"));
    }
}
