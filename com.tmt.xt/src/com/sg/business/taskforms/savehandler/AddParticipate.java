package com.sg.business.taskforms.savehandler;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class AddParticipate implements IDataObjectDialogCallback {

	public AddParticipate() {
	}

	@Override
	public boolean okPressed() {
		return false;
	}

	@Override
	public void cancelPressed() {

	}

	@Override
	public boolean needSave() {
		return true;
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		Object listValue = taskform.getValue("reviewer_list"); //$NON-NLS-1$
		if (listValue instanceof List) {
			List<?> userList = (List<?>) listValue;
			Object projectid = taskform.getProcessInstanceVarible("projectid", //$NON-NLS-1$
					new CurrentAccountContext());
			if (projectid instanceof String) {
				Project project = ModelService.createModelObject(Project.class,
						new ObjectId((String) projectid));
				project.doAddParticipate(userList.toArray(new String[0]));
			}
		}
		return true;
	}

}
