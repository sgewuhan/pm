package com.sg.business.commons.page;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessControlSetting;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public abstract class AbstractProcessSettingPage extends
		AbstractFormPageDelegator {

	private boolean editable;
	private ProcessSettingPanel2 psp2;

	@Override
	final public ProcessSettingPanel2 createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		setFormInput(input);
		editable = input.isEditable();
		final IProcessControl ipc = getIProcessControl();

		psp2 = new ProcessSettingPanel2(parent, getProcessSettingControl()) {

			@Override
			protected AbstractRoleDefinition getRoleDefinition(
					NodeAssignment nodeAssignment) {
				if (nodeAssignment != null) {
					return ipc.getProcessActionAssignment(getProcessKey(),
							nodeAssignment.getNodeActorParameter());
				} else {
					return null;
				}
			}

			@Override
			protected User getActor(NodeAssignment nodeAssignment) {
				if (nodeAssignment == null) {
					return null;
				}
				String userid = ipc.getProcessActionActor(getProcessKey(),
						nodeAssignment.getNodeActorParameter());
				return UserToolkit.getUserById(userid);
			}

			@Override
			public DataSet getActorDataSet() {
				AbstractRoleDefinition roled = getSelectedRole();
				return AbstractProcessSettingPage.this.getActorDataSet(roled);
			}

		};
		// ���ý�ɫ��ѡ��������Ŀģ���еĽ�ɫ����
		psp2.setRoleNavigatorId("commons.generic.tableselector");

		// �����û���ѡ����
		psp2.setActorNavigatorId("commons.generic.tableselector");

		List<DroolsProcessDefinition> processDefs = getProcessDefinition();
		psp2.setProcessDefinitionChoice(processDefs);
		// ���ص�ǰѡ������
		DroolsProcessDefinition processDef = ipc
				.getProcessDefinition(getProcessKey());
		// ��ʾ��ǰѡ�����̵���Ϣ
		psp2.setProcessDefinition(processDef);

		// ��ʾ�������Ƿ񼤻�
		boolean activate = ipc.isWorkflowActivate(getProcessKey());
		psp2.setProcessActivated(activate);

		// ���ý�ɫ�����ݼ�
		psp2.setRoleDataSet(getRoleDataSet());

		psp2.createContent();

		// ��Ӽ���
		psp2.addProcessSettingListener(new ProcessControlSetting(ipc,
				getProcessKey()) {
			@Override
			protected void event(int code) {
				setDirty(true);
			}
		});

		psp2.setEditable(editable);
		return psp2;
	}

	protected abstract int getProcessSettingControl();

	protected abstract DataSet getRoleDataSet();

	protected IProcessControl getIProcessControl() {
		PrimaryObjectEditorInput input = getInput();
		PrimaryObject po = input.getData();
		return (IProcessControl) po.getAdapter(IProcessControl.class);
	}

	protected abstract List<DroolsProcessDefinition> getProcessDefinition();

	protected abstract String getProcessKey();

	protected abstract DataSet getActorDataSet(AbstractRoleDefinition roled);

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void refresh() {
		final IProcessControl ipc = getIProcessControl();

		List<DroolsProcessDefinition> processDefs = getProcessDefinition();
		psp2.setProcessDefinitionChoice(processDefs);
		// ���ص�ǰѡ������
		DroolsProcessDefinition processDef = ipc
				.getProcessDefinition(getProcessKey());
		// ��ʾ��ǰѡ�����̵���Ϣ
		psp2.setProcessDefinition(processDef);

		// ��ʾ�������Ƿ񼤻�
		boolean activate = ipc.isWorkflowActivate(getProcessKey());
		psp2.setProcessActivated(activate);

		// ���ý�ɫ�����ݼ�
		psp2.setRoleDataSet(getRoleDataSet());
		
		psp2.refresh();
		super.refresh();
	}
	
	public ProcessSettingPanel2 getProcessSettingPanel(){
		return psp2;
	}

}
