package com.sg.business.model.input;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.model.EditorInputFactory;

public class ProjectProviderEditorInputFactory extends EditorInputFactory {

	public ProjectProviderEditorInputFactory(PrimaryObject primaryObject) {
		super(primaryObject);
	}

	@Override
	protected String getEditorId(PrimaryObject primaryObject, Object data) {
		return "editor.visualization.projectset.home";
	}
	
}
