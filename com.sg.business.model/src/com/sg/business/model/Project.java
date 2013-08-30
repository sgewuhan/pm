package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelRelation;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.dataset.calendarsetting.SystemCalendar;
import com.sg.business.resource.BusinessResource;

/**
 * 项目
 * <p/>
 * 
 * @author jinxitao
 * 
 */
public class Project extends PrimaryObject implements IProjectTemplateRelative,
		ILifecycle, ISchedual, IProcessControlable {

	/**
	 * 项目负责人字段，保存项目负责人的userid {@link User} ,
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * 数组类型字段，每个元素为都是userid
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * 项目所属的项目职能组织
	 */
	public static final String F_FUNCTION_ORGANIZATION = "org_id";

	/**
	 * 项目发起部门
	 */
	public static final String F_LAUNCH_ORGANIZATION = "launchorg_id";

	/**
	 * 根工作定义
	 */
	public static final String F_WORK_ID = "work_id";

	public static final String F_BUDGET_ID = "budget_id";

	public static final String F_STANDARD_SET_OPTION = "standardset";

	public static final String F_PRODUCT_TYPE_OPTION = "producttype";

	public static final String F_PROJECT_TYPE_OPTION = "projecttype";

	/**
	 * 列表类型的字段，工作令号
	 */
	public static final String F_WORK_ORDER = "workorder";

	/**
	 * 项目提交流程
	 */
	public static final String F_WF_COMMIT = "wf_commit";

	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment";

	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated";

	public static final String F_WF_COMMIT_ACTORS = "wf_commit_actors";
	/**
	 * 项目变更流程
	 */
	public static final String F_WF_CHANGE = "wf_change";

	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";

	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	public static final String EDITOR_CREATE_PLAN = "project.editor";

	public static final String EDITOR_SETPROCESS = "project.flow.setting";

	public static final String EDITOR_PAGE_BUDGET = "project.financial";

	public static final String EDITOR_PAGE_TEAM = "project.team";

	public static final String EDITOR_PAGE_WBS = "project.wbs";

	public static final String EDITOR_PAGE_CHANGE_PROCESS = "processpage2";

	public static final String EDITOR_PAGE_COMMIT_PROCESS = "processpage1";

	@Override
	public String getTypeName() {
		return "项目";
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}

	public User getCharger() {
		String chargerId = (String) getValue(F_CHARGER);
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public Organization getFunctionOrganization() {
		ObjectId orgId = getFunctionOrganizationId();
		if (orgId != null) {
			return ModelService.createModelObject(Organization.class,
					(ObjectId) orgId);
		}
		return null;
	}

	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	public ProjectTemplate getProjectTemplate() {
		ObjectId id = getProjectTemplateId();
		if (id != null) {
			return ModelService.createModelObject(ProjectTemplate.class, id);
		}
		return null;
	}

	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

	@SuppressWarnings("unchecked")
	public List<String> getStandardSetOptions() {
		return (List<String>) getValue(F_STANDARD_SET_OPTION);
	}

	@SuppressWarnings("unchecked")
	public List<String> getProductTypeOptions() {
		return (List<String>) getValue(F_PRODUCT_TYPE_OPTION);
	}

	@SuppressWarnings("unchecked")
	public List<String> getProjectTypeOptions() {
		return (List<String>) getValue(F_PROJECT_TYPE_OPTION);
	}

	public ProjectBudget getBudget() {
		DBCollection col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		DBObject data = col.findOne(new BasicDBObject().append(
				ProjectBudget.F_PROJECT_ID, get_id()));
		return ModelService.createModelObject(data, ProjectBudget.class);
	}

	public Work getWBSRoot() {
		ObjectId workid = (ObjectId) getValue(F_WORK_ID);
		return ModelService.createModelObject(Work.class, workid);
	}

	public List<PrimaryObject> getChildrenWork() {
		Work root = getWBSRoot();
		return root.getChildrenWork();
	}

	public Map<ObjectId, List<PrimaryObject>> getRoleAssignmentMap() {
		List<PrimaryObject> roles = getRoleDefinitions();
		Map<ObjectId, List<PrimaryObject>> result = new HashMap<ObjectId, List<PrimaryObject>>();
		if (!roles.isEmpty()) {
			for (int i = 0; i < roles.size(); i++) {
				ProjectRole role = (ProjectRole) roles.get(i);
				List<PrimaryObject> assignment = role.getAssignment();
				result.put(role.get_id(), assignment);
			}
		}
		return result;
	}

	public List<PrimaryObject> getRoleDefinitions() {
		return getRelationById(F__ID, ProjectRole.F_PROJECT_ID,
				ProjectRole.class);
	}

	public List<PrimaryObject> getCalendarCondition() {
		ModelRelation mr = ModelService.getModelRelation("project_calendar");
		return getRelationByModel(mr);
	}

	public boolean hasOrganizationRole(Role role) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_ROLE);
		long count = col.count(new BasicDBObject().append(
				ProjectRole.F_ORGANIZATION_ROLE_ID, role.get_id()).append(
				ProjectRole.F_PROJECT_ID, get_id()));
		return count != 0;
	}

	public ProjectBudget makeBudget(IContext context) {
		ObjectId id = getProjectTemplateId();
		if (id != null) {
			return makeBudgetWithTemplate(id, context);
		} else {
			// 从全局复制
			return (ProjectBudget) BudgetItem
					.COPY_DEFAULT_BUDGET_ITEM(ProjectBudget.class);
		}
	}

	private Work makeWBSRoot() {
		BasicDBObject wbsRootData = new BasicDBObject();
		wbsRootData.put(Work.F_DESC, getDesc());
		wbsRootData.put(Work.F_PROJECT_ID, get_id());
		ObjectId wbsRootId = new ObjectId();
		wbsRootData.put(Work.F__ID, wbsRootId);
		wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
		return ModelService.createModelObject(wbsRootData, Work.class);
	}

	public ProjectRole makeProjectRole(ProjectRole po) {
		if (po == null) {
			po = ModelService.createModelObject(ProjectRole.class);
		}
		po.setValue(ProjectRole.F_PROJECT_ID, get_id());
		return po;
	}

	public ProjectRole makeOrganizationRole(Role role) {
		ProjectRole prole = ModelService.createModelObject(ProjectRole.class);
		prole.setValue(ProjectRole.F_ORGANIZATION_ROLE_ID, role.get_id());
		prole.setValue(ProjectRole.F_PROJECT_ID, get_id());
		return prole;
	}

	public ProjectBudget makeBudgetWithTemplate(ObjectId projectTemplateId,
			IContext context) {
		DBCollection col = getCollection(IModelConstants.C_BUDGET_ITEM);
		DBObject srcdata = col.findOne(new BasicDBObject().append(
				WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
		DBObject tgtData = new BasicDBObject();
		tgtData.put(ProjectBudget.F_PROJECT_ID, get_id());
		tgtData.put(ProjectBudget.F_DESC, getDesc());
		tgtData.put(ProjectBudget.F_DESC_EN, getDesc_e());
		tgtData.put(ProjectBudget.F_CHILDREN,
				srcdata.get(BudgetItem.F_CHILDREN));

		return ModelService.createModelObject(tgtData, ProjectBudget.class);
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID, new ObjectId());

		// 创建根工作定义
		Work root = makeWBSRoot();
		root.doInsert(context);
		setValue(Project.F_WORK_ID, root.get_id());

		// 预算
		ProjectBudget budget = makeBudget(context);
		budget.doInsert(context);

		super.doInsert(context);

		// 复制模板
		doSetupWithTemplate(root.get_id(), context);

		// 复制系统日历
		doCopySystemCanlendar();

	}

	private void doCopySystemCanlendar() throws Exception {
		SystemCalendar sc = new SystemCalendar();
		DataSet d = sc.getDataSet();
		List<PrimaryObject> items = d.getDataItems();
		List<DBObject> ins = new ArrayList<DBObject>();
		for (int i = 0; i < items.size(); i++) {
			CalendarSetting item = (CalendarSetting) items.get(i);
			BasicDBObject pjCalData = new BasicDBObject();
			pjCalData.put(CalendarSetting.F_CONDITION,
					item.getValue(CalendarSetting.F_CONDITION));
			pjCalData.put(CalendarSetting.F_END_DATE,
					item.getValue(CalendarSetting.F_END_DATE));
			pjCalData.put(CalendarSetting.F_OPERATOR,
					item.getValue(CalendarSetting.F_OPERATOR));
			pjCalData.put(CalendarSetting.F_SEQ,
					item.getValue(CalendarSetting.F_SEQ));
			pjCalData.put(CalendarSetting.F_START_DATE,
					item.getValue(CalendarSetting.F_START_DATE));
			pjCalData.put(CalendarSetting.F_VALUE,
					item.getValue(CalendarSetting.F_VALUE));
			pjCalData.put(CalendarSetting.F_WORKING_TIME,
					item.getValue(CalendarSetting.F_WORKING_TIME));
			pjCalData.put(CalendarSetting.F_WORKINGDAY,
					item.getValue(CalendarSetting.F_WORKINGDAY));
			pjCalData.put(CalendarSetting.F_DESC,
					item.getValue(CalendarSetting.F_DESC));
			pjCalData.put(CalendarSetting.F_PROJECT_ID, get_id());
			ins.add(pjCalData);
		}
		DBCollection col = getCollection(IModelConstants.C_CALENDAR_SETTING);
		WriteResult ws = col.insert(ins, WriteConcern.NORMAL);
		checkWriteResult(ws);
	}

	/**
	 * 
	 * @param context
	 * @param wbsRootId
	 * @throws Exception
	 */
	public void doSetupWithTemplate(ObjectId wbsRootId, IContext context)
			throws Exception {
		ObjectId id = getProjectTemplateId();
		if (id == null) {
			return;
		}

		// 复制角色定义
		Map<ObjectId, DBObject> roleMap = doSetupRolesWithTemplate(id, context);

		// 复制工作定义
		Map<ObjectId, DBObject> workMap = doSetupWBSWithTemplate(id, wbsRootId,
				roleMap, context);

		// 复制工作的前后序关系
		doSetupWorkConnectionWithTemplate(id, workMap, context);

		// 设置项目的流程和角色
		doSetupWorkflowWithTemplate(id, roleMap, context);

	}

	private void doSetupWorkflowWithTemplate(ObjectId id,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {

		DBCollection col = getCollection(IModelConstants.C_PROJECT_TEMPLATE);
		DBObject pjTempData = col.findOne(new BasicDBObject().append(
				ProjectTemplate.F__ID, getProjectTemplateId()));
		if (pjTempData == null) {
			return;
		}

		// 设置变更工作流
		Object value = pjTempData.get(ProjectTemplate.F_WF_CHANGE);
		if (value != null) {
			setValue(ProjectTemplate.F_WF_CHANGE, value);
		}

		// 设置变更工作流是否激活
		value = pjTempData.get(ProjectTemplate.F_WF_CHANGE_ACTIVATED);
		if (value != null) {
			setValue(ProjectTemplate.F_WF_CHANGE_ACTIVATED, value);
		}

		// 设置变更流程的活动执行人
		setRoleDBObjectField(get_data(), pjTempData,
				ProjectTemplate.F_WF_CHANGE_ASSIGNMENT, roleMap);

		// 设置执行工作流
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT);
		if (value != null) {
			setValue(ProjectTemplate.F_WF_COMMIT, value);
		}

		// 设置执行工作流是否激活
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT_ACTIVATED);
		if (value != null) {
			setValue(ProjectTemplate.F_WF_COMMIT_ACTIVATED, value);
		}

		// 设置执行工作流的活动执行人角色
		setRoleDBObjectField(get_data(), pjTempData,
				ProjectTemplate.F_WF_COMMIT_ASSIGNMENT, roleMap);

		doSave(context);
	}

	private void doSetupWorkConnectionWithTemplate(ObjectId projectTemplateId,
			Map<ObjectId, DBObject> workMap, IContext context) throws Exception {
		DBCollection connDefCol = getCollection(IModelConstants.C_WORK_DEFINITION_CONNECTION);
		DBCursor cur = connDefCol.find(new BasicDBObject().append(
				WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
		List<DBObject> result = new ArrayList<DBObject>();
		while (cur.hasNext()) {
			DBObject connD = cur.next();
			ObjectId srcEnd1 = (ObjectId) connD
					.get(WorkDefinitionConnection.F_END1_ID);
			DBObject tgtEnd1Data = workMap.get(srcEnd1);
			ObjectId tgtEnd1 = null;
			if (tgtEnd1Data != null) {
				tgtEnd1 = (ObjectId) tgtEnd1Data.get(Work.F__ID);
			}
			ObjectId srcEnd2 = (ObjectId) connD
					.get(WorkDefinitionConnection.F_END2_ID);
			DBObject tgtEnd2Data = workMap.get(srcEnd2);
			ObjectId tgtEnd2 = null;
			if (tgtEnd2Data != null) {
				tgtEnd2 = (ObjectId) tgtEnd2Data.get(Work.F__ID);
			}
			if (tgtEnd1 == null || tgtEnd2 == null) {
				continue;
			}

			BasicDBObject conn = new BasicDBObject();
			conn.put(WorkConnection.F_PROJECT_ID, get_id());
			conn.put(WorkConnection.F__EDITOR, WorkConnection.EDITOR);
			conn.put(WorkConnection.F_CONNECTIONTYPE,
					connD.get(WorkDefinitionConnection.F_CONNECTIONTYPE));
			conn.put(WorkConnection.F_INTERVAL,
					connD.get(WorkDefinitionConnection.F_INTERVAL));
			conn.put(WorkConnection.F_OPERATOR,
					connD.get(WorkDefinitionConnection.F_OPERATOR));
			conn.put(WorkConnection.F_UNIT,
					connD.get(WorkDefinitionConnection.F_UNIT));
			conn.put(WorkConnection.F_END1_ID, tgtEnd1);
			conn.put(WorkConnection.F_END2_ID, tgtEnd2);
			result.add(conn);
		}

		DBCollection col = getCollection(IModelConstants.C_WORK_CONNECTION);
		WriteResult ws = col.insert(result);
		checkWriteResult(ws);
	}

	private Map<ObjectId, DBObject> doSetupRolesWithTemplate(
			ObjectId projectTemplateId, IContext context) throws Exception {
		DBCollection col_roled = getCollection(IModelConstants.C_ROLE_DEFINITION);
		DBCollection col_role = getCollection(IModelConstants.C_PROJECT_ROLE);
		// 删除项目现有的角色
		col_role.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
				get_id()));

		// 准备返回值
		HashMap<ObjectId, DBObject> result = new HashMap<ObjectId, DBObject>();

		// 查找模板的角色定义
		DBCursor cur = col_roled.find(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplateId));
		while (cur.hasNext()) {
			DBObject roleddata = cur.next();

			// 创建项目角色对象
			ProjectRole prole = makeProjectRole(null);

			// 给出将要创建的项目角色的_id
			ObjectId proleId = new ObjectId();
			prole.setValue(F__ID, proleId);
			// 如果是组织角色
			Object roleId = roleddata
					.get(RoleDefinition.F_ORGANIZATION_ROLE_ID);
			if (roleId != null) {
				// 设置为组织角色
				prole.setValue(ProjectRole.F_ORGANIZATION_ROLE_ID, roleId);
				// 将组织角色中的成员加入到项目的参与者
				Role role = ModelService.createModelObject(Role.class,
						(ObjectId) roleId);
				List<PrimaryObject> ass = role.getAssignment();
				doAddParticipateFromAssignment(ass);

			} else {
				// 设置为项目角色
				prole.setValue(ProjectRole.F_ROLE_NUMBER,
						roleddata.get(RoleDefinition.F_ROLE_NUMBER));
				prole.setValue(ProjectRole.F_DESC,
						roleddata.get(RoleDefinition.F_DESC));
			}
			prole.setValue(ProjectRole.F__CACCOUNT, context.getAccountInfo()
					.getUserId());
			prole.setValue(ProjectRole.F__CDATE, new Date());

			result.put((ObjectId) roleddata.get(RoleDefinition.F__ID),
					prole.get_data());
		}

		if (!result.isEmpty()) {
			DBObject[] insertData = result.values().toArray(new DBObject[0]);

			// 插入到数据库
			WriteResult ws = col_role.insert(insertData, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		return result;
	}

	private HashMap<ObjectId, DBObject> doSetupWBSWithTemplate(
			ObjectId projectTemplateId, ObjectId wbsRootId,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {

		// 取出模板的根工作定义的_id
		DBCollection pjTempCol = getCollection(IModelConstants.C_PROJECT_TEMPLATE);

		DBObject pjTemp = pjTempCol.findOne(new BasicDBObject().append(
				ProjectTemplate.F__ID, projectTemplateId), new BasicDBObject()
				.append(ProjectTemplate.F_WORK_DEFINITON_ID, 1));
		ObjectId rootWorkDefId = (ObjectId) pjTemp
				.get(ProjectTemplate.F_WORK_DEFINITON_ID);

		HashMap<ObjectId, DBObject> worksToBeInsert = new HashMap<ObjectId, DBObject>();

		HashMap<ObjectId, DBObject> documentsToBeInsert = new HashMap<ObjectId, DBObject>();

		List<DBObject> deliverableToInsert = new ArrayList<DBObject>();

		List<DBObject[]> fileToCopy = new ArrayList<DBObject[]>();

		copyWBSTemplate(rootWorkDefId, wbsRootId, wbsRootId, get_id(), roleMap,
				worksToBeInsert, documentsToBeInsert, deliverableToInsert,
				fileToCopy, context);

		// 保存工作
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// 清除已有的非根工作定义
		WriteResult ws = workCol.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()).append(Work.F__ID,
				new BasicDBObject().append("$ne", wbsRootId)));
		checkWriteResult(ws);
		ws = workCol.insert(worksToBeInsert.values().toArray(new DBObject[0]),
				WriteConcern.NORMAL);
		checkWriteResult(ws);

		// 保存文档
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		ws = docCol.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		ws = docCol.insert(documentsToBeInsert.values()
				.toArray(new DBObject[0]), WriteConcern.NORMAL);
		checkWriteResult(ws);

		// 保存交付物
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = deliCol.remove(new BasicDBObject().append(
				Deliverable.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		ws = deliCol.insert(deliverableToInsert, WriteConcern.NORMAL);
		checkWriteResult(ws);

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

		return worksToBeInsert;
	}

	/**
	 * 根据角色进行工作指派
	 * 
	 * @throws Exception
	 */
	public void doAssignmentByRole(IContext context) throws Exception {
		Map<ObjectId, List<PrimaryObject>> map = getRoleAssignmentMap();
		if (map.size() == 0) {
			throw new Exception("尚未对角色指定人员，无法执行按角色指派工作");
		}
		List<PrimaryObject> childrenWorks = getChildrenWork();
		for (int i = 0; i < childrenWorks.size(); i++) {
			Work childWork = (Work) childrenWorks.get(i);
			childWork.doAssignment(map, context);
		}
	}

	/**
	 * 为项目添加参与者, 项目的参与者可以在为项目角色指定成员时添加,也可以自由添加<br/>
	 * 自由添加的参与者可以应用到项目的工作中<br/>
	 * 传入的参数是用户id{@link User#F_USER_ID}的列表<br/>
	 * 
	 * 
	 * {@link F_PARTICIPATE} 字段是数组类型的字段<br/>
	 * 使用了$addToSet和each一次性将多个值更新到该字段，并且保证没有重复<br/>
	 * 
	 * @param userIds
	 *            参与者数组 userid {@link User#getUserid()}, {@link User#F_USER_ID}
	 * @throws Exception
	 *             抛出写入错误时
	 */
	public void doAddParticipate(String[] userIds) throws Exception {
		DBCollection pjCol = getCollection();

		DBObject update = new BasicDBObject().append("$addToSet",
				new BasicDBObject().append(Project.F_PARTICIPATE,
						new BasicDBObject().append("$each", userIds)));

		WriteResult ws = pjCol.update(
				new BasicDBObject().append(Project.F__ID, get_id()), update,
				false, false);
		checkWriteResult(ws);
	}

	public void doAddParticipateFromAssignment(List<PrimaryObject> assignment)
			throws Exception {
		if (assignment == null || assignment.size() == 0) {
			return;
		}
		String[] userIds = new String[assignment.size()];
		for (int i = 0; i < assignment.size(); i++) {
			AbstractRoleAssignment ra = (AbstractRoleAssignment) assignment
					.get(i);
			userIds[i] = ra.getUserid();
		}
		if (userIds.length > 0) {
			doAddParticipate(userIds);
		}
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		// 删除work
		DBCollection col = getCollection(IModelConstants.C_WORK);
		WriteResult ws = col.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		// 删除workconnection
		col = getCollection(IModelConstants.C_WORK_CONNECTION);
		ws = col.remove(new BasicDBObject().append(Work.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		// 删除预算
		col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		ws = col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// 删除role
		col = getCollection(IModelConstants.C_PROJECT_ROLE);
		DBCursor cur = col.find(
				new BasicDBObject().append(ProjectRole.F_PROJECT_ID, get_id()),
				new BasicDBObject().append(ProjectRole.F__ID, 1));
		ObjectId[] roleIds = new ObjectId[cur.size()];
		int i = 0;
		while (cur.hasNext()) {
			roleIds[i++] = (ObjectId) cur.next().get(ProjectRole.F__ID);
		}
		ws = col.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// 删除roleassignment
		col = getCollection(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		ws = col.remove(new BasicDBObject().append(
				ProjectRoleAssignment.F_ROLE_ID,
				new BasicDBObject().append("$in", roleIds)));
		checkWriteResult(ws);

		// 删除交付物
		col = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = col.remove(new BasicDBObject().append(Deliverable.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// 删除文档
		col = getCollection(IModelConstants.C_DOCUMENT);
		ws = col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		super.doRemove(context);
	}

	/**
	 * 
	 * @param srcParent
	 *            模板工作的Id
	 * @param tgtParentId
	 *            项目目标工作的Id
	 * @param tgtRootId
	 *            项目目标工作的根Id
	 * @param projectId
	 *            项目Id
	 * @param roleMap
	 *            角色映射，{模板角色Id:目标角色}
	 * @param worksToInsert
	 *            将要插入数据库的工作，添加的工作都需要放置到ArrayList中
	 * @param documentsToInsert
	 *            将要插入数据库的文档, {文档模板id:目标文档}
	 * @param dilerverableToInsert
	 *            将要插入数据库的交付物关系
	 * @param fileToCopy
	 *            需要复制的文件
	 * @param context
	 *            操作上下文
	 */
	private void copyWBSTemplate(ObjectId srcParent, ObjectId tgtParentId,
			ObjectId tgtRootId, ObjectId projectId,
			Map<ObjectId, DBObject> roleMap,
			Map<ObjectId, DBObject> worksToInsert,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context) {
		// 获得src的子
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(
				new BasicDBObject().append(WorkDefinition.F_PARENT_ID,
						srcParent)).sort(new SEQSorter().getBSON());
		int seq = 0;
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// 检查workdef是否符合选项过滤条件
			String optionValue = checkOptionValueFromTemplate(workdef);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// 如果是排除的，就需要跳过
				continue;

			} else {
				// ************************************************************************
				// 创建工作
				DBObject work = new BasicDBObject();

				work.put(Work.F_PROJECT_ID, projectId);

				work.put(Work.F_ROOT_ID, tgtRootId);

				work.put(Work.F_PARENT_ID, tgtParentId);

				work.put(Work.F__ID, new ObjectId());

				if (WorkDefinition.VALUE_MANDATORY.equals(optionValue)) {
					work.put(Work.F_MANDATORY, Boolean.TRUE);
				}

				// 设置工作的描述字段
				Object value = workdef.get(WorkDefinition.F_DESC);
				if (value != null) {
					work.put(Work.F_DESC, value);
				}
				value = workdef.get(WorkDefinition.F_DESC_EN);
				if (value != null) {
					work.put(Work.F_DESC_EN, value);
				}

				// 设置变更工作流
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE, value);
				}

				// 设置变更工作流是否激活
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE_ACTIVATED, value);
				}

				// 设置变更流程的活动执行人
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_CHANGE_ASSIGNMENT, roleMap);

				// 设置执行工作流
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE, value);
				}

				// 设置执行工作流是否激活
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED, value);
				}

				// 设置执行工作流的活动执行人角色
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_EXECUTE_ASSIGNMENT, roleMap);

				// 设置负责人角色
				setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID,
						roleMap);

				// 设置参与者角色
				setRoleListField(work, workdef,
						IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

				// 设置序号
				work.put(IWorkCloneFields.F_SEQ, new Integer(seq++));

				// 设置标准工时
				value = workdef.get(IWorkCloneFields.F_STANDARD_WORKS);
				if (value != null) {
					work.put(IWorkCloneFields.F_STANDARD_WORKS, value);
				}

				// 复制设置项
				for (int i = 0; i < IWorkCloneFields.SETTING_FIELDS.length; i++) {
					value = workdef.get(IWorkCloneFields.SETTING_FIELDS[i]);
					if (value != null) {
						work.put(IWorkCloneFields.SETTING_FIELDS[i], value);
					}
				}

				BasicDBObject accountData = new BasicDBObject().append(
						"userid", context.getAccountInfo().getUserId()).append(
						"username", context.getAccountInfo().getUserName());
				work.put(Work.F__CACCOUNT, accountData);

				work.put(Work.F__CDATE, new Date());

				// 完成工作属性复制
				// ************************************************************

				// ************************************************************
				// 添加工作交付物
				// 获得模板定义的交付物定义
				DBCollection deliveryDefCol = getCollection(IModelConstants.C_DELIEVERABLE_DEFINITION);
				DBCursor deliCur = deliveryDefCol.find(
						new BasicDBObject().append(
								DeliverableDefinition.F_WORK_DEFINITION_ID,
								workdef.get(WorkDefinition.F__ID)),
						new BasicDBObject().append(
								DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
								1).append(
								DeliverableDefinition.F_OPTION_FILTERS, 1));
				while (deliCur.hasNext()) {
					DBObject delidata = deliCur.next();
					// 检查交付物是否符合选项过滤条件
					String documentOptionValue = checkOptionValueFromTemplate(delidata);
					if (DeliverableDefinition.VALUE_EXCLUDE
							.equals(documentOptionValue)) {
						// 如果是排除的，就需要跳过
						continue;
					} else {
						// 根据模板的交付物定义创建交付物关系
						DBObject deliverableData = new BasicDBObject();

						// 设置项目Id
						deliverableData
								.put(Deliverable.F_PROJECT_ID, projectId);

						// 设置工作Id
						deliverableData.put(Deliverable.F_WORK_ID,
								work.get(Work.F__ID));

						// 设置是否必须
						if (DeliverableDefinition.VALUE_MANDATORY
								.equals(documentOptionValue)) {
							deliverableData.put(Deliverable.F_MANDATORY,
									Boolean.TRUE);
						}

						// 获得文档模板
						ObjectId documentTemplateId = (ObjectId) delidata
								.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
						// 检查该文档模板是否已经创建了文档
						DBObject documentData = documentsToInsert
								.get(documentTemplateId);
						if (documentData == null) {
							// 如果没有创建，需要创建该文档
							documentData = copyDocumentFromTemplate(
									documentsToInsert, fileToCopy,
									documentTemplateId, projectId);
						}
						documentsToInsert.put(documentTemplateId, documentData);
						ObjectId documentId = (ObjectId) documentData
								.get(Document.F__ID);
						deliverableData.put(Deliverable.F_DOCUMENT_ID,
								documentId);
						dilerverableToInsert.add(deliverableData);
					}
				}

				worksToInsert.put((ObjectId) workdef.get(WorkDefinition.F__ID),
						work);

				// 处理子工作
				copyWBSTemplate((ObjectId) workdef.get(WorkDefinition.F__ID),
						(ObjectId) work.get(Work.F__ID), tgtRootId, projectId,
						roleMap, worksToInsert, documentsToInsert,
						dilerverableToInsert, fileToCopy, context);
			}

		}

	}

	private DBObject copyDocumentFromTemplate(
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject[]> fileToCopy, ObjectId documentTemplateId,
			ObjectId projectId) {
		DBObject documentData;
		DBCollection docdCol = getCollection(IModelConstants.C_DOCUMENT_DEFINITION);
		DBObject documentTemplate = docdCol.findOne(new BasicDBObject().append(
				Document.F__ID, documentTemplateId));
		documentData = new BasicDBObject();

		documentData.put(Document.F__ID, new ObjectId());

		documentData.put(Document.F_PROJECT_ID, projectId);

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
	 * 
	 * @param work
	 * @param workdef
	 * @param fieldName
	 * @param roleMap
	 */
	private void setRoleDBObjectField(DBObject work, DBObject workdef,
			String fieldName, Map<ObjectId, DBObject> roleMap) {
		Object value = workdef.get(fieldName);
		if (value instanceof DBObject) {
			DBObject dbo = (DBObject) value;
			BasicDBObject actors = new BasicDBObject();
			Iterator<String> iter = dbo.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				ObjectId roleId = (ObjectId) dbo.get(key);
				DBObject tgtRole = roleMap.get(roleId);
				ObjectId tgtRoleId = (ObjectId) tgtRole.get(ProjectRole.F__ID);
				actors.put(key, tgtRoleId);
			}
			work.put(fieldName, actors);
		}
	}

	private void setRoleListField(DBObject work, DBObject workdef,
			String fieldName, Map<ObjectId, DBObject> roleMap) {
		Object value = workdef.get(fieldName);
		if (value instanceof BasicBSONList) {
			BasicBSONList participates = new BasicBSONList();
			BasicBSONList valueList = (BasicBSONList) value;
			for (int i = 0; i < valueList.size(); i++) {
				DBObject srcRole = (DBObject) valueList.get(i);
				ObjectId srcRoleId = (ObjectId) srcRole.get(ProjectRole.F__ID);
				DBObject tgtRole = roleMap.get(srcRoleId);
				if (tgtRole != null) {
					Object tgtRoleId = tgtRole.get(ProjectRole.F__ID);
					if (tgtRoleId != null) {
						participates.add(new BasicDBObject().append("_id",
								tgtRoleId));
					}
				}
			}
			work.put(fieldName, participates);
		}
	}

	private void setRoleField(DBObject work, DBObject workdef,
			String roleFieldName, Map<ObjectId, DBObject> roleMap) {
		ObjectId srcRoleId = (ObjectId) workdef.get(roleFieldName);
		if (srcRoleId != null) {
			DBObject tgtRole = roleMap.get(srcRoleId);
			if (tgtRole != null) {
				Object value = tgtRole.get(ProjectRole.F__ID);
				if (value != null) {
					work.put(roleFieldName, value);
				}
			}
		}
	}

	private String checkOptionValueFromTemplate(DBObject optionHost) {
		Object filters = optionHost.get(WorkDefinition.F_OPTION_FILTERS);
		if (filters instanceof BasicBSONList) {

			BasicBSONList filtersValue = (BasicBSONList) filters;
			// 检查标准集
			List<String> optionValueSet = getStandardSetOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_STANDARD);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}

			// 检查产品选项集
			optionValueSet = getProductTypeOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_PRODUCTTYPE);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}

			// 检查项目选项集
			optionValueSet = getProjectTypeOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_PROJECTTYPE);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}
		}
		return WorkDefinition.VALUE_OPTION;
	}

	@Override
	public boolean canDelete(IContext context) {
		return super.canDelete(context);
	}

	public void checkAndCalculateDuration(CalendarCaculater cc, String fStart,
			String fFinish, String fDuration) throws Exception {
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
			int workingdays = cc.getWorkingDays(start, finish);
			setValue(fDuration, new Integer(workingdays));
		}
	}

	public List<?> getParticipate() {
		return (List<?>) getValue(F_PARTICIPATE);
	}

	/**
	 * 重新整理WBS序号,不排除某些操作可能引起WBS的混乱，使用本功能整理WBS序号
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doArrangeWBSCode(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doArrangeWBSCode();
	}

	/**
	 * 检查项目计划 <br/>
	 * 1. 检查工作令号： 警告，没有 <br/>
	 * 2. 预算检查 ：警告，如果没有值 <br/>
	 * 3. 检查角色的指派： 警告没有指派人员的角色 <br/>
	 * 4. 检查项目的流程 ：错误，没有指明流程负责人 <br/>
	 * 5. 遍历工作进行检查<br/>
	 * 
	 * -- 重新计算项目的计划开始开始时间、计划完成时间、工期 重新计算项目的总工时
	 * 
	 * @return List<ICheckListItem> 检查项清单
	 */
	public List<ICheckListItem> checkPlan() {
		List<ICheckListItem> result = new ArrayList<ICheckListItem>();
		// 1. 检查工作令号： 警告，没有
		Object value = getValue(F_WORK_ORDER);
		if (!(value instanceof BasicBSONList)
				|| ((BasicBSONList) value).isEmpty()) {
			CheckListItem checkItem = new CheckListItem("检查工作令号", "工作令号空",
					"如果本项目在提交后确定工作令号，请忽略本提示。", ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setKey(F_WORK_ORDER);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem("检查工作令号");
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 2. 预算检查 ：警告，如果没有值
		ProjectBudget budget = getBudget();
		value = budget.getBudgetValue();
		if (value == null || ((Double) value).doubleValue() == 0d) {
			CheckListItem checkItem = new CheckListItem("检查预算",
					"没有制定项目预算，或预算为0", "如果本项目在提交后确定预算，请忽略本提示",
					ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setKey(F_WORK_ORDER);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setEditorPageId(EDITOR_PAGE_BUDGET);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem("检查预算");
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 3. 检查角色的指派： 警告没有指派人员的角色
		List<PrimaryObject> rd = getRoleDefinitions();
		Map<ObjectId, List<PrimaryObject>> raMap = getRoleAssignmentMap();
		List<PrimaryObject> ra;
		boolean passed = true;
		for (int i = 0; i < rd.size(); i++) {
			ProjectRole role = (ProjectRole) rd.get(i);
			ObjectId roldId = role.get_id();
			ra = raMap.get(roldId);
			if (ra == null) {
				CheckListItem checkItem = new CheckListItem("检查项目角色指派",
						"没有确定角色对应人员，" + "角色：[" + role.getLabel()+"]"
								, "如果本项目在提交后确定人员，请忽略本提示",
						ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setEditorId(EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(EDITOR_PAGE_TEAM);
				checkItem.setSelection(role);
				result.add(checkItem);
				passed = false;
			}
		}
		if (passed) {
			CheckListItem checkItem = new CheckListItem("检查项目角色指派");
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 4.1 检查项目变更的流程 ：错误，没有指明流程负责人
		String title = "检查项目变更流程";
		String process = F_WF_CHANGE;
		String editorId = EDITOR_SETPROCESS;
		String pageId = EDITOR_PAGE_CHANGE_PROCESS;
		passed = ModelUtil.checkProcessInternal(this,this, result, raMap, title,
				process, editorId, pageId);
		if (passed) {
			CheckListItem checkItem = new CheckListItem(title);
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 4.2 检查项目提交的流程 ：错误，没有指明流程负责人
		title = "检查项目提交流程";
		process = F_WF_COMMIT;
		pageId = EDITOR_PAGE_COMMIT_PROCESS;
		passed = ModelUtil.checkProcessInternal(this,this, result, raMap, title,
				process, editorId, pageId);
		if (passed) {
			CheckListItem checkItem = new CheckListItem(title);
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 5 检查工作
		Work work = getWBSRoot();
		List<ICheckListItem> workResult = work.checkPlan();
		result.addAll(workResult);

		return result;
	}

	@Override
	public String getProcessActionActor(String key, String nodeActorParameter) {
		DBObject data = (DBObject) getValue(key + POSTFIX_ACTORS);
		if (data == null) {
			return null;
		}
		return (String) data.get(nodeActorParameter);
	}

	@Override
	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// 取出角色指派
		DBObject data = (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
	}

	/**
	 * 变更工作流是否激活
	 * 
	 * @return
	 */
	public boolean isChangeWorkflowActivate() {
		return Boolean.TRUE.equals(getValue(F_WF_CHANGE_ACTIVATED));
	}

	/**
	 * 提交工作流是否激活
	 * 
	 * @return
	 */
	public boolean isCommitWorkflowActivate() {
		return Boolean.TRUE.equals(getValue(F_WF_COMMIT_ACTIVATED));
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
	
	public String getLifecycleStatus(){
		String lc = (String) getValue(F_LIFECYCLE);
		if(lc==null){
			return STATUS_NONE_VALUE;
		}else{
			return lc;
		}
	}

	public boolean canCheck() {
		//未完成和未取消的
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))&&(!STATUS_FINIHED_VALUE.equals(lc));
	}

	public boolean canCommit() {
		String lc = getLifecycleStatus();

		return STATUS_NONE_VALUE.equals(lc);
	}

	public boolean canStart() {
		String lc = getLifecycleStatus();
		return STATUS_ONREADY_VALUE.equals(lc)||STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canPause() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc);
	}

	public boolean canFinish() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc)||STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canCancel() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc)||STATUS_PAUSED_VALUE.equals(lc);
	}

	@Override
	public String getLifecycleStatusText() {
		String lc = getLifecycleStatus();
		return ModelUtil.getLifecycleStatusText(lc);
	}

}
