package com.sg.business.model.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;
import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class PlanStartEditing implements IEditingSupportor {

	@Override
	public EditingSupport createEditingSupport(ColumnViewer viewer,
			ViewerColumn viewerColumn, final ColumnConfigurator configurator) {
		String type = configurator.getType();
		int style = SWT.DROP_DOWN;
		if (Utils.TYPE_DATE.equals(type)) {
			style = style | SWT.MEDIUM;
		} else if (Utils.TYPE_DATETIME.equals(type)) {
			style = style | SWT.LONG;
		}
		final DateCellEditor editor = new DateCellEditor(
				(Composite) viewer.getControl(), style);
		EditingSupport es = new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					return work.canEdit(configurator.getColumn(),
							new CurrentAccountContext());
				}
				return false;
			}

			@Override
			protected Object getValue(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					return work.getValue(configurator.getColumn());
				}
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Work) {
					Work work = (Work) element;
					Work[] result = work.doUpdateSchedual(
							configurator.getColumn(), value,
							new CurrentAccountContext());
					getViewer().update(result, null);
				}
			}

		};
		return es;
	}

}
