/*
 * Copyright 2015 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.nqcx.commons.lang.sort.SortBO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author naqichuan 15/1/7 23:34
 */
public class SolrSort extends SortBO {

    /**
     * 取得搜索排序字段列表
     *
     * @return
     */
    public List<SolrQuery.SortClause> getSearchOrder() {
        List<SolrQuery.SortClause> list = null;
        String[] os = null;
        if (sort == null || sort.length() == 0 || (os = this.sort.split(",")) == null || os.length == 0)
            return list;

        String tmpField = null;
        list = new ArrayList<SolrQuery.SortClause>();
        for (String o : os) {
            if (o == null || o.length() == 0)
                continue;

            if (o.indexOf("-") == -1 && (tmpField = this.fileds.get(o)) != null && tmpField.length() > 0)
                list.add(new SolrQuery.SortClause(tmpField, SolrQuery.ORDER.asc));
            else if (o.indexOf("-") != -1 && (tmpField = this.fileds.get(o.substring(o.indexOf("-") + 1))) != null && tmpField.length() > 0)
                list.add(new SolrQuery.SortClause(tmpField, SolrQuery.ORDER.desc));
        }
        return list;
    }
}
