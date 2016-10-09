/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.nqcx.commons.lang.o.DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author naqichuan 15/1/6 14:09
 */
public abstract class SolrQuerySupport extends SolrSupport {

    private final static Logger logger = LoggerFactory.getLogger(SolrQuerySupport.class);

    @Override
    public abstract SolrServerCore getServerCore();

    /**
     * 返回 反回的 DTO 对象中 List 元素为 SolrDocumentList
     *
     * @param dto
     */
    public void searchResult(DTO dto) {
        searchResult(dto, null);
    }

    /**
     * 返回 反回的 DTO 对象中 List 元素为 SolrDocumentList
     *
     * @param dto
     * @param query
     */
    public void searchResult(DTO dto, SolrQuery query) {
        searchBeans(dto, null, query);
    }

    /**
     * 返回 反回的 DTO 对象中 List 元素为指定的类型
     *
     * @param dto
     * @param type
     * @param <T>
     */
    public <T> void searchBeans(DTO dto, Class<T> type) {
        searchBeans(dto, type, null);
    }

    /**
     * 返回 反回的 DTO 对象中 List 元素为指定的类型
     *
     * @param dto
     * @param type
     * @param query
     * @param <T>
     */
    public <T> void searchBeans(DTO dto, Class<T> type, SolrQuery query) {
        if (dto == null)
            return;

        // 查询条件
        if (query == null) {
            query = new SolrQuery().setQuery("*:*");
            if (dto.getParamsMap() != null && !dto.getParamsMap().isEmpty()) {
                Set<Map.Entry<String, Object>> sets = dto.getParamsMap().entrySet();
                if (sets != null && sets.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    Object dataObj = null;
                    for (Map.Entry<String, Object> entry : sets) {
                        dataObj = entry.getValue();
                        if (dataObj == null)
                            continue;

                        if (sb.length() > 0)
                            sb.append(" AND ");

                        if (dataObj instanceof List) {
                            List<String> valueList = (List<String>) dataObj;
                            if (valueList != null && valueList.size() > 0) {
                                sb.append(" (");
                                for (int i = 0; i < valueList.size(); i++) {
                                    if (i > 0)
                                        sb.append(" OR ");
                                    sb.append(entry.getKey() + ":" + valueList.get(i));
                                }
                                sb.append(")");
                            }
                        } else
                            sb.append(entry.getKey() + ":" + (String) dataObj);
                    }
                    query.setQuery(sb.toString());
                }
            }
        }

        // 分页
        if (dto.getPage() != null) {
            query.setStart((int) dto.getPage().getStartIndex());
            query.setRows((int) dto.getPage().getPageSize());
        }

        // 排序
        if (dto.getSort() != null && dto.getSort() instanceof SolrSort) {
            query.clearSorts();
            SolrSort ss = (SolrSort) dto.getSort();
            for (SolrQuery.SortClause sc : ss.getSearchOrder()) {
                query.addSort(sc);
            }
        }

        try {
            QueryResponse qrsp = getServer().query(query);
            if (dto.getPage() != null)
                dto.getPage().setTotalCount(qrsp.getResults().getNumFound());

            if (type == null)
                dto.setList(qrsp.getResults());
            else
                dto.setList(qrsp.getBeans(type));

            dto.setSuccess(true);
        } catch (SolrServerException e) {
            logger.error("", e);
        }
    }


    /**
     * 关键字格式化工具
     *
     * @param keyWord
     * @param delimiter AND or OR
     * @return
     */
    public static String formatKeyWord(String keyWord, String delimiter) {
        if (StringUtils.isNotBlank(keyWord)) {
            Pattern p = Pattern.compile(QUERY_STRING_FORMAT_REGEX);
            // 先替换中文空格
            Matcher m = p.matcher(keyWord.replaceAll("　", " "));
            // 将正则表达式匹配到的所有字符替换成空格
            keyWord = m.replaceAll(" ").trim();

            // 替换替换两个以上的空格为一个空格
            p = Pattern.compile(" {2,}");
            m = p.matcher(keyWord);
            keyWord = m.replaceAll(" ");

            String[] words = keyWord.split(" ");
            if (words.length == 1 && words[0] != null && (words[0] = words[0].trim()).length() > 0) {
                return words[0];
            } else if (words.length > 1) {
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < words.length; ++i) {
                    if (i > 0)
                        sb.append(" " + delimiter + " ");
                    sb.append(words[i]);
                }
                return sb.toString();
            }
        }

        return "*:*";
    }
}
