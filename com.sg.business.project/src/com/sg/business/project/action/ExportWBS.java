package com.sg.business.project.action;


import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;
import com.sg.widgets.part.NavigatorControl;

public class ExportWBS extends NavigatorAction {

	public ExportWBS() {
		setText("µ¼³ö");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
	}
	
	@Override
	protected void execute() throws Exception {
		NavigatorControl control = getNavigator();
		if(control.canExport()){
		
			control.doExport();
//			Project project = (Project) getInput().getData();
			
		}
		
	}

}
