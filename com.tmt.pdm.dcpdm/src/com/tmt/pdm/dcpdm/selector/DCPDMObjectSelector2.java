package com.tmt.pdm.dcpdm.selector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.wizard.Wizard;

import com.sg.widgets.part.CurrentAccountContext;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;

public class DCPDMObjectSelector2 extends Wizard {

	Set<String> docContainers = new HashSet<String>();


	@SuppressWarnings("unchecked")
	public DCPDMObjectSelector2() {
		String userId = new CurrentAccountContext().getConsignerId();
		List<?> code = DCPDMUtil
				.getDocumentAndDrawingContainerCode(userId);
		if(code!=null){
			docContainers.addAll((Collection<? extends String>) code);
		}
	}

	@Override
	public void addPages() {
		addPage(new ConditionPage());
		addPage(new SearchPage());

	}

	@Override
	public boolean performFinish() {
		return false;
	}



}
