package com.sg.business.commons.ui.chart;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;

import com.mobnut.commons.util.Utils;

public class AbstractChart {

	protected static final int MARKER_SIZE = 3;
	protected static final float LEGEND_FONT_SIZE = 9f;
	protected static final float TICK_FONT_SIZE = 7f;

	protected static final String[] colors = new String[] {
			Utils.COLOR_BLUE[13], Utils.COLOR_YELLOW[13], Utils.COLOR_RED[13],
			Utils.COLOR_GREEN[13], Utils.COLOR_PINK[13], Utils.COLOR_BROWN[13],
			Utils.COLOR_OLIVER[13], Utils.COLOR_BLUE[6], Utils.COLOR_YELLOW[6],
			Utils.COLOR_RED[6], Utils.COLOR_GREEN[6], Utils.COLOR_PINK[6],
			Utils.COLOR_BROWN[6], Utils.COLOR_OLIVER[6] };

	protected static void setSeriesColor(SeriesDefinition sdY) {
		sdY.getSeriesPalette().getEntries().clear();
		
		for (int i = 0; i < colors.length; i++) {
			sdY.getSeriesPalette().getEntries().add(getColor(colors[i]));
		}
	}

	protected static ColorDefinition getColor(String colorCode) {
		int[] rgb = Utils.getRGB(colorCode);// "正常完成"
		return ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
	}

}
