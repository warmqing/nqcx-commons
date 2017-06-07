/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.util.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangqing 16/11/22 上午10:21
 */
public class JsonCacheUtils {
    private final static Logger logger = LoggerFactory.getLogger(JsonCacheUtils.class);

    private static ObjectMapper objectMapper;
    private static final JsonFactory jsonFactory = new JsonFactory();

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            protected boolean _isIgnorable(Annotated annotated) {
                //不忽略字段
                return false;
            }
        });
    }

    /**
     * 判断是否是标准的json格式
     *
     * @param json
     * @return
     */
    public static boolean validJson(final String json) {
        boolean valid = true;
        try {
            JsonParser parser = jsonFactory.createJsonParser(json);
            while (parser.nextToken() != null) ;
            return true;
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }

    /**
     * json转换为指定类型对象
     *
     * @param json
     * @param classType
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * json转换为指定类型对象
     *
     * @param json
     * @param classType
     * @return
     */
    public static <T> T jsonToObject(String json, JavaType classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * InputStream类型的json转换为指定类型对象
     *
     * @param jsonStream
     * @param classType
     * @return
     */
    public static <T> T inputStreamToObject(InputStream jsonStream, Class<T> classType) {
        try {
            return objectMapper.readValue(jsonStream, classType);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * json转换为map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        return jsonToObject(((json == null || !validJson(json)) ? "{}" : json), Map.class);
    }

    /**
     * json 转换成 map
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String json, Class<? extends T> clazz) {
        try {
            return (HashMap<String, T>) jsonToObject(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json),
                    objectMapper.getTypeFactory().constructParametricType(HashMap.class, String.class, clazz));
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * json转换为list
     *
     * @param json
     * @return
     */
    public static List<?> jsonToList(String json) {
        return jsonToObject(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json), List.class);
    }

    /**
     * json 转换为 list
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String json, Class<? extends T> clazz) {
        try {
            return (List<T>) jsonToObject(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * json转换为map对象的list
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> jsonToMapList(String json) {
        return jsonToObject(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json), List.class);
    }

    /**
     * 对象转换为json
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        if (object != null) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return null;
    }

    /**
     * map转换为json
     *
     * @param map
     * @return
     */
    public static String mapToJson(Map<String, Object> map) {
        return objectToJson(map);
    }

    /**
     * list转换为json
     *
     * @param list
     * @return
     */
    public static String listToJson(List<?> list) {
        return objectToJson(list);
    }
}
