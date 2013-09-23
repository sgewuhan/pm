package com.sg.business.commons.ui.flow;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2.IProcessSettingListener;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.User;

public class ProcessControlSetting implements IProcessSettingListener {

	private IProcessControlable processControl;
	private String key;

	public ProcessControlSetting(IProcessControlable processControl, String key) {
		this.processControl = processControl;
		this.key = key;
	}

	// �����ִ���˸����¼�
	@Override
	public void actorChanged(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		processControl.setProcessActionActor(key,
				nodeAssignment.getNodeActorParameter(), newActor.getUserid());
		event(EVENT_ACTOR_CHANGED);
	}

	protected void event(int eventCode) {

	}

	// �������̶�������¼�
	@Override
	public void processChanged(DroolsProcessDefinition newProcessDefinition,
			DroolsProcessDefinition oldProcessDef) {
		processControl.setProcessDefinition(key, newProcessDefinition);
		event(EVENT_PROCESS_CHANGED);
	}

	// �������̶����Ƿ������¼�
	@Override
	public void processActivatedChanged(boolean activated) {
		processControl.setWorkflowActivate(key, activated);
		event(EVENT_PROCESSACTIVATED_CHANGED);
	}

	// ������ɫ�޶������¼�
	@Override
	public void roleChanged(AbstractRoleDefinition newRole,
			AbstractRoleDefinition oldRole, NodeAssignment na) {
		processControl.setProcessActionAssignment(key, na.getNodeActorParameter(), newRole);

		event(EVENT_ROLE_CHANGED);
	}

}
