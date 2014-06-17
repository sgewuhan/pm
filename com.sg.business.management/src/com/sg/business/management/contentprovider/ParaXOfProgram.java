package com.sg.business.management.contentprovider;

import java.util.List;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sg.business.model.WorkTimeProgram;

public class ParaXOfProgram implements ITreeContentProvider {

	public ParaXOfProgram() {
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		WorkTimeProgram workTimeProgram=(WorkTimeProgram) parentElement;
		Object paraXs = workTimeProgram.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		if(paraXs instanceof BasicBSONList){
			return ((BasicBSONList) paraXs).toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof WorkTimeProgram;
	}

}
