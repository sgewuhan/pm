package com.sg.business.commons.ui.flow;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.flow.model.DroolsProcessDiagram;
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
	 * ����ͼ���
	 */
	private ProcessTableViewer processTable;

	private ListenerList listeners = new ListenerList();

	private ScrollingGraphicalViewer processViewer;

	private SimpleSection section1;

//	private SimpleSection section2;

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
		setLayout(new FormLayout());

		section1 = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR );
		section1.setText("��ָ����Ա�������б�"); //$NON-NLS-1$

		Control control = createProcessTableTab(section1);
		section1.setClient(control);
		FormData fd = new FormData();
		section1.setLayoutData(fd);
		fd.top = new FormAttachment(0,0);
		fd.left = new FormAttachment(0,10);
		fd.right = new FormAttachment(100,-10);
		fd.bottom = new FormAttachment(100,-10);
		
//		section2 = new SimpleSection(parent, Section.SHORT_TITLE_BAR
//				| Section.TWISTIE);
//		section2.setText("����ͼ");
//		control = createProcessCanvasTab(section2);
//		section2.setClient(control);
//		section2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

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
		processTable = new ProcessTableViewer(parent,SWT.FULL_SELECTION|SWT.V_SCROLL);
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

//	/**
//	 * ��������ͼ
//	 * 
//	 * @param parent
//	 * @return Control
//	 */
//	protected Control createProcessCanvasTab(Composite parent) {
//		// ��������ͼ����
//		
//		processViewer = new ScrollingGraphicalViewer();
//		processViewer.setRootEditPart(new ScalableRootEditPart());
//		processViewer.setEditPartFactory(new ActivityPartFactory());
//		processViewer.createControl(parent);
//
//		
//		processViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//			
//			@Override
//			public void selectionChanged(SelectionChangedEvent event) {
//				StructuredSelection is = (StructuredSelection) event
//						.getSelection();
//				if (is == null || is.isEmpty()) {
//					return;
//				}
//				Object element = is.getFirstElement();
//
//				if (element instanceof SimpleActivityPart) {
//					Object model = ((SimpleActivityPart) element).getModel();
//					if(model instanceof NodeActivity){
//						Node node = ((NodeActivity) model).getNode();
//						if (node instanceof HumanTaskNode) {
//							NodeAssignment na = new NodeAssignment(
//									(HumanTaskNode) node);
//							fireNodeSelectionChange(na);
//							return;
//						}
//					}
//				}
//				
//				fireNodeSelectionChange(null);
//
//			}
//		});
//		return processViewer.getControl();
//	}

	/**
	 * ��������ͼ�ͻ�������Դ
	 * 
	 * @param processDefinition
	 */
	public void setInput(DroolsProcessDefinition processDefinition) {
		DroolsProcessDiagram diagram = new DroolsProcessDiagram(processDefinition,
				null);
		if(processViewer!=null){
			processViewer.setContents(diagram);
		}
		processTable.setDataInput(processDefinition);
		layout();
	}

	public void addListener(INodeSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(INodeSelectionListener listener) {
		listeners.remove(listener);
	}


}
