package com.sg.business.commons.ui.flow;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;

public class ProcessTableViewer extends TableViewer {

	public ProcessTableViewer(Composite parent) {
		super(parent, SWT.FULL_SELECTION|SWT.BORDER);
//		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(false);
		setContentProvider(ArrayContentProvider.getInstance());
		setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				String name = ((NodeAssignment) element).getNodeName();
				boolean needAss = ((NodeAssignment) element).isNeedAssignment();
				if(needAss){
					return name+"[需指派]";
				}else{
					return name;
				}
						
			}
		});
//		createColumns(this);
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
		column.getColumn().setText("指派参数");
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
		column.getColumn().setText("规则名称");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String name = na.getRuleAssignmentName();
				if (name != null) {
					return name;
				} else {
					return "";
				}
			}
		});
	}

	protected void createAssignmentTypeColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("指派类别");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return "动态指派";
				} else if (na.isRuleAssignment()) {
					return "规则指派";
				} else if (na.isStaticActor()) {
					return "静态执行人";
				} else if (na.isStaticGroup()) {
					return "静态执行组";
				} else {
					return "";
				}
			}
		});
	}

	protected void createActionNameColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText("活动名称");
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
			setInput(processDefinition.getNodesAssignment());
		}
	}
}
