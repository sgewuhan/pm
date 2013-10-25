package com.sg.business.finance.rndcost;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

import com.sg.business.model.CostCenterDuration;
import com.sg.business.model.Organization;

public class CostCenterSelector extends EditingSupport {

	public CostCenterSelector(ColumnViewer viewer) {
		super(viewer);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		return element instanceof CostCenterDuration;
	}

	@Override
	protected Object getValue(Object element) {
		if (element instanceof CostCenterDuration) {
			return element.toString();
		}
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof CostCenterDuration) {
			CostCenterDuration ccd = (CostCenterDuration) element;
			ccd.setOrganization((Organization) value);
		}
	}

}
