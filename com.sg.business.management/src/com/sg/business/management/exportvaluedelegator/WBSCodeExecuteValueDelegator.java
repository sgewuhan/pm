package com.sg.business.management.exportvaluedelegator;

import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.WorkDefinition;

public class WBSCodeExecuteValueDelegator implements IExportValueDelegator {

	public WBSCodeExecuteValueDelegator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		Object object = dataRow.get(iColumnExportDefinition.getColumn());
		if (object instanceof ObjectId) {
			WorkDefinition workDefinition = ModelService.createModelObject(
					WorkDefinition.class, (ObjectId) object);
			return workDefinition.getWBSCode();
		}
		return null;
	}

}
