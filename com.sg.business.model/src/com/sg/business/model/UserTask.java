package com.sg.business.model;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.jbpm.task.Status;
import org.jbpm.task.Task;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.TaskFormConfig;

public class UserTask extends PrimaryObject {

	public static final String F_WORK_ID = "work_id";

	public static final String F_USERID = "userid";

	public static final String F_TASKID = "taskid";

	/**
	 * �����ֶ�,��������
	 */
	 public static final String F_TASK_NAME = "taskname";

	/**
	 * �����ֶΣ�����ע
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * �����ֶ�,ʵ�ʵĻ������
	 */
	public static final String F_ACTUALOWNER = "actualowner";

	/**
	 * �����ֶΣ�����Ĵ�����
	 */
	public static final String F_CREATEDBY = "createdby";

	/**
	 * �����ֶΣ�����ʱ��
	 */
	public static final String F_CREATEDON = "createdon";

	/**
	 * �����ֶΣ����̶���id
	 */
	public static final String F_PROCESSID = "processid";

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_PROCESSINSTANCEID = "instanceid";

	/**
	 * �����ֶΣ�����״̬
	 */
	public static final String F_STATUS = "status";

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_WORKITEMID = "workitemid";

	/**
	 * �����ֶΣ�֪ͨ����
	 */
	public static final String F_NOTICEDATE = "noticedate";

	/**
	 * �����ֶΣ�ִ����Id(��¼�ʻ�)
	 */
	public static final String F_ACTOR = "actor";

	/**
	 * �����ֶΣ���ʼ����
	 */
	public static final String F_STARTDATE = "startdate";

	/**
	 * �����ֶΣ��������
	 */
	public static final String F_FINISHDATE = "finishdata";

	/**
	 * �����ֶΣ�����Ĳ���
	 */
	public static final String F_ACTION = "action";

	/**
	 * ���̶���ؼ���
	 */
	public static final String F_PROCESSKEY = "processkey";

	/**
	 * �û���������Ƿ��Ѿ��ı�
	 */
	public static final String F_LIFECYCLE_CHANGE_FLAG = "lifecyclechanged";

	public String getStatus() {
		return (String) getValue(F_STATUS);
	}

	public Long getTaskId() {
		Object value = getValue(F_TASKID);
		if(value instanceof Number){
			return new Long(((Number) value).longValue());
		}
		return null;
	}
	
	public Long getProcessInstanceId(){
		Object value = getValue(F_PROCESSINSTANCEID);
		if(value instanceof Number){
			return new Long(((Number) value).longValue());
		}
		return null;
	}

	public String getProcessId() {
		return (String) getValue(F_PROCESSID);
	}

	public String getTaskName() {
		return getDesc();
	}

	public Work getWork() {
		return ModelService.createModelObject(Work.class,
				(ObjectId) getValue(F_WORK_ID));
	}

	public TaskFormConfig getTaskFormConfig() {
		String procDefId = getProcessId();
		String taskName = getTaskName();
		TaskFormConfig conf = WorkflowService.getDefault().getTaskFormConfig(
				procDefId, taskName);
		return conf;
	}

	public Task getTask() {
		return WorkflowService.getDefault().getUserTask(getUserId(), getTaskId());
	}

	public String getUserId() {
		return getStringValue(F_USERID);
	}

	/**
	 * �������������
	 * 
	 * @return
	 */
	public TaskForm makeTaskForm() {
		DBObject data = getWork().get_data();
		TaskForm taskForm = ModelService.createModelObject(TaskForm.class);
		Iterator<String> iter = data.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (!Utils.inArray(key, SYSTEM_RESERVED_FIELDS)) {
				taskForm.setValue(key, data.get(key));
			}
		}
		taskForm.setValue(TaskForm.F_WORK_ID, data.get(Work.F__ID));
		taskForm.setValue(TaskForm.F_USER_TASK_ID, get_id());
		return taskForm;
	}

	public boolean isReserved() {
		return Status.Reserved.name().equals(getStringValue(F_STATUS));
	}

}
