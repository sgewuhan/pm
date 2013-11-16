package com.tmt.tb.editor;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectChangeConfirmProgramSaveHandler implements IEditorSaveHandler {

	public ProjectChangeConfirmProgramSaveHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		Object ecn = taskform.getValue("ecn");
		if(ecn instanceof List<?>){
			throw new Exception("请设置变更内容");
			
		}else{
			throw new Exception("请设置变更内容");
		}
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

}
