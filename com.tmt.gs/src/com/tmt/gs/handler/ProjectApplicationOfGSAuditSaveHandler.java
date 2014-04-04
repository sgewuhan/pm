package com.tmt.gs.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectApplicationOfGSAuditSaveHandler implements
		IDataObjectDialogCallback {

	public ProjectApplicationOfGSAuditSaveHandler() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		List<String> userList = new ArrayList<String>();
        TaskForm taskform = (TaskForm) input.getData();
        Object standardset = taskform.getValue("prj_manager"); //$NON-NLS-1$
        Object value = taskform.getValue("standardset");
        Object projecttype = taskform.getValue("projecttype");
        
        if(value instanceof String){
        	String userid=(String) value;
        	 if(standardset instanceof String){
        		 String standard=(String) standardset;
             	if(projecttype instanceof String){
             		String prjtype=(String) projecttype;
             		userList.add(0,userid);
                	userList.add(1,standard);
                	userList.add(2,prjtype);
             	}
             	
             }
        	
        	Work work = taskform.getWork();
			work.doAddParticipateList(userList);
        }
       
		return true;
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

}
