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
import org.eclipse.core.runtime.Assert;
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
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.commonlabel.ProjectCommonHTMLLable;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.business.model.dataset.calendarsetting.SystemCalendar;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectMonthlyETL;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.input.ProjectEditorInputFactory;
import com.sg.business.model.roleparameter.ProjectRoleParameter;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.UIConstants;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.commons.model.IEditorInputFactory;

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
	 * ��Ŀ���
	 */
	public static final String F_PROJECT_NUMBER = "projectnumber"; //$NON-NLS-1$

	/**
	 * ��Ŀ�������ֶΣ�������Ŀ�����˵�userid {@link User} ,
	 */
	public static final String F_CHARGER = "chargerid"; //$NON-NLS-1$

	/**
	 * ��Ŀ���������ֶΣ�������Ŀ�������˵�userid {@link User} ,
	 */
	public static final String F_BUSINESS_CHARGER = "businesschargerid"; //$NON-NLS-1$

	/**
	 * ���������ֶΣ�ÿ��Ԫ��Ϊ����userid
	 */
	public static final String F_PARTICIPATE = "participate"; //$NON-NLS-1$

	/**
	 * ��Ŀ��������Ŀְ����֯
	 */
	public static final String F_FUNCTION_ORGANIZATION = "org_id"; //$NON-NLS-1$

	/**
	 * ��Ŀ������
	 */
	public static final String F_LAUNCH_ORGANIZATION = "launchorg_id"; //$NON-NLS-1$

	/**
	 * ������ID
	 */
	public static final String F_WORK_ID = "work_id"; //$NON-NLS-1$

	public static final String F_DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * Ԥ��ID
	 */
	public static final String F_BUDGET_ID = "budget_id"; //$NON-NLS-1$

	/**
	 * ���ñ�׼��
	 */
	public static final String F_STANDARD_SET_OPTION = "standardset"; //$NON-NLS-1$

	/**
	 * ��Ʒ����ѡ�
	 */
	public static final String F_PRODUCT_TYPE_OPTION = "producttype"; //$NON-NLS-1$

	/**
	 * ��Ŀ����ѡ�
	 */
	public static final String F_PROJECT_TYPE_OPTION = "projecttype"; //$NON-NLS-1$

	/**
	 * �б����͵��ֶΣ��������
	 */
	public static final String F_WORK_ORDER = "workorder"; //$NON-NLS-1$

	/**
	 * ��Ŀ�ύ����
	 */
	public static final String F_WF_COMMIT = "wf_commit"; //$NON-NLS-1$

	/**
	 * �ύ�����еĽ�ɫָ��
	 */
	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment"; //$NON-NLS-1$

	/**
	 * �ύ�����Ƿ�����
	 */
	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated"; //$NON-NLS-1$

	/**
	 * �ύ���������ִ����
	 */
	public static final String F_WF_COMMIT_ACTORS = "wf_commit_actors"; //$NON-NLS-1$
	/**
	 * ��Ŀ�������
	 */
	public static final String F_WF_CHANGE = "wf_change"; //$NON-NLS-1$

	/**
	 * ��Ŀ��������Ƿ�����
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated"; //$NON-NLS-1$

	/**
	 * ��Ŀ������̽�ɫָ��
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment"; //$NON-NLS-1$

	/**
	 * ��Ŀ�����������ִ����
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors"; //$NON-NLS-1$

	/**
	 * ��Ŀ�༭��
	 */
	public static final String EDITOR_CREATE_PLAN = "project.editor"; //$NON-NLS-1$

	/**
	 * ��Ŀ�༭��
	 */
	public static final String EDITOR_PROJECT_FINANCIAL = "editor.project.financial"; //$NON-NLS-1$

	/**
	 * ��Ŀ��������
	 */
	public static final String EDITOR_SETPROCESS = "project.flow.setting"; //$NON-NLS-1$

	/**
	 * ��ĿԤ��
	 */
	public static final String EDITOR_PAGE_BUDGET = "project.financial"; //$NON-NLS-1$

	public static final String EDITOR_PAGE_TEAM = "project.team"; //$NON-NLS-1$

	/**
	 * ��Ŀ�����ֽ�ṹ
	 */

	public static final String EDITOR_PAGE_WBS = "project.wbs"; //$NON-NLS-1$

	public static final String EDITOR_PAGE_CHANGE_PROCESS = "processpage2"; //$NON-NLS-1$

	public static final String EDITOR_PAGE_COMMIT_PROCESS = "processpage1"; //$NON-NLS-1$

	public static final String F_FOLDER_ID = "folder_id"; //$NON-NLS-1$

	public static final String F_BUSINESS_ORGANIZATION = "businessorganization_id";

	/**
	 * ��ʱ������������ǹ�ʱ������_id
	 */
	public static final String F_WORKTIMEPROGRAM_ID = "worktimeprogram_id";

	/**
	 * ��ʱ����
	 */
	public static final String F_WORKTIME_PARA_X = "worktimepara_x";

	/**
	 * ����ѡ�����paraY �Ӽ�¼���ֶΣ�DBObject����
	 */
	public static final String F_WORKTIME_PARA_OPTIONS = "options";

	/**
	 * ��ʱ���ڼ�¼��Ŀ�ı�ǩ������Ϊ�ֶα���
	 */
	public static final String _TAG = "d_tag";

	/**
	 * ��ʱ������
	 */
	public static final String F_WORKTIME_PARA_Y = "worktimepara_y";

	private SummaryProjectWorks summaryProjectWorks;

	public static final int AUTO_ASSIGNMENT_TYPE_ALL = 0;

	public static final int AUTO_ASSIGNMENT_TYPE_NONE = 1;

	public static final String F_STATISTICS_STEP = "statisticsstep";

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().Project_0;
	}

	/**
	 * ������ʾͼ��
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

	public String getBusinessChargerId() {
		return (String) getValue(F_BUSINESS_CHARGER);
	}

	public User getBusinessCharger() {
		String chargerId = getBusinessChargerId();
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return UserToolkit.getUserById(chargerId);
	}

	/**
	 * ������Ŀ�ķ�����֯
	 * 
	 * @return Organization
	 */
	public List<PrimaryObject> getLaunchOrganization() {
		List<?> orgIds = getLaunchOrganizationId();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (orgIds != null) {
			for (int i = 0; i < orgIds.size(); i++) {
				ObjectId _id = (ObjectId) orgIds.get(i);
				if (_id != null) {
					result.add(ModelService.createModelObject(
							Organization.class, _id));
				}
			}
		}
		return result;
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

	public Organization getBusinessOrganization() {
		ObjectId orgId = getBusinessOrganizationId();
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
	public List<?> getLaunchOrganizationId() {
		return (List<?>) getValue(F_LAUNCH_ORGANIZATION);
	}

	/**
	 * ������Ŀ������֯_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	public ObjectId getBusinessOrganizationId() {
		return (ObjectId) getValue(F_BUSINESS_ORGANIZATION);
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
		ModelRelation mr = ModelService.getModelRelation("project_calendar"); //$NON-NLS-1$
		return getRelationByModel(mr);
	}

	/**
	 * �����Ŀ����������
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

	public Folder makeFolderRoot() {
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
		// ͬ��������ŵ���˾
		ensureWorkOrderRelativeToCompany(context);

		boolean saved = super.doSave(context);
		if (saved) {
			// ͬ����Ŀ�����ɫ
			// [bug:18]
			// ȷ����Ŀ�����ɫ����Ա����Ŀ������һ��
			ensureProjectManagerRole(context);

			// ͬ�����ĸ��������������
			syncRootWorkNameInternal();

			// ���¼���ʵ�ʹ�ʱ
			syncWorkActualWorksInternal(this.get_id(), context);

		}
		return saved;
	}

	private void syncWorkActualWorksInternal(final ObjectId _id,
			final IContext context) {
		Job job = new Job("���¼���ʵ�ʹ�ʱ") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				//��ȡ��������
				DBCollection col = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_WORK);
				//������һ�������Ĺ�ʱ������ʽ�Ǳ�׼��ʱ�ƵĲ�ѯ����
				DBCursor cursor = col.find(new BasicDBObject().append(
						Work.F_PARENT_ID, _id).append(Work.F_MEASUREMENT,
						Work.MEASUREMENT_TYPE_STANDARD_ID));
				while (cursor.hasNext()) {
					DBObject dbo = cursor.next();
					//���ݲ�ѯ���Ľ�����칤��ģ��
					Work work = ModelService.createModelObject(dbo, Work.class);
					double actualWorks;
					try {
						//���㹤����ʵ�ʹ�ʱ
						actualWorks = work.calculateActualWorks();
						//���������ʵ�ʹ�ʱ���õ�������ʵ�ʹ�ʱ�ֶ�
						col.update(new BasicDBObject().append(F__ID,
								work.get_id()), new BasicDBObject().append(
								"$set", new BasicDBObject().append(
										F_ACTUAL_WORKS, actualWorks)), true,
								false);
						//�������ʵ�ʹ�ʱ��̯
						work.doCalculateWorkPerformence(context);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void syncRootWorkNameInternal() {
		Job job = new Job("�����������������ͬ��") { //$NON-NLS-1$

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Object rootId = getValue(F_WORK_ID);
				DBCollection col = getCollection(IModelConstants.C_WORK);
				col.update(new BasicDBObject().append(Work.F__ID, rootId),
						new BasicDBObject().append("$set", new BasicDBObject() //$NON-NLS-1$
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
				throw new Exception(Messages.get().Project_1);
			}
			company.doSaveWorkOrders((List<?>) wonList);
		}
	}

	/**
	 * ȷ����Ŀ�����ɫ����Ա����Ŀ������һ��
	 * 
	 * @param context
	 * @throws Exception
	 */
	private void ensureProjectManagerRole(final IContext context) {
		Job job = new Job(Messages.get().Project_2) {

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

		// ���ɱ���
		generateCode();

		// ����������
		Work root = makeWBSRoot();
		root.doInsert(context);
		setValue(Project.F_WORK_ID, root.get_id());

		// �������ļ���
		Folder folderRoot = makeFolderRoot();
		folderRoot.doInsert(context);
		setValue(Project.F_FOLDER_ID, folderRoot.get_id());

		// Ԥ��
		ProjectBudget budget = makeBudget(context);
		budget.doInsert(context);

		// 6.30 ����������Ŀģ��Ĺ�ʱͳ�ƽ׶�
		ProjectTemplate projectTemplate = getProjectTemplate();
		if(projectTemplate!=null){
			BasicBSONList steps = (BasicBSONList) projectTemplate
					.getValue(ProjectTemplate.F_STATISTICSSTEP);
			if (steps != null) {
				setValue(F_STATISTICS_STEP, steps);
			}
		}

		super.doInsert(context);

		doInsertAfter(root, folderRoot, context);

	}

	private void doInsertAfter(final Work root, final Folder folderRoot,
			final IContext context) throws Exception {

		// ����ģ��
		// try {
		doSetupWithTemplate(root.get_id(), context, folderRoot.get_id());
		// } catch (Exception e) {
		// // return new Status(Status.ERROR, ModelActivator.PLUGIN_ID,
		// // Status.ERROR, "����ģ�����", e);
		//
		// throw new Exception(Messages.get().Project_3);
		// }
		Job job = new Job("��ģ�帴����Ŀ��Ϣ") { //$NON-NLS-1$

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// ����ϵͳ����
				try {
					doCopySystemCanlendar();
				} catch (Exception e) {
					return new Status(Status.ERROR, ModelActivator.PLUGIN_ID,
							Status.ERROR, Messages.get().Project_4, e);
				}

				// //�Զ���������ִ����
				// doAssignmentByRole(context);
				return Status.OK_STATUS;
			}

		};
		job.schedule();

	}

	private void generateCode() throws Exception {
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Project_5);
		}

		String code = org.getCode();
		if (code == null) {
			throw new Exception(Messages.get().Project_6);
		}

		DBCollection ids = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C__IDS);

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		String prefix = code + ("" + year).substring(2); //$NON-NLS-1$
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_PROJECT_NUMBER
				+ "." + prefix); //$NON-NLS-1$
		String seq = String.format("%03x", id).toUpperCase(); //$NON-NLS-1$

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

		// ���ƽ�ɫ����
		Map<ObjectId, DBObject> roleMap = doMakeRolesWithTemplate(id, context);

		// ʹ����Ŀģ�崴��wbs
		Map<ObjectId, DBObject> workMap = doSetupWBSWithTemplate(id, wbsRootId,
				folderRootId, roleMap, context);
		// ���ƹ�������

		// ���ƹ�����ǰ�����ϵ
		doSetupWorkConnectionWithTemplate(id, workMap, context);

		// ������Ŀ�����̺ͽ�ɫ
		doSetupWorkflowWithTemplate(id, roleMap, context);

		// �ų�û��Ӧ�õĽ�ɫ
		if (!roleMap.isEmpty()) {
			Collection<DBObject> values = roleMap.values();
			Iterator<DBObject> iter = values.iterator();

			Set<DBObject> toBeInsert = new HashSet<DBObject>();
			while (iter.hasNext()) {
				DBObject prole = iter.next();
				if (!Boolean.TRUE.equals(prole.get("used"))) { //$NON-NLS-1$
					continue;
				}
				Object roleId = prole.get(ProjectRole.F_ORGANIZATION_ROLE_ID);
				if (roleId != null) {
					// ����֯��ɫ�еĳ�Ա���뵽��Ŀ�Ĳ�����
					Role role = ModelService.createModelObject(Role.class,
							(ObjectId) roleId);
					IRoleParameter roleParameter = getAdapter(IRoleParameter.class);

					List<PrimaryObject> ass = role.getAssignment(roleParameter);
					doAddParticipateFromAssignment(ass);
				}

				prole.removeField("used"); //$NON-NLS-1$
				toBeInsert.add(prole);
			}

			DBObject[] insertData = toBeInsert.toArray(new DBObject[0]);
			// ���뵽���ݿ�
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
		ProjectToolkit.setRoleDBObjectField(update, pjTempData,
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
		ProjectToolkit.setRoleDBObjectField(update, pjTempData,
				ProjectTemplate.F_WF_COMMIT_ASSIGNMENT, roleMap);

		col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", update)); //$NON-NLS-1$

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

	public Map<ObjectId, DBObject> doMakeRolesWithTemplate(
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

				// ���ƽ�ɫ����ű�
				prole.setValue(Role.F_RULE, roleddata.get(Role.F_RULE));

				// // ����֯��ɫ�еĳ�Ա���뵽��Ŀ�Ĳ�����
				// Role role = ModelService.createModelObject(Role.class,
				// (ObjectId) roleId);
				// List<PrimaryObject> ass = role.getAssignment();
				// doAddParticipateFromAssignment(ass);

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

		return result;
	}

	private HashMap<ObjectId, DBObject> doSetupWBSWithTemplate(
			ObjectId projectTemplateId, ObjectId wbsRootId,
			ObjectId folderRootId, Map<ObjectId, DBObject> roleMap,
			IContext context) throws Exception {

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

		ProjectToolkit.copyWBSTemplate(rootWorkDefId, wbsRootId, wbsRootId,
				this, roleMap, worksToBeInsert, folderRootId,
				documentsToBeInsert, deliverableToInsert, fileToCopy, context);

		// ���湤��
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// ������еķǸ���������
		WriteResult ws = workCol.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()).append(Work.F__ID,
				new BasicDBObject().append("$ne", wbsRootId))); //$NON-NLS-1$
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
			// �����ĵ�ʱ��Ҫ�ĵ��ı���ǰ��Ԥ����
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
	public void doAssignmentByRole(int type, IContext context) throws Exception {
		Map<ObjectId, List<PrimaryObject>> map = getRoleAssignmentMap();
		if (map.size() == 0) {
			throw new Exception(Messages.get().Project_7);
		}

		// String lc = getLifecycleStatus();
		// if (!STATUS_NONE_VALUE.equals(lc)) {
		// throw new Exception(Messages.get().Project_8);
		// }
		boolean bOverride = true;
		if (type == AUTO_ASSIGNMENT_TYPE_NONE) {
			bOverride = false;
		}

		List<PrimaryObject> childrenWorks = getChildrenWork();
		for (int i = 0; i < childrenWorks.size(); i++) {
			Work childWork = (Work) childrenWorks.get(i);
			childWork.doAssignment(bOverride, map, context);
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
		if (userIds == null) {
			throw new Exception(Messages.get().Project_9);
		}
		DBCollection pjCol = getCollection();

		DBObject update = new BasicDBObject().append("$addToSet", //$NON-NLS-1$
				new BasicDBObject().append(Project.F_PARTICIPATE,
						new BasicDBObject().append("$each", userIds))); //$NON-NLS-1$

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

		// ɾ�����ʱ���
		col = getCollection(IModelConstants.C_PRODUCT);
		ws = col.remove(new BasicDBObject().append(ProductItem.F_PROJECT_ID,
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
				new BasicDBObject().append("$in", roleIds))); //$NON-NLS-1$
		checkWriteResult(ws);

		// ɾ��������
		col = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = col.remove(new BasicDBObject().append(Deliverable.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// ɾ���ļ���
		col = getCollection(IModelConstants.C_FOLDER);
		ws = col.remove(new BasicDBObject().append(Folder.F_PROJECT_ID,
				get_id()));
		checkWriteResult(ws);

		// ɾ���ĵ�
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
			// ����Ƿ�Ϸ�
			if (start.after(finish)) {
				throw new Exception(Messages.get().Project_10);
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
			CheckListItem checkItem = new CheckListItem(
					Messages.get().Project_11, Messages.get().Project_12,
					Messages.get().Project_13, ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setKey(F_WORK_ORDER);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem(
					Messages.get().Project_14);
			checkItem.setData(this);
			result.add(checkItem);
		}

		// 2. Ԥ���� �����棬���û��ֵ
		ProjectBudget budget = getBudget();
		value = budget.getBudgetValue();
		if (value == null || ((Double) value).doubleValue() == 0d) {
			CheckListItem checkItem = new CheckListItem(
					Messages.get().Project_15, Messages.get().Project_16,
					Messages.get().Project_17, ICheckListItem.WARRING);
			checkItem.setData(this);
			checkItem.setKey(F_WORK_ORDER);
			checkItem.setEditorId(EDITOR_CREATE_PLAN);
			checkItem.setEditorPageId(EDITOR_PAGE_BUDGET);
			result.add(checkItem);
		} else {
			CheckListItem checkItem = new CheckListItem(
					Messages.get().Project_18);
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
				CheckListItem checkItem = new CheckListItem(
						Messages.get().Project_19, Messages.get().Project_20
								+ Messages.get().Project_21 + role.getLabel()
								+ "]", //$NON-NLS-3$
						Messages.get().Project_22, ICheckListItem.WARRING);
				checkItem.setData(this);
				checkItem.setEditorId(EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(EDITOR_PAGE_TEAM);
				checkItem.setSelection(role);
				result.add(checkItem);
				passed = false;
			}
		}
		if (passed) {
			CheckListItem checkItem = new CheckListItem(
					Messages.get().Project_23);
			checkItem.setData(this);
			result.add(checkItem);
		}

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);

		// 4.1 �����Ŀ��������� ������û��ָ�����̸�����
		String title = Messages.get().Project_24;
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
		title = Messages.get().Project_25;
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
		// �����ϵͳ����Ա���Ա༭
		String uid = context.getAccountInfo().getUserId();
		User user = UserToolkit.getUserById(uid);
		if (Boolean.TRUE.equals(user.getValue(User.F_IS_ADMIN))) {
			return true;
		}

		// �����Ŀ�Ѿ���ɡ�ȡ�������ܱ༭
		String lc = getLifecycleStatus();
		if (STATUS_CANCELED_VALUE.equals(lc) || STATUS_FINIHED_VALUE.equals(lc)) {
			return false;
		}

		Organization org = getFunctionOrganization();
		String userId = context.getAccountInfo().getConsignerId();
		User consignerUser = UserToolkit.getUserById(userId);
		if (consignerUser.isProjectAdmin(org, getAdapter(IRoleParameter.class))) {
			return true;
		}

		// �������Ŀ�����ˣ����Ա༭
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
		// �������Ŀ����Ա�����Դ򿪲鿴
		Organization org = getFunctionOrganization();
		String userId = context.getAccountInfo().getConsignerId();
		User consignerUser = UserToolkit.getUserById(userId);
		if (consignerUser.isProjectAdmin(org, getAdapter(IRoleParameter.class))) {
			return true;
		}

		// �������Ŀ�����ߣ����Դ򿪲鿴
		List<?> participates = getParticipatesIdList();
		if (participates != null && participates.contains(userId)) {
			return true;
		}

		// �������Ŀ�������ˣ����Դ�
		String businessChargerId = getBusinessChargerId();
		return userId.equals(businessChargerId);
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
	 * ������Ϣ�����ύ��<br/>
	 * ��Ҫ���͵���Ŀ�ĸ����ˣ�����Ϣ��Ҫ��������Ŀ����<br/>
	 * 
	 * @param context
	 * 
	 * @param context
	 * @throws Exception
	 */
	private Map<String, Message> getCommitMessage(IContext context)
			throws Exception {
		Map<String, Message> messageList = new HashMap<String, Message>();
		// 1. ��ȡ��Ŀ������
		String title = Messages.get().Project_26 + " " + this; //$NON-NLS-2$
		appendMessageForCharger(messageList, title, context);
		// 2. ��ȡ��Ŀ�Ĳ�����

		appendMessageForParticipate(messageList, title, context);

		// 3. ��Ŀ����֪ͨ
		appendMessageForChangeWorkflowActor(messageList, title, context);

		// 4. ��������
		Work root = getWBSRoot();
		messageList = root.getCommitMessage(messageList, title, context);

		MessageToolkit.appendProjectCommitMessageEndContent(messageList);
		return messageList;
	}

	/**
	 * ����Ϣ�嵥�������Ŀ�����˵�������Ϣ
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForCharger(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit
				.appendMessage(
						messageList,
						getChargerId(),
						title,
						Messages.get().Project_27 + ": " + getLabel(), this, EDITOR_CREATE_PLAN, context); //$NON-NLS-1$
	}

	/**
	 * ����Ϣ�嵥�������Ŀ�Ĳ�����������Ϣ
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForParticipate(Map<String, Message> messageList,
			String title, IContext context) {
		MessageToolkit
				.appendMessage(
						messageList,
						getChargerId(),
						title,
						Messages.get().Project_28 + ": " + getLabel(), this, EDITOR_CREATE_PLAN, context); //$NON-NLS-1$
	}

	/**
	 * ����Ϣ�嵥����ӹ������ִ���ߵ���ʾ��Ϣ
	 * 
	 * @param messageList
	 * @param context
	 */
	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList, String title, IContext context) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList,
				F_WF_CHANGE, Messages.get().Project_29, title, context
						.getAccountInfo().getConsignerId(), null);
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
			work.setValue(Work.F_CHARGER, context.getAccountInfo()
					.getConsignerId());// ���ø�����Ϊ��ǰ�û�
		}
		work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// ���øù�����״̬Ϊ׼���У��Ա��Զ���ʼ
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

		work.setValue(Work.F_DESC, "��Ŀ�ƻ��ύ" + " " + this); //$NON-NLS-1$ //$NON-NLS-2$
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
	 * ȡ����Ŀ
	 */
	public Object doCancel(IContext context) throws Exception {
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Project_31 + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception(Messages.get().Project_30 + this);
		}

		Work root = getWBSRoot();
		root.doCancel(context);

		doChangeLifecycleStatus(context, STATUS_CANCELED_VALUE);

		// ������Ŀ��Ϣ��������
		doNoticeProjectAction(context, "��ȡ��"); //$NON-NLS-1$

		doArchive(context);

		return this;
	}

	/**
	 * �����Ŀ
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
						"$set", //$NON-NLS-1$
						new BasicDBObject().append(F_LIFECYCLE,
								STATUS_FINIHED_VALUE).append(F_ACTUAL_FINISH,
								new Date())),

				true, false);
		set_data(data);

		// ������Ŀ��Ϣ��������
		doNoticeProjectAction(context, "�����"); //$NON-NLS-1$

		doArchive(context);

		return this;

	}

	public void doArchive(IContext context) throws Exception {
		// 1.�鵵��Ŀ��ɫ
		BasicDBObject q = new BasicDBObject();
		q.put(IProjectRelative.F_PROJECT_ID, get_id());

		// ɾ����ɫָ��
		DBCollection col = getCollection(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		WriteResult ws = col.remove(q);
		checkWriteResult(ws);

		// ɾ����Ŀ��ɫ
		col = getCollection(IModelConstants.C_PROJECT_ROLE);
		ws = col.remove(q);
		checkWriteResult(ws);

		// �鵵��Ŀ����
		col = getCollection(IModelConstants.C_WORK);
		BasicDBObject update = new BasicDBObject();
		update.put(Work.F_ARCHIVE, Boolean.TRUE);
		for (String archiveField : Work.ARCHIVE_FIELDS) {
			update.put(archiveField, null);
		}
		ws = col.update(q, new BasicDBObject().append("$set", update), false, //$NON-NLS-1$
				true);
		checkWriteResult(ws);

		// �鵵��Ŀ����
		col = getCollection(IModelConstants.C_BULLETINBOARD);
		ws = col.remove(q);
		checkWriteResult(ws);

		// �鵵��Ŀ�ļ�
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Project_32 + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception(Messages.get().Project_33 + this);
		}

		col = getCollection(IModelConstants.C_FOLDER);
		ws = col.update(q, new BasicDBObject().append("$set", //$NON-NLS-1$
				new BasicDBObject().append(Folder.F_ROOT_ID,
						containerOrganizationId)), false, true);
		checkWriteResult(ws);

		BasicDBObject condition = new BasicDBObject();
		condition.put(Work.F_PROJECT_ID, get_id());
		List<PrimaryObject> workList = getRelationByCondition(Work.class,
				condition);
		if (workList != null && workList.size() > 0) {
			for (PrimaryObject po : workList) {
				Work work = (Work) po;
				List<PrimaryObject> documentList = work
						.getOutputDeliverableDocuments();
				if (documentList != null && documentList.size() > 0) {
					for (PrimaryObject primaryObject : documentList) {
						Document document = (Document) primaryObject;
						if (document != null) {
							String lc = document.getLifecycle();
							if (Document.STATUS_WORKING_ID.equals(lc)) {
								document.doSetLifeCycleStatus(context,
										Document.STATUS_RELEASED_ID);
							}
						}
					}
				}
			}
		}

		// д��־
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(),
				Messages.get().Project_34, new Date(), this.getLabel(),
				IModelConstants.DB);
	}

	/**
	 * ��ͣ��Ŀ
	 */
	public Object doPause(IContext context) throws Exception {
		Work root = getWBSRoot();
		root.doPause(context);

		doChangeLifecycleStatus(context, STATUS_PAUSED_VALUE);

		// ������Ŀ��Ϣ��������
		doNoticeProjectAction(context, Messages.get().Project_35);
		return this;
	}

	/**
	 * ������Ŀ
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
						"$set", //$NON-NLS-1$
						new BasicDBObject().append(F_LIFECYCLE,
								STATUS_WIP_VALUE).append(F_ACTUAL_START,
								new Date())),

				true, false);
		set_data(data);

		// ������Ŀ��Ϣ��������
		doNoticeProjectAction(context, Messages.get().Project_36);
		return this;
	}

	/**
	 * ������Ŀ������ɵ�֪ͨ
	 * 
	 * @param context
	 *            ��ǰ��������
	 * @param actionName
	 *            �������ı�����
	 * @return
	 * @throws Exception
	 *             ������Ϣ���ֵĴ���
	 */
	public Message doNoticeProjectAction(IContext context, String actionName)
			throws Exception {
		// �����ռ���
		BasicBSONList participatesIdList = (BasicBSONList) getParticipatesIdList();
		if (participatesIdList == null || participatesIdList.isEmpty()) {
			return null;
		}
		// �ų��Լ�
		participatesIdList.remove(context.getAccountInfo().getConsignerId());

		// ����֪ͨ����
		String title = "" + this + " " + actionName; //$NON-NLS-1$ //$NON-NLS-2$

		// ����֪ͨ����
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-size:14px'>"); //$NON-NLS-1$
		sb.append(Messages.get().Project_37);
		sb.append("</span><br/><br/>"); //$NON-NLS-1$
		sb.append(Messages.get().Project_38);
		sb.append("<br/><br/>"); //$NON-NLS-1$

		sb.append(context.getAccountInfo().getUserId() + "|" //$NON-NLS-1$
				+ context.getAccountInfo().getUserName());
		sb.append(", "); //$NON-NLS-1$
		sb.append(actionName);
		sb.append(Messages.get().Project_39);
		sb.append("\""); //$NON-NLS-1$
		sb.append(this);
		sb.append("\""); //$NON-NLS-1$

		sb.append("<br/><br/>"); //$NON-NLS-1$
		sb.append(Messages.get().Project_40);

		Message message = MessageToolkit.makeMessage(participatesIdList, title,
				context.getAccountInfo().getConsignerId(), sb.toString());

		MessageToolkit.appendEndMessage(message);

		// ���õ�������
		message.appendTargets(this, EDITOR_CREATE_PLAN, false);

		message.doSave(context);

		return message;
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
				new BasicDBObject().append(F__ID, get_id()), null, null, false,
				new BasicDBObject().append("$set", //$NON-NLS-1$
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
			throw new Exception(Messages.get().Project_41 + this);
		}
		// 2.�����Ŀ�Ĺ����Ƿ�������������
		Work rootWork = getWBSRoot();
		message.addAll(rootWork.checkCascadeStart(false));

		List<Work> mileStoneWorks = getMileStoneWorks();
		for (Work mileStoneWork : mileStoneWorks) {
			message.addAll(mileStoneWork.checkWorkStart(false));
		}
		return message;
	}

	@Override
	public List<Object[]> checkCancelAction(IContext context) throws Exception {
		// 1.����Ƿ���Ŀ������Ŀ�����ŵ���Ŀ����Ա
		String userId = context.getAccountInfo().getConsignerId();
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Project_42 + this);
		}

		Role role = org.getRole(Role.ROLE_PROJECT_ADMIN_ID,
				Organization.ROLE_NOT_SEARCH);
		boolean b = true;
		// ʹ��TYPEΪTYPE_PROJECT��RoleParameter��������ĿID������Աָ��
		IRoleParameter roleParameter = getAdapter(IRoleParameter.class);

		List<PrimaryObject> assignment = role.getAssignment(roleParameter);
		for (PrimaryObject po : assignment) {
			User user = (User) po;
			if (userId.equals(user.getUserid())) {
				b = true;
				break;
			}
		}
		if (b) {
			throw new Exception(Messages.get().Project_43 + this);
		}

		// 2.�����Ŀ�Ƿ���Խ��й鵵
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception(Messages.get().Project_44 + this);
		}
		// TODO �鵵�ж��Ƿ�����

		return null;
	}

	@Override
	public List<Object[]> checkFinishAction(IContext context) throws Exception {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception(Messages.get().Project_45 + this);
		}
		// 2.�����Ŀ�Ĺ����Ƿ�������������
		Work work = getWBSRoot();
		message.addAll(work.checkCascadeFinish(work.get_id()));

		// 3.�����Ŀ�Ƿ���Խ��й鵵
		Organization org = getFunctionOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Project_46 + this);
		}
		ObjectId containerOrganizationId = org.getContainerOrganizationId();
		if (containerOrganizationId == null) {
			throw new Exception(Messages.get().Project_47 + this);
		}
		// TODO �鵵�ж��Ƿ�����

		return message;
	}

	@Override
	public List<Object[]> checkPauseAction(IContext context) throws Exception {
		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception(Messages.get().Project_48 + this);
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
		reload();

		// 1.����Ƿ���Ŀ�ĸ�����
		String userId = context.getAccountInfo().getConsignerId();
		if (!userId.equals(this.getChargerId())) {
			throw new Exception(Messages.get().Project_49 + this);
		}

		// ��Ҫ����Ƿ��Ѿ��������ύ����������ǣ������ύ�������������״̬���ԣ�Ҳ�����ύ

		// �жϵ�ǰ��Ŀ״̬�Ƿ���Խ����ύ
		String lc = getLifecycleStatus();
		if (!STATUS_NONE_VALUE.equals(lc)) {
			throw new Exception(Messages.get().Project_50 + this);
		}

		// �ж��Ƿ�����ύ����
		BasicDBObject condition = new BasicDBObject();
		condition.put(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		condition.put(Work.F_PROJECT_ID, get_id());
		condition.put(Work.F_LIFECYCLE,
				new BasicDBObject().append("$nin", new String[] { //$NON-NLS-1$
						STATUS_CANCELED_VALUE, STATUS_FINIHED_VALUE }));

		if (getRelationCountByCondition(Work.class, condition) > 0) {
			throw new Exception(Messages.get().Project_51 + this);
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
		} else if (adapter == CommonHTMLLabel.class) {
			return (T) new ProjectCommonHTMLLable(this);
		} else if (adapter == IEditorInputFactory.class) {
			return (T) new ProjectEditorInputFactory(this);
		} else if (adapter == IRoleParameter.class) {
			return (T) new ProjectRoleParameter(this);
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
	 * �ƽ��û�
	 * 
	 * @param changedUserId
	 * @param changeUserId
	 * @param changeWork
	 * @throws Exception
	 */
	public void doChangeUsers(String changedUserId, String changeUserId,
			List<PrimaryObject> changeWork, IContext context) throws Exception {

		String messageContent = ""; //$NON-NLS-1$
		for (PrimaryObject po : changeWork) {
			Work work = (Work) po;
			String changeWorkUserMessageContent = work.changeWorkUser(
					changedUserId, changeUserId);
			if (changeWorkUserMessageContent != null) {
				messageContent = messageContent + "<br/>" //$NON-NLS-1$
						+ changeWorkUserMessageContent;
			}
		}
		if (messageContent != "") { //$NON-NLS-1$
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
							new BasicDBObject().append("$set", //$NON-NLS-1$
									new BasicDBObject().append(F_PARTICIPATE,
											newParticipatesIdList)), false,
							true);
				}
			}

			Message message = ModelService.createModelObject(Message.class);
			message.setValue(Message.F_RECIEVER, newParticipatesIdList);
			message.setValue(Message.F_DESC, Messages.get().Project_52);
			message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
			message.setValue(Message.F_CONTENT,
					"<span style='font-size:14px'>" //$NON-NLS-1$
							+ Messages.get().Project_53
							+ "</span><br/><br/>" //$NON-NLS-1$
							+ UserToolkit.getUserById(changeUserId)
									.getUsername()
							+ Messages.get().Project_54
							+ UserToolkit.getUserById(changedUserId)
									.getUsername()
							+ Messages.get().Project_55
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
			message.add(new Object[] { Messages.get().Project_56, this,
					SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleStatus)) {
			message.add(new Object[] { Messages.get().Project_57, this,
					SWT.ICON_ERROR });
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { Messages.get().Project_58, this,
					SWT.ICON_WARNING });
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(getLifecycleStatus())) {
			message.add(new Object[] { Messages.get().Project_59, this,
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
		// δ��ɺ�δȡ����
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
	 * ȡ����Ŀ���е���̱�����,���ռƻ���ʼʱ�䣬��С��������
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

	public List<PrimaryObject> getProduct() {
		return getRelationById(F__ID, ProductItem.F_PROJECT_ID,
				ProductItem.class);
	}

	/**
	 * ת��
	 * 
	 * @param context
	 * @param productList
	 * @throws Exception
	 */
	public void doChangeMassProduction(IContext context, List<?> productList)
			throws Exception {
		ObjectId _id = get_id();
		for (Object object : productList) {
			BasicDBObject q = new BasicDBObject();
			q.put(ProductItem.F_PROJECT_ID, _id);
			q.put(ProductItem.F_DESC, object);
			ProductItem productItem = ModelService.createModelObject(q,
					ProductItem.class);
			productItem.doChangeToMassProduction(context);
		}
	}

	public UserProjectPerf makeUserProjectPerf() {
		UserProjectPerf pperf = ModelService
				.createModelObject(UserProjectPerf.class);
		pperf.setValue(UserProjectPerf.F_PROJECT_ID, get_id());
		return pperf;
	}

	public List<PrimaryObject> getSubconcessionsProduct() {
		return getRelationByCondition(
				ProductItem.class,
				new BasicDBObject()
						.append(ProductItem.F_PROJECT_ID, get_id())
						.append(ProductItem.F_IS_MASS_PRODUCTION,
								new BasicDBObject().append("$ne", Boolean.TRUE))); //$NON-NLS-1$
	}

	public String[] getProductCode() {
		List<PrimaryObject> products = getProduct();
		if (products == null) {
			return new String[0];
		}
		String[] result = new String[products.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = products.get(i).getDesc();
		}
		return result;

	}

	public String[] getProductCode2() {
		DBCollection col = getCollection(IModelConstants.C_PRODUCT);
		List<?> list = col.distinct(ProductItem.F_DESC,
				new BasicDBObject().append(ProductItem.F_PROJECT_ID, get_id()));

		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) list.get(i);
		}
		return result;
	}

	public ProjectPresentation getPresentation() {
		return new ProjectPresentation(this);
	}

	public ProjectETL getETL() {
		return new ProjectETL(this);
	}

	public ProjectMonthlyETL getMonthlyETL() {
		return new ProjectMonthlyETL(this);
	}

	/**
	 * ���¼��㹤����ŵĳɱ�
	 * 
	 * @param collection
	 * 
	 * @return
	 */
	public List<DBObject> getAggregationCost(String collection) {
		DBCollection col = getCollection(collection);
		String[] workorders = getWorkOrders();
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(Project.F_WORK_ORDER,
				new BasicDBObject().append("$in", workorders)); //$NON-NLS-1$
		DBObject match = new BasicDBObject().append("$match", matchCondition); //$NON-NLS-1$
		DBObject groupCondition = new BasicDBObject();
		groupCondition.put(
				"_id", //$NON-NLS-1$
				new BasicDBObject().append(WorkOrderPeriodCost.F_YEAR,
						"$" + WorkOrderPeriodCost.F_YEAR).append( //$NON-NLS-1$
						WorkOrderPeriodCost.F_MONTH,
						"$" + WorkOrderPeriodCost.F_MONTH));// //$NON-NLS-1$
		String[] costElements = CostAccount.getCostElemenArray();
		for (int i = 0; i < costElements.length; i++) {
			groupCondition.put(costElements[i],
					new BasicDBObject().append("$sum", "$" + costElements[i])); //$NON-NLS-1$ //$NON-NLS-2$
		}
		DBObject group = new BasicDBObject().append("$group", groupCondition); //$NON-NLS-1$
		AggregationOutput agg = col.aggregate(match, group);
		Iterable<DBObject> results = agg.results();
		Iterator<DBObject> iter = results.iterator();

		double summary = 0d;
		ArrayList<DBObject> result = new ArrayList<DBObject>();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			for (int i = 0; i < costElements.length; i++) {
				Number value = (Number) data.get(costElements[i]);
				summary += value.doubleValue();
			}
			data.put("summ", summary); //$NON-NLS-1$
			result.add(data);
		}

		return result;
	}

	public List<DBObject> getAggregationRevenue() {
		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		String[] productCode = getProductCode();
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER,
				new BasicDBObject().append("$in", productCode)); //$NON-NLS-1$
		DBObject match = new BasicDBObject().append("$match", matchCondition); //$NON-NLS-1$
		DBObject groupCondition = new BasicDBObject();
		groupCondition.put(
				"_id", //$NON-NLS-1$
				new BasicDBObject().append(SalesData.F_ACCOUNT_YEAR,
						"$" + SalesData.F_ACCOUNT_YEAR).append( //$NON-NLS-1$
						SalesData.F_ACCOUNT_MONTH,
						"$" + SalesData.F_ACCOUNT_MONTH));// //$NON-NLS-1$
		groupCondition.put(SalesData.F_SALES_INCOME,
				new BasicDBObject().append("$sum", "$" //$NON-NLS-1$ //$NON-NLS-2$
						+ SalesData.F_SALES_INCOME));
		groupCondition.put(SalesData.F_SALES_COST, new BasicDBObject().append(
				"$sum", "$" + SalesData.F_SALES_COST)); //$NON-NLS-1$ //$NON-NLS-2$

		DBObject group = new BasicDBObject().append("$group", groupCondition); //$NON-NLS-1$
		DBObject sort = new BasicDBObject().append(
				"$sort", new BasicDBObject().append("_id", -1)); //$NON-NLS-1$
		AggregationOutput agg = col.aggregate(match, group, sort);
		Iterable<DBObject> results = agg.results();
		Iterator<DBObject> iter = results.iterator();

		ArrayList<DBObject> result = new ArrayList<DBObject>();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			result.add(data);
		}

		return result;
	}

	/**
	 * ����ETL����
	 * 
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	public void doModifyETL(String field, String value) throws Exception {
		// ���±���¼ֵ
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				queryThis(),
				new BasicDBObject().append("$set",
						new BasicDBObject().append("etl." + field, value)));
		checkWriteResult(ws);
	}

	public void doModifyMonthETL(String field, String value) throws Exception {
		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		WriteResult ws = col.update(
				new BasicDBObject().append(ProjectMonthlyETL.F_PROJECTID,
						this.get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(field, value)));
		checkWriteResult(ws);

	}

	public void doUpdateBusinessManager(User user) {
		String userid = user.getUserid();
		String username = user.getUsername();

		Organization org = user.getOrganization();
		ObjectId orgid = org.get_id();
		String orgtext = org.getPath(2);

		// ������Ŀ

		DBCollection col = getCollection();
		col.update(queryThis(), new BasicDBObject().append(
				"$set",
				new BasicDBObject()
						.append(F_BUSINESS_CHARGER, userid)
						.append(F_BUSINESS_ORGANIZATION, orgid)
						.append("etl." + ProjectETL.F_BUSINESS_MANAGER_TEXT,
								username)
						.append("etl."
								+ ProjectETL.F_BUSINESS_ORGANIZATION_TEXT,
								orgtext)));

		col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		col.update(new BasicDBObject().append(ProjectMonthlyETL.F_PROJECTID,
				get_id()), new BasicDBObject().append(
				"$set",
				new BasicDBObject()
						.append(F_BUSINESS_CHARGER, userid)
						.append(F_BUSINESS_ORGANIZATION, orgid)
						.append(ProjectETL.F_BUSINESS_MANAGER_TEXT, username)
						.append(ProjectETL.F_BUSINESS_ORGANIZATION_TEXT,
								orgtext)));

		setValue(F_BUSINESS_CHARGER, userid);
		setValue(F_BUSINESS_ORGANIZATION, orgid);
	}

	public long getDelayDays() {
		Date pf = getPlanFinish();
		Date af = getActualFinish();
		if (pf == null) {
			return -1;
		}
		if (af == null) {
			af = new Date();
		}
		return (af.getTime() - pf.getTime()) / (1000 * 60 * 60);
	}

	public ProjectTemplate getTemplate() {
		ObjectId projectTemplateId = getObjectIdValue(F_PROJECT_TEMPLATE_ID);
		if (projectTemplateId != null) {
			return ModelService.createModelObject(ProjectTemplate.class,
					projectTemplateId);
		}
		return null;
	}

	/**
	 * ��ù�ʱ���� ��ĿF_WORKTIME_PARA_X�ֶ�û��ֵʱ����null,��Ŀ�Ĺ�ʱ�����ֶα������һ��BsonList
	 * ����Ŀ��ʱ�����в�����������paraXId��Ӧ��paraXʱ,����Ϊnull
	 * 
	 * @param typeId
	 * @param typeFieldName
	 * @return
	 */
	public DBObject getParaXOrParaY(ObjectId typeId, String typeFieldName) {
		// ͨ����ʱ����id����ȡ��ʱ����
		BasicBSONList paraXs = (BasicBSONList) getValue(typeFieldName);
		if (paraXs == null) {
			return null;
		}
		for (int i = 0; i < paraXs.size(); i++) {
			DBObject paraX = (DBObject) paraXs.get(i);
			ObjectId _typeId = (ObjectId) paraX.get(F__ID);
			if (_typeId.equals(typeId)) {
				return paraX;
			}
		}
		return null;
	}

	/**
	 * ��ȡ��ʱ����ѡ��
	 * 
	 * @param paraXId
	 * @return
	 */
	public DBObject getParaXOption(ObjectId paraXId) {
		DBObject paraX = getParaXOrParaY(paraXId, F_WORKTIME_PARA_X);
		if (paraX == null) {
			return null;
		}
		BasicBSONList paraXs = (BasicBSONList) paraX
				.get(F_WORKTIME_PARA_OPTIONS);
		if (paraXs == null || paraXs.size() == 0) {
			return null;
		}
		DBObject option = (DBObject) paraXs.get(0);
		return option;

	}

	/**
	 * Ϊ��Ŀ�Ĺ�ʱ����������ѡ��
	 * 
	 * @param paraXId
	 * @param option
	 */
	public void makeParaXOption(ObjectId paraXId, String paraXDesc,
			DBObject option) {
		BasicBSONList paraXs = null;
		Object value = getValue(F_WORKTIME_PARA_X);

		if (value instanceof BasicBSONList) {
			paraXs = (BasicBSONList) value;
			for (Object object : paraXs) {
				DBObject paraX = (DBObject) object;
				if (paraX.get(F__ID).equals(paraXId)) {
					BasicBSONList options = (BasicBSONList) paraX
							.get(F_WORKTIME_PARA_OPTIONS);
					options.clear();
					options.add(option);
					return;
				}
			}
		} else {
			paraXs = new BasicDBList();
			setValue(F_WORKTIME_PARA_X, paraXs);
		}
		BasicDBObject paraX = new BasicDBObject();
		paraX.put(F__ID, paraXId);
		paraX.put(F_DESC, paraXDesc);
		BasicBSONList options = new BasicDBList();
		options.add(option);
		paraX.put(F_WORKTIME_PARA_OPTIONS, options);
		paraXs.add(paraX);

	}

	/**
	 * ������Ŀ��ѡ��Ĺ�ʱ���� ����Ŀ�Ѿ�ѡ���˹�ʱ����ʱ���ȶԸù�ʱ�����봫��Ĺ�ʱ�����Ƿ�һ�£�����һ�£�ʹ���µĹ�ʱ��������ɵĹ�ʱ����
	 * ͬʱ�����øù�ʱ������Ĭ��ѡ�������Ĺ�ʱ����Ϊnullʱ���������Ŀ�б���Ĺ�ʱ�������͵�ֵ
	 * 
	 * @param workTimeProgram
	 */
	public void makeSelectedWorkTimeProgram(WorkTimeProgram workTimeProgram) {
		if (workTimeProgram == null) {
			clearWorkTimeProgramSetting();
			return;
		}

		if (workTimeProgram.get_id().equals(getValue(F_WORKTIMEPROGRAM_ID))) {
			return;
		}

		clearWorkTimeProgramSetting();
		setValue(F_WORKTIMEPROGRAM_ID, workTimeProgram.get_id());
		makeDefaultParaXOption(workTimeProgram);

	}

	/**
	 * Ϊ��Ŀ�еĹ�ʱ��������Ĭ�ϵ�ѡ����ù�ʱ����ֻ��һ��ѡ��ʱ������ѡ�����õ���Ŀ�Ĺ�ʱ������
	 */
	private void makeDefaultParaXOption(WorkTimeProgram workTimeProgram) {

		BasicBSONList paraXs = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		for (Object object : paraXs) {
			DBObject paraX = (DBObject) object;
			BasicBSONList paraXOptions = (BasicBSONList) paraX
					.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
			if (paraXOptions.size() == 1) {
				ObjectId paraXId = (ObjectId) paraX.get(F__ID);
				String paraXDesc = (String) paraX.get(F_DESC);
				makeParaXOption(paraXId, paraXDesc,
						(DBObject) paraXOptions.get(0));
			}
		}

	}

	/**
	 * ����ʱ����ѡ���������ѡ���ÿ�
	 */
	public void clearWorkTimeProgramSetting() {
		setValue(F_WORKTIME_PARA_X, null);
		setValue(F_WORKTIME_PARA_Y, null);
	}

	/**
	 * ����ʱ�����Լ���ʱ����ѡ���������ѡ���ÿ�
	 */
	public void clearWorkTimeProgram() {
		setValue(F_WORKTIMEPROGRAM_ID, null);
		clearWorkTimeProgramSetting();
	}

	public BasicBSONList getWorkTimeParaYOption(ObjectId paraYId) {
		DBObject paraY = getParaXOrParaY(paraYId, F_WORKTIME_PARA_Y);
		if (paraY == null) {
			return null;
		}
		return (BasicBSONList) paraY.get(F_WORKTIME_PARA_OPTIONS);

	}

	/**
	 * �жϸ�ѡ��ʱ����ѡ���������ѡ���Ƿ�����Ŀ�б�ѡ��
	 * 
	 * @param optionId
	 * @param typeFieldName
	 *            F_WORKTIME_PARA_X/F_WORKTIME_PARA_Y
	 * @return
	 */
	public boolean isSelectedWorkTimeOption(ObjectId optionId,
			String typeFieldName) {
		Assert.isNotNull(optionId);
		Assert.isNotNull(typeFieldName);
		// �����Ŀ��ǰ��ѡ�Ĺ�ʱ����
		ObjectId programId = (ObjectId) getValue(F_WORKTIMEPROGRAM_ID);
		WorkTimeProgram program = ModelService.createModelObject(
				WorkTimeProgram.class, programId);
		// ��ȡ��ʱ�����������ͣ�ΪBsonList����
		// ע�⵽ �ڹ�ʱ�����еĹ�ʱ���ͺ͹�ʱ�������ֶ����ƿ�������Ŀ�еĲ�ͬ���˴�����һ��ת��
		String key = F_WORKTIME_PARA_Y.equals(typeFieldName) ? WorkTimeProgram.F_WORKTIME_PARA_Y
				: WorkTimeProgram.F_WORKTIME_PARA_X;
		DBObject type = program.getParaX(optionId, key);
		if (type == null) {
			return false;
		}
		ObjectId typeId = (ObjectId) type.get(F__ID);

		// ����Ŀ�л�ö�Ӧ��ʱ�����͵�ѡ��
		BasicBSONList workTimeParaYOptions = getWorkTimeParaYOption(typeId);
		if (workTimeParaYOptions == null) {
			return false;
		}
		// ����Ŀû�ж�Ӧ�Ĺ�ʱ������ѡ��ʱ�����ز�ѡ�е�ͼƬ�����򷵻�ѡ�е�ͼƬ
		for (Object object : workTimeParaYOptions) {
			if (((DBObject) object).get(WorkTimeProgram.F__ID).equals(optionId)) {
				// ��Ŀ��ѡ���˸������͸�ѡ��
				return true;
			}
		}
		return false;
	}

	public WorkTimeProgram getWorkTimeProgram() {
		ObjectId programId = (ObjectId) getValue(F_WORKTIMEPROGRAM_ID);
		if (programId == null) {
			return null;
		}
		return ModelService.createModelObject(WorkTimeProgram.class, programId);
	}

	public void selectWorkTimeParaYOption(ObjectId typeId, String typeDesc,
			DBObject option, boolean select) {
		if (select) {
			makeWorkTimeParaYOption(typeId, typeDesc, option);
		} else {
			removeWorkTimeParaYOption(typeId, option);
		}
	}

	private void removeWorkTimeParaYOption(ObjectId typeId, DBObject option) {
		BasicBSONList typeOptions = getWorkTimeParaYOption(typeId);
		for (Object object : typeOptions) {
			DBObject typeOption = (DBObject) object;
			if (typeOption.get(F__ID).equals(option.get(F__ID))) {
				typeOptions.remove(typeOption);
				return;
			}
		}
	}

	public void makeWorkTimeParaYOption(ObjectId workTimeParaYId,
			String workTimeParaYDesc, DBObject option) {
		BasicBSONList workTimeParaYs = null;
		Object value = getValue(F_WORKTIME_PARA_Y);

		if (value instanceof BasicBSONList) {
			workTimeParaYs = (BasicBSONList) value;
			for (Object object : workTimeParaYs) {
				DBObject workTimeParaY = (DBObject) object;
				if (workTimeParaY.get(F__ID).equals(workTimeParaYId)) {
					BasicBSONList options = (BasicBSONList) workTimeParaY
							.get(F_WORKTIME_PARA_OPTIONS);
					for (Object object2 : options) {
						// ��Ŀ���Ѱ�������ѡ��
						if (option.get(F__ID).equals(
								((DBObject) object2).get(F__ID))) {
							return;
						}
					}
					options.add(option);
					setValue(F_WORKTIME_PARA_Y, workTimeParaYs);
					return;
				}
			}
		} else {
			workTimeParaYs = new BasicDBList();
		}
		BasicDBObject paraY = new BasicDBObject();
		paraY.put(F__ID, workTimeParaYId);
		paraY.put(F_DESC, workTimeParaYDesc);
		BasicBSONList options = new BasicDBList();
		options.add(option);
		paraY.put(F_WORKTIME_PARA_OPTIONS, options);
		workTimeParaYs.add(paraY);
		setValue(F_WORKTIME_PARA_Y, workTimeParaYs);
	}

	public void checkWorkTimeProgram() throws Exception {

		ProjectTemplate projectTemplate = getProjectTemplate();
		BasicBSONList workTimePrograms = (BasicBSONList) projectTemplate
				.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS);
		// ��Ŀ��ѡ����Ŀģ��δ���幤ʱ��������֤ͨ��
		if (workTimePrograms == null) {
			return;
		}
		// ��Ŀ��ѡ����Ŀģ�嶨���˹�ʱ����
		WorkTimeProgram workTimeProgram = getWorkTimeProgram();
		if (workTimeProgram == null) {
			// û��ѡ��ʱ����,������ʾû��ѡ��ʱ����
			throw new Exception(Messages.get().WorkTimeProgramNotSelected);
		} else {
			// ѡ���˹�ʱ����
			// 1.��鹤ʱ����ѡ���Ƿ�����
			// �ж�û��ѡ�Ĺ�ʱ����ѡ��
			boolean valid = checkWorkTimeParaXOption(workTimeProgram);
			if (!valid) {
				throw new Exception(Messages.get().ParaXOptionNotSelected);
			}
			// 2.������е�������ѡ��
			// ���е�������ѡ�ûѡ����ʾ����
			valid = checkWorkTimeParaYOption(workTimeProgram);
			if (!valid) {
				throw new Exception(Messages.get().ParaXOptionNotSelected);
			}
			return;
		}

	}

	private boolean checkWorkTimeParaXOption(WorkTimeProgram program) {
		BasicBSONList types_inProg = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		BasicBSONList types_inProj = (BasicBSONList) getValue(F_WORKTIME_PARA_X);
		if (types_inProj == null || types_inProj.isEmpty()) {
			return false;
		}

		for (Object object : types_inProg) {
			DBObject type_inProg = (DBObject) object;
			// ����Ŀ�л�ȡ�ù�ʱ����
			for (Object object2 : types_inProj) {
				DBObject type_inProj = (DBObject) object2;
				// �ж���Ŀ�еĹ�ʱ����id�͹�ʱ�����еĹ�ʱ����id�Ƿ�һ��
				if (type_inProj.get(F__ID).equals(type_inProg.get(F__ID))) {
					BasicBSONList options_inProj = (BasicBSONList) type_inProj
							.get(F_WORKTIME_PARA_OPTIONS);
					if (options_inProj == null || options_inProj.isEmpty()) {
						return false;
					}
					// �ж���Ŀ�е�ѡ���Ƿ��ڹ�ʱ������Ӧ���͵�ѡ����
					// ȡ����ʱ�����ж�Ӧ���͵�����ѡ��
					BasicBSONList options_inProg = (BasicBSONList) type_inProg
							.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
					Set<ObjectId> optionIdSet_inProg = new HashSet<ObjectId>();
					for (Object object3 : options_inProg) {
						DBObject option_inProg = (DBObject) object3;
						optionIdSet_inProg.add((ObjectId) option_inProg
								.get(F__ID));
					}

					for (Object object4 : options_inProj) {
						DBObject option_inProj = (DBObject) object4;
						// �����еĶ�Ӧ�Ĺ�ʱ����ѡ�������Ŀ�еĹ�ʱ����ѡ����׳��쳣
						if (optionIdSet_inProg.contains(option_inProj
								.get(F__ID))) {
							return true;
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �����Ŀ�еĹ�ʱ������ѡ��
	 * 
	 * @param program
	 * @return
	 */
	private boolean checkWorkTimeParaYOption(WorkTimeProgram program) {
		// 1. ��ȡ�����е�������
		BasicBSONList types_inProg = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_Y);

		// 2. ��ȡ��Ŀ��������
		BasicBSONList types_inProj = (BasicBSONList) getValue(F_WORKTIME_PARA_Y);
		if (types_inProj == null || types_inProj.isEmpty()) {
			return false;
		}

		// 3.�Է����������;��б������ж���Ŀ���������Ƿ���ѡ��
		for (Object type_inProg : types_inProg) {
			// ����Ŀ���������в��Ҹ�����
			for (Object type_inProj : types_inProj) {
				if (((DBObject) type_inProg).get(F__ID).equals(
						((DBObject) type_inProj).get(F__ID))) {
					// �ҵ���Ӧ���������ͺ󣬼��ѡ��

					// ��÷����ж�Ӧ�����͵�ѡ��
					BasicBSONList options_inProg = (BasicBSONList) ((DBObject) type_inProg)
							.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
					// ��鷽���ж�Ӧ��������ѡ������Ŀ���Ƿ���ѡ��
					Set<ObjectId> optionSet_inProg = new HashSet<ObjectId>();
					for (Object option_inProg : options_inProg) {
						optionSet_inProg
								.add((ObjectId) ((DBObject) option_inProg)
										.get(F__ID));
					}

					// �����Ŀ��Ӧ��������ѡ��
					BasicBSONList options_inProj = (BasicBSONList) ((DBObject) type_inProj)
							.get(F_WORKTIME_PARA_OPTIONS);
					for (Object option_inProj : options_inProj) {
						if (optionSet_inProg
								.contains(((DBObject) option_inProj).get(F__ID))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * ��ȡ��Ŀ�ж���Ĺ�ʱ������ѡ�� ����Ŀ�е�������û��ֵ����������û��������ѡ��ʱ���᷵��һ��û��Ԫ�صļ��ϣ����ϲ���Ϊnull
	 * 
	 * @return
	 */
	public Set<ObjectId> getWorkTimeParaYOptionIds() {
		// ʵ����һ��Set����
		Set<ObjectId> result = new HashSet<ObjectId>();
		// ȡ����ʱ�����ͣ���BasicBSONList����
		BasicBSONList paraYs = (BasicBSONList) getValue(F_WORKTIME_PARA_Y);
		if (paraYs != null) {
			// �ж������Ͳ�Ϊ�գ���ѭ�����������͵�list
			for (int i = 0; i < paraYs.size(); i++) {
				// ���������list�е�DBObject�����������
				DBObject paraY = (DBObject) paraYs.get(i);
				// ��ȡ�����͵�ѡ���BasicBSONList����
				BasicBSONList options = (BasicBSONList) paraY
						.get(F_WORKTIME_PARA_OPTIONS);
				// ѭ������������ѡ��list
				for (int j = 0; j < options.size(); j++) {
					// �õ�DBObject���͵�������ѡ��
					DBObject option = (DBObject) options.get(j);
					// ��ȡ������ѡ���id������ӵ�Set������
					result.add((ObjectId) option.get(F__ID));
				}
			}
		}
		return result;
	}

	/**
	 * �༭��ʱ����
	 * 
	 * @param context
	 * @return
	 */
	public boolean canEditWorkTimesSetting(IContext context) {
		// ����Ŀ���ǹ滮��ʱ���ܱ༭��ʱ����
		String lifecycleStatus = getLifecycleStatus();
		if (Utils.inArray(lifecycleStatus, new String[] {
				STATUS_CANCELED_VALUE, STATUS_FINIHED_VALUE,
				STATUS_ONREADY_VALUE, STATUS_PAUSED_VALUE, STATUS_WIP_VALUE })) {
			return false;
		}
		// context������Ŀ������ʱ���ܱ༭��ʱ����
		String chargerId = getChargerId();
		String consignerId = context.getAccountInfo().getConsignerId();
		if (consignerId.equals(chargerId)) {
			return true;
		} else {
			// context������Ŀְ�ܲ��ŵ���Ŀ����Աʱ���ܱ༭��ʱ����
			Organization organization = getFunctionOrganization();
			String[] userIds = organization.getRoleAssignmentUserIds(
					Role.ROLE_PROJECT_ADMIN_ID, Organization.ROLE_NOT_SEARCH);
			if (Utils.inArray(consignerId, userIds)) {
				return true;
			} else {
				userIds = organization.getRoleAssignmentUserIds(
						Role.ROLE_WORKS_STATISTICS_ID,
						Organization.ROLE_SEARCH_UP);
				if (Utils.inArray(consignerId, userIds)) {
					return true;
				} else {
					return false;
				}
			}

		}
	}

	// /**
	// * ��Ŀ�Ĺ�ʱ����ֻ��
	// *
	// * @param context
	// * @return true ֻ��
	// */
	// public boolean canWorkTimeProgramReadonly(IContext context) {
	// //��Ŀ�ǳ־û� ʱ,��ʱ�������Ա༭
	// if (!isPersistent()) {
	// return false;
	// }
	// String lc = getLifecycleStatus();
	// if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
	// //��Ŀ����״̬ʱ,��ʱ�������Ա༭
	// return false;
	// } else {
	// //��õ�ǰ��¼�û�
	// String consignerId = context.getAccountInfo().getConsignerId();
	// //�����Ŀ�Ĺ�����֯
	// Organization functionOrg = getFunctionOrganization();
	// //��ñ�����֯�й�ʱͳ��Ա���û�id,��String������
	// String[] assignmentUserIds = functionOrg
	// .getRoleAssignmentUserIds(Role.ROLE_WORKS_STATISTICS_ID,
	// Organization.ROLE_NOT_SEARCH);
	// for (String userId : assignmentUserIds) {
	// if (consignerId.equals(userId)) {
	// //��ǰ��¼�û��ǹ�ʱͳ��Ա,��ʱ�������Ա༭
	// return false;
	// }
	// }
	// }
	// return true;
	// }
	//
	// /**
	// * ��Ŀ�Ĺ�����ʱ����ֻ��
	// * @param context
	// * @return
	// */
	// public boolean canWorkTimeParaXReadonly(IContext context) {
	// //��Ŀ�ǳ־û� ʱ,������ʱ�������Ա༭
	// if (!isPersistent()) {
	// return false;
	// }
	// //�����Ŀ����������״̬
	// String lc = getLifecycleStatus();
	// if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
	// //��Ŀ����״̬ʱ,������ʱ�������Ա༭
	// return false;
	// } else {
	// String consignerId = context.getAccountInfo().getConsignerId();
	// Organization functionOrg = getFunctionOrganization();
	// String[] projectAdminUserIds;
	// if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
	// //��Ŀ��׼����״̬ʱ,�ڱ�����Ŀ�Ĺ�����֯�л����Ŀ����Ա���û�id����
	// projectAdminUserIds = functionOrg.getRoleAssignmentUserIds(
	// Role.ROLE_PROJECT_ADMIN_ID,
	// Organization.ROLE_NOT_SEARCH);
	// } else {
	// //��Ŀ����׼����״̬,��Ŀ����Ա���鳤��Ϊ0
	// projectAdminUserIds = new String[0];
	// }
	// //�ڱ�����Ŀ�Ĺ�����֯�л�ù�ʱͳ��Ա���û�id����
	// String[] workStatisticsUserIds = functionOrg
	// .getRoleAssignmentUserIds(Role.ROLE_WORKS_STATISTICS_ID,
	// Organization.ROLE_NOT_SEARCH);
	// //����Ŀ����Ա���û�id����,��ʱͳ��Ա���û�id�������Object����
	// Object[] assignmentUserIds = Utils.arrayAppend(
	// workStatisticsUserIds, projectAdminUserIds);
	//
	// for (Object userId : assignmentUserIds) {
	// if (consignerId.equals(userId)) {
	// //��ǰ��¼�û�����Ŀ����Ա��ʱͳ��Ա,���Ա༭������ʱ����
	// return false;
	// }
	// }
	// }
	// return true;
	// }
	//
	// /**
	// * ��Ŀ��ʱ����ֻ��
	// * �ɱ༭�������빤ʱ����һ��,����ֱ�ӵ��ù�ʱ����ֻ���ķ���
	// * @param context
	// * @return
	// */
	// public boolean canWorkTimeParaYReadonly(IContext context) {
	// return canWorkTimeProgramReadonly(context);
	// }

}
