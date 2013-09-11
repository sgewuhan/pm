package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;

public class StartTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof Work){
			Work work = (Work) selected;
			try {
				work.doStartTask(Work.F_WF_EXECUTE,new CurrentAccountContext());
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("请选择工作后执行启动流程操作", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(event);
	}

}
