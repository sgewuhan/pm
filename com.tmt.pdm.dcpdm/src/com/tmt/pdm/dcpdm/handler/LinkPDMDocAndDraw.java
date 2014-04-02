package com.tmt.pdm.dcpdm.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelectWizard;

public class LinkPDMDocAndDraw extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		Set<String> result = DCPDMObjectSelectWizard.selectPDMObject(part.getSite().getShell());
		if(result== null){
			return;
		}
		
		Iterator<String> iter = result.iterator();
		while(iter.hasNext()){
			String ouid = iter.next();
			try {
				DCPDMUtil.createDocument2((Work) selected, ouid, new CurrentAccountContext());
			} catch (Exception e) {
			}
		}
	}

//	@Override
//	protected void execute(PrimaryObject selected, IWorkbenchPart part,
//			ViewerControl vc, Command command, Map<String, Object> parameters,
//			IStructuredSelection selection) {
//		String userId = new CurrentAccountContext().getConsignerId();
//
//		Shell shell = part.getSite().getShell();
//		boolean result;
//		try {
//			result = DCPDMUtil.createDocumentFromDCPDM(userId,(Work) selected, shell);
//			if (result) {
//				vc.doRefresh();
//			}
//		} catch (Exception e) {
//			MessageUtil.showToast(e);
//		}
//	}

	
}
