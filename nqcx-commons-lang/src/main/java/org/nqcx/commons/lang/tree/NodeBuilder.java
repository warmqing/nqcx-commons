/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.tree;

/**
 * 
 * Node接口类，数据类需要实现该接口
 * 
 * @author huangbaoguang 2013-3-27 下午4:27:01
 * 
 */
public interface NodeBuilder {

	public String getNodeId();

	public void setNodeId(String nodeId);

	public String getParentNodeId();

	public void setParentNodeId(String parentNodeId);
}
