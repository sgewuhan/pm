package com.sg.business.model;

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

public class Organization extends PrimaryObject {

	public static final String F_ORGANIZATION_NUMBER = "organizationnumber";

	public static final String F_DESCRIPTION = "description";

	public static final String F_PARENT_ID = "parent_id";

	public static final String F_IS_FUNCTION_DEPARTMENT = "isfunctiondepartment";

	public static final String F_IS_CONTAINER = "iscontainer";

	public static final String F_COST_CENTER_CODE = "costcentercode";

	public static final String F_ORGANIZATION_TYPE = "organizationtype";

	public String getDescription() {
		return (String) getValue(F_DESCRIPTION);
	}

	public Boolean isFunctionDepartment() {
		return (Boolean) getValue(F_IS_FUNCTION_DEPARTMENT);
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

	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	public String getOrganizationNumber() {
		return (String) getValue(F_ORGANIZATION_NUMBER);
	}

	public Boolean isContainer() {
		return (Boolean) getValue(F_IS_CONTAINER);
	}

	public String getCostCenterCode() {
		return (String) getValue(F_COST_CENTER_CODE);
	}

	public String getOrganizationType() {
		return (String) getValue(F_ORGANIZATION_TYPE);
	}

	@Override
	public String getLabel() {
		return getDesc() + "|" + getOrganizationNumber();
	}

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

	public String getImageURL() {
		if (getValue(F_PARENT_ID) == null) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_ORG_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else {
			return FileUtil.getImageURL(BusinessResource.IMAGE_TEAM_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
	}

	@Override
	public Image getImage() {
		if (getValue(F_PARENT_ID) == null) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ORG_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_TEAM_16);
		}
	}

	public void doAddMembers(ObjectId[] userIdList) {
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		userCol.update(
				new BasicDBObject().append(User.F__ID,
						new BasicDBObject().append("$in", userIdList)),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(User.F_ORGANIZATION_ID,
								get_id()).append(User.F_ORGANIZATION_NAME,
								getDesc())), false, true);
	}

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

	@Override
	public void doRemove(IContext context) throws Exception {
		// 删除本组织的角色
		List<PrimaryObject> childrenRole = getRoles();
		for (int i = 0; i < childrenRole.size(); i++) {
			childrenRole.get(i).doRemove(context);
		}
		// 删除该组织的下级组织
		List<PrimaryObject> childrenOrg = getChildrenOrganization();
		for (int i = 0; i < childrenOrg.size(); i++) {
			childrenOrg.get(i).doRemove(context);
		}
		super.doRemove(context);
	}

	/**
	 * 获取组织的上级组织
	 * 
	 * @return
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
	 * 获取下级组织
	 * 
	 * @return
	 */
	public List<PrimaryObject> getChildrenOrganization() {
		return getRelationById(F__ID, F_PARENT_ID, Organization.class);
	}

	/**
	 * 获取组织下的所有角色
	 * 
	 * @return
	 */
	public List<PrimaryObject> getRoles() {
		return getRelationById(F__ID, Role.F_ORGANIZATION_ID, Role.class);
	}

	/**
	 * 获取当前组织的所有系统角色
	 * 
	 * @return
	 */
	public List<PrimaryObject> getSystemRoles() {
		DBObject condition = new BasicDBObject();
		condition.put(Role.F_ORGANIZATION_ID, get_id());
		condition.put(Role.F_ROLE_NUMBER,
				new BasicDBObject().append("$in", Role.ROLE_ID_SYSTEM));
		return getRelationByCondition(Role.class, condition);
	}

	/**
	 * 检查组织下是否包含某个角色
	 * 
	 * @param roleNumber
	 *            , 角色编号
	 * @return
	 */
	public boolean hasRole(String roleNumber) {
		return getRelationCountByCondition(Role.class,
				new BasicDBObject().append(Role.F_ROLE_NUMBER, roleNumber)
						.append(Role.F_ORGANIZATION_ID, get_id())) > 0;
	}

	/**
	 * 添加组织的一个角色
	 * 
	 * @param roleNumber
	 *            , 角色编号
	 * @param roleName
	 *            , 角色名称
	 * @return
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
	 * 获得当前组织的路径
	 * 
	 * @return
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
	 * 判断当前组织是否是某个组织的上级组织
	 * 
	 * @param organization
	 * @return
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

	public WorkDefinition makeGenericWorkDefinition(WorkDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_GENERIC_WORK);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_GENERIC);
		return po;
	}

	public ProjectTemplate makeProjectTemplate(ProjectTemplate po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), ProjectTemplate.class);
		}
		po.setValue(ProjectTemplate.F_ORGANIZATION_ID, get_id());
		
		return po;
	}

	public WorkDefinition makeStandardWorkDefinition(WorkDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), WorkDefinition.class);
		}
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		po.setValue(WorkDefinition.F__EDITOR,WorkDefinition.EDITOR_STANDLONE_WORK);
		po.setValue(WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_STANDLONE);
		return po;
		
	}
	

	public DocumentDefinition makeDocumentDefinition(DocumentDefinition po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), DocumentDefinition.class);
		}	
		po.setValue(WorkDefinition.F_ORGANIZATION_ID, get_id());
		return po;
	}

	public Organization makeChildOrganization(Organization po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Organization.class);
		}
		po.setValue(Organization.F_PARENT_ID, get_id());
		return po;
	}

	public Role makeRole(Role po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Role.class);
		}
		po.setValue(Role.F_ORGANIZATION_ID, get_id());
		return po;

	}

}
