package com.sg.business.project.menu;

import com.sg.widgets.viewer.EditableControledMenuProvider;

public class ProjectWBSMenuProvider extends EditableControledMenuProvider {

	@Override
	protected String getUneditableMenuId() {
		return null;
	}

	@Override
	protected String getEditableMenuId() {
		return "popup:project.wbs";
	}

}
