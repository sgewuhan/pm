package com.tmt.pdm.dcpdm.selector;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

import com.sg.widgets.part.CurrentAccountContext;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;

public class DCPDMObjectSelectWizard extends Wizard {

	Set<String> docContainers = new HashSet<String>();
	HashMap<String,String> ouid_name = new HashMap<String,String>();
	ISelection selection;
	Set<String> allContainers = new HashSet<String>();;
	private AdvanceSearchPage advanceSearchPage;


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
		addPage(new ConditionPage());
		addPage(new SearchPage());
		advanceSearchPage = new AdvanceSearchPage();
		addPage(advanceSearchPage);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
		if(selection!=null){
			advanceSearchPage.setInput(((IStructuredSelection)selection).toArray());
		}else{
			advanceSearchPage.setInput(null);
		}
	}
	
	public ISelection getSelection() {
		return selection;
	}



}
