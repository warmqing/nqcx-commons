/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.service;

import org.nqcx.commons.dao.DaoInterface;
import org.nqcx.commons.lang.o.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午10:48:24
 */
public abstract class ServiceSupport implements ServiceInterface {

    protected abstract DaoInterface getDao();

    @Override
    public <T> DTO add(T t) {
        try {
            int affected = getDao().insert(t);
            return new DTO(affected > 0).putResult("affected", affected).setObject(beforeAdd(t));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport add error", e);
        }
    }

    /**
     * 添加之前处理
     *
     * @param t
     * @param <T>
     * @return
     */
    protected <T> T beforeAdd(T t) {
        return t;
    }

    @Override
    public <T> DTO modify(T t) {
        try {
            return new DTO(true).putResult("changed", getDao().update(beforeModify(t))).setObject(t);// 如果返回0，表示数据未被修改
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport modify error", e);
        }
    }

    /**
     * 修改之前处理
     *
     * @param t
     * @param <T>
     * @return
     */
    protected <T> T beforeModify(T t) {
        return t;
    }

    @Override
    public DTO deleteOne(long id) {
        try {
            return new DTO(true).putResult("affected", getDao().deleteById(id));// 如果返回0，表示数据未被删除
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteOne error", e);
        }
    }

    @Override
    public DTO deleteMultiple(long[] ids) {
        try {
            return new DTO(true).putResult("affected", getDao().deleteByIds(ids));// 如果返回0，表示数据未被删除
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteMultiple error", e);
        }
    }

    /**
     * 根据ID查询一条数据
     */
    @Override
    public <T> T getById(long id) {
        try {
            return afterGet((T) getDao().getById(id));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport getById error", e);
        }
    }

    /**
     * 根据一条数据后处理
     *
     * @param t
     * @param <T>
     * @return
     */
    protected <T> T afterGet(T t) {
        return t;
    }

    /**
     * 根据条件查询数据列表
     */
    @Override
    public <T> List<T> query(DTO dto) {
        try {
            dto.setSuccess(true);

            if (dto.getPage() != null)
                dto.getPage().setTotalCount(this.queryCount(dto));

            dto.setList(afterQuery(getDao().query(parseParams(dto))));

            return dto.getList();
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport query error", e);
        }
    }

    /**
     * 查询数据列表后处理
     *
     * @param list
     * @param <T>
     * @return
     */
    protected <T> List<T> afterQuery(List<T> list) {
        return list;
    }

    /**
     * 根据条件查询数据列表
     */
    @Override
    public DTO rmiQuery(DTO dto) {
        this.query(dto);
        return dto;
    }

    /**
     * 根据条件查询数据总数
     */
    @Override
    public long queryCount(DTO dto) {
        try {
            return getDao().queryCount(parseParams(dto));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport queryCount error", e);
        }
    }

    /**
     * 解析参数，返回 mapper 需要的参数
     *
     * @param dto
     * @return
     */
    protected <T> Map<String, Object> parseParams(DTO dto) {
        if (dto == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();
        if (dto.getParamsMap() != null)
            map.putAll(dto.getParamsMap());
        if (dto.getSort() != null)
            map.put("order", dto.getSort().getOrder());
        else
            map.put("order", "");

        if (dto.getPage() != null) {
            map.put("pageTag", 1);
            map.put("position", dto.getPage().getPosition());
        } else {
            map.put("pageTag", 0);
            map.put("position", "");
        }
        return map;
    }
}
