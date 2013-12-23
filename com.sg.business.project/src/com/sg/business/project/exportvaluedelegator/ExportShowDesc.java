package com.sg.business.project.exportvaluedelegator;

import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class ExportShowDesc implements IExportValueDelegator {

	public ExportShowDesc() {
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {

		System.out.println(dataRow.values());
		
		Object object = dataRow.get("userid"); //$NON-NLS-1$
		if (object instanceof String) {
			User user = UserToolkit.getUserById((String) object);
			System.out.println("    " + user.getLabel()); //$NON-NLS-1$

			return "    " + user.getLabel(); //$NON-NLS-1$
		}
		
		object = dataRow.get("role_id"); //$NON-NLS-1$
		if (object instanceof ObjectId) {
			Role role = ModelService.createModelObject(Role.class,
					(ObjectId) object);
				return role.getLabel();
			
			
		}
		

		object=dataRow.get("_id"); //$NON-NLS-1$
		if(object instanceof ObjectId){
			ProjectRole projectRole = ModelService.createModelObject(ProjectRole.class,
					(ObjectId) object);
			return projectRole.getLabel();
		}
		return null;
		
	}

}
