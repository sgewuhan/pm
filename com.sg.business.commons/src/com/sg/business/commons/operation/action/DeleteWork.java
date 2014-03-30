package com.sg.business.commons.operation.action;

import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class DeleteWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		try {
			work.doRemove(getContext());
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canDelete(getContext());
	}

}
