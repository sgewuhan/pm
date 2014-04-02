package com.sg.business.commons.operation.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class EditDeliverableDocument extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Deliverable) {
			String editable = (String) parameters
					.get("deliverabledocument.editable"); //$NON-NLS-1$
			Boolean _editable = null;
			if ("true".equalsIgnoreCase(editable)) { //$NON-NLS-1$
				_editable = true;
			} else if ("false".equalsIgnoreCase(editable)) { //$NON-NLS-1$
				_editable = false;
			}
			Deliverable deliverable = (Deliverable) selected;
			Document doc = deliverable.getDocument();
			if (doc == null) {
				MessageUtil.showToast(Messages.get().EditDeliverableDocument_2, SWT.ICON_WARNING);
			} else {
				try {
					DataObjectEditor.open(doc, doc.getDefaultEditorId(),
							_editable, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}
	}

}
