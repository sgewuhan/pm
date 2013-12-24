package com.sg.business.visualization.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
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
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;

public class LineChart extends AbstractChart {

	public static Chart getChart(String[] xAxisText, String[] lsText,
			double[][] lsValue,int shift) {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();
		cwaLine.setType("Line Chart"); //$NON-NLS-1$
		cwaLine.setSubType("Overlay"); //$NON-NLS-1$

		// Plot
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		// Title
		cwaLine.getTitle().setVisible(false);

		// Legend
		cwaLine.getLegend().getText().getFont().setSize(LEGEND_FONT_SIZE);
		cwaLine.getLegend().setPosition(Position.BELOW_LITERAL);
		cwaLine.getLegend().setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getLabel().getCaption().getFont().setSize(TICK_FONT_SIZE);
		xAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.create(0x9d, 0x9d, 0x9d));

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
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
		sdY.getSeriesPalette( ).shift( shift );
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		for (int i = 0; i < lsValue.length; i++) {
			NumberDataSet orthoValues = NumberDataSetImpl.create(lsValue[i]);
			OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
					.createOrthogonalSampleData();
			sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
			sdOrthogonal.setSeriesDefinitionIndex(0);
			sd.getOrthogonalSampleData().add(sdOrthogonal);

			LineSeries ls = (LineSeries) LineSeriesImpl.create();
			ls.setSeriesIdentifier(lsText[i]);
			ls.setDataSet(orthoValues);
			ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
			for (int j = 0; j < ls.getMarkers().size(); j++) {
				Marker marker = (Marker) ls.getMarkers().get(j);
				marker.setType(MarkerType.CIRCLE_LITERAL);
				marker.setSize(MARKER_SIZE);
			}
			ls.getLabel().setVisible(true);
			ls.getLabel().getCaption().getFont().setSize(TICK_FONT_SIZE);
			ls.setCurve(true);
			sdY.getSeries().add(ls);
		}
		cwaLine.setSampleData(sd);
		return cwaLine;
	}
}
