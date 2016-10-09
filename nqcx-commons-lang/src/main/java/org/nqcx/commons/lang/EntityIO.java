/*
 * Copyright 2016 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.commons.lang;

import java.util.Date;

/**
 * Entity interface object
 *
 * @author naqichuan 16/10/9 11:00
 */
public interface EntityIO {

    long getId();

    void setId(long id);

    // Get short id
    String getShId();

    // Set short id, do nothing
    void setShId(String shId);

    Date getCreate();

    void setCreate(Date create);

    Date getModify();

    void setModify(Date modify);
}
