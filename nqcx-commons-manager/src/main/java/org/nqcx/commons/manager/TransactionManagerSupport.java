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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author naqichuan Dec 23, 2013 10:27:24 PM
 * 
 */
public abstract class TransactionManagerSupport extends ManagerSupport {

	@Autowired
	protected DataSourceTransactionManager transactionManager;

	/**
	 * 取得事务模板
	 * 
	 * @author 黄保光 Sep 24, 2013 2:21:35 PM
	 * @return
	 */
	public TransactionTemplate getTransactionTemplate() {
		return new TransactionTemplate(transactionManager);
	}

	/**
	 * del重名，在事务回调中使用 this 和 supper 不能正确调用方法，用于中转
	 * 
	 * @author 黄保光 Sep 24, 2013 3:00:23 PM
	 * @param id
	 * @return
	 */
	protected int delForInside(long id) {
		return super.del(id);
	}

	/**
	 * del重名，在事务回调中使用 this 和 supper 不能正确调用方法，用于中转
	 * 
	 * @author 黄保光 Sep 24, 2013 3:02:27 PM
	 * @param ids
	 * @return
	 */
	protected int delForInside(long[] ids) {
		return super.del(ids);
	}

	@Override
	protected int del(final long[] ids) {
		return getTransactionTemplate().execute(
				new TransactionCallback<Integer>() {

					@Override
					public Integer doInTransaction(TransactionStatus arg0) {
						return delForInside(ids);
					}
				}).intValue();
	}
}
