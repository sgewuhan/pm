package com.sg.business.project.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.widgets.part.IEditablePart;

public class SettingHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(part instanceof IEditablePart){
			String editorId = event.getParameter("command.worksset"); //$NON-NLS-1$
			String editable = event.getParameter("projectworksprogram"); //$NON-NLS-1$
			Boolean _editable = null;
			if("true".equalsIgnoreCase(editable)){ //$NON-NLS-1$
				_editable = true;
			}else if("false".equalsIgnoreCase(editable)){ //$NON-NLS-1$
				_editable = false;
			}
			((IEditablePart)part).doEdit(editorId,editable);
		}
		return null;
	}

}
