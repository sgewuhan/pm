package com.sg.business.model.commonlabel;

import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class WorkDefinitionCommonHTMLLable extends CommonHTMLLabel {
	private WorkDefinition workd;

	public WorkDefinitionCommonHTMLLable(WorkDefinition workd) {
		this.workd = workd;
	}

	@Override
	public String getHTML() {
		StringBuffer sb = new StringBuffer();
		// ¹¤×÷desc
		String workDesc = workd.getDesc();
//		workDesc = Utils.getPlainText(workDesc);
		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt;padding-left:10px;'>"); //$NON-NLS-1$
		sb.append(workDesc);
		sb.append("<br/><small style='color:#909090;padding-left:10px;'>");
		Organization org = workd.getOrganization();
		sb.append(org.getPath(2));
		sb.append("</small>");
		sb.append("</span>");
		
		sb.append("<a href=\"launchwork@" + workd.get_id().toString() //$NON-NLS-1$ 
				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;position:absolute; right:40; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
		sb.append("</a>");//$NON-NLS-1$
		return sb.toString();
	}

}
