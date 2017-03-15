/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.web;

import org.apache.commons.lang.StringUtils;
import org.nqcx.commons.lang.o.DTO;
import org.nqcx.commons.lang.page.PageIO;
import org.nqcx.commons.util.MapBuilder;
import org.nqcx.commons.util.json.JsonUtils;
import org.nqcx.commons.web.result.NqcxResult;
import org.nqcx.commons.web.result.ResultBuilder;
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

    protected final static String DEFAULT_CHARSET_NAME = "UTF-8";

    @Autowired(required = false)
    protected ResultBuilder resultBuilder;
    @Autowired(required = false)
    protected MessageSource messageSource;

    protected String m(String code) {
        NqcxResult nqcxResult = getResult(ResultBuilder.M, code);
        return nqcxResult == null ? "" : getPropertyValue(nqcxResult.getSubject());
    }

    protected String e(String code) {
        NqcxResult nqcxResult = getResult(ResultBuilder.E, code);
        return nqcxResult == null ? "" : getPropertyValue(nqcxResult.getSubject());
    }

    protected String s(String code) {
        NqcxResult nqcxResult = getResult(ResultBuilder.S, code);
        return nqcxResult == null ? "" : getPropertyValue(nqcxResult.getSubject());
    }

    /**
     * 取得配置文件中的Result
     *
     * @param type
     * @param code
     * @return
     */
    protected NqcxResult getResult(String type, String code) {
        return resultBuilder == null ? null : resultBuilder.getResult(type, code);
    }

    // ========================================================================

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
            if (locale == null)
                locale = getLocale();

            rv = messageSource == null ? null : messageSource.getMessage(code, arguments, locale);
        } catch (NoSuchMessageException e) {
            logger.warn("WebSupport.getPropertyValue ," + e.getMessage());
        }
        return rv == null ? code : rv;
    }

    // ========================================================================

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

    // ========================================================================

    /**
     * @return
     */
    protected WebContext getWebContext() {
        return WebContext.getWebContext();
    }

    /**
     * @return
     */
    protected String getScheme() {
        return getWebContext() == null ? null : getWebContext().getScheme();
    }

    /**
     * @return
     */
    protected String getServerName() {
        return getWebContext() == null ? null : getWebContext().getServerName();
    }

    /**
     * @return
     */
    protected String getRemoteAddr() {
        return getWebContext() == null ? null : getWebContext().getRemoteAddr();
    }

    /**
     * @return
     */
    protected String getContextPath() {
        return getWebContext() == null ? null : getWebContext().getContextPath();
    }

    /**
     * @return
     */
    protected boolean isAjax() {
        return getWebContext() == null ? false : getWebContext().isAjax();
    }

    /**
     * @return
     */
    protected Locale getLocale() {
        return getWebContext() == null ? null : getWebContext().getLocale();
    }

    // ========================================================================

    /**
     * 构建返回结果，返回 String 类型
     *
     * @param dto
     * @return
     */
    protected String buildJsonResult(DTO dto) {
        return JsonUtils.mapToJson((Map<String, Object>) buildResult(dto));
    }

    /**
     * 构建返回结果，返回 map 类型
     *
     * @param dto
     * @return
     */
    protected Map<?, ?> buildResult(DTO dto) {
        if (dto == null)
            // 这里的 value 只做说明，最终返回以 gmsg.properties 中 key 对应的配置为准
            dto = new DTO().putResult("10", "操作数据出错");

        MapBuilder mb = MapBuilder.newInstance().put(SUCCESS, dto.isSuccess());

        if (dto.isSuccess())
            parseSuccess(mb, dto);
        else
            parseError(mb, dto);

        return mb.build();
    }

    /**
     * 解析错误结果
     *
     * @param mapBuilder
     * @param dto
     */
    private void parseError(MapBuilder mapBuilder, DTO dto) {
        Map<String, Object> errorMap;
        if (mapBuilder == null || dto == null || (errorMap = dto.getResultMap()) == null || errorMap.isEmpty())
            return;
        else if (errorMap.size() == 1)
            parseErrorJson(mapBuilder, errorMap.entrySet().iterator().next());
        else
            parseMultipleErrorJson(mapBuilder, errorMap.entrySet());
    }

    /**
     * 处理单个错误
     *
     * @param mapBuilder
     * @param entry
     */
    protected void parseErrorJson(MapBuilder mapBuilder, Map.Entry<String, Object> entry) {
        mapBuilder.putMap(putError(entry.getKey()));
    }

    /**
     * 处理多个错误，errorCode 使用默认错误码“10”
     * multipleError 的 multipleErrorCode 使用 Entry.key
     * multipleError 的 multipleErrorText 使用 Entry.value
     * <p/>
     * 参数:
     * <pre>
     *      entrySet {key="1", value="错误1"}
     *      entrySet {key="2", value="错误2"}
     *      entrySet {key="3", value="错误3"}
     * </pre>
     * 转换结果:
     * <pre>
     * {
     *     success: false,
     *     errorCode: "10",
     *     errorText: "操作数据出错",
     *     multipleError: [
     *         {
     *             multipleErrorCode: "1",
     *             multipleErrorText: "错误1"
     *         },
     *         {
     *             multipleErrorCode: "2",
     *             multipleErrorText: "错误2"
     *         },
     *         {
     *             multipleErrorCode: "3",
     *             multipleErrorText: "错误3"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param mapBuilder mapBuilder
     * @param entrySet   entrySet
     */
    protected void parseMultipleErrorJson(MapBuilder mapBuilder, Set<Map.Entry<String, Object>> entrySet) {
        if (mapBuilder == null || entrySet == null)
            return;
        mapBuilder.putMap(putError("10")).put(ERROR_MULTIPLE, convertMultipleErrorJsonArray(entrySet));
    }

    /**
     * 处理多个错误，errorCode 使用参数中的错误码 errorCode
     * multipleError 的 multipleErrorCode 全部使用 “1X”
     * multipleError 的 multipleErrorText 使用参数 errors[index] 中的值
     * <p/>
     * 参数:
     * <pre>
     *     errorCode="11"
     *     errors {"错误1","错误2","错误3"}
     * </pre>
     * 转换结果:
     * <pre>
     * {
     *     success: false,
     *     errorCode: "11",
     *     errorText: "操作的数据不存在",
     *     multipleError: [
     *         {
     *             multipleErrorCode: "1X",
     *             multipleErrorText: "错误1"
     *         },
     *         {
     *             multipleErrorCode: "1X",
     *             multipleErrorText: "错误2"
     *         },
     *         {
     *             multipleErrorCode: "1X",
     *             multipleErrorText: "错误3"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param mapBuilder mapBuilder
     * @param errorCode  errorCode
     * @param errors     errors
     */
    protected void parseMultipleErrorJson(MapBuilder mapBuilder, String errorCode, List<String> errors) {
        if (mapBuilder == null)
            return;
        if (StringUtils.isBlank(errorCode))
            errorCode = "10";

        mapBuilder.putMap(putError(errorCode)).put(ERROR_MULTIPLE, convertMultipleErrorJsonArray(errors));
    }

    /**
     * 转换多个 Entry 错误为 array
     *
     * @param entrySet entrySet
     * @return list
     * @author 黄保光
     */
    private List<Object> convertMultipleErrorJsonArray(Set<Map.Entry<String, Object>> entrySet) {
        List<Object> list = new ArrayList<Object>();
        if (entrySet != null) {
            for (Map.Entry<String, Object> error : entrySet)
                list.add(MapBuilder.newInstance().put(ERROR_MULTIPLE_CODE, error.getKey()).put(ERROR_MULTIPLE_TEXT, error.getValue()).build());
        }
        return list;
    }

    /**
     * 将多个 List 型的 error 转成 List<>Map><>String,String</></>
     *
     * @param errors
     * @return
     * @author 黄保光 Sep 29, 2013 12:37:41 PM
     */
    protected List<Object> convertMultipleErrorJsonArray(List<String> errors) {
        List<Object> list = new ArrayList<Object>();
        if (errors != null && errors.size() == 0) {
            for (String error : errors)
                list.add(MapBuilder.newInstance().put(ERROR_MULTIPLE_CODE, "1X").put(ERROR_MULTIPLE_TEXT, error).build());
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
     * @param pageIO
     */
    private void parsePageBuilder(MapBuilder mapBuilder, PageIO pageIO) {
        if (pageIO == null)
            return;

        mapBuilder.put("page", pageIO.getPage());
        mapBuilder.put("totalCount", pageIO.getTotalCount());
        mapBuilder.put("pageSize", pageIO.getPageSize());
        mapBuilder.put("totalPage", pageIO.getTotalPage());
    }

    // ========================================================================

    /**
     * 通过 response 直接返回 ContentType 为 application/json 格式字符串
     *
     * @param response
     * @param result
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */
    protected void responseJsonResult(HttpServletResponse response, String result) {
        response.setContentType("application/json; charset=UTF-8");
        responseResult(response, result);
    }

    /**
     * 通过 response 直接返回 ContentType 为 text/html 格式字符串
     *
     * @param response
     * @param result
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */
    protected void responseHtmlResult(HttpServletResponse response, String result) {
        response.setContentType("text/html; charset=UTF-8");
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

    // ========================================================================

    /**
     * 跳转到错误页
     *
     * @param dto
     */
    protected void sendRedirectErrorResult(HttpServletResponse response, DTO dto) {
        String errorCode = "1";
        Map<String, Object> errorMap = null;

        if (dto != null && dto.isSuccess())
            return;
        else if (dto != null && (errorMap = dto.getResultMap()) != null && errorMap.size() > 0)
            errorCode = errorMap.entrySet().iterator().next().getKey();

        try {
            response.sendRedirect(getContextPath() + "/r/e/" + errorCode);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }
}
