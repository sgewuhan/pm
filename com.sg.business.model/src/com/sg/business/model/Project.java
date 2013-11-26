package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelRelation;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.dataset.calendarsetting.SystemCalendar;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.UIConstants;

/**
 * 项目
 * <p/>
 * 
 * @author jinxitao
 * 
 */
public class Project extends PrimaryObject implements IProjectTemplateRelative,
		ILifecycle, ISchedual, IReferenceContainer {
	/**
	 * 项目编号
	 */
	public static final String F_PROJECT_NUMBER = "projectnumber";

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
	 * 根工作ID
	 */
	public static final String F_WORK_ID = "work_id";

	public static final String F_DESCRIPTION = "description";

	/**
	 * 预算ID
	 */
	public static final String F_BUDGET_ID = "budget_id";

	/**
	 * 适用标准集
	 */
	public static final String F_STANDARD_SET_OPTION = "standardset";

	/**
	 * 产品类型选项集
	 */
	public static final String F_PRODUCT_TYPE_OPTION = "producttype";

	/**
	 * 项目类型选项集
	 */
	public static final String F_PROJECT_TYPE_OPTION = "projecttype";

	/**
	 * 列表类型的字段，工作令号
	 */
	public static final String F_WORK_ORDER = "workorder";

	/**
	 * 项目提交流程
	 */
	public static final String F_WF_COMMIT = "wf_commit";

	/**
	 * 提交流程中的角色指派
	 */
	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment";

	/**
	 * 提交流程是否启用
	 */
	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated";

	/**
	 * 提交流程任务的执行人
	 */
	public static final String F_WF_COMMIT_ACTORS = "wf_commit_actors";
	/**
	 * 项目变更流程
	 */
	public static final String F_WF_CHANGE = "wf_change";

	/**
	 * 项目变更流程是否启用
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	/**
	 * 项目变更流程角色指派
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";

	/**
	 * 项目变更流程任务执行人
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	/**
	 * 项目编辑器
	 */
	public static final String EDITOR_CREATE_PLAN = "project.editor";

	/**
	 * 项目流程设置
	 */
	public static final String EDITOR_SETPROCESS = "project.flow.setting";

	/**
	 * 项目预算
	 */
	public static final String EDITOR_PAGE_BUDGET = "project.financial";

	public static final String EDITOR_PAGE_TEAM = "project.team";

	/**
	 * 项目工作分解结构
	 */

	public static final String EDITOR_PAGE_WBS = "project.wbs";

	public static final String EDITOR_PAGE_CHANGE_PROCESS = "processpage2";

	public static final String EDITOR_PAGE_COMMIT_PROCESS = "processpage1";

	public static final String F_FOLDER_ID = "folder_id";

	private SummaryProjectWorks summaryProjectWorks;

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "项目";
	}

	/**
	 * 返回显示图标
	 */
	@Override
	public Image getImage() {
		String lc = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PROJECT_CANCEL_16);
		} else if (STATUS_FINIHED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PROJECT_FINISH_16);
		} else if (STATUS_ONREADY_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PROJECT_ONREADY_16);
		} else if (STATUS_PAUSED_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PROJECT_PAUSED_16);
		} else if (STATUS_WIP_VALUE.equals(lc)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PROJECT_WIP_16);
		}

		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}

	/**
	 * 返回项目负责人
	 * 
	 * @return User
	 */
	public User getCharger() {
		String chargerId = getChargerId();
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return UserToolkit.getUserById(chargerId);
	}

	public String getChargerId() {
		return (String) getValue(F_CHARGER);
	}

	/**
	 * 返回项目的发起组织
	 * 
	 * @return Organization
	 */
	public List<PrimaryObject> getLaunchOrganization() {
		List<?> orgIds = getLaunchOrganizationId();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (orgIds != null) {
			for (int i = 0; i < orgIds.size(); i++) {
				result.add(ModelService.createModelObject(Organization.class,
						(ObjectId) orgIds.get(i)));
			}
		}
		return result;
	}

	/**
	 * 返回项目的管理组织
	 * 
	 * @return Organization
	 */
	public Organization getFunctionOrganization() {
		ObjectId orgId = getFunctionOrganizationId();
		if (orgId != null) {
			return ModelService.createModelObject(Organization.class,
					(ObjectId) orgId);
		}
		return null;
	}

	/**
	 * 返回项目发起组织_id
	 * 
	 * @return ObjectId
	 */
	public List<?> getLaunchOrganizationId() {
		return (List<?>) getValue(F_LAUNCH_ORGANIZATION);
	}

	/**
	 * 返回项目管理组织_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	/**
	 * 返回项目模板
	 * 
	 * @return ProjectTemplate
	 */
	public ProjectTemplate getProjectTemplate() {
		ObjectId id = getProjectTemplateId();
		if (id != null) {
			return ModelService.createModelObject(ProjectTemplate.class, id);
		}
		return null;
	}

	/**
	 * 返回项目模板_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

	/**
	 * 返回项目标准集
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStandardSetOptions() {
		return (List<String>) getValue(F_STANDARD_SET_OPTION);
	}

	/**
	 * 返回产品类型集选项集
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProductTypeOptions() {
		return (List<String>) getValue(F_PRODUCT_TYPE_OPTION);
	}

	/**
	 * 返回项目类型选项集
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProjectTypeOptions() {
		return (List<String>) getValue(F_PROJECT_TYPE_OPTION);
	}

	/**
	 * 返回项目预算
	 * 
	 * @return ProjectBudget
	 */
	public ProjectBudget getBudget() {
		DBCollection col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		DBObject data = col.findOne(new BasicDBObject().append(
				ProjectBudget.F_PROJECT_ID, get_id()));
		return ModelService.createModelObject(data, ProjectBudget.class);
	}

	/**
	 * 返回项目的WBS结构根工作
	 * 
	 * @return Work
	 */
	public Work getWBSRoot() {
		ObjectId workid = (ObjectId) getValue(F_WORK_ID);
		return ModelService.createModelObject(Work.class, workid);
	}

	/**
	 * 返回根工作的下级工作
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getChildrenWork() {
		Work root = getWBSRoot();
		return root.getChildrenWork();
	}

	public Map<ObjectId, List<PrimaryObject>> getRoleAssignmentMap() {
		List<PrimaryObject> roles = getProjectRole();
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

	/**
	 * 获取项目角色
	 * 
	 * @return
	 */
	public List<PrimaryObject> getProjectRole() {
		return getRelationById(F__ID, ProjectRole.F_PROJECT_ID,
				ProjectRole.class);
	}

	public List<PrimaryObject> getCalendarCondition() {
		ModelRelation mr = ModelService.getModelRelation("project_calendar");
		return getRelationByModel(mr);
	}

	/**
	 * 获得项目日历计算器
	 * 
	 * @return
	 */
	public CalendarCaculater getCalendarCaculater() {
		List<PrimaryObject> conditions = getCalendarCondition();
		return new CalendarCaculater(conditions);
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
		wbsRootData.put(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);
		wbsRootData.put(Work.F_PROJECT_ID, get_id());
		ObjectId wbsRootId = new ObjectId();
		wbsRootData.put(Work.F__ID, wbsRootId);
		wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
		wbsRootData.put(Work.F_IS_PROJECT_WBSROOT, Boolean.TRUE);
		return ModelService.createModelObject(wbsRootData, Work.class);
	}

	private Folder makeFolderRoot() {
		BasicDBObject folderRootData = new BasicDBObject();
		folderRootData.put(Folder.F_DESC, getDesc());
		folderRootData.put(Folder.F_PROJECT_ID, get_id());
		ObjectId folderRootId = new ObjectId();
		folderRootData.put(Folder.F__ID, folderRootId);
		folderRootData.put(Folder.F_ROOT_ID, folderRootId);
		folderRootData.put(Folder.F_IS_PROJECT_FOLDERROOT, Boolean.TRUE);
		String containerCollection, containerDB;
		containerCollection = IModelConstants.C_ORGANIZATION;
		Container container = Container.adapter(this,
				Container.TYPE_ADMIN_GRANTED);
		containerDB = (String) container.getValue(Container.F_SOURCE_DB);
		folderRootData.put(Folder.F_CONTAINER_DB, containerDB);
		folderRootData.put(Folder.F_CONTAINER_COLLECTION, containerCollection);
		return ModelService.createModelObject(folderRootData, Folder.class);
	}

	public ProjectRole makeProjectRole(ProjectRole po) {
		if (po == null) {
			po = ModelService.createModelObject(ProjectRole.class);
		}
		po.setValue(ProjectRole.F_PROJECT_ID, get_id());
		return po;
	}

	public BulletinBoard makeBulletinBoard(BulletinBoard po) {
		if (po == null) {
			po = ModelService.createModelObject(BulletinBoard.class);
		}
		po.setValue(BulletinBoard.F_PROJECT_ID, get_id());
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
	public boolean doSave(IContext context) throws Exception {
		// 同步工作令号到公司
		ensureWorkOrderRelativeToCompany(context);

		boolean saved = super.doSave(context);
		if (saved) {
			// 同步项目经理角色
			// [bug:18]
			// 确保项目经理角色的人员与项目负责人一致
			ensureProjectManagerRole(context);

			// 同步更改根工作定义的名称
			syncRootWorkNameInternal();

		}
		return saved;
	}

	private void syncRootWorkNameInternal() {
		Job job = new Job("处理根工作定义名称同步") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Object rootId = getValue(F_WORK_ID);
				DBCollection col = getCollection(IModelConstants.C_WORK);
				col.update(new BasicDBObject().append(Work.F__ID, rootId),
						new BasicDBObject().append("$set", new BasicDBObject()
								.append(Work.F_DESC, getDesc())));
				return Status.OK_STATUS;
			}

		};

		job.schedule();
	}

	private void ensureWorkOrderRelativeToCompany(IContext context)
			throws Exception {
		Object wonList = getValue(F_WORK_ORDER);
		if (wonList instanceof List<?>) {
			Organization org = getFunctionOrganization();
			Organization company = org.getCompany();
			if (company == null) {
				throw new Exception("项目所在管理组织或上级没有定义公司代码,无法保存工作令号，项目保存失败");
			}
			company.doSaveWorkOrders((List<?>) wonList);
		}
	}

	/**
	 * 确保项目经理角色的人员与项目负责人一致
	 * 
	 * @param context
	 * @throws Exception
	 */
	private void ensureProjectManagerRole(final IContext context) {
		Job job = new Job("确保项目经理角色与项目负责人一致") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				User charger = getCharger();
				if (charger == null) {
					return Status.OK_STATUS;
				}

				List<User> users = new ArrayList<User>();
				users.add(charger);

				List<PrimaryObject> roles = getProjectRole();
				for (int i = 0; i < roles.size(); i++) {
					ProjectRole projectRole = (ProjectRole) roles.get(i);
					String rn = projectRole.getRoleNumber();
					if (ProjectRole.ROLE_PROJECT_MANAGER_ID.equals(rn)) {
						try {
							projectRole.doAssignUsers(users, context);
						} catch (Exception e) {
						}
					}
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID, new ObjectId());

		// 生成编码
		generateCode();

		// 创建根工作定义
		Work root = makeWBSRoot();
		root.doInsert(context);
		setValue(Project.F_WORK_ID, root.get_id());

		// 创建根文件夹
		Folder folderRoot = makeFolderRoot();
		folderRoot.doInsert(context);
		setValue(Project.F_FOLDER_ID, folderRoot.get_id());

		// 预算
		ProjectBudget budget = makeBudget(context);
		budget.doInsert(context);

		super.doInsert(context);

		doInsertAfter(root, folderRoot, context);

	}

	private void doInsertAfter(final Work root, final Folder folderRoot,
			final IContext context) throws Exception {

		// 复制模板
		try {
			doSetupWithTemplate(root.get_id(), context, folderRoot.get_id());
		} catch (Exception e) {
			// return new Status(Status.ERROR, ModelActivator.PLUGIN_ID,
			// Status.ERROR, "复制模板出错", e);

			throw new Exception("复制模板出错");
		}
		Job job = new Job("从模板复制项目信息") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// 复制系统日历
				try {
					doCopySystemCanlendar();
				} catch (Exception e) {
					return new Status(Status.ERROR, ModelActivator.PLUGIN_ID,
							Status.ERROR, "复制系统日历出错", e);
				}

				// //自动设置任务执行人
				// doAssignmentByRole(context);
				return Status.OK_STATUS;
			}

		};
		job.schedule();

	}

	private void generateCode() throws Exception {
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception("缺少项目管理职能组织");
		}

		String code = org.getCode();
		if (code == null) {
			throw new Exception("项目管理职能组织没有定义代码");
		}

		DBCollection ids = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C__IDS);

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		String prefix = code + ("" + year).substring(2);
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_PROJECT_NUMBER
				+ "." + prefix);
		String seq = String.format("%03x", id).toUpperCase();

		String codeValue = prefix + seq;
		setValue(F_PROJECT_NUMBER, codeValue);

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

		if (ins.isEmpty()) {
			return;
		}
		DBCollection col = getCollection(IModelConstants.C_CALENDAR_SETTING);
		WriteResult ws = col.insert(ins, WriteConcern.NORMAL);
		checkWriteResult(ws);
	}

	/**
	 * 
	 * @param context
	 * @param wbsRootId
	 * @param folderRootId
	 * @throws Exception
	 */
	public void doSetupWithTemplate(ObjectId wbsRootId, IContext context,
			ObjectId folderRootId) throws Exception {
		ObjectId id = getProjectTemplateId();
		if (id == null) {
			return;
		}

		// 复制角色定义
		Map<ObjectId, DBObject> roleMap = doMakeRolesWithTemplate(id, context);

		// 复制工作定义
		Map<ObjectId, DBObject> workMap = doSetupWBSWithTemplate(id, wbsRootId,
				folderRootId, roleMap, context);

		// 复制工作的前后序关系
		doSetupWorkConnectionWithTemplate(id, workMap, context);

		// 设置项目的流程和角色
		doSetupWorkflowWithTemplate(id, roleMap, context);

		// 排除没有应用的角色
		if (!roleMap.isEmpty()) {
			Collection<DBObject> values = roleMap.values();
			Iterator<DBObject> iter = values.iterator();

			Set<DBObject> toBeInsert = new HashSet<DBObject>();
			while (iter.hasNext()) {
				DBObject prole = iter.next();
				if (!Boolean.TRUE.equals(prole.get("used"))) {
					continue;
				}
				Object roleId = prole.get(ProjectRole.F_ORGANIZATION_ROLE_ID);
				if (roleId != null) {
					// 将组织角色中的成员加入到项目的参与者
					Role role = ModelService.createModelObject(Role.class,
							(ObjectId) roleId);
					List<PrimaryObject> ass = role.getAssignment();
					doAddParticipateFromAssignment(ass);

				}

				prole.removeField("used");
				toBeInsert.add(prole);
			}

			DBObject[] insertData = toBeInsert.toArray(new DBObject[0]);
			// 插入到数据库
			DBCollection col_role = getCollection(IModelConstants.C_PROJECT_ROLE);

			WriteResult ws = col_role.insert(insertData, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

	}

	private void doSetupWorkflowWithTemplate(ObjectId id,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {

		DBCollection col = getCollection(IModelConstants.C_PROJECT_TEMPLATE);
		DBObject pjTempData = col.findOne(new BasicDBObject().append(
				ProjectTemplate.F__ID, getProjectTemplateId()));
		if (pjTempData == null) {
			return;
		}

		BasicDBObject update = new BasicDBObject();

		// 设置变更工作流
		Object value = pjTempData.get(ProjectTemplate.F_WF_CHANGE);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_CHANGE, value);
		}

		// 设置变更工作流是否激活
		value = pjTempData.get(ProjectTemplate.F_WF_CHANGE_ACTIVATED);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_CHANGE_ACTIVATED, value);
		}

		// 设置变更流程的活动执行人
		ProjectToolkit.setRoleDBObjectField(update, pjTempData,
				ProjectTemplate.F_WF_CHANGE_ASSIGNMENT, roleMap);

		// 设置执行工作流
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_COMMIT, value);
		}

		// 设置执行工作流是否激活
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT_ACTIVATED);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_COMMIT_ACTIVATED, value);
		}

		// 设置执行工作流的活动执行人角色
		ProjectToolkit.setRoleDBObjectField(update, pjTempData,
				ProjectTemplate.F_WF_COMMIT_ASSIGNMENT, roleMap);

		col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", update));

		checkWriteResult(ws);

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

		if (result.isEmpty()) {
			return;
		}
		DBCollection col = getCollection(IModelConstants.C_WORK_CONNECTION);
		WriteResult ws = col.insert(result);
		checkWriteResult(ws);
	}

	private Map<ObjectId, DBObject> doMakeRolesWithTemplate(
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
				// // 将组织角色中的成员加入到项目的参与者
				// Role role = ModelService.createModelObject(Role.class,
				// (ObjectId) roleId);
				// List<PrimaryObject> ass = role.getAssignment();
				// doAddParticipateFromAssignment(ass);

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

		return result;
	}

	private HashMap<ObjectId, DBObject> doSetupWBSWithTemplate(
			ObjectId projectTemplateId, ObjectId wbsRootId,
			ObjectId folderRootId, Map<ObjectId, DBObject> roleMap,
			IContext context) throws Exception {

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

		ProjectToolkit.copyWBSTemplate(rootWorkDefId, wbsRootId, wbsRootId,
				this, roleMap, worksToBeInsert, folderRootId,
				documentsToBeInsert, deliverableToInsert, fileToCopy, context);

		// 保存工作
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// 清除已有的非根工作定义
		WriteResult ws = workCol.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()).append(Work.F__ID,
				new BasicDBObject().append("$ne", wbsRootId)));
		checkWriteResult(ws);
		Collection<DBObject> collection = worksToBeInsert.values();
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

		collection = documentsToBeInsert.values();
		if (!collection.isEmpty()) {
			// 保存文档时需要文档的保存前的预处理
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

		String lc = getLifecycleStatus();
		if (!STATUS_NONE_VALUE.equals(lc)) {
			throw new Exception("您只能在项目准备状态进行按角色指派工作");
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

		// 删除文件夹
		col = getCollection(IModelConstants.C_FOLDER);
		ws = col.remove(new BasicDBObject().append(Folder.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// 删除文档
		col = getCollection(IModelConstants.C_DOCUMENT);
		ws = col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		super.doRemove(context);
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

	/**
	 * 获取项目参与者<br/>
	 * 
	 * @return 由每个元素为用户的userid组成的List,有可能为空
	 */
	public List<?> getParticipatesIdList() {
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
		List<PrimaryObject> rd = getProjectRole();
		Map<ObjectId, List<PrimaryObject>> raMap = getRoleAssignmentMap();
		List<PrimaryObject> ra;
		boolean passed = true;
		for (int i = 0; i < rd.size(); i++) {
			ProjectRole role = (ProjectRole) rd.get(i);
			ObjectId roldId = role.get_id();
			ra = raMap.get(roldId);
			if (ra == null) {
				CheckListItem checkItem = new CheckListItem("检查项目角色指派",
						"没有确定角色对应人员，" + "角色：[" + role.getLabel() + "]",
						"如果本项目在提交后确定人员，请忽略本提示", ICheckListItem.WARRING);
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

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 4.1 检查项目变更的流程 ：错误，没有指明流程负责人
		String title = "检查项目变更流程";
		String process = F_WF_CHANGE;
		String editorId = EDITOR_SETPROCESS;
		String pageId = EDITOR_PAGE_CHANGE_PROCESS;
		passed = ProjectToolkit.checkProcessInternal(this, pc, result, raMap,
				title, process, editorId, pageId);
		if (passed) {
			CheckListItem checkItem = new CheckListItem(title);
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 4.2 检查项目提交的流程 ：错误，没有指明流程负责人
		title = "检查项目提交流程";
		process = F_WF_COMMIT;
		pageId = EDITOR_PAGE_COMMIT_PROCESS;
		passed = ProjectToolkit.checkProcessInternal(this, pc, result, raMap,
				title, process, editorId, pageId);
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

	public String getLifecycleStatus() {
		String lc = (String) getValue(F_LIFECYCLE);
		if (lc == null) {
			return STATUS_NONE_VALUE;
		} else {
			return lc;
		}
	}

	@Override
	public boolean canCheck() {
		// 未完成和未取消的
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))
				&& (!STATUS_FINIHED_VALUE.equals(lc));
	}

	@Override
	public boolean canCommit() {
		String lc = getLifecycleStatus();

		return STATUS_NONE_VALUE.equals(lc);
	}

	@Override
	public boolean canStart() {
		String lc = getLifecycleStatus();
		return STATUS_ONREADY_VALUE.equals(lc)
				|| STATUS_PAUSED_VALUE.equals(lc);
	}

	@Override
	public boolean canPause() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc);
	}

	@Override
	public boolean canFinish() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
	}

	@Override
	public boolean canCancel() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
	}

	@Override
	public boolean canEdit(IContext context) {
		// 如果项目已经完成、取消，不能编辑
		String lc = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lc) || STATUS_FINIHED_VALUE.equals(lc)) {
			return false;
		}

		String userId = context.getAccountInfo().getConsignerId();
		Organization org = getFunctionOrganization();
		if (org != null) {
			Role role = org.getRole(Role.ROLE_PROJECT_ADMIN_ID, 0);
			if (role != null) {
				List<PrimaryObject> assignmentList = role.getAssignment();
				if (assignmentList != null && assignmentList.size() > 0) {
					for (PrimaryObject po : assignmentList) {
						RoleAssignment roleAssignment = (RoleAssignment) po;
						String assignmentuserId = roleAssignment.getUserid();
						if (userId.equals(assignmentuserId)) {
							return true;
						}
					}
				}
			}
		}

		// 如果是项目负责人，可以编辑
		String chargerId = getChargerId();
		if (!userId.equals(chargerId)) {
			return false;
		}

		return true;
		// return super.canEdit(context);
	}

	@Override
	public boolean canDelete(IContext context) {
		String lc = getLifecycleStatus();
		if (!STATUS_NONE_VALUE.equals(lc) && !STATUS_ONREADY_VALUE.equals(lc)) {
			return false;
		}

		String chargerId = getChargerId();
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(chargerId)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean canRead(IContext context) {
		// 如果是项目参与者，可以打开查看
		List<?> participates = getParticipatesIdList();
		String userId = context.getAccountInfo().getConsignerId();

		return participates != null && participates.contains(userId);
		// return super.canRead(context);
	}

	@Override
	public String getLifecycleStatusText() {
		String lc = getLifecycleStatus();
		return LifecycleToolkit.getLifecycleStatusText(lc);
	}

	public void doCommitWithSendMessage(IContext context) throws Exception {
		Map<String, Message> msgList = getCommitMessage(context);
		Iterator<Message> iter = msgList.values().iterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			message.doSave(context);
		}

		doReady(context);
	}

	public void doCommitWithWork(Work work) {

	}

	/**
	 * 发送消息进行提交：<br/>
	 * 需要发送到项目的负责人，（消息需要关联到项目），<br/>
	 * 
	 * @param context
	 * 
	 * @param context
	 * @throws Exception
	 */
	private Map<String, Message> getCommitMessage(IContext context)
			throws Exception {
		Map<String, Message> messageList = new HashMap<String, Message>();
		// 1. 获取项目负责人
		String title = "项目计划提交" + " " + this;
		appendMessageForCharger(messageList, title, context);
		// 2. 获取项目的参与者

		appendMessageForParticipate(messageList, title, context);

		// 3. 项目流程通知
		appendMessageForChangeWorkflowActor(messageList, title, context);

		// 4. 遍历工作
		Work root = getWBSRoot();
		messageList = root.getCommitMessage(messageList, title, context);

		MessageToolkit.appendProjectCommitMessageEndContent(messageList);
		return messageList;
	}

	/**
	 * 向消息清单中添加项目负责人的提醒消息
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForCharger(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "负责项目"
				+ ": " + getLabel(), this, EDITOR_CREATE_PLAN, context);
	}

	/**
	 * 向消息清单中添加项目的参与者提醒消息
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForParticipate(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit.appendMessage(messageList, getChargerId(), title, "参与项目"
				+ ": " + getLabel(), this, EDITOR_CREATE_PLAN, context);
	}

	/**
	 * 向消息清单中添加工作流活动执行者的提示消息
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_CHANGE, "项目变更流程", title, context.getAccountInfo()
						.getConsignerId(), null);
	}

	/**
	 * 使用工作流进行提交，创建一个工作，并将工作流绑定到该工作
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Work makeWorkflowCommitableWork(Work work, IContext context)
			throws Exception {
		if (work == null) {
			work = ModelService.createModelObject(Work.class);
			work.setValue(Work.F_CHARGER, context.getAccountInfo()
					.getConsignerId());// 设置负责人为当前用户
		}
		work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// 设置该工作的状态为准备中，以便自动开始
		work.setValue(Work.F_USE_PROJECT_ROLE, Boolean.TRUE);
		Date today = new Date();
		work.setValue(Work.F_PLAN_START, today);

		Date finishDate = today;
		Object sDuration = Setting
				.getSystemSetting(IModelConstants.S_DEFAULT_PROJECT_COMMIT_DURATION);
		if (sDuration != null) {
			Integer duration = Utils.getIntegerValue(sDuration);
			if (duration != null) {
				CalendarCaculater ds = getCalendarCaculater();
				while (duration > 0) {
					finishDate = Utils.getDateAfter(finishDate, 1);
					if (ds.getWorkingTime(finishDate) > 0) {
						duration--;
					}
				}
			}
		}
		work.setValue(Work.F_PLAN_FINISH, finishDate);

		work.setValue(Work.F_DESC, "项目计划提交" + " " + this);
		work.setValue(Work.F_DESCRIPTION, getDesc());
		work.setValue(Work.F_PLAN_WORKS, new Double(0d));
		BasicBSONList targets = new BasicBSONList();
		targets.add(new BasicDBObject().append(SF_TARGET, get_id())
				.append(SF_TARGET_CLASS, Project.class.getName())
				.append(SF_TARGET_EDITING_TYPE, UIConstants.EDITING_BY_EDITOR)
				.append(SF_TARGET_EDITOR, EDITOR_CREATE_PLAN)
				.append(SF_TARGET_EDITABLE, Boolean.TRUE));
		work.setValue(Work.F_TARGETS, targets);
		work.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);

		work.setValue(Work.F_PROJECT_ID, get_id());
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		DBObject wfdef = pc.getWorkflowDefinition(F_WF_COMMIT);
		work.bindingWorkflowDefinition(Work.F_WF_EXECUTE, wfdef);
		return work;
	}

	/**
	 * 取消项目
	 */
	public Object doCancel(IContext context) throws Exception {
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception("项目无管理部门或管理部门被删除，" + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception("项目管理部门及其上级部门无文档容器，" + this);
		}

		Work root = getWBSRoot();
		root.doCancel(context);

		doChangeLifecycleStatus(context, STATUS_CANCELED_VALUE);

		// 发送项目消息到参与者
		doNoticeProjectAction(context, "已取消");

		doArchive(context);

		return this;
	}

	/**
	 * 完成项目
	 */
	public Object doFinish(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doFinish(context);

		DBCollection col = getCollection();
		DBObject data = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()),
				null,
				null,
				false,
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(F_LIFECYCLE,
								STATUS_FINIHED_VALUE).append(F_ACTUAL_FINISH,
								new Date())),

				true, false);
		set_data(data);

		// 发送项目消息到参与者
		doNoticeProjectAction(context, "已完成");

		doArchive(context);

		return this;

	}

	public void doArchive(IContext context) throws Exception {
		// 1.归档项目角色
		BasicDBObject q = new BasicDBObject();
		q.put(IProjectRelative.F_PROJECT_ID, get_id());

		// 删除角色指派
		DBCollection col = getCollection(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		WriteResult ws = col.remove(q);
		checkWriteResult(ws);

		// 删除项目角色
		col = getCollection(IModelConstants.C_PROJECT_ROLE);
		ws = col.remove(q);
		checkWriteResult(ws);

		// 归档项目工作
		col = getCollection(IModelConstants.C_WORK);
		BasicDBObject update = new BasicDBObject();
		update.put(Work.F_ARCHIVE, Boolean.TRUE);
		for (String archiveField : Work.ARCHIVE_FIELDS) {
			update.put(archiveField, null);
		}
		ws = col.update(q, new BasicDBObject().append("$set", update), false,
				true);
		checkWriteResult(ws);

		// 归档项目文件
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception("项目无管理部门或管理部门被删除，" + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception("项目管理部门及其上级部门无文档容器，" + this);
		}

		col = getCollection(IModelConstants.C_FOLDER);
		ws = col.update(q, new BasicDBObject().append("$set",
				new BasicDBObject().append(Folder.F_ROOT_ID,
						containerOrganizationId)), false, true);
		checkWriteResult(ws);

		// 归档项目公告
		col = getCollection(IModelConstants.C_BULLETINBOARD);
		ws = col.remove(q);
		checkWriteResult(ws);

		// 写日志
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "项目归档",
				new Date(), this.getLabel(), IModelConstants.DB);
	}

	/**
	 * 暂停项目
	 */
	public Object doPause(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doPause(context);

		doChangeLifecycleStatus(context, STATUS_PAUSED_VALUE);

		// 发送项目消息到参与者
		doNoticeProjectAction(context, "已暂停");
		return this;
	}

	/**
	 * 启动项目
	 */
	public Object doStart(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doStart(context);

		DBCollection col = getCollection();
		DBObject data = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()),
				null,
				null,
				false,
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(F_LIFECYCLE,
								STATUS_WIP_VALUE).append(F_ACTUAL_START,
								new Date())),

				true, false);
		set_data(data);

		// 发送项目消息到参与者
		doNoticeProjectAction(context, "已启动");
		return this;
	}

	/**
	 * 发送项目操作完成的通知
	 * 
	 * @param context
	 *            当前的上下文
	 * @param actionName
	 *            操作的文本名称
	 * @return
	 * @throws Exception
	 *             发送消息出现的错误
	 */
	public Message doNoticeProjectAction(IContext context, String actionName)
			throws Exception {
		// 设置收件人
		BasicBSONList participatesIdList = (BasicBSONList) getParticipatesIdList();
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
		sb.append("您参与的项目有新的进展。");
		sb.append("<br/><br/>");

		sb.append(context.getAccountInfo().getUserId() + "|"
				+ context.getAccountInfo().getUserName());
		sb.append(", ");
		sb.append(actionName);
		sb.append("项目");
		sb.append("\"");
		sb.append(this);
		sb.append("\"");

		sb.append("<br/><br/>");
		sb.append("如有不明，请查阅有关工作信息。");

		Message message = MessageToolkit.makeMessage(participatesIdList, title,
				context.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// 设置导航附件
		message.appendTargets(this, EDITOR_CREATE_PLAN, false);

		message.doSave(context);

		return message;
	}

	/**
	 * 将项目的状态更改为准备中
	 * 
	 * @param context
	 */
	public void doReady(IContext context) throws Exception {
		doChangeLifecycleStatus(context, STATUS_ONREADY_VALUE);
	}

	/**
	 * 更改生命周期状态
	 * 
	 * @param context
	 * @param status
	 */
	private void doChangeLifecycleStatus(IContext context, String status) {
		DBCollection col = getCollection();
		DBObject data = col.findAndModify(
				new BasicDBObject().append(F__ID, get_id()),
				null,
				null,
				false,
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_LIFECYCLE, status)),

				true, false);
		set_data(data);
	}

	@Override
	public BasicBSONList getTargetList() {
		return (BasicBSONList) getValue(F_TARGETS);
	}

	@Override
	public List<Object[]> checkStartAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.检查是否本项目的负责人
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("不是本项目负责人，" + this);
		}
		// 2.检查项目的工作是否满足启动条件
		Work work = getWBSRoot();
		message.addAll(work.checkCascadeStart(false));
		return message;
	}

	@Override
	public List<Object[]> checkCancelAction(IContext context) throws Exception {
		// 1.检查是否本项目的负责人
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("不是本项目负责人，" + this);
		}

		// 2.检查项目是否可以进行归档
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception("项目无管理部门或管理部门被删除，" + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception("项目管理部门及其上级部门无文档容器，" + this);
		}
		// TODO 归档判断是否完整

		return null;
	}

	@Override
	public List<Object[]> checkFinishAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.检查是否本项目的负责人
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("不是本项目负责人，" + this);
		}
		// 2.检查项目的工作是否满足启动条件
		Work work = getWBSRoot();
		message.addAll(work.checkCascadeFinish(work.get_id()));

		// 3.检查项目是否可以进行归档
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception("项目无管理部门或管理部门被删除，" + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception("项目管理部门及其上级部门无文档容器，" + this);
		}
		// TODO 归档判断是否完整

		return message;
	}

	@Override
	public List<Object[]> checkPauseAction(IContext context) throws Exception {
		// 1.检查是否本项目的负责人
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("不是本项目负责人，" + this);
		}
		return null;
	}

	/**
	 * 是否允许提交项目
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void checkCommitAction(IContext context) throws Exception {
		reload();

		// 1.检查是否本项目的负责人
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("不是本项目负责人，" + this);
		}

		// 需要检查是否已经创建了提交工作，如果是，则不能提交，如果生命周期状态不对，也不能提交

		// 判断当前项目状态是否可以进行提交
		String lc = getLifecycleStatus();
		if (!STATUS_NONE_VALUE.equals(lc)) {
			throw new Exception("项目当前状态不允许进行提交，" + this);
		}

		// 判断是否具有提交工作
		BasicDBObject condition = new BasicDBObject();
		condition.put(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		condition.put(Work.F_PROJECT_ID, get_id());
		condition.put(
				Work.F_LIFECYCLE,
				new BasicDBObject().append("$nin", new String[] {
						STATUS_CANCELED_VALUE, STATUS_FINIHED_VALUE }));

		if (getRelationCountByCondition(Work.class, condition) > 0) {
			throw new Exception("该项目已经进行过提交操作，" + this);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IProcessControl.class)) {
			return (T) new ProcessControl(this) {
				@Override
				protected Class<? extends PrimaryObject> getRoleDefinitionClass() {
					return ProjectRole.class;
				}
			};
		} else if (adapter == IWorksSummary.class) {
			if (summaryProjectWorks == null) {
				summaryProjectWorks = new SummaryProjectWorks(this);
			}
			return (T) summaryProjectWorks;
		}
		return super.getAdapter(adapter);
	}

	public String getProjectNumber() {
		return (String) getValue(F_PROJECT_NUMBER);
	}

	public String[] getWorkOrders() {
		Object workOrder = getValue(F_WORK_ORDER);
		if (workOrder instanceof List<?>) {
			List<?> list = (List<?>) workOrder;
			return list.toArray(new String[] {});
		}
		return new String[] {};
	}

	public Date getPlanStart() {
		return (Date) getValue(F_PLAN_START);
	}

	public Date getPlanFinish() {
		return (Date) getValue(F_PLAN_FINISH);
	}

	public Date getActualStart() {
		return (Date) getValue(F_ACTUAL_START);
	}

	public Date getActualFinish() {
		return (Date) getValue(F_ACTUAL_FINISH);

	}

	/**
	 * 移交用户
	 * 
	 * @param changedUserId
	 * @param changeUserId
	 * @param changeWork
	 * @throws Exception
	 */
	public void doChangeUsers(String changedUserId, String changeUserId,
			List<PrimaryObject> changeWork, IContext context) throws Exception {

		String messageContent = "";
		for (PrimaryObject po : changeWork) {
			Work work = (Work) po;
			String changeWorkUserMessageContent = work.changeWorkUser(
					changedUserId, changeUserId);
			if (changeWorkUserMessageContent != null) {
				messageContent = messageContent + "<br/>"
						+ changeWorkUserMessageContent;
			}
		}
		if (messageContent != "") {
			List<?> oldParticipatesIdList = getParticipatesIdList();
			BasicBSONList newParticipatesIdList = new BasicDBList();
			if (oldParticipatesIdList != null) {
				boolean bchange = true;
				for (int i = 0; i < oldParticipatesIdList.size(); i++) {
					String userId = (String) oldParticipatesIdList.get(i);
					if (userId.equals(changeUserId)) {
						bchange = false;
					}
					newParticipatesIdList.add(userId);
				}
				if (bchange) {
					newParticipatesIdList.add(changeUserId);
					DBCollection userCol = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_PROJECT);
					userCol.update(new BasicDBObject().append(F__ID, get_id()),
							new BasicDBObject().append("$set",
									new BasicDBObject().append(F_PARTICIPATE,
											newParticipatesIdList)), false,
							true);
				}
			}

			Message message = ModelService.createModelObject(Message.class);
			message.setValue(Message.F_RECIEVER, newParticipatesIdList);
			message.setValue(Message.F_DESC, "工作移交通知");
			message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
			message.setValue(
					Message.F_CONTENT,
					"<span style='font-size:14px'>"
							+ "您好: "
							+ "</span><br/><br/>"
							+ UserToolkit.getUserById(changeUserId)
									.getUsername()
							+ "将代替"
							+ UserToolkit.getUserById(changedUserId)
									.getUsername() + "作为：<br/>"
							+ messageContent);
			message.appendTargets(this, EDITOR_CREATE_PLAN, Boolean.TRUE);
			message.doSave(context);
		}
	}

	public List<Object[]> checkChangeUser(String changedUserId,
			String changeUserId, List<PrimaryObject> changeWork) {
		List<Object[]> message = new ArrayList<Object[]>();
		String lifecycleStatus = getLifecycleStatus();

		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { "项目已经取消，无法进行修改", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { "项目已经完成，无法进行修改", this, SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "项目在进行中，不会修改项目流程执行人", this,
					SWT.ICON_WARNING });
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { "项目已经暂停，不会修改项目流程执行人", this,
					SWT.ICON_WARNING });
		}

		for (PrimaryObject po : changeWork) {
			Work work = (Work) po;
			message.addAll(work
					.checkChangeWorkUser(changedUserId, changeUserId));
		}

		return message;
	}

	public boolean canChangUser(IContext context) {
		// 未完成和未取消的
		String lc = getLifecycleStatus();
		return (!STATUS_FINIHED_VALUE.equals(lc))
				&& (!STATUS_CANCELED_VALUE.equals(lc) && (!STATUS_PAUSED_VALUE
						.equals(lc)));
	}

	public ObjectId getFolderRootId() {
		return (ObjectId) getValue(F_FOLDER_ID);
	}

	public Folder getFolderRoot() {
		ObjectId id = getFolderRootId();
		if (id != null) {
			return ModelService.createModelObject(Folder.class, id);
		}

		return null;
	}

	/**
	 * 取出项目所有的里程碑工作,按照计划开始时间，从小到大排序
	 * 
	 * @return
	 */
	public List<Work> getMileStoneWorks() {
		List<Work> result = new ArrayList<Work>();
		DBCollection col = getCollection(IModelConstants.C_WORK);
		DBCursor cur = col.find(
				new BasicDBObject().append(Work.F_PROJECT_ID, get_id()).append(
						Work.F_MILESTONE, Boolean.TRUE)).sort(
				new BasicDBObject().append(Work.F_PLAN_START, -1));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			result.add(ModelService.createModelObject(dbo, Work.class));
		}
		return result;
	}

	/**
	 * 项目已经延期，当前的时间已经超过了项目的计划完成时间
	 * 
	 * @return
	 */
	public boolean isDelay() {
		Date pf = getPlanFinish();
		if (pf != null) {
			return new Date().after(pf);
		} else {
			return false;
		}
	}

	public boolean isAdvanced() {
		String lc = getLifecycleStatus();
		if (STATUS_FINIHED_VALUE.equals(lc)) {
			Date pf = getPlanFinish();
			if (pf != null) {
				return new Date().before(pf);
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean maybeDelay() {
		if (!isDelay()) {
			List<Work> milestones = getMileStoneWorks();
			for (int i = 0; i < milestones.size(); i++) {
				if (milestones.get(i).isDelayNow()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean maybeAdvanced() {
		if (!isAdvanced()) {
			List<Work> milestones = getMileStoneWorks();
			for (int i = 0; i < milestones.size(); i++) {
				if (!milestones.get(i).isAdvanced()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public List<PrimaryObject> getProduct() {
		return getRelationById(F__ID, ProductItem.F_PROJECT_ID,
				ProductItem.class);
	}

	public void doProductFinal(IContext context, List<?> productList)
			throws Exception {
		ObjectId _id = get_id();
		for (Object object : productList) {
			BasicDBObject q = new BasicDBObject();
			q.put(ProductItem.F_PROJECT_ID, _id);
			q.put(ProductItem.F_DESC, object);
			ProductItem productItem = ModelService.createModelObject(q,
					ProductItem.class);
			productItem.doFinal(context);
		}
	}
	public UserProjectPerf makeUserProjectPerf() {
		UserProjectPerf pperf = ModelService
				.createModelObject(UserProjectPerf.class);
		pperf.setValue(UserProjectPerf.F_PROJECT_ID, get_id());
		return pperf;
	}

	/**
	 * 获得项目截至当前的投资总额（研发成本）
	 * @return
	 */
	public Double getInvestment() {
		// TODO Auto-generated method stub
		return 100000d;
	}
}
