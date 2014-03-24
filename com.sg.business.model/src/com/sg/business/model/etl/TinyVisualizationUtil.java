package com.sg.business.model.etl;

import com.mobnut.commons.util.Utils;

public class TinyVisualizationUtil {
	
	public static String getColorBar(int i, String style, String percent,
			String color, String currentStageIcon,String currentStageIText, String location,String height) {
		if (color == null) {
			if (style.equals("red")) { //$NON-NLS-1$
				if (i < Utils.COLOR_RED.length) {
					color = Utils.COLOR_RED[i];
				} else {
					color = Utils.COLOR_RED[i - 1];
				}
			} else if (style.equals("yellow")) { //$NON-NLS-1$
				if (i < Utils.COLOR_YELLOW.length) {
					color = Utils.COLOR_YELLOW[i];
				} else {
					color = Utils.COLOR_YELLOW[i - 1];
				}
			} else if (style.equals("green")) { //$NON-NLS-1$
				if (i < Utils.COLOR_GREEN.length) {
					color = Utils.COLOR_GREEN[i];
				} else {
					color = Utils.COLOR_GREEN[i - 1];
				}
			} else {
				if (i < Utils.COLOR_BLUE.length) {
					color = Utils.COLOR_BLUE[i];
				} else {
					color = Utils.COLOR_BLUE[i - 1];
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='display:block;width:" + percent //$NON-NLS-1$
				+ "; height:"+(height==null?"20":height)+"px;  float:left;background:" + color + ";'>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (currentStageIcon != null) {
			// ÏÔÊ¾×´Ì¬Í¼±ê<div align="center"><span>abc</span></div>
			String imageUrl = "<div align='" //$NON-NLS-1$
					+ location
					+ "'><img src='" //$NON-NLS-1$
					+ currentStageIcon
					+ "' style='margin-top:2;margin-right:2' width='16' height='16' />"
					+ "</div>"; //$NON-NLS-1$
			sb.append(imageUrl);
		}
		if (currentStageIText != null) {
			// ÏÔÊ¾×´Ì¬Í¼±ê<div align="center"><span>abc</span></div>
			String imageUrl = "<div align='"+location
					+ "' style='color:#ffffff'/>"
					+ currentStageIText
					+ "</div>"; //$NON-NLS-1$
			sb.append(imageUrl);
		}
		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}
	
	
}
