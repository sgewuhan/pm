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
import com.sg.business.resource.BusinessResource;

/**
 * 项目模板<p>
 * 项目模板由业务管理员创建，用于新建项目
 * @author jinxitao
 *
 */
public class ProjectTemplate extends PrimaryObject {

	/**
	 * 所属组织
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 是否启用
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * 预算定义ID
	 */
	public static final String F_BUDGET_ID = "budget_id";

	/**
	 * 工作定义ID
	 */
	public static final String F_WORK_DEFINITON_ID = "workd_id";

	/**
	 * 标准集，用于选配，确定工作和交付物是否必须
	 */
	public static final String F_STANDARD_OPTION_SET = "standardset";

	/**
	 * 产品类型，用于选配，确定工作和交付物是否必须
	 */
	public static final String F_PRODUCTTYPE_OPTION_SET = "producttype";

	/**
	 * 项目类型，用于选配，确定工作和交付物是否必须
	 */
	public static final String F_PROJECTTYPE_OPTION_SET = "projecttype";

	/**
	 * 项目提交流程
	 */
	public static final String F_WF_COMMIT = "wf_commit";
	
	public static final String F_WF_COMMIT_ASSIGNMENT = "wf_commit_assignment";
	
	public static final String F_WF_COMMIT_ACTIVATED = "wf_commit_activated";

	/**
	 * 项目变更流程
	 */
	public static final String F_WF_CHANGE = "wf_change";
	
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";
	
	/**
	 * 返回显示图标
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_TEMPLATE_16);
	}

	/**
	 * 插入新模板数据到数据库中
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID,new ObjectId());//需要预设ID,否则后面的get_id()取出的是空
		
		if (getValue(F_WORK_DEFINITON_ID) == null) {
			BasicDBObject wbsRootData = new BasicDBObject();
			wbsRootData.put(WorkDefinition.F_WORK_TYPE, new Integer(WorkDefinition.WORK_TYPE_PROJECT));
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

		super.doInsert(context);
	}

	/**
	 * 删除模板
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		if(isActivated()){
			throw new Exception("项目模板启用状态不可删除");
		}
		
		// 删除预算根
		doRemoveBudgetItemInternal();
		
		// 删除工作定义
		doRemoveWorkDefinitionsInternal();
		
		//删除交付物定义
		doRemoveDeliverableDefinitionsInternal();
		
		super.doRemove(context);
	}

	/**
	 * 该模板是否已经启用
	 */
	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}

	/**
	 * 删除模板中的交付物定义
	 */
	private void doRemoveDeliverableDefinitionsInternal() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_DELIEVERABLE_DEFINITION);
		col.remove(new BasicDBObject().append(DeliverableDefinition.F_PROJECTTEMPLATE_ID, get_id()));		
	}

	/**
	 * 删除模板中工作定义与交付物的关系
	 */
	private void doRemoveWorkDefinitionsInternal() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		col.remove(new BasicDBObject().append(WorkDefinition.F_PROJECT_TEMPLATE_ID, get_id()));
	}

	/**
	 * 删除模板中的预算定义
	 */
	private void doRemoveBudgetItemInternal() {
		Object bioid = getValue(F_BUDGET_ID);
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_BUDGET_ITEM);
		col.remove(new BasicDBObject().append(F__ID, bioid));
	}

	/**
	 * 新建角色定义
	 * @param roled
	 *        ,角色定义
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

	public RoleDefinition makeOrganizationRole(Role role) {
		RoleDefinition roled = ModelService
				.createModelObject(RoleDefinition.class);
		roled.setValue(RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id());
		roled.setValue(RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id());
		return roled;
	}
	

	public WorkDefinitionConnection makeWorkDefinitionConnection() {
		WorkDefinitionConnection wdc = ModelService.createModelObject(WorkDefinitionConnection.class);
		wdc.setValue(WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID, get_id());
		return wdc;
	}

	public boolean hasOrganizationRole(Role role) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_DEFINITION);
		long count = col.count(new BasicDBObject().append(
				RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id()).append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id()));
		return count != 0;
	}

	public List<?> getStandardOptionSet() {
		return (List<?>) getValue(F_STANDARD_OPTION_SET);
	}

	public List<?> getProductOptionSet() {
		return (List<?>) getValue(F_PRODUCTTYPE_OPTION_SET);
	}

	public List<?> getProjectOptionSet() {
		return (List<?>) getValue(F_PROJECTTYPE_OPTION_SET);
	}

	public Organization getOrganization() {
		ObjectId orgId = (ObjectId) getValue(F_ORGANIZATION_ID);
		Assert.isNotNull(orgId);
		return ModelService.createModelObject(Organization.class, orgId);
	}

	public List<PrimaryObject> getRoleDefinitions() {
		return getRelationById(F__ID, RoleDefinition.F_PROJECT_TEMPLATE_ID, RoleDefinition.class);
	}

	
	@Override
	public String getTypeName() {
		return "项目模板";
	}
}
