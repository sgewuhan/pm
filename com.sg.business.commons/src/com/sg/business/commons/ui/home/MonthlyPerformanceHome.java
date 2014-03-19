package com.sg.business.commons.ui.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.editor.DataObjectView;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class MonthlyPerformanceHome extends DataObjectView {

	private MonthlyPerformanceHomePanel homePanel;

	@Override
	protected void initContent() {
		cleanUI();
		content.setLayout(new GridLayout());
		homePanel = new MonthlyPerformanceHomePanel(content);
		super.goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		homePanel = new MonthlyPerformanceHomePanel(content);
		content.layout(false, false);
		super.goHome();
	}

	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (!part.getSite().getId().equals("homenavigator2")) {
			return false;
		}
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
			return false;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		if (element instanceof Work || element instanceof WorkDefinition
				|| element instanceof Message) {
			return true;
		} else if (element instanceof PrimaryObject) {
			if (element instanceof ProjectProvider
					|| ((PrimaryObject) element)
							.getAdapter(ProjectProvider.class) != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected PrimaryObjectEditorInput getInput(PrimaryObject primary) {
		if (primary instanceof ProjectProvider) {
			return super.getInput(primary);
		} else {
			ProjectProvider pp = primary.getAdapter(ProjectProvider.class);
			if (pp != null) {
				return super.getInput(pp);
			}
		}
		return super.getInput(primary);
	}

	@Override
	public void doRefresh() {
		if (isHome) {
			homePanel.doRefresh();
		} else {
			loadMaster();
		}
	}

}
