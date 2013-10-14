package com.sg.business.work.handler;

import java.util.Date;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.model.WorksPerformence;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

public class AddWorkRecord extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			String userid = context.getAccountInfo().getConsignerId();
			Date date = new Date();
			WorksPerformence po = work.getWorksPerformence(date, userid);

			if (po != null) {
				int ok = MessageUtil.showMessage(null, "填报工作记录",
						"您今天已经填报了工时记录，您希望打开编辑吗？", SWT.ICON_QUESTION | SWT.YES
								| SWT.NO);
				if(ok!=SWT.YES){
					return;
				}
			}else{
				po = work.makeWorksPerformence(userid);
			}

			try {
				DataObjectDialog.openDialog(po, "editor.create.workrecord",
						true, null);
				ViewerControl vc = getCurrentViewerControl(event);
				vc.getViewer().update(work, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
