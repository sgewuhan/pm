package com.tmt.gs.handler;

import org.bson.types.ObjectId;
import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.value.IFieldActionHandler;

public class OpenBudgetOfGS implements IFieldActionHandler {

	public OpenBudgetOfGS() {
	}

	@Override
	public Object run(IFormPart abstractFieldPart,
			PrimaryObjectEditorInput input) {
		PrimaryObject po = input.getData();
		if (po instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) po;
			try {
				String project_id = (String) taskForm
						.getProcessInstanceVarible(
								"project_id", new CurrentAccountContext()); //$NON-NLS-1$
				ObjectId projectid = new ObjectId(project_id);
				Project project = ModelService.createModelObject(Project.class,
						projectid);
				DataObjectDialog.openDialog(project,
						Project.EDITOR_PROJECT_FINANCIAL, false, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
