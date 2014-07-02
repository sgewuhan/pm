package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;

public class WorkTimeProgramAndTypeLabel extends ColumnLabelProvider {

	
	
	@Override
	public String getText(Object element) {
		if(element instanceof DBObject){
			return (String) ((DBObject) element).get(WorkTimeProgram.F_DESC);
		}else if(element instanceof WorkTimeProgram){
			return (String) ((WorkTimeProgram) element).getValue(WorkTimeProgram.F_DESC);
		}
		return super.getText(element);
	}

}
