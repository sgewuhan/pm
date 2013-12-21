package com.sg.business.project.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.file.ExcelExportJob;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.project.nls.Messages;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.registry.config.NavigatorConfigurator;

public class ExportTeam extends NavigatorAction {

	private Project project=null;
	public ExportTeam() {
		setText(Messages.get().ExportTeam_0);
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
	}

	@Override
	protected void execute() throws Exception {
		NavigatorControl control = getNavigator();
		project = (Project) getInput().getData();
		if(project==null){
			return;
		}
		StructuredViewer viewer = control.getViewer();
		if (control.canExport()) {
			Object input = viewer.getInput();
			if(input instanceof List){
				doExport(viewer.getControl().getDisplay(),control.getConfigurator(),project);
			}
			
		}

	}
	
	
	private void doExport(Display display, NavigatorConfigurator configurator,Project project) {
			ExcelExportJob job = new ExcelExportJob("TEAM"); //$NON-NLS-1$
			job.setColumnExportDefinition(configurator.getExportColumns());
			job.setInput(getExportData(project));
			job.setUser(true);
			job.setFormat(false);
			job.start(display);
	}

	private List<PrimaryObject> getExportData(Project project) {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> projectRole = project.getProjectRole();
		for(PrimaryObject rolepo:projectRole){
			result.add(rolepo);
			List<PrimaryObject> assignment = ((ProjectRole)rolepo).getAssignment();
			for(PrimaryObject assignmentpo:assignment){
				String userid = ((AbstractRoleAssignment)assignmentpo).getUserid();
				User user = UserToolkit.getUserById(userid);
				result.add(user);
			}
			
			
		}
		
	
		return result;
	}

}
