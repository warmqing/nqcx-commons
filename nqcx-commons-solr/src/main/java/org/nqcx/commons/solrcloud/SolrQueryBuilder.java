/*
 * Copyright 2017 ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */

package org.nqcx.commons.solrcloud;

import org.apache.solr.client.solrj.SolrQuery;
import org.nqcx.commons.lang.o.DTO;
import org.nqcx.commons.solr.SolrSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author Jiangsiqi on 2017/8/17 19:18.
 */
public class SolrQueryBuilder {

    private final static Logger logger = LoggerFactory.getLogger(SolrQueryBuilder.class);

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
                } else if (object instanceof SolrNull) {
                    SolrNull solrNull = (SolrNull) object;
                    if (solrNull.isTrue()) {
                        sb.append("-");
                    }
                    sb.append(field.getKey() + "[\"\" TO * ]");
                } else if (object instanceof SolrDate) {
                    SolrDate solrDate = (SolrDate) object;
                    if (solrDate.getBegintime() != null && solrDate.getEndtime() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
                        sb.append(field.getKey() + "createdate:[" + sdf.format(solrDate.getBegintime()) + " TO " + sdf.format(solrDate.getEndtime()) + "]");
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
