package com.sg.business.project.editor;

import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Project;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateProject implements ICreateEditorDelegator {


	@Override
	public IInputProvider create(IStructuredSelection selection,
			String editorId, ViewerControl viewerControl) throws Exception {
		Project po = ModelService.createModelObject(new BasicDBObject(), Project.class);
		String userId = new CurrentAccountContext().getUserId();
		//默认的项目经理是当前的用户
		po.setValue(Project.F_CHARGER, userId);
		po.addEventListener(viewerControl);
		DataObjectWizard w = DataObjectWizard.open(po, editorId, true, null);
		po.removeEventListener(viewerControl);
		return w;
	}

}
