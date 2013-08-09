package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.resource.BusinessResource;

public class ProjectTemplate extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organization_id";

	public static final String F_ACTIVATED = "activated";

	public static final String F_BUDGET_ID = "budget_id";

	public static final String F_WORK_DEFINITON_ID = "workd_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_TEMPLATE_16);
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		//É¾³ýÔ¤Ëã¸ù
		doRemoveBudgetItemInternal();
		super.doRemove(context);
	}

	private void doRemoveBudgetItemInternal() {
		Object bioid = getValue(F_BUDGET_ID);
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_BUDGET_ITEM);
		col.remove(new BasicDBObject().append(F__ID, bioid));
	}

	public RoleDefinition makeRoleDefinition(RoleDefinition roled) {
		if(roled==null){
			BasicDBObject data = new BasicDBObject();
			roled = ModelService.createModelObject(data, RoleDefinition.class);
		}
		roled.setValue(RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id());
		return roled;
	}

	public RoleDefinition makeOrganizationRole(Role role) {
		RoleDefinition roled = ModelService.createModelObject(RoleDefinition.class);
		roled.setValue(RoleDefinition.F_ORGANIZATION_ROLE_ID,role.get_id());
		roled.setValue(RoleDefinition.F_PROJECT_TEMPLATE_ID, get_id());
		return roled;
	}
	
	
}
