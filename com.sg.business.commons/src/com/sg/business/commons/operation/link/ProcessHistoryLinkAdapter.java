package com.sg.business.commons.operation.link;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProcessHistoryLinkAdapter implements SelectionListener {
	
	public ProcessHistoryLinkAdapter() {
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == RWT.HYPERLINK) {
			try {
				String _id = event.text.substring(
						event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
						event.text.indexOf("@")); //$NON-NLS-1$
				String action = event.text.substring(event.text
						.indexOf("@") + 1); //$NON-NLS-1$
				if ("open".equals(action)) { //$NON-NLS-1$
					UserTask userTask = ModelService.createModelObject(
							UserTask.class, new ObjectId(_id));
					doOpenTaskForm(userTask);
				}
			} catch (Exception e) {
			}
		}

	}
	
	private static void doOpenTaskForm(UserTask userTask) {
		String editorId = (String) userTask.getStringValue(TaskForm.F_EDITOR);
		if (editorId == null) {
			return;
		}
		TaskForm taskForm = userTask.makeTaskForm();

		Iterator<String> iter = userTask.get_data().keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (key.startsWith("form_")) { //$NON-NLS-1$
				String nkey = key.substring(5);
				taskForm.setValue(nkey, userTask.getValue(key));
			}
		}
		DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		try {
			DataObjectDialog.openDialog(taskForm, ec, false, null, Messages.get().ProcessHistoryUIToolkit_5);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {

	}

}
