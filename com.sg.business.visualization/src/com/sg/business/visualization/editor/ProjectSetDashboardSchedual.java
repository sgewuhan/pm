package com.sg.business.visualization.editor;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.mobnut.commons.util.Utils;
import com.sg.widgets.birtcharts.ChartCanvas;

public class ProjectSetDashboardSchedual extends AbstractProjectPage {

	private static final int STRONG_SIZE = 11;
	private static final int NORMAL_SIZE = 9;
	private static final int SMALL_SIZE = 8;

	@Override
	protected Composite createContent(Composite body) {
		SashForm content = new SashForm(body, SWT.HORIZONTAL);
		Composite tableContent = new Composite(content, SWT.NONE);
		navi.createPartContent(tableContent);

		Composite graphicContent = new Composite(content, SWT.NONE);
		graphicContent.setLayout(new FillLayout());
		createGraphic(graphicContent);

		content.setWeights(new int[] { 3, 1 });
		return content;
	}

	private void createGraphic(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.TOP);
		ChartCanvas pieChart = new ChartCanvas(tabFolder, SWT.NONE);
		pieChart.setChart(createPieChart());
		ChartCanvas barChart = new ChartCanvas(tabFolder, SWT.NONE);
		
		barChart.setChart(SuperImposedMeter.createSuperImposedMeter());
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("状态摘要");
		pieTabItem.setControl(pieChart);
		TabItem barTabItem = new TabItem(tabFolder, SWT.NONE);
		barTabItem.setText("部门分布");
		barTabItem.setControl(barChart);
	}

	private Chart createPieChart() {
		String pieChartCaption =  "状态摘要";
		String[] texts = new String[] { "正常完成", "超期完成","进度延迟","预期延迟","正常进行" };
		double[] values = new double[] { 10, 20,15,29,30 };
		
		int[] rgb = Utils.getRGB(Utils.COLOR_GREEN[10]);// "正常完成"
		ColorDefinition color1 = ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
		
		rgb = Utils.getRGB(Utils.COLOR_GREEN[2]);//"超期完成"
		ColorDefinition color2 = ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
		
		rgb = Utils.getRGB(Utils.COLOR_RED[5]);//"进度延迟"
		ColorDefinition color3 = ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
		
		rgb = Utils.getRGB(Utils.COLOR_YELLOW[5]);
		ColorDefinition color4 = ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
		
		rgb = Utils.getRGB(Utils.COLOR_BLUE[5]);//"预期延迟"
		ColorDefinition color5 = ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);

		final Fill[] fiaBase = { color1,color2,color3,color4,color5 };
		
		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
//		chart.setScript(FilterActionHandler.class.getName());
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
//		chart.setSeriesThickness(2);//设置厚度
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue(pieChartCaption);
		adjustFont(caption.getFont(),STRONG_SIZE);
		Legend legend = chart.getLegend();
		legend.setItemType(LegendItemType.CATEGORIES_LITERAL);
		legend.setVisible(true);
		adjustFont(legend.getText().getFont(),NORMAL_SIZE);
		TextDataSet categoryValues = TextDataSetImpl.create(texts);//$NON-NLS-1$ //$NON-NLS-2$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(values);
		// Base Series
		Series series = SeriesImpl.create();
		series.setDataSet(categoryValues);
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(series);
		// new colors
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}
		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setExplosion(1);
		sePie.setRotation(40);
	
		/**
		 * 使用反馈
		 */
//		EList<Trigger> triggers = sePie.getTriggers();
//		ActionValue a = CallBackValueImpl.create("onclick");
//		triggers.add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, ActionImpl.create(ActionType.CALL_BACK_LITERAL, a)));
		
//		sePie.setTranslucent(true);//设置半透明
		sePie.setLabelPosition(Position.INSIDE_LITERAL);//设置在内部显示数字
		adjustFont(sePie.getLabel().getCaption().getFont(),SMALL_SIZE);//设置字体
		

		SeriesDefinition sdef = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdef);
		sdef.getSeries().add(sePie);
		
		return chart;
	}
	
	private Chart createBarChart1() {
		ChartWithAxes chart = ChartWithAxesImpl.create();
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		Plot plot = chart.getPlot();
		plot.setBackground(ColorDefinitionImpl.WHITE());
		plot.getClientArea().setBackground(ColorDefinitionImpl.WHITE());
		chart.getLegend().setVisible(false);
		
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue("Distribution of Chart Column Heights");
		adjustFont(caption.getFont(),NORMAL_SIZE);
		Axis xAxis = ((ChartWithAxes) chart).getPrimaryBaseAxes()[0];
		xAxis.getTitle().setVisible(true);
		xAxis.getTitle().getCaption().setValue("");

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"short", "medium", "tall" });
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		adjustFont(seCategory.getLabel().getCaption().getFont(),NORMAL_SIZE);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(1);
		xAxis.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);
		NumberDataSet orthoValuesDataSet1 = NumberDataSetImpl
				.create(new double[] { 1, 2, 3 });
		DialChart bs1 = (DialChart) DialChartImpl.create();
//		bs1.setDataSet(orthoValuesDataSet1);
//		adjustFont(bs1.getLabel().getCaption().getFont(),NORMAL_SIZE);

		return chart;
	}

	private Chart createBarChart() {
		ChartWithAxes chart = ChartWithAxesImpl.create();
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		Plot plot = chart.getPlot();
		plot.setBackground(ColorDefinitionImpl.WHITE());
		plot.getClientArea().setBackground(ColorDefinitionImpl.WHITE());
		Legend legend = chart.getLegend();
		legend.setItemType(LegendItemType.CATEGORIES_LITERAL);
		legend.setVisible(true);
		adjustFont(legend.getText().getFont(),NORMAL_SIZE);
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue("Distribution of Chart Column Heights");
		adjustFont(caption.getFont(),NORMAL_SIZE);
		Axis xAxis = ((ChartWithAxes) chart).getPrimaryBaseAxes()[0];
		xAxis.getTitle().setVisible(true);
		xAxis.getTitle().getCaption().setValue("");
		Axis yAxis = ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(xAxis);
		yAxis.getTitle().setVisible(true);
		yAxis.getTitle().getCaption().setValue("");
		yAxis.getScale().setStep(1.0);
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"short", "medium", "tall" });
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		adjustFont(seCategory.getLabel().getCaption().getFont(),NORMAL_SIZE);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(1);
		xAxis.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);
		NumberDataSet orthoValuesDataSet1 = NumberDataSetImpl
				.create(new double[] { 1, 2, 3 });
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setDataSet(orthoValuesDataSet1);
		adjustFont(bs1.getLabel().getCaption().getFont(),NORMAL_SIZE);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxis.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		return chart;
	}

	private void adjustFont(FontDefinition font,int size) {
		font.setSize(size);
		font.setName("Segoe");
	}

}
