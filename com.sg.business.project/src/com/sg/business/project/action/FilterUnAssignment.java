package com.sg.business.project.action;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectRole;
import com.sg.business.project.nls.Messages;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;

public class FilterUnAssignment extends NavigatorAction {

	private boolean filtered;
	private ViewerFilter filter;

	public FilterUnAssignment() {
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_USER_24));
		setText(Messages.get().FilterUnAssignment_0);
		this.filtered = false;
		filter = new ViewerFilter(){
			
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if(element instanceof ProjectRole){
					ProjectRole prole = (ProjectRole) element;
					List<PrimaryObject> assg = prole.getAssignment();
					return assg==null||assg.size() ==0; 
				}
				return false;
			}
			
		};
	}

	@Override
	public void execute() throws Exception {
		
		if(!filtered){
			getNavigator().getViewer().setFilters(new ViewerFilter[]{filter});
			filtered = true;
		}else{
			getNavigator().getViewer().resetFilters();
			filtered = false;

		}
		
	}

}
