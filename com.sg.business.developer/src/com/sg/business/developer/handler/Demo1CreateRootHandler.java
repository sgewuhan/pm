package com.sg.business.developer.handler;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.ModelService;
import com.sg.business.developer.model.Demo1;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.CTreeViewer;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class Demo1CreateRootHandler implements ICreateEditorDelegator {

	@Override
	public DataObjectEditor create(IStructuredSelection selection,
			String editorId, ColumnViewer viewer) throws Exception {
		Demo1 root = ModelService.createModelObject(Demo1.class);
		ViewerControl vc = ((CTreeViewer)viewer).getViewerControl();
		root.addEventListener(vc);
		
		DataObjectEditor editor = DataObjectEditor.open(root, editorId, true, null);
		return editor;
	}

}
