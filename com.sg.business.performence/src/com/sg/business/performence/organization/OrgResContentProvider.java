package com.sg.business.performence.organization;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;

public class OrgResContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (( List<?>)inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Organization){
			List<PrimaryObject> suborg = ((Organization) parentElement).getChildrenOrganization();
			List<PrimaryObject> member = ((Organization) parentElement).getUser();
			Object[] result = new Object[suborg.size()+member.size()];
			System.arraycopy(suborg.toArray(), 0, result, 0, suborg.size());
			System.arraycopy(member.toArray(), 0, result, suborg.size(), member.size());
			
			return result;
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Organization;
	}

}
