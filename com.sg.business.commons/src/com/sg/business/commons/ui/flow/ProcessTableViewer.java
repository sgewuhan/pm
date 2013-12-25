package com.sg.business.commons.ui.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.nls.Messages;

public class ProcessTableViewer extends TableViewer {

	public ProcessTableViewer(Composite parent, int style) {
		super(parent, SWT.FULL_SELECTION | SWT.BORDER | style);
		// getTable().setHeaderVisible(true);
		getTable().setLinesVisible(false);
		setContentProvider(ArrayContentProvider.getInstance());
		setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String name = ((NodeAssignment) element).getNodeName();
				// boolean needAss = ((NodeAssignment)
				// element).isNeedAssignment();
				// if (needAss) {
				// }
				if(((NodeAssignment) element).forceAssignment()){
					return name + "(*)"; //$NON-NLS-1$
				}
				return name;
			}
		});
		// createColumns(this);
	}

	protected void createColumns(ProcessTableViewer processViewer) {

		createActionNameColumn(processViewer);

		createAssignmentTypeColumn(processViewer);

		createRuleColumn(processViewer);

		createParameterColumn(processViewer);

	}

	protected void createParameterColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessTableViewer_1);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.getNodeActorParameter();
			}

		});
	}

	protected void createRuleColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessTableViewer_2);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String name = na.getRuleAssignmentName();
				if (name != null) {
					return name;
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});
	}

	protected void createAssignmentTypeColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessTableViewer_4);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return Messages.get().ProcessTableViewer_5;
				} else if (na.isRuleAssignment()) {
					return Messages.get().ProcessTableViewer_6;
				} else if (na.isStaticActor()) {
					return Messages.get().ProcessTableViewer_7;
				} else if (na.isStaticGroup()) {
					return Messages.get().ProcessTableViewer_8;
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});
	}

	protected void createActionNameColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessTableViewer_10);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((NodeAssignment) element).getNodeName();
			}

		});
	}

	public void setDataInput(DroolsProcessDefinition processDefinition) {
		if (processDefinition == null) {
			setInput(null);
		} else {
			List<NodeAssignment> nodesAssignment = processDefinition
					.getNodesAssignment();
			ArrayList<NodeAssignment> input = new ArrayList<NodeAssignment>();
			for (int i = 0; i < nodesAssignment.size(); i++) {
				NodeAssignment na = nodesAssignment.get(i);
				if (na.isNeedAssignment()&&na.forceAssignment()) {
					input.add(na);
				}
			}
			setInput(input);
		}
	}
}
