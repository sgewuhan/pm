package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class DeleteProject extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selection == null || selection.isEmpty()) {
			return;
		}
		DBCollection projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		Object[] array = selection.toArray();
		for (Object object : array) {
			if (object instanceof Project) {
				Project project = (Project) object;
				try {
					project.doRemove(new CurrentAccountContext());
				} catch (Exception e) {
				}
				projectCol.remove(new BasicDBObject().append(Project.F__ID,
						project.get_id()));
			}
		}
	}

}
