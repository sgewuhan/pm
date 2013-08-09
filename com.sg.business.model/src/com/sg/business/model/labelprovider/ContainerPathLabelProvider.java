package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Container;

public class ContainerPathLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		PrimaryObject po = (PrimaryObject)element;
		if(po instanceof Container){
			return ((Container)po).getContainerHostPath();
		}
		return super.getText(element);
	}


}
