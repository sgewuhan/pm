package com.sg.business.commons.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.work.view.ProjectDeliverableView;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class OpenProjectDeliverable extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {

		if (selected instanceof Work) {
			Work work = (Work) selected;
			PrimaryObject rootWork = work.getRoot();
			List<PrimaryObject> result = getProjectDeliverableDocuments(
					rootWork, new ArrayList<PrimaryObject>());
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				ProjectDeliverableView part = (ProjectDeliverableView) page
						.showView("work.view.projectDeliverable");
				part.setInput(result);
			} catch (PartInitException e) {
				MessageUtil.showToast(e);
				e.printStackTrace();
			}
		}

	}

	private List<PrimaryObject> getProjectDeliverableDocuments(PrimaryObject work, ArrayList<PrimaryObject> arrayList) {
		List<PrimaryObject> workList = ((Work) work).getChildrenWork();
		for (PrimaryObject po : workList) {
			List<PrimaryObject> docList = ((Work) po).getDeliverableDocuments();
			arrayList.addAll(docList);
			getProjectDeliverableDocuments(po, arrayList);
		}

		return arrayList;
	}

}
