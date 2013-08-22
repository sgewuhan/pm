package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.business.resource.BusinessResource;

/**
 * ��֯<p/>
 * 
 * ��֯Ϊ��˾����֯�ṹ<br>
 * ��֯����������֯,��֯��ɫ���û�<br>
 * @author jinxitao
 */
public class Organization extends PrimaryObject {

	/**
	 * ��֯���
	 */
	public static final String F_ORGANIZATION_NUMBER = "organizationnumber";

	/**
	 * ��֯˵��
	 */
	public static final String F_DESCRIPTION = "description";

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
	 * ��֯�ĳɱ����Ĵ��룬	�ɱ����Ĵ���ΪSAPϵͳ�еĳɱ����Ĵ���
	 */
	public static final String F_COST_CENTER_CODE = "costcentercode";

	/**
	 * ��֯���ͣ���֯���ͷ�Ϊ���ˣ���ҵ�������ź��Ŷ�
	 */
	public static final String F_ORGANIZATION_TYPE = "organizationtype";

	/**
	 * ������֯��˵��. see {@link #F_DESCRIPTION}
	 * @return String
	 */
	public String getDescription() {
		return (String) getValue(F_DESCRIPTION);
	}

	/**
	 * �ж���֯�Ƿ������Ŀ����ְ�ܡ�see {@link #F_IS_FUNCTION_DEPARTMENT}
	 * @return boolean
	 */
	public boolean isFunctionDepartment() {
		return Boolean.TRUE.equals((Boolean) getValue(F_IS_FUNCTION_DEPARTMENT));
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
	 * ������֯���ϼ���֯_id  see {@link #F_PARENT_ID}
	 * @return ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	/**
	 * ������֯�ı�š�see {@link #F_ORGANIZATION_NUMBER}
	 * @return String
	 */
	public String getOrganizationNumber() {
		return (String) getValue(F_ORGANIZATION_NUMBER);
	}

	/**
	 * �ж���֯�Ƿ�����ĵ�����ְ�ܡ�see {@link #F_IS_CONTAINER}
	 * @return boolean
	 */
	public boolean isContainer() {
		return Boolean.TRUE.equals((Boolean) getValue(F_IS_CONTAINER));
	}

	/**
	 * ��ȡ��֯�ĳɱ����Ĵ��롣see {@link #F_COST_CENTER_CODE}
	 * @return String
	 */
	public String getCostCenterCode() {
		return (String) getValue(F_COST_CENTER_CODE);
	}

	/**
	 * ��ȡ��֯�����͡�see {@link #F_ORGANIZATION_TYPE}
	 * @return String
	 */
	public String getOrganizationType() {
		return (String) getValue(F_ORGANIZATION_TYPE);
	}

	/**
	 * ������֯��ϵͳ�е���ʾ����
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getOrganizationNumber();
	}

	/**
	 * ������֯��ϵͳ�е���ʾ���ݵĸ�ʽ
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
		sb.append(path);
		return sb.toString();
	}

	/**
	 * ������֯��ϵͳ�е���ʾͼ���ַ
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
	 * ����û�����֯��
	 * @param userIdList
	 *            ,�û�_id������
	 */
	public void doAddMembers(ObjectId[] userIdList) {
		//��ȡ�û���
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		//�����û�����������֯
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
	 * @param userDatas 
	 *            ,�û����󼯺�
	 */
	public void doAddMembers(List<PrimaryObject> userDatas) {
		ObjectId[] userIdList = new ObjectId[userDatas.size()];
		for (int i = 0; i < userDatas.size(); i++) {
			User user = (User) userDatas.get(i);
			String userId = user.getUserid();
			UserSessionContext.noticeAccountChanged(userId,
					UserSessionContext.EVENT_ORG_CHANGED);
			userIdList[i] = (ObjectId) user.get_id();
		}
		doAddMembers(userIdList);
	}

	/**
	 * ɾ����֯
	 * @param context
	 *           ,������
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
	 *              ,��֯
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
	 * @param po
	 *         ,ͨ�ù�������
	 * @return WorkDefinition
	 */
	public WorkDefinition makeGenericWorkDefinition(WorkDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_GENERIC_WORK_ROOT);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_GENERIC);
		return po;
	}

	/**
	 * �½���Ŀģ��
	 * @param po
	 *         ,��Ŀģ��
	 * @return ProjectTemplate
	 */
	public ProjectTemplate makeProjectTemplate(ProjectTemplate po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), ProjectTemplate.class);
		}
		po.setValue(ProjectTemplate.F_ORGANIZATION_ID, get_id());
		
		return po;
	}

	/**
	 * �½���׼��������
	 * @param po
	 *         ,��������
	 * @return
	 */
	public WorkDefinition makeStandardWorkDefinition(WorkDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_STANDLONE_WORK_ROOT);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_STANDLONE);
		return po;
		
	}
	

	/**
	 * �½��ĵ�ģ��
	 * @param po
	 *        ,�ĵ�����
	 * @return
	 */
	public DocumentDefinition makeDocumentDefinition(DocumentDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), DocumentDefinition.class);
		}	
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		return po;
	}

	/**
	 * �½�����֯
	 * @param po
	 *        ,��֯
	 * @return Organization
	 */
	public Organization makeChildOrganization(Organization po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Organization.class);
		}
		po.setValue(Organization.F_PARENT_ID, get_id());
		return po;
	}

	/**
	 * �½���ɫ
	 * @param po
	 *        ,��ɫ
	 * @return  ,Role
	 */
	public Role makeRole(Role po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Role.class);
		}
		po.setValue(Role.F_ORGANIZATION_ID, get_id());
		return po;

	}

	/**
	 * ��ȡ�����Լ��¼����еĽ�ɫ
	 * @return List
	 */
	public List<PrimaryObject> getRolesIteration() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		iterateSearchRolesRoles(this,result);
		return result;
	}

	/**
	 * ��ѯ��֯������֯�����н�ɫ
	 * @param org
	 *          ,��֯
	 * @param dataItems
	 *          ,��֯�µ����н�ɫ
	 */
	private void iterateSearchRolesRoles(Organization org, List<PrimaryObject> dataItems) {
		dataItems.addAll(org.getRoles());
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (PrimaryObject primaryObject : children) {
			iterateSearchRolesRoles((Organization) primaryObject, dataItems);
		}
	}	
	
	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��֯";
	}

}
