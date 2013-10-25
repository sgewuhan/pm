package com.sg.business.finance.rndcost;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.CostCenterDuration;
import com.sg.business.model.Organization;
import com.sg.business.model.dataset.finance.FinanceOrganizationDataSet;

public class CostCenterSelector extends EditingSupport {

	private ComboBoxViewerCellEditor editor;

	public CostCenterSelector(ColumnViewer viewer) {
		super(viewer);
		
		createEditor((Composite) viewer.getControl());
	}

	private void createEditor(Composite control) {
		editor = new ComboBoxViewerCellEditor(control);
		editor.setActivationStyle(ComboBoxViewerCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
		DataSet ds = new FinanceOrganizationDataSet().getDataSet();
		List<PrimaryObject> orgList = ds.getDataItems();
		editor.setContentProvider(ArrayContentProvider.getInstance());
		editor.setInput(orgList);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return element instanceof CostCenterDuration;
	}

	@Override
	protected Object getValue(Object element) {
//		if (element instanceof CostCenterDuration) {
//			return element.toString();
//		}
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (element instanceof CostCenterDuration) {
			CostCenterDuration ccd = (CostCenterDuration) element;
			ccd.setOrganization((Organization) value);
			getViewer().update(element, null);
		}
	}

}
