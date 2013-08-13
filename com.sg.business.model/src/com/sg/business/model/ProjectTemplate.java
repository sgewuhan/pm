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
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectTemplate extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organization_id";

	public static final String F_ACTIVATED = "activated";

	public static final String F_BUDGET_ID = "budget_id";

	public static final String F_WORK_DEFINITON_ID = "workd_id";

	public static final String F_STANDARD_OPTION_SET = "standardset";

	public static final String F_PRODUCTTYPE_OPTION_SET = "producttype";

	public static final String F_PROJECTTYPE_OPTION_SET = "projecttype";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_TEMPLATE_16);
	}

	@Override
	protected void doInsert(IContext context) throws Exception {
		setValue(F__ID,new ObjectId());//需要预设ID,否则后面的get_id()取出的是空
		
		if (getValue(F_WORK_DEFINITON_ID) == null) {
			BasicDBObject wbsRootData = new BasicDBObject();
			wbsRootData.put(WorkDefinition.F_WORK_TYPE, new Integer(
					WorkDefinition.WORK_TYPE_PROJECT));
			wbsRootData.put(WorkDefinition.F_PROJECTTEMPLATE_ID, get_id());

			WorkDefinition wbsRoot = ModelService.createModelObject(
					wbsRootData, WorkDefinition.class);
			wbsRoot.doSave(new CurrentAccountContext());
			setValue(ProjectTemplate.F_WORK_DEFINITON_ID, wbsRoot.get_id());
		}

		if (getValue(F_BUDGET_ID) == null) {
			BudgetItem biRoot = BudgetItem.COPY_DEFAULT_BUDGET_ITEM();
			biRoot.setValue(WorkDefinition.F_PROJECTTEMPLATE_ID, get_id());

			biRoot.doSave(new CurrentAccountContext());
			setValue(ProjectTemplate.F_BUDGET_ID, biRoot.get_id());
		}

		super.doInsert(context);
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		// 删除预算根
		doRemoveBudgetItemInternal();
		super.doRemove(context);
	}

	private void doRemoveBudgetItemInternal() {
		Object bioid = getValue(F_BUDGET_ID);
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_BUDGET_ITEM);
		col.remove(new BasicDBObject().append(F__ID, bioid));
	}

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
}
