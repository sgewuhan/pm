package com.sg.business.commons.flow.model;

import java.util.Map;

import org.drools.definition.process.Node;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;

public class NodeActivity extends Activity {

	private Rectangle bounds;
	private Node node;
	private boolean startNode;
	private boolean endNode;

	public NodeActivity(Node node) {
		String name;
		if (node instanceof StartNode) {
			name = "¿ªÊ¼";
			startNode = true;
		} else if (node instanceof EndNode) {
			name = "½áÊø";
			endNode = true;
		} else {
			name = node.getName();
		}
		setName(name);
		this.node = node;
		initialBounds();
	}

	private void initialBounds() {
		Map<String, Object> data = node.getMetaData();
		Object x = data.get("x");
		Object y = data.get("y");
		Object width = data.get("width");
		Object height = data.get("height");
		if ((x instanceof Integer) && (y instanceof Integer)
				&& (width instanceof Integer) && (height instanceof Integer)) {
			this.bounds = new Rectangle((Integer) x, (Integer) y,
					(Integer) width, (Integer) height);
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean isStartNode() {
		return startNode;
	}

	public boolean isEndNode() {
		return endNode;
	}
	
	public Node getNode(){
		return node;
	}

}
