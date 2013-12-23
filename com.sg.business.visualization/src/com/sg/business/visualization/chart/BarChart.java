package com.sg.business.visualization.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
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
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

public class BarChart extends AbstractChart {

	public static Chart getChart(String[] xAxisText, String[] bsText,
			double[][] bsValue,String subType,int shift) {
		ChartWithAxes cwabar = ChartWithAxesImpl.create();
		cwabar.setType("Bar Chart"); //$NON-NLS-1$
		cwabar.setSubType(subType); //$NON-NLS-1$
		cwabar.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		cwabar.setSeriesThickness(2);

		// Plot
		cwabar.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		Plot p = cwabar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		// Title
		cwabar.getTitle().setVisible(false);

		// Legend
		cwabar.getLegend().getText().getFont().setSize(FONT_SIZE + 1);
		cwabar.getLegend().getText().getFont().setName("微软雅黑");
		cwabar.getLegend().setPosition(Position.BELOW_LITERAL);
		cwabar.getLegend().setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwabar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getLabel().getCaption().getFont().setSize(FONT_SIZE);
		xAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.create(0x9d, 0x9d, 0x9d));

		// Y-Axis
		Axis yAxisPrimary = cwabar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setSize(FONT_SIZE);
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
		//TODO 需要改变颜色
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		setSeriesColor(sdY);
		sdY.getSeriesPalette( ).shift( shift );
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		for (int i = 0; i < bsValue.length; i++) {
			NumberDataSet orthoValues = NumberDataSetImpl.create(bsValue[i]);
			OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
					.createOrthogonalSampleData();
			sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
			sdOrthogonal.setSeriesDefinitionIndex(0);
			sd.getOrthogonalSampleData().add(sdOrthogonal);

			BarSeries bs = (BarSeries) BarSeriesImpl.create();
			bs.setSeriesIdentifier(bsText[i]);
			bs.setDataSet(orthoValues);
			bs.getLabel().setVisible(false);
			if(subType == "Stacked"){
				bs.setStacked(true);
//			} else {
//				bs.setRiser( RiserType.TUBE_LITERAL );
			}
//			bs.getLabel().getCaption().getFont().setSize(FONT_SIZE);
			sdY.getSeries().add(bs);
		}
		cwabar.setSampleData(sd);
		return cwabar;
	}
	
}
