package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.jbpm.task.Status;
import org.jbpm.task.Task;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

public class UserTask extends PrimaryObject implements IWorkRelative{

	public static final String F_FORM_CHOICE = "form_choice";
	
	public static final String F_FORM_COMMENT = "form_comment";

	public static final String F_WORK_ID = "work_id";
	
	public static final String F_WORK_DESC = "work_desc";


	public static final String F_USERID = "userid";

	public static final String F_TASKID = "taskid";

	/**
	 * 任务字段,任务名称
	 */
	public static final String F_TASK_NAME = "taskname";

	/**
	 * 任务字段，任务备注
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * 任务字段,实际的活动所有者
	 */
	public static final String F_ACTUALOWNER = "actualowner";

	/**
	 * 任务字段，任务的创建者
	 */
	public static final String F_CREATEDBY = "createdby";

	/**
	 * 任务字段，创建时间
	 */
	public static final String F_CREATEDON = "createdon";

	/**
	 * 任务字段，流程定义id
	 */
	public static final String F_PROCESSID = "processid";

	/**
	 * 流程名称
	 */
	public static final String F_PROCESSNAME = "processname";

	/**
	 * 任务字段，流程实例id
	 */
	public static final String F_PROCESSINSTANCEID = "instanceid";

	/**
	 * 任务字段，任务状态
	 */
	public static final String F_STATUS = "status";

	/**
	 * 任务字段，流程实例id
	 */
	public static final String F_WORKITEMID = "workitemid";

	/**
	 * 任务字段，通知日期
	 */
	public static final String F_NOTICEDATE = "noticedate";

	/**
	 * 任务字段，执行人Id(登录帐户)
	 */
	public static final String F_ACTOR = "actor";

	/**
	 * 任务字段，开始日期
	 */
	public static final String F_STARTDATE = "startdate";

	/**
	 * 任务字段，完成日期
	 */
	public static final String F_FINISHDATE = "finishdata";

	/**
	 * 任务字段，任务的操作
	 */
	public static final String F_ACTION = "action";

	/**
	 * 流程定义关键字
	 */
	public static final String F_PROCESSKEY = "processkey";

	/**
	 * 该活动生命周期是否已经改变
	 */
	public static final String F_LIFECYCLE_CHANGE_FLAG = "lifecyclechanged";


	public String getStatus() {
		return (String) getValue(F_STATUS);
	}

	public Long getTaskId() {
		Object value = getValue(F_TASKID);
		if (value instanceof Number) {
			return new Long(((Number) value).longValue());
		}
		return null;
	}

	public Long getProcessInstanceId() {
		Object value = getValue(F_PROCESSINSTANCEID);
		if (value instanceof Number) {
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
		return WorkflowService.getDefault().getUserTask(getUserId(),
				getTaskId());
	}

	public String getUserId() {
		return getStringValue(F_USERID);
	}

	/**
	 * 创建任务表单对象
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

	@Override
	public void doInsert(IContext context) throws Exception {
		// 更新旧的相同任务Id的数据为完成
		WriteResult ws = getCollection().update(
				new BasicDBObject().append(UserTask.F_TASKID, getTaskId()),
				new BasicDBObject().append("$set", new BasicDBObject().append(
						UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.TRUE)),
				false, true);
		checkWriteResult(ws);

		super.doInsert(context);

		// 如果是插入reserved 任务，需要消息通知执行人
		if (isReserved()) {
			doNoticeWorkflow(getUserId(), getTaskName(), context);
		}

	}

	private void doNoticeWorkflow(final String recieverId,
			final String taskName, final IContext context) throws Exception {
		// doNoticeWorkflowInternal(actorId, taskName, key, action, context);

		Job job = new Job("发送流程通知") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					doNoticeWorkflowInternal(recieverId, taskName, context);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return org.eclipse.core.runtime.Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	private Message doNoticeWorkflowInternal(String actorId, String taskName,
			IContext context) throws Exception {
		Work work = getWork();
		List<String> recievers = new ArrayList<String>();
		recievers.add(actorId);

		// 设置通知标题
		Project project = work.getProject();
		String title = (project == null ? "" : project.getLabel()) + work + " "
				+ "流程任务: " + taskName;
		// 设置通知内容
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>");
		sb.append("您好: ");
		sb.append("</span><br/><br/>");
		sb.append("您收到了一项流程任务。");
		sb.append("<br/><br/>");

		sb.append("工作");
		sb.append("\"");
		sb.append(work);
		sb.append("\"");
		if (work.isProjectWork()) {
			sb.append(" \"");
			sb.append("项目:");
			sb.append(project);
			sb.append(" \"");
		}
		sb.append("流程任务: ");
		sb.append("\"");
		sb.append(taskName);
		sb.append("\"。");

		sb.append("<br/><br/>");
		sb.append("请登陆系统后查阅有关工作信息和流程历史");

		Message message = MessageToolkit.makeMessage(recievers, title, context
				.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// 设置导航附件
		message.appendTargets(work, Work.EDITOR, false);

		boolean sended = message.doSave(context);
		if (sended) {
			doMarkMessageSended();
		}
		return message;
	}

	private void doMarkMessageSended() throws Exception {
		WriteResult ws = getCollection().update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_NOTICEDATE, new Date())));
		checkWriteResult(ws);
	}

	public Date getCreatedOn() {
		return getDateValue(F_CREATEDON);
	}

	public String getProcessName() {
		return getStringValue(F_PROCESSNAME);
	}

	public String getWorkName() {
		return getStringValue(F_WORK_DESC);
	}

	public User getActualOwner() {
		String userId = getStringValue(F_ACTUALOWNER);
		return UserToolkit.getUserById(userId);
	}

	public String getChoice() {
		return getStringValue(F_FORM_CHOICE);
	}
	
	public String getComment() {
		return getStringValue(F_FORM_COMMENT);
	}

	public ObjectId getWorkId() {
		return getObjectIdValue(F_WORK_ID);
	}

	
	@Override
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");
		// 显示否决图标
		String choice = getChoice();
		if (choice != null) {
			if (Utils.isDenied(choice)) {
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_DENIED_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />");
			} else if (Utils.isAdmit(choice)) {
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_PASS_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />");
			}
		}
		
		
		// 显示流程任务的内容
		String status = getStatus();
		sb.append("<span style='float:right;padding-right:14px'>");
		User owner = getActualOwner();
		sb.append(owner);
		sb.append(" ");

		Date createOn = get_cdate();
		sb.append(String.format("%1$tm/%1$te %1$tH:%1$tM", createOn));
		sb.append(" ");

		if (Status.Reserved.name().equals(status)) {
			sb.append("接收");
		} else if (Status.InProgress.name().equals(status)) {
			sb.append("开始");
		} else if (Status.Completed.name().equals(status)) {
			sb.append("完成");
		} else if (Status.Created.name().equals(status)) {
			sb.append("创建");
		} else if (Status.Ready.name().equals(status)) {
			sb.append("预备");
		} else if (Status.Suspended.name().equals(status)) {
			sb.append("暂停");
		} else if (Status.Exited.name().equals(status)) {
			sb.append("退出");
		}

		sb.append("</span>");

		if (Status.Completed.name().equals(status)) {
			String editorId = (String) getStringValue(TaskForm.F_EDITOR);
			if (editorId != null) {
				sb.append("<a href=\"" + get_id().toString() + "@"
						+ "open" + "\" target=\"_rwt\">");
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_BULLETING_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />");
				sb.append("</a>");
			}
		}

		String desc = getTaskName();
		sb.append(desc);

		if (choice != null) {
			sb.append(" ");
			String coloredString = Utils.appendColor(choice);
			sb.append(coloredString);
			sb.append("");
		}
		

		sb.append("<br/>");

		// 显示任务详细信息
		sb.append("<small>");
		String comment = getComment();
		if (comment != null) {
			sb.append(" ");
			comment = Utils.getPlainText(comment);
			comment = Utils.getLimitLengthString(comment, 40);
			sb.append(comment);
		}
		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
	}
}
