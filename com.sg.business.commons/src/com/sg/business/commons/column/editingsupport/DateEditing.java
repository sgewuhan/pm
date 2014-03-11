package com.sg.business.commons.column.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.commons.editingsupport.SchedualCellEditor;
import com.sg.widgets.registry.config.ColumnConfigurator;

public  class DateEditing implements IEditingSupportor {

	@Override
	public EditingSupport createEditingSupport(final ColumnViewer viewer,
			ViewerColumn viewerColumn, final ColumnConfigurator configurator) {
		String type = configurator.getType();
		int style = SWT.DROP_DOWN;
		if (Utils.TYPE_DATE.equals(type)) {
			style = style | SWT.MEDIUM;
		} else if (Utils.TYPE_DATETIME.equals(type)) {
			style = style | SWT.LONG;
		}

		final CellEditor editor = new SchedualCellEditor(
				(Composite) viewer.getControl());

		EditingSupport es = new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				if (element instanceof PrimaryObject) {
					PrimaryObject po = (PrimaryObject) element;
					return po.getValue(configurator.getColumn());
				}
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof PrimaryObject) {
					PrimaryObject po = (PrimaryObject) element;
					po.setValue(configurator.getColumn(), value);
					viewer.update(po, null);
					}
			}

		};
		return es;
	}


}
