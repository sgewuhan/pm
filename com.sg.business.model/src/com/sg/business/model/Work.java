package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.drools.runtime.process.ProcessInstance;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.jbpm.task.I18NText;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.IPrimaryObjectEventListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.BackgroundContext;

/**
 * <p>
 * ����
 * <p/>
 * ��������������Ŀ�еĹ����ֽ�ṹ
 * 
 * @author jinxitao
 * 
 */
public class Work extends AbstractWork implements IProjectRelative, ISchedual,
		ILifecycle, IReferenceContainer {

	/**
	 * �趨����������ı༭��
	 */
	public static final String EDIT_WORK_DELIVERABLE = "edit.work.deliverable";

	/**
	 * ������Ҷ�ӹ����༭��
	 */
	public static final String EDIT_WORK_PLAN_1 = "edit.work.plan.1";

	public static final String TEMPLATE_DELIVERABLE = "template_deliverable";

	/**
	 * ��������Ҷ�ӹ����༭��
	 */
	public static final String EDIT_WORK_PLAN_0 = "edit.work.plan.0";

	/**
	 * �����ı༭��ID
	 */
	public static final String EDITOR = "view.work";

	/**
	 * �������õı༭��ID
	 */
	public static final String EDITOR_SETTING = "editor.work.setting";

	/**
	 * ���������ı༭��ID
	 */
	public static final String EDITOR_CREATE_RUNTIME_WORK = "editor.create.runtimework";

	/**
	 * �༭�����ƻ�
	 */
	// public static final String EDITOR_WORK_PLAN = "edit.work.plan";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * �鵵�ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_ARCHIVE = "archive";

	/**
	 * �����˵�id userid
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * ָ���ߵ�id
	 */
	public static final String F_ASSIGNER = "assignerid";

	/**
	 * �����е���
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * �����������ִ����
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	/**
	 * ����ִ������ִ����
	 */
	public static final String F_WF_EXECUTE_ACTORS = "wf_execute_actors";

	/**
	 * �����¹������򵼱༭��
	 */
	public static final String EDITOR_LAUNCH = "editor.work.launch";

	/**
	 * ����
	 */
	public static final String F_DESCRIPTION = "description";

	public static final String F_IS_PROJECT_WBSROOT = "iswbsroot";

	public static final String F_MARK = "marked";

	public static final String F_RECORD = "record";

	public static final String F_WORK_DEFINITION_ID = "workd_id";

	public static final String F_USE_PROJECT_ROLE = "useprojectrole";

	public static final String F_PERFORMENCE = "performence";

	public static final String[] ARCHIVE_FIELDS = new String[] {
			F_ASSIGNMENT_CHARGER_ROLE_ID, F_CHARGER_ROLE_ID,
			F_PARTICIPATE_ROLE_SET, F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
			F_SETTING_AUTOSTART_WHEN_PARENT_START,
			F_SETTING_CAN_ADD_DELIVERABLES, F_SETTING_CAN_BREAKDOWN,
			F_SETTING_CAN_MODIFY_PLANWORKS,
			F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
			F_SETTING_PROJECTCHANGE_MANDORY, F_SETTING_WORKCHANGE_MANDORY,
			F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH, F_WF_CHANGE_ASSIGNMENT,
			F_WF_EXECUTE_ASSIGNMENT, F_TARGETS };

	private Double overCount = null;

	// /**
	// * ��Ч��¼�ֶ�
	// */
	// private static final String F_SF_PERFORMENCE_USERID = "userid";
	//
	// private static final String F_SF_PERFORMENCE_DATE = "date";
	//
	// private static final String F_SF_PERFORMENCE_ACTUALWORKS = "actualworks";
	//
	// /**
	// * ��Ч����ʱ�����{����1:{����:value,����:value},����2:{����:value,����:value}}
	// */
	// private static final String F_PERFORMENCE_WORKS_ALLOCATE_TABLE =
	// "performence_works_allocate";
	//
	// private static final String F_PERFORMENCE_ISSUMMARY =
	// "performence_issummary";

	/**
	 * ����״̬���ز�ͬ��ͼ��
	 */
	@Override
	public Image getImage() {
		String lc = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_WORK_CANCEL_16);
		} else if (STATUS_FINIHED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_WORK_FINISH_16);
		} else if (STATUS_NONE_VALUE.equals(lc)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
		} else if (STATUS_ONREADY_VALUE.equals(lc)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
		} else if (STATUS_PAUSED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_WORK_PAUSE_16);
		} else if (STATUS_WIP_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_WORK_WIP_16);
		}
		return super.getImage();
	}

	/**
	 * ���ع���������Ŀ
	 * 
	 * @return Project
	 */
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * �����¼�����
	 * 
	 * @return Work
	 */
	@Override
	public Work makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(F_PARENT_ID, get_id());
		data.put(F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		data.put(F_PROJECT_ID, getValue(F_PROJECT_ID));

		// ����һЩ������ѡ���趨
		data.put(F_SETTING_CAN_ADD_DELIVERABLES, Boolean.TRUE);
		data.put(F_SETTING_CAN_BREAKDOWN, Boolean.TRUE);
		data.put(F_SETTING_CAN_MODIFY_PLANWORKS, Boolean.TRUE);

		Work po = ModelService.createModelObject(data, Work.class);
		return po;
	}

	/**
	 * ���ع���������Ŀ
	 * 
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProject();
	}

	/**
	 * ���ع���������Ŀ_id
	 * 
	 * @return
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * ���ع����ƻ���ʼʱ��
	 * 
	 * @return Date
	 */
	public Date getPlanStart() {
		Object value = getValue(F_PLAN_START);
		if (value instanceof Date) {
			return Utils.getDayBegin((Date) value).getTime();
		} else {
			return null;
		}

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// Object value = getValue(F_PLAN_START);
		// if (value instanceof Date) {
		// return Utils.getDayBegin((Date) value).getTime();
		// } else {
		// return null;
		// }
		// } else {
		// Date start = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Date s = child.getPlanStart();
		// if (s != null && (start == null || s.before(start))) {
		// start = s;
		// }
		// }
		//
		// // ����¼�û�мƻ���ʼ��ȡ�Լ��ļƻ���ʼ
		// if (start != null) {
		// return start;
		// } else {
		// Object value = getValue(F_PLAN_START);
		// if (value instanceof Date) {
		// return Utils.getDayBegin((Date) value).getTime();
		// } else {
		// return null;
		// }
		// }
		//
		// }
	}

	/**
	 * ���ع����ƻ����ʱ��
	 * 
	 * @return Date
	 */
	public Date getPlanFinish() {
		Object value = getValue(F_PLAN_FINISH);
		if (value instanceof Date) {
			return Utils.getDayEnd((Date) value).getTime();
		} else {
			return null;
		}

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// Object value = getValue(F_PLAN_FINISH);
		// if (value instanceof Date) {
		// return Utils.getDayEnd((Date) value).getTime();
		// } else {
		// return null;
		// }
		// } else {
		// Date finish = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Date f = child.getPlanFinish();
		// if (f != null && (finish == null || f.after(finish))) {
		// finish = f;
		// }
		// }
		//
		// if (finish != null) {
		// return finish;
		// } else {
		// Object value = getValue(F_PLAN_FINISH);
		// if (value instanceof Date) {
		// return Utils.getDayEnd((Date) value).getTime();
		// } else {
		// return null;
		// }
		// }
		//
		// }
	}

	/**
	 * ���ع���ʵ�ʿ�ʼʱ��
	 * 
	 * @return Date
	 */
	public Date getActualStart() {
		Date d = (Date) getValue(F_ACTUAL_START);
		if (d != null) {
			return Utils.getDayBegin(d).getTime();
		} else {
			return null;
		}

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// Date d = (Date) getValue(F_ACTUAL_START);
		// if (d != null) {
		// return Utils.getDayBegin(d).getTime();
		// } else {
		// return null;
		// }
		// } else {
		// Date start = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Date s = child.getActualStart();
		// if (s != null && (start == null || s.before(start))) {
		// start = s;
		// }
		// }
		// return start;
		// }
	}

	/**
	 * ���ع���ʵ�����ʱ��
	 * 
	 * @return Date
	 */
	public Date getActualFinish() {
		Date d = (Date) getValue(F_ACTUAL_FINISH);
		if (d != null) {
			return Utils.getDayEnd(d).getTime();
		} else {
			return null;
		}
		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// Date d = (Date) getValue(F_ACTUAL_FINISH);
		// if (d != null) {
		// return Utils.getDayEnd(d).getTime();
		// } else {
		// return null;
		// }
		// } else {
		// Date finish = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Date f = child.getActualFinish();
		// if (f != null && (finish == null || f.after(finish))) {
		// finish = f;
		// }
		// }
		// return finish;
		// }
	}

	/**
	 * ���ع�����ʵ�ʹ�ʱ
	 * 
	 * @return Double
	 */
	public Double getActualWorks() {
		return (Double) getValue(F_ACTUAL_WORKS);

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// return (Double) getValue(F_ACTUAL_WORKS);
		// } else {
		// Double works = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Double w = child.getActualWorks();
		// if (w != null) {
		// if (works == null) {
		// works = w;
		// } else {
		// works = works + w;
		// }
		// }
		// }
		// return works;
		// }
	}

	/**
	 * ���ع����ļƻ���ʱ
	 * 
	 * @return Double
	 */
	public Double getPlanWorks() {
		return (Double) (getValue(F_PLAN_WORKS));

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// return (Double) getValue(F_PLAN_WORKS);
		// } else {
		// Double works = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Double w = child.getPlanWorks();
		// if (w != null) {
		// if (works == null) {
		// works = w;
		// } else {
		// works = works + w;
		// }
		// }
		// }
		// return works;
		// }
	}

	/**
	 * ���ع�����ʵ�ʹ���
	 * 
	 * @return Integer
	 */
	public Integer getActualDuration() {
		return (Integer) getValue(F_ACTUAL_DURATION);

		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// return (Integer) getValue(F_ACTUAL_DURATION);
		// } else {
		// Integer duration = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Integer d = child.getActualDuration();
		// if (d != null) {
		// if (duration == null) {
		// duration = d;
		// } else if (duration < d) {
		// duration = d;
		// }
		// }
		// }
		// return duration;
		// }
	}

	/**
	 * ���ع����ļƻ�����
	 * 
	 * @return Integer
	 */
	public Integer getPlanDuration() {
		return (Integer) getValue(F_PLAN_DURATION);
		// List<PrimaryObject> children = getChildrenWork();
		// if (children.size() == 0) {
		// return (Integer) getValue(F_PLAN_DURATION);
		// } else {
		// Integer duration = null;
		// for (int i = 0; i < children.size(); i++) {
		// Work child = (Work) children.get(i);
		// Integer d = child.getPlanDuration();
		// if (d != null) {
		// if (duration == null) {
		// duration = d;
		// } else if (duration < d) {
		// duration = d;
		// }
		// }
		// }
		// return duration;
		// }
	}

	/**
	 * �½�����������
	 * 
	 * @return Deliverable
	 */
	@Override
	public Deliverable makeDeliverableDefinition(String type) {
		return makeDeliverableDefinition(null, type);
	}

	/**
	 * �½�����������
	 * 
	 * @param docd
	 *            ,�ĵ�ģ�嶨��
	 * @return Deliverable
	 */
	public Deliverable makeDeliverableDefinition(DocumentDefinition docd,
			String type) {
		DBObject data = new BasicDBObject();
		data.put(Deliverable.F_WORK_ID, get_id());

		data.put(Deliverable.F_PROJECT_ID, getValue(F_PROJECT_ID));
		data.put(Deliverable.F_TYPE, type);

		if (docd != null) {
			data.put(Deliverable.F_DOCUMENT_DEFINITION_ID, docd.get_id());
			data.put(Deliverable.F_DESC, docd.getDesc());
		}

		Deliverable po = ModelService
				.createModelObject(data, Deliverable.class);

		return po;
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "����";
	}

	/**
	 * ���ع���Ĭ�ϱ༭��ID
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * �жϹ����������Ƿ�ֻ��
	 * 
	 * @param field
	 *            ,����������
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String field, IContext context) {
		return true;
	}

	@Override
	public boolean canCheck() {
		// δ��ɺ�δȡ����
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))
				&& (!STATUS_FINIHED_VALUE.equals(lc));
	}

	/**
	 * �ܷ�������
	 */
	@Override
	public boolean canStart() {
		// ��鱾������������״̬�Ƿ����:׼���У���״̬������ͣ
		// ���������Щ״̬(����ɡ���ȡ����������)������false
		String lifeCycle = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_WIP_VALUE.equals(lifeCycle)) {
			return false;
		}

		return true;
	}

	/**
	 * �ܷ�����ͣ
	 */
	@Override
	public boolean canPause() {
		// ��鱾������������״̬�Ƿ����:������
		// ���������Щ״̬(����ɡ���ȡ����׼���С���״̬������ͣ)������false
		String lifeCycle = getLifecycleStatus();
		if (STATUS_WIP_VALUE.equals(lifeCycle)) {
			return true;
		}

		return false;
	}

	/**
	 * �ܷ���ȡ��
	 */
	@Override
	public boolean canCancel() {
		// ��鱾������������״̬�Ƿ����:����ͣ,�����У�׼����
		String lifeCycle = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)
				|| STATUS_ONREADY_VALUE.equals(lifeCycle)
				|| STATUS_NONE_VALUE.equals(lifeCycle);
	}

	@Override
	public boolean canFinish() {
		// 1.���ȼ�鱾������������״̬�Ƿ����:����ͣ,������
		// ���������Щ״̬(����ɡ�׼���С���״̬����ȡ��)������false
		String lifeCycle = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle);
	}

	@Override
	public boolean canCommit() {
		String lc = getLifecycleStatus();

		return STATUS_NONE_VALUE.equals(lc);
	}

	/**
	 * �ж������Ƿ���Ե����ʼ
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public boolean canWorkflowStart(IContext context) {
		String lc = getLifecycleStatus();
		return ILifecycle.STATUS_WIP_VALUE.equals(lc);
	}

	/**
	 * �ж������Ƿ���Ե�����
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public boolean canWorkflowFinish(IContext context) {
		String lc = getLifecycleStatus();
		return ILifecycle.STATUS_WIP_VALUE.equals(lc);
	}

	/**
	 * �ж������Ƿ���Ե����ֹ
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public boolean canWorkflowCancel(IContext context) {
		String lc = getLifecycleStatus();
		return ILifecycle.STATUS_WIP_VALUE.equals(lc);
	}

	/**
	 * ��鹤���Ƿ����ȡ��
	 * 
	 * @param context
	 * @throws Exception
	 */
	// public void cancelCheck(IContext context) throws Exception {
	// // 1.�ж��Ƿ��Ǳ����ĸ�����
	// String userId = context.getAccountInfo().getConsignerId();
	// Work parent = (Work) getParent();
	// if (parent != null) {
	// if (!userId.equals(parent.getChargerId())) {
	// throw new Exception("���ǹ��������ˣ�" + parent);
	// }
	// } else {
	// throw new Exception("����������ȡ����" + this);
	// }
	//
	// if (isMandatory()) {
	// throw new Exception("����������ȡ����" + this);
	// }
	//
	// // 2.�ж��ϼ�������������״̬�Ƿ���ϣ�������
	// // ������ڽ����У�����false
	// Work parentWork = (Work) getParent();
	// if (parentWork != null) {
	// if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
	// throw new Exception("�ϼ��������ڽ����У�" + this);
	// }
	// } else {
	// Project project = getProject();
	// if (project != null) {
	// if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
	// throw new Exception("��Ŀ���ڽ����У�" + this);
	// }
	// }
	// }
	// }

	public boolean isMandatory() {
		return Boolean.TRUE.equals(getValue(F_MANDATORY)) || isMilestone();
	}

	public boolean isMilestone() {
		return Boolean.TRUE.equals(getValue(F_MILESTONE));
	}

	public boolean isArchive() {
		return Boolean.TRUE.equals(getValue(F_ARCHIVE));
	}

	/**
	 * �ܷ����༭
	 */
	@Override
	public boolean canEdit(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
		String lifeCycle = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		if (isProjectWBSRoot()) {
			return false;
		}
		// ��Ŀ������,��̱�����������༭
		// if (!isStandloneWork()) {
		// Project project = getProject();
		// if (project != null) {
		// String projectLifecycle = project.getLifecycleStatus();
		// if (STATUS_WIP_VALUE.equals(projectLifecycle)) {
		// if (isMilestone()) {
		// return false;
		// }
		// }
		// }
		// }

		// 2.�ж��Ƿ�Ϊ�ù������ϼ������ĸ����˻���Ŀ����Ŀ����
		
		if (hasPermissionForReassignment(context)) {
			return true;
		}
		return hasPermission(context);
	}

	/**
	 * �ܹ����������Ϣ
	 * 
	 * @param currentAccountContext
	 * @return
	 */
//	public boolean canSendMessage(IContext context) {
//		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
//		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
//		String lifeCycle = getLifecycleStatus();
//		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
//				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
//				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
//			return false;
//		}
//
//		if (isProjectWBSRoot()) {
//			return false;
//		}
//		if (hasPermissionForReassignment(context)) {
//			return true;
//		}
//		return hasPermission(context);
//	}

	/**
	 * �ܷ���ɾ��
	 */
	@Override
	public boolean canDelete(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ��������)������false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)
				|| STATUS_WIP_VALUE.equals(lifeCycle)) {
			return false;
		}

		if (isMandatory()) {
			return false;
		}
		if (isProjectWBSRoot()) {
			return false;
		}

		// ��Ŀ������,��̱�����������ɾ��
		if (!isStandloneWork()) {
			Project project = getProject();
			if (project != null) {
				String projectLifecycle = project.getLifecycleStatus();
				if (STATUS_WIP_VALUE.equals(projectLifecycle)) {
					if (isMilestone()) {
						return false;
					}
				}
			}
		}
		// 2.�ж��Ƿ�Ϊ�ù������ϼ������ĸ����˻���Ŀ����Ŀ����
		return hasPermission(context);
	}

	/**
	 * �ܷ�༭������¼
	 * 
	 * @param context
	 * @return
	 */
	public boolean canEditWorkRecord(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. ����ժҪ����ʱ������false
		if (isSummaryWork()) {
			return false;
		}

		// 3.�ж��Ƿ�Ϊ�ù������ϼ������ĸ����˻���Ŀ����Ŀ����
		return hasPermission(context);
	}

	/**
	 * �ܷ���ӹ���
	 * 
	 * @param context
	 * @return
	 */
	public boolean canCreateChildWork(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. ����ժҪ����ʱ���Ƿ�����������ֽ⣬���û�У�����false
		if (!isSummaryWork()
				&& !Boolean.TRUE.equals(getValue(F_SETTING_CAN_BREAKDOWN))) {
			return false;
		}

		// 3.�ж��Ƿ�Ϊ�ù������ϼ������ĸ����˻���Ŀ����Ŀ����
		return hasPermission(context);
	}

	/**
	 * �ܷ���ӽ�����
	 * 
	 * @param context
	 * @return
	 */
	public boolean canCreateDeliverable(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. ����ժҪ����ʱ������false
		if (isSummaryWork()) {
			return false;
		}

		// 3.��������˲�����ӽ�������ؼ�
		if (!Boolean.TRUE.equals(getValue(F_SETTING_CAN_ADD_DELIVERABLES))) {
			return false;
		}

		// 4.�ж��Ƿ�Ϊ�ù������ϼ������ĸ����˻���Ŀ����Ŀ����
		if (hasPermission(context)) {
			return true;
		} else {
			String userId = context.getAccountInfo().getConsignerId();
			BasicBSONList participatesIdList = getParticipatesIdList();
			for (Object object : participatesIdList) {
				String participatesId = (String) object;
				if (userId.equals(participatesId)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * �ܷ���й���ָ��
	 * 
	 * @param context
	 * @return
	 */
	public boolean canReassignment(IContext context) {
		// 1.���ȼ�鱾������������״̬�Ƿ����:׼���У������У���״̬��
		// ���������Щ״̬(����ɡ���ȡ��������ͣ)������false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2.�ж��Ƿ�Ϊ�ù������ϼ�������ָ����
		return hasPermissionForReassignment(context);
	}

	/**
	 * ��鹤���Ƿ��������
	 * 
	 * @param context
	 * @throws Exception
	 */
	public List<Object[]> checkStartAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.�ж��Ƿ��Ǳ����ĸ�����
		// ������ڽ����У�����false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("��Ŀ���ڽ����У�" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("���Ǳ����������ˣ�" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("�ϼ��������ڽ����У�" + this);
				}
			}
		}

		// 3.�жϱ����������¼������ı�Ҫ��Ϣ�Ƿ�¼��
		message.addAll(checkCascadeStart(false));
		return message;
	}

	@Override
	public List<Object[]> checkCancelAction(IContext context) throws Exception {
		try {
			reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���ж��Ƿ񶥼�����
		// 1.�ж��Ƿ��Ǳ����ĸ�����
		// 2.�ж��ϼ�������������״̬�Ƿ���ϣ�������
		// ������ڽ����У�����false
		String userId = context.getAccountInfo().getConsignerId();
		Project project = getProject();
		if (isProjectWBSRoot()) {
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("��Ŀ���ڽ����У�" + this);
				}
			}
		} else {
			// 1.�ж��Ƿ��Ǳ����ĸ�����
			Work parent = (Work) getParent();
			if (parent != null) {
				if (!STATUS_WIP_VALUE.equals(parent.getLifecycleStatus())) {
					throw new Exception("�ϼ��������ڽ����У�" + this);
				}
				if (parent.isProjectWBSRoot()) {
					if (!userId.equals(project.getChargerId())) {
						throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
					}
				} else {
					if (!parent.hasPermission(context)) {
						throw new Exception("���ǹ��������ˣ�" + parent);
					}
				}
			} else {
				if (project != null) {
					if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
						throw new Exception("��Ŀ���ڽ����У�" + this);
					}
				}
				throw new Exception("����������ȡ����" + this);
			}

			if (isMandatory()) {
				throw new Exception("����������ȡ����" + this);
			}
		}

		return null;
	}

	/**
	 * ��鹤���Ƿ�������
	 * 
	 * @param context
	 * @throws Exception
	 */
	@Override
	public List<Object[]> checkFinishAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// ���ж��Ƿ񶥼�����
		// 1.�ж��Ƿ��Ǳ����ĸ�����
		// 2.�ж��ϼ�������������״̬�Ƿ���ϣ�������
		// ������ڽ����У�����false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("��Ŀ���ڽ����У�" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("���Ǳ����������ˣ�" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("�ϼ��������ڽ����У�" + this);
				}
			}
		}

		// ��������Ƿ����
		if (!Boolean.TRUE
				.equals(getValue(F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH))) {
			ProcessInstance pi = getExecuteProcess();
			if (pi != null) {
				if (pi.getState() != ProcessInstance.STATE_COMPLETED
						|| pi.getState() != ProcessInstance.STATE_ABORTED) {
					throw new Exception("�������������ǰ������ɣ�" + this);
				}
			}
		}

		// ����¼�����Ľ������Ƿ��ϴ��˸���
		List<PrimaryObject> delis = getDeliverable();
		if (delis != null) {
			for (int i = 0; i < delis.size(); i++) {
				Deliverable deli = (Deliverable) delis.get(i);
				if (deli.isMandatory()) {
					Document doc = deli.getDocument();
					doc.checkMandatory();
				}
			}
		}

		// 3.�ж��¼�������ɵĹ����Ƿ������ɣ��Ǽ�����ɵĹ����Ƿ��Ѿ��������״̬����ȡ��״̬
		message.addAll(checkCascadeFinish(get_id()));
		return message;

	}

	@Override
	public List<Object[]> checkPauseAction(IContext context) throws Exception {
		try {
			reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���ж��Ƿ񶥼�����
		// 1.�ж��Ƿ��Ǳ����ĸ�����
		// 2.�ж��ϼ�������������״̬�Ƿ���ϣ�������
		// ������ڽ����У�����false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("��Ŀ���ڽ����У�" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("���Ǳ����������ˣ�" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("�ϼ��������ڽ����У�" + this);
				}
			}
		}
		return null;
	}

	/**
	 * ���㹤��
	 * 
	 * @param fStart
	 *            ,��ʼ����
	 * @param fFinish
	 *            ,�������
	 * @param fDuration
	 *            ,����
	 * @throws Exception
	 */
	public void checkAndCalculateDuration(String fStart, String fFinish,
			String fDuration) throws Exception {
		Date start = (Date) getValue(fStart);
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish = (Date) getValue(fFinish);
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start != null && finish != null) {
			// ����Ƿ�Ϸ�
			if (start.after(finish)) {
				throw new Exception("��ʼ���ڱ��������������");
			}

			// ���㹤��
			Calendar sdate = Utils.getDayBegin(start);
			Calendar edate = Utils.getDayEnd(finish);
			long l = (edate.getTimeInMillis() - sdate.getTimeInMillis())
					/ (1000 * 60 * 60 * 24);
			setValue(fDuration, new Integer((int) l));
		}
	}

	/**
	 * ���Ӽ�� �����Ŀ�ʼʱ�䲻��������Ŀ�Ŀ�ʼʱ�䣬����ʱ�䲻��������Ŀ�Ľ���ʱ��
	 * 
	 * @throws Exception
	 */
	public void checkProjectTimeline() throws Exception {
		Project project = getProject();
		if (!isProjectWork()) {
			return;
		}

		Date start = getPlanStart();
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish = getPlanFinish();
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start == null && finish == null) {
			return;
		}

		Date projstart = project.getPlanStart();
		if (projstart != null) {
			projstart = Utils.getDayBegin(projstart).getTime();

			if (start != null && start.before(projstart)) {
				throw new Exception("�����Ŀ�ʼʱ�䲻��������Ŀ�Ŀ�ʼʱ��");
			}

		} else {
			return;
		}

		Date projfinish = project.getPlanFinish();
		if (projfinish != null) {
			projfinish = Utils.getDayEnd(projfinish).getTime();

			if (finish != null && finish.after(projfinish)) {
				throw new Exception("�����Ľ���ʱ�䲻��������Ŀ�Ľ���ʱ��");
			}
		} else {
			return;
		}

	}

	/**
	 * ��Ŀ���
	 * 
	 * @return
	 */
	public List<ICheckListItem> checkPlan() {
		Project project = getProject();
		Map<ObjectId, List<PrimaryObject>> ram = project.getRoleAssignmentMap();
		return checkPlan(project, ram);
	}

	/**
	 * 1. ��鹤���ļƻ�ʱ�䣺����û���趨�ƻ���ʼ���ƻ���ɡ��ƻ���ʱ��Ҷ���� <br/>
	 * 2. ��鹤���ĸ����� ������û���趨�����ˣ�����û���趨ָ���ߵ�Ҷ���� <br/>
	 * 3. �����������趨 �����棬û��ָ������ִ���ߵĹ��� <br/>
	 * 4. �������� <br/>
	 * 4.1. ��鹤���Ƿ���н�������棬û�н������Ҷ���� 4.2. ��齻�����ĵ�û�е����ļ���Ϊģ�壺����
	 * 
	 * @param project
	 * 
	 * @param roleMap
	 * 
	 * @return
	 */
	public List<ICheckListItem> checkPlan(Project project,
			Map<ObjectId, List<PrimaryObject>> roleMap) {
		ArrayList<ICheckListItem> result = new ArrayList<ICheckListItem>();
		List<PrimaryObject> childrenWork = getChildrenWork();
		if (childrenWork.size() > 0) {// ������¼���ֱ�ӷ����¼��ļ����
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				result.addAll(childWork.checkPlan(project, roleMap));
			}
		}
		if (isMilestone()) {
			// ****************************************************************************************
			// 1 ��鹤���ļƻ���ʼ�ͼƻ����
			Object value = getPlanStart();
			boolean passed = true;
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ���ʼû��ȷ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanFinish();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ����ʱ��û��ȷ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanWorks();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ���ʱû��ȷ��", "��ȷ���üƻ���ʱ��ʾ�ù��������㹤ʱ�����������ϣ�������������ύǰȷ����",
						ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_WORKS);
				result.add(checkItem);
				passed = false;
			}

			value = getDesc();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"��������Ϊ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_DESC);
				result.add(checkItem);
			}

			if (passed) {
				CheckListItem checkItem = new CheckListItem("��鹤����������");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			// ****************************************************************************************
			// 2 ��鸺����
			value = getCharger();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����",
						"����������Ϊ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_CHARGER);
				result.add(checkItem);
				passed = false;
			}

			// 3.��������
			value = getParticipatesIdList();
			if (value == null || ((BasicBSONList) value).isEmpty()) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����",
						"û����ӹ���������", "�����ύǰȷ����", ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
			// 4.1 ��鹤����������� ������û��ָ�����̸�����
			String title = "��鹤���������";
			String process = F_WF_CHANGE;
			String editorId = Project.EDITOR_CREATE_PLAN;
			String pageId = Project.EDITOR_PAGE_WBS;
			passed = ProjectToolkit.checkProcessInternal(this, pc, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			// 4.2 �����Ŀ�ύ������ ������û��ָ�����̸�����
			title = "��鹤��ִ������";
			process = F_WF_EXECUTE;
			passed = ProjectToolkit.checkProcessInternal(this, pc, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			passed = true;
			// ��鹤��������
			List<PrimaryObject> docs = getDeliverableDocuments();
			if (docs.isEmpty()) {
				CheckListItem checkItem = new CheckListItem("��齻����",
						"�ù���û���趨������", "�ύǰ������趨�����ɹ���ִ�к���ִ����������ӣ������ύǰȷ����",
						ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("��齻����");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
		}

		return result;
	}

	protected List<Object[]> checkWorkStart(boolean warningCheck) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.��鹤���ļƻ���ʼ�ͼƻ����
		Object value = getPlanStart();
		if (value == null) {
			message.add(new Object[] { "�����ļƻ���ʼʱ��û��ȷ��", this,
					warningCheck ? SWT.ICON_WARNING : SWT.ICON_ERROR,
					EDIT_WORK_PLAN_0 });
		}
		value = getPlanFinish();
		if (value == null) {
			message.add(new Object[] { "�����ļƻ����ʱ��û��ȷ��", this,
					warningCheck ? SWT.ICON_WARNING : SWT.ICON_ERROR,
					EDIT_WORK_PLAN_0 });
		}
		// 2.��鹤���ļƻ���ʱ
		// ����Ƕ���������������������
		if (!isStandloneWork()) {
			value = getPlanWorks();
			if (value == null) {
				message.add(new Object[] { "�����ļƻ���ʱû��ȷ��", this,
						SWT.ICON_WARNING, EDIT_WORK_PLAN_0 });
			}
		}
		// 3.��鹤������
		value = getDesc();
		if (Utils.isNullOrEmptyString(value)) {
			message.add(new Object[] { "��������Ϊ��", this, SWT.ICON_ERROR,
					EDIT_WORK_PLAN_0 });
		}
		// 4.��鸺����
		value = getCharger();
		if (value == null) {
			message.add(new Object[] { "����������Ϊ��", this,
					warningCheck ? SWT.ICON_WARNING : SWT.ICON_ERROR,
					EDIT_WORK_PLAN_0 });
		}
		// 5.��������
		value = getParticipatesIdList();
		if (!(value instanceof List) || ((List<?>) value).isEmpty()) {
			message.add(new Object[] { "û����ӹ���������", this, SWT.ICON_WARNING,
					EDIT_WORK_PLAN_0 });
		}

		// // 6.1.��鹤����������� ������û��ָ�����̸�����
		// String process = F_WF_CHANGE;
		// if (!ProjectToolkit.checkProcessInternal(this, process)) {
		// throw new Exception("�ù����������û��ָ�����̸����ˣ�" + this);
		// }

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 6.2.��鹤��ִ�е����� ������û��ָ�����̸�����
		if (!ProjectToolkit.checkProcessInternal(pc, F_WF_EXECUTE)) {
			message.add(new Object[] { "�ù���ִ������û��û��ָ�����̸�����", this,
					warningCheck ? SWT.ICON_WARNING : SWT.ICON_ERROR,
					EDIT_WORK_PLAN_1 });
		}

		// 7.��鹤��������,����
		List<PrimaryObject> docs = getDeliverableDocuments();
		if (docs.isEmpty()) {
			message.add(new Object[] { "�ù���û���趨������", this, SWT.ICON_WARNING,
					EDITOR });
		}
		return message;
	}

	/**
	 * ����¼�������ɵĹ����Ƿ������ɣ��Ǽ�����ɵĹ����Ƿ��Ѿ��������״̬����ȡ��״̬
	 * 
	 * @param id
	 * @return
	 */
	protected List<Object[]> checkCascadeFinish(ObjectId id) {
		try {
			reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.�жϷǼ�����ɵĹ����Ƿ��Ѿ��������״̬����ȡ����׼���С���״̬��״̬
		DBObject condition = new BasicDBObject();
		condition.put(F_PARENT_ID, id);
		condition.put(
				F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						STATUS_PAUSED_VALUE, STATUS_WIP_VALUE }));
		condition.put(F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
				new BasicDBObject().append("$ne", Boolean.TRUE));
		long count = getRelationCountByCondition(Work.class, condition);
		if (count > 0) {
			message.add(new Object[] { "��ͣ������еķǼ�����ɵ��¼�����δ��ɻ�ȡ��", this,
					SWT.ICON_ERROR, EDITOR });
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 2.ѭ���õ��¼���������ͣ�ͽ�����״̬�Ĺ���,
		// 2.1�ж�ȡ�������Ƿ������ɣ��ж����Ƿ��������������ɹ���
		// 2.2�ж�ȡ���������¼��Ǽ�����ɵĹ����Ƿ�������
		condition.put(F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH, Boolean.TRUE);
		List<PrimaryObject> childrenWork = getRelationByCondition(Work.class,
				condition);
		if (childrenWork.size() > 0) {
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				if (pc.isWorkflowActivate(F_WF_EXECUTE)
						&& !Boolean.TRUE
								.equals(childWork
										.getValue(F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH))) {
					message.add(new Object[] { "�����޷����������е�������ɵ��¼�������ɹ���", this,
							SWT.ICON_ERROR, EDITOR });
				}
				message.addAll(checkCascadeFinish(childWork.get_id()));
			}
		}

		// 3.�жϷǼ�����ɵĹ����Ƿ�����Ǳ���ģ��Ǳ���ʱ��������ɸù���
		condition = new BasicDBObject();
		condition.put(F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
				new BasicDBObject().append("$ne", Boolean.TRUE));
		condition.put(F_PARENT_ID, id);
		condition
				.put("$or",
						new BasicDBObject[] {
								new BasicDBObject().append(F_MILESTONE,
										Boolean.TRUE),
								new BasicDBObject().append(F_MANDATORY,
										Boolean.TRUE) });
		condition.put(
				F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						STATUS_PAUSED_VALUE, STATUS_WIP_VALUE,
						STATUS_NONE_VALUE, null, STATUS_ONREADY_VALUE }));

		count = getRelationCountByCondition(Work.class, condition);
		if (count > 0) {
			message.add(new Object[] { "�Ǽ�������������˱���(��̱�)���¼�����δ���", this,
					SWT.ICON_ERROR, EDITOR });
		}

		return message;
	}

	/**
	 * ��Ŀ�Ĺ�������Ҫ��Ϣ�Ƿ�¼��
	 * 
	 * 2013-10-31 �޸� zhonghua
	 * 
	 * ��������¼�����ʱ��ֻ������ͬ�������Ĺ���
	 * 
	 * @return
	 */
	protected List<Object[]> checkCascadeStart(boolean warningCheck) {
		try {
			reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Object[]> message = new ArrayList<Object[]>();
		List<PrimaryObject> childrenWork = getChildrenWork();
		if (childrenWork.size() > 0) {// ������¼��������¼��ļ����
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				// ͨ��warningCheck�������¼��ļ���׼
				if (Boolean.TRUE.equals(childWork
						.getValue(F_SETTING_AUTOSTART_WHEN_PARENT_START))) {
					message.addAll(childWork.checkCascadeStart(warningCheck));
				}
			}
		}

		// �Ǽ����������������
		if (!isProjectWBSRoot()) {
			message.addAll(checkWorkStart(warningCheck));
		}
		return message;
	}

	/**
	 * �Ƿ���Ȩ�޽���ָ�ɣ��ù������ϼ�������ָ����ʱ��Ȩ��
	 * 
	 * @param context
	 * @return
	 */
	private boolean hasPermissionForReassignment(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		// �ж��Ƿ��Ǳ�����ָ����
		if (userId.equals(getAssignerId())) {
			return true;
		} else {
			Work parent = (Work) getParent();
			if (parent != null) {
				return parent.hasPermissionForReassignment(context);
			} else {
				return false;
			}
		}
	}

	/**
	 * �Ƿ���Ȩ�޴����ӹ���������������༭��ǰ���� �ù������ϼ������ĸ����˻���Ŀ����Ŀ����ʱ��Ȩ��
	 * 
	 * @param context
	 * @return
	 */
	public boolean hasPermission(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		// �ж��Ƿ��Ǳ����ĸ�����
		if (userId.equals(getChargerId())) {
			return true;
		} else {
			Work parent = (Work) getParent();
			if (parent != null && !parent.isEmpty()) {
				return parent.hasPermission(context);
			} else {
				// ��Root�������ж��Ƿ�����Ŀ����
				Project project = getProject();
				if (project != null) {
					return userId.equals(project.getChargerId());
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * ���������ϼ�����
	 * 
	 * @return List
	 */
	public List<Work> getAllParents() {
		List<Work> result = new ArrayList<Work>();
		result.add(this);
		Work parent = (Work) getParent();
		while (parent != null && !parent.isEmpty()) {
			result.add(parent);
			parent = (Work) parent.getParent();
		}
		return result;
	}

	/**
	 * ��������ִ����
	 * 
	 * @param wfRoleAss
	 * @param roleAssign
	 * @return BasicDBObject
	 */
	private BasicDBObject getWorkFlowActors(DBObject wfRoleAss,
			Map<ObjectId, List<PrimaryObject>> roleAssign) {
		AbstractRoleAssignment assItem;
		List<PrimaryObject> assignments;
		String userid;
		BasicDBObject wfRoleActors = new BasicDBObject();

		Iterator<String> iter = wfRoleAss.keySet().iterator();
		while (iter.hasNext()) {
			String actionName = iter.next();
			ObjectId actorRoleId = (ObjectId) wfRoleAss.get(actionName);
			if (actorRoleId != null) {
				assignments = roleAssign.get(actorRoleId);
				/**
				 * 
				 * ֻ����ý�ɫֻ��һ����Ա�����
				 * 
				 */
				if (assignments != null && assignments.size() == 1) {
					// String[] actorList = new String[assignments.size()];
					// for (int j = 0; j < assignments.size(); j++) {
					// assItem = (AbstractRoleAssignment) assignments.get(j);
					// userid = assItem.getUserid();
					// actorList[j] = userid;
					// }
					// wfRoleActors.put(actionName, actorList);

					// ֻ����ָ��һ����
					assItem = (AbstractRoleAssignment) assignments.get(0);
					userid = assItem.getUserid();
					wfRoleActors.put(actionName, userid);
				}
			}
		}
		return wfRoleActors;
	}

	/**
	 * ���ع����ĸ����˽�ɫ
	 * 
	 * @return ProjectRole
	 */
	public ProjectRole getChargerRoleDefinition() {
		return getChargerRoleDefinition(ProjectRole.class);
	}

	/**
	 * ��ȡ������Ӧ�Ľ�����
	 * 
	 * @return
	 */
	public List<PrimaryObject> getDeliverable() {
		return getRelationById(F__ID, Deliverable.F_WORK_ID, Deliverable.class);
	}

	/**
	 * ��ȡ������Ӧ�Ľ������ĵ�
	 * 
	 * @return
	 */
	public List<PrimaryObject> getDeliverableDocuments() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> d = getDeliverable();
		for (int i = 0; i < d.size(); i++) {
			Deliverable ditem = (Deliverable) d.get(i);
			Document doc = ditem.getDocument();
			if (doc != null) {
				result.add(doc);
			}
		}
		return result;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public User getCharger() {
		String chargerId = getChargerId();
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return UserToolkit.getUserById(chargerId);
	}

	/**
	 * ��ȡ������ID
	 * 
	 * @return
	 */
	public String getChargerId() {
		return (String) getValue(F_CHARGER);
	}

	/**
	 * ��ȡָ����ID
	 * 
	 * @return
	 */
	public String getAssignerId() {
		return (String) getValue(F_ASSIGNER);
	}

	/**
	 * ��ȡ�����е���
	 * 
	 * @return
	 */
	public BasicBSONList getParticipatesIdList() {
		return (BasicBSONList) getValue(F_PARTICIPATE);
	}

	@Override
	public String getLifecycleStatus() {
		String lc = (String) getValue(F_LIFECYCLE);
		if (lc == null) {
			return STATUS_NONE_VALUE;
		} else {
			return lc;
		}
	}

	@Override
	public String getLifecycleStatusText() {
		String lc = getLifecycleStatus();
		return LifecycleToolkit.getLifecycleStatusText(lc);
	}

	/**
	 * ���͵������ĸ����ˡ������ߡ�ָ���ߣ���Ϣ��Ҫ������������<br/>
	 * ������Ϣ�����Ǻϲ���Ϣ��������ͬһ�˵�ͬһ���͵���ϢӦ������Ϊһ��<br/>
	 * 
	 * @param messageList
	 *            , �������������Ϣ�б�������ͬ�û��Ŀ��Ժϲ�
	 * @param context
	 * @return
	 */
	public Map<String, Message> getCommitMessage(
			Map<String, Message> messageList, String title, IContext context) {
		// 1. ȡ����������
		appendMessageForCharger(messageList, title, context);

		// 2. ȡ����ָ����
		appendMessageForAssigner(messageList, title, context);

		// 3. ��ȡ������
		appendMessageForParticipate(messageList, title, context);

		// 4. ��ȡ���̵�ִ����
		appendMessageForChangeWorkflowActor(messageList, title, context);

		appendMessageForExecuteWorkflowActor(messageList, title, context);

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childwork = (Work) children.get(i);
			childwork.getCommitMessage(messageList, title, context);
		}
		return messageList;
	}

	public void appendMessageForCharger(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "������"
				+ ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForAssigner(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title,
				"Ϊ����ָ�ɸ����˺Ͳ����ߣ�����" + ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForParticipate(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "���빤��"
				+ ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForExecuteWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_EXECUTE, "ִ������", title, context.getAccountInfo()
						.getConsignerId(), null);
	}

	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_CHANGE, "�������", title, context.getAccountInfo()
						.getConsignerId(), null);
	}

	/**
	 * �󶨹���������
	 * 
	 * @param workflowKey
	 *            ,�������ؼ��֣�ִ�����̻����Ǳ������ {@link #F_WF_EXECUTE} {@link #F_WF_CHANGE}
	 * @param workflowDefinition
	 */
	public void bindingWorkflowDefinition(String workflowKey,
			DBObject workflowDefinition) {
		setValue(workflowKey, workflowDefinition.get("KEY"));
		setValue(workflowKey + IProcessControl.POSTFIX_ACTIVATED,
				workflowDefinition.get(IProcessControl.POSTFIX_ACTIVATED));
		setValue(workflowKey + IProcessControl.POSTFIX_ACTORS,
				workflowDefinition.get(IProcessControl.POSTFIX_ACTORS));
		setValue(workflowKey + IProcessControl.POSTFIX_ASSIGNMENT,
				workflowDefinition.get(IProcessControl.POSTFIX_ASSIGNMENT));
	}

	/**
	 * ���湤��
	 * 
	 * @return boolean
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {

		/**
		 * BUG:10006
		 */
		String lc = getLifecycleStatus();
		if (lc.equals(STATUS_NONE_VALUE)) {
			setValue(F_LIFECYCLE, STATUS_ONREADY_VALUE);
		}

		checkAndCalculateDuration(F_PLAN_START, F_PLAN_FINISH, F_PLAN_DURATION);

		checkAndCalculateDuration(F_ACTUAL_START, F_ACTUAL_FINISH,
				F_ACTUAL_DURATION);

		checkProjectTimeline();

		super.doSave(context);

		resetCaculateCache();

		// ����ƻ���ʱ����
		doCaculateWorksAllocated(context);

		// ���¼����ϼ������Ĺ�ʱ
		Work parent = (Work) getParent();
		if (parent != null) {
			parent.doReCaculateParentWork(false);
		}

		return true;

	}

	@Override
	public void doRemove(IContext context) throws Exception {
		if (!canDelete(context)) {
			return;
		}
		Work parent = (Work) getParent();
		doDelectIterator(context);
		// ����ƻ���ʱ����
		doCaculateWorksAllocated(context);
		if (parent != null) {
			parent.doReCaculateParentWork(false);
		}
	}

	private void doDelectIterator(IContext context) throws Exception {
		if (hasChildrenWork()) {
			List<PrimaryObject> childrenWorks = getChildrenWork();
			for (PrimaryObject po : childrenWorks) {
				Work childrenWork = (Work) po;
				childrenWork.doDelectIterator(context);
			}
		}
		DBCollection col = getCollection();
		WriteResult ws = col.remove(
				new BasicDBObject().append(F__ID, get_id()),
				WriteConcern.NORMAL);
		checkWriteResult(ws);
		fireEvent(IPrimaryObjectEventListener.REMOVE);

		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "ɾ��", new Date(),
				getLabel() + "\n" + getDbName() + "\\" + getCollectionName()
						+ "\\" + get_id(), getDbName());
	}

	private void doReCaculateParentWork(boolean useJob) {
		if (useJob) {
			Job job = new Job("���¼��㹤��") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					Work.this.caculate();
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

			};

			job.schedule();
		} else {
			this.caculate();
		}

	}

	protected void caculate() {
		DBCollection col = getCollection();
		// ���㿪ʼʱ������ʱ��
		Object planStart = getChildrenValue(F_PLAN_START, 1, col);
		setValue(F_PLAN_START, planStart);

		Object planFinish = getChildrenValue(F_PLAN_FINISH, -1, col);
		setValue(F_PLAN_FINISH, planFinish);

		// Object actualStart = getChildrenValue(F_ACTUAL_START, 1, col);
		// setValue(F_ACTUAL_START, actualStart);
		//
		// Object actualFinish = getChildrenValue(F_ACTUAL_FINISH, -1, col);
		// setValue(F_ACTUAL_FINISH, actualFinish);

		// ����ƻ���ʱ��ʵ�ʹ�ʱ
		DBObject result = getChildrenGroupValue(F_PLAN_WORKS, F_ACTUAL_WORKS,
				"$sum", col);
		Object planWorks = result == null ? null : result.get("result"
				+ F_PLAN_WORKS);
		if (planWorks instanceof Number) {
			planWorks = new Double(((Number) planWorks).doubleValue());
		}
		setValue(F_PLAN_WORKS, planWorks);

		Object actualWorks = result == null ? null : result.get("result"
				+ F_ACTUAL_WORKS);
		if (actualWorks instanceof Number) {
			actualWorks = new Double(((Number) actualWorks).doubleValue());
		}
		setValue(F_ACTUAL_WORKS, actualWorks);

		// ����ƻ����ں�ʵ�ʹ���
		result = getChildrenGroupValue(F_PLAN_DURATION, F_ACTUAL_DURATION,
				"$max", col);
		Object planDuration = result == null ? null : result.get("result"
				+ F_PLAN_DURATION);
		setValue(F_PLAN_DURATION, planDuration);

		Object actualDuration = result == null ? null : result.get("result"
				+ F_ACTUAL_DURATION);
		setValue(F_ACTUAL_DURATION, actualDuration);

		DBObject val = new BasicDBObject();
		val.put(F_PLAN_START, planStart);
		val.put(F_PLAN_FINISH, planFinish);
		// val.put(F_ACTUAL_START, actualStart);
		// val.put(F_ACTUAL_FINISH, actualFinish);
		val.put(F_PLAN_WORKS, planWorks);
		val.put(F_ACTUAL_WORKS, actualWorks);
		val.put(F_PLAN_DURATION, planDuration);
		val.put(F_ACTUAL_DURATION, actualDuration);
		col.update(new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", val));

		Work parent = (Work) getParent();
		if (parent != null) {
			parent.caculate();
		}
	}

	private DBObject getChildrenGroupValue(String field1, String field2,
			String op, DBCollection col) {
		DBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject()
						.append(F__ID, "$" + F_PARENT_ID)
						.append("result" + field1,
								new BasicDBObject().append(op, "$" + field1))
						.append("result" + field2,
								new BasicDBObject().append(op, "$" + field2)));

		DBObject match = new BasicDBObject();
		match.put("$match", new BasicDBObject().append(F__ID, get_id()));

		AggregationOutput agg = col.aggregate(group, match);
		Iterator<DBObject> iter = agg.results().iterator();
		if (iter.hasNext()) {
			return iter.next();
		}
		return null;
	}

	private Object getChildrenValue(String field, int i, DBCollection col) {
		DBCursor cursor = col.find(
				new BasicDBObject().append(F_PARENT_ID, get_id()).append(field,
						new BasicDBObject().append("$ne", null)),
				new BasicDBObject().append(field, 1)).sort(
				new BasicDBObject().append(field, i));
		if (cursor.hasNext()) {
			return cursor.next().get(field);
		}
		return null;
	}

	private void resetCaculateCache() {
		overCount = null;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		// ͬ�������ˡ����̻ִ���˵������Ĳ����ߡ�
		ensureParticipatesConsistency();
		super.doUpdate(context);
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		ObjectId id = get_id();
		if (id == null) {
			id = new ObjectId();
			setValue(F__ID, id);

			if (getValue(F_PARENT_ID) == null) {// ������
				setValue(F_ROOT_ID, id);
			} else {
				AbstractWork parent = getParent();
				ObjectId rootId = (ObjectId) parent.getValue(F_ROOT_ID);
				setValue(F_ROOT_ID, rootId);
			}
		}
		if (isStandloneWork()) {
			copyWorkDefinition(Work.F_WF_EXECUTE, context);

			// �����ĵ��ĸ���
			copyDeliveryFromWorkDefinition(context);
		}

		// ͬ�������ˡ����̻ִ���˵������Ĳ����ߡ�
		ensureParticipatesConsistency();

		// ȱʡ������ӽ�����
		Object value = getValue(F_SETTING_CAN_ADD_DELIVERABLES);
		if (value == null) {
			setValue(F_SETTING_CAN_ADD_DELIVERABLES, Boolean.TRUE);
		}

		super.doInsert(context);
	}

	private void copyDeliveryFromWorkDefinition(IContext context)
			throws Exception {
		WorkDefinition workdef = getWorkDefinition();
		if (workdef == null) {
			return;
		}

		// �����ĵ�
		Map<ObjectId, DBObject> documentsToInsert = new HashMap<ObjectId, DBObject>();
		List<DBObject> deliverableToInsert = new ArrayList<DBObject>();
		List<DBObject[]> fileToCopy = new ArrayList<DBObject[]>();

		DBCollection deliveryDefCol = getCollection(IModelConstants.C_DELIEVERABLE_DEFINITION);
		DBCursor deliCur = deliveryDefCol.find(new BasicDBObject().append(
				DeliverableDefinition.F_WORK_DEFINITION_ID,
				workdef.getValue(WorkDefinition.F__ID)));
		while (deliCur.hasNext()) {
			DBObject delidata = deliCur.next();
			// ����ģ��Ľ����ﶨ�崴���������ϵ
			DBObject deliverableData = new BasicDBObject();

			// ���ù���Id
			deliverableData.put(Deliverable.F_WORK_ID, get_id());

			// ����ĵ�ģ��
			ObjectId documentTemplateId = (ObjectId) delidata
					.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
			DBObject documentData = copyDocumentFromTemplate(documentsToInsert,
					fileToCopy, documentTemplateId);
			documentsToInsert.put(documentTemplateId, documentData);
			ObjectId documentId = (ObjectId) documentData.get(Document.F__ID);
			deliverableData.put(Deliverable.F_DOCUMENT_ID, documentId);
			deliverableToInsert.add(deliverableData);
		}

		// �����ĵ�
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		Collection<DBObject> collection = documentsToInsert.values();
		WriteResult ws;
		if (!collection.isEmpty()) {
			DBObject[] docList = new DBObject[collection.size()];
			int i = 0;
			for (DBObject documentObject : collection) {
				Document document = ModelService.createModelObject(
						documentObject, Document.class);
				document.initVerStatus();
				document.initVersionNumber();
				document.initInsertDefault(document.get_data(), context);
				docList[i++] = document.get_data();
			}
			ws = docCol.insert(docList, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// ���潻����
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		if (!deliverableToInsert.isEmpty()) {
			ws = deliCol.insert(deliverableToInsert, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// �����ļ�
		for (DBObject[] dbObjects : fileToCopy) {
			DBObject src = dbObjects[0];
			DBObject tgt = dbObjects[1];

			String srcDB = (String) src.get(RemoteFile.F_DB);
			String srcFilename = (String) src.get(RemoteFile.F_FILENAME);
			String srcNamespace = (String) src.get(RemoteFile.F_NAMESPACE);
			ObjectId srcID = (ObjectId) src.get(RemoteFile.F_ID);

			String tgtDB = (String) tgt.get(RemoteFile.F_DB);
			String tgtFilename = (String) tgt.get(RemoteFile.F_FILENAME);
			String tgtNamespace = (String) tgt.get(RemoteFile.F_NAMESPACE);
			ObjectId tgtID = (ObjectId) tgt.get(RemoteFile.F_ID);

			FileUtil.copyGridFSFile(srcID, srcDB, srcFilename, srcNamespace,
					tgtID, tgtDB, tgtFilename, tgtNamespace);
		}

	}

	/**
	 * �����ڶ����������ӹ��������и��ƽ�ɫ
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private Map<ObjectId, DBObject> copyRoleDefinition(IContext context)
			throws Exception {
		// ׼������ֵ
		HashMap<ObjectId, DBObject> result = new HashMap<ObjectId, DBObject>();

		WorkDefinition workd = getWorkDefinition();
		if (workd == null) {
			return result;
		}
		ObjectId workDefinitionId = workd.get_id();

		DBCollection col_roled = getCollection(IModelConstants.C_ROLE_DEFINITION);

		// ����ģ��Ľ�ɫ����
		DBCursor cur = col_roled.find(new BasicDBObject().append(
				RoleDefinition.F_WORKDEFINITION_ID, workDefinitionId),
				new BasicDBObject().append(
						RoleDefinition.F_ORGANIZATION_ROLE_ID, 1));
		while (cur.hasNext()) {
			DBObject roleddata = cur.next();
			ObjectId oldId = (ObjectId) roleddata.get(F__ID);
			BasicDBObject newRoleData = new BasicDBObject()
					.append(RoleDefinition.F_WORK_ID, get_id())
					.append(RoleDefinition.F_ORGANIZATION_ROLE_ID,
							roleddata
									.get(RoleDefinition.F_ORGANIZATION_ROLE_ID))
					.append(RoleDefinition.F__ID, new ObjectId());
			result.put(oldId, newRoleData);
		}

		if (!result.isEmpty()) {
			DBObject[] insertData = result.values().toArray(new DBObject[0]);

			// ���뵽���ݿ�
			WriteResult ws = col_roled.insert(insertData, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		return result;
	}

	public WorkDefinition getWorkDefinition() {
		Object value = getValue(F_WORK_DEFINITION_ID);
		if (value instanceof ObjectId) {
			return ModelService.createModelObject(WorkDefinition.class,
					(ObjectId) value);
		}
		return null;
	}

	/**
	 * ȷ�������Ĳ����߰��������ĸ����ˡ�����ִ����
	 */
	public void ensureParticipatesConsistency() {
		// ��ȡ�����ĸ�����
		String chargerId = getChargerId();
		if (chargerId != null) {
			addParticipate(chargerId);
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// ������̵�ִ����
		if (pc.isWorkflowActivate(F_WF_EXECUTE)) {
			DBObject processActorsMap = pc.getProcessActorsData(F_WF_EXECUTE);
			if (processActorsMap != null) {
				Iterator<String> iter = processActorsMap.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					String userId = (String) processActorsMap.get(key);
					if (userId != null) {
						addParticipate(userId);
					}
				}
			}
		}

	}

	public void addParticipate(String chargerId) {
		Assert.isTrue(chargerId != null, "��������Ϊ��");
		BasicBSONList participatesIdList = getParticipatesIdList();
		if (participatesIdList == null) {
			participatesIdList = new BasicDBList();
			setValue(F_PARTICIPATE, participatesIdList);
		}
		if (!participatesIdList.contains(chargerId)) {
			participatesIdList.add(chargerId);
		}
	}

	/**
	 * Ϊ�������¼������ĸ�����,������,��������ִ����ָ���û�
	 * 
	 * @param roleAssign
	 * @param context
	 * @throws Exception
	 */
	public void doAssignment(Map<ObjectId, List<PrimaryObject>> roleAssign,
			IContext context) throws Exception {
		AbstractRoleAssignment assItem;
		List<PrimaryObject> assignments;
		String userid;
		boolean modified = false;

		// ���ø�����
		ObjectId roleId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if (roleId != null) {
			assignments = roleAssign.get(roleId);
			if (assignments != null && !assignments.isEmpty()) {
				assItem = (AbstractRoleAssignment) assignments.get(0);
				userid = assItem.getUserid();
				setValue(F_CHARGER, userid);
				modified = true;
			}
		}

		// ����ָ����
		roleId = (ObjectId) getValue(F_ASSIGNMENT_CHARGER_ROLE_ID);
		if (roleId != null) {
			assignments = roleAssign.get(roleId);
			if (assignments != null && !assignments.isEmpty()) {
				assItem = (AbstractRoleAssignment) assignments.get(0);
				userid = assItem.getUserid();
				setValue(F_ASSIGNER, userid);
				modified = true;
			}
		}

		// ���ò�����
		BasicBSONList roleIds = (BasicBSONList) getValue(F_PARTICIPATE_ROLE_SET);
		if (roleIds != null && roleIds.size() > 0) {
			BasicBSONList participates = new BasicDBList();
			for (int i = 0; i < roleIds.size(); i++) {
				DBObject object = (DBObject) roleIds.get(i);
				assignments = roleAssign.get(object.get(F__ID));
				if (assignments != null && !assignments.isEmpty()) {
					for (int j = 0; j < assignments.size(); j++) {
						assItem = (AbstractRoleAssignment) assignments.get(j);
						userid = assItem.getUserid();
						participates.add(userid);
					}
				}
			}
			if (participates.size() > 0) {
				setValue(F_PARTICIPATE, participates);
				modified = true;
			}
		}

		// ���ñ��������ִ����

		DBObject wfRoleAss = (DBObject) getValue(F_WF_CHANGE_ASSIGNMENT);
		if (wfRoleAss != null) {
			BasicDBObject wfRoleActors = getWorkFlowActors(wfRoleAss,
					roleAssign);
			if (!wfRoleActors.isEmpty()) {
				setValue(F_WF_CHANGE_ACTORS, wfRoleActors);
				modified = true;
			}
		}

		// ����ִ�й�������ִ����
		wfRoleAss = (DBObject) getValue(F_WF_EXECUTE_ASSIGNMENT);
		if (wfRoleAss != null) {
			BasicDBObject wfRoleActors = getWorkFlowActors(wfRoleAss,
					roleAssign);
			if (!wfRoleActors.isEmpty()) {
				setValue(F_WF_EXECUTE_ACTORS, wfRoleActors);
				modified = true;
			}
		}
		if (modified) {
			doSave(context);
		}

		// �����¼�
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work child = (Work) children.get(i);
			child.doAssignment(roleAssign, context);
		}
	}

	/**
	 * �½�������
	 * 
	 * @param doc
	 *            ,�ĵ�
	 * @param context
	 * @return Deliverable
	 * @throws Exception
	 */
	public Deliverable doAddDeliverable(Document doc, String type,
			IContext context) throws Exception {
		Deliverable deli = makeDeliverableDefinition(type);
		deli.setValue(Deliverable.F_DOCUMENT_ID, doc.get_id());
		deli.doInsert(context);
		return deli;

	}

	/**
	 * ��������
	 */
	@SuppressWarnings("unchecked")
	public Object doStart(IContext context) throws Exception {
		// ׼������Ķ���
		DBObject update = new BasicDBObject();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!isProjectWBSRoot()) {
			// �ж��ܷ����������״̬
			Assert.isTrue(canStart(), "�����ĵ�ǰ״̬����ִ����������");
			// ����ǰ����
			doStartBefore(context, params);

			// �ж��Ƿ�ʹ��ִ�й�����
			IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
			if (pc.isWorkflowActivate(F_WF_EXECUTE)) {
				// ����ǣ�����������
				String lc = getLifecycleStatus();
				if (!STATUS_PAUSED_VALUE.equals(lc)) {

					Workflow wf = pc.getWorkflow(F_WF_EXECUTE);
					DBObject actors = pc.getProcessActorsData(F_WF_EXECUTE);
					Map<String, String> actorParameter = null;
					if (actors != null) {
						actorParameter = actors.toMap();
					}

					// ������̽�ɫ�Ƿ��Ѿ�ָ�ɵ���
					checkProcessActorParameter(pc, actorParameter);

					ProcessInstance processInstance = wf.startHumanProcess(
							actorParameter, params);
					Assert.isNotNull(processInstance, "��������ʧ��:" + this);

					update.put(F_WF_EXECUTE
							+ IProcessControl.POSTFIX_INSTANCEID,
							processInstance.getId());
				}
			}
		}

		// �����¼�ͬ�������Ĺ���
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// ����¼��Ĺ����Ƿ����������ϼ�ͬ������
			if (Boolean.TRUE.equals(childWork
					.getValue(F_SETTING_AUTOSTART_WHEN_PARENT_START))) {
				// �����¼�����
				childWork.doStart(context);
			}
		}

		// ��ǹ����Ľ�����
		update.put(F_LIFECYCLE, STATUS_WIP_VALUE);
		// ���ù�����ʵ�ʿ�ʼʱ��
		update.put(F_ACTUAL_START, new Date());

		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		// ��ʾ��������
		doNoticeWorkAction(context, "������");

		// ���ú���
		doStartAfter(context, params);

		return null;
	}

	private void checkProcessActorParameter(IProcessControl pc,
			Map<String, String> actorParameter) throws Exception {
		DroolsProcessDefinition procd = pc.getProcessDefinition(F_WF_EXECUTE);
		Iterator<NodeAssignment> iter = procd.getNodesAssignment().iterator();
		while (iter.hasNext()) {
			NodeAssignment nas = iter.next();

			if (!nas.isRuleAssignment() && nas.forceAssignment()) {

				String ass = actorParameter.get(nas.getNodeActorParameter());
				if (Utils.isNullOrEmpty(ass)) {
					throw new Exception("����ȱ�ٱ�Ҫ����Աָ��" + this + ":"
							+ procd.getProcessName());
				}
			}
		}
	}

	/**
	 * ��ͣ����
	 */
	public Object doPause(IContext context) throws Exception {
		Assert.isTrue(canPause(), "�����ĵ�ǰ״̬����ִ����ͣ����");
		Map<String, Object> params = new HashMap<String, Object>();
		doPauseBefore(context, params);

		// ��ͣ����

		DBObject update = new BasicDBObject();

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// ����¼��Ĺ���״̬�Ƿ�Ϊ������
			if (STATUS_WIP_VALUE.equals(childWork.getValue(F_LIFECYCLE))) {
				// ��ͣ�¼�����
				childWork.doPause(context);
			}
		}

		// ��ǹ�������ͣ
		update.put(F_LIFECYCLE, STATUS_PAUSED_VALUE);
		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);

		set_data(newData);

		// ��ʾ��������ͣ
		doNoticeWorkAction(context, "����ͣ");

		// ����
		doPauseAfter(context, params);

		return null;

	}

	/**
	 * ȡ������
	 */
	public Object doCancel(IContext context) throws Exception {
		Assert.isTrue(canCancel(), "�����ĵ�ǰ״̬����ִ��ȡ������");
		Map<String, Object> params = new HashMap<String, Object>();
		doCancelBefore(context, params);

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// // ����¼��Ĺ���״̬�Ƿ�Ϊ�����л�������ͣ
			// if (STATUS_WIP_VALUE.equals(childWork.getValue(F_LIFECYCLE))
			// || STATUS_PAUSED_VALUE.equals(childWork
			// .getValue(F_LIFECYCLE))) {
			// }
			// ȡ���¼�����
			childWork.checkCancelAction(context);
			childWork.doCancel(context);
		}

		cancelExecuteProcessInstance(context);

		DBObject update = new BasicDBObject();
		// ��ǹ�����ȡ��
		update.put(F_LIFECYCLE, STATUS_CANCELED_VALUE);

		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		doSaveProcessHistoryToDocument(context);

		// ��ʾ������ȡ��
		doNoticeWorkAction(context, "��ȡ��");
		doCancelAfter(context, params);

		return null;

	}

	private void doSaveProcessHistoryToDocument(final IContext context) {
		// �����������̼�¼�洢���������ĵ���
		Job job = new Job("����������ʷ") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (isExecuteWorkflowActivateAndAvailable()) {
					IProcessControl ip = Work.this
							.getAdapter(IProcessControl.class);
					BasicBSONList historys = ip.getWorkflowHistroyData();

					if (historys != null && historys.size() > 0) {
						DBObject wfHistory = new BasicDBObject();
						wfHistory.put(F_DESC, getDesc());
						wfHistory.put(F__CDATE, new Date());
						wfHistory.put(IDocumentProcess.F_HISTORY, historys);
						wfHistory.put(IDocumentProcess.F_WORK_ID, get_id());
						wfHistory.put(IDocumentProcess.F_PROCESS_INSTANCEID,
								getExecuteProcessId());
						DroolsProcessDefinition pd = ip
								.getProcessDefinition(F_WF_EXECUTE);
						wfHistory.put(IDocumentProcess.F_PROCESSID,
								pd.getProcessId());
						wfHistory.put(IDocumentProcess.F_PROCESSNAME,
								pd.getProcessName());

						try {
							doWFHistoryToDocument(wfHistory, context);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return org.eclipse.core.runtime.Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	private void cancelExecuteProcessInstance(IContext context)
			throws Exception {
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		if (pc.isWorkflowActivate(F_WF_EXECUTE)) {

			Workflow wf = pc.getWorkflow(F_WF_EXECUTE);
			List<UserTask> reservedTasks = getAllUserTasks(Status.Reserved
					.name());
			for (int i = 0; i < reservedTasks.size(); i++) {
				UserTask userTask = reservedTasks.get(i);
				TaskData taskData = userTask.getTask().getTaskData();
				long workItemId = taskData.getWorkItemId();
				try {
					wf.abortWorkItem(workItemId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// userTask.setValue(UserTask.F_STATUS, Status.Exited.name());
				// userTask.doSave(context);
			}
			try {
				Long instanceId = getExecuteProcessId();
				wf.abortProcess(instanceId.longValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Long getExecuteProcessId() {
		return getLongValue(F_WF_EXECUTE + IProcessControl.POSTFIX_INSTANCEID)
				.longValue();
	}

	public ProcessInstance getExecuteProcess() {
		IProcessControl pc = getAdapter(IProcessControl.class);
		if (pc.isWorkflowActivate(F_WF_EXECUTE)) {
			Long pid = getExecuteProcessId();
			if (pid != null) {
				Workflow wf = pc.getWorkflow(F_WF_EXECUTE);
				return wf.getProcess(pid);
			}
		}

		return null;
	}

	public Object doFinish(IContext context) throws Exception {
		Assert.isTrue(canFinish(), "�����ĵ�ǰ״̬����ִ����ɲ���");
		Map<String, Object> params = new HashMap<String, Object>();
		doFinishBefore(context, params);

		/*
		 * DBCollection col = getCollection(); DBObject query=new
		 * BasicDBObject().append(Work.F__ID,get_id()); DBObject update =
		 * col.findOne(query); update.put(F_LIFECYCLE,
		 * Work.STATUS_FINIHED_VALUE); update.put(F_ACTUAL_FINISH, new Date());
		 * DBObject sort=new BasicDBObject().append(F__ID, -1);
		 * 
		 * col.findAndModify(query, null, sort, false, update, false, false);
		 * 
		 * //��ѯ�¼� BasicDBObject queryCondition = new BasicDBObject();
		 * //���ò�ѯ�������ù����������¼����� queryCondition.put(Work.F_PARENT_ID,get_id());
		 * //���ò�ѯ�������ù����������ڽ����к�����ͣ���¼����� queryCondition.put(Work.F_LIFECYCLE,new
		 * BasicDBObject().append("$in", new String[] { Work.STATUS_WIP_VALUE,
		 * Work.STATUS_PAUSED_VALUE})); //��ѯ�����ظù����������ڽ����е��¼����� DBCursor cur =
		 * col.find(queryCondition); while(cur.hasNext()){ DBObject dbobject =
		 * cur.next(); Work work = ModelService.createModelObject(dbobject,
		 * Work.class); work.doFinish(context);
		 * 
		 * }
		 */

		// ��������Ƿ����
		// if (Boolean.TRUE
		// .equals(getValue(F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH))) {
		// ProcessInstance pi = getExecuteProcess();
		// if (pi != null) {
		// if (pi.getState() != ProcessInstance.STATE_COMPLETED
		// || pi.getState() != ProcessInstance.STATE_ABORTED) {
		// IProcessControl pc = (IProcessControl)
		// getAdapter(IProcessControl.class);
		// if (pc.isWorkflowActivate(F_WF_EXECUTE)) {
		// Workflow wf = pc.getWorkflow(F_WF_EXECUTE);
		// Long instanceId = getExecuteProcessId();
		// wf.abortProcess(instanceId.longValue());
		// }
		// }
		// }
		// }

		DBObject update = new BasicDBObject();
		System.out.println("ok");
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// ����¼��Ĺ���״̬�Ƿ�Ϊ�����л�����ͣ
			String childLC = childWork.getLifecycleStatus();
			if (STATUS_WIP_VALUE.equals(childLC)
					|| STATUS_PAUSED_VALUE.equals(childWork
							.getValue(F_LIFECYCLE))) {
				// ����¼�����
				childWork.doFinish(context);
			} else if (STATUS_CANCELED_VALUE.equals(childLC)
					|| STATUS_FINIHED_VALUE.equals(childLC)) {
			} else {
				// ȡ������
				childWork.doCancel(context);

			}
		}

		// ��ǹ��������
		update.put(F_LIFECYCLE, STATUS_FINIHED_VALUE);
		// ���ù�����ʵ�����ʱ��
		update.put(F_ACTUAL_FINISH, new Date());

		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		// ��ʾ���������
		doNoticeWorkAction(context, "�����");
		doFinishAfter(context, params);

		doCalculatePerformence(context);
		return null;

	}

	/**
	 * �����������̼�¼�洢�������������ӹ����Ľ������ĵ���
	 * 
	 * @param context
	 * @param wfHistory
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doWFHistoryToDocument(DBObject wfHistory, IContext context)
			throws Exception {
		/**
		 * ���ǵ�������ʷ�ķ�ʽ����Ϊÿ���˹�������ɶ����б��档��д����Ĵ��롣
		 */

		/*
		 * List<PrimaryObject> documentList = getOutputDeliverableDocuments();
		 * DBCollection col = getCollection(IModelConstants.C_DOCUMENT); for
		 * (PrimaryObject po : documentList) { Document document = (Document)
		 * po; WriteResult ws = col.update(new BasicDBObject().append(
		 * Document.F__ID, document.get_id()), new BasicDBObject()
		 * .append("$push", new BasicDBObject().append( Document.F_WF_HISTORY,
		 * wfHistory))); checkWriteResult(ws); }
		 */
		List<PrimaryObject> documentList = getOutputDeliverableDocuments();
		DBCollection col = getCollection(IModelConstants.C_DOCUMENT);
		for (PrimaryObject po : documentList) {
			Document document = (Document) po;
			wfHistory.put(IDocumentProcess.F_MAJOR_VID,
					document.getValue(Document.F_MAJOR_VID));
			wfHistory.put(IDocumentProcess.F_SECOND_VID,
					document.getValue(Document.F_SECOND_VID));
			// �����Ѿ����������ʷ
			Object historyList = document.getValue(Document.F_WF_HISTORY);
			if (historyList instanceof List<?>) {
				for (int i = 0; i < ((List<?>) historyList).size(); i++) {
					DBObject historyProcessRec = (DBObject) ((List<?>) historyList)
							.get(i);
					// ȡ������ʵ����ID
					Object pid = historyProcessRec
							.get(IDocumentProcess.F_PROCESS_INSTANCEID);
					if (pid.equals(wfHistory
							.get(IDocumentProcess.F_PROCESS_INSTANCEID))) {
						((List<?>) historyList).remove(i);
						break;
					}
				}
			} else {
				historyList = new ArrayList<DBObject>();
			}
			((List) historyList).add(wfHistory);

			WriteResult ws = col.update(new BasicDBObject().append(
					Document.F__ID, document.get_id()), new BasicDBObject()
					.append("$set", new BasicDBObject().append(
							Document.F_WF_HISTORY, historyList)));
			checkWriteResult(ws);
		}

		/**
		 * zhonghua: ��Ϊ���µĴ���Ƿ��.��ʱע��
		 * 
		 * isExecuteWorkflowActivateAndAvailable ��ָ�������̣������̼���Ĺ�����
		 * ���Ƿ񱣴汾�����������ʷ�Ƿ��й�ϵ��
		 */
		List<PrimaryObject> childrenWorkList = getChildrenWork();
		for (PrimaryObject po : childrenWorkList) {
			Work childrenWork = (Work) po;
			childrenWork.doWFHistoryToDocument(wfHistory, context);
		}
	}

	/**
	 * ���㴦��ƻ���ʱ�ķ���
	 * 
	 * @param context
	 */
	public void doCaculateWorksAllocated(IContext context) {
		final String userid = context.getAccountInfo().getUserId();
		Job job = new Job("���㹤ʱ����") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// ����ù�����׼���У�����ʱ���ܹ��������
				String lc = getLifecycleStatus();
				if (!Utils.inArray(lc, new String[] { STATUS_ONREADY_VALUE,
						STATUS_WIP_VALUE })) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ֻ������Ŀ����
				if (!isProjectWork()) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}
				Project project = getProject();
				lc = project.getLifecycleStatus();
				if (!Utils.inArray(lc, new String[] { STATUS_ONREADY_VALUE,
						STATUS_WIP_VALUE })) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ��¼�ϼ�������׼��ɾ��
				List<ObjectId> syncRemove = new ArrayList<ObjectId>();
				Work parent = (Work) getParent();
				int loopcount = 0;
				while (parent != null && loopcount < 20) {
					loopcount++;
					if (syncRemove.contains(parent.get_id())) {
						break;
					}
					syncRemove.add(parent.get_id());
					parent = (Work) getParent();
				}

				// �����ժҪ������׼��ɾ��
				if (isSummaryWork()) {
					syncRemove.add(get_id());
				}

				// �õ��ƻ���ʱ
				Double works = getPlanWorks();
				if (works == null || works.doubleValue() == 0d) {
					syncRemove.add(get_id());
				}

				// �õ�ʵ�ʿ�ʼʱ���ʵ�����ʱ��
				Date start = getPlanStart();
				Date finish = getPlanFinish();
				if (start == null || finish == null) {
					syncRemove.add(get_id());
				}

				DBCollection col = getCollection(IModelConstants.C_WORKS_ALLOCATE);
				col.remove(new BasicDBObject().append(WorksAllocate.F_WORKID,
						new BasicDBObject().append("$in", syncRemove)),
						WriteConcern.NORMAL);

				if (works == null || works.doubleValue() == 0d || start == null
						|| finish == null) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ����ֹ������ʱ�����Զ�����
				if (hasManualRecordAllocate()) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// �õ�����������
				CalendarCaculater calCaculater = project.getCalendarCaculater();

				Calendar currentCal = Calendar.getInstance();
				currentCal.setTime(start);
				Calendar finishCal = Calendar.getInstance();
				finishCal.setTime(finish);

				// ��������ÿ��Ĺ�ʱ
				// ���������ƽ��
				int workDays = calCaculater.getWorkingDays(start, finish);
				double personDayWorks = works / workDays;

				List<WorksPerformence> records = new ArrayList<WorksPerformence>();

				String chargerId = getChargerId();

				while (currentCal.before(finishCal)) {
					Date date = currentCal.getTime();
					double workingTime = calCaculater.getWorkingTime(date);

					// �ų��ǹ���ʱ��
					if (workingTime > 0) {

						long dateCode = currentCal.getTimeInMillis()
								/ (24 * 60 * 60 * 1000);
						WorksPerformence po = makeWorksPerformence(chargerId,
								new Long(dateCode));
						po.setValue(WorksPerformence.F_WORKS, new Double(
								personDayWorks));
						po.setValue(WorksPerformence.F_DESC, "[ϵͳ����]");
						records.add(po);
					}
					currentCal.add(Calendar.DATE, 1);
				}

				DBObject[] data = new DBObject[records.size()];
				for (int i = 0; i < records.size(); i++) {
					data[i] = records.get(i).get_data();
				}
				col.insert(data, WriteConcern.NORMAL);

				try {
					DBUtil.SAVELOG(userid, "�Զ�����ƻ���ʱ", new Date(), "����ƻ���ʱ����",
							IModelConstants.DB);
				} catch (Exception e) {
				}
				return org.eclipse.core.runtime.Status.OK_STATUS;
			}

		};

		job.setUser(false);
		job.schedule();
	}

	protected boolean hasManualRecordAllocate() {
		return false;
	}

	/**
	 * ���㴦��ʵ�ʹ�ʱ�ķֵ�
	 * 
	 * @param context
	 */
	public void doCalculatePerformence(final IContext context) {
		final String userid = context.getAccountInfo().getUserId();

		Job job = new Job("����ʵ�ʹ�ʱ") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				// ����ù������״̬���ܹ����㼨Ч
				String lc = getLifecycleStatus();
				if (!STATUS_FINIHED_VALUE.equals(lc)) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ֻ������Ŀ����
				if (!isProjectWork()) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ������ժҪ����
				if (isSummaryWork()) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// ����ֹ������ʱ�����Զ�����
				if (hasManualRecordPerformence()) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// �õ�ʵ�ʹ�ʱ�����û�У���ȡ�ƻ���ʱ
				Double works = getActualWorks();
				if (works == null) {
					works = getPlanWorks();
				}
				if (works == null) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// �õ�ʵ�ʿ�ʼʱ���ʵ�����ʱ��
				Date start = getActualStart();
				Date finish = getActualFinish();
				if (start == null || finish == null) {
					return org.eclipse.core.runtime.Status.OK_STATUS;
				}

				// �õ������Ĳ�����(�����������)
				// BasicBSONList participatesIds = getParticipatesIdList();
				// if (participatesIds == null || participatesIds.size() < 1) {
				// return;
				// }

				// �õ�����������
				Project project = getProject();
				CalendarCaculater calCaculater = project.getCalendarCaculater();

				Calendar currentCal = Calendar.getInstance();
				currentCal.setTime(start);
				Calendar finishCal = Calendar.getInstance();
				finishCal.setTime(finish);

				// ��������ÿ��Ĺ�ʱ
				// ���������ƽ��
				int workDays = calCaculater.getWorkingDays(start, finish);
				double personDayWorks = works / workDays;

				List<WorksPerformence> records = new ArrayList<WorksPerformence>();

				String chargerId = getChargerId();

				while (currentCal.before(finishCal)) {
					Date date = currentCal.getTime();
					double workingTime = calCaculater.getWorkingTime(date);

					// �ų��ǹ���ʱ��
					if (workingTime > 0) {
						long dateCode = currentCal.getTimeInMillis()
								/ (24 * 60 * 60 * 1000);
						WorksPerformence po = makeWorksPerformence(chargerId,
								new Long(dateCode));
						po.setValue(WorksPerformence.F_WORKS, new Double(
								personDayWorks));
						po.setValue(WorksPerformence.F_DESC, "[ϵͳ����]");
						records.add(po);
					}

					currentCal.add(Calendar.DATE, 1);
				}

				DBCollection col = getCollection(IModelConstants.C_WORKS_PERFORMENCE);
				DBObject[] data = new DBObject[records.size()];
				for (int i = 0; i < records.size(); i++) {
					data[i] = records.get(i).get_data();
				}
				col.insert(data, WriteConcern.NORMAL);

				try {
					DBUtil.SAVELOG(userid, "�Զ���̯ʵ�ʹ�ʱ", new Date(),
							"�������ʱ���㹤ʱ����", IModelConstants.DB);
				} catch (Exception e) {
				}
				return org.eclipse.core.runtime.Status.OK_STATUS;
			}

		};

		job.setUser(false);
		job.schedule();
	}

	/**
	 * ���͹���������ɵ�֪ͨ
	 * 
	 * @param context
	 *            ��ǰ��������
	 * @param actionName
	 *            �������ı�����
	 * @return
	 * @throws Exception
	 *             ������Ϣ���ֵĴ���
	 */
	private Message doNoticeWorkActionInternal(IContext context,
			String actionName) throws Exception {
		// �����ռ���
		BasicBSONList participatesIdList = getParticipatesIdList();
		if (participatesIdList == null || participatesIdList.isEmpty()) {
			return null;
		}
		// �ų��Լ�
		participatesIdList.remove(context.getAccountInfo().getConsignerId());

		// ����֪ͨ����
		Project project = getProject();

		String title = (project == null ? "" : project.getLabel()) + " " + this
				+ " " + actionName;

		// ����֪ͨ����
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>");
		sb.append("����: ");
		sb.append("</span><br/><br/>");
		sb.append("������Ĺ������µĽ�չ��");
		sb.append("<br/><br/>");

		sb.append(context.getAccountInfo().getUserId() + "|"
				+ context.getAccountInfo().getUserName());
		sb.append(", ");
		sb.append(actionName);
		sb.append("����");
		sb.append("\"");
		sb.append(this);
		sb.append("\"");
		if (isProjectWork()) {
			sb.append(" \"");
			sb.append("��Ŀ:");
			sb.append(getProject());
			sb.append(" \"");
		}

		sb.append("<br/><br/>");
		sb.append("���в�����������йع�����Ϣ��");

		Message message = MessageToolkit.makeMessage(participatesIdList, title,
				context.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// ���õ�������
		message.appendTargets(this, EDITOR, false);

		message.doSave(context);

		return message;
	}

	private void doNoticeWorkAction(final IContext context,
			final String actionName) throws Exception {
		// doNoticeWorkActionInternal(context, actionName);

		Job job = new Job("���͹���֪ͨ") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					doNoticeWorkActionInternal(context, actionName);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return org.eclipse.core.runtime.Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@Override
	public BasicBSONList getTargetList() {
		return (BasicBSONList) getValue(F_TARGETS);
	}

	/**
	 * �������ĳ���ִ���߶�Ӧ���û�
	 * 
	 * @param nodeActor
	 * @return
	 */
	public List<User> getPermittedUserOfWorkflowActor(String processKey,
			String nodeActor) {
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		DBObject processRoleDefinitions = pc
				.getProcessRoleAssignmentData(processKey);
		ObjectId _id = (ObjectId) processRoleDefinitions.get(nodeActor);

		List<PrimaryObject> ralist;
		if (getProjectId() != null) {
			ProjectRole roled = ModelService.createModelObject(
					ProjectRole.class, _id);
			ralist = roled.getAssignment();

		} else {
			Role role = ModelService.createModelObject(Role.class, _id);
			ralist = role.getAssignment();
		}
		List<User> result = new ArrayList<User>();
		if (ralist != null) {
			for (int i = 0; i < ralist.size(); i++) {
				AbstractRoleAssignment ra = (AbstractRoleAssignment) ralist
						.get(i);
				String userid = ra.getUserid();
				User user = UserToolkit.getUserById(userid);
				result.add(user);
			}
		}

		return result;
	}

	private void doStartAfter(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "start.after", params);
	}

	private void doStartBefore(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "start.before", params);
	}

	private void doPauseAfter(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "pause.after", params);

	}

	private void doPauseBefore(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "pause.before", params);

	}

	private void doFinishAfter(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "finish.after", params);

	}

	private void doFinishBefore(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "finish.before", params);

	}

	private void doCancelAfter(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "cancel.after", params);

	}

	private void doCancelBefore(IContext context, Map<String, Object> params)
			throws Exception {
		ModelActivator.executeEvent(this, "cancel.before", params);

	}

	// public boolean doUpdateTask(String key, Task task,
	// String userid) throws Exception {
	// // IProcessControl pc = (IProcessControl)
	// getAdapter(IProcessControl.class);
	//
	// TaskData taskData = task.getTaskData();
	// Status status = taskData.getStatus();
	//
	// /*
	// * ��õ�ǰ�û������������������� * �������id��״̬һ�£���ô������������²���
	// */
	//
	// DBCollection col = getCollection(IModelConstants.C_USERTASK);
	//
	// BasicDBObject query = new BasicDBObject();
	// query.put(UserTask.F_WORK_ID, get_id());
	// query.put(UserTask.F_USERID, userid);
	// query.put(UserTask.F_TASKID, task.getId());
	// query.put(UserTask.F_STATUS, status.name());
	// query.put(UserTask.F_PROCESSKEY, key);
	//
	// long cnt = col.count(query);
	//
	// if (cnt > 0) {
	// return false;
	// }
	//
	// /*
	// * ������Ҫ�������������
	// */
	//
	// DBObject data = new BasicDBObject();
	//
	// // ��������id
	// data.put(UserTask.F_TASKID, task.getId());
	//
	// // ������������
	// List<I18NText> names = task.getNames();
	// Assert.isLegal(names != null && names.size() > 0, "���̻����û�ж���");
	// String taskName = names.get(0).getText();
	// data.put(UserTask.F_DESC, taskName);
	//
	// // ������������
	// List<I18NText> descriptions = task.getDescriptions();
	// if (descriptions != null && descriptions.size() > 0) {
	// String taskComment = descriptions.get(0).getText();
	// data.put(UserTask.F_DESCRIPTION, taskComment);
	// }
	//
	// // ���������ʵ��ִ����id
	// org.jbpm.task.User actualOwner = taskData.getActualOwner();
	// String actorId = actualOwner.getId();
	// data.put(UserTask.F_ACTUALOWNER, actorId);
	//
	// // ��������Ĵ�����
	// org.jbpm.task.User createdBy = taskData.getCreatedBy();
	// data.put(UserTask.F_CREATEDBY, createdBy.getId());
	//
	// // ����Ĵ���ʱ��
	// Date createdOn = taskData.getCreatedOn();
	// data.put(UserTask.F_CREATEDON, createdOn);
	//
	// // ��������̶���id
	// String processId = taskData.getProcessId();
	// data.put(UserTask.F_PROCESSID, processId);
	//
	// // ���������ʵ��id
	// long processInstanceId = taskData.getProcessInstanceId();
	// data.put(UserTask.F_PROCESSINSTANCEID, new Long(processInstanceId));
	//
	// // ����״̬
	// data.put(UserTask.F_STATUS, status.name());
	//
	// // �����workitem ID
	// long workItemId = taskData.getWorkItemId();
	// data.put(UserTask.F_WORKITEMID, new Long(workItemId));
	//
	// WriteResult ws = col.insert(data);
	// checkWriteResult(ws);
	// /**
	// * ���´����Ѳ������ڱ��������������ʷ����ɾ��
	// */
	// // // ����������Ϣ��������
	// // BackgroundContext context = new BackgroundContext();
	// // Message message = doNoticeWorkflow(actorId, taskName, key, context);
	// // Assert.isNotNull(message, "��Ϣ����ʧ��");
	// // data.put(IProcessControl.F_WF_TASK_NOTICEDATE,
	// // message.getValue(Message.F_SENDDATE));
	//
	// /*
	// * ����PrimaryObject��������ڲ�ͬ���û������д��ڶ������������֮�����ݲ�����ά��һ�¡�
	// *
	// * ��ˣ�����doSave()�������б��棬�����������û������ݡ� ʹ��
	// * doSave()�������б��������ں󱣴�Ķ��󸲸��ȱ���ļ�¼�ĳ�����
	// *
	// *
	// * �����ڸ��¹�������Ϣ���Բ����á���ˣ�����ʹ��col.updata�ķ�ʽ���и��£�
	// *
	// * ���ҽ����º�Ľ����д��_data
	// *
	// * ���µĴ���չʾ���ֱ�ӵ���col�ĸ��·���
	// *
	// * 1. ʹ�� findAndModify��ø��º�Ķ���ֵ
	// * ע�⣺ֱ�ӵ���col.update�ǲ��ܻ�ø��º����ݵģ�����PrimaryObject ._data�����ݾ��޷�ͬ������
	// *
	// * 2. �˽�findAndModify���������ĸ������
	// */
	//
	// // // ���̵��ֶ�����
	// // String field = key + IProcessControl.POSTFIX_TASK;
	// //
	// // // ��ñ�ģ�Ͷ�Ӧ�ļ���
	// // DBCollection col = getCollection();
	// //
	// // DBObject nd = col
	// // .findAndModify(
	// // // ��ѯ��������update������queryһ��
	// // new BasicDBObject().append(F__ID, get_id()),
	// // // �����ֶΣ���find�����ķ����ֶ�һ��
	// // new BasicDBObject().append(field, 1),
	// // // ������find����������һ��
	// // null,
	// // // ɾ�������Ϊ�棬�����ѯ�����Ľ���ɾ��
	// // false,
	// // // ������������update�ĸ���һ��
	// // new BasicDBObject().append("$set", new BasicDBObject()
	// // .append(field + "." + userid, data)),
	// // // �Ƿ񷵻ظ��º�Ķ���
	// // true,
	// // // ����ĵ��������Ƿ����
	// // false);
	// //
	// // // �µĶ�����Ϊ��
	// // Assert.isNotNull(nd);
	// //
	// // // ʹ�û�õ��¶���ˢ��_data��ֵ
	// // Object value = nd.get(field);
	// //
	// // setValue(field, value);
	//
	// return true;
	// }

	public UserTask doSaveUserTask(String flowKey, Task task,
			Map<String, Object> taskMetaData, String userid) throws Exception {

		TaskData taskData = task.getTaskData();
		Status status = taskData.getStatus();

		DBCollection col = getCollection(IModelConstants.C_USERTASK);

		BasicDBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		query.put(UserTask.F_USERID, userid);
		query.put(UserTask.F_TASKID, task.getId());
		query.put(UserTask.F_STATUS, status.name());
		query.put(UserTask.F_PROCESSKEY, flowKey);

		DBObject data = col.findOne(query);

		UserTask userTask;

		if (data != null) {
			userTask = ModelService.createModelObject(data, UserTask.class);
		} else {
			/*
			 * ������Ҫ�������������
			 */
			userTask = ModelService.createModelObject(UserTask.class);

			// ��������id
			userTask.setValue(UserTask.F_WORK_ID, get_id());

			userTask.setValue(UserTask.F_WORK_DESC, getDesc());

			// ����״̬
			userTask.setValue(UserTask.F_STATUS, status.name());

			userTask.setValue(UserTask.F_USERID, userid);

			userTask.setValue(UserTask.F_TASKID, task.getId());

			userTask.setValue(UserTask.F_PROCESSKEY, flowKey);

			// ������������
			List<I18NText> names = task.getNames();
			Assert.isLegal(names != null && names.size() > 0, "���̻����û�ж���");
			String taskName = names.get(0).getText();
			userTask.setValue(UserTask.F_DESC, taskName);
			userTask.setValue(UserTask.F_TASK_NAME, taskName);// ����history

			// ������������
			List<I18NText> descriptions = task.getDescriptions();
			if (descriptions != null && descriptions.size() > 0) {
				String taskComment = descriptions.get(0).getText();
				userTask.setValue(UserTask.F_DESCRIPTION, taskComment);
			}

			// ���������ʵ��ִ����id
			org.jbpm.task.User actualOwner = taskData.getActualOwner();
			String actorId = actualOwner.getId();
			userTask.setValue(UserTask.F_ACTUALOWNER, actorId);

			// ��������Ĵ�����
			org.jbpm.task.User createdBy = taskData.getCreatedBy();
			userTask.setValue(UserTask.F_CREATEDBY, createdBy.getId());

			// ����Ĵ���ʱ��
			Date createdOn = taskData.getCreatedOn();
			userTask.setValue(UserTask.F_CREATEDON, createdOn);

			// ��������̶���id
			String processId = taskData.getProcessId();
			userTask.setValue(UserTask.F_PROCESSID, processId);

			// ��������
			DroolsProcessDefinition pd = new DroolsProcessDefinition(processId);
			String processName = pd.getProcess().getName();
			userTask.setValue(UserTask.F_PROCESSNAME, processName);

			// ���������ʵ��id
			long processInstanceId = taskData.getProcessInstanceId();
			userTask.setValue(UserTask.F_PROCESSINSTANCEID, new Long(
					processInstanceId));

			// �����workitem ID
			long workItemId = taskData.getWorkItemId();
			userTask.setValue(UserTask.F_WORKITEMID, new Long(workItemId));

			// ������δ���
			userTask.setValue(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);

		}

		// ���������
		// ��taskformData���䵽��ǰ������������
		if (taskMetaData != null && !taskMetaData.isEmpty()) {
			Iterator<String> iterator = taskMetaData.keySet().iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				String field = "form_" + next;
				userTask.setValue(field, taskMetaData.get(next));
			}
		}

		userTask.doSave(new BackgroundContext());
		return userTask;
	}

	public boolean isProjectWBSRoot() {
		return Boolean.TRUE.equals(getValue(F_IS_PROJECT_WBSROOT));
	}

	public void doStartTask(String processKey, UserTask userTask,
			IContext context) throws Exception {
		String lc = getLifecycleStatus();
		Assert.isTrue(ILifecycle.STATUS_WIP_VALUE.equals(lc), "������ǰ״̬������ִ�����̲���");

		// Task task = userTask.getTask();
		// Assert.isNotNull(task, "�޷���õ�ǰ����������");

		String taskstatus = userTask.getStatus();
		boolean canStartTask = WorkflowService.canStartTask(taskstatus);
		Assert.isTrue(canStartTask, "����ǰ��״̬������ִ�п�ʼ");

		Long taskId = userTask.getTaskId();
		String userId = context.getAccountInfo().getConsignerId();
		Task task = WorkflowService.getDefault().startTask(userId, taskId);

		Assert.isNotNull(task, "��ʼ��������ʧ��");

		// ��ȡ��ǰ����������
		Map<String, Object> taskMetaData = new HashMap<String, Object>();
		taskMetaData.put(IProcessControl.F_WF_TASK_ACTOR, context
				.getAccountInfo().getUserId());
		taskMetaData.put(IProcessControl.F_WF_TASK_STARTDATE, new Date());
		taskMetaData.put(IProcessControl.F_WF_TASK_ACTION,
				IProcessControl.TASK_ACTION_START);

		UserTask newUserTask = doSaveUserTask(processKey, task, taskMetaData,
				userId);
		doSaveWorkflowHistroy(processKey, newUserTask, taskMetaData, context);

		/**
		 * �Ƶ���UserTask�ı����¼�
		 */
		// ����������Ϣ��������

		// List<I18NText> names = task.getNames();
		// String taskName = "";
		// if (names != null && names.size() > 0) {
		// taskName = names.get(0).getText();
		// }
		// doNoticeWorkflow(userId, taskName, processKey, "������", context);
		// data.put(IProcessControl.F_WF_TASK_NOTICEDATE,
		// message.getValue(Message.F_SENDDATE));
	}

	/**
	 * ���processKeyָ�����̵ĵ�ǰ����
	 * 
	 * @param processKey
	 *            ������key Ŀǰֻ��{@link IWorkCloneFields#F_WF_EXECUTE}
	 *            {@link IWorkCloneFields#F_WF_CHANGE}<br/>
	 *            ����֧�ְ󶨸�������̶���
	 * @param executeTask
	 * @param taskMetaData
	 * @param context
	 * @throws Exception
	 */
	public void doCompleteTask(String processKey, UserTask executeTask,
			Map<String, Object> inputParameter,
			Map<String, Object> taskMetaData, IContext context)
			throws Exception {
		String lc = getLifecycleStatus();
		Assert.isTrue(ILifecycle.STATUS_WIP_VALUE.equals(lc), "������ǰ״̬������ִ�����̲���");

		// Task task = getTask(processKey, context);
		// Assert.isNotNull(task, "�޷���õ�ǰ����������");

		String taskstatus = executeTask.getStatus();
		boolean canStartTask = WorkflowService.canFinishTask(taskstatus);
		Assert.isTrue(canStartTask, "����ǰ��״̬������ִ�����");

		Long taskId = executeTask.getTaskId();
		String userId = context.getAccountInfo().getConsignerId();
		Task task = WorkflowService.getDefault().completeTask(taskId, userId,
				inputParameter);

		Assert.isNotNull(task, "�����������ʧ��");

		taskMetaData.put(IProcessControl.F_WF_TASK_ACTOR, context
				.getAccountInfo().getUserId());
		taskMetaData.put(IProcessControl.F_WF_TASK_FINISHDATE, new Date());
		taskMetaData.put(IProcessControl.F_WF_TASK_ACTION,
				IProcessControl.TASK_ACTION_COMPLETE);

		UserTask newUserTask = doSaveUserTask(processKey, task, taskMetaData,
				userId);

		// �����������̼�¼�洢���������ĵ���
		doSaveProcessHistoryToDocument(context);

		doSaveWorkflowHistroy(processKey, newUserTask, taskMetaData, context);

		/**
		 * �Ƶ���UserTask�ı����¼�
		 */
		// // ����������Ϣ��������
		//
		// List<I18NText> names = task.getNames();
		// String taskName = "";
		// if (names != null && names.size() > 0) {
		// taskName = names.get(0).getText();
		// }
		// doNoticeWorkflow(userId, taskName, processKey, "�����", context);
	}

	public List<UserTask> getReservedUserTasks(String userId) {
		return getUserTasks(userId, Status.Reserved.name());
	}

	public long countReservedUserTasks(String userId) {
		return countUserTasks(userId, Status.Reserved.name());
	}

	public List<UserTask> getInprogressUserTasks(String userId) {
		return getUserTasks(userId, Status.InProgress.name());
	}

	public long countInprogressUserTasks(String userId) {
		return countUserTasks(userId, Status.InProgress.name());
	}

	public long countReservedAndInprogressUserTasks(String userId) {
		DBCollection col = getCollection(IModelConstants.C_USERTASK);
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		query.put(UserTask.F_USERID, userId);
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put(
				"$or",
				new BasicDBObject[] {
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.Reserved.name()),
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.InProgress.name()) });
		return col.count(query);
	}

	public List<UserTask> getAllUserTasks(String status) {
		DBCollection col = getCollection(IModelConstants.C_USERTASK);
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put(UserTask.F_STATUS, status);
		DBCursor cur = col.find(query);
		List<UserTask> result = new ArrayList<UserTask>();
		while (cur.hasNext()) {
			DBObject data = cur.next();
			result.add(ModelService.createModelObject(data, UserTask.class));
		}

		return result;
	}

	public List<UserTask> getUserTasks(String userId, String status) {
		DBCollection col = getCollection(IModelConstants.C_USERTASK);
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		query.put(UserTask.F_USERID, userId);
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put(UserTask.F_STATUS, status);
		DBCursor cur = col.find(query);
		List<UserTask> result = new ArrayList<UserTask>();
		while (cur.hasNext()) {
			DBObject data = cur.next();
			result.add(ModelService.createModelObject(data, UserTask.class));
		}

		return result;
	}

	public long countUserTasks(String userId, String status) {
		DBCollection col = getCollection(IModelConstants.C_USERTASK);
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		query.put(UserTask.F_USERID, userId);
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put(UserTask.F_STATUS, status);
		return col.count(query);
	}

	/**
	 * ��ȡ�������ʾ������
	 * 
	 * @return
	 */
	public UserTask getLastDisplayTask(String userId) {
		DBCollection col = getCollection(IModelConstants.C_USERTASK);
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_WORK_ID, get_id());
		DBCursor cur = col.find(query).sort(
				new BasicDBObject().append(UserTask.F__ID, -1));
		UserTask otherUserReservedTask = null;
		// ������û�Id��������ʾ���û�Id������
		UserTask userReservedTask = null;
		UserTask laskTask = null;
		while (cur.hasNext()) {

			UserTask ut = ModelService.createModelObject(cur.next(),
					UserTask.class);

			if (ut.isReserved()) {
				if (ut.getUserId().equals(userId)) {
					if (userReservedTask == null) {
						userReservedTask = ut;
					}
				} else {
					if (otherUserReservedTask == null) {
						otherUserReservedTask = ut;
					}
				}
			} else {
				if (laskTask == null) {
					laskTask = ut;
				}
			}

			// ���û�Ԥ�����������ȷ���
			if (userReservedTask != null) {
				return userReservedTask;
			}

			if (otherUserReservedTask != null) {
				return otherUserReservedTask;
			}

			if (laskTask != null) {
				return laskTask;
			}

		}

		return null;
	}

	// /**
	// * replaced by getReservedUserTasks()
	// * @param processKey
	// * @param context
	// * @return
	// * @throws Exception
	// */
	// @Deprecated
	// public Task getTask(String processKey, IContext context) throws Exception
	// {
	// String userid = context.getAccountInfo().getConsignerId();
	//
	// DBCollection col = getCollection(IModelConstants.C_USERTASK);
	// DBObject query = new BasicDBObject();
	// query.put(UserTask.F_PROCESSKEY, processKey);
	// query.put(UserTask.F_USERID, userid);
	// query.put(UserTask.F_WORK_ID, get_id());
	//
	// DBObject fields = new BasicDBObject();
	// fields.put(UserTask.F_TASKID, 1);
	// DBCursor cur = col.find(query, fields);
	//
	// while (cur.hasNext()) {
	// Long taskId = (Long) cur.next().get(UserTask.F_TASKID);
	//
	// return WorkflowService.getDefault().getUserTask(userid, taskId);
	// }
	//
	// // if (data != null) {
	// // Long taskId = (Long) data.get(IProcessControl.F_WF_TASK_ID);
	// // Assert.isNotNull(taskId);
	// // Task task = WorkflowService.getDefault()
	// // .getUserTask(userid, taskId);
	// // return task;
	// // }
	// return null;
	// }
	//
	// @Deprecated
	// public Task getExecuteTask(IContext context) throws Exception {
	// return getTask(Work.F_WF_EXECUTE, context);
	// }

	/**
	 * �������̵���ʷ��¼,�������ѱ�ȡ��������ɾ��
	 * 
	 * @param key
	 * @param newUserTask
	 * @param taskFormData
	 * @param context
	 * @throws Exception
	 */
	@Deprecated
	private void doSaveWorkflowHistroy(String key, UserTask newUserTask,
			Map<String, Object> taskFormData, IContext context)
			throws Exception {
		DBObject currentData = newUserTask.get_data();

		// ��taskformData���䵽��ǰ������������
		if (taskFormData != null && !taskFormData.isEmpty()) {
			Iterator<String> iterator = taskFormData.keySet().iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				String field = "form_" + next;
				currentData.put(field, taskFormData.get(next));
			}
		}

		// ����ǰ����������append����ʷ������
		String histroyField = key + IProcessControl.POSTFIX_HISTORY;

		DBCollection col = getCollection();
		WriteResult wr = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$push",
						new BasicDBObject().append(histroyField, currentData)));

		checkWriteResult(wr);

	}

	public void mark(String userId, boolean marked) throws Exception {
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", new BasicDBObject().append(
						F_MARK + "." + userId, marked)));
		checkWriteResult(ws);
		DBObject data = (DBObject) getValue(F_MARK);
		if (data == null) {
			data = new BasicDBObject();
			setValue(F_MARK, data);
		}
		data.put(userId, marked);
	}

	public boolean isMarked(String userId) {
		DBObject data = (DBObject) getValue(F_MARK);
		return data != null && Boolean.TRUE.equals(data.get(userId));
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IProcessControl.class)) {
			return (T) new ProcessControl(this) {
				@Override
				protected Class<? extends PrimaryObject> getRoleDefinitionClass() {
					if (isProjectWork()) {
						return ProjectRole.class;
					} else {
						// ����Ŀ�ƻ�������ύ�����Ƕ�������������ʹ������Ŀ�Ľ�ɫ
						if (forceUseProjectRole()) {
							return ProjectRole.class;
						} else {
							return RoleDefinition.class;
						}
					}
				}
			};
		}
		return super.getAdapter(adapter);
	}

	protected boolean forceUseProjectRole() {
		return Boolean.TRUE.equals(getValue(F_USE_PROJECT_ROLE));
	}

	@Deprecated
	public WorkRecord makeWorkRecord() {
		DBObject data = new BasicDBObject();
		data.put(WorkRecord.F_WORK_ID, get_id());
		WorkRecord po = ModelService.createModelObject(data, WorkRecord.class);
		return po;
	}

	public List<WorkRecord> getWorkRecord() {
		Object record = getValue(F_RECORD, true);
		List<WorkRecord> result = new ArrayList<WorkRecord>();
		if (record instanceof List<?>) {
			for (int i = 0; i < ((List<?>) record).size(); i++) {
				Object data = ((List<?>) record).get(i);
				if (data instanceof DBObject) {
					result.add(0, ModelService.createModelObject(
							(DBObject) data, WorkRecord.class));
				}
			}
		}
		return result;
	}

	public boolean isExecuteWorkflowActivateAndAvailable() {
		IProcessControl ip = getAdapter(IProcessControl.class);
		return ip.isWorkflowActivateAndAvailable(F_WF_EXECUTE);
	}

	/**
	 * �޸Ĺ��������ˡ�ָ���ߡ������ߺ͹�������ִ����
	 * 
	 * @param fromUserId
	 *            : ��Ҫ�޸ĵ���Ա
	 * @param toUserId
	 *            : �޸ĳɸ���Ա
	 */
	public String changeWorkUser(String fromUserId, String toUserId) {
		if (canChangeWorkUser(fromUserId, toUserId)) {
			String changeFiled = "";
			BasicDBObject object = new BasicDBObject();
			// �޸ĸ�����
			if (fromUserId.equals(getChargerId())) {
				object.put(F_CHARGER, toUserId);
				changeFiled = changeFiled + "������";
			}
			// �޸�ָ����
			if (fromUserId.equals(getAssignerId())) {
				object.put(F_ASSIGNER, toUserId);
				if (changeFiled != "") {
					changeFiled = changeFiled + "��";
				}
				changeFiled = changeFiled + "ָ����";
			}
			// �޸Ĳ�����
			List<?> oldParticipatesIdList = getParticipatesIdList();
			BasicBSONList newParticipatesIdList = new BasicDBList();
			if (oldParticipatesIdList != null) {
				boolean bchange = false;
				for (int i = 0; i < oldParticipatesIdList.size(); i++) {
					String userId = (String) oldParticipatesIdList.get(i);
					if (userId.equals(fromUserId)) {
						bchange = true;
						newParticipatesIdList.add(toUserId);
					}
					newParticipatesIdList.add(userId);
				}
				if (bchange) {
					object.put(F_PARTICIPATE, newParticipatesIdList);
					if (changeFiled != "") {
						changeFiled = changeFiled + "��";
					}
					changeFiled = changeFiled + "������";
				}
			}

			// ��������ִ����
			// ִ�й�������
			if (changeWorkFlowActors(fromUserId, toUserId, F_WF_EXECUTE, object)) {
				if (changeFiled != "") {
					changeFiled = changeFiled + "��";
				}
				changeFiled = changeFiled + "����ִ������ִ����";
			}

			// �����������
			if (changeWorkFlowActors(fromUserId, toUserId, F_WF_CHANGE, object)) {
				if (changeFiled != "") {
					changeFiled = changeFiled + "��";
				}
				changeFiled = changeFiled + "�����������ִ����";
			}

			if (object.size() > 0) {
				DBCollection userCol = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_WORK);
				userCol.update(new BasicDBObject().append(F__ID, get_id()),
						new BasicDBObject().append("$set", object), false, true);
			}
			if (changeFiled != "") {
				return "\"" + getDesc() + "\"������" + changeFiled;
			} else {
				return null;
			}
		}
		return null;
	}

	private boolean canChangeWorkUser(String changedUserId, String changeUserId) {
		return true;
	}

	/**
	 * ����ܷ��޸ĸù���
	 * 
	 * @param changedUserId
	 * @param changeUserId
	 * @return
	 */
	public List<Object[]> checkChangeWorkUser(String changedUserId,
			String changeUserId) {
		List<Object[]> message = new ArrayList<Object[]>();
		String lifecycleStatus = getLifecycleStatus();

		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { "�����Ѿ�ȡ�����޷������޸�", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { "�����Ѿ���ɣ��޷������޸�", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "�����ڽ����У������޸Ĺ�������ִ����", this,
					SWT.ICON_WARNING });
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "�����Ѿ���ͣ�������޸Ĺ�������ִ����", this,
					SWT.ICON_WARNING });
		}

		return message;
	}

	private boolean changeWorkFlowActors(String changedUserId,
			String changeUserId, String process, BasicDBObject object) {
		IProcessControl ip = getAdapter(IProcessControl.class);
		boolean hasChange = false;
		DBObject actorsData = ip.getProcessActorsData(process);
		if (actorsData != null) {
			Iterator<String> iter = actorsData.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String userid = (String) actorsData.get(key);
				if (changedUserId.equals(userid)) {
					object.put(process + IProcessControl.POSTFIX_ACTORS + "."
							+ key, changedUserId);
					hasChange = true;
				}
			}
		}
		return hasChange;
	}

	public int getRemindBefore() {
		Object value = getValue(F_REMIND_BEFORE);
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		return 0;
	}

	public boolean isRemindNow() {
		int remindBefore = getRemindBefore();
		if (remindBefore > 0) {
			Date now = new Date();
			Date _planFinish = getPlanFinish();
			return _planFinish != null
					&& remindBefore > 0
					&& (_planFinish.getTime() - now.getTime()) < remindBefore * 3600000;
		}
		return false;
	}

	public boolean isDelayFinish() {
		Date _planFinish = getPlanFinish();
		Date _actualFinish = getActualFinish();

		if (_planFinish == null) {
			return false;
		}
		if (_actualFinish != null) {
			return _actualFinish.after(_planFinish);
		} else {
			return new Date().after(_planFinish);
		}
	}
	
	public boolean isDelayStart() {
		Date _planStart = getPlanStart();
		Date _actualStart = getActualStart();

		if (_planStart == null) {
			return false;
		}
		if (_actualStart != null) {
			return _actualStart.after(_planStart);
		} else {
			return new Date().after(_planStart);
		}
	}
	
	public boolean isAdvanceFinish() {
		Date _planFinish = getPlanFinish();
		Date _actualFinish = getActualFinish();
		
		if (_planFinish == null) {
			return false;
		}
		
		if (_actualFinish != null) {
			return _actualFinish.before(_planFinish);
		} else {
			return false;
		}
	}
	
	public boolean isAdvanceStart() {
		Date _planStart = getPlanStart();
		Date _actualStart = getActualStart();
		
		if (_planStart == null) {
			return false;
		}
		
		if (_actualStart != null) {
			return _actualStart.before(_planStart);
		} else {
			return false;
		}
	}

	public boolean isStandloneWork() {
		Object type = getValue(F_WORK_TYPE);
		return type instanceof Integer
				&& ((Integer) type).intValue() == WORK_TYPE_STANDLONE;
	}

	// @Override
	// public boolean isSummaryWork() {
	// Object value = getValue(F_PERFORMENCE_ISSUMMARY);
	// if(value == null){
	// return super.isSummaryWork();
	// }else{
	// return Boolean.TRUE.equals(value);
	// }
	// }

	public boolean isProjectWork() {
		return !isStandloneWork();
	}

	public void copyWorkDefinition(String key, IContext context)
			throws Exception {
		WorkDefinition wd = getWorkDefinition();
		if (wd == null) {
			return;
		}
		if (!isStandloneWork()) {
			return;
		}
		Map<ObjectId, DBObject> rolemap = copyRoleDefinition(context);

		IProcessControl ipc = getAdapter(IProcessControl.class);

		// �����ɫ����
		DBObject radata = ipc.getProcessRoleAssignmentData(key);
		Iterator<String> iterator;
		if (radata != null) {
			iterator = radata.keySet().iterator();
			while (iterator.hasNext()) {
				String parameter = iterator.next();
				ObjectId value = (ObjectId) radata.get(parameter);
				DBObject newRoleDef = rolemap.get(value);
				if (newRoleDef != null) {
					RoleDefinition rd = ModelService.createModelObject(
							newRoleDef, RoleDefinition.class);
					ipc.setProcessActionAssignment(key, parameter, rd);
				}
			}
		}

		// �����û�����
		DBObject acdata = ipc.getProcessActorsData(key);
		if (acdata == null) {
			radata = ipc.getProcessRoleAssignmentData(key);
			if (radata != null) {

				iterator = radata.keySet().iterator();
				while (iterator.hasNext()) {
					String parameter = iterator.next();
					ObjectId value = (ObjectId) radata.get(parameter);
					RoleDefinition rd = ModelService.createModelObject(
							RoleDefinition.class, value);
					Role orole = rd.getOrganizationRole();
					List<PrimaryObject> roleAss = orole.getAssignment();
					if (!roleAss.isEmpty()) {
						ipc.setProcessActionActor(key, parameter,
								((AbstractRoleAssignment) roleAss.get(0))
										.getUserid());
					}
				}
			}
		}

	}

	private DBObject copyDocumentFromTemplate(
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject[]> fileToCopy, ObjectId documentTemplateId) {
		DBObject documentData;
		DBCollection docdCol = getCollection(IModelConstants.C_DOCUMENT_DEFINITION);
		DBObject documentTemplate = docdCol.findOne(new BasicDBObject().append(
				Document.F__ID, documentTemplateId));
		documentData = new BasicDBObject();

		documentData.put(Document.F__ID, new ObjectId());

		documentData.put(Document.F_WORK_ID, get_id());

		Object value = documentTemplate.get(DocumentDefinition.F_DESC);
		if (value != null) {
			documentData.put(Document.F_DESC, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DESC_EN);
		if (value != null) {
			documentData.put(Document.F_DESC_EN, value);
		}

		value = new Boolean(Boolean.TRUE.equals(documentTemplate
				.get(DocumentDefinition.F_ATTACHMENT_CANNOT_EMPTY)));
		documentData.put(Document.F_ATTACHMENT_CANNOT_EMPTY, value);

		value = documentTemplate.get(DocumentDefinition.F_DESCRIPTION);
		if (value != null) {
			documentData.put(Document.F_DESCRIPTION, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DOCUMENT_EDITORID);
		if (value != null) {
			documentData.put(Document.F__EDITOR, value);
		}

		// �����ĵ��ĸ��������ļ�
		BasicBSONList templateFiles = (BasicBSONList) documentTemplate
				.get(DocumentDefinition.F_TEMPLATEFILE);
		if (templateFiles != null) {
			BasicBSONList documentFiles = new BasicBSONList();
			for (int i = 0; i < templateFiles.size(); i++) {
				DBObject templateFile = (DBObject) templateFiles.get(i);
				DBObject documentFile = new BasicDBObject();
				documentFile.put(RemoteFile.F_ID, new ObjectId());
				documentFile.put(RemoteFile.F_FILENAME,
						templateFile.get(RemoteFile.F_FILENAME));
				documentFile.put(RemoteFile.F_NAMESPACE,
						Document.FILE_NAMESPACE);
				documentFile.put(RemoteFile.F_DB, Document.FILE_DB);
				documentFiles.add(documentFile);
				fileToCopy.add(new DBObject[] { templateFile, documentFile });
			}
			documentData.put(Document.F_VAULT, documentFiles);
		}
		// ����ĵ�����
		documentsToInsert.put(documentTemplateId, documentData);

		return documentData;
	}

	/**
	 * ͨ��ͨ�ù������崴���¹���
	 * 
	 * @param workd
	 * @param context
	 * @throws Exception
	 */
	public void doCreateChildFromGenericWorkDefinition(WorkDefinition workdef,
			IContext context) throws Exception {
		// 1.����workd
		ObjectId tgtParentId = get_id();

		ObjectId tgtRootId = getRoot().get_id();

		Project project = getProject();

		HashMap<ObjectId, DBObject> worksToBeInsert = new HashMap<ObjectId, DBObject>();

		HashMap<ObjectId, DBObject> documentsToInsert = new HashMap<ObjectId, DBObject>();

		List<DBObject> deliverableToInsert = new ArrayList<DBObject>();

		List<DBObject[]> fileToCopy = new ArrayList<DBObject[]>();

		Map<ObjectId, DBObject> roleMap = new HashMap<ObjectId, DBObject>();

		int seq = getMaxChildSeq();

		ObjectId folderRootId = project.getFolderRootId();

		ObjectId srcParent = workdef.get_id();

		DBObject targetParentWorkData = ProjectToolkit
				.getWorkFromWorkDefinition(tgtParentId, tgtRootId, project,
						roleMap, folderRootId, documentsToInsert,
						deliverableToInsert, fileToCopy, context,
						project.get_id(), new Integer(seq + 1),
						workdef.get_data(), null);
		worksToBeInsert.put(srcParent, targetParentWorkData);
		tgtParentId = (ObjectId) targetParentWorkData.get(F__ID);

		ProjectToolkit.copyWBSTemplate(srcParent, tgtParentId, tgtRootId,
				project, roleMap, worksToBeInsert, folderRootId,
				documentsToInsert, deliverableToInsert, fileToCopy, context);

		// ���湤��
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		Collection<DBObject> collection = worksToBeInsert.values();
		WriteResult ws;
		if (!collection.isEmpty()) {
			ws = workCol.insert(collection.toArray(new DBObject[0]),
					WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// �����ĵ�
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		ws = docCol.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		collection = documentsToInsert.values();
		if (!collection.isEmpty()) {
			ws = docCol.insert(collection.toArray(new DBObject[0]),
					WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// ���潻����
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = deliCol.remove(new BasicDBObject().append(
				Deliverable.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		if (!deliverableToInsert.isEmpty()) {
			ws = deliCol.insert(deliverableToInsert, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// �����ļ�
		for (DBObject[] dbObjects : fileToCopy) {
			DBObject src = dbObjects[0];
			DBObject tgt = dbObjects[1];

			String srcDB = (String) src.get(RemoteFile.F_DB);
			String srcFilename = (String) src.get(RemoteFile.F_FILENAME);
			String srcNamespace = (String) src.get(RemoteFile.F_NAMESPACE);
			ObjectId srcID = (ObjectId) src.get(RemoteFile.F_ID);

			String tgtDB = (String) tgt.get(RemoteFile.F_DB);
			String tgtFilename = (String) tgt.get(RemoteFile.F_FILENAME);
			String tgtNamespace = (String) tgt.get(RemoteFile.F_NAMESPACE);
			ObjectId tgtID = (ObjectId) tgt.get(RemoteFile.F_ID);

			FileUtil.copyGridFSFile(srcID, srcDB, srcFilename, srcNamespace,
					tgtID, tgtDB, tgtFilename, tgtNamespace);
		}
	}

	/**
	 * ��ó�������ı��������ƻ���ʱ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public double getOverloadCount() throws Exception {
		if (overCount != null) {
			return overCount;
		}

		if (!isProjectWork()) {
			throw new Exception("ֻ����Ŀ�������ܽ��г�������");
		}
		BasicBSONList idlist = getParticipatesIdList();
		if (idlist == null || idlist.size() < 1) {
			throw new Exception("����û��ָ��������");
		}
		Double planWorks = getPlanWorks();
		if (planWorks == null) {
			throw new Exception("����û��ָ���ƻ���ʱ");
		}

		// ��ȡ�ƻ���������
		Date planStart = getPlanStart();
		Date planFinih = getPlanFinish();

		CalendarCaculater cc = getProject().getCalendarCaculater();
		double hours = cc.getWorkingHours(planStart, planFinih);
		// ������������X����ʱ����õĻ�����ʱ
		double totalWorkHourAvailable = hours * idlist.size();
		if (totalWorkHourAvailable == 0) {
			throw new Exception("�޿��üƻ���ʱ");
		}
		overCount = planWorks / totalWorkHourAvailable;
		return overCount;
	}

	// /**
	// * ���ĳ����������ĳ���ʵ�ʹ�ʱ
	// *
	// * @param userid
	// * @param date
	// * @return
	// */
	// public double getParticipatesActualWorks(String userid, Date date) {
	//
	// // ���ȶ�ȡperformence��¼
	// BasicBSONList performence = getPerformence();
	// if (performence != null) {// ���ݼ�Ч��¼��ȡ
	// for (int i = 0; i < performence.size(); i++) {
	// DBObject item = (DBObject) performence.get(i);
	// String _userid = (String) item.get(F_SF_PERFORMENCE_USERID);
	// if (userid.equals(_userid)) {
	// Date _date = (Date) item.get(F_SF_PERFORMENCE_DATE);
	// // ����Ƚϣ���ͬ
	// if (date.getTime() / (24 * 60 * 60 * 1000) == _date
	// .getTime() / (24 * 60 * 60 * 1000)) {
	// Double value = (Double) item
	// .get(F_SF_PERFORMENCE_ACTUALWORKS);
	// if (value != null) {
	// return value.doubleValue();
	// } else {
	// return 0d;
	// }
	// }
	// }
	// }
	// return 0d;
	// } else {
	// // û�м�Ч��¼�ģ���������Ѿ���ɣ����ռƻ���ʱ�����ݲ����������͹��ڽ��з�̯
	// String ls = getLifecycleStatus();
	// if (STATUS_FINIHED_VALUE.equals(ls)) {
	// Double works = getActualWorks();
	// if (works == null) {
	// works = getPlanWorks();
	// }
	// if (works == null) {
	// return 0d;
	// }
	//
	// //�����Ƿ���ʵ�ʿ�ʼ��ʱ�����֮��
	// Date as = getActualStart();
	// Date af = getActualFinish();
	// if(date.getTime()<as.getTime()||date.getTime()>af.getTime()){
	// return 0d;
	// }
	//
	// // ȡ�����Ƿ�Ϊ������
	// Double workingTimeOfDateUseCache = getWorkingTimeOfDateUseCache(date);
	// if (workingTimeOfDateUseCache==0d) {
	// return 0d;
	// }
	//
	// BasicBSONList ids = getParticipatesIdList();
	// if (ids != null) {
	// return works / ids.size();
	// } else {
	// return 0d;
	// }
	// } else {
	// return 0d;
	// }
	// }
	// }

	// private Double getWorkingTimeOfDateUseCache(Date date) {
	// long key = date.getTime() / (24 * 60 * 60 * 1000);
	// if (workingDateUseCache == null) {
	// workingDateUseCache = new HashMap<Long, Double>();
	// }
	// Double workingTime = workingDateUseCache.get(key);
	// if (workingTime == null) {
	// Project project = getProject();
	// CalendarCaculater cc;
	// if (project != null) {
	// cc = project.getCalendarCaculater();
	// } else {
	// List<PrimaryObject> conditions = new SystemCalendar()
	// .getDataSet().getDataItems();
	// cc = new CalendarCaculater(conditions);
	// }
	// workingTime = cc.getWorkingTime(date);
	// workingDateUseCache.put(key, workingTime);
	// }
	// return workingTime;
	// }
	//
	//
	// private DBObject calculateWorksAllocateTable() {
	// BasicBSONList performence = getPerformence();
	// if(performence!=null){
	// return calculateWorksAllocateTableFromPerformenceRecord();
	// }
	//
	//
	// //��ʵ�ʿ�ʼ���ڿ�ʼ����
	// Date start = getActualStart();
	// Date finish = getActualFinish();
	//
	//
	//
	// return null;
	// }

	public DBObject getPerformence() {
		return (DBObject) getValue(F_PERFORMENCE);
	}

	public double getParticipatesActualWorks(String userid, Date date) {
		String key = "" + date.getTime() / (24 * 60 * 60 * 1000);

		DBCollection col = getCollection(IModelConstants.C_WORKS_PERFORMENCE);
		col.find(new BasicDBObject()
				.append(WorksPerformence.F_WORKID, get_id()));

		DBObject performence = getPerformence();
		if (performence != null) {
			DBObject userPerformence = (DBObject) performence.get(userid);
			if (userPerformence != null) {
				Double value = (Double) userPerformence.get("" + key);
				if (value != null) {
					return value.doubleValue();
				}
			}
		}
		return 0;
	}

	/**
	 * ����ĳ����������ĳ��ı������Ĺ�ʱ
	 * 
	 * @param userid
	 * @param date
	 * @param works
	 */
	public void doSetParticipatesActualWorks(String userid, Date date,
			double works) {
		// long datefield = date.getTime() / (24 * 60 * 60 * 1000);
		// String key = F_PERFORMENCE + "." + userid + "." + datefield;
		// value = new BasicDBObject().append(key, val)
		// getCollection().update(new BasicDBObject().append(F__ID, get_id()),
		// o);
	}

	public WorksPerformence getWorksPerformence(Date date, String userid) {
		Long dateCode = new Long(date.getTime() / (24 * 60 * 60 * 1000));

		DBCollection col = getCollection(IModelConstants.C_WORKS_PERFORMENCE);
		DBObject data = col.findOne(new BasicDBObject()
				.append(WorksPerformence.F_WORKID, get_id())
				.append(WorksPerformence.F_USERID, userid)
				.append(WorksPerformence.F_DATECODE, dateCode));
		if (data != null) {
			return ModelService.createModelObject(data, WorksPerformence.class);
		}
		return null;
	}

	public boolean hasManualRecordPerformence() {
		DBCollection col = getCollection(IModelConstants.C_WORKS_PERFORMENCE);
		long count = col.count(new BasicDBObject().append(
				WorksPerformence.F_WORKID, get_id()));
		return count > 0;
	}

	public WorksPerformence makeTodayWorksPerformence(String userid) {
		Long dateCode = new Long(new Date().getTime() / (24 * 60 * 60 * 1000));
		return makeWorksPerformence(userid, dateCode);
	}

	public WorksPerformence makeWorksPerformence(String userid, Long dateCode) {
		DBObject data = new BasicDBObject();
		WorksPerformence po = ModelService.createModelObject(data,
				WorksPerformence.class);
		po.setValue(WorksPerformence.F_WORKID, get_id());
		po.setValue(WorksPerformence.F_USERID, userid);
		po.setValue(WorksPerformence.F_COMMITDATE, new Date());
		po.setValue(WorksPerformence.F_DATECODE, dateCode);

		Project project = getProject();
		if (project != null) {
			po.setValue(WorksPerformence.F_PROJECTDESC, project.getLabel());
			po.setValue(WorksPerformence.F_PROJECT_ID, project.get_id());
		}

		po.setValue(WorksPerformence.F_WORKDESC, getLabel());
		po.setValue(WorksPerformence.F_PLANWORKS, getPlanWorks());

		return po;
	}

	public void doAddParticipateList(List<?> userList) throws Exception {
		List<String> allUser = new ArrayList<String>();
		BasicBSONList participates = getParticipatesIdList();
		for (Object obj : userList) {
			if (!allUser.contains(obj)) {
				allUser.add((String) obj);
			}
		}
		for (Object obj : participates) {
			if (!allUser.contains(obj)) {
				allUser.add((String) obj);
			}
		}
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_PARTICIPATE, allUser)));
		checkWriteResult(ws);
	}

	public void doChangeDeliverableLifeCycleStatus(IContext context,
			String operation) {
		try {
			List<PrimaryObject> documents = getOutputDeliverableDocuments();
			for (PrimaryObject po : documents) {
				Document document = (Document) po;
				document.doSetLifeCycleStatus(context, operation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PrimaryObject> getOutputDeliverableDocuments() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> d = getOutputDeliverable();
		for (int i = 0; i < d.size(); i++) {
			Deliverable ditem = (Deliverable) d.get(i);
			Document doc = ditem.getDocument();
			if (doc != null) {
				result.add(doc);
			}
		}
		return result;
	}

	public List<PrimaryObject> getOutputDeliverable() {
		BasicDBObject condition = new BasicDBObject();
		condition.put(Deliverable.F_WORK_ID, get_id());
		condition.put(
				"$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Deliverable.F_TYPE,
								Deliverable.TYPE_OUTPUT),
						new BasicDBObject().append(Deliverable.F_TYPE, null) });
		return getRelationByCondition(Deliverable.class, condition);
	}

	public void doSetDocumentLock(IContext context, boolean locked) {
		try {
			List<PrimaryObject> documents = getOutputDeliverableDocuments();
			for (PrimaryObject po : documents) {
				Document document = (Document) po;
				if (locked) {
					document.doLock(context);
				} else {
					document.doUnLock(context);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
