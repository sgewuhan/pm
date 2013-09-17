package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.business.model.event.AccountEvent;
import com.sg.business.resource.BusinessResource;

/**
 * ��֯
 * <p/>
 * 
 * ��֯Ϊ��˾����֯�ṹ<br>
 * ��֯����������֯,��֯��ɫ���û�<br>
 * 
 * @author jinxitao
 */
public class Organization extends PrimaryObject {

	/**
	 * ���ź��Ŷӱ༭��
	 */
	public static final String EDITOR_SUBTEAM = "editor.organization.subteam";

	/**
	 * ������֯�༭��
	 */
	public static final String EDITOR_TEAM = "editor.organization.team";
	/**
	 * ��֯���
	 */
	public static final String F_ORGANIZATION_NUMBER = "organizationnumber";

	/**
	 * ��֯˵��
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * ��֯ȫ��
	 */
	public static final String F_FULLDESC = "fulldesc";

	/**
	 * ��֯���ϼ���֯_id
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * ��֯�Ƿ������Ŀ����ְ��,������Ŀ����ְ�ܵ���֯����Ϊ��Ŀ�Ĺ����֯
	 */
	public static final String F_IS_FUNCTION_DEPARTMENT = "isfunctiondepartment";

	/**
	 * ��֯�Ƿ����Ϊ�ĵ�����,Ϊ�ĵ���������֯���Թ鵵��Ŀ�ĵ�
	 */
	public static final String F_IS_CONTAINER = "iscontainer";

	/**
	 * ��֯�ĳɱ����Ĵ��룬 �ɱ����Ĵ���ΪSAPϵͳ�еĳɱ����Ĵ���
	 */
	public static final String F_COST_CENTER_CODE = "costcentercode";

	/**
	 * ��֯���ͣ���֯���ͷ�Ϊ���ˣ���ҵ�������ź��Ŷ�
	 */
	public static final String F_ORGANIZATION_TYPE = "organizationtype";

	/**
	 * ������֯��˵��. see {@link #F_DESCRIPTION}
	 * 
	 * @return String
	 */
	public String getDescription() {
		return (String) getValue(F_DESCRIPTION);
	}

	/**
	 * �ж���֯�Ƿ������Ŀ����ְ�ܡ�see {@link #F_IS_FUNCTION_DEPARTMENT}
	 * 
	 * @return boolean
	 */
	public boolean isFunctionDepartment() {
		return Boolean.TRUE
				.equals((Boolean) getValue(F_IS_FUNCTION_DEPARTMENT));
	}

	// public void handleCreateSubTeam(Object data) {
	// System.out.println("handleCreateSubTeam" + data);
	// }
	//
	// public void handleRemoveTeam(Object data) {
	// System.out.println("handleCreateSubTeam" + data);
	// }
	//
	// public static Organization handleCreateRootOrganization() {
	// System.out.println("handleCreateRootOrganization");
	// return null;
	// }

	/**
	 * ������֯���ϼ���֯_id see {@link #F_PARENT_ID}
	 * 
	 * @return ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	/**
	 * ������֯�ı�š�see {@link #F_ORGANIZATION_NUMBER}
	 * 
	 * @return String
	 */
	public String getOrganizationNumber() {
		return (String) getValue(F_ORGANIZATION_NUMBER);
	}

	/**
	 * �ж���֯�Ƿ�����ĵ�����ְ�ܡ�see {@link #F_IS_CONTAINER}
	 * 
	 * @return boolean
	 */
	public boolean isContainer() {
		return Boolean.TRUE.equals((Boolean) getValue(F_IS_CONTAINER));
	}

	/**
	 * ��ȡ��֯�ĳɱ����Ĵ��롣see {@link #F_COST_CENTER_CODE}
	 * 
	 * @return String
	 */
	public String getCostCenterCode() {
		return (String) getValue(F_COST_CENTER_CODE);
	}

	/**
	 * ��ȡ��֯�����͡�see {@link #F_ORGANIZATION_TYPE}
	 * 
	 * @return String
	 */
	public String getOrganizationType() {
		return (String) getValue(F_ORGANIZATION_TYPE);
	}

	/**
	 * ������֯��ϵͳ�е���ʾ����
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getDesc();
	}

	/**
	 * ������֯��ϵͳ�е���ʾ���ݵĸ�ʽ
	 * 
	 * @return String
	 */
	@Override
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();
		String imageUrl = "<img src='" + getImageURL()
				+ "' style='float:left;padding:2px' width='24' height='24' />";
		String label = getLabel();
		String path = getPath();

		sb.append(imageUrl);
		sb.append("<b>");
		sb.append(label);
		sb.append("</b>");
		sb.append("<br/>");
		sb.append("<small>");
		sb.append(path);
		sb.append("</small>");
		return sb.toString();
	}

	/**
	 * ������֯��ϵͳ�е���ʾͼ���ַ
	 * 
	 * @return String
	 */
	public String getImageURL() {
		if (getValue(F_PARENT_ID) == null) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_ORG_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else {
			return FileUtil.getImageURL(BusinessResource.IMAGE_TEAM_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
	}

	/**
	 * ������֯��ϵͳ�е���ʾͼ��
	 * 
	 * @return String
	 */
	@Override
	public Image getImage() {
		if (getValue(F_PARENT_ID) == null) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ORG_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_TEAM_16);
		}
	}

	/**
	 * �����Ŀģ�浽��֯��
	 * 
	 * @param projectTemplateId
	 */
	public void doAddProjectTemplate(ObjectId projectTemplateId) {
		DBCollection projectTemplateCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		projectTemplateCol.update(
				new BasicDBObject(ProjectTemplate.F__ID, projectTemplateId),
				new BasicDBObject("$set", (new BasicDBObject().append(
						ProjectTemplate.F_ORGANIZATION_ID, get_id()))), false,
				true);
	}

	/**
	 * ��ӹ������嵽��֯��
	 * 
	 * @param workDefinitionId
	 */
	public void doAddWorkDefinition(ObjectId workDefinitionId) {
		DBCollection projectTemplateCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		projectTemplateCol.update(
				new BasicDBObject(WorkDefinition.F__ID, workDefinitionId),
				new BasicDBObject("$set", (new BasicDBObject().append(
						WorkDefinition.F_ORGANIZATION_ID, get_id()))), false,
				true);
	}

	/**
	 * ����û�����֯��
	 * 
	 * @param userIdList
	 *            ,�û�_id������
	 */
	public void doAddMembers(ObjectId[] userIdList) {
		// ��ȡ�û���
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		// �����û�����������֯
		userCol.update(
				new BasicDBObject().append(User.F__ID,
						new BasicDBObject().append("$in", userIdList)),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(User.F_ORGANIZATION_ID,
								get_id()).append(User.F_ORGANIZATION_NAME,
								getDesc())), false, true);
	}

	/**
	 * �����û���������û�����֯��
	 * 
	 * @param userDatas
	 *            ,�û����󼯺�
	 */
	public void doAddMembers(List<PrimaryObject> userDatas) {
		ObjectId[] userIdList = new ObjectId[userDatas.size()];
		for (int i = 0; i < userDatas.size(); i++) {
			User user = (User) userDatas.get(i);
			String userId = user.getUserid();
			UserSessionContext.noticeAccountChanged(userId, new AccountEvent(
					AccountEvent.EVENT_ORG_CHANGED, this));
			userIdList[i] = (ObjectId) user.get_id();
		}
		doAddMembers(userIdList);
	}

	/**
	 * TODO
	 * 
	 * ɾ����֯�ж� 1.��֯û���¼���֯ 2.û�й�������֯���û� 3.��֯���½�ɫû���û�����֯�ϹҵĽ�ɫҲҪ����飩 4.��Ŀģ�� 5.ͨ�ù�������
	 * 6.������������ 7.�ĵ�ģ�� 8.�ĵ��� 9.������֯�͹�����֯����Ŀ
	 * 
	 */

	/**
	 * ɾ����֯
	 * 
	 * @param context
	 *            ,������
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// ɾ������֯�Ľ�ɫ
		List<PrimaryObject> childrenRole = getRoles();
		for (int i = 0; i < childrenRole.size(); i++) {
			childrenRole.get(i).doRemove(context);
		}
		// ɾ������֯���¼���֯
		List<PrimaryObject> childrenOrg = getChildrenOrganization();
		for (int i = 0; i < childrenOrg.size(); i++) {
			childrenOrg.get(i).doRemove(context);
		}
		// ɾ����֯�����

		super.doRemove(context);
	}

	/**
	 * ��ȡ��֯���ϼ���֯
	 * 
	 * @return PrimaryObject
	 */
	public PrimaryObject getParentOrganization() {
		ObjectId organization_id = getParent_id();
		if (organization_id != null) {
			return ModelService.createModelObject(Organization.class,
					organization_id);
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ�¼���֯
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getChildrenOrganization() {
		return getRelationById(F__ID, F_PARENT_ID, Organization.class);
	}

	/**
	 * ��ȡ��֯�µ����н�ɫ
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getRoles() {
		return getRelationById(F__ID, Role.F_ORGANIZATION_ID, Role.class);
	}

	/**
	 * ��ȡ��ǰ��֯������ϵͳ��ɫ
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getSystemRoles() {
		DBObject condition = new BasicDBObject();
		condition.put(Role.F_ORGANIZATION_ID, get_id());
		condition.put(Role.F_ROLE_NUMBER,
				new BasicDBObject().append("$in", Role.ROLE_ID_SYSTEM));
		return getRelationByCondition(Role.class, condition);
	}

	/**
	 * �����֯���Ƿ����ĳ����ɫ
	 * 
	 * @param roleNumber
	 *            ,��ɫ���
	 * @return boolean
	 */
	public boolean hasRole(String roleNumber) {
		return getRelationCountByCondition(Role.class,
				new BasicDBObject().append(Role.F_ROLE_NUMBER, roleNumber)
						.append(Role.F_ORGANIZATION_ID, get_id())) > 0;
	}

	/**
	 * �����֯��һ����ɫ
	 * 
	 * @param roleNumber
	 *            , ��ɫ���
	 * @param roleName
	 *            , ��ɫ����
	 * @return Role
	 */
	public Role doAddRole(String roleNumber, String roleName) {
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE);
		BasicDBObject data = new BasicDBObject();
		data.put(Role.F__ID, new ObjectId());
		data.put(Role.F_ORGANIZATION_ID, get_id());
		data.put(Role.F_ROLE_NUMBER, roleNumber);
		data.put(Role.F_DESC, roleName);
		WriteResult wr = roleCollection.insert(data);
		if (wr.getN() > 0) {
			return ModelService.createModelObject(data, Role.class);
		}
		return null;
	}

	/**
	 * ��õ�ǰ��֯��·��
	 * 
	 * @return String
	 */
	public String getPath() {
		PrimaryObject parent = getParentOrganization();
		if (parent instanceof Organization) {
			return ((Organization) parent).getPath() + "/" + getDesc();
		} else {
			return getDesc();
		}
	}

	/**
	 * �жϵ�ǰ��֯�Ƿ���ĳ����֯���ϼ���֯
	 * 
	 * @param organization
	 *            ,��֯
	 * @return boolean
	 */
	public boolean isSuperOf(Organization organization) {
		Organization parent = (Organization) organization
				.getParentOrganization();
		while (parent != null) {
			if (parent.equals(organization)) {
				return true;
			}
			parent = (Organization) organization.getParentOrganization();
		}
		return false;
	}

	/**
	 * �½�ͨ�ù�������
	 * 
	 * @param po
	 *            ,ͨ�ù�������
	 * @return WorkDefinition
	 */
	public WorkDefinition makeGenericWorkDefinition(WorkDefinition po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,
				WorkDefinition.EDITOR_GENERIC_WORK_ROOT);
		po.setValue(WorkDefinition.F_WORK_TYPE,
				WorkDefinition.WORK_TYPE_GENERIC);
		return po;
	}

	/**
	 * �½���Ŀģ��
	 * 
	 * @param po
	 *            ,��Ŀģ��
	 * @return ProjectTemplate
	 */
	public ProjectTemplate makeProjectTemplate(ProjectTemplate po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					ProjectTemplate.class);
		}
		po.setValue(ProjectTemplate.F_ORGANIZATION_ID, get_id());

		return po;
	}

	/**
	 * �½���׼��������
	 * 
	 * @param po
	 *            ,��������
	 * @return
	 */
	public WorkDefinition makeStandardWorkDefinition(WorkDefinition po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,
				WorkDefinition.EDITOR_STANDLONE_WORK_ROOT);
		po.setValue(WorkDefinition.F_WORK_TYPE,
				WorkDefinition.WORK_TYPE_STANDLONE);
		return po;

	}

	/**
	 * �½��ĵ�ģ��
	 * 
	 * @param po
	 *            ,�ĵ�����
	 * @return
	 */
	public DocumentDefinition makeDocumentDefinition(DocumentDefinition po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					DocumentDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		return po;
	}

	/**
	 * �½�����֯
	 * 
	 * @param po
	 *            ,��֯
	 * @return Organization
	 */
	public Organization makeChildOrganization(Organization po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					Organization.class);
		}
		po.setValue(Organization.F_PARENT_ID, get_id());
		return po;
	}

	/**
	 * �½���ɫ
	 * 
	 * @param po
	 *            ,��ɫ
	 * @return ,Role
	 */
	public Role makeRole(Role po) {
		if (po == null) {
			po = ModelService
					.createModelObject(new BasicDBObject(), Role.class);
		}
		po.setValue(Role.F_ORGANIZATION_ID, get_id());
		return po;

	}

	/**
	 * ��ȡ�����Լ��¼����еĽ�ɫ
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getRolesIteration() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		iterateSearchRolesRoles(this, result);
		return result;
	}

	/**
	 * ��ѯ��֯������֯�����н�ɫ
	 * 
	 * @param org
	 *            ,��֯
	 * @param dataItems
	 *            ,��֯�µ����н�ɫ
	 */
	private void iterateSearchRolesRoles(Organization org,
			List<PrimaryObject> dataItems) {
		dataItems.addAll(org.getRoles());
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (PrimaryObject primaryObject : children) {
			iterateSearchRolesRoles((Organization) primaryObject, dataItems);
		}
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��֯";
	}

	/**
	 * ����������Ŀģ�浽����֯
	 * 
	 * @param selectList
	 * @param context
	 * @throws Exception
	 */
	public void doCopyProjectTemplates(List<ProjectTemplate> selectList,
			IContext context) throws Exception {
		AccountInfo account = context.getAccountInfo();
		BasicDBObject accountInfo = new BasicDBObject().append("userid",
				account.getUserId()).append("username", account.getUserName());
		List<DBObject> projectTemplateList = new ArrayList<DBObject>();
		List<DBObject> budgetItemList = new ArrayList<DBObject>();
		List<DBObject> roleDefinitionList = new ArrayList<DBObject>();
		List<DBObject> workDefinitionList = new ArrayList<DBObject>();
		List<DBObject> deliverableDefinitionList = new ArrayList<DBObject>();
		List<DBObject> workConnectionList = new ArrayList<DBObject>();

		for (ProjectTemplate projectTemplate : selectList) {
			ObjectId projectTemplate_id = new ObjectId();
			ObjectId budgetItem_id = new ObjectId();
			ObjectId roleDefinition_id = new ObjectId();
			ObjectId workDefinition_id = new ObjectId();
			ObjectId deliverableDefinition_id = new ObjectId();
			ObjectId workConnection_id = new ObjectId();

			// 1.������Ŀģ��
			DBObject projectTemplateData = new BasicDBObject();
			projectTemplateData.put(ProjectTemplate.F_ACTIVATED,
					projectTemplate.getValue(ProjectTemplate.F_ACTIVATED));
			projectTemplateData.put(ProjectTemplate.F_BUDGET_ID, budgetItem_id);
			projectTemplateData.put(ProjectTemplate.F_ORGANIZATION_ID,
					this.get_id());
			projectTemplateData
					.put(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET,
							projectTemplate
									.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET));
			projectTemplateData
					.put(ProjectTemplate.F_PROJECTTYPE_OPTION_SET,
							projectTemplate
									.getValue(ProjectTemplate.F_PROJECTTYPE_OPTION_SET));
			projectTemplateData.put(ProjectTemplate.F_STANDARD_OPTION_SET,
					projectTemplate
							.getValue(ProjectTemplate.F_STANDARD_OPTION_SET));
			projectTemplateData.put(ProjectTemplate.F_WF_CHANGE,
					projectTemplate.getValue(ProjectTemplate.F_WF_CHANGE));
			projectTemplateData.put(ProjectTemplate.F_WF_CHANGE_ACTIVATED,
					projectTemplate
							.getValue(ProjectTemplate.F_WF_CHANGE_ACTIVATED));
			projectTemplateData.put(ProjectTemplate.F_WF_CHANGE_ASSIGNMENT,
					projectTemplate
							.getValue(ProjectTemplate.F_WF_CHANGE_ASSIGNMENT));
			projectTemplateData.put(ProjectTemplate.F_WF_COMMIT,
					projectTemplate.getValue(ProjectTemplate.F_WF_COMMIT));
			projectTemplateData.put(ProjectTemplate.F_WF_COMMIT_ACTIVATED,
					projectTemplate
							.getValue(ProjectTemplate.F_WF_COMMIT_ACTIVATED));
			projectTemplateData.put(ProjectTemplate.F_WF_COMMIT_ASSIGNMENT,
					projectTemplate
							.getValue(ProjectTemplate.F_WF_COMMIT_ASSIGNMENT));
			projectTemplateData.put(ProjectTemplate.F_WORK_DEFINITON_ID,
					workDefinition_id);
			projectTemplateData.put(ProjectTemplate.F__CACCOUNT, accountInfo);
			projectTemplateData.put(ProjectTemplate.F__CDATE, new Date());
			projectTemplateData.put(ProjectTemplate.F__EDITOR,
					projectTemplate.getValue(ProjectTemplate.F__EDITOR));
			projectTemplateData.put(ProjectTemplate.F__ID, projectTemplate_id);
			projectTemplateData.put(ProjectTemplate.F__VID, 0);
			projectTemplateData.put(ProjectTemplate.F_DESC,
					projectTemplate.getValue(ProjectTemplate.F_DESC));
			projectTemplateData.put(ProjectTemplate.F_DESC_EN,
					projectTemplate.getValue(ProjectTemplate.F_DESC_EN));
			projectTemplateList.add(projectTemplateData);

			// 2.����Ԥ��
			DBObject budgetItemData = new BasicDBObject();
			List<PrimaryObject> budgetItems = projectTemplate.getBudgetItems();
			for (PrimaryObject po : budgetItems) {
				if (po instanceof BudgetItem) {
					BudgetItem budgetItem = (BudgetItem) po;
					budgetItemData.put(BudgetItem.F_CHILDREN,
							budgetItem.getValue(BudgetItem.F_CHILDREN));
					budgetItemData.put(BudgetItem.F_ISDEFAULT,
							budgetItem.getValue(BudgetItem.F_ISDEFAULT));
					budgetItemData.put(BudgetItem.F_PROJECTTEMPLATE_ID,
							projectTemplate_id);

					budgetItemData.put(BudgetItem.F__CACCOUNT, accountInfo);
					budgetItemData.put(BudgetItem.F__CDATE, new Date());
					budgetItemData.put(BudgetItem.F__EDITOR,
							budgetItem.getValue(BudgetItem.F__EDITOR));
					budgetItemData.put(BudgetItem.F__ID, budgetItem_id);
					budgetItemData.put(BudgetItem.F__VID, 0);
					budgetItemData.put(BudgetItem.F_DESC,
							budgetItem.getValue(BudgetItem.F_DESC));
					budgetItemData.put(BudgetItem.F_DESC_EN,
							budgetItem.getValue(BudgetItem.F_DESC_EN));
					budgetItemList.add(budgetItemData);
					budgetItem_id = new ObjectId();
				}
			}
			// 3.���ƽ�ɫ
			DBObject roleDefinitionData = new BasicDBObject();
			List<PrimaryObject> roleDefinitions = projectTemplate
					.getRoleDefinitions();
			for (PrimaryObject po : roleDefinitions) {
				if (po instanceof RoleDefinition) {
					RoleDefinition roleDefinition = (RoleDefinition) po;
					roleDefinitionData
							.put(RoleDefinition.F_ORGANIZATION_ROLE_ID,
									roleDefinition
											.getValue(RoleDefinition.F_ORGANIZATION_ROLE_ID));
					roleDefinitionData.put(RoleDefinition.F_ROLE_NUMBER,
							roleDefinition
									.getValue(RoleDefinition.F_ROLE_NUMBER));
					roleDefinitionData.put(
							RoleDefinition.F_PROJECT_TEMPLATE_ID,
							projectTemplate_id);

					roleDefinitionData.put(RoleDefinition.F__CACCOUNT,
							accountInfo);
					roleDefinitionData.put(RoleDefinition.F__CDATE, new Date());
					roleDefinitionData.put(RoleDefinition.F__EDITOR,
							roleDefinition.getValue(RoleDefinition.F__EDITOR));
					roleDefinitionData.put(RoleDefinition.F__ID,
							roleDefinition_id);
					roleDefinitionData.put(RoleDefinition.F__VID, 0);
					roleDefinitionData.put(RoleDefinition.F_DESC,
							roleDefinition.getValue(RoleDefinition.F_DESC));
					roleDefinitionData.put(RoleDefinition.F_DESC_EN,
							roleDefinition.getValue(RoleDefinition.F_DESC_EN));
					roleDefinitionList.add(roleDefinitionData);
					roleDefinition_id = new ObjectId();
				}
			}

			// TODO ��Ҫ�Ѹ���������������
			// 4.����WBS
			HashMap<ObjectId, ObjectId> workDefinitionSet = new HashMap<ObjectId, ObjectId>();
			DBObject workDefinitionData = new BasicDBObject();
			List<PrimaryObject> workDefinitions = projectTemplate
					.getWorkDefinitions();
			for (PrimaryObject po : workDefinitions) {
				if (po instanceof WorkDefinition) {
					workDefinition_id = new ObjectId();
					WorkDefinition workDefinition = (WorkDefinition) po;
					workDefinitionData
							.put(WorkDefinition.F_ACTIVATED, workDefinition
									.getValue(WorkDefinition.F_ACTIVATED));
					workDefinitionData
							.put(WorkDefinition.F_WORK_TYPE, workDefinition
									.getValue(WorkDefinition.F_WORK_TYPE));
					workDefinitionData
							.put(WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID,
									workDefinition
											.getValue(WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID));
					workDefinitionData
							.put(WorkDefinition.F_CHARGER_ROLE_ID,
									workDefinition
											.getValue(WorkDefinition.F_CHARGER_ROLE_ID));
					workDefinitionData
							.put(WorkDefinition.F_MILESTONE, workDefinition
									.getValue(WorkDefinition.F_MILESTONE));
					workDefinitionData.put(WorkDefinition.F_OPTION_FILTERS,
							workDefinition
									.getValue(WorkDefinition.F_OPTION_FILTERS));
					//TODO �ϼ���������_id�Ļ�ȡ
					workDefinitionData.put(WorkDefinition.F_PARENT_ID, "");
					workDefinitionData
							.put(WorkDefinition.F_PARTICIPATE_ROLE_SET,
									workDefinition
											.getValue(WorkDefinition.F_PARTICIPATE_ROLE_SET));
					workDefinitionData.put(
							WorkDefinition.F_PROJECT_TEMPLATE_ID,
							projectTemplate_id);
					workDefinitionData.put(WorkDefinition.F_ROOT_ID,
							workDefinition.getValue(WorkDefinition.F_ROOT_ID));
					workDefinitionData.put(WorkDefinition.F_SEQ,
							workDefinition.getValue(WorkDefinition.F_SEQ));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_CHILDREN_FINISHED,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_CHILDREN_FINISHED));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_AUTOSTART_WHEN_PARENT_START,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_AUTOSTART_WHEN_PARENT_START));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_CAN_ADD_DELIVERABLES,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_CAN_ADD_DELIVERABLES));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_CAN_BREAKDOWN,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_CAN_BREAKDOWN));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_CAN_MODIFY_PLANWORKS,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_CAN_MODIFY_PLANWORKS));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_PROJECTCHANGE_MANDORY,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_PROJECTCHANGE_MANDORY));
					workDefinitionData
							.put(WorkDefinition.F_SETTING_WORKCHANGE_MANDORY,
									workDefinition
											.getValue(WorkDefinition.F_SETTING_WORKCHANGE_MANDORY));
					workDefinitionData.put(WorkDefinition.F_STANDARD_WORKS,
							workDefinition
									.getValue(WorkDefinition.F_STANDARD_WORKS));
					workDefinitionData
							.put(WorkDefinition.F_WF_CHANGE, workDefinition
									.getValue(WorkDefinition.F_WF_CHANGE));
					workDefinitionData
							.put(WorkDefinition.F_WF_CHANGE_ACTIVATED,
									workDefinition
											.getValue(WorkDefinition.F_WF_CHANGE_ACTIVATED));
					workDefinitionData
							.put(WorkDefinition.F_WF_CHANGE_ASSIGNMENT,
									workDefinition
											.getValue(WorkDefinition.F_WF_CHANGE_ASSIGNMENT));
					workDefinitionData.put(WorkDefinition.F_WF_EXECUTE,
							workDefinition
									.getValue(WorkDefinition.F_WF_EXECUTE));
					workDefinitionData
							.put(WorkDefinition.F_WF_EXECUTE_ACTIVATED,
									workDefinition
											.getValue(WorkDefinition.F_WF_EXECUTE_ACTIVATED));
					workDefinitionData
							.put(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT,
									workDefinition
											.getValue(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT));

					workDefinitionData.put(WorkDefinition.F__CACCOUNT,
							accountInfo);
					workDefinitionData.put(WorkDefinition.F__CDATE, new Date());
					workDefinitionData.put(WorkDefinition.F__EDITOR,
							workDefinition.getValue(WorkDefinition.F__EDITOR));
					workDefinitionData.put(WorkDefinition.F__ID,
							workDefinition_id);
					workDefinitionData.put(WorkDefinition.F__VID, 0);
					workDefinitionData.put(WorkDefinition.F_DESC,
							workDefinition.getValue(WorkDefinition.F_DESC));
					workDefinitionData.put(WorkDefinition.F_DESC_EN,
							workDefinition.getValue(WorkDefinition.F_DESC_EN));
					workDefinitionList.add(workDefinitionData);
					workDefinitionSet.put(workDefinition.get_id(),
							workDefinition_id);
				}
			}

			// 5.���ƽ�����
			DBObject deliverableDefinitionData = new BasicDBObject();
			List<PrimaryObject> deliverableDefinitions = projectTemplate
					.getDeliverableDefinitions();
			for (PrimaryObject po : deliverableDefinitions) {
				if (po instanceof DeliverableDefinition) {
					DeliverableDefinition deliverableDefinition = (DeliverableDefinition) po;
					// TODO ȱ��documentd_id�ĸ���
					deliverableDefinitionData
							.put(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
									deliverableDefinition
											.getValue(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID));
					deliverableDefinitionData.put(
							DeliverableDefinition.F_PROJECTTEMPLATE_ID,
							projectTemplate);
					deliverableDefinitionData
							.put(DeliverableDefinition.F_WORK_DEFINITION_ID,
									deliverableDefinition
											.getValue(DeliverableDefinition.F_WORK_DEFINITION_ID));
					deliverableDefinitionData
							.put(DeliverableDefinition.F_OPTION_FILTERS,
									deliverableDefinition
											.getValue(DeliverableDefinition.F_OPTION_FILTERS));

					deliverableDefinitionData.put(
							DeliverableDefinition.F__CACCOUNT, accountInfo);
					deliverableDefinitionData.put(
							DeliverableDefinition.F__CDATE, new Date());
					deliverableDefinitionData.put(
							DeliverableDefinition.F__EDITOR,
							deliverableDefinition
									.getValue(DeliverableDefinition.F__EDITOR));
					deliverableDefinitionData.put(DeliverableDefinition.F__ID,
							deliverableDefinition_id);
					deliverableDefinitionData.put(DeliverableDefinition.F__VID,
							0);
					deliverableDefinitionData.put(DeliverableDefinition.F_DESC,
							deliverableDefinition
									.getValue(DeliverableDefinition.F_DESC));
					deliverableDefinitionData.put(
							DeliverableDefinition.F_DESC_EN,
							deliverableDefinition
									.getValue(DeliverableDefinition.F_DESC_EN));
					deliverableDefinitionList.add(deliverableDefinitionData);
					deliverableDefinition_id = new ObjectId();
				}
			}

			// 6.����ǰ���ù�ϵ
			DBObject workConnectionData = new BasicDBObject();
			List<PrimaryObject> workConnections = projectTemplate
					.getWorkConnections();
			for (PrimaryObject po : workConnections) {
				if (po instanceof WorkConnection) {
					WorkConnection workConnection = (WorkConnection) po;
					workConnectionData.put(WorkConnection.F_CONNECTIONTYPE,
							workConnection
									.getValue(WorkConnection.F_CONNECTIONTYPE));
					workConnectionData.put(WorkConnection.F_END1_ID,
							workDefinitionSet.get(workConnection
									.getValue(WorkConnection.F_END1_ID)));
					workConnectionData.put(WorkConnection.F_END2_ID,
							workDefinitionSet.get(workConnection
									.getValue(WorkConnection.F_END2_ID)));
					workConnectionData.put(WorkConnection.F_INTERVAL,
							workConnection.getValue(WorkConnection.F_INTERVAL));
					workConnectionData.put(WorkConnection.F_OPERATOR,
							workConnection.getValue(WorkConnection.F_OPERATOR));
					workConnectionData.put(WorkConnection.F_PROJECT_ID,
							projectTemplate_id);
					workConnectionData.put(WorkConnection.F_UNIT,
							workConnection.getValue(WorkConnection.F_UNIT));

					workConnectionData.put(WorkConnection.F__CACCOUNT,
							accountInfo);
					workConnectionData.put(WorkConnection.F__CDATE, new Date());
					workConnectionData.put(WorkConnection.F__EDITOR,
							workConnection.getValue(WorkConnection.F__EDITOR));
					workConnectionData.put(WorkConnection.F__ID,
							workConnection_id);
					workConnectionData.put(WorkConnection.F__VID, 0);
					workConnectionData.put(WorkConnection.F_DESC,
							workConnection.getValue(WorkConnection.F_DESC));
					workConnectionData.put(WorkConnection.F_DESC_EN,
							workConnection.getValue(WorkConnection.F_DESC_EN));
					workConnectionList.add(workConnectionData);
					workConnection_id = new ObjectId();
				}
			}

		}
		String error = null;
		DBCollection projectTemplateCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		DBCollection budgetItemCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_BUDGET_ITEM);
		DBCollection roleDefinitionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
		DBCollection workDefinitionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		DBCollection deliverableDefinitionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_DELIEVERABLE_DEFINITION);
		DBCollection workConnectionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_WORK_CONNECTION);

		WriteResult projectTemplateWriteResult = projectTemplateCol
				.insert(projectTemplateList);
		error = projectTemplateWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}
		WriteResult budgetItemWriteResult = budgetItemCol
				.insert(budgetItemList);
		error = budgetItemWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}
		WriteResult roleDefinitionWriteResult = roleDefinitionCol
				.insert(roleDefinitionList);
		error = roleDefinitionWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}
		WriteResult workDefinitionWriteResult = workDefinitionCol
				.insert(workDefinitionList);
		error = workDefinitionWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}
		WriteResult deliverableDefinitionWriteResult = deliverableDefinitionCol
				.insert(deliverableDefinitionList);
		error = deliverableDefinitionWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}
		WriteResult workConnectionWriteResult = workConnectionCol
				.insert(workConnectionList);
		error = workConnectionWriteResult.getError();
		if (error != null) {
			throw new Exception(error);
		}

		// д��־
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "������Ŀģ��",
				new Date(), "��֯��" + this + "\n��Ŀģ��" + selectList.toString(),
				IModelConstants.DB);

	}

}
