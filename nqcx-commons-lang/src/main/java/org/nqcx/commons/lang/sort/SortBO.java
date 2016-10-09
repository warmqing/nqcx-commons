/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.sort;

import org.nqcx.commons.lang.EntityBO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午11:03:31
 */
public class SortBO extends EntityBO {

    // 排序字段列表，在子类中定义，KEY为序号，value为字段名
    protected Map<String, String> fileds = new HashMap<String, String>(5);
    // 排序字段，用半角逗号（,）分隔，每个字段第一个字母表示排序顺序（-表示反序）
    protected String sort;

//    /**
//     * 取得一个新实例
//     *
//     * @return
//     */
//    public static SortBase newInstance() {
//        return new SortBase();
//    }

    /**
     * 取得用于排序的字段列表
     *
     * @return
     */
    public Map<String, String> getFileds() {
        return fileds;
    }

    /**
     * 设置用于排序的字段列表
     *
     * @param fileds
     * @return
     */
    public SortBO setFileds(Map<String, String> fileds) {
        this.fileds = fileds;
        return this;
    }

    /**
     * 添加排序字段
     *
     * @param key
     * @param field
     * @return
     */
    public SortBO putField(String key, String field) {
        this.fileds.put(key, field);
        return this;
    }

    /**
     * 取得排序字段
     *
     * @return
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置排序字段
     *
     * @param sort
     * @return
     */
    public SortBO setSort(String sort) {
        this.sort = sort;
        return this;
    }

    /**
     * 取反其中指定的字段
     *
     * @param sort
     * @param field
     * @return
     */

    public String opposite(String sort, String field) {
        if (field == null || field.length() == 0)
            return "";
        if (sort == null || sort.length() == 0) {
            return field;
        }

        String[] sortArray = sort.split(",");
        String[] newArray = null;
        int len = sortArray.length;
        if (len == 0)
            return field;

        for (int i = 0; i < len; i++) {
            if (field.equals(sortArray[i])) {
                newArray = new String[len];
                System.arraycopy(sortArray, 0, newArray, 0, len);
                System.arraycopy(newArray, 0, newArray, 1, i);
                newArray[0] = "-" + field;
                break;
            }
            if (("-" + field).equals(sortArray[i])) {
                newArray = new String[len];
                System.arraycopy(sortArray, 0, newArray, 0, len);
                System.arraycopy(newArray, 0, newArray, 1, i);
                newArray[0] = "";
                break;
            }
            if (i == (len - 1)) {
                newArray = new String[len + 1];
                System.arraycopy(sortArray, 0, newArray, 1, len);
                newArray[0] = field;
            }
        }
        StringBuffer sortBuffer = new StringBuffer();
        for (int i = 0; i < newArray.length; i++) {
            if (i > 0)
                sortBuffer.append(",");
            sortBuffer.append(newArray[i]);
        }
        return this.sort = sortBuffer.toString();
    }

    public String getOrder() {
        String order = "";
        if (sort != null && sort.length() > 0) {
            String[] os = this.sort.split(",");
            if (os != null && os.length > 0) {
                for (String o : os) {
                    if (o != null && o.length() > 0) {
                        String tmpField = null;
                        if (order != null && order.length() > 0)
                            order += ",";

                        if (o.indexOf("-") == -1) {
                            tmpField = this.fileds.get(o);
                            if (tmpField != null && tmpField.length() > 0) {
                                order += tmpField;
                            }
                        } else {
                            tmpField = this.fileds.get(o.substring(o.indexOf("-") + 1));
                            if (tmpField != null && tmpField.length() > 0) {
                                order += tmpField;
                                order += " DESC";
                            }
                        }
                    }
                }
            }
        }
        if (order != null && order.length() > 0)
            return "ORDER BY " + order;
        else return "";
    }

    public static void main(String[] args) {
        System.out.println(new SortBO().putField("0", "id").putField("1", "id1").putField("2", "id2").setSort("-1,-2").getOrder());
    }
}
