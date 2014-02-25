package com.sg.business.visualization.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

public class PieChart extends AbstractChart {
	
	public static Chart getChart( String[] texts,
			double[] values) {
		// double maxValue = Utils.max(values);
		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		// chart.setMinSlice(maxValue / 50);// 最大的十分之一
		// chart.setMinSliceLabel("其他");
		// chart.setMinSlicePercent(true);
		chart.getTitle().setVisible(false);
		Legend legend = chart.getLegend();
		legend.setItemType(LegendItemType.CATEGORIES_LITERAL);
		legend.setVisible(true);
		legend.getText().getFont().setSize(LEGEND_FONT_SIZE);
		TextDataSet categoryValues = TextDataSetImpl.create(texts);//$NON-NLS-1$ //$NON-NLS-2$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(values);
		// Base Series
		Series series = SeriesImpl.create();
		series.setDataSet(categoryValues);
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(series);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setExplosion(2);
		sePie.setRotation(40);

		sePie.setLabelPosition(Position.INSIDE_LITERAL);// 设置在内部显示数字
		sePie.getLabel().getCaption().getFont().setSize(TICK_FONT_SIZE);
		SeriesDefinition sdef = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdef);
		sdef.getSeries().add(sePie);

		return chart;
	}
}
