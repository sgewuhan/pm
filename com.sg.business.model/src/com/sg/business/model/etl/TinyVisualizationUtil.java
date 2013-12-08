package com.sg.business.model.etl;

import com.mobnut.commons.util.Utils;

public class TinyVisualizationUtil {
	
	public static String getColorBar(int i, String style, String percent,
			String color, String currentStageIcon, String location,String height) {
		if (color == null) {
			if (style.equals("red")) {
				if (i < Utils.COLOR_RED.length) {
					color = Utils.COLOR_RED[i];
				} else {
					color = Utils.COLOR_RED[i - 1];
				}
			} else if (style.equals("yellow")) {
				if (i < Utils.COLOR_YELLOW.length) {
					color = Utils.COLOR_YELLOW[i];
				} else {
					color = Utils.COLOR_YELLOW[i - 1];
				}
			} else if (style.equals("green")) {
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
		sb.append("<span style='display:block;width:" + percent
				+ "; height:"+(height==null?"20":height)+"px;  float:left;background:" + color + ";'>");

		if (currentStageIcon != null) {
			// ÏÔÊ¾×´Ì¬Í¼±ê<div align="center"><span>abc</span></div>
			String imageUrl = "<div align='"
					+ location
					+ "'><img src='"
					+ currentStageIcon
					+ "' style='margin-top:2;margin-right:2' width='16' height='16' /></div>";
			sb.append(imageUrl);
		}
		sb.append("</span>");
		return sb.toString();
	}
}
