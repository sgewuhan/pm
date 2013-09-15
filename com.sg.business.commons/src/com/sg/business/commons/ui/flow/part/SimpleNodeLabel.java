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
package com.sg.business.commons.ui.flow.part;

import org.drools.definition.process.Node;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.jbpm.workflow.core.node.StartNode;

import com.sg.business.resource.BusinessResource;

/**
 * A customized Label for SimpleActivities. Primary selection is denoted by
 * highlight and focus rectangle. Normal selection is denoted by highlight only.
 * 
 * @author Daniel Lee
 */
public class SimpleNodeLabel extends Label {

	private boolean selected;
	private boolean hasFocus;
	private Node _node;

	public SimpleNodeLabel(Node node) {
		super();
		this._node = node;
		init();
	}

	private void init() {
		if(_node instanceof StartNode){
			setText("");
			setIconAlignment(Label.CENTER);
			setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_START_24));
		}else if(_node instanceof EndNode){
			setText("");
			setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_FINISH_24));
			setIconAlignment(Label.CENTER);
		}else{
			if(_node instanceof Join){
				setText("");
				setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_JOIN_24));
				setIconAlignment(Label.CENTER);
			}else if(_node instanceof Split){
				setText("");
				setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_SPLIT_24));
				setIconAlignment(Label.CENTER);
			}else if(_node instanceof HumanTaskNode){
				setText(_node.getName());
				setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_HUMAN_24));
				setIconAlignment(Label.LEFT);
				setLabelAlignment(Label.LEFT);
			}else {
				setText(_node.getName());
				setIcon(BusinessResource.getImage(BusinessResource.IMAGE_FLOW_ACTIVITIE_24));
				setIconAlignment(Label.LEFT);
				setLabelAlignment(Label.LEFT);
			}
		}		
	}

	public Node getNode(){
		return _node;
	}

	private Rectangle getSelectionRectangle() {
		Rectangle bounds = getTextBounds();
		bounds.expand(new Insets(2, 2, 0, 0));
		translateToParent(bounds);
		bounds.intersect(getBounds());
		return bounds;
	}

	/**
	 * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (selected) {
			graphics.pushState();
			graphics.setBackgroundColor(ColorConstants.menuBackgroundSelected);
			graphics.fillRectangle(getSelectionRectangle());
			graphics.popState();
			graphics.setForegroundColor(ColorConstants.white);
		}
		if (hasFocus) {
			graphics.pushState();
			graphics.setXORMode(true);
			graphics.setForegroundColor(ColorConstants.menuBackgroundSelected);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.drawFocus(getSelectionRectangle().resize(-1, -1));
			graphics.popState();
		}
		super.paintFigure(graphics);
	}

	/**
	 * Sets the selection state of this SimpleNodeLabel
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
	public void setSelected(boolean b) {
		selected = b;
		repaint();
	}

	/**
	 * Sets the focus state of this SimpleNodeLabel
	 * 
	 * @param b
	 *            true will cause a focus rectangle to be drawn around the text
	 *            of the Label
	 */
	public void setFocus(boolean b) {
		hasFocus = b;
		repaint();
	}

}
