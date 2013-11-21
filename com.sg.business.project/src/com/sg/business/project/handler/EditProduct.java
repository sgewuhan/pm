package com.sg.business.project.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.widgets.part.IEditablePart;

public class EditProduct extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(part instanceof IEditablePart){
			((IEditablePart)part).doEdit("project.product.editor",null,null,null);
		}
		return null;
	}
}
