package com.sg.business.performence.project;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Project;
import com.sg.business.model.User;
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
				User[] result = new User[idlist.size()];
				for (int i = 0; i < result.length; i++) {
					result[i] = UserToolkit.getUserById((String) idlist.get(i));
					if(result[i] == null){
						result[i] = ModelService.createModelObject(User.class);
						result[i].setValue(User.F_USER_ID, "?");
						result[i].setValue(User.F_USER_NAME, "?");
					}
					result[i].setValue("$projectid", ((Project) parentElement).get_id());
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
