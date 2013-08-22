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
 * 组织<p/>
 * 
 * 组织为公司的组织结构<br>
 * 组织下面有子组织,组织角色和用户<br>
 * @author jinxitao
 */
public class Organization extends PrimaryObject {

	/**
	 * 组织编号
	 */
	public static final String F_ORGANIZATION_NUMBER = "organizationnumber";

	/**
	 * 组织说明
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * 组织的上级组织_id
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * 组织是否具有项目管理职能,具有项目管理职能的组织可以为项目的归口组织
	 */
	public static final String F_IS_FUNCTION_DEPARTMENT = "isfunctiondepartment";

	/**
	 * 组织是否可以为文档容器,为文档容器的组织可以归档项目文档
	 */
	public static final String F_IS_CONTAINER = "iscontainer";

	/**
	 * 组织的成本中心代码，	成本中心代码为SAP系统中的成本中心代码
	 */
	public static final String F_COST_CENTER_CODE = "costcentercode";

	/**
	 * 组织类型，组织类型分为法人，事业部，部门和团队
	 */
	public static final String F_ORGANIZATION_TYPE = "organizationtype";

	/**
	 * 返回组织的说明. see {@link #F_DESCRIPTION}
	 * @return String
	 */
	public String getDescription() {
		return (String) getValue(F_DESCRIPTION);
	}

	/**
	 * 判断组织是否具有项目管理职能。see {@link #F_IS_FUNCTION_DEPARTMENT}
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
	 * 返回组织的上级组织_id  see {@link #F_PARENT_ID}
	 * @return ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	/**
	 * 返回组织的编号。see {@link #F_ORGANIZATION_NUMBER}
	 * @return String
	 */
	public String getOrganizationNumber() {
		return (String) getValue(F_ORGANIZATION_NUMBER);
	}

	/**
	 * 判断组织是否具有文档容器职能。see {@link #F_IS_CONTAINER}
	 * @return boolean
	 */
	public boolean isContainer() {
		return Boolean.TRUE.equals((Boolean) getValue(F_IS_CONTAINER));
	}

	/**
	 * 获取组织的成本中心代码。see {@link #F_COST_CENTER_CODE}
	 * @return String
	 */
	public String getCostCenterCode() {
		return (String) getValue(F_COST_CENTER_CODE);
	}

	/**
	 * 获取组织的类型。see {@link #F_ORGANIZATION_TYPE}
	 * @return String
	 */
	public String getOrganizationType() {
		return (String) getValue(F_ORGANIZATION_TYPE);
	}

	/**
	 * 返回组织在系统中的显示内容
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getOrganizationNumber();
	}

	/**
	 * 返回组织在系统中的显示内容的格式
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
	 * 返回组织在系统中的显示图标地址
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
	 * 返回组织在系统中的显示图标
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
	 * 添加用户到组织中
	 * @param userIdList
	 *            ,用户_id的数组
	 */
	public void doAddMembers(ObjectId[] userIdList) {
		//获取用户表
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		//更新用户表，关联至组织
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
	 * 根据用户对象，添加用户到组织中
	 * @param userDatas 
	 *            ,用户对象集合
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
	 * 删除组织
	 * @param context
	 *           ,上下文
	 */
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
	 * 获取下级组织
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getChildrenOrganization() {
		return getRelationById(F__ID, F_PARENT_ID, Organization.class);
	}

	/**
	 * 获取组织下的所有角色
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getRoles() {
		return getRelationById(F__ID, Role.F_ORGANIZATION_ID, Role.class);
	}

	/**
	 * 获取当前组织的所有系统角色
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
	 * 检查组织下是否包含某个角色
	 * 
	 * @param roleNumber
	 *            ,角色编号
	 * @return boolean
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
	 * 获得当前组织的路径
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
	 * 判断当前组织是否是某个组织的上级组织
	 * 
	 * @param organization
	 *              ,组织
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
	 * 新建通用工作定义
	 * @param po
	 *         ,通用工作定义
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
	 * 新建项目模板
	 * @param po
	 *         ,项目模板
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
	 * 新建标准工作定义
	 * @param po
	 *         ,工作定义
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
	 * 新建文档模板
	 * @param po
	 *        ,文档定义
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
	 * 新建子组织
	 * @param po
	 *        ,组织
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
	 * 新建角色
	 * @param po
	 *        ,角色
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
	 * 获取本级以及下级所有的角色
	 * @return List
	 */
	public List<PrimaryObject> getRolesIteration() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		iterateSearchRolesRoles(this,result);
		return result;
	}

	/**
	 * 查询组织和子组织中所有角色
	 * @param org
	 *          ,组织
	 * @param dataItems
	 *          ,组织下的所有角色
	 */
	private void iterateSearchRolesRoles(Organization org, List<PrimaryObject> dataItems) {
		dataItems.addAll(org.getRoles());
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (PrimaryObject primaryObject : children) {
			iterateSearchRolesRoles((Organization) primaryObject, dataItems);
		}
	}	
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "组织";
	}

}
