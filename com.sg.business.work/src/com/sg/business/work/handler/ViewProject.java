package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class ViewProject extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> para, IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			Project project = work.getProject();
			if (project == null) {
				return;
			}
			String pageid = (String) para.get("work.project.view");
			try {
				DataObjectEditor editor = DataObjectEditor.open(project,
						Project.EDITOR_CREATE_PLAN, false, null);
				if (pageid != null) {
					editor.setActivePage(pageid);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}


}
