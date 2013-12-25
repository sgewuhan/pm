package com.tmt.tb.handler;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectChangeAddParticipate implements IEditorSaveHandler {

	public ProjectChangeAddParticipate() {
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
		Object value = taskform.getValue("project"); //$NON-NLS-1$

		if (value instanceof ObjectId) {
			Project pro = ModelService.createModelObject(Project.class,
					(ObjectId) value);
			String chargerId = pro.getChargerId();
			Work work = taskform.getWork();
			work.addParticipate(chargerId);
			try {
				work.doSave(new CurrentAccountContext());
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
		return true;
	}


}
