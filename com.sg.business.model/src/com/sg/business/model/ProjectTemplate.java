package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.nls.Messages;
import com.sg.business.resource.BusinessResource;

/**
 * ��Ŀģ��
 * <p>
 * ��Ŀģ����ҵ�����Ա�����������½���Ŀ
 * 
 * @author jinxitao
 * 
 */
public class ProjectTemplate extends PrimaryObject{

	/**
	 * ������֯
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$


	/**
	 * Ԥ�㶨��ID
	 */
	public static final String F_BUDGET_ID = "budget_id"; //$NON-NLS-1$

	/**
	 * ��������ID
	 */
	public static final String F_WORK_DEFINITON_ID = "workd_id"; //$NON-NLS-1$

	/**
	 * ��׼��������ѡ�䣬ȷ�������ͽ������Ƿ����
	 */
	public static final String F_STANDARD_OPTION_SET = "standardset"; //$NON-NLS-1$

	/**
	 * ��Ʒ���ͣ�����ѡ�䣬ȷ�������ͽ������Ƿ����
	 */
	public static final String F_PRODUCTTYPE_OPTION_SET = "producttype"; //$NON-NLS-1$

	/**
	 * ��Ŀ���ͣ�����ѡ�䣬ȷ�������ͽ������Ƿ����
	 */
	public static final String F_PROJECTTYPE_OPTION_SET = "projecttype"; //$NON-NLS-1$

	/**
	 * �ύ���̵Ķ���
	 */
	public static final String F_WF_COMMIT = "wf_commit"; //$NON-NLS-1$

	/**
	 * ���̶����е�ָ��
	 */
	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment"; //$NON-NLS-1$

	/**
	 * �����Ƿ�����
	 */
	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated"; //$NON-NLS-1$

	public static final String POSTFIX_ACTIVATED = "_activated"; //$NON-NLS-1$

	public static final String POSTFIX_ASSIGNMENT = "_assignment"; //$NON-NLS-1$

	/**
	 * ������̶���
	 */
	public static final String F_WF_CHANGE = "wf_change"; //$NON-NLS-1$

	/**
	 * �����Ƿ�����
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated"; //$NON-NLS-1$

	/**
	 * ������̶����е�ָ��
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment"; //$NON-NLS-1$


	public static final String F_ACTIVATED = "activated"; //$NON-NLS-1$

	/**
	 * ������ʾͼ��
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_TEMPLATE_16);
	}

	/**
	 * ������ģ�����ݵ����ݿ���
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID, new ObjectId());// ��ҪԤ��ID,��������get_id()ȡ�����ǿ�

		if (getValue(F_WORK_DEFINITON_ID) == null) {
			BasicDBObject wbsRootData = new BasicDBObject();
			wbsRootData.put(WorkDefinition.F_WORK_TYPE, new Integer(
					WorkDefinition.WORK_TYPE_PROJECT));
			wbsRootData.put(WorkDefinition.F_DESC, getDesc());
			wbsRootData.put(WorkDefinition.F_PROJECT_TEMPLATE_ID, get_id());
			ObjectId wbsRootId = new ObjectId();
			wbsRootData.put(WorkDefinition.F__ID, wbsRootId);
			wbsRootData.put(WorkDefinition.F_ROOT_ID, wbsRootId);

			WorkDefinition wbsRoot = ModelService.createModelObject(
					wbsRootData, WorkDefinition.class);
			wbsRoot.doInsert(context);

			setValue(ProjectTemplate.F_WORK_DEFINITON_ID, wbsRoot.get_id());
		}

		if (getValue(F_BUDGET_ID) == null) {
			BudgetItem biRoot = BudgetItem.COPY_DEFAULT_BUDGET_ITEM();
			biRoot.setValue(BudgetItem.F_PROJECTTEMPLATE_ID, get_id());
			biRoot.doInsert(context);

			setValue(ProjectTemplate.F_BUDGET_ID, biRoot.get_id());
		}

		/*
		 * *****************************************************************************
		 * zhonghua 2013/9/16
		 * 
		 * [bug:18] ��Ŀģ�� ������Ŀģ����Զ��ڽ�ɫ������ϵͳ��ɫ����Ŀ����������Ŀ�۲��ߡ�
		 * 
		 * ����û���Զ���Ӹý�ɫ�����̺�WBS���޷����øý�ɫ
		 * 
		 * �Զ���Ӹý�ɫ�󣬸ý�ɫӦ������ɾ�����������
		 * 
		 * ���Ҹý�ɫ�ĳ�Ա����Ŀ�����˱���ͬ��
		 * 
		 * ͬʱ��ҪΪ��Ŀģ��Ľ�ɫ��ӽ���У�飬��ֹ����ϵͳ�����Ľ�ɫID
		 */
		RoleDefinition projMgr = makeRoleDefinition(null);
		projMgr.setValue(RoleDefinition.F_ROLE_NUMBER,
				RoleDefinition.ROLE_PROJECT_MANAGER_ID);
		projMgr.setValue(RoleDefinition.F_DESC,
				RoleDefinition.ROLE_PROJECT_MANAGER_TEXT);
		projMgr.doInsert(context);

		projMgr = makeRoleDefinition(null);
		projMgr.setValue(RoleDefinition.F_ROLE_NUMBER,
				RoleDefinition.ROLE_PROJECT_GUEST_ID);
		projMgr.setValue(RoleDefinition.F_DESC,
				RoleDefinition.ROLE_PROJECT_GUEST_ID);

		/*
		 * *****************************************************************************
		 */

		super.doInsert(context);
	}

	/**
	 * ɾ��ģ��
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		if (isActivated()) {
			throw new Exception(Messages.get().ProjectTemplate_0);
		}

		// ɾ��Ԥ���
		doRemoveBudgetItemInternal();

		// ɾ����������
		doRemoveWorkDefinitionsInternal();

		// ɾ�������ﶨ��
		doRemoveDeliverableDefinitionsInternal();

		// ɾ����ɫ����
		doRemoveRoleDefinitionInternal();

		super.doRemove(context);
	}

	/**
	 * ��ģ���Ƿ��Ѿ�����
	 */
	public boolean isActivated() {
		IActivateSwitch adapter = getAdapter(IActivateSwitch.class);
		return adapter.isActivated();
	}

	/**
	 * ɾ��ģ���еĽ����ﶨ��
	 */
	private void doRemoveDeliverableDefinitionsInternal() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_DELIEVERABLE_DEFINITION);
		col.remove(new BasicDBObject().append(
				DeliverableDefinition.F_PROJECTTEMPLATE_ID, get_id()));
	}

	/**
	 * ɾ��ģ��Ľ�ɫ����
	 */
	private void doRemoveRoleDefinitionInternal() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_DEFINITION);
		col.remove(new BasicDBObject().append(
				DeliverableDefinition.F_PROJECTTEMPLATE_ID, get_id()));
	}

	/**
	 * ɾ��ģ���й�������
	 */
	private void doRemoveWorkDefinitionsInternal() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK_DEFINITION);
		col.remove(new BasicDBObject().append(
				WorkDefinition.F_PROJECT_TEMPLATE_ID, get_id()));
	}
	
	@Override
	public boolean canEdit(IContext context) {
		if(isActivated()){
			return false;
		}
		return super.canEdit(context);
	}

	/**
	 * ɾ��ģ���е�Ԥ�㶨��
	 */
	private void doRemoveBudgetItemInternal() {
		Object bioid = getValue(F_BUDGET_ID);
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_BUDGET_ITEM);
		col.remove(new BasicDBObject().append(F__ID, bioid));
	}

	/**
	 * �½���ɫ����
	 * 
	 * @param roled
	 *            ,��ɫ����
	 * @return RoleDefinition
	 */
	public RoleDefinition makeRoleDefinition(RoleDefinition roled) {
		if (roled == null) {
			BasicDBObject data = new BasicDBObject();
			roled = ModelService.createModelObject(data, RoleDefinition.class);
		}
		roled.setValue(RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id());
		return roled;
	}

	/**
	 * ������֯��ɫ
	 * 
	 * @param role
	 * @return RoleDefinition
	 */
	public RoleDefinition makeOrganizationRole(Role role) {
		RoleDefinition roled = ModelService
				.createModelObject(RoleDefinition.class);
		roled.setValue(RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id());
		roled.setValue(RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id());
		return roled;
	}

	/**
	 * �½�ǰ���ù�ϵ����
	 * 
	 * @return WorkDefinitionConnection
	 */
	public WorkDefinitionConnection makeWorkDefinitionConnection() {
		WorkDefinitionConnection wdc = ModelService
				.createModelObject(WorkDefinitionConnection.class);
		wdc.setValue(WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID, get_id());
		return wdc;
	}

	/**
	 * �ж�ģ���еĽ�ɫ�����Ƿ��Ǵ���֯��ɫ����
	 * 
	 * @param role
	 * @return
	 */
	public boolean hasOrganizationRole(Role role) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_DEFINITION);
		long count = col.count(new BasicDBObject().append(
				RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id()).append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id()));
		return count != 0;
	}

	/**
	 * ���ر�׼��
	 * 
	 * @return List
	 */
	public List<?> getStandardOptionSet() {
		return (List<?>) getValue(F_STANDARD_OPTION_SET);
	}

	/**
	 * ���ز�Ʒ���ͼ���
	 * 
	 * @return List
	 */
	public List<?> getProductOptionSet() {
		return (List<?>) getValue(F_PRODUCTTYPE_OPTION_SET);
	}

	/**
	 * ������Ŀ���ͼ���
	 * 
	 * @return List
	 */
	public List<?> getProjectOptionSet() {
		return (List<?>) getValue(F_PROJECTTYPE_OPTION_SET);
	}

	/**
	 * ����ģ�������֯
	 * 
	 * @return Organization
	 */
	public Organization getOrganization() {
		ObjectId orgId = (ObjectId) getValue(F_ORGANIZATION_ID);
		Assert.isNotNull(orgId);
		return ModelService.createModelObject(Organization.class, orgId);
	}

	/**
	 * ����ģ���е�����Ԥ�㶨��
	 * 
	 * @return
	 */
	public List<PrimaryObject> getBudgetItems() {
		return getRelationById(F__ID, BudgetItem.F_PROJECTTEMPLATE_ID,
				BudgetItem.class);
	}

	/**
	 * ����ģ���е����н�ɫ����
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getRoleDefinitions() {
		return getRelationById(F__ID, RoleDefinition.F_PROJECT_TEMPLATE_ID,
				RoleDefinition.class);
	}

	/**
	 * 
	 * ����ģ���еĳ�������������й������� 
	 * 
	 * @return
	 */
	public List<PrimaryObject> getWorkDefinitions() {
		BasicDBObject query = new BasicDBObject().append(
				WorkDefinition.F_PROJECT_TEMPLATE_ID, getValue(F__ID)).append(
				WorkDefinition.F_ROOT_ID,
				new BasicDBObject().append("$ne", getWBSRoot().get_id())); //$NON-NLS-1$
		return getRelationByCondition(WorkDefinition.class, query);
	}

	/**
	 * ����ģ���е����н����ﶨ��
	 * 
	 * @return
	 */
	public List<PrimaryObject> getDeliverableDefinitions() {
		return getRelationById(F__ID,
				DeliverableDefinition.F_PROJECTTEMPLATE_ID,
				DeliverableDefinition.class);
	}

	/**
	 * ����ģ���е�����ǰ���ù�ϵ
	 * 
	 * @return
	 */
	public List<PrimaryObject> getWorkConnections() {
		return getRelationById(F__ID, WorkConnection.F_PROJECT_ID,
				WorkConnection.class);
	}

	/**
	 * ������Ŀģ���WBS�ṹ����������
	 * 
	 * @return WorkDefinition
	 */
	public WorkDefinition getWBSRoot() {
		ObjectId workDefinitionid = (ObjectId) getValue(F_WORK_DEFINITON_ID);
		return ModelService.createModelObject(WorkDefinition.class,
				workDefinitionid);
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ProjectTemplate_1;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IProcessControl.class)) {
			return (T) new ProcessControl(this){
				@Override
				protected Class<? extends PrimaryObject> getRoleDefinitionClass() {
					return RoleDefinition.class;
				}
			};
		}else if(adapter.equals(IActivateSwitch.class)){
			return (T) new ActivateSwitch(this);
		}
		return super.getAdapter(adapter);
	}

	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// ȡ����ɫָ��
		DBObject data = (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
	}

}
