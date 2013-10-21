package com.sg.business.commons.handler;

import java.util.List;

import org.eclipse.swt.SWT;

import com.mobnut.db.model.IContext;
import com.sg.business.model.ILifecycle;
import com.sg.widgets.MessageUtil;

public class LifeCycleActionFinish extends AbstractLifecycleAction {

	@Override
	protected List<Object[]> checkBeforeExecute(ILifecycle lc, IContext context)
			throws Exception {
		return lc.checkFinishAction(context);
	}

	@Override
	protected void execute(ILifecycle lc, IContext context) throws Exception {
		int yes = MessageUtil.showMessage(null, "���",
				""+lc+"\n��ɲ��������ɻظ�����ȷ������Ҫִ����ɲ�����\n\nѡ��YES������ɲ���\nѡ��NOȡ����ɲ���",
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		if (yes == SWT.YES) {
			lc.doFinish(context);
		}
	}

}
