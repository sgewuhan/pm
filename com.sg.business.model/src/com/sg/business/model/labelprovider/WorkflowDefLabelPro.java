package com.sg.business.model.labelprovider;

import com.mongodb.DBObject;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkflowDefLabelPro extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		WorkDefinition workd = (WorkDefinition)element;
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
