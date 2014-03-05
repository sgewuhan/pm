package com.sg.business.pm2.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

public class BasicHome extends PrimaryObjectDetailFormView {

	private GenericHomePanel genericHomePanel;

	@Override
	protected void initContent() {
		cleanUI();
		content.setLayout(new GridLayout());
		genericHomePanel = new GenericHomePanel(content);
		super.goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		genericHomePanel = new GenericHomePanel(content);
		content.layout(false,false);
		super.goHome();
	}

	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (!part.getSite().getId().equals("homenavigator")) {
			return false;
		}
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
			return false;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		return element instanceof Work || element instanceof WorkDefinition
				|| element instanceof Message;
	}

	@Override
	public void doRefresh() {
		if (isHome) {
			genericHomePanel.doRefresh();
		} else {
			loadMaster();
		}
	}

}
