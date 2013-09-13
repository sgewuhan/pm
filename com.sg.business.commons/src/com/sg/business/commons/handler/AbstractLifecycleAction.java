package com.sg.business.commons.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.ObjectInformationView;
import com.sg.widgets.viewer.ViewerControl;

public abstract class AbstractLifecycleAction extends AbstractNavigatorHandler {

	public AbstractLifecycleAction() {
		super();
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof ILifecycle) {
			ViewerControl vc = getCurrentViewerControl(event);
			ILifecycle lc = (ILifecycle) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			try {
				List<Object[]> message = checkBeforeExecute(lc, context);
				if (message != null && message.size() > 0) {
					showCheckMessages(message, selected);
				}
				if (!hasError(message)) {
					execute(lc, context);
					vc.getViewer().update(selected, null);
				} else {
					String name = event.getCommand().getName();
					MessageUtil.showToast(null,name,"存在一些错误，请查看检查视图，完成修改后重新执行",
							SWT.ICON_INFORMATION);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	private boolean hasError(List<Object[]> message) {
		if (message == null|| message.size() == 0) {
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
					"com.sg.widgets.objectinfo", "" + selected.hashCode() + "_"
							+ getClass().getName(),
					IWorkbenchPage.VIEW_ACTIVATE);
			view.setInput(message, "检查" + selected);
		} catch (PartInitException e) {
			MessageUtil.showToast(e);
		}

	}

}