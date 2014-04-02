package com.sg.business.commons.operation.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;

public class DeleteWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		try {
			Shell shell = control.getShell();
			String desc = work.getDesc();
			int yes = MessageUtil.showMessage(
					shell,
					Messages.get().RemoveWork_1 + desc,
					Messages.get().RemoveWork_4 + desc
							+ Messages.get().RemoveWork_6
							+ Messages.get().RemoveWork_7, SWT.YES | SWT.NO
							| SWT.ICON_QUESTION);
			if (yes == SWT.YES) {
				work.doRemove(getContext());
				pageClear();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canDelete(getContext());
	}

}
