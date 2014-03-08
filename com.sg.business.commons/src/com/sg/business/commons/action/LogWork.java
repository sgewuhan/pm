package com.sg.business.commons.action;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Work;
import com.sg.business.model.WorksPerformence;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;

public class LogWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		CurrentAccountContext context = new CurrentAccountContext();
		String userid = context.getAccountInfo().getConsignerId();
		Date date = new Date();
		WorksPerformence po = work.getWorksPerformence(date, userid);

		if (po != null) {
			int ok = MessageUtil.showMessage(null,
					Messages.get(control.getDisplay()).AddWorkRecord_0,
					Messages.get(control.getDisplay()).AddWorkRecord_1,
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			if (ok != SWT.YES) {
				return;
			}
		} else {
			po = work.makeTodayWorksPerformence(userid);
		}

		try {
			DataObjectDialog.openDialog(po, "editor.create.workrecord", //$NON-NLS-1$
					true, null);
			pageReload();
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canEditWorkRecord(getContext());
	}

}
