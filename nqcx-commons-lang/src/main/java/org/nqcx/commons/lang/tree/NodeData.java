/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构和数据节点
 * 
 * @author huangbaoguang 2013-3-27 下午5:02:23
 * 
 * @param <O>
 */
public class NodeData<O> extends NodeBase implements NodeBuilder {

	private static final long serialVersionUID = 2534433535758557656L;

	// 存储节点对象
	private O object;
	// 父节点
	private NodeData<O> parentNode;
	// 所有子节点
	private List<NodeData<O>> childNodes;

	public NodeData() {

	}

	@SuppressWarnings("unchecked")
	public NodeData(NodeBuilder nodeBuilder) {
		setNodeId(nodeBuilder.getNodeId());
		setParentNodeId(nodeBuilder.getParentNodeId());

		this.object = (O) nodeBuilder;
	}

	/**
	 * has parent
	 * 
	 * @return
	 */
	public boolean hasParent() {
		return parentNode != null;
	}

	/**
	 * has childs
	 * 
	 * @return
	 */
	public boolean hasChilds() {
		return childNodes != null && childNodes.size() > 0;
	}

	public O getObject() {
		return object;
	}

	public void setObject(O o) {
		this.object = o;
	}

	public NodeData<O> getParentNode() {
		return parentNode;
	}

	public void setParentNode(NodeData<O> parentNode) {
		this.parentNode = parentNode;
	}

	public List<NodeData<O>> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<NodeData<O>> childNodes) {
		this.childNodes = childNodes;
	}

	public void setChildNode(NodeData<O> nodeData) {
		if (childNodes == null)
			childNodes = new ArrayList<NodeData<O>>();
		childNodes.add(nodeData);
	}
}
