package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

public class ViewProject extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			Project project = work.getProject();
			if (project == null) {
				return;
			}
			String pageid = event.getParameter("work.project.view");
			try {
				DataObjectEditor editor = DataObjectEditor.open(project,Project.EDITOR_CREATE_PLAN,
						false, null);
				if(pageid!=null){
					editor.setActivePage(pageid);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
