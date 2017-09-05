/*
 * Copyright 2017 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.solrcloud;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.nqcx.commons.lang.o.DTO;
import org.nqcx.commons.solr.SolrSort;
import org.nqcx.commons.util.date.DateFormatUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author Jiangsiqi on 2017/8/17 19:18.
 */
public class SolrQueryBuilder {

    private final static String SOLR_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:sss'Z'";

    public static SolrQuery dto2query(DTO dto, SolrQuery query) {

        if (dto == null)
            return null;

        if (query == null)
            query = new SolrQuery();

        Map<String, Object> fields = dto.getParamsMap();

        if (fields == null) {
            query = new SolrQuery().setQuery("*:*");
        } else {
            StringBuffer sb = new StringBuffer();
            Object object = null;
            for (Map.Entry<String, Object> field : fields.entrySet()) {
                object = field.getValue();
                if (object == null)
                    continue;

                if (sb.length() > 0)
                    sb.append(" AND ");

                if (object instanceof List) {
                    List<String> valueList = (List<String>) object;
                    if (valueList != null && valueList.size() > 0) {
                        sb.append("(");
                        for (int i = 0; i < valueList.size(); i++) {
                            if (i > 0)
                                sb.append(" OR ");
                            sb.append(field.getKey() + ":" + valueList.get(i));
                        }
                        sb.append(")");
                    }
                } else if (object instanceof SolrList) {
                    SolrList solrList = (SolrList) object;
                    if (solrList != null && solrList.size() > 0) {
                        if (solrList.isAnd()) {
                            SolrList sl = new SolrList();
                            String q = sl.getQueryString(field.getKey());
                            sb.append(q);
                        }
                    }
                } else if (object instanceof SolrNull) {
                    SolrNull solrNull = (SolrNull) object;
                    if (solrNull.isTrue()) {
                        sb.append("-");
                    }
                    sb.append(field.getKey() + "[\"\" TO * ]");
                } else if (object instanceof SolrDate) {
                    SolrDate solrDate = (SolrDate) object;

                    if (solrDate != null && StringUtils.isNotBlank(field.getKey())
                            && (solrDate.getBegintime() != null || solrDate.getEndtime() != null)) {
                        String b = "*";
                        String e = "*";
                        if (solrDate.getBegintime() != null)
                            b = DateFormatUtils.format(solrDate.getBegintime(), SOLR_DATE_PATTERN);
                        if (solrDate.getEndtime() != null)
                            e = DateFormatUtils.format(solrDate.getEndtime(), SOLR_DATE_PATTERN);

                        sb.append(MessageFormat.format(" {0}:[{1} TO {2}] ", field.getKey(), b, e));
                    }
                } else
                    sb.append(field.getKey() + ":" + object);
            }
            query.setQuery(sb.toString());
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
        return query;
    }

    /**
     * @param dto
     * @return
     */
    public static SolrQuery dto2query(DTO dto) {
        return dto2query(dto, null);
    }

}
