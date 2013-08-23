package com.sg.business.commons.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkflowDefLabelPro extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		PrimaryObject workd = (PrimaryObject)element;
		String key = getFieldName();
		Object data = workd.getValue(key);
		if (data instanceof DBObject) {
			String processName = (String) ((DBObject) data).get("processName");
			if(processName !=null){
				return processName;
			}
		}
		return super.getText(element);
	}
}
