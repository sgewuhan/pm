package com.sg.business.commons.ui.flow;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2.IProcessSettingListener;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.User;

public class ProcessControlSetting implements IProcessSettingListener {

	private IProcessControl IProcessControl;
	private String key;

	public ProcessControlSetting(IProcessControl IProcessControl, String key) {
		this.IProcessControl = IProcessControl;
		this.key = key;
	}

	// �����ִ���˸����¼�
	@Override
	public void actorChanged(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		IProcessControl.setProcessActionActor(key,
				nodeAssignment.getNodeActorParameter(), newActor.getUserid());
		event(EVENT_ACTOR_CHANGED);
	}

	protected void event(int eventCode) {

	}

	// �������̶�������¼�
	@Override
	public void processChanged(DroolsProcessDefinition newProcessDefinition,
			DroolsProcessDefinition oldProcessDef) {
		IProcessControl.setProcessDefinition(key, newProcessDefinition);
		event(EVENT_PROCESS_CHANGED);
	}

	// �������̶����Ƿ������¼�
	@Override
	public void processActivatedChanged(boolean activated) {
		IProcessControl.setWorkflowActivate(key, activated);
		event(EVENT_PROCESSACTIVATED_CHANGED);
	}

	// ������ɫ�޶������¼�
	@Override
	public void roleChanged(AbstractRoleDefinition newRole,
			AbstractRoleDefinition oldRole, NodeAssignment na) {
		IProcessControl.setProcessActionAssignment(key, na.getNodeActorParameter(), newRole);

		event(EVENT_ROLE_CHANGED);
	}

}
