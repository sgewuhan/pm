package com.sg.business.commons.flow.model;

import java.util.Map;

import org.drools.definition.process.Node;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;

import com.sg.business.resource.nls.Messages;

public class NodeActivity extends Activity {

	private Rectangle bounds;
	private Node node;
	private boolean startNode;
	private boolean endNode;

	public NodeActivity(Node node) {
		String name;
		if (node instanceof StartNode) {
			name = Messages.get().NodeActivity_0;
			startNode = true;
		} else if (node instanceof EndNode) {
			name = Messages.get().NodeActivity_1;
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
		Object x = data.get("x"); //$NON-NLS-1$
		Object y = data.get("y"); //$NON-NLS-1$
		Object width = data.get("width"); //$NON-NLS-1$
		Object height = data.get("height"); //$NON-NLS-1$
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
