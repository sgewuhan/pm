package com.sg.business.model.commonlabel;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class ProjectCommonHTMLLable extends CommonHTMLLabel {
	
	private Project project;

	public ProjectCommonHTMLLable(Project project){
		this.project = project;
	}

	@Override
	public String getHTML() {
		if (project == null) {
			return ""; //$NON-NLS-1$
		}
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		String schedualLabel = pres.getSchedualHTMLLabel();

		
		StringBuffer sb = new StringBuffer();
		sb.append(schedualLabel);
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:10pt;position:absolute; left:0; top:0; display:block;'>"); //$NON-NLS-1$
		// 显示项目名称
		sb.append(desc);
		sb.append("</span>");
		
		
		return sb.toString();
	}

}
