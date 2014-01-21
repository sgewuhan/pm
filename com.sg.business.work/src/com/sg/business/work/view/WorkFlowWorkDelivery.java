package com.sg.business.work.view;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.Document;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.view.TreeNavigator;

public class WorkFlowWorkDelivery extends TreeNavigator {

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master instanceof Work) {
			String workname = master.getLabel();
			setPartName(workname + Messages.get().WorkFlowWorkDelivery_0);
		} else if (master instanceof IWorkRelative) {
			String workname = ((IWorkRelative) master).getWork().getLabel();
			setPartName(workname + Messages.get().WorkFlowWorkDelivery_0);
		} else {
			setPartName(originalPartName);
		}
	}

	@Override
	public boolean canCreate() {
		return master != null;
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canEdit() {
		return selectDocument();
	}

	@Override
	public boolean canRefresh() {
		return super.canRefresh();
	}

	@Override
	public boolean canRead() {
		return selectDocument();
	}

	private boolean selectDocument() {
		IStructuredSelection selection = navi.getViewerControl().getSelection();
		if (selection != null && !selection.isEmpty()) {
			Object selected = selection.getFirstElement();
			return selected instanceof Document;
		}
		return false;
	}

	@Override
	public void doCreate() {
		return;
	}

}
