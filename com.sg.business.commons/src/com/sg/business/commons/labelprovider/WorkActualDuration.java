package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Work;

public class WorkActualDuration extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Integer value = ((Work) element).getActualDuration();
			if (value != null) {
				return "" + value;
			}
		}
		return "";
	}
}
