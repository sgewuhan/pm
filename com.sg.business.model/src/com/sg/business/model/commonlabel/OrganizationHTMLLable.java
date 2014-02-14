package com.sg.business.model.commonlabel;

import com.sg.business.model.Organization;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class OrganizationHTMLLable extends CommonHTMLLabel {

	private Organization org;

	public OrganizationHTMLLable(Organization org) {
		this.org = org;
	}

	@Override
	public String getHTML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt'>"); //$NON-NLS-1$

		String imageUrl = "<img src='" + org.getImageURL() //$NON-NLS-1$
				+ "' style='float:left;padding:2px' width='24' height='24' />"; //$NON-NLS-1$
		String label = org.getLabel();
		String path = org.getFullName();

		sb.append(imageUrl);
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(label);
		sb.append("</b>"); //$NON-NLS-1$
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(path);
		sb.append("</small></span>"); //$NON-NLS-1$
		return sb.toString();
	}

}
