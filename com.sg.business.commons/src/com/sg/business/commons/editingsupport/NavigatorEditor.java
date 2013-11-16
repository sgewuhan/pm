package com.sg.business.commons.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sg.widgets.commons.selector.DropdownNavigatorSelector;

public abstract class NavigatorEditor extends CellEditor {

	private DropdownNavigatorSelector selector;
	private String naviId;
	private Shell parentShell;
	protected Object value;

	public NavigatorEditor(String naviId, ColumnViewer viewer) {
		super((Composite) viewer.getControl());
		this.naviId = naviId;
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		ViewerCell cell = (ViewerCell) activationEvent.getSource();
		createSelector(cell);
		super.activate(activationEvent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		parentShell = parent.getShell();
		return composite;
	}

	private void createSelector(final ViewerCell cell) {
		selector = new DropdownNavigatorSelector(naviId){
			@Override
			protected void doOK(IStructuredSelection is) {
				value = getValueFromSelection(is);
				super.doOK(is);
				NavigatorEditor.this.fireApplyEditorValue();
			}
			
			@Override
			public void shellDeactivated(ShellEvent e) {
				NavigatorEditor.this.fireApplyEditorValue();
				super.shellDeactivated(e);
			}
			
			
			
			
			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return true;
			}
			
			@Override
			protected Point getLocation(Shell shell, Control control) {
				Display display = cell.getControl().getDisplay();
				Rectangle displayBounds = display.getBounds();

				Rectangle cellBounds = cell.getBounds();
				Point location = cell.getControl()
						.toDisplay(cellBounds.x, cellBounds.y);
				
//				Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point size = new Point(400,400);
				shell.setSize(size);

				// 能否与控件横向对齐显示
				if (location.x + size.x > displayBounds.width) {
					location.x = displayBounds.width - size.x - 1;
				}

				// 能否在控件下方显示
				if (location.y + cellBounds.height + size.y > displayBounds.height) {
					location.y = location.y - size.y;
				} else {
					location.y = location.y + cellBounds.height;
				}
				
				return location;
			}
			
			
		};
		
		selector.show(parentShell, cell.getControl(), false);

	}

	protected abstract Object getValueFromSelection(IStructuredSelection is);


	@Override
	protected Object doGetValue() {
		return value;
	}

	@Override
	protected void doSetFocus() {

	}

	@Override
	protected void doSetValue(Object value) {
	}
	
	@Override
	protected int getDoubleClickTimeout() {
		return 0;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
