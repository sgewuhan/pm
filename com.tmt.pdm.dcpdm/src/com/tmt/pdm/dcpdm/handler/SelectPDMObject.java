package com.tmt.pdm.dcpdm.handler;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;

import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IFieldActionHandler;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelector2;

public class SelectPDMObject implements IFieldActionHandler {

	public SelectPDMObject() {
	}

	@Override
	public Object run(IFormPart abstractFieldPart,
			PrimaryObjectEditorInput input) {
		AbstractFieldPart fieldPart = (AbstractFieldPart)abstractFieldPart;
	
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		DCPDMObjectSelector2 wiz = new DCPDMObjectSelector2();

		WizardDialog wizardDialog = new WizardDialog(shell, wiz);
		
		wizardDialog.open();
		
		fieldPart.presentValue();
		fieldPart.getForm().dirtyStateChanged();
		return null;
	}

}
