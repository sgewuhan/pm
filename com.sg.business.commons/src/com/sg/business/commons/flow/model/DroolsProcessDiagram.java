/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.sg.business.commons.flow.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.definition.process.NodeContainer;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.StartNode;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;

/**
 * 
 * @author hudsonr Created on Jun 30, 2003
 */
public class DroolsProcessDiagram extends ActivityDiagram {

	private BasicBSONList history;

	public DroolsProcessDiagram(DroolsProcessDefinition procDefinition,BasicBSONList procHistory) {
		super();
		this.history = procHistory;
		if(procDefinition!=null){
			readInput((NodeContainer) procDefinition.getProcess(), this);
		}
	}

	private boolean isDisplayNode(Node node) {
		return node instanceof StartNode || node instanceof EndNode
				|| node instanceof HumanTaskNode;
	}

	private void readInput(NodeContainer nodeContainer, ParallelActivity diagram) {

		// 1.构造节点
		Map<Node, Activity> nodemap = new HashMap<Node, Activity>();
		Map<Node, Activity> displayNodes = new HashMap<Node, Activity>();

		Node[] nodes = nodeContainer.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			Activity activity = new NodeActivity(nodes[i]);
			activity.setHistory(getNodeHistory(nodes[i]));
			nodemap.put(nodes[i], activity);
			if (isDisplayNode(nodes[i])) {
				diagram.addChild(activity);
				displayNodes.put(nodes[i], activity);
			}
		}

		Iterator<Node> iter = displayNodes.keySet().iterator();
		while(iter.hasNext()){
			Node from = iter.next();
			Activity _from = nodemap.get(from);
			List<Node> to = getNextDisplayNodes(from);
			for(int i=0;i<to.size();i++){
				Activity _to = nodemap.get(to.get(i));
				new Transition(_from, _to);
			}
		}
		
	}

	private List<Node> getNextDisplayNodes(Node from) {
		List<Node> result = new ArrayList<Node>();
		
		Map<String, List<Connection>> conns = from.getOutgoingConnections();
		if(conns==null||conns.isEmpty()){
			return result;
		}
		
		Collection<List<Connection>> vs = conns.values();
		
		if(vs==null||vs.isEmpty()){
			return result;
		}
		
		Iterator<List<Connection>> iter = vs.iterator();
		while (iter.hasNext()) {
			List<Connection> sublist = iter.next();
			if (sublist == null) {
				continue;
			}
			for (int j = 0; j < sublist.size(); j++) {
				Connection connection = sublist.get(j);
				Node to = connection.getTo();
				if(isDisplayNode(to)){
					result.add(to);
				}else{
					result.addAll(getNextDisplayNodes(to));
				}
			}
		}

		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> getNodeHistory(Node _node){
		if (history == null) {
			return null;
		}
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		String nodeName = _node.getName();
		for (int i = 0; i < history.size(); i++) {
			DBObject historyItem = (DBObject) history.get(i);
			if (nodeName.equals(historyItem.get("taskname"))){ //$NON-NLS-1$
				result.add(historyItem.toMap());
			}
		}
		return result;
	}
}
