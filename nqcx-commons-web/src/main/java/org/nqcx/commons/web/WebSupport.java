/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web;

import org.nqcx.commons.lang.DTO;
import org.nqcx.commons.lang.page.PageBuilder;
import org.nqcx.commons.util.MapBuilder;
import org.nqcx.commons.web.result.NqcxResult;
import org.nqcx.commons.web.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public abstract class WebSupport {


    private final static Logger logger = LoggerFactory.getLogger(WebSupport.class);

    protected final static String SUCCESS = "success";
    protected final static String ERROR_CODE = "errorCode";
    protected final static String ERROR_TEXT = "errorText";
    protected final static String ERROR_MULTIPLE = "multipleError";
    protected final static String ERROR_MULTIPLE_CODE = "multipleErrorCode";
    protected final static String ERROR_MULTIPLE_TEXT = "multipleErrorText";
    //
    protected final static String DEFAULT_CHARSET_NAME = "UTF-8";

    @Autowired(required = false)
    private NqcxResult resultConfig;
    @Autowired(required = false)
    private MessageSource messageSource;

    protected String m(String code) {
        Result result = getResult(NqcxResult.M, code);
        return result == null ? "" : getPropertyValue(result.getSubject());
    }

    protected String e(String code) {
        Result result = getResult(NqcxResult.E, code);
        return result == null ? "" : getPropertyValue(result.getSubject());
    }

    protected String s(String code) {
        Result result = getResult(NqcxResult.S, code);
        return result == null ? "" : getPropertyValue(result.getSubject());
    }

    /**
     * 取得配置文件中的Result
     *
     * @param type
     * @param code
     * @return
     */
    protected Result getResult(String type, String code) {
        return resultConfig == null ? null : resultConfig.getResult(type, code);
    }

    /**
     * 从 properties 中取值
     *
     * @param code
     * @return
     */
    protected String getPropertyValue(String code) {
        return getPropertyValue(code, null);
    }

    /**
     * 从 properties 中取值
     *
     * @param code
     * @param arguments
     * @return
     */
    protected String getPropertyValue(String code, Object[] arguments) {
        return getPropertyValue(code, arguments, null);
    }

    /**
     * 从 properties 中取值
     *
     * @param code
     * @param arguments
     * @param locale
     * @return
     */
    protected String getPropertyValue(String code, Object[] arguments, Locale locale) {
        String rv = null;
        try {
            if (locale == null && WebContext.getWebContext() != null)
                locale = WebContext.getWebContext().getLocale();

            rv = messageSource == null ? null : messageSource.getMessage(code, arguments, locale);
        } catch (NoSuchMessageException e) {
            logger.warn("WebSupport.getPropertyValue ," + e.getMessage());
        }
        return rv == null ? code : rv;
    }

    /**
     * 向 MAP 中添加错误信息，同时转换错误代码为说明
     *
     * @param map
     * @param value
     * @return
     */
    protected Map<?, ?> putError(Map<Object, Object> map, String value) {
        if (map != null && value != null) {
            putValue(map, ERROR_CODE, value);
            putValue(map, ERROR_TEXT, e(value));
        }
        return map;
    }

    /**
     * 向 MAP 中添加错误信息，同时转换错误代码为说明
     *
     * @param value
     * @return
     */
    protected Map<?, ?> putError(String value) {
        return putError(new HashMap<Object, Object>(), value);
    }

    /**
     * 向 MAP 中添加信息
     *
     * @param map
     * @param key
     * @param value
     * @return
     */
    protected Map<?, ?> putValue(Map<Object, Object> map, String key, Object value) {
        if (map != null && key != null) {
            map.put(key, value);
        }
        return map;
    }

    /**
     * 向 MAP 中添加信息
     *
     * @param key
     * @param value
     * @return
     */
    protected Map<?, ?> putValue(String key, Object value) {
        return putValue(new HashMap<Object, Object>(), key, value);
    }

    /**
     * @return
     * @author huangbg 2014年8月1日 下午5:49:40
     */
    protected WebContext getWebContext() {
        return WebContext.getWebContext();
    }

    /**
     * 分析返回结果
     *
     * @param dto
     * @return
     */
    protected Map<?, ?> returnResult(DTO dto) {
        if (dto == null) {
            dto = new DTO();
            // 默认调用失败
            dto.putResult("10", "10");
        }

        MapBuilder mb = MapBuilder.newInstance().put(SUCCESS, dto.isSuccess());

        if (dto.isSuccess())
            parseSuccess(mb, dto);
        else
            parseError(mb, dto.getResultMap());

        return mb.build();
    }

    /**
     * 解析错误结果
     *
     * @param mapBuilder
     * @param errorMap
     */
    private void parseError(MapBuilder mapBuilder, Map<String, Object> errorMap) {
        if (errorMap == null || errorMap.isEmpty())
            return;
        else if (errorMap.size() == 1)
            parseErrorJson(mapBuilder, errorMap.entrySet().iterator().next());
        else {
            parseMultipleErrorJson(mapBuilder, errorMap.entrySet());
        }
    }

    /**
     * 处理单个错误
     *
     * @param mapBuilder
     * @param entry
     */
    private void parseErrorJson(MapBuilder mapBuilder, Map.Entry<String, Object> entry) {
        mapBuilder.putMap(putError(entry.getValue().toString()));
    }

    /**
     * 处理多个错误
     *
     * @param mapBuilder
     * @param entrySet
     */
    private void parseMultipleErrorJson(MapBuilder mapBuilder, Set<Map.Entry<String, Object>> entrySet) {
        mapBuilder.putMap(putError("10")).put(ERROR_MULTIPLE, convertMultipleErrorJsonArray(entrySet));
    }

    /**
     * 转换多个错误到 array
     *
     * @param entrySet
     * @return
     * @author 黄保光 Sep 29, 2013 4:30:37 PM
     */
    private List<Object> convertMultipleErrorJsonArray(Set<Map.Entry<String, Object>> entrySet) {
        List<Object> list = new ArrayList<Object>();
        for (Map.Entry<String, Object> error : entrySet) {
            list.add(MapBuilder.newInstance().put(ERROR_MULTIPLE_CODE, "1x")
                    .put(ERROR_MULTIPLE_TEXT, error.getValue().toString()).build());
        }
        return list;
    }

    /**
     * 处理成功结果
     *
     * @param mapBuilder
     * @param dto
     */
    private <T> void parseSuccess(MapBuilder mapBuilder, DTO dto) {
        // 1. 解析对象
        parseSuccessObject(mapBuilder, dto.getObject());
        // 2. 解析分页
        parsePageBuilder(mapBuilder, dto.getPage());
        // 3. 解析列表
        parseSuccessList(mapBuilder, dto.getList());
        // 4. 解析结果
        parseSuccessResult(mapBuilder, dto.getResultMap());
    }

    /**
     * 处理 object
     *
     * @param mapBuilder
     * @param object
     */
    private void parseSuccessObject(MapBuilder mapBuilder, Object object) {
        if (object != null)
            mapBuilder.put("object", object);
    }

    /**
     * 处理 list
     *
     * @param mapBuilder
     * @param list
     */
    private void parseSuccessList(MapBuilder mapBuilder, List<?> list) {
        if (list != null)
            mapBuilder.put("list", list);
    }

    /**
     * 处理 result
     *
     * @param mapBuilder
     * @param map
     */
    private void parseSuccessResult(MapBuilder mapBuilder, Map<?, ?> map) {
        if (map != null)
            mapBuilder.put("result", map);
    }

    /**
     * 设置返回分页的结果
     *
     * @param mapBuilder
     * @param pageBuilder
     */
    private void parsePageBuilder(MapBuilder mapBuilder, PageBuilder pageBuilder) {
        if (pageBuilder == null)
            return;

        mapBuilder.put("page", pageBuilder.getPage());
        mapBuilder.put("totalCount", pageBuilder.getTotalCount());
        mapBuilder.put("pageSize", pageBuilder.getPageSize());
        mapBuilder.put("totalPage", pageBuilder.getTotalPage());
    }

    /**
     * 通过 response 直接返回 ContentType 为 application/json 格式字符串
     *
     * @param response
     * @param result
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */

    protected void responseJsonResult(HttpServletResponse response, String result) {
        response.setContentType("application/json; charset=utf-8");
        responseResult(response, result);
    }

    /**
     * 通过 response 直接返回字符串
     *
     * @param response
     * @param result
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */

    protected void responseResult(HttpServletResponse response, String result) {
        response.setCharacterEncoding(DEFAULT_CHARSET_NAME);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(result);
        } catch (IOException e) {
            logger.warn("WebSupport.responseResult, " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
