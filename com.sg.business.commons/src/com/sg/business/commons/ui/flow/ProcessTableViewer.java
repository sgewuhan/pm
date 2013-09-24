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
					return name+"[��ָ��]";
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
		column.getColumn().setText("ָ�ɲ���");
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
		column.getColumn().setText("��������");
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
		column.getColumn().setText("ָ�����");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return "��ָ̬��";
				} else if (na.isRuleAssignment()) {
					return "����ָ��";
				} else if (na.isStaticActor()) {
					return "��ִ̬����";
				} else if (na.isStaticGroup()) {
					return "��ִ̬����";
				} else {
					return "";
				}
			}
		});
	}

	protected void createActionNameColumn(ProcessTableViewer processViewer) {
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText("�����");
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
