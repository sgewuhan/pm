package com.sg.business.mangement.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.file.ExcelExportJob;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.NavigatorAction;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.registry.config.NavigatorConfigurator;

public class ExportProjectTemplateWBS extends NavigatorAction {

	public ExportProjectTemplateWBS() {
		setText(Messages.get().ExportWBS_0);
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
	}

	@Override
	protected void execute() throws Exception {
		NavigatorControl control = getNavigator();
		StructuredViewer viewer = control.getViewer();
		if (control.canExport()) {
			ProjectTemplate projectTemplate = (ProjectTemplate) getInput().getData();
			WorkDefinition wbsRoot = projectTemplate.getWBSRoot();
			doExport(viewer.getControl().getDisplay(),control.getConfigurator(),wbsRoot);
		}

	}
	
	
	private void doExport(Display display, NavigatorConfigurator configurator, WorkDefinition wbsRoot) {
			ExcelExportJob job = new ExcelExportJob("WBS"); //$NON-NLS-1$
			job.setColumnExportDefinition(configurator.getExportColumns());
			job.setInput(getExportData(wbsRoot));
			job.setUser(true);
			job.setFormat(false);
			job.start(display);
	}

	private List<PrimaryObject> getExportData(WorkDefinition wbsRoot) {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		result.add(wbsRoot);
		List<PrimaryObject> children = wbsRoot.getChildrenWork();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				result.addAll(getExportData((WorkDefinition) children.get(i)));
			}
		}
		return result;
	}

}
