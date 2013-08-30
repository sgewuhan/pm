package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;

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
		IProcessControlable {

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
			setValue(fDuration, new Integer(
					(int) (edate.getTimeInMillis() - sdate.getTimeInMillis())
							/ (1000 * 60 * 60 * 24)));
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
		return checkPlan(ram);
	}

	/**
	 * 1. 检查工作的计划时间：错误，没有设定计划开始、计划完成、计划工时的叶工作 <br/>
	 * 2. 检查工作的负责人 ：错误，没有设定负责人，而且没有设定指派者的叶工作 <br/>
	 * 3. 工作的流程设定 ：警告，没有指明流程执行者的工作 <br/>
	 * 4. 交付物检查 <br/>
	 * 4.1. 检查工作是否具有交付物：警告，没有交付物的叶工作 4.2. 检查交付物文档没有电子文件作为模板：警告
	 * 
	 * @param raMap
	 * 
	 * @return
	 */
	public List<ICheckListItem> checkPlan(
			Map<ObjectId, List<PrimaryObject>> raMap) {
		ArrayList<ICheckListItem> result = new ArrayList<ICheckListItem>();
		List<PrimaryObject> childrenWork = getChildrenWork();
		if (childrenWork.size() > 0) {// 如果有下级，直接返回下级的检查结果
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				result.addAll(childWork.checkPlan(raMap));
			}
		} else {
			// ****************************************************************************************
			// 1 检查工作的计划开始和计划完成
			Object value = getPlanStart();
			boolean passed = true;
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划开始没有确定，请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(this);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanFinish();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划完成时间没有确定，请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(this);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanWorks();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作的计划工时没有确定，不确定该计划工时表示该工作不计算工时，如果您并不希望这样，请在提交前确定。",
						ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setKey(F_PLAN_WORKS);
				result.add(checkItem);
				passed = false;
			}

			value = getDesc();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性",
						"工作名称为空，请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(this);
				checkItem.setKey(F_DESC);
				result.add(checkItem);
			}

			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查工作基本属性", "",
						ICheckListItem.PASS);
				checkItem.setData(this);
				result.add(checkItem);
			}
			passed = true;

			// ****************************************************************************************
			// 2 检查负责人
			value = getCharger();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人",
						"工作负责人为空，请在提交前确定。", ICheckListItem.ERROR);
				checkItem.setData(this);
				checkItem.setKey(F_CHARGER);
				result.add(checkItem);
				passed = false;
			}

			// 3.检查参与者
			value = getParticipate();
			if (value == null || ((BasicBSONList) value).isEmpty()) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人",
						"没有添加工作参与者，请在提交前确定。", ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查工作执行人", "",
						ICheckListItem.PASS);
				checkItem.setData(this);
				result.add(checkItem);
			}
			passed = true;

			// 4.1 检查工作变更的流程 ：错误，没有指明流程负责人
			String title = "检查工作变更流程";
			String process = F_WF_CHANGE;
			String editorId = Project.EDITOR_CREATE_PLAN;
			String pageId = Project.EDITOR_PAGE_WBS;
			passed = ModelUtil.checkProcessInternal(this, result, raMap, title, process,
					editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title, "",
						ICheckListItem.PASS);
				checkItem.setData(this);
				result.add(checkItem);
			}

			// 4.2 检查项目提交的流程 ：错误，没有指明流程负责人
			title = "检查工作执行流程";
			process = F_WF_EXECUTE;
			passed = ModelUtil.checkProcessInternal(this, result, raMap, title, process,
					editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title, "",
						ICheckListItem.PASS);
				checkItem.setData(this);
				result.add(checkItem);
			}

			passed = true;
			// 检查工作交付物
			List<PrimaryObject> docs = getDeliverableDocuments();
			if (docs.isEmpty()) {
				CheckListItem checkItem = new CheckListItem("检查交付物",
						"该工作没有设定交付物，提交前如果不设定，将由工作执行后由执行者自行添加，请在提交前确定。",
						ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("检查交付物", "",
						ICheckListItem.PASS);
				checkItem.setData(this);
				result.add(checkItem);
			}

		}

		return result;
	}

	public User getCharger() {
		String chargerId = (String) getValue(F_CHARGER);
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public List<?> getParticipate() {
		return (List<?>) getValue(F_PARTICIPATE);
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
		DBObject data = (DBObject) getValue(key + POSTFIX_ACTORS);
		if(data==null){
			return null;
		}
		return (String) data.get(nodeActorParameter);
	}

	@Override
	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// 取出角色指派
		DBObject data = (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
		if(data==null){
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
	}

}
