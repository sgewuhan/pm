package com.sg.business.project.exportvaluedelegator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class ExportUserIdArray2UserFullNameArray implements
		IExportValueDelegator {

	public ExportUserIdArray2UserFullNameArray() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(Map<String, Object> dataRow,
			IColumnExportDefinition iColumnExportDefinition) {
		List<String> participate=new ArrayList<String>();
		Object dataRowValue =  dataRow.get(iColumnExportDefinition.getColumn());
		if(dataRowValue instanceof List){
			List<String> list = ((List<String>) dataRowValue);
			for(String userid:list){
				User user = UserToolkit.getUserById(userid);
				participate.add(user.getLabel());
			}
		}
		return participate;
	}

}
