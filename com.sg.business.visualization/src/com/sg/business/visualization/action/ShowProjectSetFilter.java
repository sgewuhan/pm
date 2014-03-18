package com.sg.business.visualization.action;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.ui.DurationSetting;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.IDataObjectEditorAction;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;

public class ShowProjectSetFilter implements IDataObjectEditorAction {

	protected CurrentAccountContext context;
	protected Locale locale;
	protected Control control;
	protected IValidable validable;

	public ShowProjectSetFilter() {
		this.context = new CurrentAccountContext();
		this.locale = RWT.getLocale();
	}

	@Override
	public void run(final PrimaryObjectEditorInput input, Control control) {
		ProjectProvider data = (ProjectProvider)input.getData();
		DurationSetting shell = new DurationSetting(control.getShell(), data){
			@Override
			protected void setFilter(int yearIndex, int quarterIndex,
					int monthIndex, boolean clearFilter) {
				super.setFilter(yearIndex, quarterIndex, monthIndex, clearFilter);
				input.fireTopicChanged();
			}
		};
		Point location = control.toDisplay(0, control.getBounds().y+48);
		Display display = shell.getDisplay();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if (display.getBounds().width < shell.getBounds().width + location.x) {
			location.x = display.getBounds().width - size.x
					- 10;
		}
		shell.open(location);
	}

	@Override
	public boolean visiableWhen(PrimaryObjectEditorInput input) {
		return true;
	}

	@Override
	public boolean enableWhen(PrimaryObjectEditorInput input) {
		return true;
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
