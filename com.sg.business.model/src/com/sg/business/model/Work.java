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
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

/**
 * <p>
 * 工作
 * <p/>
 * 工作用于描述项目中的工作分解结构
 * 
 * @author jinxitao
 * 
 */
public class Work extends AbstractWork implements IProjectRelative, ISchedual,
		ILifecycle, IReferenceContainer {

	/**
	 * 设定工作交付物的编辑器
	 */
	public static final String EDIT_WORK_DELIVERABLE = "edit.work.deliverable";

	/**
	 * 带流程叶子工作编辑器
	 */
	public static final String EDIT_WORK_PLAN_1 = "edit.work.plan.1";

	/**
	 * 不带流程叶子工作编辑器
	 */
	public static final String EDIT_WORK_PLAN_0 = "edit.work.plan.0";

	/**
	 * 工作的编辑器ID
	 */
	public static final String EDITOR = "view.work";

	/**
	 * 工作设置的编辑器ID
	 */
	public static final String EDITOR_SETTING = "editor.work.setting";

	/**
	 * 创建工作的编辑器ID
	 */
	public static final String EDITOR_CREATE_RUNTIME_WORK = "editor.create.runtimework";

	/**
	 * 编辑工作计划
	 */
	// public static final String EDITOR_WORK_PLAN = "edit.work.plan";

	/**
	 * 必需的，不可删除，布尔类型的字段
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * 负责人的id userid
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * 指派者的id
	 */
	public static final String F_ASSIGNER = "assignerid";

	/**
	 * 工作承担者
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * 工作变更流程执行者
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	/**
	 * 工作执行流程执行者
	 */
	public static final String F_WF_EXECUTE_ACTORS = "wf_execute_actors";

	/**
	 * 发起新工作的向导编辑器
	 */
	public static final String EDITOR_LAUNCH = "editor.work.launch";

	/**
	 * 描述
	 */
	public static final String F_DESCRIPTION = "description";

	public static final String F_IS_PROJECT_WBSROOT = "iswbsroot";

	/**
	 * 下级所有工作完成时，本工作自动完成
	 */
	public static final String F_S_AUTOFINISHWITHCHILDREN = "s_autofinishwithchildren";

	/**
	 * 上级工作完成时，本工作自动完成
	 */
	public static final String F_S_AUTOFINISHWITHPARENT = "s_autofinishwithparent";

	/**
	 * 上级工作开始时，本工作自动开始
	 */
	public static final String F_S_AUTOSTARTWITHPARENT = "s_autostartwithparent";

	/**
	 * 是否允许添加交付物
	 */
	public static final String F_S_CANADDDELIVERABLES = "s_canadddeliverables";

	/**
	 * 是否允许分解工作
	 */
	public static final String F_S_CANBREAKDOWN = "s_canbreakdown";

	/**
	 * 是否允许修改计划工时
	 */
	public static final String F_S_CANMODIFYPLANWORKS = "s_canmodifyplanworks";

	/**
	 * 是否可以跳过进行中的流程完成工作
	 */
	public static final String F_S_CANSKIPTOFINISH = "s_canskiptofinish";

	/**
	 * 需启动项目变更流程实施工作的更改
	 */
	public static final String F_S_PROJECTCHANGEFLOWMANDORY = "s_projectchangeflowmandory";

	/**
	 * 需启动变更流程实施工作的更改
	 */
	public static final String F_S_WORKCHANGEFLOWMANDORY = "s_workchangeflowmandory";

	public static final String F_MARK = "marked";

	public static final String F_RECORD = "record";

	public static final String F_WORK_DEFINITION_ID = "workd_id";

	public static final String F_USE_PROJECT_ROLE = "useprojectrole";

	/**
	 * 根据状态返回不同的图标
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
	 * 返回工作所属项目
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
	 * 构建下级工作
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

		// 设置一些基本的选项设定
		data.put(F_S_CANADDDELIVERABLES, Boolean.TRUE);
		data.put(F_S_CANBREAKDOWN, Boolean.TRUE);
		data.put(F_S_CANMODIFYPLANWORKS, Boolean.TRUE);

		Work po = ModelService.createModelObject(data, Work.class);
		return po;
	}

	/**
	 * 返回工作所属项目
	 * 
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProject();
	}

	/**
	 * 返回工作所属项目_id
	 * 
	 * @return
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * 返回工作计划开始时间
	 * 
	 * @return Date
	 */
	public Date getPlanStart() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_PLAN_START);
			if (d != null) {
				return Utils.getDayBegin(d).getTime();
			} else {
				return null;
			}
		} else {
			Date start = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date s = child.getPlanStart();
				if (s != null && (start == null || s.before(start))) {
					start = s;
				}
			}
			return start;
		}
	}

	/**
	 * 返回工作计划完成时间
	 * 
	 * @return Date
	 */
	public Date getPlanFinish() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_PLAN_FINISH);
			if (d != null) {
				return Utils.getDayEnd(d).getTime();
			} else {
				return null;
			}
		} else {
			Date finish = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date f = child.getPlanFinish();
				if (f != null && (finish == null || f.after(finish))) {
					finish = f;
				}
			}
			return finish;
		}
	}

	/**
	 * 返回工作实际开始时间
	 * 
	 * @return Date
	 */
	public Date getActualStart() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_ACTUAL_START);
			if (d != null) {
				return Utils.getDayBegin(d).getTime();
			} else {
				return null;
			}
		} else {
			Date start = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date s = child.getActualStart();
				if (s != null && (start == null || s.before(start))) {
					start = s;
				}
			}
			return start;
		}
	}

	/**
	 * 返回工作实际完成时间
	 * 
	 * @return Date
	 */
	public Date getActualFinish() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_ACTUAL_FINISH);
			if (d != null) {
				return Utils.getDayEnd(d).getTime();
			} else {
				return null;
			}
		} else {
			Date finish = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date f = child.getActualFinish();
				if (f != null && (finish == null || f.after(finish))) {
					finish = f;
				}
			}
			return finish;
		}
	}

	/**
	 * 返回工作的实际工时
	 * 
	 * @return Double
	 */
	public Double getActualWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_ACTUAL_WORKS);
		} else {
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getActualWorks();
				if (w != null) {
					if (works == null) {
						works = w;
					} else {
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	/**
	 * 返回工作的计划工时
	 * 
	 * @return Double
	 */
	public Double getPlanWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_PLAN_WORKS);
		} else {
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getPlanWorks();
				if (w != null) {
					if (works == null) {
						works = w;
					} else {
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	/**
	 * 返回工作的实际工期
	 * 
	 * @return Integer
	 */
	public Integer getActualDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_ACTUAL_DURATION);
		} else {
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getActualDuration();
				if (d != null) {
					if (duration == null) {
						duration = d;
					} else if (duration < d) {
						duration = d;
					}
				}
			}
			return duration;
		}
	}

	/**
	 * 返回工作的计划工期
	 * 
	 * @return Integer
	 */
	public Integer getPlanDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_PLAN_DURATION);
		} else {
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getPlanDuration();
				if (d != null) {
					if (duration == null) {
						duration = d;
					} else if (duration < d) {
						duration = d;
					}
				}
			}
			return duration;
		}
	}

	/**
	 * 新建工作交付物
	 * 
	 * @return Deliverable
	 */
	@Override
	public Deliverable makeDeliverableDefinition() {
		return makeDeliverableDefinition(null);
	}

	/**
	 * 新建工作交付物
	 * 
	 * @param docd
	 *            ,文档模板定义
	 * @return Deliverable
	 */
	public Deliverable makeDeliverableDefinition(DocumentDefinition docd) {
		DBObject data = new BasicDBObject();
		data.put(Deliverable.F_WORK_ID, get_id());

		data.put(Deliverable.F_PROJECT_ID, getValue(F_PROJECT_ID));

		if (docd != null) {
			data.put(Deliverable.F_DOCUMENT_DEFINITION_ID, docd.get_id());
			data.put(Deliverable.F_DESC, docd.getDesc());
		}

		Deliverable po = ModelService
				.createModelObject(data, Deliverable.class);

		return po;
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "工作";
	}

	/**
	 * 返回工作默认编辑器ID
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * 判断工作的属性是否只读
	 * 
	 * @param field
	 *            ,工作的属性
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String field, IContext context) {
		return true;
	}

	@Override
	public boolean canCheck() {
		// 未完成和未取消的
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))
				&& (!STATUS_FINIHED_VALUE.equals(lc));
	}

	/**
	 * 能否点击启动
	 */
	@Override
	public boolean canStart() {
		// 检查本工作生命周期状态是否符合:准备中，无状态，已暂停
		// 如果不是这些状态(已完成、已取消、进行中)，返回false
		String lifeCycle = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_WIP_VALUE.equals(lifeCycle)) {
			return false;
		}

		return true;
	}

	/**
	 * 能否点击暂停
	 */
	@Override
	public boolean canPause() {
		// 检查本工作生命周期状态是否符合:进行中
		// 如果不是这些状态(已完成、已取消、准备中、无状态、已暂停)，返回false
		String lifeCycle = getLifecycleStatus();
		if (STATUS_WIP_VALUE.equals(lifeCycle)) {
			return true;
		}

		return false;
	}

	/**
	 * 能否点击取消
	 */
	@Override
	public boolean canCancel() {
		// 检查本工作生命周期状态是否符合:已暂停,进行中，准备中
		String lifeCycle = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)
				|| STATUS_ONREADY_VALUE.equals(lifeCycle)
				|| STATUS_NONE_VALUE.equals(lifeCycle);
	}

	@Override
	public boolean canFinish() {
		// 1.首先检查本工作生命周期状态是否符合:已暂停,进行中
		// 如果不是这些状态(已完成、准备中、无状态、已取消)，返回false
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
	 * 判断流程是否可以点击开始
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
	 * 判断流程是否可以点击完成
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
	 * 判断流程是否可以点击中止
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
	 * 检查工作是否可以取消
	 * 
	 * @param context
	 * @throws Exception
	 */
	// public void cancelCheck(IContext context) throws Exception {
	// // 1.判断是否是本级的负责人
	// String userId = context.getAccountInfo().getConsignerId();
	// Work parent = (Work) getParent();
	// if (parent != null) {
	// if (!userId.equals(parent.getChargerId())) {
	// throw new Exception("不是工作负责人，" + parent);
	// }
	// } else {
	// throw new Exception("本工作不能取消，" + this);
	// }
	//
	// if (isMandatory()) {
	// throw new Exception("本工作不能取消，" + this);
	// }
	//
	// // 2.判断上级工作生命周期状态是否符合：进行中
	// // 如果不在进行中，返回false
	// Work parentWork = (Work) getParent();
	// if (parentWork != null) {
	// if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
	// throw new Exception("上级工作不在进行中，" + this);
	// }
	// } else {
	// Project project = getProject();
	// if (project != null) {
	// if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
	// throw new Exception("项目不在进行中，" + this);
	// }
	// }
	// }
	// }

	public boolean isMandatory() {
		return Boolean.TRUE.equals(getValue(F_MANDATORY));
	}

	/**
	 * 能否点击编辑
	 */
	@Override
	public boolean canEdit(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，进行中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停)，返回false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		if (isProjectWBSRoot()) {
			return false;
		}

		// 2.判断是否为该工作或上级工作的负责人或项目的项目经理
		return hasPermission(context);
	}

	/**
	 * 能否点击删除
	 */
	@Override
	public boolean canDelete(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停、进行中)，返回false
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
		// 2.判断是否为该工作或上级工作的负责人或项目的项目经理
		return hasPermission(context);
	}

	/**
	 * 能否编辑工作记录
	 * 
	 * @param context
	 * @return
	 */
	public boolean canEditWorkRecord(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，进行中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停)，返回false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. 当是摘要工作时，返回false
		if (isSummaryWork()) {
			return false;
		}

		// 3.判断是否为该工作或上级工作的负责人或项目的项目经理
		return hasPermission(context);
	}

	/**
	 * 能否添加工作
	 * 
	 * @param context
	 * @return
	 */
	public boolean canCreateChildWork(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，进行中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停)，返回false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. 当是摘要工作时，是否设置了允许分解，如果没有，返回false
		if (!isSummaryWork()
				&& !Boolean.TRUE.equals(getValue(F_S_CANBREAKDOWN))) {
			return false;
		}

		// 3.判断是否为该工作或上级工作的负责人或项目的项目经理
		return hasPermission(context);
	}

	/**
	 * 能否添加交付物
	 * 
	 * @param context
	 * @return
	 */
	public boolean canCreateDeliverable(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，进行中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停)，返回false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2. 当是摘要工作时，返回false
		if (isSummaryWork()) {
			return false;
		}

		// 3.如果设置了不能添加交付物，返回假
		if (!Boolean.TRUE.equals(getValue(F_S_CANADDDELIVERABLES))) {
			return false;
		}

		// 4.判断是否为该工作或上级工作的负责人或项目的项目经理
		return hasPermission(context);
	}

	/**
	 * 能否进行工作指派
	 * 
	 * @param context
	 * @return
	 */
	public boolean canReassignment(IContext context) {
		// 1.首先检查本工作生命周期状态是否符合:准备中，进行中，无状态，
		// 如果不是这些状态(已完成、已取消、已暂停)，返回false
		String lifeCycle = (String) getValue(F_LIFECYCLE);
		if (STATUS_CANCELED_VALUE.equals(lifeCycle)
				|| STATUS_FINIHED_VALUE.equals(lifeCycle)
				|| STATUS_PAUSED_VALUE.equals(lifeCycle)) {
			return false;
		}

		// 2.判断是否为该工作或上级工作的指派者
		return hasPermissionForReassignment(context);
	}

	/**
	 * 检查工作是否可以启动
	 * 
	 * @param context
	 * @throws Exception
	 */
	public List<Object[]> checkStartAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.判断是否是本级的负责人
		// 如果不在进行中，返回false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("不是本项目负责人，" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("项目不在进行中，" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("不是本工作负责人，" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("上级工作不在进行中，" + this);
				}
			}
		}

		// 3.判断本工作及其下级工作的必要信息是否录入
		message.addAll(checkCascadeStart());
		return message;
	}

	@Override
	public List<Object[]> checkCancelAction(IContext context) throws Exception {
		// 先判断是否顶级工作
		// 1.判断是否是本级的负责人
		// 2.判断上级工作生命周期状态是否符合：进行中
		// 如果不在进行中，返回false
		String userId = context.getAccountInfo().getConsignerId();
		Project project = getProject();
		if (isProjectWBSRoot()) {
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("不是本项目负责人，" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("项目不在进行中，" + this);
				}
			}
		} else {
			// 1.判断是否是本级的负责人
			Work parent = (Work) getParent();
			if (parent != null) {
				if (!STATUS_WIP_VALUE.equals(parent.getLifecycleStatus())) {
					throw new Exception("上级工作不在进行中，" + this);
				}
				if (parent.isProjectWBSRoot()) {
					if (!userId.equals(project.getChargerId())) {
						throw new Exception("不是本项目负责人，" + this);
					}
				} else {
					if (parent.hasPermission(context)) {
						throw new Exception("不是工作负责人，" + parent);
					}
				}
			} else {
				if (project != null) {
					if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
						throw new Exception("项目不在进行中，" + this);
					}
				}
				throw new Exception("本工作不能取消，" + this);
			}

			if (isMandatory()) {
				throw new Exception("本工作不能取消，" + this);
			}
		}
		return null;
	}

	/**
	 * 检查工作是否可以完成
	 * 
	 * @param context
	 * @throws Exception
	 */
	@Override
	public List<Object[]> checkFinishAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 先判断是否顶级工作
		// 1.判断是否是本级的负责人
		// 2.判断上级工作生命周期状态是否符合：进行中
		// 如果不在进行中，返回false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("不是本项目负责人，" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("项目不在进行中，" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("不是本工作负责人，" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("上级工作不在进行中，" + this);
				}
			}
		}

		// 3.判断下级级联完成的工作是否可以完成，非级联完成的工作是否已经在已完成状态或已取消状态
		message.addAll(checkCascadeFinish(get_id()));
		return message;

	}

	@Override
	public List<Object[]> checkPauseAction(IContext context) throws Exception {
		// 先判断是否顶级工作
		// 1.判断是否是本级的负责人
		// 2.判断上级工作生命周期状态是否符合：进行中
		// 如果不在进行中，返回false
		String userId = context.getAccountInfo().getConsignerId();
		if (isProjectWBSRoot()) {
			Project project = getProject();
			if (!userId.equals(project.getChargerId())) {
				throw new Exception("不是本项目负责人，" + this);
			}

			if (project != null) {
				if (!STATUS_WIP_VALUE.equals(project.getLifecycleStatus())) {
					throw new Exception("项目不在进行中，" + this);
				}
			}
		} else {
			if (!userId.equals(getChargerId())) {
				throw new Exception("不是本工作负责人，" + this);
			}
			Work parentWork = (Work) getParent();
			if (parentWork != null) {
				if (!STATUS_WIP_VALUE.equals(parentWork.getLifecycleStatus())) {
					throw new Exception("上级工作不在进行中，" + this);
				}
			}
		}
		return null;
	}

	/**
	 * 计算工期
	 * 
	 * @param fStart
	 *            ,开始日期
	 * @param fFinish
	 *            ,完成日期
	 * @param fDuration
	 *            ,工期
	 * @throws Exception
	 */
	public void checkAndCalculateDuration(String fStart, String fFinish,
			String fDuration) throws Exception {
		// TODO 增加检测 工作的开始时间不能早于项目的开始时间，结束时间不能晚于项目的结束时间
		Date start = (Date) getValue(fStart);
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish = (Date) getValue(fFinish);
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start != null && finish != null) {
			// 检查是否合法
			if (start.after(finish)) {
				throw new Exception("开始日期必须早于完成日期");
			}
			// 计算工期
			Calendar sdate = Utils.getDayBegin(start);
			Calendar edate = Utils.getDayEnd(finish);
			long l = (edate.getTimeInMillis() - sdate.getTimeInMillis())
					/ (1000 * 60 * 60 * 24);
			setValue(fDuration, new Integer((int) l));
		}
	}

	/**
	 * 项目检查
	 * 
	 * @return
	 */
	public List<ICheckListItem> checkPlan() {
		Project project = getProject();
		Map<ObjectId, List<PrimaryObject>> ram = project.getRoleAssignmentMap();
		return checkPlan(project, ram);
	}

	/**
	 * 1. 检查工作的计划时间：错误，没有设定计划开始、计划完成、计划工时的叶工作 <br/>
	 * 2. 检查工作的负责人 ：错误，没有设定负责人，而且没有设定指派者的叶工作 <br/>
	 * 3. 工作的流程设定 ：警告，没有指明流程执行者的工作 <br/>
	 * 4. 交付物检查 <br/>
	 * 4.1. 检查工作是否具有交付物：警告，没有交付物的叶工作 4.2. 检查交付物文档没有电子文件作为模板：警告
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
		if (childrenWork.size() > 0) {// 如果有下级，直接返回下级的检查结果
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				result.addAll(childWork.checkPlan(project, roleMap));
			}
		} else {
			// ****************************************************************************************
			// 1 检查工作的计划开始和计划完成
			Object value = getPlanStart();
			boolean passed = true;
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划开始没有确定", "请在提交前确定。", ICheckListItem.ERROR);
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
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划完成时间没有确定", "请在提交前确定。", ICheckListItem.ERROR);
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
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划工时没有确定", "不确定该计划工时表示该工作不计算工时，如果您并不希望这样，请在提交前确定。",
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
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作名称为空", "请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_DESC);
				result.add(checkItem);
			}

			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			// ****************************************************************************************
			// 2 检查负责人
			value = getCharger();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人",
						"工作负责人为空", "请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_CHARGER);
				result.add(checkItem);
				passed = false;
			}

			// 3.检查参与者
			value = getParticipatesIdList();
			if (value == null || ((BasicBSONList) value).isEmpty()) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人",
						"没有添加工作参与者", "请在提交前确定。", ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
			// 4.1 检查工作变更的流程 ：错误，没有指明流程负责人
			String title = "检查工作变更流程";
			String process = F_WF_CHANGE;
			String editorId = Project.EDITOR_CREATE_PLAN;
			String pageId = Project.EDITOR_PAGE_WBS;
			passed = ProjectToolkit.checkProcessInternal(project, pc, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			// 4.2 检查项目提交的流程 ：错误，没有指明流程负责人
			title = "检查工作执行流程";
			process = F_WF_EXECUTE;
			passed = ProjectToolkit.checkProcessInternal(project, pc, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			passed = true;
			// 检查工作交付物
			List<PrimaryObject> docs = getDeliverableDocuments();
			if (docs.isEmpty()) {
				CheckListItem checkItem = new CheckListItem("检查交付物",
						"该工作没有设定交付物", "提交前如果不设定，将由工作执行后由执行者自行添加，请在提交前确定。",
						ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查交付物");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

		}

		return result;
	}

	/**
	 * 项目的工作检查必要信息是否录入
	 * 
	 * @return
	 */
	public List<Object[]> checkCascadeStart() {
		List<Object[]> message = new ArrayList<Object[]>();
		// 检查下级工作，非摘要工作不处理
		List<PrimaryObject> childrenWork = getChildrenWork();
		if (childrenWork.size() > 0) {// 如果有下级，返回下级的检查结果
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				message.addAll(childWork.checkCascadeStart());
			}
		} else {
			// 1.检查工作的计划开始和计划完成
			Object value = getPlanStart();
			if (value == null) {
				message.add(new Object[] { "工作的计划开始时间没有确定", this,
						SWT.ICON_ERROR, EDIT_WORK_PLAN_0 });
			}
			value = getPlanFinish();
			if (value == null) {
				message.add(new Object[] { "工作的计划完成时间没有确定", this,
						SWT.ICON_ERROR, EDIT_WORK_PLAN_0 });
			}
			// 2.检查工作的计划工时
			value = getPlanWorks();
			if (value == null) {
				message.add(new Object[] { "工作的计划工时没有确定", this, SWT.ICON_ERROR,
						EDIT_WORK_PLAN_0 });
			}
			// 3.检查工作名称
			value = getDesc();
			if (Utils.isNullOrEmptyString(value)) {
				message.add(new Object[] { "工作名称为空", this, SWT.ICON_ERROR,
						EDIT_WORK_PLAN_0 });
			}
			// 4.检查负责人
			value = getCharger();
			if (value == null) {
				message.add(new Object[] { "工作负责人为空", this, SWT.ICON_ERROR,
						EDIT_WORK_PLAN_0 });
			}
			// 5.检查参与者
			value = getParticipatesIdList();
			if (!(value instanceof List) || ((List<?>) value).isEmpty()) {
				message.add(new Object[] { "没有添加工作参与者", this, SWT.ICON_WARNING,
						EDIT_WORK_PLAN_0 });
			}

			// // 6.1.检查工作变更的流程 ：错误，没有指明流程负责人
			// String process = F_WF_CHANGE;
			// if (!ProjectToolkit.checkProcessInternal(this, process)) {
			// throw new Exception("该工作变更流程没有指明流程负责人，" + this);
			// }

			IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

			// 6.2.检查工作执行的流程 ：错误，没有指明流程负责人
			if (!ProjectToolkit.checkProcessInternal(pc, F_WF_EXECUTE)) {
				message.add(new Object[] { "该工作执行流程没有没有指明流程负责人", this,
						SWT.ICON_WARNING, EDIT_WORK_PLAN_1 });
			}

			// 7.检查工作交付物,警告
			List<PrimaryObject> docs = getDeliverableDocuments();
			if (docs.isEmpty()) {
				message.add(new Object[] { "该工作没有设定交付物", this,
						SWT.ICON_WARNING, EDITOR });
			}
		}
		return message;
	}

	/**
	 * 检查下级级联完成的工作是否可以完成，非级联完成的工作是否已经在已完成状态或已取消状态
	 * 
	 * @param id
	 * @return
	 */
	public List<Object[]> checkCascadeFinish(ObjectId id) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.判断非级联完成的工作是否已经在已完成状态、已取消、准备中、无状态的状态
		DBObject condition = new BasicDBObject();
		condition.put(F_PARENT_ID, id);
		condition.put(
				F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						STATUS_PAUSED_VALUE, STATUS_WIP_VALUE }));
		condition.put(F_S_AUTOFINISHWITHPARENT,
				new BasicDBObject().append("$ne", Boolean.TRUE));
		long count = getRelationCountByCondition(Work.class, condition);
		if (count > 0) {
			message.add(new Object[] { "非级联完成的下级工作未完成或取消", this,
					SWT.ICON_ERROR, EDITOR });
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 2.循环得到下级级联的暂停和进行中状态的工作,
		// 2.1判断取出工作是否可以完成，判断其是否可以跳过流程完成工作
		// 2.2判断取出工作的下级非级联完成的工作是否可以完成
		condition.put(F_S_AUTOFINISHWITHPARENT, Boolean.TRUE);
		List<PrimaryObject> childrenWork = getRelationByCondition(Work.class,
				condition);
		if (childrenWork.size() > 0) {
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				if (pc.isWorkflowActivate(F_WF_EXECUTE)
						&& !Boolean.TRUE.equals(childWork
								.getValue(F_S_CANSKIPTOFINISH))) {
					message.add(new Object[] { "存在无法跳过进行中的流程完成的下级级联完成工作", this,
							SWT.ICON_ERROR, EDITOR });
				}
				message.addAll(checkCascadeFinish(childWork.get_id()));
			}
		}
		return message;
	}

	/**
	 * 是否有权限进行指派，该工作或上级工作的指派者时有权限
	 * 
	 * @param context
	 * @return
	 */
	private boolean hasPermissionForReassignment(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		// 判断是否是本级的指派者
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
	 * 是否有权限创建子工作、创建交付物、编辑当前工作 该工作或上级工作的负责人或项目的项目经理时有权限
	 * 
	 * @param context
	 * @return
	 */
	public boolean hasPermission(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		// 判断是否是本级的负责人
		if (userId.equals(getChargerId())) {
			return true;
		} else {
			return parentPermission(context, userId);
		}
	}

	public boolean parentPermission(IContext context, String userId) {
		Work parent = (Work) getParent();
		if (parent != null) {
			return parent.hasPermission(context);
		} else {
			// 是Root工作，判断是否是项目经理
			Project project = getProject();
			if (project != null) {
				return userId.equals(project.getChargerId());
			} else {
				return false;
			}
		}
	}

	/**
	 * 返回所有上级共组
	 * 
	 * @return List
	 */
	public List<Work> getAllParents() {
		List<Work> result = new ArrayList<Work>();
		result.add(this);
		Work parent = (Work) getParent();
		while (parent != null) {
			result.add(parent);
			parent = (Work) parent.getParent();
		}
		return result;
	}

	/**
	 * 返回流程执行者
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
				if (assignments != null && !assignments.isEmpty()) {
					// String[] actorList = new String[assignments.size()];
					// for (int j = 0; j < assignments.size(); j++) {
					// assItem = (AbstractRoleAssignment) assignments.get(j);
					// userid = assItem.getUserid();
					// actorList[j] = userid;
					// }
					// wfRoleActors.put(actionName, actorList);

					// 只考虑指派一个人
					assItem = (AbstractRoleAssignment) assignments.get(0);
					userid = assItem.getUserid();
					wfRoleActors.put(actionName, userid);
				}
			}
		}
		return wfRoleActors;
	}

	/**
	 * 返回工作的负责人角色
	 * 
	 * @return ProjectRole
	 */
	public ProjectRole getChargerRoleDefinition() {
		return getChargerRoleDefinition(ProjectRole.class);
	}

	/**
	 * 获取工作对应的交付物
	 * 
	 * @return
	 */
	public List<PrimaryObject> getDeliverable() {
		return getRelationById(F__ID, Deliverable.F_WORK_ID, Deliverable.class);
	}

	/**
	 * 获取工作对应的交付物文档
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
	 * 获取负责人
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
	 * 获取负责人ID
	 * 
	 * @return
	 */
	public String getChargerId() {
		return (String) getValue(F_CHARGER);
	}

	/**
	 * 获取指派人ID
	 * 
	 * @return
	 */
	public String getAssignerId() {
		return (String) getValue(F_ASSIGNER);
	}

	/**
	 * 获取工作承担者
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
	 * 发送到工作的负责人、参与者、指派者（消息需要关联到工作）<br/>
	 * 发送消息将考虑合并消息，发送至同一人的同一类型的消息应当整合为一条<br/>
	 * 
	 * @param messageList
	 *            , 传入待发生的消息列表，如有相同用户的可以合并
	 * @param context
	 * @return
	 */
	public Map<String, Message> getCommitMessage(
			Map<String, Message> messageList, String title, IContext context) {
		// 1. 取工作负责人
		appendMessageForCharger(messageList, title, context);

		// 2. 取工作指派者
		appendMessageForAssigner(messageList, title, context);

		// 3. 获取参与者
		appendMessageForParticipate(messageList, title, context);

		// 4. 获取流程的执行人
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
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "负责工作"
				+ ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForAssigner(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title,
				"为工作指派负责人和参与者，工作" + ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForParticipate(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "参与工作"
				+ ": " + getLabel(), this, EDITOR, context);
	}

	public void appendMessageForExecuteWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_EXECUTE, "执行流程", title, context.getAccountInfo()
						.getConsignerId(), null);
	}

	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_CHANGE, "变更流程", title, context.getAccountInfo()
						.getConsignerId(), null);
	}

	/**
	 * 绑定工作流定义
	 * 
	 * @param workflowKey
	 *            ,工作流关键字，执行流程或者是变更流程 {@link #F_WF_EXECUTE} {@link #F_WF_CHANGE}
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
	 * 保存工作
	 * 
	 * @return boolean
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {
		// if (calendarCaculater == null) {
		// calendarCaculater = new CalendarCaculater(getProjectId());
		// }

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

		super.doSave(context);

		return true;

	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		// 同步负责人、流程活动执行人到工作的参与者。
		ensureParticipatesConsistency();
		super.doUpdate(context);
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		ObjectId id = new ObjectId();
		setValue(F__ID, id);

		if (getValue(F_PARENT_ID) == null) {// 根工作
			setValue(F_ROOT_ID, id);
		} else {
			AbstractWork parent = getParent();
			ObjectId rootId = (ObjectId) parent.getValue(F_ROOT_ID);
			setValue(F_ROOT_ID, rootId);
		}

		if (isStandloneWork()) {
			copyWorkDefinition(Work.F_WF_EXECUTE, context);

			// 处理文档的复制
			copyDeliveryFromWorkDefinition();
		}

		// 同步负责人、流程活动执行人到工作的参与者。
		ensureParticipatesConsistency();

		// 缺省可以添加交付物
		Object value = getValue(F_S_CANADDDELIVERABLES);
		if (value == null) {
			setValue(F_S_CANADDDELIVERABLES, Boolean.TRUE);
		}

		super.doInsert(context);
	}

	private void copyDeliveryFromWorkDefinition() throws Exception {
		WorkDefinition workdef = getWorkDefinition();
		if (workdef == null) {
			return;
		}

		// 处理文档

		Map<ObjectId, DBObject> documentsToInsert = new HashMap<ObjectId, DBObject>();
		List<DBObject> dilerverableToInsert = new ArrayList<DBObject>();
		List<DBObject[]> fileToCopy = new ArrayList<DBObject[]>();

		DBCollection deliveryDefCol = getCollection(IModelConstants.C_DELIEVERABLE_DEFINITION);
		DBCursor deliCur = deliveryDefCol.find(new BasicDBObject().append(
				DeliverableDefinition.F_WORK_DEFINITION_ID,
				workdef.getValue(WorkDefinition.F__ID)));
		while (deliCur.hasNext()) {
			DBObject delidata = deliCur.next();
			// 根据模板的交付物定义创建交付物关系
			DBObject deliverableData = new BasicDBObject();

			// 设置工作Id
			deliverableData.put(Deliverable.F_WORK_ID, get_id());

			// 获得文档模板
			ObjectId documentTemplateId = (ObjectId) delidata
					.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
			DBObject documentData = copyDocumentFromTemplate(documentsToInsert,
					fileToCopy, documentTemplateId);
			documentsToInsert.put(documentTemplateId, documentData);
			ObjectId documentId = (ObjectId) documentData.get(Document.F__ID);
			deliverableData.put(Deliverable.F_DOCUMENT_ID, documentId);
			dilerverableToInsert.add(deliverableData);
		}

		// 保存文档
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		Collection<DBObject> collection = documentsToInsert.values();
		WriteResult ws;
		if (!collection.isEmpty()) {
			ws = docCol.insert(collection.toArray(new DBObject[0]),
					WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// 保存交付物
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		if (!dilerverableToInsert.isEmpty()) {
			ws = deliCol.insert(dilerverableToInsert, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// 保存文件
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
	 * 仅用于独立工作，从工作定义中复制角色
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private Map<ObjectId, DBObject> copyRoleDefinition(IContext context)
			throws Exception {
		// 准备返回值
		HashMap<ObjectId, DBObject> result = new HashMap<ObjectId, DBObject>();

		WorkDefinition workd = getWorkDefinition();
		if (workd == null) {
			return result;
		}
		ObjectId workDefinitionId = workd.get_id();

		DBCollection col_roled = getCollection(IModelConstants.C_ROLE_DEFINITION);

		// 查找模板的角色定义
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

			// 插入到数据库
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
	 * 确保工作的参与者包括工作的负责人、流程执行人
	 */
	public void ensureParticipatesConsistency() {
		// 获取工作的负责人
		String chargerId = getChargerId();
		if (chargerId != null) {
			addParticipate(chargerId);
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 获得流程的执行人
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
		Assert.isTrue(chargerId != null, "参数不可为空");
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
	 * 为工作及下级工作的负责人,参与者,工作流的执行者指定用户
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

		// 设置负责人
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

		// 设置指派者
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

		// 设置参与者
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

		// 设置变更工作流执行人

		DBObject wfRoleAss = (DBObject) getValue(F_WF_CHANGE_ASSIGNMENT);
		if (wfRoleAss != null) {
			BasicDBObject wfRoleActors = getWorkFlowActors(wfRoleAss,
					roleAssign);
			if (!wfRoleActors.isEmpty()) {
				setValue(F_WF_CHANGE_ACTORS, wfRoleActors);
				modified = true;
			}
		}

		// 设置执行工作流的执行人
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

		// 设置下级
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work child = (Work) children.get(i);
			child.doAssignment(roleAssign, context);
		}
	}

	/**
	 * 新建交付物
	 * 
	 * @param doc
	 *            ,文档
	 * @param context
	 * @return Deliverable
	 * @throws Exception
	 */
	public Deliverable doAddDeliverable(Document doc, IContext context)
			throws Exception {
		Deliverable deli = makeDeliverableDefinition();
		deli.setValue(Deliverable.F_DOCUMENT_ID, doc.get_id());
		deli.doInsert(context);
		return deli;

	}

	/**
	 * 启动工作
	 */
	@SuppressWarnings("unchecked")
	public Object doStart(IContext context) throws Exception {
		// 准备保存的对象
		DBObject update = new BasicDBObject();
		Map<String, Object> params = new HashMap<String, Object>();

		if (!isProjectWBSRoot()) {
			// 判断能否启动，检查状态
			Assert.isTrue(canStart(), "工作的当前状态不能执行启动操作");
			// 调用前处理
			doStartBefore(context, params);

			// 判定是否使用执行工作流
			IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
			if (pc.isWorkflowActivate(F_WF_EXECUTE)) {
				// 如果是，启动工作流
				Workflow wf = pc.getWorkflow(F_WF_EXECUTE);
				DBObject actors = pc.getProcessActorsData(F_WF_EXECUTE);
				Map<String, String> actorParameter = null;
				if (actors != null) {
					actorParameter = actors.toMap();
				}
				ProcessInstance processInstance = wf.startHumanProcess(
						actorParameter, params);
				Assert.isNotNull(processInstance, "流程启动失败");

				update.put(F_WF_EXECUTE + IProcessControl.POSTFIX_INSTANCEID,
						processInstance.getId());
			}
		}

		// 启动下级同步启动的工作
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// 检查下级的工作是否设置了与上级同步启动
			if (Boolean.TRUE.equals(childWork
					.getValue(F_SETTING_AUTOSTART_WHEN_PARENT_START))) {
				// 启动下级工作
				childWork.doStart(context);
			}
		}

		// 标记工作的进行中
		update.put(F_LIFECYCLE, STATUS_WIP_VALUE);
		// 设置工作的实际开始时间
		update.put(F_ACTUAL_START, new Date());

		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		// 提示工作启动
		doNoticeWorkAction(context, "已启动");

		// 调用后处理
		doStartAfter(context, params);

		return null;
	}

	/**
	 * 暂停工作
	 */
	public Object doPause(IContext context) throws Exception {
		Assert.isTrue(canPause(), "工作的当前状态不能执行暂停操作");
		Map<String, Object> params = new HashMap<String, Object>();
		doPauseBefore(context, params);

		DBObject update = new BasicDBObject();

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// 检查下级的工作状态是否为进行中
			if (STATUS_WIP_VALUE.equals(childWork.getValue(F_LIFECYCLE))) {
				// 暂停下级工作
				childWork.doPause(context);
			}
		}

		// 标记工作已暂停
		update.put(F_LIFECYCLE, STATUS_PAUSED_VALUE);
		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);

		set_data(newData);

		// 提示工作已暂停
		doNoticeWorkAction(context, "已暂停");

		// 后处理
		doPauseAfter(context, params);

		return null;

	}

	/**
	 * 取消工作
	 */
	public Object doCancel(IContext context) throws Exception {
		Assert.isTrue(canCancel(), "工作的当前状态不能执行取消操作");
		Map<String, Object> params = new HashMap<String, Object>();
		doCancelBefore(context, params);

		DBObject update = new BasicDBObject();
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// // 检查下级的工作状态是否为进行中或者已暂停
			// if (STATUS_WIP_VALUE.equals(childWork.getValue(F_LIFECYCLE))
			// || STATUS_PAUSED_VALUE.equals(childWork
			// .getValue(F_LIFECYCLE))) {
			// }
			// 取消下级工作
			if (childWork.canCancel()) {
				childWork.doCancel(context);
			}
		}

		// 标记工作已取消
		update.put(F_LIFECYCLE, STATUS_CANCELED_VALUE);

		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		// 提示工作已取消
		doNoticeWorkAction(context, "已取消");
		doCancelAfter(context, params);

		return null;

	}

	public Object doFinish(IContext context) throws Exception {
		Assert.isTrue(canFinish(), "工作的当前状态不能执行完成操作");
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
		 * //查询下级 BasicDBObject queryCondition = new BasicDBObject();
		 * //设置查询条件，该工作的所有下级工作 queryCondition.put(Work.F_PARENT_ID,get_id());
		 * //设置查询条件，该工作所有正在进行中和已暂停的下级工作 queryCondition.put(Work.F_LIFECYCLE,new
		 * BasicDBObject().append("$in", new String[] { Work.STATUS_WIP_VALUE,
		 * Work.STATUS_PAUSED_VALUE})); //查询，返回该工作所有正在进行中的下级工作 DBCursor cur =
		 * col.find(queryCondition); while(cur.hasNext()){ DBObject dbobject =
		 * cur.next(); Work work = ModelService.createModelObject(dbobject,
		 * Work.class); work.doFinish(context);
		 * 
		 * }
		 */

		DBObject update = new BasicDBObject();
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			// 检查下级的工作状态是否为进行中或已暂停
			String childLC = childWork.getLifecycleStatus();
			if (STATUS_WIP_VALUE.equals(childLC)
					|| STATUS_PAUSED_VALUE.equals(childWork
							.getValue(F_LIFECYCLE))) {
				// 完成下级工作
				childWork.doFinish(context);
			} else if (STATUS_CANCELED_VALUE.equals(childLC)
					|| STATUS_FINIHED_VALUE.equals(childLC)) {
			} else {
				// 取消工作
				childWork.doCancel(context);

			}
		}

		// 标记工作已完成
		update.put(F_LIFECYCLE, STATUS_FINIHED_VALUE);
		// 设置工作的实际完成时间
		update.put(F_ACTUAL_FINISH, new Date());
		DBCollection col = getCollection();
		DBObject newData = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", update), true, false);
		set_data(newData);

		// 提示工作已完成
		doNoticeWorkAction(context, "已完成");
		doFinishAfter(context, params);
		return null;

	}

	/**
	 * 发送工作操作完成的通知
	 * 
	 * @param context
	 *            当前的上下文
	 * @param actionName
	 *            操作的文本名称
	 * @return
	 * @throws Exception
	 *             发送消息出现的错误
	 */
	public Message doNoticeWorkAction(IContext context, String actionName)
			throws Exception {
		// 设置收件人
		BasicBSONList participatesIdList = getParticipatesIdList();
		if (participatesIdList == null || participatesIdList.isEmpty()) {
			return null;
		}
		// 排除自己
		participatesIdList.remove(context.getAccountInfo().getConsignerId());

		// 设置通知标题
		String title = "" + this + " " + actionName;

		// 设置通知内容
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>");
		sb.append("您好: ");
		sb.append("</span><br/><br/>");
		sb.append("您参与的工作有新的进展。");
		sb.append("<br/><br/>");

		sb.append(context.getAccountInfo().getUserId() + "|"
				+ context.getAccountInfo().getUserName());
		sb.append(", ");
		sb.append(actionName);
		sb.append("工作");
		sb.append("\"");
		sb.append(this);
		sb.append("\"");
		if (isProjectWork()) {
			sb.append(" \"");
			sb.append("项目:");
			sb.append(getProject());
			sb.append(" \"");
		}

		sb.append("<br/><br/>");
		sb.append("如有不明，请查阅有关工作信息。");

		Message message = MessageToolkit.makeMessage(participatesIdList, title,
				context.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// 设置导航附件
		message.appendTargets(this, EDITOR, false);

		message.doSave(context);

		return message;
	}

	public Message doNoticeWorkflow(String actorId, String taskName,
			String key, String action, IContext context) throws Exception {
		BasicBSONList recievers = getParticipatesIdList();
		if (recievers == null) {
			return null;
		}
		// 排除自己
		recievers.remove(actorId);

		// 设置通知标题
		String title = "" + this + " " + "流程任务" + taskName + " " + action;
		// 设置通知内容
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>");
		sb.append("您好: ");
		sb.append("</span><br/><br/>");
		sb.append("您参与的工作有新的进展。");
		sb.append("<br/><br/>");

		User user = UserToolkit.getUserById(actorId);
		sb.append(user);
		sb.append(", ");
		sb.append(action);
		sb.append("工作");
		sb.append("\"");
		sb.append(this);
		sb.append("\"");
		if (isProjectWork()) {
			sb.append(" \"");
			sb.append("项目:");
			sb.append(getProject());
			sb.append(" \"");
		}
		sb.append("的流程任务: ");
		sb.append("\"");
		sb.append(taskName);
		sb.append("\"。");

		sb.append("<br/><br/>");
		sb.append("如有不明，请查阅有关工作信息和流程历史");

		Message message = MessageToolkit.makeMessage(recievers, title, actorId,
				sb.toString());

		MessageToolkit.appendEndMessage(message);

		// 设置导航附件
		message.appendTargets(this, EDITOR, false);

		message.doSave(context);
		return message;
	}

	@Override
	public BasicBSONList getTargetList() {
		return (BasicBSONList) getValue(F_TARGETS);
	}

	/**
	 * 获得流程某个活动执行者对应的用户
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

	/**
	 * 使用工作流的任务更新当前工作的流程任务信息
	 * 
	 * @param key
	 *            流程字段名
	 * @param task
	 *            任务
	 * @param context
	 * @param
	 * @param context
	 * @return 是否更新
	 * @throws Exception
	 */
	public boolean doUpdateWorkflowDataByTask(String key, Task task,
			String userid) throws Exception {
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		/*
		 * 获得当前用户的现有流程任务数据 * 如果任务id和状态一致，那么就无需继续更新操作
		 */
		DBObject olddata = pc.getCurrentWorkflowTaskData(key, userid, true);
		if (olddata != null) {
			Object oldTaskId = olddata.get(IProcessControl.F_WF_TASK_ID);
			if (task.getId().equals(oldTaskId)) {
				TaskData taskData = task.getTaskData();
				Status status = taskData.getStatus();
				if (status.name().equals(
						olddata.get(IProcessControl.F_WF_TASK_STATUS))) {
					return false;
				}
			}
		}

		/*
		 * 构造需要保存的任务数据
		 */

		DBObject data = new BasicDBObject();

		// 保存任务id
		data.put(IProcessControl.F_WF_TASK_ID, task.getId());

		// 保存任务名称
		List<I18NText> names = task.getNames();
		Assert.isLegal(names != null && names.size() > 0, "流程活动名称没有定义");
		String taskName = names.get(0).getText();
		data.put(IProcessControl.F_WF_TASK_NAME, taskName);

		// 保存任务描述
		List<I18NText> descriptions = task.getDescriptions();
		if (descriptions != null && descriptions.size() > 0) {
			String taskComment = descriptions.get(0).getText();
			data.put(IProcessControl.F_WF_TASK_DESC, taskComment);
		}

		// 保存任务的实际执行人id
		TaskData taskData = task.getTaskData();
		org.jbpm.task.User actualOwner = taskData.getActualOwner();
		String actorId = actualOwner.getId();
		data.put(IProcessControl.F_WF_TASK_ACTUALOWNER, actorId);

		// 保存任务的创建者
		org.jbpm.task.User createdBy = taskData.getCreatedBy();
		data.put(IProcessControl.F_WF_TASK_CREATEDBY, createdBy.getId());

		// 任务的创建时间
		Date createdOn = taskData.getCreatedOn();
		data.put(IProcessControl.F_WF_TASK_CREATEDON, createdOn);

		// 任务的流程定义id
		String processId = taskData.getProcessId();
		data.put(IProcessControl.F_WF_TASK_PROCESSID, processId);

		// 任务的流程实例id
		long processInstanceId = taskData.getProcessInstanceId();
		data.put(IProcessControl.F_WF_TASK_PROCESSINSTANCEID, new Long(
				processInstanceId));

		// 任务状态
		Status status = taskData.getStatus();
		data.put(IProcessControl.F_WF_TASK_STATUS, status.name());

		// 任务的workitem ID
		long workItemId = taskData.getWorkItemId();
		data.put(IProcessControl.F_WF_TASK_WORKITEMID, new Long(workItemId));

		// // 发送任务消息，并保存
		// BackgroundContext context = new BackgroundContext();
		// Message message = doNoticeWorkflow(actorId, taskName, key, context);
		// Assert.isNotNull(message, "消息发送失败");
		// data.put(IProcessControl.F_WF_TASK_NOTICEDATE,
		// message.getValue(Message.F_SENDDATE));

		/*
		 * 由于PrimaryObject对象可能在不同的用户进程中存在多个副本，副本之间数据并不能维护一致。
		 * 
		 * 因此，调用doSave()方法进行保存，将覆盖其他用户的数据。 使用
		 * doSave()方法进行保存适用于后保存的对象覆盖先保存的记录的场景。
		 * 
		 * 
		 * 但对于更新工作流信息而言不适用。因此，必须使用col.updata的方式进行更新，
		 * 
		 * 并且将更新后的结果回写到_data
		 * 
		 * 以下的代码展示如何直接调用col的更新方法
		 * 
		 * 1. 使用 findAndModify获得更新后的对象值
		 * 注意：直接调用col.update是不能获得更新后数据的，进而PrimaryObject ._data的数据就无法同步更新
		 * 
		 * 2. 了解findAndModify完整方法的各项参数
		 */

		// 流程的字段名称
		String field = key + IProcessControl.POSTFIX_TASK;

		// 获得本模型对应的集合
		DBCollection col = getCollection();

		DBObject nd = col
				.findAndModify(
						// 查询条件，与update方法的query一致
						new BasicDBObject().append(F__ID, get_id()),
						// 返回字段，与find方法的返回字段一致
						new BasicDBObject().append(field, 1),
						// 排序，与find方法的排序一致
						null,
						// 删除，如果为真，满足查询条件的将被删除
						false,
						// 更新条件，与update的更新一致
						new BasicDBObject().append("$set", new BasicDBObject()
								.append(field + "." + userid, data)),
						// 是否返回更新后的对象
						true,
						// 如果文档不存在是否插入
						false);

		// 新的对象不能为空
		Assert.isNotNull(nd);

		// 使用获得的新对象刷新_data的值
		Object value = nd.get(field);

		setValue(field, value);

		return true;
	}

	public boolean isProjectWBSRoot() {
		return Boolean.TRUE.equals(getValue(F_IS_PROJECT_WBSROOT));
	}

	/**
	 * 启动processKey指定流程的当前任务
	 * 
	 * @param processKey
	 *            ，流程key 目前只有{@link IWorkCloneFields#F_WF_EXECUTE}
	 *            {@link IWorkCloneFields#F_WF_CHANGE}<br/>
	 *            可以支持绑定更多的流程定义
	 * @param context
	 */
	public void doStartTask(String processKey, IContext context)
			throws Exception {
		String lc = getLifecycleStatus();
		Assert.isTrue(ILifecycle.STATUS_WIP_VALUE.equals(lc), "工作当前状态不允许执行流程操作");

		Task task = getTask(processKey, context);
		Assert.isNotNull(task, "无法获得当前的流程任务");

		Status taskstatus = task.getTaskData().getStatus();
		boolean canStartTask = WorkflowService.canStartTask(taskstatus.name());
		Assert.isTrue(canStartTask, "任务当前的状态不允许执行开始");

		Long taskId = task.getId();
		String userId = context.getAccountInfo().getConsignerId();
		task = WorkflowService.getDefault().startTask(userId, taskId);

		Assert.isNotNull(task, "开始流程任务失败");

		// 提取当前的任务数据
		Map<String, Object> taskFormData = new HashMap<String, Object>();
		taskFormData.put(IProcessControl.F_WF_TASK_ACTOR, context
				.getAccountInfo().getUserId());
		taskFormData.put(IProcessControl.F_WF_TASK_STARTDATE, new Date());
		taskFormData.put(IProcessControl.F_WF_TASK_ACTION,
				IProcessControl.TASK_ACTION_START);

		doSaveWorkflowHistroy(processKey, task, taskFormData, context);

		// 发送任务消息，并保存

		List<I18NText> names = task.getNames();
		String taskName = "";
		if (names != null && names.size() > 0) {
			taskName = names.get(0).getText();
		}
		doNoticeWorkflow(userId, taskName, processKey, "已启动", context);
		// data.put(IProcessControl.F_WF_TASK_NOTICEDATE,
		// message.getValue(Message.F_SENDDATE));
	}

	/**
	 * 完成processKey指定流程的当前任务
	 * 
	 * @param processKey
	 *            ，流程key 目前只有{@link IWorkCloneFields#F_WF_EXECUTE}
	 *            {@link IWorkCloneFields#F_WF_CHANGE}<br/>
	 *            可以支持绑定更多的流程定义
	 * @param taskFormData
	 * @param context
	 * @throws Exception
	 */
	public void doCompleteTask(String processKey,
			Map<String, Object> inputParameter,
			Map<String, Object> taskFormData, IContext context)
			throws Exception {
		String lc = getLifecycleStatus();
		Assert.isTrue(ILifecycle.STATUS_WIP_VALUE.equals(lc), "工作当前状态不允许执行流程操作");

		Task task = getTask(processKey, context);
		Assert.isNotNull(task, "无法获得当前的流程任务");

		Status taskstatus = task.getTaskData().getStatus();
		boolean canStartTask = WorkflowService.canFinishTask(taskstatus.name());
		Assert.isTrue(canStartTask, "任务当前的状态不允许执行完成");

		Long taskId = task.getId();
		String userId = context.getAccountInfo().getConsignerId();
		task = WorkflowService.getDefault().completeTask(taskId, userId,
				inputParameter);

		Assert.isNotNull(task, "完成流程任务失败");

		taskFormData.put(IProcessControl.F_WF_TASK_ACTOR, context
				.getAccountInfo().getUserId());
		taskFormData.put(IProcessControl.F_WF_TASK_FINISHDATE, new Date());
		taskFormData.put(IProcessControl.F_WF_TASK_ACTION,
				IProcessControl.TASK_ACTION_COMPLETE);

		doSaveWorkflowHistroy(processKey, task, taskFormData, context);

		// 发送任务消息，并保存

		List<I18NText> names = task.getNames();
		String taskName = "";
		if (names != null && names.size() > 0) {
			taskName = names.get(0).getText();
		}
		doNoticeWorkflow(userId, taskName, processKey, "已完成", context);
	}

	public Task getTask(String processKey, IContext context) throws Exception {
		String userid = context.getAccountInfo().getConsignerId();
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		DBObject data = pc.getCurrentWorkflowTaskData(processKey, userid, true);
		if (data != null) {
			Long taskId = (Long) data.get(IProcessControl.F_WF_TASK_ID);
			Assert.isNotNull(taskId);
			Task task = WorkflowService.getDefault()
					.getUserTask(userid, taskId);
			return task;
		}
		return null;
	}

	/**
	 * 保存流程的历史记录
	 * 
	 * @param key
	 * @param task
	 * @param taskFormData
	 * @param context
	 * @throws Exception
	 */
	private void doSaveWorkflowHistroy(String key, Task task,
			Map<String, Object> taskFormData, IContext context)
			throws Exception {
		String userid = context.getAccountInfo().getConsignerId();

		// 取出当前的任务数据
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		DBObject currentData = pc.getCurrentWorkflowTaskData(key, userid, true);

		// 将taskformData补充到当前的任务数据中
		if (taskFormData != null && !taskFormData.isEmpty()) {
			Iterator<String> iterator = taskFormData.keySet().iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				String field = "form_" + next;
				currentData.put(field, taskFormData.get(next));
			}
		}

		// 将当前的任务数据append到历史数组中
		String histroyField = key + IProcessControl.POSTFIX_HISTORY;

		DBCollection col = getCollection();
		WriteResult wr = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$push",
						new BasicDBObject().append(histroyField, currentData)));

		checkWriteResult(wr);

		doUpdateWorkflowDataByTask(key, task, userid);
	}

	/**
	 * 创建任务表单对象
	 * 
	 * @return
	 */
	public TaskForm makeTaskForm() {
		DBObject data = get_data();
		TaskForm taskForm = ModelService.createModelObject(TaskForm.class);
		Iterator<String> iter = data.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (!Utils.inArray(key, SYSTEM_RESERVED_FIELDS)) {
				taskForm.setValue(key, data.get(key));
			}
		}
		taskForm.setValue(TaskForm.F_WORK_ID, get_id());
		return taskForm;
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
						// 由项目计划构造的提交工作是独立工作，但是使用了项目的角色
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
	 * 修改工作负责人、指派者、参与者和工作流程执行者
	 * 
	 * @param fromUserId
	 *            : 需要修改的人员
	 * @param toUserId
	 *            : 修改成该人员
	 */
	public String changeWorkUser(String fromUserId, String toUserId) {
		if (canChangeWorkUser(fromUserId, toUserId)) {
			String changeFiled = "";
			BasicDBObject object = new BasicDBObject();
			// 修改负责人
			if (fromUserId.equals(getChargerId())) {
				object.put(F_CHARGER, toUserId);
				changeFiled = changeFiled + "负责人";
			}
			// 修改指派者
			if (fromUserId.equals(getAssignerId())) {
				object.put(F_ASSIGNER, toUserId);
				if (changeFiled != "") {
					changeFiled = changeFiled + "、";
				}
				changeFiled = changeFiled + "指派者";
			}
			// 修改参与者
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
						changeFiled = changeFiled + "、";
					}
					changeFiled = changeFiled + "参与者";
				}
			}

			// 工作流程执行人
			// 执行工作流程
			if (changeWorkFlowActors(fromUserId, toUserId, F_WF_EXECUTE, object)) {
				if (changeFiled != "") {
					changeFiled = changeFiled + "、";
				}
				changeFiled = changeFiled + "工作执行流程执行者";
			}

			// 变更工作流程
			if (changeWorkFlowActors(fromUserId, toUserId, F_WF_CHANGE, object)) {
				if (changeFiled != "") {
					changeFiled = changeFiled + "、";
				}
				changeFiled = changeFiled + "工作变更流程执行者";
			}

			if (object.size() > 0) {
				DBCollection userCol = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_WORK);
				userCol.update(new BasicDBObject().append(F__ID, get_id()),
						new BasicDBObject().append("$set", object), false, true);
			}
			if (changeFiled != "") {
				return "\"" + getDesc() + "\"工作的" + changeFiled;
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
	 * 检查能否修改该工作
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
			message.add(new Object[] { "工作已经取消，无法进行修改", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { "工作已经完成，无法进行修改", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "工作在进行中，不会修改工作流程执行人", this,
					SWT.ICON_WARNING });
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "工作已经暂停，不会修改工作流程执行人", this,
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

	public boolean isDelayNow() {
		Date now = new Date();
		Date _planFinish = getPlanFinish();
		return _planFinish != null && now.after(_planFinish);
	}
	
	public boolean isDelayed(){
		Date _planFinish = getPlanFinish();
		Date _actualFinish = getActualFinish();
		if(_actualFinish!=null){
			return _actualFinish.after(_planFinish);
		}else{
			return new Date().after(_planFinish);
		}
	}

	public boolean isStandloneWork() {
		Object type = getValue(F_WORK_TYPE);
		return type instanceof Integer
				&& ((Integer) type).intValue() == WORK_TYPE_STANDLONE;
	}

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

		// 处理角色定义
		DBObject radata = ipc.getProcessRoleAssignmentData(key);
		Iterator<String> iterator = radata.keySet().iterator();
		while (iterator.hasNext()) {
			String parameter = iterator.next();
			ObjectId value = (ObjectId) radata.get(parameter);
			DBObject newRoleDef = rolemap.get(value);
			if (newRoleDef != null) {
				RoleDefinition rd = ModelService.createModelObject(newRoleDef,
						RoleDefinition.class);
				ipc.setProcessActionAssignment(key, parameter, rd);
			}
		}

		// 处理用户设置
		DBObject acdata = ipc.getProcessActorsData(key);
		if (acdata == null) {
			radata = ipc.getProcessRoleAssignmentData(key);
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

		// 根据文档的附件创建文件
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
		// 完成文档创建
		documentsToInsert.put(documentTemplateId, documentData);

		return documentData;
	}

	/**
	 * 通过通用工作定义创建新工作
	 * 
	 * @param workd
	 * @param context
	 * @throws Exception
	 */
	public void doCreateChildFromGenericWorkDefinition(WorkDefinition workdef,
			IContext context) throws Exception {
		// 1.处理workd
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
						project.get_id(), seq, workdef.get_data(), null);
		worksToBeInsert.put(srcParent, targetParentWorkData);
		tgtParentId = (ObjectId) targetParentWorkData.get(F__ID);

		ProjectToolkit.copyWBSTemplate(srcParent, tgtParentId, tgtRootId,
				project, roleMap, worksToBeInsert, folderRootId,
				documentsToInsert, deliverableToInsert, fileToCopy, context);

		// 保存工作
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		Collection<DBObject> collection = worksToBeInsert.values();
		WriteResult ws;
		if (!collection.isEmpty()) {
			ws = workCol.insert(collection.toArray(new DBObject[0]),
					WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// 保存文档
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

		// 保存交付物
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = deliCol.remove(new BasicDBObject().append(
				Deliverable.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		if (!deliverableToInsert.isEmpty()) {
			ws = deliCol.insert(deliverableToInsert, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		// 保存文件
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
	 * 获得超量分配的倍速
	 * @return
	 */
	public float getOverloadCount() {
		if(!isProjectWork()){
			return 0f;
		}
		BasicBSONList idlist = getParticipatesIdList();
		if(idlist==null||idlist.size()<1){
			return 0f;
		}
//		getPlanWorks()
		
		
		//获取计划工作天数
		Date planStart = getPlanStart();
		Date planFinih = getPlanFinish();
		
		CalendarCaculater cc = getProject().getCalendarCaculater();
		double hours = cc.getWorkingHours(planStart, planFinih);
		//获得满额工时
		double totalWorkHourAvailabel = hours*idlist.size();
		//
		
		
		// TODO Auto-generated method stub
		return 0;
	}
}
