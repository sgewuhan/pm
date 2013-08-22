package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class Folder extends PrimaryObject {

	public static final String F_FOLDER_TYPE = "folderType";

	public static final String F_PARENT_ID = "parent_id";
	public static final String F_ROOT_ID = "root_id";

	public static final String F_CONTAINER_DB = "containerdb";

	public static final String F_CONTAINER_COLLECTION = "containerCollection";

	public String getFolderType() {
		return (String) getValue(F_FOLDER_TYPE);
	}

	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	public ObjectId getRoot_id() {
		return (ObjectId) getValue(F_ROOT_ID);
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_FOLDER_16);
	}

	@Override
	public boolean canDelete(IContext context) {
		// 如果有下级文档或目录，不可删除
		if (hasSubFolder()) {
			return false;
		}
		if (hasFile()) {
			return false;
		}
		return super.canDelete(context);
	}

	// /**
	// * 获得当前帐号对当前目录的容器
	// *
	// * @param accountInfo
	// * @return
	// */
	// public Container getContainer(String userId) {
	// // if (root instanceof Organization) {
	// // if (roles != null&&roles.size()>0) {
	// // // 首先判断该用户是否具有管理权
	// // boolean h = hasRole(userId,Role.ROLE_VAULT_ADMIN_ID);
	// // if(h){
	// // return Container.adapter(root, Container.TYPE_ADMIN_GRANTED);
	// // }
	// // //判断该用户是否具有访问权
	// // h = hasRole(roles,userId,Role.ROLE_VAULT_GUEST_ID);
	// // if(h){
	// // return Container.adapter(root, Container.TYPE_GUEST_GRANTED);
	// // }
	// // }
	// // //判断用户组织是否是容器组织或容器组织的上级组织
	// //
	// // User user = User.getUserById(userId);
	// // Organization userOrg = user.getOrganization();
	// // if(userOrg.equals(root)||userOrg.isSuperOf((Organization)root)){
	// // return Container.adapter(root, Container.TYPE_OWNER);
	// // }
	// // }
	// return null;
	// }
	//
	// private boolean hasRole(String userId,
	// String targetRoleNumber) {
	// PrimaryObject root = getRoot();
	// List<PrimaryObject> roles = ((Organization) root).getSystemRoles();
	//
	// Role adminRole = null;
	// for (int i = 0; i < roles.size(); i++) {
	// Role role = (Role) roles.get(i);
	// String roleNumber = role.getRoleNumber();
	// if (roleNumber.equals(targetRoleNumber)) {
	// adminRole = role;
	// break;
	// }
	// }
	// if (adminRole != null) {
	// DBCollection raCol = DBActivator.getCollection(
	// IModelConstants.DB,
	// IModelConstants.C_ROLE_ASSIGNMENT);
	// long cnt = raCol.count(new BasicDBObject().append(
	// RoleAssignment.F_ROLE_ID, adminRole.get_id())
	// .append(RoleAssignment.F_USER_ID, userId));
	// if(cnt>0){
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public PrimaryObject getRoot() {
	// // 取出目录当前的根
	// String containerDB = (String) getValue(F_CONTAINER_DB);
	// String containerCollection = (String) getValue(F_CONTAINER_COLLECTION);
	// ObjectId rootId = (ObjectId) getValue(F_ROOT_ID);
	// Class<? extends PrimaryObject> rootClass = ModelService.getModelClass(
	// containerDB, containerCollection);
	// PrimaryObject root = ModelService.createModelObject(rootClass, rootId);
	// return root;
	// }

	private boolean hasFile() {
		return getRelationCountById(Folder.F__ID, Document.F_FOLDER_ID,
				Document.class) > 0;
	}

	private boolean hasSubFolder() {
		return getRelationCountById(Folder.F__ID, Folder.F_PARENT_ID,
				Folder.class) > 0;
	}
	
	@Override
	public String getTypeName() {
		return "目录";
	}

}
