package com.sg.business.commons.column.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ECNCheckBox implements IEditingSupportor {

	public ECNCheckBox() {
	}

	@Override
	public EditingSupport createEditingSupport(final ColumnViewer viewer,
			ViewerColumn viewerColumn, final ColumnConfigurator configurator) {
		final CheckboxCellEditor editor = new CheckboxCellEditor(
				(Composite) viewer.getControl());

		// final IContext context=new CurrentAccountContext();
		return new EditingSupport(viewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof PrimaryObject) {
					PrimaryObject po = (PrimaryObject) element;
					po.setValue(configurator.getColumn(), value);
					viewer.update(po, null);
				}
			}

			@Override
			protected Object getValue(Object element) {
				if (element instanceof PrimaryObject) {
					PrimaryObject po = (PrimaryObject) element;
					Object value = po.getValue(configurator.getColumn());
					return Boolean.TRUE.equals(value);
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof PrimaryObject) {
					return !Boolean.FALSE.equals(((PrimaryObject) element)
							.getValue("ecn_canedit"));
				}
				return true;
			}
		};
	}

}
