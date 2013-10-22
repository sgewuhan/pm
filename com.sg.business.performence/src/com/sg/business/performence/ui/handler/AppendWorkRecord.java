package com.sg.business.performence.ui.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.WorksPerformence;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

public class AppendWorkRecord extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		
		BasicDBObject data = new BasicDBObject();
		WorksPerformence po = ModelService.createModelObject(data, WorksPerformence.class);
		try {
			DataObjectDialog.openDialog(po, "editor.append.workrecord", Boolean.TRUE, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
