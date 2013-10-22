package com.scom.sg.widgets.part.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.view.NavigatorPart;
import com.sg.widgets.viewer.CTreeViewer;


public class TreeNavigator extends NavigatorPart {

	public TreeNavigator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
        IToolBarManager manager = getToolBarManager();
        Action action = new Action(){
			@Override
			public void run() {
				CTreeViewer viewer = ((CTreeViewer)getNavigator().getViewer());
				viewer.expandAll();
			                     }
                                                     };
        action.setText("展开所有层次");
        action.setImageDescriptor(Widgets.getImageDescriptor(ImageResource.EXPAND_24));
        manager.add(action);
        action = new Action(){
			@Override
			public void run() 
       {
				CTreeViewer viewer = ((CTreeViewer)getNavigator().getViewer());
				viewer.collapseAll();
			}
        };
        action.setText("收起所有层次");
        action.setImageDescriptor(Widgets.getImageDescriptor(ImageResource.COLLAPSE_24));
        manager.add(action);
	}



	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
