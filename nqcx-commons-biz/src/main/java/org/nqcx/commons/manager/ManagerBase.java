/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author nqcx 2013-1-13 下午9:01:13
 * 
 */
public abstract class ManagerBase {

	@Autowired
	protected DataSourceTransactionManager transactionManager;

	public TransactionTemplate getTransactionTemplate() {
		return new TransactionTemplate(transactionManager);
	}
}
