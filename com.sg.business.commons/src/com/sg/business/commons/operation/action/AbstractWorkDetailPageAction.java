package com.sg.business.commons.operation.action;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Control;

import com.mobnut.db.model.IContext;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.IDataObjectEditorAction;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;

public abstract class AbstractWorkDetailPageAction implements
		IDataObjectEditorAction {

	private Locale locale;
	private IContext context;
	private Control control;
	private PrimaryObjectEditorInput input;
	private IValidable validable;

	public AbstractWorkDetailPageAction() {
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
		return visiableWhen((Work) input.getData());
	}

	protected boolean visiableWhen(Work work) {
		return true;
	}

	@Override
	final public boolean enableWhen(PrimaryObjectEditorInput input) {
		return enableWhen((Work) input.getData());
	}

	protected boolean enableWhen(Work work) {
		return true;
	}

	@Override
	public void run(PrimaryObjectEditorInput input, Control control) {
		this.input = input;
		run((Work) input.getData(), control);
	}

	public abstract void run(Work data, Control control);

	public void pageClear() {
		UIFrameworkUtils.refreshSidebar();
		UIFrameworkUtils.navigateHome();
	}

	public void pageReload() {
		pageReload(false);
	}

	/**
	 * 是否重新加载input, 当编辑器或者input可能发生改变时 reloadInput设置为true
	 * 
	 * @param reloadInput
	 */
	public void pageReload(boolean reloadInput) {
		UIFrameworkUtils.refreshHomePart(reloadInput);
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
