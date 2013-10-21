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
package com.sg.business.commons.flow.part;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.sg.business.commons.flow.figures.SimpleActivityLabel;
import com.sg.business.commons.flow.model.NodeActivity;
import com.sg.business.commons.ui.flow.ProcessHistoryTable;
import com.sg.business.resource.BusinessResource;

/**
 * @author hudsonr Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart {

	public SimpleActivityPart() {
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s,
			Map map) {
		Node n = new Node(this, s);
		n.outgoingOffset = getAnchorOffset();
		n.incomingOffset = getAnchorOffset();
		n.width = getFigure().getPreferredSize().width;
		n.height = getFigure().getPreferredSize().height;
		n.setPadding(new Insets(10, 8, 10, 12));
		map.put(this, n);
		graph.nodes.add(n);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Label l = new SimpleActivityLabel();
		Object model = getModel();
		if (model instanceof NodeActivity) {
			NodeActivity activity = (NodeActivity) model;
			l.setSize(activity.getBounds().width, activity.getBounds().y);
			if (activity.isStartNode()) {
				l.setIcon(BusinessResource
						.getImage(BusinessResource.IMAGE_FLOW_START_24));
			} else if (activity.isEndNode()) {
				l.setIcon(BusinessResource
						.getImage(BusinessResource.IMAGE_FLOW_FINISH_24));
			} else if (activity.isCompleted()) {
				l.setIcon(BusinessResource
						.getImage(BusinessResource.IMAGE_FLOW_ACTIVITY_FINISH_24));
			} else {
				l.setIcon(BusinessResource
						.getImage(BusinessResource.IMAGE_FLOW_ACTIVITY_UNFINISH_24));
			}

		}
		l.setLabelAlignment(PositionConstants.LEFT);
		l.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent me) {
				getViewer().setSelection(
						new StructuredSelection(
								new Object[] { SimpleActivityPart.this }));
			}

			@Override
			public void mouseReleased(MouseEvent me) {

			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
//				showHistory(me.getLocation());
			}
		});

		return l;
	}

	protected void showHistory(Point location) {
		Object model = getModel();
		if (model instanceof NodeActivity) {
			List<Map<String, Object>> history = ((NodeActivity) model)
					.getHistory();
			if (history == null || history.isEmpty()) {
				return;
			}
			Shell shell = new Shell(getViewer().getControl().getShell(),SWT.RESIZE | SWT.BORDER);
			shell.setLayout(new FillLayout());
			// 创建历史记录
			final ProcessHistoryTable pt = new ProcessHistoryTable(shell);
			pt.setInput(null,history);
			
			shell.setLocation(location.x,location.y);
			
			shell.open();
		}
	}

	int getAnchorOffset() {
		return 12;
	}

	protected void performDirectEdit() {
		if (manager == null) {
			Label l = (Label) getFigure();
			manager = new ActivityDirectEditManager(this, TextCellEditor.class,
					new ActivityCellEditorLocator(l), l);
		}
		manager.show();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		((Label) getFigure()).setText(getActivity().getName());
	}

	public String getName() {
		return getActivity().getName();
	}

}
