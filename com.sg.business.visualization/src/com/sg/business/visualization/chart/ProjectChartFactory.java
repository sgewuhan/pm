package com.sg.business.visualization.chart;

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

import com.mobnut.commons.util.Utils;
import com.sg.business.model.ProjectProvider;

public class ProjectChartFactory {

	private static final int STRONG_SIZE = 9;

	private static final int NORMAL_SIZE = 9;

	private static final int SMALL_SIZE = 8;

	// 正常完成
	private static final ColorDefinition color1 = getRGBColorDefinition(Utils.COLOR_GREEN[10]);

	// "超期完成"
	private static final ColorDefinition color2 = getRGBColorDefinition("#00a99d");

	// "进度延迟"
	private static final ColorDefinition color3 = getRGBColorDefinition(Utils.COLOR_RED[5]);

	// "预期延迟"
	private static final ColorDefinition color4 = getRGBColorDefinition(Utils.COLOR_YELLOW[5]);

	// 正常进行
	private static final ColorDefinition color5 = getRGBColorDefinition(Utils.COLOR_BLUE[5]);

	private static final Fill[] fiaBase = { color1, color2, color3, color4,
			color5 };

	public static Chart createPieChart(String pieChartCaption, String[] texts,
			double[] values) {
		// double maxValue = Utils.max(values);
		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		// chart.setMinSlice(maxValue / 50);// 最大的十分之一
		// chart.setMinSliceLabel("其他");
		// chart.setMinSlicePercent(true);
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

		sePie.setLabelPosition(Position.INSIDE_LITERAL);// 设置在内部显示数字
		adjustFont(sePie.getLabel().getCaption().getFont(), NORMAL_SIZE);// 设置字体

		SeriesDefinition sdef = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdef);
		sdef.getSeries().add(sePie);

		return chart;
	}

	public static Chart createStackedBarChart(String title,
			String[] deptParameter, double[] deptValue1, double[] deptValue2,
			String[] seriesTitle) {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Stacked"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		cwaBar.getBlock().getOutline().setVisible(false);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue(title); //$NON-NLS-1$
		adjustFont(cwaBar.getTitle().getLabel().getCaption().getFont(),
				STRONG_SIZE);
		Legend lg = cwaBar.getLegend();
		lg.setItemType(LegendItemType.SERIES_LITERAL);
		adjustFont(lg.getText().getFont(), NORMAL_SIZE);
		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		FontDefinition font = xAxisPrimary.getLabel().getCaption().getFont();
		adjustFont(font, NORMAL_SIZE);
		font.setRotation(-45);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		font = yAxisPrimary.getLabel().getCaption().getFont();
		adjustFont(font, NORMAL_SIZE);

		// 取数
		TextDataSet categoryValues = TextDataSetImpl.create(deptParameter);
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
		bs1.setSeriesIdentifier(seriesTitle[0]);
		bs1.setStacked(true);
		bs1.getLabel().setVisible(true);
		font = bs1.getLabel().getCaption().getFont();
		adjustFont(font, SMALL_SIZE);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setDataSet(orthoValues2);
		bs2.setSeriesIdentifier(seriesTitle[1]);
		bs2.setStacked(true);
		bs2.getLabel().setVisible(true);
		font = bs2.getLabel().getCaption().getFont();
		adjustFont(font, SMALL_SIZE);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// sdY.getSeriesPalette().getEntries().clear();
		// // "预期延迟"
		// ColorDefinition color1 =
		// getRGBColorDefinition(Utils.COLOR_YELLOW[5]);
		// // 正常进行
		// ColorDefinition color2 = getRGBColorDefinition(Utils.COLOR_BLUE[5]);
		// final Fill[] fiaBase = { color1, color2 };
		// for (int i = 0; i < fiaBase.length; i++) {
		// sdY.getSeriesPalette().getEntries().add(fiaBase[i]);
		// }
		// sdY.getSeriesPalette().shift(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
	}

	public static Chart createMeterChart(String chartCaptionText, String label,
			double value) {
		String[] oValues = new String[] { label };

		DialChart chart = (DialChart) DialChartImpl.create();
		chart.setDialSuperimposition(false);

		// 使用标准的仪表盘
		chart.setType("Standard Meter"); //$NON-NLS-1$  

		// Title/Plot
		chart.getBlock().setBackground(ColorDefinitionImpl.TRANSPARENT());
		chart.getPlot().getClientArea().setVisible(false);
		chart.setCoverage(1);

		TitleBlock title = chart.getTitle();
		title.getOutline().setVisible(false);
		Text caption = title.getLabel().getCaption();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		caption.setValue(chartCaptionText + (nf.format(value)) + "%");//$NON-NLS-1$
		adjustFont(caption.getFont(), STRONG_SIZE);

		// Legend
		Legend legend = chart.getLegend();
		legend.setVisible(false);

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

		chart.setSampleData(sd);

		SeriesDefinition sdBase = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add(sdBase);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sdBase.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

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
		dial.getMinorGrid().getTickAttributes().setVisible(false);
		dial.getLabel().setVisible(false);
		seDial1.getDial().getMajorGrid().getTickAttributes()
				.setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		dial.getScale().setMin(NumberDataElementImpl.create(0));
		dial.getScale().setMax(NumberDataElementImpl.create(100));
		dial.getScale().setStep(10);

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
		dregion2.setStartValue(NumberDataElementImpl.create(30));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dial.getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(GradientImpl.create(
				getRGBColorDefinition(Utils.COLOR_GREEN[0]),
				getRGBColorDefinition(Utils.COLOR_GREEN[10]), 90, true));
		dregion3.setOutline(LineAttributesImpl.create(ColorDefinitionImpl
				.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(30));
		dial.getDialRegions().add(dregion3);

		//
		sdBase.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);

		return chart;
	}

	private static void adjustFont(FontDefinition font, int size) {
		font.setSize(size);
		font.setName("Segoe");
	}

	private static ColorDefinition getRGBColorDefinition(String colorCode) {
		int[] rgb = Utils.getRGB(colorCode);// "正常完成"
		return ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]);
	}

	public static Chart getSchedualStatusPieChart(ProjectProvider data) {
		// "正常完成"
		int value1 = data.sum.finished_normal;
		// "超期完成",
		int value2 = data.sum.finished_delay;
		// "提前完成",
		int value3 = data.sum.finished_advance;
		// "进度延迟",
		int value4 = data.sum.processing_delay;
		// "正常进行"
		int value5 = data.sum.processing_normal;
		// 进度提前
		int value6 = data.sum.processing_advance;

		String pieChartCaption = "进度摘要";
		String[] texts = new String[] { "正常完成", "超期完成", "进度延迟", "正常进行", "进度提前" };
		double[] values = new double[] { (value1 + value3), value2, value4,
				value5, value6 };
		return createPieChart(pieChartCaption, texts, values);
	}

	public static Chart getFinishedProjectSchedualMeter(ProjectProvider data) {
		// "正常完成"
		int value1 = data.sum.finished_normal;
		// "超期完成",
		int value2 = data.sum.finished_delay;
		// "提前完成",
		int value3 = data.sum.finished_advance;
		int sum = value1 + value2 + value3;
		double finishProjectOverTimeRate = sum == 0 ? 0 : (100d * value2 / sum);

		return createMeterChart("已完成项目超期 ", "进度延迟", finishProjectOverTimeRate);
	}

	public static Chart getProcessProjectSchedualMeterChart(ProjectProvider data) {
		// "进度延迟",
		int value4 = data.sum.processing_delay;
		// "正常进行"
		int value5 = data.sum.processing_normal;
		// 进度提前
		int value6 = data.sum.processing_advance;

		int sum = value4 + value5 + value6;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value4 / sum);

		return createMeterChart("进行中项目超期 ", "进度延迟", processProjectOverTimeRate);
	}

	public static Chart getProjectSchedualMeter(ProjectProvider data) {
		// "正常完成"
		int value1 = data.sum.finished_normal;
		// "超期完成",
		int value2 = data.sum.finished_delay;
		// "提前完成",
		int value3 = data.sum.finished_advance;
		// "进度延迟",
		int value4 = data.sum.processing_delay;
		// "正常进行"
		int value5 = data.sum.processing_normal;
		// 进度提前
		int value6 = data.sum.processing_advance;
		int sum = value1 + value2 + value3 + value4 + value5 + value6;
		double allProjectOverTimeRate = sum == 0 ? 0
				: (100d * (value2 + value4) / sum);
		return createMeterChart("总体项目超期率 ", "进度延迟", allProjectOverTimeRate);
	}

	public static Chart getDeptSchedualBar(ProjectProvider data) {
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue1 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue2 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		for (int i = 0; i < deptParameter.length; i++) {
			ProjectProvider projectProvider = data.sum.subOrganizationProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			deptParameter[i] = projectProvider.getDesc();
			deptValue1[i] = projectProvider.sum.processing_normal
					+ projectProvider.sum.processing_advance;
			deptValue2[i] = projectProvider.sum.processing_delay;
		}

		return createStackedBarChart("部门项目执行状况", deptParameter, deptValue1,
				deptValue2, new String[] { "正常", "超期" });
	}

	public static Chart getChargerSchedualBar(ProjectProvider data) {
		String[] chargerName = new String[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue1 = new double[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue2 = new double[data.sum.subChargerProjectProvider
				.size()];
		for (int i = 0; i < chargerName.length; i++) {
			ProjectProvider projectProvider = data.sum.subChargerProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			chargerName[i] = projectProvider.getDesc();
			userValue1[i] = projectProvider.sum.processing_normal
					+ projectProvider.sum.processing_advance;
			userValue2[i] = projectProvider.sum.processing_delay;
		}

		return createStackedBarChart("项目经理项目执行状况", chargerName, userValue1,
				userValue2, new String[] { "正常", "超期" });
	}

	public static Chart getFinishedProjectBudgetAndCostMeter(
			ProjectProvider data) {
		// "预算内完成"
		int value1 = data.sum.finished_cost_normal;
		// "超预算完成"
		int value2 = data.sum.finished_cost_over;
		int sum = value1 + value2;
		// 已经完成的项目的超支比例
		double finishedProjectOverCostRate = sum == 0 ? 0
				: (100d * value2 / sum);

		return createMeterChart("超支完成项目比例 ", "预算超支",
				finishedProjectOverCostRate);
	}

	public static Chart getProjectBudgetAndCostPie(ProjectProvider data) {
		// "预算内完成"
		int value1 = data.sum.finished_cost_normal;
		// "超预算完成"
		int value2 = data.sum.finished_cost_over;
		// "超支风险"
		int value3 = data.sum.processing_cost_over;
		// "正常运行"
		int value4 = data.sum.processing_cost_normal;

		String pieChartCaption = "预算使用摘要";
		String[] texts = new String[] { "预算内完成", "超预算完成", "超支风险", "正常运行" };
		double[] values = new double[] { value1, value2, value3, value4 };
		return createPieChart(pieChartCaption, texts, values);
	}

	public static Chart getProcessProjectBudgetAndCostMeter(ProjectProvider data) {
		// "超支风险"
		int value3 = data.sum.processing_cost_over;
		// "正常运行"
		int value4 = data.sum.processing_cost_normal;
		// 进行中项目的超支风险
		int sum = value4 + value3;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value3 / sum);

		return createMeterChart("进行中项目超支风险 ", "超支风险",
				processProjectOverTimeRate);
	}

	public static Chart getDeptBudgetAndCostBar(ProjectProvider data) {
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue1 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue2 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		for (int i = 0; i < deptParameter.length; i++) {
			ProjectProvider projectProvider = data.sum.subOrganizationProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			deptParameter[i] = projectProvider.getDesc();
			deptValue1[i] = projectProvider.sum.total_budget_amount / 10000;
			deptValue2[i] = projectProvider.sum.total_investment_amount / 10000;
		}
		return createStackedBarChart("部门项目预算执行状况", deptParameter, deptValue1,
				deptValue2, new String[] { "预算", "实际" });
	}

	public static Chart getChargerBudgetAndCostBar(ProjectProvider data) {
		String[] chargerName = new String[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue1 = new double[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue2 = new double[data.sum.subChargerProjectProvider
				.size()];
		for (int i = 0; i < chargerName.length; i++) {
			ProjectProvider projectProvider = data.sum.subChargerProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			chargerName[i] = projectProvider.getDesc();
			userValue1[i] = projectProvider.sum.total_budget_amount / 10000;
			userValue2[i] = projectProvider.sum.total_investment_amount / 10000;
		}
		return createStackedBarChart("项目经理预算执行状况", chargerName, userValue1,
				userValue2, new String[] { "预算", "实际" });
	}

	public static Chart getCostAndProfitPie(ProjectProvider data) {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long value3 = value1 - value2;

		String pieChartCaption = "销售成本及利润";
		String[] texts = new String[] { "销售成本", "销售利润" };
		double[] values = new double[] { value2 / 10000, value3 / 10000 };
		return createPieChart(pieChartCaption, texts, values);
	}

	public static Chart getProfitRateMeter(ProjectProvider data) {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long value3 = value1 - value2;
		// 利润率
		double profit = value1 == 0 ? 0 : 100d * value3 / value1;

		return createMeterChart("销售利润率", "销售收入", profit);
	}

	public static Chart getDeptRevenueBar(ProjectProvider data) {
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue1 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		double[] deptValue2 = new double[data.sum.subOrganizationProjectProvider
				.size()];
		for (int i = 0; i < deptParameter.length; i++) {
			ProjectProvider projectProvider = data.sum.subOrganizationProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			deptParameter[i] = projectProvider.getDesc();
			deptValue1[i] = projectProvider.sum.total_budget_amount / 10000;
			deptValue2[i] = projectProvider.sum.total_investment_amount / 10000;
		}
		return createStackedBarChart("按部门计算项目收入情况", deptParameter, deptValue1,
				deptValue2, new String[] { "销售收入", "销售利润" });
	}

	public static Chart getProjectChargerRevenueBar(ProjectProvider data) {
		String[] chargerName = new String[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue1 = new double[data.sum.subChargerProjectProvider
				.size()];
		double[] userValue2 = new double[data.sum.subChargerProjectProvider
				.size()];
		for (int i = 0; i < chargerName.length; i++) {
			ProjectProvider projectProvider = data.sum.subChargerProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			chargerName[i] = projectProvider.getDesc();
			userValue1[i] = projectProvider.sum.total_sales_revenue / 10000;
			userValue2[i] = (projectProvider.sum.total_sales_revenue - projectProvider.sum.total_sales_cost) / 10000;
		}
		return createStackedBarChart("按项目经理计算项目收入情况", chargerName, userValue1,
				userValue2, new String[] { "销售收入", "销售利润" });
	}

	public static Chart getROIMeter(ProjectProvider data) {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long profit = value1 - value2;

		// 期初资产
		long inv = data.sum.total_investment_amount;

		double roi = value1 == 0 ? 0 : 100d * profit / inv;

		return createMeterChart("ROI", "ROI", roi);
	}

}
