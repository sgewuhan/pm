package com.sg.business.commons.ui.viewer;

import java.util.List;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;

public class ParaXOptionProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List<?>){
			return ((List<?>) inputElement).toArray();
		}else if(inputElement instanceof Object[]){
			return (Object[]) inputElement;
		}else{
			return new Object[]{inputElement};
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		DBObject parent = ((DBObject) parentElement);
		Object options = parent.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
		if (options instanceof BasicBSONList) {
			return ((BasicBSONList) options).toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}