package com.sg.business.project.exportvaluedelegator;

import java.util.Map;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class ExportUserId2UserFullName implements IExportValueDelegator {

	public ExportUserId2UserFullName() {
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		String dataRowValue = (String) dataRow.get(iColumnExportDefinition.getColumn());
		if(dataRowValue!=null){
			User user = UserToolkit.getUserById(dataRowValue);
			return user.getLabel();
		}
		return null;
	}

}
