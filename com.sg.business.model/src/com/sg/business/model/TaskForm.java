package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.jbpm.task.Task;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class TaskForm extends PrimaryObject {

	public static final String F_WORK_ID = "workid";
	public static final String F_EDITOR = "form_editor";

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
		Assert.isNotNull(workid, "任务表单无法确定关联的工作");
		return ModelService.createModelObject(Work.class, workid);
	}

	public Task getExecuteTask(IContext context) throws Exception {
		Work work = getWork();
		Assert.isNotNull(work, "任务表单无法确定关联的工作");
		return work.getExecuteTask(context);
	}

	/**
	 * 从任务表单的字段中获取用户的userid,添加到工作中
	 * 字段可以是String类型的字段，直接保存的userid,也可以是数组类型的，保存用户id的列表
	 * 
	 * @param fieldlist
	 * @throws Exception
	 */
	public void doAddWorkParticipatesFromField(String[] fieldlist)
			throws Exception {
		Work work = getWork();
		Assert.isNotNull(work, "任务表单无法确定关联的工作");

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
		Assert.isNotNull(work, "任务表单无法确定关联的工作");
		work.doAddParticipateList(useridlist);
	}
}
