package com.sg.business.commons.ui.flow;

import org.drools.definition.process.Node;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import org.jbpm.workflow.core.node.HumanTaskNode;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.part.SimpleNodeLabel;
import com.sg.widgets.part.SimpleSection;

/**
 * 流程显示信息，包括流程图和活动表
 * 
 * @author jinxitao
 * 
 */
public class ActivitySelecter extends Composite {

	public interface INodeSelectionListener {

		void selectionChange(NodeAssignment nodeAssignment);

	}

	/**
	 * 流程图画板
	 */
	private ProcessCanvas processCanvas;

	/**
	 * 流程图活动表
	 */
	private ProcessTableViewer processTable;

	private ListenerList listeners = new ListenerList();

	public ActivitySelecter(Composite parent) {
		super(parent, SWT.NONE);
		createContent(this);
	}

	/**
	 * 构建流程详细信息显示内容
	 * 
	 * @param parent
	 */
	protected void createContent(Composite parent) {
		setLayout(new GridLayout());

		Section section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section.setText("任务列表");
		Control control = createProcessTableTab(section);
		section.setClient(control);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		section = new SimpleSection(parent, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);
		section.setText("流程图");
		control = createProcessCanvasTab(section);
		section.setClient(control);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		//
		//
		//
		// // 显示流程图
		// CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP | SWT.BORDER);
		// CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		// cti1.setText("流程图");
		// Control control1 = createProcessCanvasTab(tabFolder);
		// cti1.setControl(control1);
		//
		// // 显示活动表
		// CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		// cti2.setText("活动表");
		// Control control2 = createProcessTableTab(tabFolder);
		// cti2.setControl(control2);
		//
		// tabFolder.setSelection(cti1);

	}

	/**
	 * 创建流程活动表
	 * 
	 * @param parent
	 * @return Control
	 */
	private Control createProcessTableTab(Composite parent) {
		processTable = new ProcessTableViewer(parent);
		processTable
				.addPostSelectionChangedListener(new ISelectionChangedListener() {
					// 监听活动选择改变事件
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
			((INodeSelectionListener) lis[i]).selectionChange(nodeAssignment);
		}
	}

	/**
	 * 创建流程图
	 * 
	 * @param parent
	 * @return Control
	 */
	private Control createProcessCanvasTab(Composite parent) {
		// 创建流程图画板
		processCanvas = new ProcessCanvas(parent, SWT.NONE);
		processCanvas.addNodeSelectListener(new INodeSelectListener() {
			// 监听活动选择改变事件
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

	/**
	 * 设置流程图和活动表的数据源
	 * 
	 * @param processDefinition
	 */
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
