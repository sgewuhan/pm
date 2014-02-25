package com.sg.business.work.home.action;

import org.eclipse.swt.widgets.Control;

import com.mobnut.db.model.IContext;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class AssignWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		String editorId = "editor.runtimereassignment";

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				editorId);

		if (conf != null) {
			try {
				DataObjectDialog d = DataObjectDialog.openDialog(work,
						(DataEditorConfigurator) conf, true, null,
						work.getLabel());
				if(DataObjectDialog.CANCEL!=d.getReturnCode()){
					pageClear();
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	@Override
	protected boolean visiableWhen(Work work) {
		IContext context = getContext();
		String userId = context.getAccountInfo().getConsignerId();
		String assignerId = work.getAssignerId();
		return userId.equals(assignerId)&&work.canEdit(context);
	}

}
