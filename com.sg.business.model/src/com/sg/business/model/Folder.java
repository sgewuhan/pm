package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.sg.business.resource.BusinessResource;

/**
 * 文件夹
 * 
 * @author jinxitao
 * 
 */
public class Folder extends PrimaryObject {

	/**
	 * 文件类型
	 */
	public static final String F_FOLDER_TYPE = "folderType";

	/**
	 * 上级文件夹ID
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * 根文件夹ID
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * 容器存储的数据库
	 */
	public static final String F_CONTAINER_DB = "containerdb";

	/**
	 * 容器存储的集合
	 */
	public static final String F_CONTAINER_COLLECTION = "containerCollection";

	/**
	 * 项目ID
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * 是否项目根文件夹
	 */
	public static final String F_IS_PROJECT_FOLDERROOT = "isflderroot";

	/**
	 * 返回文件夹类型
	 * 
	 * @return String
	 */
	public String getFolderType() {
		return (String) getValue(F_FOLDER_TYPE);
	}

	/**
	 * 返回上级文件夹_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_PARENT_ID);
	}

	/**
	 * 返回根文件夹_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getRoot_id() {
		return (ObjectId) getValue(F_ROOT_ID);
	}

	/**
	 * 返回项目_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getProject_id() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * 返回显示图标
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_FOLDER_16);
	}

	/**
	 * 判断是否可以删除
	 * 
	 * @param context
	 * @return boolean
	 */
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

	/**
	 * 判断是否存在文件
	 * 
	 * @return boolean
	 */
	private boolean hasFile() {
		return getRelationCountById(Folder.F__ID, Document.F_FOLDER_ID,
				Document.class) > 0;
	}

	/**
	 * 判断是否存在下级文件夹
	 * 
	 * @return boolean
	 */
	private boolean hasSubFolder() {
		return getRelationCountById(Folder.F__ID, Folder.F_PARENT_ID,
				Folder.class) > 0;
	}

	/**
	 * 返回类型名称
	 */
	@Override
	public String getTypeName() {
		return "目录";
	}

	public void doMoveToOtherFolder(ObjectId get_id) throws Exception {
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_PARENT_ID, get_id)));

		checkWriteResult(ws);
	}

	public Folder getParentFolder() {
		ObjectId folderId = (ObjectId) getValue(F_PARENT_ID);
		if (folderId != null) {
			return ModelService.createModelObject(Folder.class, folderId);
		} else {
			return null;
		}
	}

	@Override
	public String getDefaultEditorId() {
		return "editor.folder";
	}
}
