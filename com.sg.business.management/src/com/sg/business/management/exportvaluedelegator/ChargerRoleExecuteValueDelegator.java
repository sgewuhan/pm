package com.sg.business.management.exportvaluedelegator;

import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.RoleDefinition;

public class ChargerRoleExecuteValueDelegator implements IExportValueDelegator {

	public ChargerRoleExecuteValueDelegator() {
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		Object object = dataRow.get(iColumnExportDefinition.getColumn());
		if(object instanceof ObjectId){
			RoleDefinition roleDefinition = ModelService.createModelObject(RoleDefinition.class, (ObjectId)object);
			return roleDefinition.getLabel();
			
		}
		return null;
	}

}
