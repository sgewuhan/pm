package com.sg.business.commons.ui.viewer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sg.business.model.ProjectBudget;

public class ProjectBudgetTreeContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return ((ProjectBudget)element).hasChildren();
	}
	
	@Override
	public Object getParent(Object element) {
		return ((ProjectBudget)element).getParent();
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[])inputElement ;
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return ((ProjectBudget)parentElement).getChildren();
	}
}
