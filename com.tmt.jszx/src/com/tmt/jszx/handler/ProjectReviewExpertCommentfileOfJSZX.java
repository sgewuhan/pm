package com.tmt.jszx.handler;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectReviewExpertCommentfileOfJSZX implements
		IDataObjectDialogCallback {

	public ProjectReviewExpertCommentfileOfJSZX() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

}
