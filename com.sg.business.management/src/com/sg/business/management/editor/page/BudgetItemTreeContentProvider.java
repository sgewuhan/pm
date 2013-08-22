package com.sg.business.management.editor.page;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sg.business.model.BudgetItem;

public class BudgetItemTreeContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return ((BudgetItem)element).hasChildren();
	}
	
	@Override
	public Object getParent(Object element) {
		return ((BudgetItem)element).getParent();
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[])inputElement ;
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return ((BudgetItem)parentElement).getChildren();
	}
}
