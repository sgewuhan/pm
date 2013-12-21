package com.sg.business.visualization.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

abstract class AbstractProjectLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Project) {
			String text = getProjectText((Project) element);
			return text == null ? "" : text; //$NON-NLS-1$
		} else {
			return "unsupport type, required: com.sg.business.model.Project"; //$NON-NLS-1$
		}
	}

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 1000;
	}

	@Override
	public String getToolTipText(Object element) {
		if (element instanceof Project) {
			return getProjectToolTipText((Project) element);
		} else {
			return null;
		}
	}

	@Override
	public Image getToolTipImage(Object element) {
		if (element instanceof Project) {
			return getProjectToolTipImage((Project) element);
		} else {
			return null;
		}
	}

	protected Image getProjectToolTipImage(Project element){
		return null;
	}

	protected String getProjectToolTipText(Project element){
		return null;
	}

	protected abstract String getProjectText(Project project);
	

	protected void toolsForOpenProject(Project project,StringBuffer sb,String eventCode) {
		sb.append("<a href=\"" + project.get_id().toString() //$NON-NLS-1$
				+ "/"+eventCode+"\" target=\"_rwt\">"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("<img src='"+FileUtil.getImageURL(BusinessResource.IMAGE_GO_32, BusinessResource.PLUGIN_ID) //$NON-NLS-1$
		+"' style='border-style:none;position:absolute; right:0; bottom:2; display:block;' width='14' height='14'/>"); //$NON-NLS-1$
		sb.append("<a>"); //$NON-NLS-1$
	}
	
	protected String getCurrency(double value,int size) {
		if (value >= 0) {
			return "<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:"+size+"pt;margin-left:1;'>" //$NON-NLS-1$ //$NON-NLS-2$
					+ String.format("%.2f",value / 10000)+ "</span>"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return "<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:"+size+"pt;margin-left:1;color=" //$NON-NLS-1$ //$NON-NLS-2$
					+ Utils.COLOR_RED[10]
					+ "'>" //$NON-NLS-1$
					+ String.format("%.2f",value / 10000) + "</span>"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	
	
}
