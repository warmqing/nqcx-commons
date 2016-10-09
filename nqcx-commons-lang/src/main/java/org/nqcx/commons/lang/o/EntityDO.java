/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.o;

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


}
