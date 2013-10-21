package com.sg.business.commons.handler;

import java.util.List;

import org.eclipse.swt.SWT;

import com.mobnut.db.model.IContext;
import com.sg.business.model.ILifecycle;
import com.sg.widgets.MessageUtil;

public class LifeCycleActionCancel extends AbstractLifecycleAction {

	@Override
	protected List<Object[]> checkBeforeExecute(ILifecycle lc, IContext context)
			throws Exception {
		return lc.checkCancelAction(context);
	}

	@Override
	protected void execute(ILifecycle lc, IContext context) throws Exception {
		int yes = MessageUtil.showMessage(null, "中止",
				""+lc+"\n中止操作将不可回复，请确认您将要执行中止操作。\n\n选择YES继续中止操作\n选择NO取消中止操作",
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		if (yes == SWT.YES) {
			lc.doCancel(context);
		}
	}

}
