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
 * ������ʾ��Ϣ����������ͼ�ͻ��
 * 
 * @author jinxitao
 * 
 */
public class ActivitySelecter extends Composite {

	public interface INodeSelectionListener {

		void selectionChange(NodeAssignment nodeAssignment);

	}

	/**
	 * ����ͼ����
	 */
	private ProcessCanvas processCanvas;

	/**
	 * ����ͼ���
	 */
	private ProcessTableViewer processTable;

	private ListenerList listeners = new ListenerList();

	public ActivitySelecter(Composite parent) {
		super(parent, SWT.NONE);
		createContent(this);
	}

	/**
	 * ����������ϸ��Ϣ��ʾ����
	 * 
	 * @param parent
	 */
	protected void createContent(Composite parent) {
		setLayout(new GridLayout());

		Section section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section.setText("�����б�");
		Control control = createProcessTableTab(section);
		section.setClient(control);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		section = new SimpleSection(parent, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);
		section.setText("����ͼ");
		control = createProcessCanvasTab(section);
		section.setClient(control);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		//
		//
		//
		// // ��ʾ����ͼ
		// CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP | SWT.BORDER);
		// CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		// cti1.setText("����ͼ");
		// Control control1 = createProcessCanvasTab(tabFolder);
		// cti1.setControl(control1);
		//
		// // ��ʾ���
		// CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		// cti2.setText("���");
		// Control control2 = createProcessTableTab(tabFolder);
		// cti2.setControl(control2);
		//
		// tabFolder.setSelection(cti1);

	}

	/**
	 * �������̻��
	 * 
	 * @param parent
	 * @return Control
	 */
	private Control createProcessTableTab(Composite parent) {
		processTable = new ProcessTableViewer(parent);
		processTable
				.addPostSelectionChangedListener(new ISelectionChangedListener() {
					// �����ѡ��ı��¼�
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
	 * ��������ͼ
	 * 
	 * @param parent
	 * @return Control
	 */
	private Control createProcessCanvasTab(Composite parent) {
		// ��������ͼ����
		processCanvas = new ProcessCanvas(parent, SWT.NONE);
		processCanvas.addNodeSelectListener(new INodeSelectListener() {
			// �����ѡ��ı��¼�
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
	 * ��������ͼ�ͻ�������Դ
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
