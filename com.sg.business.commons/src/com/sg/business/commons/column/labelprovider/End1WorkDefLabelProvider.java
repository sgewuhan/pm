package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkDefinitionConnection;

public class End1WorkDefLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		WorkDefinition end1 = ((WorkDefinitionConnection) element).getEnd1();
		if (end1 != null)
			return end1.getLabel();
		return ""; //$NON-NLS-1$
	}
}
