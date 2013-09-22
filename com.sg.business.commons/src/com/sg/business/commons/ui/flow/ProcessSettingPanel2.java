package com.sg.business.commons.ui.flow;

import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ActivityEditor.IActivityEditListener;
import com.sg.business.commons.ui.flow.ActivitySelecter.INodeSelectionListener;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.User;

/**
 * ���̶���ҳ�棬��������ѡ�������Ƿ�����ѡ���������ϸ��Ϣ������ͼ�ͻ������ڵ���ϸ��Ϣ������ƣ�ָ�����ָ�ɹ��򣬽�ɫ�޶����ִ���˵ȣ�
 * @author jinxitao
 *
 */
public class ProcessSettingPanel2 extends Composite {

	public interface IProcessSettingListener {

		void actorChanged(User newActor, User oldActor,
				NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef);

		void processChanged(DroolsProcessDefinition newProcessDefinition,
				DroolsProcessDefinition oldProcessDef);

		void processActivatedChanged(boolean activated);

		void roleChanged(AbstractRoleDefinition newRole,
				AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment);

	}

	/**
	 * ���̶���ѡ����
	 */
	private ComboViewer processSelecter;
	
	/**
	 * �����Ƿ�����ѡ���
	 */
	private Button activatedChecker;     
	
	/**
	 * ����ͼ�ͻ��
	 */
	private ActivitySelecter activitySelecter;
	
	/**
	 * ���̻�ڵ�༭��
	 */
	private ActivityEditor activiteEditor;
	
	/**
	 * �Ƿ���Ҫ����ѡ����
	 */
	private boolean hasProcessSelector;
	
	/**
	 * �Ƿ���Ҫ�ִ�����ֶ�
	 */
	private boolean hasActorSelector;
	
	/**
	 * ���̶���
	 */
	private DroolsProcessDefinition processDefinition;
	
	/**
	 * �Ƿ���Ҫ��ɫ�޶��ֶ�
	 */
	private boolean hasRoleSelector;
	private ListenerList listeners = new ListenerList();
	
	/**
	 * ģ��������֯�����̿����������̶���
	 */
	private List<DroolsProcessDefinition> processDefinitionsChoice;
	
	/**
	 * ���̶����Ƿ�����
	 */
	private boolean processActivate;

	public void addProcessSettingListener(IProcessSettingListener listener) {
		listeners.add(listener);
	}

	public void removeProcessSettingListener(IProcessSettingListener listener) {
		listeners.remove(listener);
	}

	public ProcessSettingPanel2(Composite parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * ��������ҳ������
	 */
	public void createContent() {
		setLayout(new FormLayout());
		//�ж��Ƿ���ʾ����ѡ����������ǣ��򴴽�����ѡ����
		if (hasProcessSelector) {
			createProcessSelector(this);
		}

		Composite panel = new Composite(this, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));

		//����������ϸ��Ϣ������ͼ�ͻ��
		activitySelecter = new ActivitySelecter(panel);
		activitySelecter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));

		activitySelecter.addListener(new INodeSelectionListener() {
            //������ǰѡ�����̸ı��¼�
			@Override
			public void selectionChange(NodeAssignment nodeAssignment) {
				AbstractRoleDefinition roleDef = getRoleDefinition(nodeAssignment);
				User actor = getActor(nodeAssignment);
				activiteEditor.setInput(nodeAssignment, roleDef, actor);
			}
		});

		//�������̻�ڵ�༭��
		activiteEditor = new ActivityEditor(panel, hasRoleSelector,
				hasActorSelector);
		activiteEditor.addActiviteEditListener(new IActivityEditListener() {
            //�������̻ִ���˵ĸı��¼�
			@Override
			public void actorChanged(User newActor, User oldActor,
					NodeAssignment nodeAssignment,
					AbstractRoleDefinition roleDef) {
				ProcessSettingPanel2.this.actorChanged(newActor, oldActor,
						nodeAssignment, roleDef);
			}
            //������ɫ�޶��ĸı�ʱ��
			@Override
			public void roleChanged(AbstractRoleDefinition newRole,
					AbstractRoleDefinition oldRole,
					NodeAssignment nodeAssignment) {
				ProcessSettingPanel2.this.roleChanged(newRole, oldRole,
						nodeAssignment);
			}
		});
		GridData gd = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gd.widthHint = 300;
		activiteEditor.setLayoutData(gd);

		FormData fd = new FormData();
		panel.setLayoutData(fd);
		if (processSelecter != null) {
			fd.top = new FormAttachment(processSelecter.getControl(), 10);
		} else {
			fd.top = new FormAttachment(0, 10);
		}
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.bottom = new FormAttachment(100, -10);

		initInputValue();
	}

	//��ʼ�����̶�������
	private void initInputValue() {
		//�����Ƿ�����
		if (activatedChecker != null) {
			activatedChecker.setSelection(processActivate);
		}
		//����ѡ��������Դ
		if (processSelecter != null) {
			processSelecter.setInput(processDefinitionsChoice);
			processSelecter.setSelection(new StructuredSelection(
					new Object[] { processDefinition }));
		} else {
			activitySelecter.setInput(processDefinition);
		}

	}

	/**
	 * ��������ѡ����
	 * @param parent
	 */
	private void createProcessSelector(Composite parent) {
		// ��������ѡ����
		processSelecter = new ComboViewer(parent, SWT.READ_ONLY);
		processSelecter.setUseHashlookup(true);
		processSelecter.setContentProvider(ArrayContentProvider.getInstance());
		processSelecter.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DroolsProcessDefinition) {
					DroolsProcessDefinition definition = (DroolsProcessDefinition) element;
					return definition.getProcessName();
				} else {
					return "";
				}
			}
		});

		FormData fd = new FormData();
		processSelecter.getControl().setLayoutData(fd);
		fd.top = new FormAttachment(0, 10);
		fd.left = new FormAttachment(0, 10);

		processSelecter
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection is = (IStructuredSelection) event
								.getSelection();
						DroolsProcessDefinition input = null;
						if (is != null && !is.isEmpty()) {
							input = (DroolsProcessDefinition) is
									.getFirstElement();
						}
						activitySelecter.setInput(input);
						DroolsProcessDefinition oldProcessDef = processDefinition;
						processDefinition = input;
						processChanged(oldProcessDef, processDefinition);

					}
				});
		activatedChecker = new Button(parent, SWT.CHECK);
		activatedChecker.setText("����");

		fd = new FormData();
		activatedChecker.setLayoutData(fd);
		fd.left = new FormAttachment(processSelecter.getControl(), 10);
		fd.top = new FormAttachment(0, 10);

		activatedChecker.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				processActivatedChanged(activatedChecker.getSelection());
			}

		});
	}

	/**
	 * ���ؽ�ɫ�޶�
	 * @param nodeAssignment
	 * @return
	 */
	protected AbstractRoleDefinition getRoleDefinition(
			NodeAssignment nodeAssignment) {
		return null;
	}

	/**
	 * ���ػִ����
	 * @param nodeAssignment
	 * @return
	 */
	protected User getActor(NodeAssignment nodeAssignment) {
		return null;
	}

	/**
	 * �ж������Ƿ�����
	 * @return boolean
	 */
	final public boolean isProcessActivated() {
		return activatedChecker.getSelection();
	}

	final public void setProcessActivated(boolean activate) {
		processActivate = activate;
		// if (processSelecter != null) {
		// activatedChecker.setSelection(activate);
		// }
	}

	/**
	 * ��������ѡ����
	 * @param processDefs
	 */
	final public void setProcessDefinitionChoice(
			List<DroolsProcessDefinition> processDefs) {
		this.processDefinitionsChoice = processDefs;
		// if (processSelecter != null) {
		// processSelecter.setInput(processDefs);
		// }
	}

	/**
	 * ��������
	 * @param processDef
	 */
	final public void setProcessDefinition(DroolsProcessDefinition processDef) {
		this.processDefinition = processDef;
		// if (processSelecter != null) {
		// processSelecter.setSelection(new StructuredSelection(
		// new Object[] { processDef }));
		// // set selection will set activiteSelector input.
		// } else {
		// activitySelecter.setInput(processDef);
		// }
	}

	/**
	 * �Ƿ���ʾ����ѡ�������
	 */
	final public void setHasProcessSelector(boolean hasProcessSelector) {
		this.hasProcessSelector = hasProcessSelector;
	}

	/**
	 * �Ƿ���ʾ�ִ�����ֶ�
	 * @param hasActorSelector
	 */
	final public void setHasActorSelector(boolean hasActorSelector) {
		this.hasActorSelector = hasActorSelector;
	}

	/**
	 * �Ƿ���ʾ��ɫ�޶��ֶ�
	 * @param hasRoleSelector
	 */
	final public void setHasRoleSelector(boolean hasRoleSelector) {
		this.hasRoleSelector = hasRoleSelector;
	}

	/**
	 * ���̶���ѡ��ı��¼�
	 * @param oldProcessDef
	 * @param newProcessDefinition
	 */
	private void processChanged(DroolsProcessDefinition oldProcessDef,
			DroolsProcessDefinition newProcessDefinition) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).processChanged(
					newProcessDefinition, oldProcessDef);
		}
	}

	/**
	 * ���̻ѡ��ı��¼�
	 * @param activated
	 */
	private void processActivatedChanged(boolean activated) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i])
					.processActivatedChanged(activated);
		}
	}

	/**
	 * �ִ���˸ı��¼�
	 * @param newActor
	 * @param oldActor
	 * @param nodeAssignment
	 * @param roleDef
	 */
	private void actorChanged(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).actorChanged(newActor, oldActor,
					nodeAssignment, roleDef);
		}
	}

	/**
	 * ���ɫ�޶��ı��¼�
	 * @param newRole
	 * @param oldRole
	 * @param nodeAssignment
	 */
	private void roleChanged(AbstractRoleDefinition newRole,
			AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).roleChanged(newRole, oldRole,
					nodeAssignment);
		}

	}

}
