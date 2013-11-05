package com.sg.business.work.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowPreview extends AbstractHandler {

	private static final String WORKFLOW_DOCUMENT_PREVIEWER = "workflow.document.previewer";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		IViewPart view = page.findView(WORKFLOW_DOCUMENT_PREVIEWER);
		if(view==null){
			try {
				page.showView(WORKFLOW_DOCUMENT_PREVIEWER);
			} catch (PartInitException e) {
			}
		}else{
			page.hideView(view);
		}
		return null;
	}

}
