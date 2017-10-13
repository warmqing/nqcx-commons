/*
 * Copyright 2017 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.solrcloud;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.nqcx.commons.lang.o.DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author wangqing 17/8/17 下午3:36
 */
public abstract class SolrCloudSupport {

    public abstract SolrCloudCollection getSolrCollection();

    public abstract SolrCloudClientFactory getSolrCloudClientFactory();

    private final static Logger logger = LoggerFactory.getLogger(SolrCloudSupport.class);

    /**
     * 获取solrcloud客户端
     *
     * @return
     */
    public CloudSolrClient getClient() {
        return this.getSolrCloudClientFactory() == null ? null : this.getSolrCloudClientFactory().getCloudSolrClient(getSolrCollection(), false);
    }


    /**
     * 添加单个文档
     *
     * @param bean
     * @param <T>
     */
    public <T> void addBean(T bean) {
        try {
            getClient().addBean(bean);
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport addBean error", e);
        }
    }


    /**
     * 添加文档集合
     *
     * @param beans
     * @param <T>
     */
    public <T> void addBeans(Collection<T> beans) {
        try {
            getClient().addBeans(beans);
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport addBeans error", e);
        }
    }

    /**
     * 根据id删除文档
     *
     * @param id
     */
    public void deleteOne(String id) {
        try {
            getClient().deleteById(id);
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport deleteOne error", e);
        }
    }

    /**
     * 批量删除文档
     *
     * @param ids
     */
    public void deleteMulti(List<String> ids) {
        try {
            getClient().deleteById(ids);
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport deleteMulti error", e);
        }
    }

    /**
     * 删除全部文档
     */
    public void deleteAll() {
        try {
            getClient().deleteByQuery("*:*");
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport deleteAll error", e);
        }
    }

    /**
     * 查询
     *
     * @param dto
     * @param type
     * @param <T>
     */
    public <T> DTO searchBeans(DTO dto, Class<T> type, SolrQuery query) {
        if (getClient() == null)
            return null;

        try {
            QueryResponse qrsp = getClient().query(SolrQueryBuilder.dto2query(dto, query));
            if (dto.getPage() != null)
                dto.getPage().setTotalCount(qrsp.getResults().getNumFound());

            if (type == null)
                dto.setList(qrsp.getResults());
            else
                dto.setList(qrsp.getBeans(type));

            dto.setSuccess(true);
            return dto;
        } catch (Exception e) {
            throw new SolrCloudSupportException("SolrCloudSupport searchBeans error", e);
        }
    }

    /**
     * @param dto
     * @param type
     * @param <T>
     * @return
     */
    public <T> DTO searchBeans(DTO dto, Class<T> type) {
        return this.searchBeans(dto, type, null);
    }

}
