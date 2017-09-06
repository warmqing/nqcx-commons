/*
 * Copyright 2017 ChineseAll Inc. All right reserved. This software is the confidential and proprietary information of ChineseAll Inc.
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ChineseAll Inc.
 */

package org.nqcx.commons.solrcloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @Author Jiangsiqi on 2017/9/5 10:30.
 */
public class SolrList extends ArrayList {


    private boolean and = false;

    public SolrList() {
        super();
    }

    public SolrList(boolean and) {
        super();
        this.and = and;
    }

    public boolean getAnd() {
        return and;
    }

    public void setAnd(boolean and) {
        this.and = and;
    }

    public SolrList addValue(String e) {
        super.add(e);
        return this;
    }

    public SolrList addAllValues(Collection<String> c) {
        super.addAll(c);
        return this;
    }

    public String getQueryString(String key) {
        Iterator<String> it = super.iterator();
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        while (it.hasNext()) {
            String value = it.next();
            if (sb.length() > 1)
                sb.append(and ? " AND " : " OR ");
            sb.append(key + ":" + value);
        }
        sb.append(")");
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
