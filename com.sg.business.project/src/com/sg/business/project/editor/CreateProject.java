package com.sg.business.project.editor;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.user.UserSessionContext;
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
		AccountInfo ac = UserSessionContext.getAccountInfo();
		//默认的项目经理是当前的用户
		po.setValue(Project.F_CHARGER, ac.getUserId());
		DataObjectWizard.open(po, editorId, true, null);
		return null;
	}

}
