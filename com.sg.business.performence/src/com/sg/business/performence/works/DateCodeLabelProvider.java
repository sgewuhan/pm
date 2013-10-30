package com.sg.business.performence.works;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.WorksPerformence;

public class DateCodeLabelProvider extends ColumnLabelProvider {


	@Override
	public String getText(Object element) {
		if(element instanceof WorksPerformence){
			return ((WorksPerformence) element).getLogDate();
		}
		
		return "";
	}
}
