package com.sg.business.project.menu;

import com.sg.widgets.viewer.EditableControledMenuProvider;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectFolderMenuProvider extends EditableControledMenuProvider {

	@Override
	protected String getUneditableMenuId(ViewerControl viewerControl) {
		return null;
	}

	@Override
	protected String getEditableMenuId(ViewerControl viewerControl) {
		return "popup:project.folder";
	}

}
