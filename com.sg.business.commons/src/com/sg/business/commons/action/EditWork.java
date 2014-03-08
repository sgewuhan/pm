package com.sg.business.commons.action;

import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class EditWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		String editorId = work.getEditorId();

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
		return work.canEdit(getContext());
	}
}
