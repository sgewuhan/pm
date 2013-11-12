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
		// ȡ����ɫָ��
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

//	@Override
//	public UserTask getUserTask(String userid,Long taskId) {
//		Object hostId = primaryObject.getValue(PrimaryObject.F__ID);
//		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
//				IModelConstants.C_USERTASK);
//		BasicDBObject query = new BasicDBObject();
//		query.put(UserTask.F_HOST_ID, hostId);
//		query.put(UserTask.F_HOST_COLLECTION, primaryObject.getCollectionName());
//		query.put(UserTask.F_USERID, userid);
//		query.put(UserTask.F_TASKID, taskId);
//		DBObject data  = col.findOne(query);
//
//		if(data!=null){
//			return ModelService.createModelObject(data, UserTask.class);
//		}
//
//		return null;
//	}

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
	 * @param key
	 * @param userid
	 * @param query
	 * @return
	 */
	@Override @Deprecated
	public BasicBSONList getWorkflowHistroyData(String key, boolean query) {
		String field = key + POSTFIX_HISTORY;
		Object value = primaryObject.getValue(field, query);
		return (BasicBSONList) value;
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
		result.put("KEY", primaryObject.getValue(workflowKey));
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
		// ��������Ƿ��Ѿ�����
		if (!isWorkflowActivateAndAvailable(key)) {
			result.add(new String[] { "error", "û��Ϊ�����������̻򼤻�����" });
			return result;
		}
		// ������̵���Ҫָ���û��Ľڵ��Ƿ��Ѿ�ָ��
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
								"error",
								"��������:\"" + nodeName + "\""
										+ ", �޷�ȷ��ִ���ˣ����̿����޷�����ִ�С�" });
					} else {
						result.add(new String[] {
								"info",
								"��������:\"" + nodeName + "\"" + ", ִ����[" + user
										+ "]��" });
					}
				} else {
					AbstractRoleDefinition rd = getProcessActionAssignment(key,
							na.getNodeActorParameter());
					if (rd == null) {
						result.add(new String[] {
								"error",
								"��������:\"" + nodeName + "\""
										+ ", �޷�ȷ��ִ���ˣ����̿����޷�����ִ�С�" });
					} else {
						List<PrimaryObject> roleAssiment = null;
						if (rd instanceof ProjectRole) {
							roleAssiment = ((ProjectRole) rd).getAssignment();
						} else if (rd instanceof RoleDefinition) {
							if (((RoleDefinition) rd).isOrganizatioRole()) {
								Role r = ((RoleDefinition) rd)
										.getOrganizationRole();
								roleAssiment = r.getAssignment();
							}
						}
						if (roleAssiment == null) {
							result.add(new String[] {
									"error",
									"��������:\"" + nodeName + "\""
											+ ", ָ�ɵĽ�ɫû�ж�Ӧ��Ա�����̿����޷�����ִ�С�" });
						} else {
							String userList = "";
							for (int j = 0; j < roleAssiment.size(); j++) {
								AbstractRoleAssignment a = (AbstractRoleAssignment) roleAssiment
										.get(j);
								if (j != 0) {
									userList += ", ";
								}
								userList += a.getUserid() + "|"
										+ a.getUsername();
							}

							if (roleAssiment.size() > 1) {
								result.add(new String[] {
										"warning",
										"��������:\"" + nodeName + "\""
												+ ", ָ�ɵĽ�ɫ��Ӧ������Ա, " + userList
												+ "����ȷ�������á�" });
							} else {

								result.add(new String[] {
										"info",
										"��������:\"" + nodeName + "\""
												+ ", ͨ����ɫָ�ɵ�[" + userList
												+ "]��" });
							}

						}
					}

				}
			}
		}
		return result;
	}
}
