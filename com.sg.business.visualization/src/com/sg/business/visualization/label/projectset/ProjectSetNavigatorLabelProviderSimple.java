package com.sg.business.visualization.label.projectset;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.User;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.visualization.data.ProjectSetFolder;
import com.sg.widgets.Widgets;

public class ProjectSetNavigatorLabelProviderSimple extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		PrimaryObject po = ((PrimaryObject) element);
		if (po instanceof Organization) {
			return getOrganizationText((Organization) po);
		} else if (po instanceof User) {
			return getUserText((User) po);
		} else if (po instanceof UserProjectPerf) {
			return getUserProjectSetText((UserProjectPerf) po);
		} else if (po instanceof ProductTypeProvider) {
			return getProductTypeText((ProductTypeProvider) po);
		} else if (po instanceof ProjectSetFolder) {
			return getFolderText((ProjectSetFolder) po);
		} else {
			return po.getLabel();
		}
	}

	private String getFolderText(ProjectSetFolder po) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='font-family:Î¢ÈíÑÅºÚ;font-size:10pt;'>"); //$NON-NLS-1$
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(po.getLabel());

		sb.append("</b>"); //$NON-NLS-1$
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small style='color=#999'>"); //$NON-NLS-1$
		sb.append(po.getDescription());
		sb.append("</small></div>"); //$NON-NLS-1$
		return sb.toString();
	}

	private String getProductTypeText(ProductTypeProvider producttTypeProvider) {

		StringBuffer sb = new StringBuffer();

		sb.append("<div style='font-family:Î¢ÈíÑÅºÚ;font-size:10pt;margin-top:4px;cursor:pointer;'>"); //$NON-NLS-1$
		sb.append(producttTypeProvider.getDesc());
		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}

	private String getUserProjectSetText(UserProjectPerf po) {
		UserProjectPerf pperf = (UserProjectPerf) po;
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='font-family:Î¢ÈíÑÅºÚ;font-size:10pt;;margin-top:4px;cursor:pointer;'>"); //$NON-NLS-1$
		sb.append(pperf.getDesc());
		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}

	private String getUserText(User po) {
		User user = (User) po;
		StringBuffer sb = new StringBuffer();

		sb.append("<div style='font-family:Î¢ÈíÑÅºÚ;font-size:10pt;;margin-top:4px;cursor:pointer;'>"); //$NON-NLS-1$

		sb.append(user.getLabel());
		sb.append("</div>"); //$NON-NLS-1$

		return sb.toString();
	}

	private String getOrganizationText(Organization po) {
		Organization organization = (Organization) po;
		String label = organization.getLabel();
		StringBuffer sb = new StringBuffer();


		sb.append("<div style='font-family:Î¢ÈíÑÅºÚ;font-size:10pt;margin-top:4px;cursor:pointer'>"); //$NON-NLS-1$
		sb.append(label);
		sb.append("</div>"); //$NON-NLS-1$

		return sb.toString();
	}
	
	@Override
	public Color getBackground(Object element) {
		PrimaryObject po = ((PrimaryObject) element);
		if (po instanceof ProjectSetFolder) {
			return Widgets.getColor(null, 0xfa, 0xfa, 0xfa);
		}
		return super.getBackground(element);
	}


}
