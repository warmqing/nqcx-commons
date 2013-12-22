/* 
 * Copyright 2012-2013 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.lang.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * tree builder
 * 
 * @author huangbaoguang 2013-3-27 下午5:02:48
 * 
 * @param <O>
 */
public class TreeBuilder<O> implements Cloneable, Serializable {

	private static final long serialVersionUID = -3889238862426715948L;

	private final Map<String, NodeData<O>> nodeMap = new LinkedHashMap<String, NodeData<O>>();

	public void put(NodeBuilder nodeBuilder) {
		if (nodeBuilder == null)
			return;
		if (containsNodeId(nodeBuilder.getNodeId()))
			return;

		NodeData<O> dataNode = new NodeData<O>(nodeBuilder);
		nodeMap.put(dataNode.getNodeId(), dataNode);

		// 关联上级
		relateParentNode(dataNode);

		// 关联下级
		relateChildNodes(dataNode);
	}

	public O get(String nodeId) {
		return getNode(nodeId) != null ? getNode(nodeId).getObject()
				: null;
	}

	private void relateParentNode(NodeData<O> dataNode) {
		NodeData<O> parent = getNode(dataNode.getParentNodeId());
		if (parent != null) {
			parent.setChildNode(dataNode);
			dataNode.setParentNode(parent);
		}
	}

	private void relateChildNodes(NodeData<O> dataNode) {
		// 关联下级
		Set<String> nodeIds = nodeMap.keySet();
		for (String nodeId : nodeIds) {
			NodeData<O> dataNodeTmp = nodeMap.get(nodeId);
			if (dataNode.getNodeId().equals(dataNodeTmp.getParentNodeId())) {
				dataNode.setChildNode(dataNodeTmp);
				dataNodeTmp.setParentNode(dataNode);
			}
		}

	}

	public boolean containsNodeId(String nodeId) {
		return nodeMap.containsKey(nodeId);
	}

	public void clean() {
		nodeMap.clear();
	}

	public int size() {
		return nodeMap.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public NodeData<O> getNode(String nodeId) {
		return nodeMap.get(nodeId);
	}

	public List<NodeData<O>> getNodesByParentId(String parentId) {
		if (parentId == null)
			return null;

		List<NodeData<O>> list = new ArrayList<NodeData<O>>();
		Set<String> nodeIds = nodeMap.keySet();
		for (String nodeId : nodeIds) {
			NodeData<O> dataNodeTmp = nodeMap.get(nodeId);
			if (parentId.equals(dataNodeTmp.getParentNodeId())) {
				list.add(dataNodeTmp);
			}
		}
		return list;
	}

	public static void main(String[] args) {
		/*
		 * TreeBuilder<MyO> t = new TreeBuilder<MyO>();
		 * 
		 * MyO n1 = new MyO(); n1.setNodeId("1"); n1.setParentNodeId("0");
		 * n1.setJxx("ni hao kcdg");
		 * 
		 * t.put(n1);
		 * 
		 * MyO n2 = new MyO(); n2.setNodeId("2"); n2.setParentNodeId("1");
		 * n2.setJxx("ni hao kcdg");
		 * 
		 * t.put(n2);
		 * 
		 * MyO n3 = new MyO(); n3.setNodeId("3"); n3.setParentNodeId("2");
		 * n3.setJxx("ni hao kcdg");
		 * 
		 * t.put(n3);
		 * 
		 * System.out.println(t.get("1")); System.out.print(t.getNode("1"));
		 */
	}

	/*
	 * public static class MyO extends NodeBase implements NodeBuilder { private
	 * static final long serialVersionUID = 2900176936172699183L;
	 * 
	 * private String jxx;
	 * 
	 * public String getJxx() { return jxx; }
	 * 
	 * public void setJxx(String jxx) { this.jxx = jxx; } }
	 */
}
