package com.sg.business.model.input;

import com.sg.business.model.Project;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProjectEditorInputFactory implements IEditorInputFactory {

	private Project project;

	public ProjectEditorInputFactory(Project project) {
		this.project = project;
	}

	@Override
	public PrimaryObjectEditorInput getInput(Object data) {
		DataEditorConfigurator conf = getEditorConfig(data);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
				project, conf, null);
		return editorInput;
	}

	@Override
	public DataEditorConfigurator getEditorConfig(Object data) {
		String editorId;
		if ("create".equals(data)) {
			editorId = "project.editor.wizard";
		} else {
			editorId = "project.editor";
		}
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		return conf;
	}

}
