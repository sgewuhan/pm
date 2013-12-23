package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.AbstractWork;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class WorkMoveDown extends AbstractNavigatorHandler {

	private static final String TITLE = Messages.get().WorkMoveDown_0;

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(part.getSite().getShell(), TITLE, Messages.get().WorkMoveDown_1,
				SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		try {
			PrimaryObject[] relativeObjects = ((AbstractWork) selected)
					.doMoveDown(new CurrentAccountContext());

			ColumnViewer viewer = vc.getViewer();
			for (int i = 0; i < relativeObjects.length; i++) {
				viewer.refresh(relativeObjects[i]);
			}
			viewer.setSelection(new StructuredSelection(selected), true);

			/**********************************************************
			 * [BUG:20] 将更改消息传递到编辑器
			 */
			sendNavigatorActionEvent(part, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}

	}

}
