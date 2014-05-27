package com.sg.business.management.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.WorkDefinition;

public class MeasurementLabelProvider extends ColumnLabelProvider {

	public MeasurementLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if (element instanceof WorkDefinition) {
			WorkDefinition workd = (WorkDefinition) element;
			return workd.getMeasurementLabel();

		}
		return "";
	}
}
