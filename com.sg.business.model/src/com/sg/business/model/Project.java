package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
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
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.dataset.calendarsetting.SystemCalendar;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

/**
 * ��Ŀ
 * <p/>
 * 
 * @author jinxitao
 * 
 */
public class Project extends PrimaryObject implements IProjectTemplateRelative,
		ILifecycle, ISchedual, IReferenceContainer {

	/**
	 * ��Ŀ�������ֶΣ�������Ŀ�����˵�userid {@link User} ,
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * ���������ֶΣ�ÿ��Ԫ��Ϊ����userid
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * ��Ŀ��������Ŀְ����֯
	 */
	public static final String F_FUNCTION_ORGANIZATION = "org_id";

	/**
	 * ��Ŀ������
	 */
	public static final String F_LAUNCH_ORGANIZATION = "launchorg_id";

	/**
	 * ������ID
	 */
	public static final String F_WORK_ID = "work_id";

	/**
	 * Ԥ��ID
	 */
	public static final String F_BUDGET_ID = "budget_id";

	/**
	 * ���ñ�׼��
	 */
	public static final String F_STANDARD_SET_OPTION = "standardset";

	/**
	 * ��Ʒ����ѡ�
	 */
	public static final String F_PRODUCT_TYPE_OPTION = "producttype";

	/**
	 * ��Ŀ����ѡ�
	 */
	public static final String F_PROJECT_TYPE_OPTION = "projecttype";

	/**
	 * �б����͵��ֶΣ��������
	 */
	public static final String F_WORK_ORDER = "workorder";

	/**
	 * ��Ŀ�ύ����
	 */
	public static final String F_WF_COMMIT = "wf_commit";

	/**
	 * �ύ�����еĽ�ɫָ��
	 */
	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment";

	/**
	 * �ύ�����Ƿ�����
	 */
	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated";

	/**
	 * �ύ���������ִ����
	 */
	public static final String F_WF_COMMIT_ACTORS = "wf_commit_actors";
	/**
	 * ��Ŀ�������
	 */
	public static final String F_WF_CHANGE = "wf_change";

	/**
	 * ��Ŀ��������Ƿ�����
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	/**
	 * ��Ŀ������̽�ɫָ��
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";

	/**
	 * ��Ŀ�����������ִ����
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	/**
	 * ��Ŀ�༭��
	 */
	public static final String EDITOR_CREATE_PLAN = "project.editor";

	/**
	 * ��Ŀ��������
	 */
	public static final String EDITOR_SETPROCESS = "project.flow.setting";

	/**
	 * ��ĿԤ��
	 */
	public static final String EDITOR_PAGE_BUDGET = "project.financial";

	public static final String EDITOR_PAGE_TEAM = "project.team";

	/**
	 * ��Ŀ�����ֽ�ṹ
	 */

	public static final String EDITOR_PAGE_WBS = "project.wbs";

	public static final String EDITOR_PAGE_CHANGE_PROCESS = "processpage2";

	public static final String EDITOR_PAGE_COMMIT_PROCESS = "processpage1";

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��Ŀ";
	}

	/**
	 * ������ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}

	/**
	 * ������Ŀ������
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
	 * ������Ŀ�Ĺ�����֯
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
	 * ������Ŀ������֯_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	/**
	 * ������Ŀģ��
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
	 * ������Ŀģ��_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

	/**
	 * ������Ŀ��׼��
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStandardSetOptions() {
		return (List<String>) getValue(F_STANDARD_SET_OPTION);
	}

	/**
	 * ���ز�Ʒ���ͼ�ѡ�
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProductTypeOptions() {
		return (List<String>) getValue(F_PRODUCT_TYPE_OPTION);
	}

	/**
	 * ������Ŀ����ѡ�
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProjectTypeOptions() {
		return (List<String>) getValue(F_PROJECT_TYPE_OPTION);
	}

	/**
	 * ������ĿԤ��
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
	 * ������Ŀ��WBS�ṹ������
	 * 
	 * @return Work
	 */
	public Work getWBSRoot() {
		ObjectId workid = (ObjectId) getValue(F_WORK_ID);
		return ModelService.createModelObject(Work.class, workid);
	}

	/**
	 * ���ظ��������¼�����
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
	 * ��ȡ��Ŀ��ɫ
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
	 * �����Ŀ����������
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
			// ��ȫ�ָ���
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
		boolean saved = super.doSave(context);
		if (saved) {
			// ͬ����Ŀ�����ɫ
			// [bug:18]
			// ȷ����Ŀ�����ɫ����Ա����Ŀ������һ��
			ensureProjectManagerRole(context);
		}
		return saved;
	}

	/**
	 * ȷ����Ŀ�����ɫ����Ա����Ŀ������һ��
	 * 
	 * @param context
	 * @throws Exception
	 */
	private void ensureProjectManagerRole(IContext context) throws Exception {
		User charger = getCharger();
		if (charger == null) {
			return;
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
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID, new ObjectId());

		// ��������������
		Work root = makeWBSRoot();
		root.doInsert(context);
		setValue(Project.F_WORK_ID, root.get_id());

		// Ԥ��
		ProjectBudget budget = makeBudget(context);
		budget.doInsert(context);

		super.doInsert(context);

		// ����ģ��
		doSetupWithTemplate(root.get_id(), context);

		// ����ϵͳ����
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
	 * @throws Exception
	 */
	public void doSetupWithTemplate(ObjectId wbsRootId, IContext context)
			throws Exception {
		ObjectId id = getProjectTemplateId();
		if (id == null) {
			return;
		}

		// ���ƽ�ɫ����
		Map<ObjectId, DBObject> roleMap = doSetupRolesWithTemplate(id, context);

		// ���ƹ�������
		Map<ObjectId, DBObject> workMap = doSetupWBSWithTemplate(id, wbsRootId,
				roleMap, context);

		// ���ƹ�����ǰ�����ϵ
		doSetupWorkConnectionWithTemplate(id, workMap, context);

		// ������Ŀ�����̺ͽ�ɫ
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

		BasicDBObject update = new BasicDBObject();

		// ���ñ��������
		Object value = pjTempData.get(ProjectTemplate.F_WF_CHANGE);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_CHANGE, value);
		}

		// ���ñ���������Ƿ񼤻�
		value = pjTempData.get(ProjectTemplate.F_WF_CHANGE_ACTIVATED);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_CHANGE_ACTIVATED, value);
		}

		// ���ñ�����̵Ļִ����
		setRoleDBObjectField(update, pjTempData,
				ProjectTemplate.F_WF_CHANGE_ASSIGNMENT, roleMap);

		// ����ִ�й�����
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_COMMIT, value);
		}

		// ����ִ�й������Ƿ񼤻�
		value = pjTempData.get(ProjectTemplate.F_WF_COMMIT_ACTIVATED);
		if (value != null) {
			update.put(ProjectTemplate.F_WF_COMMIT_ACTIVATED, value);
		}

		// ����ִ�й������Ļִ���˽�ɫ
		setRoleDBObjectField(update, pjTempData,
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

	private Map<ObjectId, DBObject> doSetupRolesWithTemplate(
			ObjectId projectTemplateId, IContext context) throws Exception {
		DBCollection col_roled = getCollection(IModelConstants.C_ROLE_DEFINITION);
		DBCollection col_role = getCollection(IModelConstants.C_PROJECT_ROLE);
		// ɾ����Ŀ���еĽ�ɫ
		col_role.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
				get_id()));

		// ׼������ֵ
		HashMap<ObjectId, DBObject> result = new HashMap<ObjectId, DBObject>();

		// ����ģ��Ľ�ɫ����
		DBCursor cur = col_roled.find(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplateId));
		while (cur.hasNext()) {
			DBObject roleddata = cur.next();

			// ������Ŀ��ɫ����
			ProjectRole prole = makeProjectRole(null);

			// ������Ҫ��������Ŀ��ɫ��_id
			ObjectId proleId = new ObjectId();
			prole.setValue(F__ID, proleId);
			// �������֯��ɫ
			Object roleId = roleddata
					.get(RoleDefinition.F_ORGANIZATION_ROLE_ID);
			if (roleId != null) {
				// ����Ϊ��֯��ɫ
				prole.setValue(ProjectRole.F_ORGANIZATION_ROLE_ID, roleId);
				// ����֯��ɫ�еĳ�Ա���뵽��Ŀ�Ĳ�����
				Role role = ModelService.createModelObject(Role.class,
						(ObjectId) roleId);
				List<PrimaryObject> ass = role.getAssignment();
				doAddParticipateFromAssignment(ass);

			} else {
				// ����Ϊ��Ŀ��ɫ
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

			// ���뵽���ݿ�
			WriteResult ws = col_role.insert(insertData, WriteConcern.NORMAL);
			checkWriteResult(ws);
		}

		return result;
	}

	private HashMap<ObjectId, DBObject> doSetupWBSWithTemplate(
			ObjectId projectTemplateId, ObjectId wbsRootId,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {

		// ȡ��ģ��ĸ����������_id
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

		// ���湤��
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// ������еķǸ���������
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

		// �����ĵ�
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		ws = docCol.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		collection = documentsToBeInsert.values();
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

		return worksToBeInsert;
	}

	/**
	 * ���ݽ�ɫ���й���ָ��
	 * 
	 * @throws Exception
	 */
	public void doAssignmentByRole(IContext context) throws Exception {
		Map<ObjectId, List<PrimaryObject>> map = getRoleAssignmentMap();
		if (map.size() == 0) {
			throw new Exception("��δ�Խ�ɫָ����Ա���޷�ִ�а���ɫָ�ɹ���");
		}
		List<PrimaryObject> childrenWorks = getChildrenWork();
		for (int i = 0; i < childrenWorks.size(); i++) {
			Work childWork = (Work) childrenWorks.get(i);
			childWork.doAssignment(map, context);
		}
	}

	/**
	 * Ϊ��Ŀ��Ӳ�����, ��Ŀ�Ĳ����߿�����Ϊ��Ŀ��ɫָ����Աʱ���,Ҳ�����������<br/>
	 * ������ӵĲ����߿���Ӧ�õ���Ŀ�Ĺ�����<br/>
	 * ����Ĳ������û�id{@link User#F_USER_ID}���б�<br/>
	 * 
	 * 
	 * {@link F_PARTICIPATE} �ֶ����������͵��ֶ�<br/>
	 * ʹ����$addToSet��eachһ���Խ����ֵ���µ����ֶΣ����ұ�֤û���ظ�<br/>
	 * 
	 * @param userIds
	 *            ���������� userid {@link User#getUserid()}, {@link User#F_USER_ID}
	 * @throws Exception
	 *             �׳�д�����ʱ
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
		// ɾ��work
		DBCollection col = getCollection(IModelConstants.C_WORK);
		WriteResult ws = col.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		// ɾ��workconnection
		col = getCollection(IModelConstants.C_WORK_CONNECTION);
		ws = col.remove(new BasicDBObject().append(Work.F_PROJECT_ID, get_id()));
		checkWriteResult(ws);

		// ɾ��Ԥ��
		col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		ws = col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// ɾ��role
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

		// ɾ��roleassignment
		col = getCollection(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		ws = col.remove(new BasicDBObject().append(
				ProjectRoleAssignment.F_ROLE_ID,
				new BasicDBObject().append("$in", roleIds)));
		checkWriteResult(ws);

		// ɾ��������
		col = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = col.remove(new BasicDBObject().append(Deliverable.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// ɾ���ĵ�
		col = getCollection(IModelConstants.C_DOCUMENT);
		ws = col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		super.doRemove(context);
	}

	/**
	 * 
	 * @param srcParent
	 *            ģ�幤����Id
	 * @param tgtParentId
	 *            ��ĿĿ�깤����Id
	 * @param tgtRootId
	 *            ��ĿĿ�깤���ĸ�Id
	 * @param projectId
	 *            ��ĿId
	 * @param roleMap
	 *            ��ɫӳ�䣬{ģ���ɫId:Ŀ���ɫ}
	 * @param worksToInsert
	 *            ��Ҫ�������ݿ�Ĺ�������ӵĹ�������Ҫ���õ�ArrayList��
	 * @param documentsToInsert
	 *            ��Ҫ�������ݿ���ĵ�, {�ĵ�ģ��id:Ŀ���ĵ�}
	 * @param dilerverableToInsert
	 *            ��Ҫ�������ݿ�Ľ������ϵ
	 * @param fileToCopy
	 *            ��Ҫ���Ƶ��ļ�
	 * @param context
	 *            ����������
	 */
	private void copyWBSTemplate(ObjectId srcParent, ObjectId tgtParentId,
			ObjectId tgtRootId, ObjectId projectId,
			Map<ObjectId, DBObject> roleMap,
			Map<ObjectId, DBObject> worksToInsert,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context) {
		// ���src����
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(
				new BasicDBObject().append(WorkDefinition.F_PARENT_ID,
						srcParent)).sort(new SEQSorter().getBSON());
		int seq = 0;
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// ���workdef�Ƿ����ѡ���������
			String optionValue = checkOptionValueFromTemplate(workdef);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// ������ų��ģ�����Ҫ����
				continue;

			} else {
				// ************************************************************************
				// ��������
				DBObject work = new BasicDBObject();

				work.put(Work.F_PROJECT_ID, projectId);

				work.put(Work.F_ROOT_ID, tgtRootId);

				work.put(Work.F_PARENT_ID, tgtParentId);

				work.put(Work.F__ID, new ObjectId());
				/**
				 * BUG:10006
				 */
				work.put(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);

				if (WorkDefinition.VALUE_MANDATORY.equals(optionValue)) {
					work.put(Work.F_MANDATORY, Boolean.TRUE);
				}

				// ���ù����������ֶ�
				Object value = workdef.get(WorkDefinition.F_DESC);
				if (value != null) {
					work.put(Work.F_DESC, value);
				}
				value = workdef.get(WorkDefinition.F_DESC_EN);
				if (value != null) {
					work.put(Work.F_DESC_EN, value);
				}

				// ���ñ��������
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE, value);
				}

				// ���ñ���������Ƿ񼤻�
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE_ACTIVATED, value);
				}

				// ���ñ�����̵Ļִ����
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_CHANGE_ASSIGNMENT, roleMap);

				// ����ִ�й�����
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE, value);
				}

				// ����ִ�й������Ƿ񼤻�
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED, value);
				}

				// ����ִ�й������Ļִ���˽�ɫ
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_EXECUTE_ASSIGNMENT, roleMap);

				// ���ø����˽�ɫ
				setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID,
						roleMap);

				// ����ָ���˽�ɫ
				setRoleField(work, workdef,
						IWorkCloneFields.F_ASSIGNMENT_CHARGER_ROLE_ID, roleMap);

				// ���ò����߽�ɫ
				setRoleListField(work, workdef,
						IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

				// �������
				work.put(IWorkCloneFields.F_SEQ, new Integer(seq++));

				// ���ñ�׼��ʱ
				value = workdef.get(IWorkCloneFields.F_STANDARD_WORKS);
				if (value != null) {
					work.put(IWorkCloneFields.F_STANDARD_WORKS, value);
				}

				// ����������
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

				// ��ɹ������Ը���
				// ************************************************************

				// ************************************************************
				// ��ӹ���������
				// ���ģ�嶨��Ľ����ﶨ��
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
					// ��齻�����Ƿ����ѡ���������
					String documentOptionValue = checkOptionValueFromTemplate(delidata);
					if (DeliverableDefinition.VALUE_EXCLUDE
							.equals(documentOptionValue)) {
						// ������ų��ģ�����Ҫ����
						continue;
					} else {
						// ����ģ��Ľ����ﶨ�崴���������ϵ
						DBObject deliverableData = new BasicDBObject();

						// ������ĿId
						deliverableData
								.put(Deliverable.F_PROJECT_ID, projectId);

						// ���ù���Id
						deliverableData.put(Deliverable.F_WORK_ID,
								work.get(Work.F__ID));

						// �����Ƿ����
						if (DeliverableDefinition.VALUE_MANDATORY
								.equals(documentOptionValue)) {
							deliverableData.put(Deliverable.F_MANDATORY,
									Boolean.TRUE);
						}

						// ����ĵ�ģ��
						ObjectId documentTemplateId = (ObjectId) delidata
								.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
						// �����ĵ�ģ���Ƿ��Ѿ��������ĵ�
						DBObject documentData = documentsToInsert
								.get(documentTemplateId);
						if (documentData == null) {
							// ���û�д�������Ҫ�������ĵ�
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

				// �����ӹ���
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
			// ����׼��
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

			// ����Ʒѡ�
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

			// �����Ŀѡ�
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
			// ����Ƿ�Ϸ�
			if (start.after(finish)) {
				throw new Exception("��ʼ���ڱ��������������");
			}
			// ���㹤��
			int workingdays = cc.getWorkingDays(start, finish);
			setValue(fDuration, new Integer(workingdays));
		}
	}

	/**
	 * ��ȡ��Ŀ������<br/>
	 * 
	 * @return ��ÿ��Ԫ��Ϊ�û���userid��ɵ�List,�п���Ϊ��
	 */
	public List<?> getParticipatesIdList() {
		return (List<?>) getValue(F_PARTICIPATE);
	}

	/**
	 * ��������WBS���,���ų�ĳЩ������������WBS�Ļ��ң�ʹ�ñ���������WBS���
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doArrangeWBSCode(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doArrangeWBSCode();
	}

	/**
	 * �����Ŀ�ƻ� <br/>
	 * 1. ��鹤����ţ� ���棬û�� <br/>
	 * 2. Ԥ���� �����棬���û��ֵ <br/>
	 * 3. ����ɫ��ָ�ɣ� ����û��ָ����Ա�Ľ�ɫ <br/>
	 * 4. �����Ŀ������ ������û��ָ�����̸����� <br/>
	 * 5. �����������м��<br/>
	 * 
	 * -- ���¼�����Ŀ�ļƻ���ʼ��ʼʱ�䡢�ƻ����ʱ�䡢���� ���¼�����Ŀ���ܹ�ʱ
	 * 
	 * @return List<ICheckListItem> ������嵥
	 */
	public List<ICheckListItem> checkPlan() {
		List<ICheckListItem> result = new ArrayList<ICheckListItem>();
		// 1. ��鹤����ţ� ���棬û��
		Object value = getValue(F_WORK_ORDER);
		if (!(value instanceof BasicBSONList)
				|| ((BasicBSONList) value).isEmpty()) {
			CheckListItem checkItem = new CheckListItem("��鹤�����", "������ſ�",
					"�������Ŀ���ύ��ȷ��������ţ�����Ա���ʾ��", ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setKey(F_WORK_ORDER);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem("��鹤�����");
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 2. Ԥ���� �����棬���û��ֵ
		ProjectBudget budget = getBudget();
		value = budget.getBudgetValue();
		if (value == null || ((Double) value).doubleValue() == 0d) {
			CheckListItem checkItem = new CheckListItem("���Ԥ��",
					"û���ƶ���ĿԤ�㣬��Ԥ��Ϊ0", "�������Ŀ���ύ��ȷ��Ԥ�㣬����Ա���ʾ",
					ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setKey(F_WORK_ORDER);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setEditorPageId(EDITOR_PAGE_BUDGET);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem("���Ԥ��");
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 3. ����ɫ��ָ�ɣ� ����û��ָ����Ա�Ľ�ɫ
		List<PrimaryObject> rd = getProjectRole();
		Map<ObjectId, List<PrimaryObject>> raMap = getRoleAssignmentMap();
		List<PrimaryObject> ra;
		boolean passed = true;
		for (int i = 0; i < rd.size(); i++) {
			ProjectRole role = (ProjectRole) rd.get(i);
			ObjectId roldId = role.get_id();
			ra = raMap.get(roldId);
			if (ra == null) {
				CheckListItem checkItem = new CheckListItem("�����Ŀ��ɫָ��",
						"û��ȷ����ɫ��Ӧ��Ա��" + "��ɫ��[" + role.getLabel() + "]",
						"�������Ŀ���ύ��ȷ����Ա������Ա���ʾ", ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setEditorId(EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(EDITOR_PAGE_TEAM);
				checkItem.setSelection(role);
				result.add(checkItem);
				passed = false;
			}
		}
		if (passed) {
			CheckListItem checkItem = new CheckListItem("�����Ŀ��ɫָ��");
			checkItem.setData(this);
			result.add(checkItem);
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 4.1 �����Ŀ��������� ������û��ָ�����̸�����
		String title = "�����Ŀ�������";
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

		// 4.2 �����Ŀ�ύ������ ������û��ָ�����̸�����
		title = "�����Ŀ�ύ����";
		process = F_WF_COMMIT;
		pageId = EDITOR_PAGE_COMMIT_PROCESS;
		passed = ProjectToolkit.checkProcessInternal(this, pc, result, raMap,
				title, process, editorId, pageId);
		if (passed) {
			CheckListItem checkItem = new CheckListItem(title);
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 5 ��鹤��
		Work work = getWBSRoot();
		List<ICheckListItem> workResult = work.checkPlan();
		result.addAll(workResult);

		return result;
	}

	/**
	 * ����������Ƿ񼤻�
	 * 
	 * @return
	 */
	public boolean isChangeWorkflowActivate() {
		return Boolean.TRUE.equals(getValue(F_WF_CHANGE_ACTIVATED));
	}

	/**
	 * �ύ�������Ƿ񼤻�
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
		// δ��ɺ�δȡ����
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
		//�����Ŀ�Ѿ���ɡ�ȡ�������ܱ༭
		String lc = getLifecycleStatus();
		if(STATUS_CANCELED_VALUE.equals(lc)||STATUS_FINIHED_VALUE.equals(lc)){
			return false;
		}
		
		//�������Ŀ�����ˣ����Ա༭
		String chargerId = getChargerId();
		String userId = context.getAccountInfo().getConsignerId();
		if( !userId.equals(chargerId)){
			return false;
		}
		
		return true;
//		return super.canEdit(context);
	}
	
	@Override
	public boolean canDelete(IContext context) {
		String lc = getLifecycleStatus();
		if(!STATUS_NONE_VALUE.equals(lc)&&!STATUS_ONREADY_VALUE.equals(lc)){
			return false;
		}
		
		String chargerId = getChargerId();
		String userId = context.getAccountInfo().getConsignerId();
		if( !userId.equals(chargerId)){
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public boolean canRead(IContext context) {
		//�������Ŀ�����ߣ����Դ򿪲鿴
		List<?> participates = getParticipatesIdList();
		String userId = context.getAccountInfo().getConsignerId();
		
		return participates!=null&&participates.contains(userId);
//		return super.canRead(context);
	}

	@Override
	public String getLifecycleStatusText() {
		String lc = getLifecycleStatus();
		return LifecycleToolkit.getLifecycleStatusText(lc);
	}

	public void doCommitWithSendMessage(IContext context) throws Exception {
		Map<String, Message> msgList = getCommitMessage();
		Iterator<Message> iter = msgList.values().iterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			message.doSave(context);
		}
	}

	public void doCommitWithWork(Work work) {

	}

	/**
	 * ������Ϣ�����ύ��<br/>
	 * ��Ҫ���͵���Ŀ�ĸ����ˣ�����Ϣ��Ҫ��������Ŀ����<br/>
	 * 
	 * @param context
	 * @throws Exception
	 */
	private Map<String, Message> getCommitMessage() throws Exception {
		Map<String, Message> messageList = new HashMap<String, Message>();
		// 1. ��ȡ��Ŀ������
		appendMessageForCharger(messageList);
		// 2. ��ȡ��Ŀ�Ĳ�����

		appendMessageForParticipate(messageList);

		// 3. ��Ŀ����֪ͨ
		appendMessageForChangeWorkflowActor(messageList);

		// 4. ��������
		Work root = getWBSRoot();
		messageList = root.getCommitMessage(messageList);

		MessageToolkit.appendProjectCommitMessageEndContent(messageList);
		return messageList;
	}

	/**
	 * ����Ϣ�嵥�������Ŀ�Ĳ�����������Ϣ
	 * 
	 * @param messageList
	 */
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
				MessageToolkit.appendMessageContent(message, "���빤��" + ": "
						+ getLabel());
				message.appendTargets(this, EDITOR_CREATE_PLAN, Boolean.TRUE);
			}
		}
	}

	/**
	 * ����Ϣ�嵥����ӹ������ִ���ߵ���ʾ��Ϣ
	 * 
	 * @param messageList
	 */
	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_CHANGE, "��Ŀ�������");
	}

	/**
	 * ����Ϣ�嵥�������Ŀ�����˵�������Ϣ
	 * 
	 * @param messageList
	 */
	public void appendMessageForCharger(Map<String, Message> messageList) {
		Message message;
		String userId = getChargerId();
		if (userId != null) {
			message = messageList.get(userId);
			if (message == null) {
				message = MessageToolkit.createProjectCommitMessage(userId);
				messageList.put(userId, message);
			}
			MessageToolkit.appendMessageContent(message, "������Ŀ�����ˣ���Ŀ" + ": "
					+ getLabel());
			message.appendTargets(this, EDITOR_CREATE_PLAN, Boolean.TRUE);
			messageList.put(userId, message);
		}
	}

	/**
	 * ʹ�ù����������ύ������һ�������������������󶨵��ù���
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Work makeWorkflowCommitableWork(Work work, IContext context)
			throws Exception {
		if (work == null) {
			work = ModelService.createModelObject(Work.class);
			work.setValue(Work.F_CHARGER, context.getAccountInfo().getUserId());// ���ø�����Ϊ��ǰ�û�
			work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// ���øù�����״̬Ϊ׼���У��Ա��Զ���ʼ
			Date today = new Date();
			work.setValue(Work.F_PLAN_START, today);

			Date finishDate = today;
			Object sDuration = Setting
					.getSystemSetting(IModelConstants.S_DEFAULT_PROJECT_COMMIT_DURATION);
			if(sDuration !=null){
				Integer duration = Utils.getIntegerValue(sDuration);
				if(duration!=null){
					CalendarCaculater ds = getCalendarCaculater();
					while(duration >0){
						finishDate = Utils.getDateAfter(finishDate, 1);
						if(ds.getWorkingTime(finishDate)>0){
							duration --;
						}
					}
				}
			}
			work.setValue(Work.F_PLAN_FINISH, finishDate);

			work.setValue(Work.F_DESC, "��Ŀ�ƻ��ύ" + " " + this);
			work.setValue(Work.F_DESCRIPTION, getDesc());
			work.setValue(Work.F_PLAN_WORKS, new Double(0d));
			BasicBSONList targets = new BasicBSONList();
			targets.add(new BasicDBObject().append(SF_TARGET, get_id())
					.append(SF_TARGET_CLASS, Project.class.getName())
					.append(SF_TARGET_EDITING_TYPE, EDITING_BY_EDITOR)
					.append(SF_TARGET_EDITOR, EDITOR_CREATE_PLAN)
					.append(SF_TARGET_EDITABLE, Boolean.TRUE));
			work.setValue(Work.F_TARGETS, targets);

		}
		work.setValue(Work.F_PROJECT_ID, get_id());
		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		DBObject wfdef = pc.getWorkflowDefinition(F_WF_COMMIT);
		work.bindingWorkflowDefinition(Work.F_WF_EXECUTE, wfdef);
		return work;
	}


	public Object doCancel(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doCancel(context);

		doChangeLifecycleStatus(context, STATUS_CANCELED_VALUE);
		return this;
	}

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
		return this;

	}

	public Object doPause(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doPause(context);

		doChangeLifecycleStatus(context, STATUS_PAUSED_VALUE);
		return this;
	}

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

		return this;
	}

	/**
	 * ����Ŀ��״̬����Ϊ׼����
	 * 
	 * @param context
	 */
	public void doReady(IContext context) throws Exception {
		doChangeLifecycleStatus(context, STATUS_ONREADY_VALUE);
	}

	/**
	 * ������������״̬
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
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
		}
		// 2.�����Ŀ�Ĺ����Ƿ�������������
		Work work = getWBSRoot();
		message.addAll(work.checkCascadeStart());
		return message;
	}

	@Override
	public List<Object[]> checkCancelAction(IContext context) throws Exception {
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
		}
		return null;
	}

	@Override
	public List<Object[]> checkFinishAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
		}
		// 2.�����Ŀ�Ĺ����Ƿ�������������
		Work work = getWBSRoot();
		message.addAll(work.checkCascadeFinish(work.get_id()));
		return message;
	}

	@Override
	public List<Object[]> checkPauseAction(IContext context) throws Exception {
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
		}
		return null;
	}

	/**
	 * �Ƿ������ύ��Ŀ
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void checkCommitAction(IContext context) throws Exception {
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception("���Ǳ���Ŀ�����ˣ�" + this);
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
		}
		return super.getAdapter(adapter);
	}
}
