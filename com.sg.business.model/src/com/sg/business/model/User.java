package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBObjectComparator;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * 用户
 * <p/>
 * 用户为公司职员，归属于组织
 * 
 * @author jinxitao
 * 
 */
public class User extends PrimaryObject {

	/**
	 * 用户邮箱
	 */
	public static final String F_EMAIL = "email";

	/**
	 * 用户ID
	 */
	public static final String F_USER_ID = "userid";

	/**
	 * 用户名称
	 */
	public static final String F_USER_NAME = "username";

	/**
	 * 用户昵称
	 */
	public static final String F_NICK = "nick";

	/**
	 * 用户头部照片
	 */
	public static final String F_HEADPIC = "headpic";

	/**
	 * 激活
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * 所属组织ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 所属组织名称
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_NAME = "organization_name";

	/**
	 * 委托人
	 */
	public static final String F_CONSIGNER = "consigner";

	/**
	 * 是否系统管理员
	 */
	public static final String F_IS_ADMIN = "isadmin";

	private static final String F_LASTOPENED = "lastopened";
	
	public static final String F_SCENARIO="scenario";

	/**
	 * 获取组织_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * 返回用户编号
	 * 
	 * @return String
	 */
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}

	/**
	 * 返回用户名称
	 * 
	 * @return String
	 */
	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}

	/**
	 * 获得用户从属的组织
	 * 
	 * @return Organization
	 */
	public Organization getOrganization() {
		ObjectId organization_id = getOrganization_id();
		if (organization_id != null) {
			return ModelService.createModelObject(Organization.class,
					organization_id);
		} else {
			return null;
		}
	}

	/**
	 * TODO 停用用户 只要控制不允许选择
	 */

	/**
	 * 获取用户授予的角色
	 * 
	 * @param roleNumber
	 *            , 如果传空,查询所有的角色
	 * @return
	 */
	public List<PrimaryObject> getRoles(String roleNumber) {

		// 获得角色指派集合
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);

		// 构造查询条件
		BasicDBObject condition = new BasicDBObject();
		condition.put(RoleAssignment.F_USER_ID, getUserid());

		// 同样一个角色的编号可能被应用到多个组织中，所以可能多个角色对象的角色编号可能相同
		// 如果传递了roleNumber，需要查询符合该角色编号的所有角色
		if (roleNumber != null) {
			condition.put(RoleAssignment.F_ROLE_NUMBER, roleNumber);
		}

		// 执行查询
		// 由于只需要获得角色的id,所以限定了查询的返回字段
		DBCursor cur = roleAssignmentCol.find(condition,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1));

		// 取出角色id
		ObjectId[] roleIds = new ObjectId[cur.size()];
		int i = 0;
		while (cur.hasNext()) {
			roleIds[i++] = (ObjectId) cur.next().get(RoleAssignment.F_ROLE_ID);
		}

		List<PrimaryObject> result = new ArrayList<PrimaryObject>();

		// 获得角色集合
		DBCollection roleCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE);

		// 查询id包含在已经取出的id数组中的记录，并反射为角色对象
		cur = roleCol.find(new BasicDBObject().append(Role.F__ID,
				new BasicDBObject().append("$in", roleIds)));
		while (cur.hasNext()) {
			result.add(ModelService.createModelObject(cur.next(), Role.class));
		}

		return result;
	}

	/**
	 * 用户的显示内容
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getUsername() + "|" + getUserid();
	}

	/**
	 * 用户的显示图标
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		if (getValue(F_ORGANIZATION_ID) == null) {
			return BusinessResource.getImage(BusinessResource.IMAGE_USER2_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
		}
	}

	/**
	 * 删除用户
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		if (context != null
				&& "organization.member".equals(context.getPartId())) {// 删除团队成员

			setValue(F_ORGANIZATION_ID, null);
			try {
				doSave(context);
			} catch (Exception e) {
			}
		} else {
			super.doRemove(context);
		}
	}
	
	@Override
	public void doInsert(IContext context) throws Exception {
		String id = Portal.getDefault().getDefaultScenarioId();
		
		BasicDBList scenarioList;
		Object value = getValue(F_SCENARIO);
		if (value!=null&&value instanceof BasicBSONList) {
			scenarioList = (BasicDBList)value;
			if(!scenarioList.contains(value)){
				scenarioList.add(id);
			}
		}else{
			scenarioList=new BasicDBList();
			scenarioList.add(id);
			setValue(F_SCENARIO, scenarioList);
		}
		
		super.doInsert(context);
	}

	/**
	 * 获得用户具有某角色的组织
	 * 
	 * @param roleNumber
	 *            ,角色编号
	 * @return
	 */
	public List<PrimaryObject> getRoleGrantedOrganization(String roleNumber) {

		List<PrimaryObject> roles = getRoles(roleNumber);

		// 取出这些角色的所属组织的id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		List<PrimaryObject> orgs = new ArrayList<PrimaryObject>();

		// 查询属于可管理的组织
		DBCollection orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition = new BasicDBObject();
		condition.put(Organization.F__ID,
				new BasicDBObject().append("$in", orgIds));
		condition.put(Organization.F_IS_FUNCTION_DEPARTMENT, Boolean.TRUE);
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			if (!orgs.contains(org)) {
				orgs.add(org);
			}
		}

		return orgs;
	}

	/**
	 * 将用户委托至其他用户
	 * 
	 * @param consigner
	 *            ,被委托用户
	 * @param context
	 * @throws Exception
	 */
	public void doConsignTo(User consigner, IContext context) throws Exception {
		setValue(F_CONSIGNER, consigner.getUserid());
		doSave(context);
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "用户";
	}

	public List<DBObject> getLastOpen() {
		List<?> value = (List<?>) getValue(F_LASTOPENED, true);
		List<DBObject> result = new ArrayList<DBObject>();
		if (value != null) {
			for (int i = 0; i < value.size(); i++) {
				DBObject element = (DBObject) value.get(i);
				if (!Utils.contains(result,element,"id")) {
					result.add(element);
				}
			}

			Collections.sort(result, new DBObjectComparator(
					new String[] { "date,-1" }));
		}

		return result;

	}

	public String getEmail() {
		return (String) getValue(F_EMAIL);
	}

	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}
}
