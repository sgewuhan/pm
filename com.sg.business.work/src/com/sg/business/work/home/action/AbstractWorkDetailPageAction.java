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
	
	public void pageClear() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		SideBarNavigator view = (SideBarNavigator) page.findView("homenavigator");
		if(view!=null){
			view.doRefresh();
		}
		WorkDetail view2 = (WorkDetail) page.findView("pm2.work.detail");
		view2.cleanUI();
		view2.cleanInput();
		String text = "操作已完成。<br/>请在左边导航栏中选择您要处理的工作。" ;
		view2.initContent(text);
	}
	
	public void pageReload() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		WorkDetail view2 = (WorkDetail) page.findView("pm2.work.detail");
		view2.loadMaster();
	}
	
}
