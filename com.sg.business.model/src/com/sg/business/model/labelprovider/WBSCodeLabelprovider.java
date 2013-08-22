package com.sg.business.model.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WBSCodeLabelprovider extends ConfiguratorColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return ((PrimaryObject)element).getImage();
	}

	@Override
	public String getText(Object element) {
		AbstractWork po = (AbstractWork)element;
		return po.getWBSCode();
	}


	

}
