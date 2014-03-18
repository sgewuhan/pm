package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.nls.Messages;

public abstract class ProcessControl implements IProcessControl {

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
		return isWorkflowActivate(fieldKey)
				&& getProcessDefinition(fieldKey) != null;
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
	public void setProcessActionActor(String key, String nodeActorParameter,
			String userid) {
		DBObject data = getProcessActorsData(key);
		if (data == null) {
			data = new BasicDBObject();
			primaryObject.setValue(key + POSTFIX_ACTORS, data);
		}
		data.put(nodeActorParameter, userid);
	}

	@Override
	public DBObject getProcessActorsData(String key) {
		return (DBObject) primaryObject.getValue(key + POSTFIX_ACTORS);
	}

	// @Override
	// public UserTask getUserTask(String userid,Long taskId) {
	// Object hostId = primaryObject.getValue(PrimaryObject.F__ID);
	// DBCollection col = DBActivator.getCollection(IModelConstants.DB,
	// IModelConstants.C_USERTASK);
	// BasicDBObject query = new BasicDBObject();
	// query.put(UserTask.F_HOST_ID, hostId);
	// query.put(UserTask.F_HOST_COLLECTION, primaryObject.getCollectionName());
	// query.put(UserTask.F_USERID, userid);
	// query.put(UserTask.F_TASKID, taskId);
	// DBObject data = col.findOne(query);
	//
	// if(data!=null){
	// return ModelService.createModelObject(data, UserTask.class);
	// }
	//
	// return null;
	// }

	@Deprecated
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
	 * @return
	 */
	@Override
	public BasicBSONList getWorkflowHistroyData() {
		List<PrimaryObject> userTaskList = primaryObject.getRelationById(PrimaryObject.F__ID, UserTask.F_WORK_ID,
				UserTask.class);
		BasicBSONList result = new BasicBSONList();
		for (PrimaryObject userTask : userTaskList) {
			result.add(userTask.get_data());
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<PrimaryObject> getWorkflowHistroy() {
		return primaryObject.getRelationById(PrimaryObject.F__ID, UserTask.F_WORK_ID,
				UserTask.class);
		
		
	}

	public void setProcessRoleAssignmentData(String key,
			DBObject wfRoleAssignment) {
		primaryObject.setValue(key + IProcessControl.POSTFIX_ASSIGNMENT,
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
	public void setProcessActionAssignment(String key, String nap,
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
		result.put("KEY", primaryObject.getValue(workflowKey)); //$NON-NLS-1$
		result.put(POSTFIX_ACTIVATED,
				primaryObject.getValue(workflowKey + POSTFIX_ACTIVATED));
		result.put(POSTFIX_ACTORS,
				primaryObject.getValue(workflowKey + POSTFIX_ACTORS));
		result.put(POSTFIX_ASSIGNMENT,
				primaryObject.getValue(workflowKey + POSTFIX_ASSIGNMENT));
		return result;
	}

	public List<String[]> checkProcessRunnable(String key) {
		List<String[]> result = new ArrayList<String[]>();
		DroolsProcessDefinition processd = getProcessDefinition(key);
		if(processd == null){
			return result;
		}
		// 检查流程是否已经激活
		if (!isWorkflowActivate(key)) {
			result.add(new String[] { "error", Messages.get().ProcessControl_2 }); //$NON-NLS-1$
			return result;
		}
		// 检查流程的需要指定用户的节点是否已经指定
		DroolsProcessDefinition pd = getProcessDefinition(key);
		List<NodeAssignment> nodes = pd.getNodesAssignment();
		for (int i = 0; i < nodes.size(); i++) {
			NodeAssignment na = nodes.get(i);

			if (na.isNeedAssignment()&& na.forceAssignment()) {
				String actorId = getProcessActionActor(key,
						na.getNodeActorParameter());
				String nodeName = na.getNodeName();
				if (actorId != null) {
					User user = UserToolkit.getUserById(actorId);
					if (user == null) {
						result.add(new String[] {
								"error", //$NON-NLS-1$
								Messages.get().ProcessControl_4 + nodeName + "\"" //$NON-NLS-2$
										+ Messages.get().ProcessControl_6 });
					} else {
						result.add(new String[] {
								"info", //$NON-NLS-1$
								Messages.get().ProcessControl_8 + nodeName + "\"" + Messages.get().ProcessControl_10 + user //$NON-NLS-2$
										+ "]。" }); //$NON-NLS-1$
					}
				} else {
					AbstractRoleDefinition rd = getProcessActionAssignment(key,
							na.getNodeActorParameter());
					if (rd == null) {
						result.add(new String[] {
								"error", //$NON-NLS-1$
								Messages.get().ProcessControl_13 + nodeName + "\"" //$NON-NLS-2$
										+ Messages.get().ProcessControl_15 });
					} else {
						List<PrimaryObject> roleAssiment = null;
						if (rd instanceof ProjectRole) {
							roleAssiment = ((ProjectRole) rd).getAssignment();
						} else if (rd instanceof RoleDefinition) {
							if (((RoleDefinition) rd).isOrganizatioRole()) {
								Role r = ((RoleDefinition) rd)
										.getOrganizationRole();
								//TODO 根据primaryObject的类型进行判断
								//TODO 如果是项目时，使用ProjectRole.getAssignment();
								
								//TODO 如果是独立工作时，使用TYPE为TYPE_WORK_PROCESS的RoleParameter，传入工作ID进行人员指派
								roleAssiment = r.getAssignment();
							}
						}
						if (roleAssiment == null) {
							result.add(new String[] {
									"error", //$NON-NLS-1$
									Messages.get().ProcessControl_17 + nodeName + "\"" //$NON-NLS-2$
											+ Messages.get().ProcessControl_19 });
						} else {
							String userList = ""; //$NON-NLS-1$
							for (int j = 0; j < roleAssiment.size(); j++) {
								AbstractRoleAssignment a = (AbstractRoleAssignment) roleAssiment
										.get(j);
								if (j != 0) {
									userList += ", "; //$NON-NLS-1$
								}
								userList += a.getUserid() + "|" //$NON-NLS-1$
										+ a.getUsername();
							}

							if (roleAssiment.size() > 1) {
								result.add(new String[] {
										"warning", //$NON-NLS-1$
										Messages.get().ProcessControl_24 + nodeName + "\"" //$NON-NLS-2$
												+ Messages.get().ProcessControl_26 + userList
												+ Messages.get().ProcessControl_27 });
							} else {

								result.add(new String[] {
										"info", //$NON-NLS-1$
										"流程任务:\"" + nodeName + "\"" //$NON-NLS-1$ //$NON-NLS-2$
												+ Messages.get().ProcessControl_31 + userList
												+ "]。" }); //$NON-NLS-1$
							}

						}
					}

				}
			}
		}
		return result;
	}
}
