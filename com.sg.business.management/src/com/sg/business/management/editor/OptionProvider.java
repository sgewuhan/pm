package com.sg.business.management.editor;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;

class OptionProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((BasicDBList) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		DBObject parent = ((DBObject) parentElement);
		Object options = parent.get(WorkTimeProgram.F_TYPE_OPTIONS);
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