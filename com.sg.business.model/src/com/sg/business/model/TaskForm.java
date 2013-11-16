package com.sg.business.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.eclipse.core.runtime.Assert;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.WorkflowService;

public class TaskForm extends PrimaryObject {

	public static final String F_WORK_ID = "work_id";
	
	public static final String F_EDITOR = "form_editor";
	
	public static final String F_USER_TASK_ID = "usertask_id";
	
	public static final String F_PROCESSINPUT = "processinput";

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	public void doInsert(IContext context) throws Exception {
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	public Work getWork() {
		ObjectId workid = (ObjectId) getValue(F_WORK_ID);
		Assert.isNotNull(workid, "������޷�ȷ�������Ĺ���");
		return ModelService.createModelObject(Work.class, workid);
	}

	public Task getExecuteTask(IContext context) throws Exception {
		UserTask userTask = getUserTask();
		Assert.isNotNull(userTask, "������޷�ȷ�������Ĺ���");
		return userTask.getTask();
	}

	public UserTask getUserTask() {
		return ModelService.createModelObject(UserTask.class, getObjectIdValue(F_USER_TASK_ID));
	}

	/**
	 * ����������ֶ��л�ȡ�û���userid,��ӵ�������
	 * �ֶο�����String���͵��ֶΣ�ֱ�ӱ����userid,Ҳ�������������͵ģ������û�id���б�
	 * 
	 * @param fieldlist
	 * @throws Exception
	 */
	public void doAddWorkParticipatesFromField(String[] fieldlist)
			throws Exception {
		Work work = getWork();
		Assert.isNotNull(work, "������޷�ȷ�������Ĺ���");

		List<String> userList = new ArrayList<String>();

		for (int i = 0; i < fieldlist.length; i++) {
			Object value = getValue(fieldlist[i]);
			if (value instanceof String) {
				userList.add((String) value);
			} else if (value instanceof List<?>) {
				for (int j = 0; j < ((List<?>) value).size(); j++) {
					userList.add((String) ((List<?>) value).get(j));
				}
			}
		}

		work.doAddParticipateList(userList);
	}
	
	public void doAddWorkParticipates(List<String> useridlist) throws Exception{
		Work work = getWork();
		Assert.isNotNull(work, "������޷�ȷ�������Ĺ���");
		work.doAddParticipateList(useridlist);
	}

	public Object getProcessInstanceVarible(String varible, IContext context) throws Exception {
		Task executeTask = getExecuteTask(context);
		Assert.isNotNull(executeTask, "�޷������������");
		TaskData taskData = executeTask.getTaskData();
		Assert.isNotNull(taskData);
		String processId = taskData.getProcessId();
		long processInstanceId = taskData.getProcessInstanceId();
		WorkflowProcessInstance process = WorkflowService.getDefault().getProcessInstance(processId, processInstanceId);
		Assert.isNotNull(process,"�޷��������ʵ��");

		Object value = process.getVariable(varible);
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setProcessInputValue(String processVar,Object value){
		Assert.isNotNull(processVar);
		Assert.isNotNull(value);
		Object processInput = getValue(F_PROCESSINPUT);
		if(processInput == null){
			processInput = new HashMap<String,Object>();
			setValue(F_PROCESSINPUT, processInput);
		}
		((Map<String, Object>)processInput).put(processVar, value);
	}
}
