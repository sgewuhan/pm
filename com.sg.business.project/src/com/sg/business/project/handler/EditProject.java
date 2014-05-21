package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.IEditablePart;
import com.sg.widgets.viewer.ViewerControl;

public class EditProject extends AbstractNavigatorHandler {
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("请选中项目", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Project) {
			try {
				((IEditablePart)part).doEdit("project.systemadmin.editor", null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

}
