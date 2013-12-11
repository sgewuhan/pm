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
		
		String charger = pres.getChargerText();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		//��ʾ��Ŀ����
		if(coverImageURL!=null){
			sb.append("<img src='"+ coverImageURL+ "' style='float:left; left:0; top:0; display:block;' width='48' height='48' />");
		}
		//��ʾ��Ŀ����
		sb.append("<b>");
		sb.append(desc);
		sb.append("</b>");
		sb.append("<small style='color=#006789'>");
		sb.append("<br/>");
		//��ʾ�е���֯
		sb.append(launchOrganization);
		//��ʾ������
		sb.append("</small>");
		sb.append(" ");
		sb.append("<small>");
		sb.append(charger);
		sb.append("</small>");
		
		sb.append("</span>");
		
		toolsForOpenProject(project,sb,"desc");
		
		return sb.toString();
	}
	
	
}
