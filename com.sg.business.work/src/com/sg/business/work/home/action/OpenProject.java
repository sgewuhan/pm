package com.sg.business.work.home.action;

import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;

public class OpenProject extends AbstractWorkDetailPageAction {

	@Override
	protected void run(Work data, Control control) {
		Project project = data.getProject();
		if (project == null) {
			return;
		}
		
		
		try {
			DataObjectEditor
					.open(project, "project.editor", true, null); //$NON-NLS-1$
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

	}
	

}
