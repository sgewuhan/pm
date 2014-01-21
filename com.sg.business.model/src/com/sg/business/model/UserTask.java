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
import com.sg.business.resource.nls.Messages;

public class UserTask extends PrimaryObject implements IWorkRelative{

	public static final String F_FORM_CHOICE = "form_choice"; //$NON-NLS-1$
	
	public static final String F_FORM_COMMENT = "form_comment"; //$NON-NLS-1$

	public static final String F_WORK_ID = "work_id"; //$NON-NLS-1$
	
	public static final String F_WORK_DESC = "work_desc"; //$NON-NLS-1$


	public static final String F_USERID = "userid"; //$NON-NLS-1$

	public static final String F_TASKID = "taskid"; //$NON-NLS-1$

	/**
	 * �����ֶ�,��������
	 */
	public static final String F_TASK_NAME = "taskname"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ע
	 */
	public static final String F_DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * �����ֶ�,ʵ�ʵĻ������
	 */
	public static final String F_ACTUALOWNER = "actualowner"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����Ĵ�����
	 */
	public static final String F_CREATEDBY = "createdby"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʱ��
	 */
	public static final String F_CREATEDON = "createdon"; //$NON-NLS-1$

	/**
	 * �����ֶΣ����̶���id
	 */
	public static final String F_PROCESSID = "processid"; //$NON-NLS-1$

	/**
	 * ��������
	 */
	public static final String F_PROCESSNAME = "processname"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_PROCESSINSTANCEID = "instanceid"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����״̬
	 */
	public static final String F_STATUS = "status"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_WORKITEMID = "workitemid"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�֪ͨ����
	 */
	public static final String F_NOTICEDATE = "noticedate"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�ִ����Id(��¼�ʻ�)
	 */
	public static final String F_ACTOR = "actor"; //$NON-NLS-1$

	/**
	 * �����ֶΣ���ʼ����
	 */
	public static final String F_STARTDATE = "startdate"; //$NON-NLS-1$

	/**
	 * �����ֶΣ��������
	 */
	public static final String F_FINISHDATE = "finishdata"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����Ĳ���
	 */
	public static final String F_ACTION = "action"; //$NON-NLS-1$

	/**
	 * ���̶���ؼ���
	 */
	public static final String F_PROCESSKEY = "processkey"; //$NON-NLS-1$

	/**
	 * �û���������Ƿ��Ѿ��ı�
	 */
	public static final String F_LIFECYCLE_CHANGE_FLAG = "lifecyclechanged"; //$NON-NLS-1$


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

	@Override
	public void doInsert(IContext context) throws Exception {
		// ���¾ɵ���ͬ����Id������Ϊ���
		WriteResult ws = getCollection().update(
				new BasicDBObject().append(UserTask.F_TASKID, getTaskId()),
				new BasicDBObject().append("$set", new BasicDBObject().append( //$NON-NLS-1$
						UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.TRUE)),
				false, true);
		checkWriteResult(ws);

		super.doInsert(context);

		// ����ǲ���reserved ������Ҫ��Ϣִ֪ͨ����
		if (isReserved()) {
			doNoticeWorkflow(getUserId(), getTaskName(), context);
		}

	}

	private void doNoticeWorkflow(final String recieverId,
			final String taskName, final IContext context) throws Exception {
		// doNoticeWorkflowInternal(actorId, taskName, key, action, context);

		Job job = new Job("��������֪ͨ") { //$NON-NLS-1$

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

		// ����֪ͨ����
		Project project = work.getProject();
		String title = (project == null ? "" : project.getLabel()) + work + " " //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.get().UserTask_0 + taskName;
		// ����֪ͨ����
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>"); //$NON-NLS-1$
		sb.append(Messages.get().UserTask_1);
		sb.append("</span><br/><br/>"); //$NON-NLS-1$
		sb.append(Messages.get().UserTask_2);
		sb.append("<br/><br/>"); //$NON-NLS-1$

		sb.append(Messages.get().UserTask_3);
		sb.append("\""); //$NON-NLS-1$
		sb.append(work);
		sb.append("\""); //$NON-NLS-1$
		if (work.isProjectWork()) {
			sb.append(" \""); //$NON-NLS-1$
			sb.append(Messages.get().UserTask_4);
			sb.append(project);
			sb.append(" \""); //$NON-NLS-1$
		}
		sb.append(Messages.get().UserTask_5);
		sb.append("\""); //$NON-NLS-1$
		sb.append(taskName);
		sb.append("\"��"); //$NON-NLS-1$

		sb.append("<br/><br/>"); //$NON-NLS-1$
		sb.append(Messages.get().UserTask_6);

		Message message = MessageToolkit.makeMessage(recievers, title, context
				.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// ���õ�������
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
				new BasicDBObject().append("$set", //$NON-NLS-1$
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
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:9pt'>"); //$NON-NLS-1$
		// ��ʾ���ͼ��
		String choice = getChoice();
		if (choice != null) {
			if (Utils.isDenied(choice)) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_DENIED_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />"); //$NON-NLS-1$
			} else if (Utils.isAdmit(choice)) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_PASS_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />"); //$NON-NLS-1$
			}
		}
		
		
		// ��ʾ�������������
		String status = getStatus();
		sb.append("<span style='float:right;padding-right:14px'>"); //$NON-NLS-1$
		User owner = getActualOwner();
		sb.append(owner.getUsername());
		sb.append(" "); //$NON-NLS-1$
		Date createOn = get_cdate();
		sb.append(String.format("%1$ty/%1$tm/%1$te %1$tH:%1$tM", createOn)); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$

		if (Status.Reserved.name().equals(status)) {
			sb.append(Messages.get().UserTask_7);
		} else if (Status.InProgress.name().equals(status)) {
			sb.append(Messages.get().UserTask_8);
		} else if (Status.Completed.name().equals(status)) {
			sb.append(Messages.get().UserTask_9);
		} else if (Status.Created.name().equals(status)) {
			sb.append(Messages.get().UserTask_10);
		} else if (Status.Ready.name().equals(status)) {
			sb.append(Messages.get().UserTask_11);
		} else if (Status.Suspended.name().equals(status)) {
			sb.append(Messages.get().UserTask_12);
		} else if (Status.Exited.name().equals(status)) {
			sb.append(Messages.get().UserTask_13);
		}

		sb.append("</span>"); //$NON-NLS-1$

		if (Status.Completed.name().equals(status)) {
			String editorId = (String) getStringValue(TaskForm.F_EDITOR);
			if (editorId != null) {
				sb.append("<a href=\"" + get_id().toString() + "@" //$NON-NLS-1$ //$NON-NLS-2$
						+ "open" + "\" target=\"_rwt\">"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_BULLETING_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />"); //$NON-NLS-1$
				sb.append("</a>"); //$NON-NLS-1$
			}
		}

		String desc = getTaskName();
		sb.append(desc);

		if (choice != null) {
			sb.append(" "); //$NON-NLS-1$
			String coloredString = Utils.appendColor(choice);
			sb.append(coloredString);
			sb.append(""); //$NON-NLS-1$
		}
		

		sb.append("<br/>"); //$NON-NLS-1$

		// ��ʾ������ϸ��Ϣ
		sb.append("<small style='color:#b0b0b0'>"); //$NON-NLS-1$
		String comment = getComment();
		if (comment != null) {
			sb.append(" "); //$NON-NLS-1$
			comment = Utils.getPlainText(comment);
			comment = Utils.getLimitLengthString(comment, 40);
			sb.append(comment);
		}
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}
	
	protected boolean savelog(int logtype, IContext context) {
		return false;
	}
}
