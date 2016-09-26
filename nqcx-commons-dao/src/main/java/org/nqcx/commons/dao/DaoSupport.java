/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.dao;

import java.util.List;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午10:43:02
 */
public abstract class DaoSupport implements DaoInterface {

    public abstract DaoInterface getMapper();

    @Override
    public <T> int insert(T t) {
        return this.getMapper().insert(t);
    }

    @Override
    public <T> int update(T t) {
        return this.getMapper().update(t);
    }

    @Override
    public int deleteById(long id) {
        return this.getMapper().deleteById(id);
    }

    @Override
    public int deleteByIds(long[] ids) {
        return this.getMapper().deleteByIds(ids);
    }

    @Override
    public <T> T getById(long id) {
        return getMapper().getById(id);
    }

    @Override
    public <T> List<T> query(Map<String, Object> map) {
        return getMapper().query(map);
    }

    @Override
    public long queryCount(Map<String, Object> map) {
        return getMapper().queryCount(map);
    }
}
