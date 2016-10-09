/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang;

import org.nqcx.commons.lang.id.IdHelper;

import java.util.Date;

/**
 * Entity data object
 *
 * @author naqichuan 2014年8月14日 上午11:00:41
 */
public abstract class EntityDO extends EntityBO implements EntityIO {

    // id
    protected long id;

    protected Date create;
    protected Date modify;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getShId() {
        return IdHelper.toAlphabet(this.id);
    }

    @Override
    public void setShId(String shId) {
        // Do nothing
    }

    @Override
    public Date getCreate() {
        return create;
    }

    @Override
    public void setCreate(Date create) {
        this.create = create;
    }

    @Override
    public Date getModify() {
        return modify;
    }

    @Override
    public void setModify(Date modify) {
        this.modify = modify;
    }
}
