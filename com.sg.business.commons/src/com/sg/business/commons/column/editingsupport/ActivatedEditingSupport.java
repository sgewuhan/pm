package com.sg.business.commons.column.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ActivatedEditingSupport extends EditingSupport {

	private ColumnConfigurator configurator;
	private CellEditor editor;
	private IContext context;

	public ActivatedEditingSupport(ColumnViewer viewer,
			ColumnConfigurator configurator) {
		super(viewer);
		this.configurator = configurator;
		editor = new ActivatedCellEditor((Composite)viewer.getControl());
		context = new CurrentAccountContext();
	}


	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return ((PrimaryObject)element).canEdit(context);
	}

	@Override
	protected Object getValue(Object element) {
		return ((PrimaryObject)element).getValue(configurator.getColumn());
	}

	@Override
	protected void setValue(Object element, Object value) {
		((PrimaryObject)element).setValue(configurator.getColumn(),value);
		try {
			((PrimaryObject)element).doSave(context);
		} catch (Exception e) {
		}

	}

}
