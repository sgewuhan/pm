package com.tmt.pdm.dcpdm.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.sg.widgets.part.CurrentAccountContext;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;

public class DCPDMObjectSelectWizard extends Wizard {

	Set<String> docContainers = new HashSet<String>();
	HashMap<String,String> ouid_name = new HashMap<String,String>();
	ISelection selection;
	Set<String> allContainers = new HashSet<String>();
	private AdvanceSearchPage advanceSearchPage;
	Set<String> selectedObjectOuid = new HashSet<String>();
	
	
	@SuppressWarnings("unchecked")
	public DCPDMObjectSelectWizard() {
		String userId = new CurrentAccountContext().getConsignerId();
		List<?> code = DCPDMUtil
				.getDocumentAndDrawingContainerCode(userId);
		if(code!=null){
			docContainers.addAll((Collection<? extends String>) code);
			allContainers.addAll((Collection<? extends String>) code);
		}
		setWindowTitle("Ñ¡ÔñDCPDMµÄÍ¼ÎÄµµ");
	}

	@Override
	public void addPages() {
		addPage(new ConditionPage(this));
		addPage(new SearchPage(this));
		advanceSearchPage = new AdvanceSearchPage(this);
		addPage(advanceSearchPage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	public void setSelection(ISelection selection) {
		this.selection = selection;
		selectedObjectOuid.clear();
		if(selection!=null){
			Object[] input = ((IStructuredSelection)selection).toArray();
			for (int i = 0; i < input.length; i++) {
				String ouid = (String)((ArrayList<?>)input[i]).get(0);
				selectedObjectOuid.add(ouid);
			}
		}
		advanceSearchPage.setInput(selectedObjectOuid.toArray(new String[0]));
	}
	
	public Set<String> getSelectedObjectOuid() {
		return selectedObjectOuid;
	}
	
	public static Set<String> selectPDMObject(Shell shell){
		DCPDMObjectSelectWizard wiz = new DCPDMObjectSelectWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, wiz);
		if (WizardDialog.OK == wizardDialog.open()) {
			return wiz.getSelectedObjectOuid();
		}
		return null;
	}

}
