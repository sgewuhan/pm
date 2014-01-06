package com.sg.business.visualization.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;

public class CommonChart extends AbstractChart {

	public static final String TYPE_LINE = "Line Chart";//$NON-NLS-1$

	public static final String TYPE_BAR = "Bar Chart";//$NON-NLS-1$

	public static final String TYPE_SUBTYPE_OVERLAY = "Overlay";//$NON-NLS-1$

	public static final String TYPE_SUBTYPE_STACKED = "Stacked";//$NON-NLS-1$

	public static final String TYPE_SUBTYPE_SIDE_BY_SIDE = "Side-by-side";//$NON-NLS-1$

	public static Chart getChart(String[] xAxisText, String[] seriesIds,
			double[][] seriesValue, String mainType, String subType,
			boolean showSeriesLabel, int colorShift) {
		ChartWithAxes chart = ChartWithAxesImpl.create();
		chart.setType(mainType); //$NON-NLS-1$
		chart.setSubType(subType); //$NON-NLS-1$
		// 处理条图的基本设置
		if (mainType.equals(TYPE_BAR)) {
			chart.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
			chart.setSeriesThickness(2);
		}

		// Plot
		chart.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		Plot p = chart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		// Title
		chart.getTitle().setVisible(false);

		// Legend
		Legend legend = chart.getLegend();
		legend.getText().getFont().setSize(LEGEND_FONT_SIZE);
		legend.setPosition(Position.BELOW_LITERAL);
		legend.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = chart.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getLabel().getCaption().getFont().setSize(TICK_FONT_SIZE);
		xAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.create(0x9d, 0x9d, 0x9d));

		// Y-Axis
		Axis yAxisPrimary = chart.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setSize(TICK_FONT_SIZE);
		yAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.create(0x9d, 0x9d, 0x9d));

		// Data Set
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);
		TextDataSet categoryValues = TextDataSetImpl.create(xAxisText);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Sereis
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		setSeriesColor(sdY);
		sdY.getSeriesPalette().shift(colorShift);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		for (int i = 0; i < seriesValue.length; i++) {
			NumberDataSet oValues = NumberDataSetImpl.create(seriesValue[i]);
			OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
					.createOrthogonalSampleData();
			sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
			sdOrthogonal.setSeriesDefinitionIndex(0);
			sd.getOrthogonalSampleData().add(sdOrthogonal);

			// 处理条图的基本设置
			Series series = null;
			if (mainType.equals(TYPE_BAR)) {
				series = createBarSeries(seriesIds[i], oValues, subType,
						showSeriesLabel);
			} else if (mainType.equals(TYPE_LINE)) {
				series = createLineSeries(seriesIds[i], oValues, subType,
						showSeriesLabel);
			}
			if (series != null) {
				sdY.getSeries().add(series);
			}
		}
		chart.setSampleData(sd);
		return chart;
	}

	private static Series createBarSeries(String seriesIdentifier,
			NumberDataSet oValues, String subType, boolean showSeriesLabel) {
		BarSeries series = (BarSeries) BarSeriesImpl.create();
		series.setSeriesIdentifier(seriesIdentifier);
		series.setDataSet(oValues);

		Label label = series.getLabel();
		label.setVisible(showSeriesLabel);
		label.getCaption().getFont().setSize(TICK_FONT_SIZE);
		if (subType.equals(TYPE_SUBTYPE_STACKED)) {
			series.setStacked(true);
		}
		return series;
	}

	private static LineSeries createLineSeries(String seriesIdentifier,
			NumberDataSet oValues, String subType, boolean showSeriesLabel) {
		LineSeries series = (LineSeries) LineSeriesImpl.create();
		series.setSeriesIdentifier(seriesIdentifier);
		series.setDataSet(oValues);

		LineAttributes lineAttributes = series.getLineAttributes();
		lineAttributes.setColor(ColorDefinitionImpl.WHITE());
		lineAttributes.setThickness(2);
		for (int j = 0; j < series.getMarkers().size(); j++) {
			Marker marker = (Marker) series.getMarkers().get(j);
			marker.setType(MarkerType.CIRCLE_LITERAL);
			marker.setSize(MARKER_SIZE);
		}
		Label label = series.getLabel();
		label.setVisible(showSeriesLabel);
		label.getCaption().getFont().setSize(TICK_FONT_SIZE);
		series.setCurve(true);
		return series;
	}
}
