package com.sg.business.commons.ui.home.basic;

import org.bson.types.BasicBSONList;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.commons.ui.home.perf.IImportantSetting;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.widgets.block.button.BusinessContentBlock;

/**
 * 用于领导层查看本部门项目的内容块
 * @author Administrator
 *
 */
public class ProjectContentBlock2 extends BusinessContentBlock {

	public ProjectContentBlock2(Composite parent) {
		super(parent);
	}

	@Override
	protected String getBodyFontStyle() {
		return "font-family:微软雅黑;font-size:8pt;font-weight:bold;";
	}

	@Override
	protected String getBodyText(boolean hoverMask) {
		Project project = (Project) getInput();
		User charger = project.getCharger();
		User bCharger = project.getBusinessCharger();
		StringBuffer sb = new StringBuffer();
		if (charger != null) {
			sb.append(charger);
			Organization org = charger.getOrganization();
			if (org != null) {
				Organization forg = (Organization) org
						.getFunctionOrganization();
				if (forg != null) {
					sb.append("|");
					sb.append(forg.getSimpleName());
				}
			}
		}
		if (bCharger != null) {
			sb.append("<br/>");
			sb.append(bCharger);
			Organization org = bCharger.getOrganization();
			if (org != null) {
				Organization forg = (Organization) org
						.getFunctionOrganization();
				if (forg != null) {
					sb.append("|");
					sb.append(forg.getSimpleName());
				}
			}
		}
		return sb.toString();
	}

	@Override
	protected String getFootText(boolean hoverMask) {
		StringBuffer sb = new StringBuffer();
		Project project = (Project) getInput();
		Object tags = project.getValue(Project._TAG);
		if (tags instanceof BasicBSONList) {
			BasicBSONList tagList = (BasicBSONList) tags;
			for (Object tag : tagList) {
				// sb.append(getTagText((String) tag));
				sb.append(getTagText((String) tag));
				sb.append(" ");
			}
		}
//		sb.append(getTagLogo(IImportantSetting.HEAVYLOSS_CNT));
//		sb.append(getTagLogo(IImportantSetting.HIGHBUDGET_CNT));
//		sb.append(getTagLogo(IImportantSetting.HIGHINVESTMENT_CNT));
//		sb.append(getTagLogo(IImportantSetting.HIGHPROFITRATE_CNT));
//		sb.append(getTagLogo(IImportantSetting.LONGTERMDEV_CNT));
//		sb.append(getTagLogo(IImportantSetting.OVERCOST_CNT));
//		sb.append(getTagLogo(IImportantSetting.OVERTIME_CNT));
		
		return sb.toString();
	}

	private String getTagText(String key) {
		StringBuffer sb = new StringBuffer();
		if (IImportantSetting.HEAVYLOSS_CNT.equals(key)) {
			sb.append("<span style='color:#b00000'>"); //$NON-NLS-1$
			sb.append("亏损");
		} else if (IImportantSetting.HIGHBUDGET_CNT.equals(key)) {
			sb.append("<span style='color:#0d0d0d'>"); //$NON-NLS-1$
			sb.append("高预算");
		} else if (IImportantSetting.HIGHINVESTMENT_CNT.equals(key)) {
			sb.append("<span style='color:#0d0d0d'>"); //$NON-NLS-1$
			sb.append("高投入");
		} else if (IImportantSetting.HIGHPROFITRATE_CNT.equals(key)) {
			sb.append("<span style='color:#007a00'>"); //$NON-NLS-1$
			sb.append("高利润");
		} else if (IImportantSetting.LONGTERMDEV_CNT.equals(key)) {
			sb.append("<span style='color:#0d0d0d'>"); //$NON-NLS-1$
			sb.append("长周期");
		} else if (IImportantSetting.OVERCOST_CNT.equals(key)) {
			sb.append("<span style='color:#b00000'>"); //$NON-NLS-1$
			sb.append("超支");
		} else if (IImportantSetting.OVERTIME_CNT.equals(key)) {
			sb.append("<span style='color:#b00000'>"); //$NON-NLS-1$
			sb.append("进展慢");
		}
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

//	private String getTagLogo(String key) {
//			String imageURL = FileUtil.getImageURL(key + "_tag.png", CommonsActivator.PLUGIN_ID);
//			if(imageURL != null){
//				return "<img src='"
//						+ imageURL
//						+ "' width='16px' height='16px'/>"; //$NON-NLS-1$
//			}else{
//				return getTagText(key);
//			}
//	}

}
