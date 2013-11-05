package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.drools.KnowledgeBase;
import org.drools.definition.process.Process;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
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
import com.sg.bpm.service.BPM;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
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
	 * ��֯�����̿⣬�����̿����Ƶ��б�
	 */
	public static final String F_KBASE = "kbase";

	/**
	 * ��֯���룬�����趨����֯�йصı���
	 */
	public static final String F_CODE = "code";

	/**
	 * ��˾���룬��SAP��Ӧ�Ĺ�˾����
	 */
	public static final String F_COMPANY_CODE = "companycode";
	
	/**
	 * PDM ͼ�ĵ���������
	 */
	public static final String F_PDM_DOC_DRAWING_COMTAINER = "pdmcontainer";
	
	/**
	 * PDM �㲿����������
	 */
	private static final String F_PDM_PART_COMTAINER = "pdmpartcontainer";


	/**
	 * ��֯����
	 */
	public static final String ORG_TYPE_COMPANY = "����";

	public static final String ORG_TYPE_BUSINESS_UNIT = "��ҵ��";

	public static final String ORG_TYPE_DEPARTMENT = "����";

	public static final String ORG_TYPE_TEAM = "�Ŷ�";

	public static final String F_FILEBASE = "filebase";


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

		String orgType = getOrganizationType();
		if (ORG_TYPE_COMPANY.equals(orgType)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_COMPANY_16);
		}
		if (ORG_TYPE_BUSINESS_UNIT.equals(orgType)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_BU_16);
		}
		if (ORG_TYPE_DEPARTMENT.equals(orgType)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_DEPTARTMENT_16);
		}
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

	@Override
	public void doInsert(IContext context) throws Exception {
		doSaveBefore();

		super.doInsert(context);

		doSaveAfter();
	}

	private void doSaveBefore() throws Exception {
		// ����������ҵ�����͵���֯����֯���������д
		String type = getOrganizationType();
		if (Utils.isNullOrEmpty(type)) {
			throw new Exception("��˾���Ͳ���Ϊ��"+this);
		}
		if (ORG_TYPE_COMPANY.equals(type)
				|| ORG_TYPE_BUSINESS_UNIT.equals(type)) {
			String companyCode = getCompanyCode();
			if (Utils.isNullOrEmpty(companyCode)) {
				throw new Exception("��ҵ����˾���͵���֯��Ҫ����\"��˾����\""+this);
			}
		}

		if (isFunctionDepartment()) {
			String code = getCode();
			if (Utils.isNullOrEmpty(code)) {
				throw new Exception("������Ŀ����ְ�ܵ���֯��Ҫ����\"����\""+this);
			}
		}
		
		if(isContainer()){
			String code = getFileBase();
			if (Utils.isNullOrEmpty(code)) {
				throw new Exception("�����ĵ���������֯��Ҫ����\"��������\""+this);
			}
		}

	}

	public String getFileBase() {
		return getStringValue(F_FILEBASE);
	}

	public String getCompanyCode() {
		return (String) getValue(F_COMPANY_CODE);
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		doSaveBefore();

		super.doUpdate(context);

		doSaveAfter();
	}

	protected void doSaveAfter() {
		/*
		 * �����ɫ�����³����SaveHandler����ֲ
		 */
		// �����֯�Ǿ�����Ŀ����ְ�ܵ���֯����Ҫ�Զ������Ŀ����Ա��ɫ
		if (Boolean.TRUE.equals(isFunctionDepartment())) {
			/*
			 * // �ж��Ƿ�������role,�������������ӡ�����ж�ͨ������Ψһ��������������������������⡣ boolean
			 * hasRole = hasRole(Role.ROLE_PROJECT_ADMIN_ID); if (!hasRole) {
			 * doAddRole(Role.ROLE_PROJECT_ADMIN_ID,
			 * Role.ROLE_PROJECT_ADMIN_TEXT); }
			 * 
			 * 
			 * hasRole = hasRole(Role.ROLE_BUSINESS_ADMIN_ID); if (!hasRole) {
			 * doAddRole(Role.ROLE_BUSINESS_ADMIN_ID,
			 * Role.ROLE_BUSINESS_ADMIN_TEXT); }
			 */
			// �Ѿ�ͨ�������������жϣ����ע�����ϳ���
			try {
				doAddRole(Role.ROLE_PROJECT_ADMIN_ID,
						Role.ROLE_PROJECT_ADMIN_TEXT);
			} catch (Exception e) {
			}
			try {
				doAddRole(Role.ROLE_BUSINESS_ADMIN_ID,
						Role.ROLE_BUSINESS_ADMIN_TEXT);
			} catch (Exception e) {
			}
		}

		// �����֯�Ǿ����ĵ���������֯����Ҫ�Զ�����ĵ������ߺ��ĵ�����Ա�Ľ�ɫ
		if (Boolean.TRUE.equals(isContainer())) {
			/*
			 * // �ж��Ƿ�������role boolean hasRole =
			 * hasRole(Role.ROLE_VAULT_ADMIN_ID); if (!hasRole) {
			 * doAddRole(Role.ROLE_VAULT_ADMIN_ID, Role.ROLE_VALUT_ADMIN_TEXT);
			 * } hasRole = hasRole(Role.ROLE_VAULT_GUEST_ID); if (!hasRole) {
			 * doAddRole(Role.ROLE_VAULT_GUEST_ID, Role.ROLE_VAULT_GUEST_TEXT);
			 * }
			 */
			// �Ѿ�ͨ�������������жϣ����ע�����ϳ���
			try {

				doAddRole(Role.ROLE_VAULT_ADMIN_ID, Role.ROLE_VALUT_ADMIN_TEXT);
			} catch (Exception e) {
			}
			try {
				doAddRole(Role.ROLE_VAULT_GUEST_ID, Role.ROLE_VAULT_GUEST_TEXT);
			} catch (Exception e) {
			}
		}

		// ���ӹ����߽�ɫ
		try {
			doAddRole(Role.ROLE_DEPT_MANAGER_ID, Role.ROLE_DEPT_MANAGER_TEXT);
		} catch (Exception e) {
		}

		try {
			// �������ҵ����֯��Ҫ�����������ɫ
			String type = getOrganizationType();
			if (ORG_TYPE_COMPANY.equals(type)
					|| ORG_TYPE_BUSINESS_UNIT.equals(type)) {
				doAddRole(Role.ROLE_FINANCIAL_MANAGER_ID,
						Role.ROLE_FINANCIAL_MANAGER_TEXT);
			}
		} catch (Exception e) {
		}
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
	 * ��ȡ��֯���µ��û�
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getUser() {
		return getRelationById(F__ID, User.F_ORGANIZATION_ID, User.class);
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
	 * ��ȡ��֯�°�����ĳ����ɫ
	 * 
	 * @param roleNumber
	 *            ,��ɫ���
	 * @param selectType
	 *            ,���ҷ�ʽ,int ����,-1Ϊ���²���,0Ϊ������,1Ϊ���ϲ���
	 * @return Role
	 */
	public Role getRole(String roleNumber, int selectType) {
		List<PrimaryObject> roleList = getRelationByCondition(Role.class,
				new BasicDBObject().append(Role.F_ROLE_NUMBER, roleNumber)
						.append(Role.F_ORGANIZATION_ID, get_id()));
		if (roleList != null && roleList.size() > 0) {
			return (Role) roleList.get(0);
		} else {
			switch (selectType) {
			case -1:
				List<PrimaryObject> childrenOrgs = getChildrenOrganization();
				if (childrenOrgs != null && childrenOrgs.size() > 0) {
					for (int i = 0; i < childrenOrgs.size(); i++) {
						Organization childrenOrg = (Organization) childrenOrgs
								.get(i);
						Role role = childrenOrg.getRole(roleNumber, selectType);
						if (role != null) {
							return role;
						}
					}
				}
				return null;
			case 1:
				Organization parentOrg = (Organization) getParentOrganization();
				if (parentOrg != null) {
					return parentOrg.getRole(roleNumber, selectType);
				}
				return null;
			default:
				return null;
			}
		}
	}

	/**
	 * �����֯��һ����ɫ
	 * 
	 * @param roleNumber
	 *            , ��ɫ���
	 * @param roleName
	 *            , ��ɫ����
	 * @return Role
	 * @throws Exception
	 */
	public Role doAddRole(String roleNumber, String roleName) throws Exception {
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE);
		BasicDBObject data = new BasicDBObject();
		data.put(Role.F__ID, new ObjectId());
		data.put(Role.F_ORGANIZATION_ID, get_id());
		data.put(Role.F_ROLE_NUMBER, roleNumber);
		data.put(Role.F_DESC, roleName);
		WriteResult wr = roleCollection.insert(data);
		checkWriteResult(wr);
		return ModelService.createModelObject(data, Role.class);
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
			if (parent.equals(this)) {
				return true;
			}
			parent = (Organization) parent.getParentOrganization();
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
	public void doCopyProjectTemplates(PrimaryObject selectList,
			IContext context) throws Exception {
		AccountInfo account = context.getAccountInfo();
		BasicDBObject accountInfo = new BasicDBObject().append("userid",
				account.getUserId()).append("username", account.getUserName());
		List<DBObject> projectTemplateList = new ArrayList<DBObject>();
		List<DBObject> budgetItemList = new ArrayList<DBObject>();
		List<DBObject> roleDefinitionList = new ArrayList<DBObject>();
		List<DBObject> workDefinitionList = new ArrayList<DBObject>();
		List<DBObject> deliverableDefinitionList = new ArrayList<DBObject>();
		List<DBObject> documentDefinitionList = new ArrayList<DBObject>();
		List<DBObject> workConnectionList = new ArrayList<DBObject>();

		ProjectTemplate projectTemplate = (ProjectTemplate) selectList;
		ObjectId projectTemplate_id = new ObjectId();
		ObjectId budgetItem_id = new ObjectId();
		ObjectId roleDefinition_id = new ObjectId();
		ObjectId wbsRoot_id = new ObjectId();
		ObjectId deliverableDefinition_id = new ObjectId();
		ObjectId workConnection_id = new ObjectId();

		// 1.������Ŀģ��
		DBObject projectTemplateData = new BasicDBObject();
		projectTemplateData.put(ProjectTemplate.F_ACTIVATED,
				projectTemplate.getValue(ProjectTemplate.F_ACTIVATED));
		projectTemplateData.put(ProjectTemplate.F_BUDGET_ID, budgetItem_id);
		projectTemplateData.put(ProjectTemplate.F_ORGANIZATION_ID,
				this.get_id());
		projectTemplateData.put(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET,
				projectTemplate
						.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET));
		projectTemplateData.put(ProjectTemplate.F_PROJECTTYPE_OPTION_SET,
				projectTemplate
						.getValue(ProjectTemplate.F_PROJECTTYPE_OPTION_SET));
		projectTemplateData
				.put(ProjectTemplate.F_STANDARD_OPTION_SET, projectTemplate
						.getValue(ProjectTemplate.F_STANDARD_OPTION_SET));
		projectTemplateData.put(ProjectTemplate.F_WF_CHANGE,
				projectTemplate.getValue(ProjectTemplate.F_WF_CHANGE));
		projectTemplateData
				.put(ProjectTemplate.F_WF_CHANGE_ACTIVATED, projectTemplate
						.getValue(ProjectTemplate.F_WF_CHANGE_ACTIVATED));
		projectTemplateData.put(ProjectTemplate.F_WF_CHANGE_ASSIGNMENT,
				projectTemplate
						.getValue(ProjectTemplate.F_WF_CHANGE_ASSIGNMENT));
		projectTemplateData.put(ProjectTemplate.F_WF_COMMIT,
				projectTemplate.getValue(ProjectTemplate.F_WF_COMMIT));
		projectTemplateData
				.put(ProjectTemplate.F_WF_COMMIT_ACTIVATED, projectTemplate
						.getValue(ProjectTemplate.F_WF_COMMIT_ACTIVATED));
		projectTemplateData.put(ProjectTemplate.F_WF_COMMIT_ASSIGNMENT,
				projectTemplate
						.getValue(ProjectTemplate.F_WF_COMMIT_ASSIGNMENT));
		projectTemplateData
				.put(ProjectTemplate.F_WORK_DEFINITON_ID, wbsRoot_id);
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
		List<PrimaryObject> budgetItems = projectTemplate.getBudgetItems();
		for (PrimaryObject po : budgetItems) {
			if (po instanceof BudgetItem) {
				DBObject budgetItemData = new BasicDBObject();
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
		HashMap<ObjectId, ObjectId> roleDefinitionSet = new HashMap<ObjectId, ObjectId>();
		List<PrimaryObject> roleDefinitions = projectTemplate
				.getRoleDefinitions();
		for (PrimaryObject po : roleDefinitions) {
			if (po instanceof RoleDefinition) {
				DBObject roleDefinitionData = new BasicDBObject();
				RoleDefinition roleDefinition = (RoleDefinition) po;
				roleDefinitionData
						.put(RoleDefinition.F_ORGANIZATION_ROLE_ID,
								roleDefinition
										.getValue(RoleDefinition.F_ORGANIZATION_ROLE_ID));
				roleDefinitionData.put(RoleDefinition.F_ROLE_NUMBER,
						roleDefinition.getValue(RoleDefinition.F_ROLE_NUMBER));
				roleDefinitionData.put(RoleDefinition.F_PROJECT_TEMPLATE_ID,
						projectTemplate_id);

				roleDefinitionData.put(RoleDefinition.F__CACCOUNT, accountInfo);
				roleDefinitionData.put(RoleDefinition.F__CDATE, new Date());
				roleDefinitionData.put(RoleDefinition.F__EDITOR,
						roleDefinition.getValue(RoleDefinition.F__EDITOR));
				roleDefinitionData.put(RoleDefinition.F__ID, roleDefinition_id);
				roleDefinitionData.put(RoleDefinition.F__VID, 0);
				roleDefinitionData.put(RoleDefinition.F_DESC,
						roleDefinition.getValue(RoleDefinition.F_DESC));
				roleDefinitionData.put(RoleDefinition.F_DESC_EN,
						roleDefinition.getValue(RoleDefinition.F_DESC_EN));
				roleDefinitionList.add(roleDefinitionData);
				roleDefinitionSet.put(roleDefinition.get_id(),
						roleDefinition_id);
				roleDefinition_id = new ObjectId();
			}
		}

		// 4.1.���Ƹ�����
		HashMap<ObjectId, ObjectId> workDefinitionSet = new HashMap<ObjectId, ObjectId>();
		DBObject wbsRootData = new BasicDBObject();
		WorkDefinition wbsRoot = projectTemplate.getWBSRoot();

		ObjectId rootAssignmentCharger_Roled_Id = roleDefinitionSet.get(wbsRoot
				.getValue(WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID));
		if (rootAssignmentCharger_Roled_Id == null) {
			wbsRootData.put(WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID,
					rootAssignmentCharger_Roled_Id);
		}
		ObjectId rootCharger_Roled_Id = roleDefinitionSet.get(wbsRoot
				.getValue(WorkDefinition.F_CHARGER_ROLE_ID));
		if (rootCharger_Roled_Id == null) {
			wbsRootData.put(WorkDefinition.F_CHARGER_ROLE_ID,
					rootCharger_Roled_Id);
		}
		List<PrimaryObject> oldRootParticipate_Roled_Set = wbsRoot
				.getParticipateRoles();
		if (oldRootParticipate_Roled_Set.size() > 0) {
			BasicBSONList newRootParticipate_Roled_Set = new BasicBSONList();
			for (PrimaryObject object : oldRootParticipate_Roled_Set) {
				// TODO �޷�ǿ��ת��
				RoleDefinition rootParticipate_Roled = (RoleDefinition) object;
				ObjectId rootParticipate_Roled_Id = roleDefinitionSet
						.get(rootParticipate_Roled.get_id());
				if (rootParticipate_Roled_Id != null) {
					BasicDBObject newRootParticipate_Roled = new BasicDBObject();

					newRootParticipate_Roled.put(
							RoleDefinition.F_ORGANIZATION_ROLE_ID,
							rootParticipate_Roled_Id);
					newRootParticipate_Roled.put(RoleDefinition.F_ROLE_NUMBER,
							rootParticipate_Roled
									.getValue(RoleDefinition.F_ROLE_NUMBER));
					newRootParticipate_Roled.put(
							RoleDefinition.F_PROJECT_TEMPLATE_ID,
							projectTemplate_id);

					newRootParticipate_Roled.put(RoleDefinition.F__CACCOUNT,
							accountInfo);
					newRootParticipate_Roled.put(RoleDefinition.F__CDATE,
							new Date());
					newRootParticipate_Roled.put(RoleDefinition.F__EDITOR,
							rootParticipate_Roled
									.getValue(RoleDefinition.F__EDITOR));
					newRootParticipate_Roled.put(RoleDefinition.F__ID,
							roleDefinition_id);
					newRootParticipate_Roled.put(RoleDefinition.F__VID, 0);
					newRootParticipate_Roled.put(RoleDefinition.F_DESC,
							rootParticipate_Roled
									.getValue(RoleDefinition.F_DESC));
					newRootParticipate_Roled.put(RoleDefinition.F_DESC_EN,
							rootParticipate_Roled
									.getValue(RoleDefinition.F_DESC_EN));
					newRootParticipate_Roled_Set.add(newRootParticipate_Roled);
				}
			}

			if (newRootParticipate_Roled_Set.size() > 0) {
				wbsRootData.put(WorkDefinition.F_PARTICIPATE_ROLE_SET,
						newRootParticipate_Roled_Set);
			}
		}

		wbsRootData.put(WorkDefinition.F_ACTIVATED,
				wbsRoot.getValue(WorkDefinition.F_ACTIVATED));
		wbsRootData.put(WorkDefinition.F_WORK_TYPE,
				wbsRoot.getValue(WorkDefinition.F_WORK_TYPE));
		wbsRootData.put(WorkDefinition.F_MILESTONE,
				wbsRoot.getValue(WorkDefinition.F_MILESTONE));
		wbsRootData.put(WorkDefinition.F_OPTION_FILTERS,
				wbsRoot.getValue(WorkDefinition.F_OPTION_FILTERS));
		wbsRootData.put(WorkDefinition.F_PROJECT_TEMPLATE_ID,
				projectTemplate_id);
		wbsRootData.put(WorkDefinition.F_ROOT_ID, wbsRoot_id);
		wbsRootData.put(WorkDefinition.F_SEQ,
				wbsRoot.getValue(WorkDefinition.F_SEQ));
		wbsRootData
				.put(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
						wbsRoot.getValue(WorkDefinition.F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH));
		wbsRootData
				.put(WorkDefinition.F_SETTING_AUTOSTART_WHEN_PARENT_START,
						wbsRoot.getValue(WorkDefinition.F_SETTING_AUTOSTART_WHEN_PARENT_START));
		wbsRootData
				.put(WorkDefinition.F_SETTING_CAN_ADD_DELIVERABLES,
						wbsRoot.getValue(WorkDefinition.F_SETTING_CAN_ADD_DELIVERABLES));
		wbsRootData.put(WorkDefinition.F_SETTING_CAN_BREAKDOWN,
				wbsRoot.getValue(WorkDefinition.F_SETTING_CAN_BREAKDOWN));
		wbsRootData
				.put(WorkDefinition.F_SETTING_CAN_MODIFY_PLANWORKS,
						wbsRoot.getValue(WorkDefinition.F_SETTING_CAN_MODIFY_PLANWORKS));
		wbsRootData
				.put(WorkDefinition.F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
						wbsRoot.getValue(WorkDefinition.F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH));
		wbsRootData.put(WorkDefinition.F_SETTING_PROJECTCHANGE_MANDORY, wbsRoot
				.getValue(WorkDefinition.F_SETTING_PROJECTCHANGE_MANDORY));
		wbsRootData.put(WorkDefinition.F_SETTING_WORKCHANGE_MANDORY,
				wbsRoot.getValue(WorkDefinition.F_SETTING_WORKCHANGE_MANDORY));
		wbsRootData.put(WorkDefinition.F_STANDARD_WORKS,
				wbsRoot.getValue(WorkDefinition.F_STANDARD_WORKS));
		wbsRootData.put(WorkDefinition.F_WF_CHANGE,
				wbsRoot.getValue(WorkDefinition.F_WF_CHANGE));
		wbsRootData.put(WorkDefinition.F_WF_CHANGE_ACTIVATED,
				wbsRoot.getValue(WorkDefinition.F_WF_CHANGE_ACTIVATED));
		wbsRootData.put(WorkDefinition.F_WF_CHANGE_ASSIGNMENT,
				wbsRoot.getValue(WorkDefinition.F_WF_CHANGE_ASSIGNMENT));
		wbsRootData.put(WorkDefinition.F_WF_EXECUTE,
				wbsRoot.getValue(WorkDefinition.F_WF_EXECUTE));
		wbsRootData.put(WorkDefinition.F_WF_EXECUTE_ACTIVATED,
				wbsRoot.getValue(WorkDefinition.F_WF_EXECUTE_ACTIVATED));
		wbsRootData.put(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT,
				wbsRoot.getValue(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT));

		wbsRootData.put(WorkDefinition.F__CACCOUNT, accountInfo);
		wbsRootData.put(WorkDefinition.F__CDATE, new Date());
		wbsRootData.put(WorkDefinition.F__EDITOR,
				wbsRoot.getValue(WorkDefinition.F__EDITOR));
		wbsRootData.put(WorkDefinition.F__ID, wbsRoot_id);
		wbsRootData.put(WorkDefinition.F__VID, 0);
		wbsRootData.put(WorkDefinition.F_DESC,
				wbsRoot.getValue(WorkDefinition.F_DESC));
		wbsRootData.put(WorkDefinition.F_DESC_EN,
				wbsRoot.getValue(WorkDefinition.F_DESC_EN));
		workDefinitionList.add(wbsRootData);
		workDefinitionSet.put(wbsRoot.get_id(), wbsRoot_id);

		// 4.2.���Ƴ����������WBS
		List<PrimaryObject> workDefinitions = projectTemplate
				.getWorkDefinitions();
		for (PrimaryObject po : workDefinitions) {
			if (po instanceof WorkDefinition) {
				DBObject workDefinitionData = new BasicDBObject();
				WorkDefinition workDefinition = (WorkDefinition) po;
				ObjectId newParent_Id = null;

				ObjectId parent_Id = (ObjectId) workDefinition
						.getValue(WorkDefinition.F_PARENT_ID);
				newParent_Id = workDefinitionSet.get(parent_Id);
				if (newParent_Id == null) {
					newParent_Id = new ObjectId();
					workDefinitionSet.put(parent_Id, newParent_Id);
				}
				ObjectId _id = null;
				_id = workDefinition.get_id();
				ObjectId workDefinition_id = workDefinitionSet.get(_id);
				if (workDefinition_id == null) {
					workDefinition_id = new ObjectId();
					workDefinitionSet.put(_id, workDefinition_id);
				}
				ObjectId assignmentCharger_Roled_Id = roleDefinitionSet
						.get(wbsRoot
								.getValue(WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID));
				if (assignmentCharger_Roled_Id == null) {
					workDefinitionData.put(
							WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID,
							assignmentCharger_Roled_Id);
				}
				ObjectId charger_Roled_Id = roleDefinitionSet.get(wbsRoot
						.getValue(WorkDefinition.F_CHARGER_ROLE_ID));
				if (charger_Roled_Id == null) {
					workDefinitionData.put(WorkDefinition.F_CHARGER_ROLE_ID,
							charger_Roled_Id);
				}

				List<PrimaryObject> oldParticipate_Roled_Set = workDefinition
						.getParticipateRoles();
				if (oldParticipate_Roled_Set.size() > 0) {
					BasicBSONList newParticipate_Roled_Set = new BasicBSONList();
					for (PrimaryObject objects : oldParticipate_Roled_Set) {
						// TODO �޷�ǿ��ת��
						RoleDefinition participate_Roled = (RoleDefinition) objects;
						ObjectId participate_Roled_Id = roleDefinitionSet
								.get(participate_Roled.get_id());
						if (participate_Roled_Id != null) {
							BasicDBObject newParticipate_Roled = new BasicDBObject();

							newParticipate_Roled.put(
									RoleDefinition.F_ORGANIZATION_ROLE_ID,
									participate_Roled_Id);
							newParticipate_Roled
									.put(RoleDefinition.F_ROLE_NUMBER,
											participate_Roled
													.getValue(RoleDefinition.F_ROLE_NUMBER));
							newParticipate_Roled.put(
									RoleDefinition.F_PROJECT_TEMPLATE_ID,
									projectTemplate_id);

							newParticipate_Roled.put(
									RoleDefinition.F__CACCOUNT, accountInfo);
							newParticipate_Roled.put(RoleDefinition.F__CDATE,
									new Date());
							newParticipate_Roled
									.put(RoleDefinition.F__EDITOR,
											participate_Roled
													.getValue(RoleDefinition.F__EDITOR));
							newParticipate_Roled.put(RoleDefinition.F__ID,
									roleDefinition_id);
							newParticipate_Roled.put(RoleDefinition.F__VID, 0);
							newParticipate_Roled.put(RoleDefinition.F_DESC,
									participate_Roled
											.getValue(RoleDefinition.F_DESC));
							newParticipate_Roled
									.put(RoleDefinition.F_DESC_EN,
											participate_Roled
													.getValue(RoleDefinition.F_DESC_EN));
							newParticipate_Roled_Set.add(newParticipate_Roled);
						}
					}

					if (newParticipate_Roled_Set.size() > 0) {
						workDefinitionData.put(
								WorkDefinition.F_PARTICIPATE_ROLE_SET,
								newParticipate_Roled_Set);
					}
				}

				workDefinitionData.put(WorkDefinition.F_ACTIVATED,
						workDefinition.getValue(WorkDefinition.F_ACTIVATED));
				workDefinitionData.put(WorkDefinition.F_WORK_TYPE,
						workDefinition.getValue(WorkDefinition.F_WORK_TYPE));

				workDefinitionData.put(WorkDefinition.F_MILESTONE,
						workDefinition.getValue(WorkDefinition.F_MILESTONE));
				workDefinitionData.put(WorkDefinition.F_OPTION_FILTERS,
						workDefinition
								.getValue(WorkDefinition.F_OPTION_FILTERS));
				workDefinitionData
						.put(WorkDefinition.F_PARENT_ID, newParent_Id);
				workDefinitionData.put(WorkDefinition.F_PROJECT_TEMPLATE_ID,
						projectTemplate_id);
				workDefinitionData.put(WorkDefinition.F_ROOT_ID, wbsRoot_id);
				workDefinitionData.put(WorkDefinition.F_SEQ,
						workDefinition.getValue(WorkDefinition.F_SEQ));

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
				workDefinitionData.put(WorkDefinition.F_WF_CHANGE,
						workDefinition.getValue(WorkDefinition.F_WF_CHANGE));
				workDefinitionData
						.put(WorkDefinition.F_WF_CHANGE_ACTIVATED,
								workDefinition
										.getValue(WorkDefinition.F_WF_CHANGE_ACTIVATED));
				workDefinitionData
						.put(WorkDefinition.F_WF_CHANGE_ASSIGNMENT,
								workDefinition
										.getValue(WorkDefinition.F_WF_CHANGE_ASSIGNMENT));
				workDefinitionData.put(WorkDefinition.F_WF_EXECUTE,
						workDefinition.getValue(WorkDefinition.F_WF_EXECUTE));
				workDefinitionData
						.put(WorkDefinition.F_WF_EXECUTE_ACTIVATED,
								workDefinition
										.getValue(WorkDefinition.F_WF_EXECUTE_ACTIVATED));
				workDefinitionData
						.put(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT,
								workDefinition
										.getValue(WorkDefinition.F_WF_EXECUTE_ASSIGNMENT));

				workDefinitionData.put(WorkDefinition.F__CACCOUNT, accountInfo);
				workDefinitionData.put(WorkDefinition.F__CDATE, new Date());
				workDefinitionData.put(WorkDefinition.F__EDITOR,
						workDefinition.getValue(WorkDefinition.F__EDITOR));
				workDefinitionData.put(WorkDefinition.F__ID, workDefinition_id);
				workDefinitionData.put(WorkDefinition.F__VID, 0);
				workDefinitionData.put(WorkDefinition.F_DESC,
						workDefinition.getValue(WorkDefinition.F_DESC));
				workDefinitionData.put(WorkDefinition.F_DESC_EN,
						workDefinition.getValue(WorkDefinition.F_DESC_EN));
				workDefinitionList.add(workDefinitionData);
			}
		}

		// 5.���ƽ�����ͽ������ĵ�
		HashMap<ObjectId, ObjectId> documentDefinitionSet = new HashMap<ObjectId, ObjectId>();
		List<PrimaryObject> deliverableDefinitions = projectTemplate
				.getDeliverableDefinitions();
		for (PrimaryObject po : deliverableDefinitions) {
			if (po instanceof DeliverableDefinition) {
				DBObject deliverableDefinitionData = new BasicDBObject();
				DeliverableDefinition deliverableDefinition = (DeliverableDefinition) po;
				// ���ƽ������ĵ�
				DocumentDefinition documentDefinition = deliverableDefinition
						.getDocumentDefinition();
				if (documentDefinition != null) {
					ObjectId _id = documentDefinition.get_id();
					ObjectId documentDefinition_id = documentDefinitionSet
							.get(_id);
					if (documentDefinition_id == null) {
						documentDefinition_id = new ObjectId();
						documentDefinitionSet.put(_id, documentDefinition_id);
						DBObject documentDefinitionData = new BasicDBObject();
						documentDefinitionData
								.put(DocumentDefinition.F_ATTACHMENT_CANNOT_EMPTY,
										documentDefinition
												.getValue(DocumentDefinition.F_ATTACHMENT_CANNOT_EMPTY));
						documentDefinitionData
								.put(DocumentDefinition.F_DESCRIPTION,
										documentDefinition
												.getValue(DocumentDefinition.F_DESCRIPTION));
						documentDefinitionData
								.put(DocumentDefinition.F_DOCUMENT_EDITORID,
										documentDefinition
												.getValue(DocumentDefinition.F_DOCUMENT_EDITORID));
						documentDefinitionData.put(
								DocumentDefinition.F_ORGANIZATION_ID, get_id());
						documentDefinitionData
								.put(DocumentDefinition.F_TEMPLATEFILE,
										documentDefinition
												.getValue(DocumentDefinition.F_TEMPLATEFILE));

						documentDefinitionData.put(
								DocumentDefinition.F__CACCOUNT, accountInfo);
						documentDefinitionData.put(DocumentDefinition.F__CDATE,
								new Date());
						documentDefinitionData
								.put(DocumentDefinition.F__EDITOR,
										documentDefinition
												.getValue(DocumentDefinition.F__EDITOR));
						documentDefinitionData.put(DocumentDefinition.F__ID,
								documentDefinition_id);
						documentDefinitionData
								.put(DocumentDefinition.F__VID, 0);
						documentDefinitionData.put(DocumentDefinition.F_DESC,
								documentDefinition
										.getValue(DocumentDefinition.F_DESC));
						documentDefinitionData
								.put(DocumentDefinition.F_DESC_EN,
										documentDefinition
												.getValue(DocumentDefinition.F_DESC_EN));
						documentDefinitionList.add(documentDefinitionData);

					}

					deliverableDefinitionData.put(
							DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
							documentDefinition_id);
				}
				deliverableDefinitionData.put(
						DeliverableDefinition.F_PROJECTTEMPLATE_ID,
						projectTemplate_id);
				deliverableDefinitionData
						.put(DeliverableDefinition.F_WORK_DEFINITION_ID,
								workDefinitionSet.get(deliverableDefinition
										.getValue(DeliverableDefinition.F_WORK_DEFINITION_ID)));
				deliverableDefinitionData
						.put(DeliverableDefinition.F_OPTION_FILTERS,
								deliverableDefinition
										.getValue(DeliverableDefinition.F_OPTION_FILTERS));

				deliverableDefinitionData.put(
						DeliverableDefinition.F__CACCOUNT, accountInfo);
				deliverableDefinitionData.put(DeliverableDefinition.F__CDATE,
						new Date());
				deliverableDefinitionData.put(DeliverableDefinition.F__EDITOR,
						deliverableDefinition
								.getValue(DeliverableDefinition.F__EDITOR));
				deliverableDefinitionData.put(DeliverableDefinition.F__ID,
						deliverableDefinition_id);
				deliverableDefinitionData.put(DeliverableDefinition.F__VID, 0);
				deliverableDefinitionData.put(DeliverableDefinition.F_DESC,
						deliverableDefinition
								.getValue(DeliverableDefinition.F_DESC));
				deliverableDefinitionData.put(DeliverableDefinition.F_DESC_EN,
						deliverableDefinition
								.getValue(DeliverableDefinition.F_DESC_EN));
				deliverableDefinitionList.add(deliverableDefinitionData);
				deliverableDefinition_id = new ObjectId();
			}
		}

		// 6.����ǰ���ù�ϵ
		List<PrimaryObject> workConnections = projectTemplate
				.getWorkConnections();
		for (PrimaryObject po : workConnections) {
			if (po instanceof WorkConnection) {
				DBObject workConnectionData = new BasicDBObject();
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

				workConnectionData.put(WorkConnection.F__CACCOUNT, accountInfo);
				workConnectionData.put(WorkConnection.F__CDATE, new Date());
				workConnectionData.put(WorkConnection.F__EDITOR,
						workConnection.getValue(WorkConnection.F__EDITOR));
				workConnectionData.put(WorkConnection.F__ID, workConnection_id);
				workConnectionData.put(WorkConnection.F__VID, 0);
				workConnectionData.put(WorkConnection.F_DESC,
						workConnection.getValue(WorkConnection.F_DESC));
				workConnectionData.put(WorkConnection.F_DESC_EN,
						workConnection.getValue(WorkConnection.F_DESC_EN));
				workConnectionList.add(workConnectionData);
				workConnection_id = new ObjectId();
			}
		}

		String error = null;
		DBCollection projectTemplateCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		WriteResult projectTemplateWriteResult = projectTemplateCol
				.insert(projectTemplateList);
		error = projectTemplateWriteResult.getError();
		if (error != null) {
			System.out.println("���ƻ�����Ϣ����");
			throw new Exception(error);
		}

		if (budgetItemList.size() > 0) {
			DBCollection budgetItemCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_BUDGET_ITEM);
			WriteResult budgetItemWriteResult = budgetItemCol
					.insert(budgetItemList);
			error = budgetItemWriteResult.getError();
			if (error != null) {
				System.out.println("����Ԥ�����");
				throw new Exception(error);
			}
		}

		if (roleDefinitionList.size() > 0) {
			DBCollection roleDefinitionCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
			WriteResult roleDefinitionWriteResult = roleDefinitionCol
					.insert(roleDefinitionList);
			error = roleDefinitionWriteResult.getError();
			if (error != null) {
				System.out.println("���ƽ�ɫ����");
				throw new Exception(error);
			}
		}

		if (workDefinitionList.size() > 0) {
			DBCollection workDefinitionCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
			WriteResult workDefinitionWriteResult = workDefinitionCol
					.insert(workDefinitionList);
			error = workDefinitionWriteResult.getError();
			if (error != null) {
				System.out.println("���ƹ����������");
				throw new Exception(error);
			}
		}

		if (deliverableDefinitionList.size() > 0) {
			DBCollection deliverableDefinitionCol = DBActivator.getCollection(
					IModelConstants.DB,
					IModelConstants.C_DELIEVERABLE_DEFINITION);
			WriteResult deliverableDefinitionWriteResult = deliverableDefinitionCol
					.insert(deliverableDefinitionList);
			error = deliverableDefinitionWriteResult.getError();
			if (error != null) {
				System.out.println("���ƽ��������");
				throw new Exception(error);
			}
		}

		if (documentDefinitionList.size() > 0) {
			DBCollection documentDefinitionCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_DOCUMENT_DEFINITION);
			WriteResult documentDefinitionWriteResult = documentDefinitionCol
					.insert(documentDefinitionList);
			error = documentDefinitionWriteResult.getError();
			if (error != null) {
				System.out.println("���ƽ������ĵ�����");
				throw new Exception(error);
			}
		}

		if (workConnectionList.size() > 0) {
			DBCollection workConnectionCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_WORK_CONNECTION);
			WriteResult workConnectionWriteResult = workConnectionCol
					.insert(workConnectionList);
			error = workConnectionWriteResult.getError();
			if (error != null) {
				System.out.println("����ǰ���ù�ϵ����");
				throw new Exception(error);
			}
		}

		// д��־
		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "������Ŀģ��",
				new Date(), "��֯��" + this + "\n��Ŀģ��" + selectList.toString(),
				IModelConstants.DB);

	}

	public List<DroolsProcessDefinition> getDroolsProcessDefinitions() {
		Object value = getValue(F_KBASE);
		ArrayList<DroolsProcessDefinition> result = new ArrayList<DroolsProcessDefinition>();
		if (value instanceof BasicBSONList) {
			BasicBSONList kbases = (BasicBSONList) value;
			for (int i = 0; i < kbases.size(); i++) {
				String kname = (String) kbases.get(i);
				KnowledgeBase kbase = BPM.getBPMService().getKnowledgeBase(
						kname);
				Collection<Process> processList = kbase.getProcesses();
				Iterator<Process> iter = processList.iterator();
				while (iter.hasNext()) {
					Process process = iter.next();
					result.add(new DroolsProcessDefinition(kname, process));
				}
			}
		}
		return result;
	}

	/**
	 * ��ȡ������֯����ľ����ĵ���������֯
	 * 
	 * @return
	 */
	public ObjectId getContainerOrganizationId() {
		if (isContainer()) {
			return this.get_id();
		} else {
			return ((Organization) getParentOrganization())
					.getContainerOrganizationId();
		}
	}
	
	/**
	 * ��ȡ������֯����ľ����ĵ���������֯
	 * 
	 * @return
	 */
	public Organization getContainerOrganization() {
		if (isContainer()) {
			return this;
		} else {
			return ((Organization) getParentOrganization())
					.getContainerOrganization();
		}
	}

	private SummaryOrganizationWorks summary;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == IWorksSummary.class) {
			if (summary == null) {
				summary = new SummaryOrganizationWorks(this);
			}
			return (T) summary;
		}
		return super.getAdapter(adapter);
	}

	public boolean isCostCenter() {
		Object value = getValue(F_COST_CENTER_CODE);
		if (value instanceof String) {
			return !((String) value).isEmpty();
		}
		return false;
	}

	/**
	 * �Ƿ��𼶲�ѯ
	 * 
	 * @param hierarchy
	 * @return
	 */
	public List<String> getMemberIds(boolean hierarchy) {
		List<String> result = new ArrayList<String>();
		List<PrimaryObject> users = getUser();
		for (int i = 0; i < users.size(); i++) {
			result.add(((User) users.get(i)).getUserid());
		}
		if (hierarchy) {
			List<PrimaryObject> children = getChildrenOrganization();
			for (int i = 0; i < children.size(); i++) {
				Organization subOrg = (Organization) children.get(i);
				result.addAll(subOrg.getMemberIds(hierarchy));
			}
		}

		return result;
	}

	public String getCode() {
		return (String) getValue(F_CODE);
	}

	public Organization getCompany() {
		String companycode = getCompanyCode();
		if (Utils.isNullOrEmpty(companycode)) {
			Organization parent = (Organization) getParentOrganization();
			if (parent != null) {
				return parent.getCompany();
			}
		} else {
			return this;
		}
		return null;
	}

	public boolean hasWorkOrder(String workOrder) {
		DBCollection col = getCollection(IModelConstants.C_COMPANY_WORKORDER);
		long count = col.count(new BasicDBObject().append(
				CompanyWorkOrder.F_ORGANIZATION_ID, get_id()).append(
				CompanyWorkOrder.F_WORKORDER, workOrder));
		return count > 0;
	}

	public void doSaveWorkOrders(List<?> wonList) {
		DBCollection col = getCollection(IModelConstants.C_COMPANY_WORKORDER);
		for (int i = 0; i < wonList.size(); i++) {
			Object won = wonList.get(i);
			try {
				col.insert(new BasicDBObject().append(
						CompanyWorkOrder.F_ORGANIZATION_ID, get_id()).append(
						CompanyWorkOrder.F_WORKORDER, won));
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ������ɫ���û�
	 * @param roleNumber
	 * @param selectType
	 * @return
	 */
	public List<String> getRoleAssignmentUserIds(String roleNumber,
			int selectType) {
		Role role = getRole(roleNumber, selectType);
		List<String> result = new ArrayList<String>();
		if (role != null) {
			List<PrimaryObject> assignment = role.getAssignment();
			if (assignment != null) {
				for (int i = 0; i < assignment.size(); i++) {
					result.add(((RoleAssignment) assignment.get(0)).getUserid());
				}
			}
		}
		return result;
	}

	public PrimaryObject getFunctionOrganization() {
		if(isFunctionDepartment()){
			return this;
		}else{
			Organization parent = (Organization) getParentOrganization();
			if(parent!=null){
				return parent.getFunctionOrganization();
			}
		}
		return null;
	}

	public List<?> getDocumentAndDrawingContainerCode() {
		return getListValue(F_PDM_DOC_DRAWING_COMTAINER);
	}
	
	public List<?> getPartContainerCode() {
		return getListValue(F_PDM_PART_COMTAINER);
	}
}
