package com.sg.business.commons.ui.flow;

import org.drools.definition.process.Node;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import org.jbpm.workflow.core.node.HumanTaskNode;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.flow.model.DroolsProcessDiagram;
import com.sg.business.commons.flow.model.NodeActivity;
import com.sg.business.commons.flow.parts.ActivityPartFactory;
import com.sg.business.commons.flow.parts.SimpleActivityPart;
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
	 * 流程图活动表
	 */
	private ProcessTableViewer processTable;

	private ListenerList listeners = new ListenerList();

	private ScrollingGraphicalViewer processViewer;

	private SimpleSection section1;

	private SimpleSection section2;

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

		section1 = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section1.setText("任务列表");
		Control control = createProcessTableTab(section1);
		section1.setClient(control);
		section1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		section2 = new SimpleSection(parent, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);
		section2.setText("流程图");
		control = createProcessCanvasTab(section2);
		section2.setClient(control);
		section2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

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
		processTable = new ProcessTableViewer(parent,SWT.FULL_SELECTION|SWT.V_SCROLL);
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
		
		processViewer = new ScrollingGraphicalViewer();
		processViewer.setRootEditPart(new ScalableRootEditPart());
		processViewer.setEditPartFactory(new ActivityPartFactory());
		processViewer.createControl(parent);

		
		processViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection is = (StructuredSelection) event
						.getSelection();
				if (is == null || is.isEmpty()) {
					return;
				}
				Object element = is.getFirstElement();

				if (element instanceof SimpleActivityPart) {
					Object model = ((SimpleActivityPart) element).getModel();
					if(model instanceof NodeActivity){
						Node node = ((NodeActivity) model).getNode();
						if (node instanceof HumanTaskNode) {
							NodeAssignment na = new NodeAssignment(
									(HumanTaskNode) node);
							fireNodeSelectionChange(na);
							return;
						}
					}
				}
				
				fireNodeSelectionChange(null);

			}
		});
		return processViewer.getControl();
	}

	/**
	 * 设置流程图和活动表的数据源
	 * 
	 * @param processDefinition
	 */
	public void setInput(DroolsProcessDefinition processDefinition) {
		DroolsProcessDiagram diagram = new DroolsProcessDiagram(processDefinition,
				null);
		processViewer.setContents(diagram);
		processTable.setDataInput(processDefinition);
	}

	public void addListener(INodeSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(INodeSelectionListener listener) {
		listeners.remove(listener);
	}


}
