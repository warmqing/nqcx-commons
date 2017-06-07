/*
 * Copyright 2014 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.util.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author naqichuan 14/10/24 08:45
 */
public class JsonUtils {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private final static JsonFactory jsonFactory = new JsonFactory();
    private final static ObjectMapper mapper = new ObjectMapper();


    static {
        // 反序列化时忽略多余的属性
        mapper.getDeserializationConfig().with(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 忽略Null的值,节省空间.
        mapper.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        // 忽略Default值木有变化的属性,更节省空间,用于接收方有相同的Class

        // 如int属性初始值为0,那么这个属性将不会被序列化
        mapper.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
    }

    /**
     * 判断是否是标准的json格式
     *
     * @param json
     * @return
     */
    public static boolean validJson(final String json) {
        try {
            JsonParser parser = jsonFactory.createJsonParser(json);
            while (parser.nextToken() != null) ;

            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return false;
    }

    /**
     * @param json
     * @param key
     * @return
     */
    public static Integer getInt(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToObject(path.toString(), Integer.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param key
     * @return
     */
    public static Long getLong(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode() && !path.isNull()) {
                return jsonToObject(path.toString(), Long.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param key
     * @return
     */
    public static String getString(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToObject(rootNode.path(key).toString(), String.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    public static Date getDate(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToObject(path.toString(), Date.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param key
     * @return
     */
    public static Boolean getBoolean(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToObject(path.toString(), Boolean.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * json转换为指定类型对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToObject(String json, Class<? extends T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * json转换为指定类型对象
     *
     * @param json
     * @param
     * @return
     */
    public static <T> T jsonToObject(String json, JavaType type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 一个jsonStr包含多个java对象，取其中一个转化为java对象的方法
     *
     * @param json  json格式的字符串
     * @param key   要转换的子json串的key
     * @param clazz 目标类型
     * @return 返回类型为T的对象
     * @throws Exception
     */
    public static <T> T jsonToObject(String json, String key, Class<? extends T> clazz) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode())
                return jsonToObject(path.toString(), clazz);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param parentkey
     * @param subkey
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, String parentkey, String subkey, Class<? extends T> clazz) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(parentkey);
            if (!path.isMissingNode()) {
                JsonNode subNode = path.path(subkey);
                if (!subNode.isMissingNode())
                    return jsonToObject(subNode.toString(), clazz);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * json转换为 Map 对象的 list
     *
     * @param json
     * @return
     */
    public static List<?> jsonToList(String json) {
        return jsonToObject(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json), List.class);
    }

    /**
     * json转List
     *
     * @param <T>
     * @param json
     * @param clazz List的元素类型，会一并转换完成 如List<T>
     * @return 元素类型为E的List
     */
    public static <T> List<T> jsonToList(String json, Class<? extends T> clazz) {
        try {
            return mapper.readValue(((json == null || (json = json.trim()).equals("") || !validJson(json)) ? "[]" : json),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 一个jsonStr包含多个java对象，将指定的key的json转化为List<E>的方法
     *
     * @param json  原始的json串
     * @param key   要转换的那部分json
     * @param clazz 目标类型
     * @return 元素为目标类型的List
     */
    public static <E> List<E> jsonToList(String json, String key, Class<? extends E> clazz) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode())
                return jsonToList(path.toString(), clazz);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param parentkey
     * @param subkey
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> jsonToList(String json, String parentkey, String subkey, Class<? extends E> clazz) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(parentkey);
            if (!path.isMissingNode()) {
                JsonNode subNode = path.path(subkey);
                if (!subNode.isMissingNode())
                    return jsonToList(subNode.toString(), clazz);

            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
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
                    TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, clazz));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * json转对象数组
     *
     * @param <T>
     * @param json
     * @param clazz 数组中的对象类型
     * @return E类型的数组，如User[]
     */
    public static <T> T[] jsonToArray(String json, Class<? extends T> clazz) {
        try {
            if (json != null) {
                return mapper.readValue(json, TypeFactory.defaultInstance().constructArrayType(clazz));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }


    /**
     * 一个jsonStr包含多个java对象，将指定的key的json转化为对象数组的方法
     *
     * @param json  原始的json串
     * @param key   要转换的部分
     * @param clazz 目标类型
     * @return 目标类型的对象数组
     */
    public static <T> T[] jsonToArray(String json, String key, Class<? extends T> clazz) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToArray(rootNode.path(key).toString(), clazz);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * @param json
     * @param key
     * @return
     */
    public static Integer[] jsonToArray(String json, String key) {
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            JsonNode path = rootNode.path(key);
            if (!path.isMissingNode()) {
                return jsonToArray(path.toString(), Integer.class);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }


    /**
     * 对象转换为json，已知问题 User u Agent a u中有a，a中有u ， 如果u和a同在一个o中将不能正常转化
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        if (object != null) {
            try {
                return mapper.writeValueAsString(object);
            } catch (Exception e) {
                logger.warn(e.getMessage());
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

    /**
     * InputStream类型的json转换为指定类型对象
     *
     * @param jsonStream
     * @param classType
     * @return
     */
    public static <T> T readJsonObject(InputStream jsonStream, Class<T> classType) {
        try {
            return mapper.readValue(jsonStream, classType);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 转换成json串到out
     *
     * @param out
     * @param o
     */
    public static void writeObjectJson(OutputStream out, Object o) {
        try {
            mapper.writeValue(out, o);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 转换成json串到writer
     *
     * @param out
     * @param o
     */
    public static void writeObjectJson(Writer out, Object o) {
        try {
            mapper.writeValue(out, o);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }
}
