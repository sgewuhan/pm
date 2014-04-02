package com.tmt.pdm.dcpdm.handler;

import java.util.Set;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;

import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IFieldActionHandler;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelectWizard;

public class SelectPDMObject implements IFieldActionHandler {

	public SelectPDMObject() {
	}

	@Override
	public Object run(IFormPart abstractFieldPart,
			PrimaryObjectEditorInput input) {

		AbstractFieldPart fieldPart = (AbstractFieldPart) abstractFieldPart;
		String ouid = (String) fieldPart.getValue();
		if (ouid != null) {
			int ret = MessageUtil
					.showMessage(
							fieldPart.getShell(),
							"ѡ��DCPDMͼֽ",
							"��ǰ��ͼֽ��¼�Ѿ�������DCPDM��ͼֽ����\n����ѡ�񽫸ı䵱ǰ��ͼֽ��¼�Ĺ�����ϵ��\n��ȷ��Ҫ�������µ�DCPDMͼֽ������",
							SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			if(ret!=SWT.OK){
				return null;
			}
		}

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		DCPDMObjectSelectWizard wiz = new DCPDMObjectSelectWizard();

		WizardDialog wizardDialog = new WizardDialog(shell, wiz);

		int ok = wizardDialog.open();
		if (ok == WizardDialog.OK) {
			Set<String> set = wiz.getSelectedObjectOuid();
			if (set.size() > 0) {
				ouid = set.toArray(new String[0])[0];
				fieldPart.setValue(ouid);
				fieldPart.setInputValue("pdm_ouid", ouid, fieldPart, true);
				fieldPart.presentValue();
				fieldPart.getForm().dirtyStateChanged();
			}
		}
		return null;
	}

}
