package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkDefinitionConnection;

public class End2WorkDefLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		WorkDefinition end2 = ((WorkDefinitionConnection) element).getEnd2();
		if (end2 != null)
			return end2.getLabel();
		return "";
	}
}
