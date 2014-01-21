package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class WorkMoveLeft extends AbstractNavigatorHandler {

	private static final String TITLE = Messages.get().WorkMoveLeft_0;

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(part.getSite().getShell(), TITLE, Messages.get().WorkMoveLeft_1,
				SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		try {
			PrimaryObject[] relativeObjects = ((AbstractWork) selected)
					.doMoveLeft(new CurrentAccountContext());

			TreeViewer viewer = (TreeViewer) vc.getViewer();
			Object[] expanded = viewer.getExpandedElements();

			for (int i = 0; i < relativeObjects.length; i++) {
				viewer.refresh(relativeObjects[i]);
			}

			Object[] newExpand = new Object[expanded.length + 1];
			System.arraycopy(expanded, 0, newExpand, 0, expanded.length);
			newExpand[expanded.length] = selected;
			viewer.setExpandedElements(newExpand);
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
