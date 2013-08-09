package com.sg.business.project.demo;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.ModelService;
import com.mongodb.DBObject;
import com.sg.business.model.Project;


public class Handler1 extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection sel = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object adaptable = sel.getFirstElement();
		Project project = ModelService.createModelObject((DBObject)adaptable, Project.class);
		Object project2 = Platform.getAdapterManager().getAdapter(adaptable, Project.class);
		return null;
	}

}
