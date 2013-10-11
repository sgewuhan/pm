package com.sg.business.project.menu;

import com.sg.widgets.viewer.EditableControledMenuProvider;

public class ProjectBulletinMenuProvider extends EditableControledMenuProvider {

	@Override
	protected String getUneditableMenuId() {
		return null;
	}

	@Override
	protected String getEditableMenuId() {
		return "popup:menu.project.bulletinboard";
	}

}
