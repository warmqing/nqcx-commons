/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang.o;

import java.util.Date;

/**
 * Entity Database object
 * @author naqichuan 16/10/9 17:35
 */
public class EntityDBO extends EntityDO {

    protected Date create;
    protected Date modify;

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
