package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

public class OpenWorkRecord extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			Work work = (Work) selected;

			try {
			DataObjectEditor editor = DataObjectEditor.open(work, "view.work", false, null);
			editor.setActivePage("workrecord");
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
