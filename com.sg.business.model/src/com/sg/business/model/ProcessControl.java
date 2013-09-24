package com.sg.business.model;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public abstract class ProcessControl implements IProcessControlable {

	private PrimaryObject primaryObject;

	public ProcessControl(PrimaryObject data) {
		this.primaryObject = data;
	}

	@Override
	public boolean isWorkflowActivate(String fieldKey) {
		return Boolean.TRUE.equals(primaryObject.getValue(fieldKey
				+ POSTFIX_ACTIVATED));
	}
	
	

	@Override
	public boolean isWorkflowActivateAndAvailable(String fieldKey) {
		return isWorkflowActivate(fieldKey)&&getProcessDefinition(fieldKey)!=null;
	}

	@Override
	public void setWorkflowActivate(String key, boolean activated) {
		primaryObject.setValue(key + POSTFIX_ACTIVATED, activated);
	}

	@Override
	public DroolsProcessDefinition getProcessDefinition(String fieldKey) {
		DBObject processData = (DBObject) primaryObject.getValue(fieldKey);
		if (processData != null) {
			return new DroolsProcessDefinition(processData);
		}
		return null;
	}

	@Override
	public Workflow getWorkflow(String key) {
		DroolsProcessDefinition processDefintion = getProcessDefinition(key);
		Workflow wf = new Workflow(processDefintion, primaryObject, key);
		return wf;
	}

	@Override
	public AbstractRoleDefinition getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// 取出角色指派
		DBObject data = getProcessRoleAssignmentData(key);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return (AbstractRoleDefinition) ModelService.createModelObject(
					getRoleDefinitionClass(), roleId);
		}
		return null;
	}

	protected abstract Class<? extends PrimaryObject> getRoleDefinitionClass();

	@Override
	public DBObject getProcessRoleAssignmentData(String key) {
		return (DBObject) primaryObject.getValue(key + POSTFIX_ASSIGNMENT);
	}

	@Override
	public String getProcessActionActor(String key, String nodeActorParameter) {
		DBObject data = getProcessActorsData(key);
		if (data == null) {
			return null;
		}
		return (String) data.get(nodeActorParameter);
	}

	@Override
	public void setProcessActionActor(String key,
			String nodeActorParameter, String  userid) {
		DBObject data = getProcessActorsData(key);
		if(data == null){
			data = new BasicDBObject();
			primaryObject.setValue(key + POSTFIX_ACTORS, data);
		}
		data.put(nodeActorParameter, userid);
	}

	@Override
	public DBObject getProcessActorsData(String key) {
		return (DBObject) primaryObject.getValue(key + POSTFIX_ACTORS);
	}

	/**
	 * 
	 * @param key
	 * @param userid
	 * @param query
	 * @return
	 */
	public DBObject getCurrentWorkflowTaskData(String key, String userid,
			boolean query) {
		String field = key + POSTFIX_TASK;
		Object value = primaryObject.getValue(field, query);
		if (value instanceof DBObject) {
			return (DBObject) ((DBObject) value).get(userid);
		}
		return null;
	}

	public DBObject getWorkflowTaskData(String key) {
		String field = key + POSTFIX_TASK;
		Object value = primaryObject.getValue(field);
		if (value instanceof DBObject) {
			return (DBObject) value;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @param userid
	 * @param query
	 * @return
	 */
	@Override
	public BasicBSONList getWorkflowHistroyData(String key, boolean query) {
		String field = key + POSTFIX_HISTORY;
		Object value = primaryObject.getValue(field, query);
		return (BasicBSONList) value;
	}

	public void setProcessRoleAssignmentData(String key,
			DBObject wfRoleAssignment) {
		primaryObject.setValue(key + IProcessControlable.POSTFIX_ASSIGNMENT,
				wfRoleAssignment);
	}

	public void setProcessRoleAssignment(String key,
			String nodeAssignmentParameter, AbstractRoleDefinition roled) {
		DBObject data = getProcessRoleAssignmentData(key);
		if (roled == null) {
			data.removeField(nodeAssignmentParameter);
		} else {
			data.put(nodeAssignmentParameter, roled.get_id());
		}
		setProcessRoleAssignmentData(key, data);
	}

	@Override
	public void setProcessDefinition(String key,
			DroolsProcessDefinition definition) {
		if (definition != null) {
			primaryObject.setValue(key, definition.getData());
		} else {
			primaryObject.setValue(key, null);
		}
	}

	@Override
	public void setProcessActionAssignment(String key,String  nap,
			AbstractRoleDefinition newRole) {
		DBObject wfRoleAssignment = (DBObject) getProcessRoleAssignmentData(key);
		if (wfRoleAssignment == null) {
			wfRoleAssignment = new BasicDBObject();
			setProcessRoleAssignmentData(key, wfRoleAssignment);
		}

		if (newRole == null) {
			setProcessRoleAssignment(key, nap, null);
		} else {
			setProcessRoleAssignment(key, nap, newRole);
		}

	}

	public DBObject getWorkflowDefinition(String workflowKey) {
		DBObject result = new BasicDBObject();
		result.put("KEY", primaryObject.getValue(workflowKey));
		result.put(POSTFIX_ACTIVATED,
				primaryObject.getValue(workflowKey + POSTFIX_ACTIVATED));
		result.put(POSTFIX_ACTORS,
				primaryObject.getValue(workflowKey + POSTFIX_ACTORS));
		result.put(POSTFIX_ASSIGNMENT,
				primaryObject.getValue(workflowKey + POSTFIX_ASSIGNMENT));
		return result;
	}

}
