package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class OpenWorkRecord extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;

			try {
				DataObjectEditor editor = DataObjectEditor.open(work,
						"view.work", false, null);
				editor.setActivePage("workrecord");
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}


}
