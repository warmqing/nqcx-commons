/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang;

import java.util.Date;

/**
 * @author naqichuan 2014年8月14日 上午11:00:41
 */
public abstract class EntityDB extends EntityBase {

    protected Date create;
    protected Date modify;

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public long getCreateLong() {
        return create == null ? 0 : create.getTime();
    }

    public void setCreateLong(long create) {
        if (create <= 0)
            return;
        this.create = new Date(create);
    }

    public Date getModify() {
        return modify;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }

    public long getModifyLong() {
        return modify == null ? 0 : modify.getTime();
    }

    public void setModifyLong(long modify) {
        if (modify <= 0)
            return;
        this.modify = new Date(modify);
    }

}
