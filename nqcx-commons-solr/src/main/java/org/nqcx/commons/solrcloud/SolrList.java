/*
 * Copyright 2017 ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */

package org.nqcx.commons.solrcloud;

import org.nqcx.commons.lang.o.EntityBO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jiangsiqi on 2017/9/5 10:30.
 */
public class SolrList extends EntityBO {

    private boolean and = false;

    private final List<String> list = new ArrayList<String>();

    public SolrList() {
    }

    public SolrList(boolean and) {
        this.and = and;
    }

    public boolean isAnd() {
        return and;
    }

    public void setAnd(boolean and) {
        this.and = and;
    }

    public SolrList addValue(String e) {
        list.add(e);
        return this;
    }

    public SolrList addAllValues(List<String> c) {
        list.addAll(c);
        return this;
    }

    public String getQueryString(String key) {
        StringBuffer sb = new StringBuffer();
        if (list.size() > 0){
            sb.append("(");
            for (String value : list) {
                if (sb.length() > 1)
                    sb.append(and ? " AND " : " OR ");
                sb.append(key + ":" + value);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // 通过单值构造 SolrList，并生成 AND 查询字符串
        System.out.println(new SolrList(true).addValue("0").addValue("1").getQueryString("bookResource"));
        // 通过单值构造 SolrList，并生成 OR 查询字符串
        System.out.println(new SolrList(false).addValue("0").addValue("1").getQueryString("bookResource"));
        System.out.println(new SolrList().addValue("0").addValue("1").getQueryString("bookResource"));

        List<String> list = new ArrayList<String>();
        list.add("0");
        list.add("1");

        // 通过 list 构造 SolrList ，并生成 OR 查询字符串（AND同单值构造）
        System.out.println(new SolrList(false).addAllValues(list).getQueryString("bookResource"));
    }
}
