package com.sg.business.work.home.action;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.sg.business.model.Work;
import com.sg.business.work.home.WorkDetail;
import com.sg.widgets.part.editor.IDataObjectEditorAction;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.view.SideBarNavigator;

public abstract class AbstractWorkDetailPageAction implements IDataObjectEditorAction{
	@Override
	public boolean visiableWhen(PrimaryObjectEditorInput input) {
		return true;
	}

	@Override
	public boolean enableWhen(PrimaryObjectEditorInput input) {
		return true;
	}
	
	@Override
	public void run(PrimaryObjectEditorInput input, Control control) {
		run((Work)input.getData(),control);
	}

	protected abstract void run(Work data, Control control);
	
	protected void pageClear() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		SideBarNavigator view = (SideBarNavigator) page.findView("homenavigator");
		if(view!=null){
			view.doRefresh();
		}
		WorkDetail view2 = (WorkDetail) page.findView("pm2.work.detail");
		view2.cleanUI();
		view2.initContent();
	}
}
