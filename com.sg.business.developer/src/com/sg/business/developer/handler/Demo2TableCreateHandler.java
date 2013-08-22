package com.sg.business.developer.handler;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.sg.business.developer.model.Demo1;
import com.sg.business.developer.model.Demo2;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class Demo2TableCreateHandler implements ICreateEditorDelegator {


	@Override
	public DataObjectEditor create(IStructuredSelection selection,
			String editorId, ViewerControl viewerControl) throws Exception {
		Demo1 demo1 = (Demo1) viewerControl.getMaster();
		Demo2 demo2 = ModelService.createModelObject(Demo2.class);
		demo2.addEventListener(viewerControl);
		demo1.makeDemo2(demo2);
		return DataObjectEditor.open(demo2, editorId, true, null);
	}

}
