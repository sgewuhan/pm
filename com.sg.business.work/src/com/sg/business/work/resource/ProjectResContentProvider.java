package com.sg.business.work.resource;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sg.business.model.Project;
import com.sg.business.model.toolkit.UserToolkit;

public class ProjectResContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<?>) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Project) {
			List<?> idlist = ((Project) parentElement).getParticipatesIdList();
			if(idlist!=null){
				Object[] result = new Object[idlist.size()];
				for (int i = 0; i < result.length; i++) {
					result[i] = UserToolkit.getUserById((String) idlist.get(i));
				}
				return result;
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Project;
	}

}
