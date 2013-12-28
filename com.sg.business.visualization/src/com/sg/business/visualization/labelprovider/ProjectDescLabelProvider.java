package com.sg.business.visualization.labelprovider;

import com.sg.business.model.Project;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.etl.ProjectPresentation;

public class ProjectDescLabelProvider extends AbstractProjectLabelProvider {

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
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block;' width='1000'>"); //$NON-NLS-1$
		// ��ʾ��Ŀ����
		if (coverImageURL != null) {
			sb.append("<img src='" + coverImageURL + "' style='float:left; left:0; top:0; display:block;' width='48' height='48' />"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// ��ʾ��Ŀ����
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</b>"); //$NON-NLS-1$

		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small style='color=#006789'>"); //$NON-NLS-1$
		// ��ʾ�е���֯
		sb.append(launchOrganization);
		// ��ʾ������
		sb.append("</small>"); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(charger);
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small style='color=#006789'>"); //$NON-NLS-1$
		// ��ʾ�е���֯
		if (businessOrganization != null) {
			sb.append(businessOrganization);
		}
		// ��ʾ������
		sb.append("</small>"); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		if (bm != null) {
			sb.append(bm);
		}
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$

		toolsForOpenProject(project, sb, "desc"); //$NON-NLS-1$

		return sb.toString();
	}

	@Override
	public String getSummary(ProjectProvider data) {
		data.getData();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='"//$NON-NLS-1$
				+ "color:#6f6f6f;"//$NON-NLS-1$
				+ "font-family:΢���ź�;"//$NON-NLS-1$
				//				+ "font-style:italic;"//$NON-NLS-1$
				//				+ "font-weight:bold;"//$NON-NLS-1$
				+ "font-size:12pt;"//$NON-NLS-1$
				+ "margin-left:10;"//$NON-NLS-1$
				+ "margin-top:14;"//$NON-NLS-1$
//				+ "text-align:center;"//$NON-NLS-1$
//				+ "word-break:break-all; "//$NON-NLS-1$
//				+ "white-space:normal; "//$NON-NLS-1$
				+ "display:block;"//$NON-NLS-1$
				+ "' "
				+ ">"); //$NON-NLS-1$
		sb.append("�ϼ�:");
		sb.append("    ");//$NON-NLS-1$
		sb.append("��Ŀ����:");
		sb.append(data.sum.total);
		sb.append("</span>");//$NON-NLS-1$
		return sb.toString();
	}

}
