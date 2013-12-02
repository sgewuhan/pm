package com.sg.business.pm2;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;

import com.mobnut.design.ext.PerspectiveEditorArea;

public class EditorArea extends PerspectiveEditorArea {


	@Override
	protected void creatEditorArea(Composite parent, IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		if(perspective.getId().equals("perspective.visualization")){
			parent.setLayout(new FormLayout());
			
			//
			
			
			
		}
	}

}
