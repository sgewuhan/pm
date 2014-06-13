package com.sg.business.performence.user;

import org.eclipse.jface.viewers.TreeViewer;
//import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class EmployeeWorksFromWork extends ViewPart {

	public EmployeeWorksFromWork() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite=new Composite(parent, SWT.NONE);
		TreeViewer treeViewer=new TreeViewer(composite);
		treeViewer.setAutoExpandLevel(3);
//		TreeViewerColumn vColumn=new TreeViewerColumn(treeViewer, SWT.FULL_SELECTION);
	}

	
	
	@Override
	public void setFocus() {

	}

}
