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

	// 监听活动执行人更改事件
	@Override
	public void actorChanged(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		IProcessControl.setProcessActionActor(key,
				nodeAssignment.getNodeActorParameter(), newActor.getUserid());
		event(EVENT_ACTOR_CHANGED);
	}

	protected void event(int eventCode) {

	}

	// 监听流程定义更改事件
	@Override
	public void processChanged(DroolsProcessDefinition newProcessDefinition,
			DroolsProcessDefinition oldProcessDef) {
		IProcessControl.setProcessDefinition(key, newProcessDefinition);
		event(EVENT_PROCESS_CHANGED);
	}

	// 监听流程定义是否启用事件
	@Override
	public void processActivatedChanged(boolean activated) {
		IProcessControl.setWorkflowActivate(key, activated);
		event(EVENT_PROCESSACTIVATED_CHANGED);
	}

	// 监听角色限定更改事件
	@Override
	public void roleChanged(AbstractRoleDefinition newRole,
			AbstractRoleDefinition oldRole, NodeAssignment na) {
		IProcessControl.setProcessActionAssignment(key, na.getNodeActorParameter(), newRole);

		event(EVENT_ROLE_CHANGED);
	}

}
