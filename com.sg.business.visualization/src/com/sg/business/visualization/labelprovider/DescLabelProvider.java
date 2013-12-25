package com.sg.business.visualization.labelprovider;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class DescLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		String coverImageURL = pres.getCoverImageURL();

		String launchOrganization = pres.getLaunchOrganizationText();
		
		String businessOrganization = pres.getBusinessOrganizationText();

		String charger = pres.getChargerText();

		String bm = pres.getBusinessManagerText();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>"); //$NON-NLS-1$
		// 显示项目封面
		if (coverImageURL != null) {
			sb.append("<img src='" + coverImageURL + "' style='float:left; left:0; top:0; display:block;' width='48' height='48' />"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// 显示项目名称
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</b>"); //$NON-NLS-1$
		
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small style='color=#006789'>"); //$NON-NLS-1$
		// 显示承担组织
		sb.append(launchOrganization);
		// 显示负责人
		sb.append("</small>"); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(charger);
		sb.append("</small>"); //$NON-NLS-1$
		
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small style='color=#006789'>"); //$NON-NLS-1$
		// 显示承担组织
		sb.append(businessOrganization);
		// 显示负责人
		sb.append("</small>"); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(bm);
		sb.append("</small>"); //$NON-NLS-1$
		
		sb.append("</span>"); //$NON-NLS-1$

		toolsForOpenProject(project, sb, "desc"); //$NON-NLS-1$

		return sb.toString();
	}

}
