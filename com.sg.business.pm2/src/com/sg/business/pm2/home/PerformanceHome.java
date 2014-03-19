package com.sg.business.pm2.home;

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

public class PerformanceHome extends DataObjectView {

	private PerformanceHomePanel homePanel;

	@Override
	protected void initContent() {
		cleanUI();
		content.setLayout(new GridLayout());
		homePanel = new PerformanceHomePanel(content);
		super.goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		homePanel = new PerformanceHomePanel(content);
		content.layout(false,false);
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
		if( element instanceof Work || element instanceof WorkDefinition
				|| element instanceof Message){
			return true;
		}else if(element instanceof PrimaryObject){
			PrimaryObject po = (PrimaryObject) element;
			ProjectProvider pp = po.getAdapter(ProjectProvider.class);
			if(pp!=null){
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected PrimaryObjectEditorInput getInput(PrimaryObject primary) {
		ProjectProvider pp = primary.getAdapter(ProjectProvider.class);
		if(pp!=null){
			return super.getInput(pp);
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
