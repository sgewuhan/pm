package com.tmt.tb.editor;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mongodb.DBObject;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.tmt.tb.nls.Messages;

public class ProjectChangeConfirmProgramSaveHandler implements
		IEditorSaveHandler {

	public ProjectChangeConfirmProgramSaveHandler() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {

		TaskForm taskform = (TaskForm) input.getData();
		String choice = (String) taskform.getValue("choice"); //$NON-NLS-1$
		taskform.setProcessInputValue("choice", choice); //$NON-NLS-1$
		if ("不通过".equals(choice)) { //$NON-NLS-1$
			return true;
		}
		Object ecn = taskform.getValue("ecn"); //$NON-NLS-1$
		if (ecn instanceof List<?>) {
			List<?> ecnlist = (List<?>) ecn;
			String var;
			for (Object obj : ecnlist) {
				Work work = ModelService.createModelObject((DBObject) obj,
						Work.class);

				Boolean noskip = (Boolean) work
						.getValue(Work.F_INTERNAL_DEFAULTSELECTED);
				if (Boolean.TRUE.equals(noskip)) {
					Object value = work.getValue(Work.F_PLAN_START);
					if (value == null) {
						throw new Exception(
								Messages.get().ProjectChangeConfirmProgramSaveHandler_4
										+ work);
					}
					value = work.getValue(Work.F_PLAN_FINISH);
					if (value == null) {
						throw new Exception(
								Messages.get().ProjectChangeConfirmProgramSaveHandler_5
										+ work);
					}
					value = work.getValue(Work.F_CHARGER);
					if (value == null) {
						throw new Exception(
								Messages.get().ProjectChangeConfirmProgramSaveHandler_6
										+ work);
					}
					checkDuration(work);

					var = (String) work.getValue("chargerpara"); //$NON-NLS-1$
					taskform.setProcessInputValue(var, value);
					var = (String) work.getValue("noskippara"); //$NON-NLS-1$
					taskform.setProcessInputValue(var, "是"); //$NON-NLS-1$

				} else {
					var = (String) work.getValue("noskippara"); //$NON-NLS-1$
					taskform.setProcessInputValue(var, "否"); //$NON-NLS-1$
				}

			}

		}
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	private void checkDuration(Work work) throws Exception {
		Date start = work.getPlanStart();
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish = work.getPlanFinish();
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start != null && finish != null) {
			// 检查是否合法
			if (start.after(finish)) {
				throw new Exception(
						Messages.get().ProjectChangeConfirmProgramSaveHandler_12);
			}

		}
	}
}
