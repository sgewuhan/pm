package com.sg.business.visualization.editor;

import java.text.NumberFormat;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

		content.setWeights(new int[] { 3, 2 });
		return content;
	}

	@Override
	protected String getProjectSetPageLabel() {
		String projectSetName = data.getProjectSetName();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:13pt'>");
		sb.append(projectSetName + " 进度状况");
		sb.append("</span>");
		return sb.toString();
	}

	private void createGraphic(Composite parent) {

		TabFolder tabFolder = new TabFolder(parent, SWT.TOP);

		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("状态");
		ChartCanvas pieChart = new ChartCanvas(tabFolder, SWT.NONE);
		pieChart.setChart(createPieChart());
		pieTabItem.setControl(pieChart);

		TabItem barTabItem = new TabItem(tabFolder, SWT.NONE);
		barTabItem.setText("部门");
		ChartCanvas barChart = new ChartCanvas(tabFolder, SWT.NONE);
		barChart.setChart(createStackedBarChart());
		barTabItem.setControl(barChart);

		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("指示器");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());

		ChartCanvas meterChart = new ChartCanvas(composite, SWT.NONE);
		meterChart.setChart(createMeterChart("已完成项目超期 ",
				getFinishProjectOverTimeRate()));
		meterChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		ChartCanvas meterChart2 = new ChartCanvas(composite, SWT.NONE);
		meterChart2.setChart(createMeterChart("进行中项目超期 ",
				getProcessProjectOverTimeRate()));
		meterChart2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		ChartCanvas meterChart3 = new ChartCanvas(composite, SWT.NONE);
		meterChart3.setChart(createMeterChart("整体项目超期 ",
				getAllProjectOverTimeRate()));
		meterChart3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		meterTabItem.setControl(composite);

	}

	private Chart createMeterChart(String chartCaptionText, double value) {
		String[] oValues = new String[] { "进度延迟" };

		DialChart chart = (DialChart) DialChartImpl.create();
		chart.setDialSuperimposition(false);

		// 使用标准的仪表盘
		chart.setType("Standard Meter"); //$NON-NLS-1$  
		//		dChart.setSubType("Superimposed Meter Chart"); //$NON-NLS-1$
		//		chart.setSubType("Standard Meter"); //$NON-NLS-1$

		// chart.setDialSuperimposition(true);
		// chart.setGridColumnCount(1);

		// Title/Plot
		chart.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		chart.getPlot().getClientArea().setVisible(false);
		chart.setCoverage(1.8);
		// chart.setSeriesThickness(2);

		TitleBlock title = chart.getTitle();
		title.getOutline().setVisible(false);
		// title.getLabel().setVisible(false);
		Text caption = title.getLabel().getCaption();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		caption.setValue(chartCaptionText + (nf.format(value)) + "%");//$NON-NLS-1$
		adjustFont(caption.getFont(), NORMAL_SIZE);

		// Legend
		Legend legend = chart.getLegend();
		// legend.setItemType(LegendItemType.SERIES_LITERAL);
		legend.setVisible(false);
		// adjustFont(legend.getText().getFont(), NORMAL_SIZE);

		TextDataSet categoryValues = TextDataSetImpl.create(oValues);//$NON-NLS-1$

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData base = DataFactory.eINSTANCE.createBaseSampleData();
		base.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(base);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		// OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE
		// .createOrthogonalSampleData();
		//		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		// sdOrthogonal2.setSeriesDefinitionIndex(1);
		// sd.getOrthogonalSampleData().add(sdOrthogonal2);
		//
		// OrthogonalSampleData sdOrthogonal3 = DataFactory.eINSTANCE
		// .createOrthogonalSampleData();
		//		sdOrthogonal3.setDataSetRepresentation("");//$NON-NLS-1$
		// sdOrthogonal3.setSeriesDefinitionIndex(2);
		// sd.getOrthogonalSampleData().add(sdOrthogonal3);

		chart.setSampleData(sd);

		SeriesDefinition sdBase = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add(sdBase);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sdBase.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		// final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(),
		// ColorDefinitionImpl.ORANGE(), ColorDefinitionImpl.WHITE() };
		// sdCity.getSeriesPalette().getEntries().clear();
		// for (int i = 0; i < fiaOrth.length; i++) {
		// sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		// }

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { value }));
		seDial1.setSeriesIdentifier("超期率");//$NON-NLS-1$
		seDial1.getNeedle().setDecorator(LineDecorator.ARROW_LITERAL);

		Dial dial = seDial1.getDial();
		dial.setFill(GradientImpl.create(getRGBColorDefinition("#b5b5b5"),
				getRGBColorDefinition("#ffffff"), -90, false));

		dial.setStartAngle(0);
		dial.setStopAngle(180);
		// dial.getMajorGrid().getTickAttributes().setVisible(false);
		dial.getMinorGrid().getTickAttributes().setVisible(false);
		dial.getLabel().setVisible(false);
		seDial1.getDial().getMajorGrid().getTickAttributes()
				.setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		dial.getScale().setMin(NumberDataElementImpl.create(0));
		dial.getScale().setMax(NumberDataElementImpl.create(100));
		dial.getScale().setStep(10);
		// FontDefinition font = dial.getLabel().getCaption().getFont();
		// adjustFont(font, SMALL_SIZE);

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(GradientImpl.create(
				getRGBColorDefinition(Utils.COLOR_RED[0]),
				getRGBColorDefinition(Utils.COLOR_RED[10]), 45, false));
		dregion1.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
				.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(100));
		dial.getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(GradientImpl.create(
				getRGBColorDefinition(Utils.COLOR_YELLOW[0]),
				getRGBColorDefinition(Utils.COLOR_YELLOW[10]), 45, true));
		dregion2.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
				.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		// 设置内部半径，和外部半径，通过以下两句，使色彩区域变成yigezh
		// dregion1.setInnerRadius(40);
		// dregion1.setOuterRadius(-1);
		dial.getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(GradientImpl.create(
				getRGBColorDefinition(Utils.COLOR_GREEN[0]),
				getRGBColorDefinition(Utils.COLOR_GREEN[10]), 90, true));
		dregion3.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
				.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		// 设置内部半径，和外部半径，通过以下两句，使色彩区域变成yigezh
		// dregion1.setInnerRadius(40);
		// dregion1.setOuterRadius(-1);
		dial.getDialRegions().add(dregion3);
		// DialRegion dregion2 = DialRegionImpl.create();
		// dregion2.setFill(ColorDefinitionImpl.YELLOW());
		// dregion2.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
		// .BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		// dregion2.setStartValue(NumberDataElementImpl.create(40));
		// dregion2.setEndValue(NumberDataElementImpl.create(70));
		// dregion2.setOuterRadius(70);
		// seDial1.getDial().getDialRegions().add(dregion2);
		//
		// DialRegion dregion3 = DialRegionImpl.create();
		// dregion3.setFill(ColorDefinitionImpl.RED());
		// dregion3.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
		// .BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		// dregion3.setStartValue(NumberDataElementImpl.create(0));
		// dregion3.setEndValue(NumberDataElementImpl.create(40));
		// dregion3.setInnerRadius(40);
		// dregion3.setOuterRadius(90);
		// seDial1.getDial().getDialRegions().add(dregion3);

		//
		chart.setDialSuperimposition(false);
		sdBase.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);

		return chart;
	}

	private Chart createPieChart() {
		String pieChartCaption = "进度摘要";
		String[] texts = getSchedualParameterNames();
		double[] values = getSchedualParameterValues();
		double maxValue = Utils.max(values);

		// 正常完成
		ColorDefinition color1 = getRGBColorDefinition(Utils.COLOR_GREEN[10]);
		// ColorDefinition color1_1 =
		// getRGBColorDefinition(Utils.COLOR_GREEN[2]);

		// "超期完成"
		ColorDefinition color2 = getRGBColorDefinition("#00a99d");
		// ColorDefinition color2_1 =
		// getRGBColorDefinition(Utils.COLOR_GREEN[2]);

		// "进度延迟"
		ColorDefinition color3 = getRGBColorDefinition(Utils.COLOR_RED[5]);
		// ColorDefinition color3_1 = getRGBColorDefinition(Utils.COLOR_RED[2]);

		// "预期延迟"
		ColorDefinition color4 = getRGBColorDefinition(Utils.COLOR_YELLOW[5]);
		// ColorDefinition color4_1 =
		// getRGBColorDefinition(Utils.COLOR_YELLOW[2]);

		// 正常进行
		ColorDefinition color5 = getRGBColorDefinition(Utils.COLOR_BLUE[5]);
		// ColorDefinition color5_1 =
		// getRGBColorDefinition(Utils.COLOR_BLUE[2]);

		// final Fill[] fiaBase = {
		// GradientImpl.create(color1, color1_1, -90, true),
		// GradientImpl.create(color2, color2_1, -90, true),
		// GradientImpl.create(color3, color3_1, -90, true),
		// GradientImpl.create(color4, color4_1, -90, true),
		// GradientImpl.create(color5, color5_1, -90, true),
		// };

		final Fill[] fiaBase = { color1, color2, color3, color4, color5 };

		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
		// chart.setScript(FilterActionHandler.class.getName());
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		// chart.setSeriesThickness(2);//设置厚度
		chart.setMinSlice(maxValue / 20);// 最大的十分之一
		chart.setMinSliceLabel("其他");
		chart.setMinSlicePercent(true);
		Text caption = chart.getTitle().getLabel().getCaption();
		caption.setValue(pieChartCaption);
		adjustFont(caption.getFont(), STRONG_SIZE);
		Legend legend = chart.getLegend();
		legend.setItemType(LegendItemType.CATEGORIES_LITERAL);
		legend.setVisible(true);
		adjustFont(legend.getText().getFont(), NORMAL_SIZE);
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
		sePie.setExplosion(2);
		sePie.setRotation(40);

		/**
		 * 使用反馈
		 */
		// EList<Trigger> triggers = sePie.getTriggers();
		// ActionValue a = CallBackValueImpl.create("onclick");
		// triggers.add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
		// ActionImpl.create(ActionType.CALL_BACK_LITERAL, a)));

		// sePie.setTranslucent(true);//设置半透明
		sePie.setLabelPosition(Position.INSIDE_LITERAL);// 设置在内部显示数字
		adjustFont(sePie.getLabel().getCaption().getFont(), NORMAL_SIZE);// 设置字体

		SeriesDefinition sdef = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdef);
		sdef.getSeries().add(sePie);

		return chart;
	}

	private ColorDefinition getRGBColorDefinition(String colorCode) {
		int[] rgb = Utils.getRGB(colorCode);// "正常完成"
		return ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
	}

	private double getFinishProjectOverTimeRate() {
		// "正常完成"
		int value1 = data.summaryData.finished_normal;
		// "超期完成",
		int value2 = data.summaryData.finished_delay;
		// "提前完成",
		int value3 = data.summaryData.finished_advance;
		return 100 * value2 / (value1 + value2 + value3);
	}

	private double getAllProjectOverTimeRate() {
		// "正常完成"
		int value1 = data.summaryData.finished_normal;
		// "超期完成",
		int value2 = data.summaryData.finished_delay;
		// "提前完成",
		int value3 = data.summaryData.finished_advance;
		// "进度延迟",
		int value4 = data.summaryData.processing_delay;
		// "正常进行"
		int value5 = data.summaryData.processing_normal;
		// 进度提前
		int value6 = data.summaryData.processing_advance;
		return 100d * (value2 + value4)
				/ (value1 + value2 + value3 + value4 + value5 + value6);
	}

	private double getProcessProjectOverTimeRate() {

		// "进度延迟",
		// "进度延迟",
		int value4 = data.summaryData.processing_delay;
		// "正常进行"
		int value5 = data.summaryData.processing_normal;
		// 进度提前
		int value6 = data.summaryData.processing_advance;
		return 100d * value4 / (value4 + value5 + value6);
	}

	private double[] getSchedualParameterValues() {
		// "正常完成"
		int value1 = data.summaryData.finished_normal;
		// "超期完成",
		int value2 = data.summaryData.finished_delay;
		// "提前完成",
		int value3 = data.summaryData.finished_advance;
		// "进度延迟",
		int value4 = data.summaryData.processing_delay;
		// "正常进行"
		int value5 = data.summaryData.processing_normal;
		// 进度提前
		int value6 = data.summaryData.processing_advance;
		return new double[] { (value1 + value3), value2, value4, value5, value6 };
	}

	private String[] getSchedualParameterNames() {
		return new String[] { "正常完成", "超期完成", "进度延迟", "正常进行", "进度提前" };
	}

	public static Chart createStackedBar() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Stacked"); //$NON-NLS-1$
		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Stacked Bar Chart"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Item 1", "Item 2", "Item 3", "Item 4", "Item 5" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] {
				25, 35, 15, 5, 20 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 5,
				10, 25, 10, 5 });

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal2.setSeriesDefinitionIndex(1);
		sd.getOrthogonalSampleData().add(sdOrthogonal2);

		cwaBar.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setDataSet(orthoValues1);
		bs1.setStacked(true);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setDataSet(orthoValues2);
		bs2.setStacked(true);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
	}

	public Chart createStackedBarChart() {
		// Data Set
		String[] deptParameter = new String[] { "弹性元件\n事业本部", "弹性元件\n事业就本",
				"技术中心", "技术中心\n产检组", "绝缘公司" };
		double[] deptValue1 = new double[] { 10, 21, 31, 18, 40 };
		double[] deptValue2 = new double[] { 5, 10, 25, 10, 5 };

		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Stacked"); //$NON-NLS-1$
		cwaBar.setOrientation(Orientation.HORIZONTAL_LITERAL);
		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		cwaBar.getBlock().getOutline().setVisible(false);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("部门项目执行状况"); //$NON-NLS-1$
		adjustFont(cwaBar.getTitle().getLabel().getCaption().getFont(),
				STRONG_SIZE);
		// Legend
		// Legend lg = cwaBar.getLegend();
		// lg.setItemType(LegendItemType.SERIES_LITERAL);
		cwaBar.getLegend().setVisible(false);
		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		FontDefinition font = xAxisPrimary.getLabel().getCaption().getFont();
		adjustFont(font, NORMAL_SIZE);
		font.setRotation(-90);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		font = yAxisPrimary.getLabel().getCaption().getFont();
		adjustFont(font, NORMAL_SIZE);

		// 取数
		TextDataSet categoryValues = TextDataSetImpl.create(deptParameter); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(deptValue1);
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(deptValue2);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal2.setSeriesDefinitionIndex(1);
		sd.getOrthogonalSampleData().add(sdOrthogonal2);

		// 绑定
		cwaBar.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setDataSet(orthoValues1);
		bs1.setStacked(true);
		bs1.getLabel().setVisible(true);
		font = bs1.getLabel().getCaption().getFont();
		adjustFont(font, SMALL_SIZE);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setDataSet(orthoValues2);
		bs2.setStacked(true);
		bs2.getLabel().setVisible(true);
		font = bs2.getLabel().getCaption().getFont();
		adjustFont(font, SMALL_SIZE);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
	}

	private void adjustFont(FontDefinition font, int size) {
		font.setSize(size);
		font.setName("Segoe");
	}

}
