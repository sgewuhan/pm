package com.tmt.jszx.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectReviewAuditSaveOfJSZX implements IDataObjectDialogCallback {

	public ProjectReviewAuditSaveOfJSZX() {
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		List<String> userList = new ArrayList<String>();
		Object listValue = taskform.getValue("reviewer_list");
		if (listValue instanceof List) {
			userList.addAll((Collection<? extends String>) listValue);
		}
		Object value = taskform.getValue("act_rule_prj_admin");
		if (value instanceof String) {
			userList.add((String) value);
		}

		if (userList.size() > 0) {
			Work work = taskform.getWork();
			work.doAddParticipateList(userList);
		}
		return true;
	}

}
