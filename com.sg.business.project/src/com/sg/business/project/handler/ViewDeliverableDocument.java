package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

public class ViewDeliverableDocument extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof Deliverable){
			Deliverable deliverable = (Deliverable) selected;
			Document doc = deliverable.getDocument();
			if(doc==null){
				MessageUtil.showToast("选择的交付物不包含文档", SWT.ICON_WARNING);
			}else{
				try {
					DataObjectEditor.open(doc, doc.getDefaultEditorId(), false, null);
				} catch (Exception e) {
					e.printStackTrace();
					MessageUtil.showToast(e);
				}
			}
		}
	}

}
