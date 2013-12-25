package com.sg.business.project.exportvaluedelegator;

import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.Work;

public class ExportShowLeverDesc implements IExportValueDelegator {

	public ExportShowLeverDesc() {
	}

	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		
		String dataRowValue = (String) dataRow.get(iColumnExportDefinition.getColumn());
		
		Object _id = dataRow.get("_id"); //$NON-NLS-1$
		if (_id instanceof ObjectId) {
			Work work = ModelService.createModelObject(Work.class,(ObjectId) _id);
			String wbsCode = work.getWBSCode();
			String[] split = wbsCode.split("\\."); //$NON-NLS-1$
			for(int i=0;i<split.length;i++){
				dataRowValue="   "+dataRowValue; //$NON-NLS-1$
			}
		}

		return dataRowValue;
	}

}
