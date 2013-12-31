package com.sg.business.visualization.label.project;

import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ProjectMonthData;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.label.projectset.ISummaryLabelProvider;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class ProjectRevenuePeriod extends ConfiguratorColumnLabelProvider implements ISummaryLabelProvider {

	@Override
	public String getText(Object element) {
		ProjectMonthData po = (ProjectMonthData)element;
		return po.getYear() +"-"+po.getMonth();
	}

	@Override
	public String getSummary(Object data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<img src = '"
				+ FileUtil.getImageURL(BusinessResource.IMAGE_SUM_32, BusinessResource.PLUGIN_ID)
				+ "' "
				+ "style='"//$NON-NLS-1$
				+ "margin-left:10;"//$NON-NLS-1$
				+ "margin-top:4;"//$NON-NLS-1$
				+ "display:block;"//$NON-NLS-1$
				+ "' "
				+ "width='24' "//$NON-NLS-1$
				+ "height='24'"//$NON-NLS-1$
				+ "/>"); //$NON-NLS-1$
		
		return sb.toString();
	}
}
