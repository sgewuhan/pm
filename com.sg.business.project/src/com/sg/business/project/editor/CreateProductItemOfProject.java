package com.sg.business.project.editor;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateProductItemOfProject implements ICreateEditorDelegator {

	@Override
	public IInputProvider create(IStructuredSelection selection,
			String editorId, ViewerControl viewerControl) throws Exception {
		ProductItem po = ModelService.createModelObject(new BasicDBObject(), ProductItem.class);
		PrimaryObject parent = viewerControl.getMaster();
		
		ObjectId projectId;

		if (parent instanceof Project) {
			projectId = ((Project) parent).get_id();
			po.setValue(ProductItem.F_PROJECT_ID, projectId);
		} 
		
		DataObjectDialog.openDialog(po, editorId, true, null);
		viewerControl.doReloadData();
		return null;
	}

}
