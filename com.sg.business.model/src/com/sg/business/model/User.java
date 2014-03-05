package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
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
import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;

/**
 * �û�
 * <p/>
 * �û�Ϊ��˾ְԱ����������֯
 * 
 * @author jinxitao
 * 
 */
public class User extends PrimaryObject {

	/**
	 * �û�����
	 */
	public static final String F_EMAIL = "email"; //$NON-NLS-1$

	/**
	 * �û�ID
	 */
	public static final String F_USER_ID = "userid"; //$NON-NLS-1$

	/**
	 * �û�����
	 */
	public static final String F_USER_NAME = "username"; //$NON-NLS-1$

	/**
	 * �û��ǳ�
	 */
	public static final String F_NICK = "nick"; //$NON-NLS-1$

	/**
	 * �û�ͷ����Ƭ
	 */
	public static final String F_HEADPIC = "headpic"; //$NON-NLS-1$

	/**
	 * ����
	 */
	public static final String F_ACTIVATED = "activated"; //$NON-NLS-1$

	/**
	 * ������֯ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * ������֯����
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_NAME = "organization_name"; //$NON-NLS-1$

	/**
	 * ί����
	 */
	public static final String F_CONSIGNER = "consigner"; //$NON-NLS-1$

	/**
	 * �Ƿ�ϵͳ����Ա
	 */
	public static final String F_IS_ADMIN = "isadmin"; //$NON-NLS-1$

	private static final String F_LASTOPENED = "lastopened"; //$NON-NLS-1$

	public static final String F_SCENARIO = "scenario"; //$NON-NLS-1$

	/**
	 * ��ȡ��֯_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * �����û����
	 * 
	 * @return String
	 */
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}

	/**
	 * �����û�����
	 * 
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
	
	@Override
	public boolean doSave(IContext context) throws Exception {
		boolean saved = super.doSave(context);
		//���»���
		UserToolkit.updateUser(this);
		
		return saved;
	}

	/**
	 * TODO ͣ���û� ֻҪ���Ʋ�����ѡ��
	 */

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
				new BasicDBObject().append("$in", roleIds))); //$NON-NLS-1$
		while (cur.hasNext()) {
			result.add(ModelService.createModelObject(cur.next(), Role.class));
		}

		return result;
	}

	/**
	 * �û�����ʾ����
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		return getUsername() + "|" + getUserid(); //$NON-NLS-1$
	}

	/**
	 * �û�����ʾͼ��
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
	 * ɾ���û�
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		if (context != null
				&& "organization.member".equals(context.getPartId())) {// ɾ���Ŷӳ�Ա //$NON-NLS-1$

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
		if (value instanceof BasicBSONList) {
			scenarioList = (BasicDBList) value;
			if (!scenarioList.contains(id)) {
				scenarioList.add(id);
			}
		} else {
			scenarioList = new BasicDBList();
			scenarioList.add(id);
			setValue(F_SCENARIO, scenarioList);
		}

		// ���û���ӵ��������ݿ�
		HTService ts = BPM.getHumanTaskService();
		ts.addParticipateUser(getUserid());
		super.doInsert(context);
	}

	/**
	 * ����û�����ĳ��ɫ����֯
	 * 
	 * @param roleNumber
	 *            ,��ɫ���
	 * @return
	 */
	public List<PrimaryObject> getRoleGrantedInFunctionDepartmentOrganization(
			String roleNumber) {

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
				new BasicDBObject().append("$in", orgIds)); //$NON-NLS-1$
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
	 * ����û�����ĳ��ɫ����֯
	 * 
	 * @param roleNumber
	 *            ,��ɫ���
	 * @return
	 */
	public List<PrimaryObject> getRoleGrantedInAllOrganization(String roleNumber) {

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
				new BasicDBObject().append("$in", orgIds)); //$NON-NLS-1$
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
	 * 
	 * @param consigner
	 *            ,��ί���û�
	 * @param context
	 * @throws Exception
	 */
	public void doConsignTo(User consigner, IContext context) throws Exception {
		setValue(F_CONSIGNER, consigner.getUserid());
		doSave(context);
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().User_0;
	}

	public List<DBObject> getLastOpen() {
		List<?> value = (List<?>) getValue(F_LASTOPENED, true);
		List<DBObject> result = new ArrayList<DBObject>();
		if (value != null) {
			for (int i = 0; i < value.size(); i++) {
				DBObject element = (DBObject) value.get(i);
				if (!Utils.contains(result, element, "id")) { //$NON-NLS-1$
					result.add(element);
				}
			}

			Collections.sort(result, new DBObjectComparator(
					new String[] { "date,-1" })); //$NON-NLS-1$
		}

		return result;

	}

	public String getEmail() {
		return (String) getValue(F_EMAIL);
	}

	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}

	private SummaryUserWorks summary;
	private ProjectManagerProvider projectProvider;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == IWorksSummary.class) {
			if (summary == null) {
				summary = new SummaryUserWorks(this);
			}
			return (T) summary;
		}else if (adapter == ProjectProvider.class) {
			if (projectProvider == null) {
				projectProvider = ModelService
						.createModelObject(ProjectManagerProvider.class);
				projectProvider.setUser(this);
			}
			return (T) projectProvider;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * ����״̬��ȡ��ǰ�û����������Ŀ��
	 * 
	 * @param status
	 *            :��Ŀ״̬��NULLʱΪ��ȡ���е���Ŀ
	 */
	public List<PrimaryObject> getChargeProject(String status) {
		if (status != null) {
			BasicDBObject condition = new BasicDBObject();
			condition.append(Project.F_CHARGER, getUserid());
			condition.append(Project.F_LIFECYCLE, status);
			List<PrimaryObject> projectList = getRelationByCondition(
					Project.class, condition);
			if (ILifecycle.STATUS_NONE_VALUE.equals(status)) {
				condition = new BasicDBObject();
				condition.append(Project.F_CHARGER, getUserid());
				condition.append(Project.F_LIFECYCLE, null);
				if (projectList == null) {
					projectList = getRelationByCondition(Project.class,
							condition);
				} else {
					projectList.addAll(getRelationByCondition(Project.class,
							condition));
				}
			}

			return projectList;
		} else {
			return getRelationById(F_USER_ID, Project.F_CHARGER, Project.class);
		}
	}

	public String getFirstHeadPicURL() {
		List<RemoteFile> headpics = getGridFSFileValue(User.F_HEADPIC);
		if (headpics != null && headpics.size() > 0) {
			try {
				return FileUtil.getImageURL(headpics.get(0).getNamespace(),
						new ObjectId(headpics.get(0).getObjectId()), headpics
								.get(0).getDbName());
			} catch (Exception e) {
			}
		}
		
		return null;
	}

	public Organization getFunctionOrganization() {
		Organization org = getOrganization();
		Organization functionOrganization = (Organization) org.getFunctionOrganization();
		return functionOrganization;
	}
}
