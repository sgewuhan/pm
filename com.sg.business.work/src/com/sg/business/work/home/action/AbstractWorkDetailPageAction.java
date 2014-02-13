package com.sg.business.work.home.action;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.IDataObjectEditorAction;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;
import com.sg.widgets.part.view.SideBarNavigator;

public abstract class AbstractWorkDetailPageAction implements IDataObjectEditorAction{
	
	private Locale locale;
	private IContext context;
	private Control control;
	private PrimaryObjectEditorInput input;
	private IValidable validable;

	public AbstractWorkDetailPageAction(){
		this.context = new CurrentAccountContext();
		this.locale = RWT.getLocale();
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public IContext getContext() {
		return context;
	}
	
	public PrimaryObjectEditorInput getInput() {
		return input;
	}
	
	public IValidable getValidable() {
		return validable;
	}
	
	@Override
	final public boolean visiableWhen(PrimaryObjectEditorInput input) {
		return visiableWhen((Work)input.getData());
	}

	protected boolean visiableWhen(Work work) {
		return true;
	}

	@Override
	final public boolean enableWhen(PrimaryObjectEditorInput input) {
		return enableWhen((Work)input.getData());
	}
	
	protected boolean enableWhen(Work work) {
		return true;
	}

	@Override
	public void run(PrimaryObjectEditorInput input, Control control) {
		this.input = input;
		run((Work)input.getData(),control);
	}

	protected abstract void run(Work data, Control control);
	
	public void pageClear() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		SideBarNavigator view = (SideBarNavigator) page.findView("homenavigator");
		if(view!=null){
			view.doRefresh();
		}
		PrimaryObjectDetailFormView view2 = (PrimaryObjectDetailFormView) page.findView("pm2.work.detail");
		view2.cleanUI();
		view2.cleanInput();
		view2.goHome();
	}
	
	public void pageReload() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PrimaryObjectDetailFormView view2 = (PrimaryObjectDetailFormView) page.findView("pm2.work.detail");
		view2.loadMaster();
	}
	
	@Override
	public void setControl(Control control) {
		this.control = control;
	}
	
	public Control getControl() {
		return control;
	}
	
	@Override
	public void setValidable(IValidable validable) {
		this.validable = validable;
	}
	
}
