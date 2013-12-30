package com.sg.business.visualization.labelprovider;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.etl.ProjectPresentation;

public class SchedualLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();
		return pres.getSchedualHTMLLabel();
	}
	

	@Override
	public String getSummary(ProjectProvider data) {
		data.getData();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='"//$NON-NLS-1$
				+ "color:#6f6f6f;"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-style:italic;"//$NON-NLS-1$
//				+ "font-weight:bold;"//$NON-NLS-1$
				+ "font-size:9pt;"//$NON-NLS-1$
				+ "margin-left:2;"//$NON-NLS-1$
				+ "margin-top:8;"//$NON-NLS-1$
//				+ "text-align:center;"//$NON-NLS-1$
//				+ "word-break:break-all; "//$NON-NLS-1$
//				+ "white-space:normal; "//$NON-NLS-1$
				+ "display:block;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append("进行:");
		sb.append(data.sum.processing);
		sb.append(" ");//$NON-NLS-1$
		sb.append(" 超期/正常:");
		sb.append("<span style='color:"+Utils.COLOR_RED[10]+"'>");
		sb.append(data.sum.processing_delay);
		sb.append("</span>");
		sb.append("/");//$NON-NLS-1$
		sb.append("<span style='color:"+Utils.COLOR_GREEN[10]+"'>");
		sb.append(data.sum.processing_normal+data.sum.processing_advance);
		sb.append("</span>");
		sb.append("<br/>");//$NON-NLS-1$
		sb.append("完成:");
		sb.append(data.sum.finished);
		sb.append(" ");//$NON-NLS-1$
		sb.append(" 超期/正常:");
		sb.append("<span style='color:"+Utils.COLOR_RED[10]+"'>");
		sb.append(data.sum.finished_delay);
		sb.append("</span>");
		sb.append("/");
		sb.append("<span style='color:"+Utils.COLOR_GREEN[10]+"'>");
		sb.append(data.sum.finished_normal+data.sum.finished_advance);
		sb.append("</span>");
		sb.append("</span>");//$NON-NLS-1$
		return sb.toString();
	}
}
