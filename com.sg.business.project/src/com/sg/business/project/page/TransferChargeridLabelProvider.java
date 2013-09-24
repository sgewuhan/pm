package com.sg.business.project.page;

import org.eclipse.swt.graphics.Image;

import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class TransferChargeridLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {

		return "";
	}
	@Override
	public Image getImage(Object element) {
		getValue(element);
		return super.getImage(element);
	}
}
