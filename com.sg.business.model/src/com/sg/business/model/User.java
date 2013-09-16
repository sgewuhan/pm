package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * �û�<p/>
 * �û�Ϊ��˾ְԱ����������֯
 * @author jinxitao
 *
 */
public class User extends PrimaryObject {

	/**
	 * �û�����
	 */
	public static final String F_EMAIL = "email";

	/**
	 * �û�ID
	 */
	public static final String F_USER_ID = "userid";

	/**
	 * �û�����
	 */
	public static final String F_USER_NAME = "username";

	/**
	 * �û��ǳ�
	 */
	public static final String F_NICK = "nick";

	/**
	 * �û�ͷ����Ƭ
	 */
	public static final String F_HEADPIC = "headpic";
	
	/**
	 * ����
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * ������֯ID
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";
	
	/**
	 * ������֯����
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_NAME = "organization_name";

	/**
	 * ί����
	 */
	public static final String F_CONSIGNER = "consigner";

	/**
	 * �Ƿ�ϵͳ����Ա
	 */
	public static final String F_IS_ADMIN = "isadmin";

	/**
	 * ��ȡ��֯_id
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * �����û����
	 * @return String
	 */
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}

	/**
	 * �����û�����
	 * @return String
	 */
	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}

	/**
	 * ����û���������֯
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
	 * ��ȡ�û�����Ľ�ɫ
	 * 
	 * @param roleNumber
	 *            , �������,��ѯ���еĽ�ɫ
	 * @return
	 */
	public List<PrimaryObject> getRoles(String roleNumber) {

		// ��ý�ɫָ�ɼ���
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);

		// �����ѯ����
		BasicDBObject condition = new BasicDBObject();
		condition.put(RoleAssignment.F_USER_ID, getUserid());

		// ͬ��һ����ɫ�ı�ſ��ܱ�Ӧ�õ������֯�У����Կ��ܶ����ɫ����Ľ�ɫ��ſ�����ͬ
		// ���������roleNumber����Ҫ��ѯ���ϸý�ɫ��ŵ����н�ɫ
		if (roleNumber != null) {
			condition.put(RoleAssignment.F_ROLE_NUMBER, roleNumber);
		}

		// ִ�в�ѯ
		// ����ֻ��Ҫ��ý�ɫ��id,�����޶��˲�ѯ�ķ����ֶ�
		DBCursor cur = roleAssignmentCol.find(condition,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1));

		// ȡ����ɫid
		ObjectId[] roleIds = new ObjectId[cur.size()];
		int i = 0;
		while (cur.hasNext()) {
			roleIds[i++] = (ObjectId) cur.next().get(RoleAssignment.F_ROLE_ID);
		}

		List<PrimaryObject> result = new ArrayList<PrimaryObject>();

		// ��ý�ɫ����
		DBCollection roleCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE);

		// ��ѯid�������Ѿ�ȡ����id�����еļ�¼��������Ϊ��ɫ����
		cur = roleCol.find(new BasicDBObject().append(Role.F__ID,
				new BasicDBObject().append("$in", roleIds)));
		while (cur.hasNext()) {
			result.add(ModelService.createModelObject(cur.next(), Role.class));
		}

		return result;
	}

	/**
	 * �û�����ʾ����
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getUsername() + "|" + getUserid();
	}

	/**
	 * �û�����ʾͼ��
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
	 * ɾ���û�
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		if (context != null
				&& "organization.member".equals(context.getPartId())) {// ɾ���Ŷӳ�Ա

			setValue(F_ORGANIZATION_ID, null);
			try {
				doSave(context);
			} catch (Exception e) {
			}
		} else {
			super.doRemove(context);
		}
	}

	
	/**
	 * ����û�����ĳ��ɫ����֯
	 * @param roleNumber
	 *           ,��ɫ���
	 * @return
	 */
	public List<PrimaryObject> getRoleGrantedOrganization(String roleNumber) {

		List<PrimaryObject> roles = getRoles(roleNumber);

		// ȡ����Щ��ɫ��������֯��id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		List<PrimaryObject> orgs = new ArrayList<PrimaryObject>();
		
		// ��ѯ���ڿɹ������֯
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
	 * ���û�ί���������û�
	 * @param consigner
	 *         ,��ί���û�
	 * @param context
	 * @throws Exception
	 */
	public void doConsignTo(User consigner, IContext context) throws Exception {
		setValue(F_CONSIGNER, consigner.getUserid());
		doSave(context);
	}

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "�û�";
	}
}
