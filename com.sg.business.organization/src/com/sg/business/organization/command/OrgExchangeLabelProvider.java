package com.sg.business.organization.command;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class OrgExchangeLabelProvider extends LabelProvider {
	@Override
	public Image getImage(Object element) {
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof OrgExchange) {
			OrgExchange orgExchange = (OrgExchange) element;
			return orgExchange.getDesc();
		}
		return super.getText(element);
	}
}
