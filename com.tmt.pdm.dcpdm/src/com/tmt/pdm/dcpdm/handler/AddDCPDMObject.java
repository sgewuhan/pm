package com.tmt.pdm.dcpdm.handler;

import java.util.Set;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mongodb.BasicDBList;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelectWizard;

public class AddDCPDMObject implements IAddTableItemHandler {

	public AddDCPDMObject() {
	}

	@Override
	public boolean addItem(BasicDBList inputData, AbstractFieldPart fieldPart) {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		DCPDMObjectSelectWizard wiz = new DCPDMObjectSelectWizard();

		WizardDialog wizardDialog = new WizardDialog(shell, wiz);

		int ok = wizardDialog.open();
		if (ok == WizardDialog.OK) {
			Set<String> set = wiz.getSelectedObjectOuid();
			if (set.size() > 0) {
				BasicDBList newValue = new BasicDBList();
				newValue.addAll(inputData);
				newValue.removeAll(set);
				newValue.addAll(set);
				fieldPart.setValue(newValue);
				fieldPart.setInputValue("pdm_ouids", newValue, fieldPart, true);
				fieldPart.setDirty(true);
				inputData.clear();
				inputData.addAll(newValue);
				return true;
			}
		}
		return false;
	}

}
