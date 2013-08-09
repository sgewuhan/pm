package com.sg.business.model.editingsupport;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerColumn;

import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ActivatedEditingSupportProvider implements IEditingSupportor {

	public ActivatedEditingSupportProvider() {
	}

	@Override
	public EditingSupport createEditingSupport(ColumnViewer viewer,
			ViewerColumn viewerColumn, ColumnConfigurator configurator) {
		return new ActivatedEditingSupport(viewer,configurator);
	}

}
