package com.sg.business.commons.ui.flow;

import org.drools.definition.process.Node;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jbpm.workflow.core.node.HumanTaskNode;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.part.SimpleNodeLabel;

public class ActiviteSelecter extends Composite {

	public interface INodeSelectionListener {

		void selectionChange(NodeAssignment nodeAssignment);

	}

	private ProcessCanvas processCanvas;

	private ProcessTableViewer processTable;

	private ListenerList listeners = new ListenerList();

	public ActiviteSelecter(Composite parent) {
		super(parent, SWT.NONE);
		createContent(this);
	}

	protected void createContent(Composite parent) {
		setLayout(new FillLayout());

		CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP);
		CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		cti1.setText("流程图");
		Control control1 = createProcessCanvasTab(tabFolder);
		cti1.setControl(control1);

		CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		cti2.setText("活动表");
		Control control2 = createProcessTableTab(tabFolder);
		cti2.setControl(control2);
		
		tabFolder.setSelection(cti1);
		
	}

	private Control createProcessTableTab(Composite parent) {
		processTable = new ProcessTableViewer(parent);
		processTable
				.addPostSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection is = (IStructuredSelection) event
								.getSelection();
						if (is != null && !is.isEmpty()) {
							NodeAssignment nodeAssignment = (NodeAssignment) is
									.getFirstElement();
							fireNodeSelectionChange(nodeAssignment);
						} else {
							fireNodeSelectionChange(null);
						}
					}
				});
		return processTable.getControl();
	}

	protected void fireNodeSelectionChange(NodeAssignment nodeAssignment) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((INodeSelectionListener)lis[i]).selectionChange(nodeAssignment);
		}
	}

	private Control createProcessCanvasTab(Composite parent) {
		// 创建流程图画板
		processCanvas = new ProcessCanvas(parent,SWT.NONE);
		processCanvas.addNodeSelectListener(new INodeSelectListener() {
			@Override
			public void select(Object source) {
				if (source instanceof SimpleNodeLabel) {
					SimpleNodeLabel label = (SimpleNodeLabel) source;
					Node node = label.getNode();
					if (node instanceof HumanTaskNode) {
						NodeAssignment na = new NodeAssignment(
								(HumanTaskNode) node);
						fireNodeSelectionChange(na);
					} else {
						fireNodeSelectionChange(null);
					}
				} else {
					fireNodeSelectionChange(null);
				}

			}
		});
		return processCanvas;
	}

	public void setInput(DroolsProcessDefinition processDefinition) {
		processCanvas.setInput(processDefinition);
		processTable.setDataInput(processDefinition);
	}

	public void addListener(INodeSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(INodeSelectionListener listener) {
		listeners.remove(listener);
	}

}
