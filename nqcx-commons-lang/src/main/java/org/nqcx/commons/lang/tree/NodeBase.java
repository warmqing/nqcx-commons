/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.tree;

import java.io.Serializable;

/**
 * Node 基础类
 * 
 * @author huangbaoguang 2013-3-27 下午3:51:27
 * 
 */
public class NodeBase implements Serializable {

	private static final long serialVersionUID = -5838334158312443998L;

	private String nodeId;
	private String parentNodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}
}
