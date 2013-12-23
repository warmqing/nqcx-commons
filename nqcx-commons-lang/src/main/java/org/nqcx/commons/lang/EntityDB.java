/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.domain;

import java.util.Date;

/**
 * 
 * @author nqcx 2013-4-3 下午6:02:53
 * 
 */
public class EntityDB extends EntityBase {

	private static final long serialVersionUID = -7327673225635624581L;

	protected Date createTime;

	protected Date modifyTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
