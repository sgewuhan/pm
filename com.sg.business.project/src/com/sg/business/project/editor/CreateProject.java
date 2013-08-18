package com.sg.business.project.editor;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Project;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ICreateEditorDelegator;

public class CreateProject implements ICreateEditorDelegator {


	@Override
	public DataObjectEditor create(IStructuredSelection selection,
			String editorId, ColumnViewer viewer) throws Exception {
		Project po = ModelService.createModelObject(new BasicDBObject(), Project.class);
		DataObjectWizard.open(po, editorId, true, null);
		return null;
	}

}
