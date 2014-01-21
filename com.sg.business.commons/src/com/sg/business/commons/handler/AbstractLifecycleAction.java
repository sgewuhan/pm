package com.sg.business.commons.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MessageBox;
import com.sg.widgets.part.ObjectInformationView;
import com.sg.widgets.viewer.ViewerControl;

public abstract class AbstractLifecycleAction extends AbstractNavigatorHandler {

	public AbstractLifecycleAction() {
		super();
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof ILifecycle) {
			ILifecycle lc = (ILifecycle) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			String name = "";
			try {
				name = command.getName();
			} catch (NotDefinedException e1) {
			}
			try {
				execute(lc, context, name, selected, vc);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	public int execute(ILifecycle lc, IContext context, String name,
			PrimaryObject work, ViewerControl vc) throws Exception {
		List<Object[]> message = checkBeforeExecute(lc, context);
		if (hasError(message)) {
			MessageUtil.showToast(null, name,
					Messages.get().AbstractLifecycleAction_0, SWT.ICON_ERROR);
			showCheckMessages(message, work);
		} else {
			if (message != null && message.size() > 0) {
				MessageBox mb = MessageUtil.createMessageBox(null, name,
						Messages.get().AbstractLifecycleAction_1
								+ "\n\n" //$NON-NLS-2$ //$NON-NLS-1$
								+ Messages.get().AbstractLifecycleAction_3
								+ "\n" //$NON-NLS-2$ //$NON-NLS-1$
								+ Messages.get().AbstractLifecycleAction_5
								+ "\n" //$NON-NLS-2$ //$NON-NLS-1$
								+ Messages.get().AbstractLifecycleAction_7,
						SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
				mb.setButtonText(SWT.YES,
						Messages.get().AbstractLifecycleAction_8);
				mb.setButtonText(SWT.NO,
						Messages.get().AbstractLifecycleAction_9);
				mb.setButtonText(SWT.CANCEL,
						Messages.get().AbstractLifecycleAction_10);
				int result = mb.open();
				if (result == SWT.NO) {
					return result;
				} else if (result == SWT.CANCEL) {
					showCheckMessages(message, work);
					return result;
				}
			}
			execute(lc, context);
			if (vc != null) {
				vc.getViewer().refresh(work);
				vc.getViewer().setSelection(null);
			}
			return SWT.YES;
		}
		return SWT.NO;
	}

	private boolean hasError(List<Object[]> message) {
		if (message == null || message.size() == 0) {
			return false;
		}
		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) != null && message.get(i).length > 2) {
				Object icon = message.get(i)[2];
				if (icon instanceof Integer
						&& ((Integer) icon).intValue() == SWT.ICON_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	protected abstract List<Object[]> checkBeforeExecute(ILifecycle lc,
			IContext context) throws Exception;

	protected abstract void execute(ILifecycle lc, IContext context)
			throws Exception;

	protected void showCheckMessages(List<Object[]> message,
			PrimaryObject selected) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			ObjectInformationView view = (ObjectInformationView) page.showView(
					"com.sg.widgets.objectinfo", "" + selected.hashCode() + "_" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ getClass().getName(),
					IWorkbenchPage.VIEW_ACTIVATE);
			view.setInput(message, Messages.get().AbstractLifecycleAction_14
					+ selected);
		} catch (PartInitException e) {
			MessageUtil.showToast(e);
		}

	}

}