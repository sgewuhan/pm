package com.sg.business.commons.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
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
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof ILifecycle) {
			ILifecycle lc = (ILifecycle) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			try {
				List<Object[]> message = checkBeforeExecute(lc, context);
				String name = command.getName();
				if (hasError(message)) {
					MessageUtil.showToast(null, name,
							"��鷢����һЩ������鿴�����������޸ĺ�����ִ�С�", SWT.ICON_ERROR);
					showCheckMessages(message, selected);
				} else {
					if (message != null && message.size() > 0) {
						MessageBox mb = MessageUtil.createMessageBox(null,
								name, "��鷢����һЩ���⣬��鿴�������" + "\n\n"
										+ "ѡ�� \"����\" ���Ӿ�����Ϣ��������" + "\n"
										+ "ѡ�� \"��ֹ\" ִֹͣ�б��β���" + "\n"
										+ "ѡ�� \"�鿴\" ȡ�����β������鿴�����",
								SWT.ICON_WARNING | SWT.YES | SWT.NO
										| SWT.CANCEL);
						mb.setButtonText(SWT.YES, "����");
						mb.setButtonText(SWT.NO, "��ֹ");
						mb.setButtonText(SWT.CANCEL, "�鿴");
						int result = mb.open();
						if (result == SWT.NO) {
							return;
						} else if (result == SWT.CANCEL) {
							showCheckMessages(message, selected);
							return;
						}
					}
					execute(lc, context);
					vc.getViewer().refresh(selected);
					vc.getViewer().setSelection(null);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
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
					"com.sg.widgets.objectinfo", "" + selected.hashCode() + "_"
							+ getClass().getName(),
					IWorkbenchPage.VIEW_ACTIVATE);
			view.setInput(message, "���" + selected);
		} catch (PartInitException e) {
			MessageUtil.showToast(e);
		}

	}

}