package com.sg.business.project.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.file.ExcelExportJob;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.registry.config.NavigatorConfigurator;

public class ExportWBS extends NavigatorAction {

	public ExportWBS() {
		setText("µ¼³ö");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
	}

	@Override
	protected void execute() throws Exception {
		NavigatorControl control = getNavigator();
		StructuredViewer viewer = control.getViewer();
		if (control.canExport()) {
			Project project = (Project) getInput().getData();
			Work wbsRoot = project.getWBSRoot();
			doExport(viewer.getControl().getDisplay(),control.getConfigurator(),wbsRoot);
		}

	}
	
	
	private void doExport(Display display, NavigatorConfigurator configurator, Work wbsRoot) {
			ExcelExportJob job = new ExcelExportJob("WBS");
			job.setColumnExportDefinition(configurator.getExportColumns());
			job.setInput(getExportData(wbsRoot));
			job.setUser(true);
			job.setFormat(false);
			job.start(display);
	}

	private List<PrimaryObject> getExportData(Work work) {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		result.add(work);
		List<PrimaryObject> children = work.getChildrenWork();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				result.addAll(getExportData((Work) children.get(i)));
			}
		}
		return result;
	}

	

}
