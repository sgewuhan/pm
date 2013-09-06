package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.drools.runtime.process.ProcessInstance;
import org.eclipse.core.runtime.Assert;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;

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
		IProcessControlable, ILifecycle, IReferenceContainer {

	/**
	 * 工作的编辑器ID
	 */
	public static final String EDITOR = "editor.work";

	/**
	 * 工作设置的编辑器ID
	 */
	public static final String EDITOR_SETTING = "editor.work.setting";

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
	 * @param column
	 *            ,工作的属性
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String column, IContext context) {
		return true;
	}

	public boolean canCheck() {
		// 未完成和未取消的
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))
				&& (!STATUS_FINIHED_VALUE.equals(lc));
	}

	public boolean canCommit() {
		String lc = getLifecycleStatus();

		return STATUS_NONE_VALUE.equals(lc);
	}

	public boolean canStart() {
		String lc = getLifecycleStatus();
		return STATUS_ONREADY_VALUE.equals(lc)
				|| STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canPause() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc);
	}

	public boolean canFinish() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canCancel() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
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

	public List<PrimaryObject> getDeliverable() {
		return getRelationById(F__ID, Deliverable.F_WORK_ID, Deliverable.class);
	}

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

			// 4.1 检查工作变更的流程 ：错误，没有指明流程负责人
			String title = "检查工作变更流程";
			String process = F_WF_CHANGE;
			String editorId = Project.EDITOR_CREATE_PLAN;
			String pageId = Project.EDITOR_PAGE_WBS;
			passed = ProjectToolkit.checkProcessInternal(project, this, result,
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
			passed = ProjectToolkit.checkProcessInternal(project, this, result,
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

	public User getCharger() {
		String chargerId = getChargerId();
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public String getChargerId() {
		return (String) getValue(F_CHARGER);
	}

	public String getAssignerId() {
		return (String) getValue(F_ASSIGNER);
	}

	public BasicBSONList getParticipatesIdList() {
		return (BasicBSONList) getValue(F_PARTICIPATE);
	}

	@Override
	public boolean isWorkflowActivate(String fieldKey) {
		return Boolean.TRUE.equals(getValue(fieldKey + POSTFIX_ACTIVATED));
	}

	@Override
	public DroolsProcessDefinition getProcessDefinition(String fieldKey) {
		DBObject processData = (DBObject) getValue(fieldKey);
		if (processData != null) {
			return new DroolsProcessDefinition(processData);
		}
		return null;
	}

	@Override
	public String getProcessActionActor(String key, String nodeActorParameter) {
		DBObject data = getProcessActorsMap(key);
		if (data == null) {
			return null;
		}
		return (String) data.get(nodeActorParameter);
	}

	public DBObject getProcessActorsMap(String key) {
		return (DBObject) getValue(key + POSTFIX_ACTORS);
	}

	public DBObject getProcessAssignmentRoleMap(String key) {
		return (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
	}

	@Override
	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// 取出角色指派
		DBObject data = getProcessAssignmentRoleMap(key);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
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
	 * @return
	 */
	public Map<String, Message> getCommitMessage(
			Map<String, Message> messageList) {
		// 1. 取工作负责人
		appendMessageForCharger(messageList);

		// 2. 取工作指派者
		appendMessageForAssigner(messageList);

		// 3. 获取参与者
		appendMessageForParticipate(messageList);

		// 4. 获取流程的执行人
		appendMessageForChangeWorkflowActor(messageList);

		appendMessageForExecuteWorkflowActor(messageList);

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childwork = (Work) children.get(i);
			childwork.getCommitMessage(messageList);
		}
		return messageList;
	}

	public void appendMessageForCharger(Map<String, Message> messageList) {
		Message message;
		String userId = getChargerId();
		if (userId != null) {
			message = messageList.get(userId);
			if (message == null) {
				message = MessageToolkit.createProjectCommitMessage(userId);
				messageList.put(userId, message);
			}
			MessageToolkit.appendMessageContent(message, "负责工作" + ": " + getLabel());
			message.appendTargets(this, EDITOR, Boolean.TRUE);
			messageList.put(userId, message);
		}
	}

	public void appendMessageForAssigner(Map<String, Message> messageList) {
		Message message;
		String userId;
		userId = getAssignerId();
		if (userId != null) {
			message = messageList.get(userId);
			if (message == null) {
				message = MessageToolkit.createProjectCommitMessage(userId);
				messageList.put(userId, message);
			}
			MessageToolkit.appendMessageContent(message, "为工作指派负责人和参与者，工作" + ": "
					+ getLabel());
			message.appendTargets(this, EDITOR, Boolean.TRUE);
			messageList.put(userId, message);
		}
	}

	public void appendMessageForParticipate(Map<String, Message> messageList) {
		Message message;
		String userId;
		List<?> userIdList = getParticipatesIdList();
		if (userIdList != null) {
			for (int i = 0; i < userIdList.size(); i++) {
				userId = (String) userIdList.get(i);
				message = messageList.get(userId);
				if (message == null) {
					message = MessageToolkit.createProjectCommitMessage(userId);
					messageList.put(userId, message);
				}
				MessageToolkit.appendMessageContent(message, "参与工作" + ": "
						+ getLabel());
				message.appendTargets(this, EDITOR, Boolean.TRUE);
			}
		}
	}

	public void appendMessageForExecuteWorkflowActor(
			Map<String, Message> messageList) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList, F_WF_EXECUTE,
				"执行流程");
	}

	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList, F_WF_CHANGE,
				"变更流程");
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
		setValue(workflowKey + POSTFIX_ACTIVATED,
				workflowDefinition.get(POSTFIX_ACTIVATED));
		setValue(workflowKey + POSTFIX_ACTORS,
				workflowDefinition.get(POSTFIX_ACTORS));
		setValue(workflowKey + POSTFIX_ASSIGNMENT,
				workflowDefinition.get(POSTFIX_ASSIGNMENT));
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

		checkAndCalculateDuration(F_PLAN_START, F_PLAN_FINISH, F_PLAN_DURATION);
		checkAndCalculateDuration(F_ACTUAL_START, F_ACTUAL_FINISH,
				F_ACTUAL_DURATION);

		// 同步负责人、流程活动执行人到工作的参与者。
		ensureParticipatesConsistency();

		super.doSave(context);

		// Work parent = (Work) getParent();
		// if (parent != null) {
		// parent.doUpdateSummarySchedual(calendarCaculater, context);
		// }else{
		// doUpdateProjectSchedual(context);
		// }

		return true;

	}

	/**
	 * 确保工作的参与者包括工作的负责人、流程执行人
	 */
	public void ensureParticipatesConsistency() {
		// 获取工作的负责人
		String chargerId = getChargerId();
		addParticipate(chargerId);

		// 获得流程的执行人
		if (isWorkflowActivate(F_WF_EXECUTE)) {
			DBObject processActorsMap = getProcessActorsMap(F_WF_EXECUTE);
			Iterator<String> iter = processActorsMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String userId = (String) processActorsMap.get(key);
				addParticipate(userId);
			}
		}

	}

	public void addParticipate(String chargerId) {
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
			List<String> participates = new ArrayList<String>();
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

	public void doCancel(IContext context) throws Exception {
		Assert.isTrue(canCancel(), "工作的当前状态不能执行取消操作");
		Map<String, Object> params = new HashMap<String,Object>();

		doCancelBefore(context, params);

		// TODO Auto-generated method stub

		doCancelAfter(context, params);
	}

	public void doFinish(IContext context) throws Exception {
		Assert.isTrue(canFinish(), "工作的当前状态不能执行完成操作");
		Map<String, Object> params = new HashMap<String,Object>();

		doFinishBefore(context, params);

		// TODO Auto-generated method stub

		doFinishAfter(context, params);

	}

	public void doPause(IContext context) throws Exception {
		Assert.isTrue(canPause(), "工作的当前状态不能执行暂停操作");
		Map<String, Object> params = new HashMap<String,Object>();

		doPauseBefore(context, params);

		// TODO Auto-generated method stub

		doPauseAfter(context, params);
	}

	/**
	 * 启动工作
	 */
	public void doStart(IContext context) throws Exception {
		// 判断能否启动，检查状态
		Assert.isTrue(canStart(), "工作的当前状态不能执行启动操作");

		Map<String, Object> params = new HashMap<String,Object>();
		//调用前处理
		doStartBefore(context,params);

		// 判定是否使用执行工作流
		if (isWorkflowActivate(F_WF_EXECUTE)) {
			// 如果是，启动工作流
			Workflow wf = getWorkflow(F_WF_EXECUTE);
			ProcessInstance processInstance = wf.startHumanProcess(params);
			Assert.isNotNull(processInstance, "流程启动失败");

			setValue(F_WF_EXECUTE+POSTFIX_INSTANCEID, processInstance.getId());
		}

		// 启动下级同步启动的工作
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			//检查下级的工作是否设置了与上级同步启动
			if (Boolean.TRUE.equals(childWork
					.getValue(F_SETTING_AUTOSTART_WHEN_PARENT_START))) {
				//启动下级工作
				childWork.doStart(context);
			}
		}

		// 标记工作的进行中
		setValue(F_LIFECYCLE, STATUS_WIP_VALUE);
		
		// 设置工作的实际开始时间
		setValue(F_ACTUAL_START, new Date());
		
		// 保存
		doSave(context);

		// 提示工作启动
		doWorkActionNotice(context, "工作启动");

		//调用后处理
		doStartAfter(context,params);
	}

	public Workflow getWorkflow(String key) {
		DBObject actors = getProcessActorsMap(key);
		@SuppressWarnings("unchecked")
		Map<String,String> actorParameter = actors.toMap();
		DroolsProcessDefinition processDefintion = getProcessDefinition(key);
		Workflow wf = new Workflow(processDefintion,actorParameter,this);
		return wf;
	}

	/**
	 * 发送工作操作完成的通知
	 * @param context 当前的上下文
	 * @param actionName 操作的文本名称
	 * @throws Exception 发送消息出现的错误
	 */
	public void doWorkActionNotice(IContext context, String actionName)
			throws Exception {
		Message message = ModelService.createModelObject(Message.class);
		//设置收件人
		BasicBSONList participatesIdList = getParticipatesIdList();
		Assert.isTrue(participatesIdList != null
				&& participatesIdList.size() > 0, "工作的参与者为空");
		message.setValue(Message.F_RECIEVER, participatesIdList);
		
		//设置通知标题
		message.setValue(Message.F_DESC, actionName + "通知");
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);

		//设置发件人
		String userId = context.getAccountInfo().getUserId();
		String userName = context.getAccountInfo().getUserName();
		message.setValue(Message.F_SENDER, userId);
	
		//设置发送时间
		message.setValue(Message.F_SENDDATE, new Date());
		
		//设置通知内容
		String content = "<span style='font-size:14px'>" + "您好: "
				+ "</span><br/><br/>" + userName + "|" + userId + "执行了"
				+ actionName + getLabel() + "<br/>您是该工作的参与者，请知晓。<br/><br/>";
		message.setValue(Message.F_CONTENT, content);
		MessageToolkit.appendEndMessage(message);
		
		//设置导航附件
		message.appendTargets(this, EDITOR, false);

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
		DBObject processRoleDefinitions = getProcessAssignmentRoleMap(processKey);
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
				User user = User.getUserById(userid);
				result.add(user);
			}
		}

		return result;
	}

	
	private void doStartAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "start.after",params);
	}

	private void doStartBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "start.before",params);
	}

	private void doPauseAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "pause.after",params);

	}

	private void doPauseBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "pause.before",params);

	}

	private void doFinishAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "finish.after",params);

	}

	private void doFinishBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "finish.before",params);

	}

	private void doCancelAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "cancel.after",params);

	}

	private void doCancelBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "cancel.before",params);

	}
}
