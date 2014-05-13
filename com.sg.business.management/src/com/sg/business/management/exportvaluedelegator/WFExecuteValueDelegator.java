package com.sg.business.management.exportvaluedelegator;

import java.util.Map;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mongodb.DBObject;

public class WFExecuteValueDelegator implements IExportValueDelegator {

	public WFExecuteValueDelegator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		Object dataRowValue =  dataRow.get(iColumnExportDefinition.getColumn());
		if (dataRowValue instanceof DBObject) {
			String processName = (String) ((DBObject) dataRowValue).get("processName"); //$NON-NLS-1$
			if(processName !=null){
				return processName;
			}
		}
		
		return null;
		
	}

}
