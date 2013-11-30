package com.sg.business.visualization.editor;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.chart.ProjectChartFoctory;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetDashboardBudInv extends AbstractProjectPage {

	private ChartCanvas statusPieChart;
	private ChartCanvas finishedProjectMeter;
	private ChartCanvas processProjectMeter;
	private ChartCanvas deptProjectBar;
	private ChartCanvas pmProjectBar;
	private TabItem deptBarTabItem;
	private TabItem pmBarTabItem;
	private TabFolder tabFolder;

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
		sb.append(projectSetName + " 项目进度状况");
		sb.append("</span>");
		return sb.toString();
	}

	private void createGraphic(Composite parent) {

		tabFolder = new TabFolder(parent, SWT.TOP);
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("预算及超支状况");
		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getStatusPieChartData();
			}
		};
		pieTabItem.setControl(statusPieChart);

		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("仪表盘");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getFinishedProjectMeterChartData();
			}
		};
		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		processProjectMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getProcessProjectMeterChartData();
			}
		};
		processProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		meterTabItem.setControl(composite);

		loadChartData();
	}

	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		// doquery
		if (navi != null) {
			ViewerControl viewerControl = navi.getViewerControl();
			if (!viewerControl.getControl().isDisposed()) {
				viewerControl.doReloadData(true, new Runnable() {

					@Override
					public void run() {
						loadChartData();
						redrawChart();
					}
				});
			}
		}
		if (filterLabel != null && !filterLabel.isDisposed()) {
			filterLabel.setText(getParameterText());
			header.layout();
		}

	}

	private void loadChartData() {

		// "预算内完成"
		int value1 = data.sum.finished_cost_normal;
		// "超预算完成"
		int value2 = data.sum.finished_cost_over;
		// "超支风险"
		int value3 = data.sum.processing_cost_over;
		// "正常运行"
		int value4 = data.sum.processing_cost_normal;

		// // "总预算"
		// long value5 = data.summaryData.total_budget_amount;
		// // "总研发成本"
		// long value6 = data.summaryData.total_investment_amount;

		int sum = value1 + value2;
		// 已经完成的项目的超支比例
		double finishedProjectOverCostRate = sum == 0 ? 0
				: (100d * value2 / sum);

		// 进行中项目的超支风险
		sum = value4 + value3;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value3 / sum);

		finishedProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"超支完成项目比例 ", "预算超支", finishedProjectOverCostRate));
		processProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"进行中项目超支风险 ", "超支风险", processProjectOverTimeRate));

		// *****************************************************************************************
		String pieChartCaption = "预算使用摘要";
		String[] texts = new String[] { "预算内完成", "超预算完成", "超支风险", "正常运行" };
		double[] values = new double[] { value1, value2, value3, value4 };
		statusPieChart.setChart(ProjectChartFoctory.createPieChart(
				pieChartCaption, texts, values));

		// *****************************************************************************************
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		if (deptParameter.length != 0) {
			if (deptBarTabItem == null) {
				deptBarTabItem = new TabItem(tabFolder, SWT.NONE);
				deptBarTabItem.setText("项目承担部门");
				deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
					@Override
					public Chart getChart() {
						return getDeptBarChartData();
					}
				};
				deptBarTabItem.setControl(deptProjectBar);
			}
		}
		// *****************************************************************************************
		String[] chargerName = new String[data.sum.subChargerProjectProvider
				.size()];
		if (chargerName.length != 0) {
			if (pmBarTabItem == null) {
				pmBarTabItem = new TabItem(tabFolder, SWT.NONE);
				pmBarTabItem.setText("项目经理");
				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
					@Override
					public Chart getChart() {
						return getProjectChargerBarCharData();
					}
				};
				pmBarTabItem.setControl(pmProjectBar);
			}
		}
	}

	protected Chart getProjectChargerBarCharData() {
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
		return ProjectChartFoctory.createStackedBarChart("项目经理预算执行状况",
				chargerName, userValue1, userValue2,
				new String[] { "预算", "实际" });
	}

	protected Chart getDeptBarChartData() {
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
		return ProjectChartFoctory.createStackedBarChart("部门项目预算执行状况",
				deptParameter, deptValue1, deptValue2, new String[] { "预算",
						"实际" });
	}

	protected Chart getProcessProjectMeterChartData() {
		// "超支风险"
		int value3 = data.sum.processing_cost_over;
		// "正常运行"
		int value4 = data.sum.processing_cost_normal;
		// 进行中项目的超支风险
		int sum = value4 + value3;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value3 / sum);

		return ProjectChartFoctory.createMeterChart("进行中项目超支风险 ", "超支风险",
				processProjectOverTimeRate);
	}

	protected Chart getFinishedProjectMeterChartData() {
		// "预算内完成"
		int value1 = data.sum.finished_cost_normal;
		// "超预算完成"
		int value2 = data.sum.finished_cost_over;
		int sum = value1 + value2;
		// 已经完成的项目的超支比例
		double finishedProjectOverCostRate = sum == 0 ? 0
				: (100d * value2 / sum);

		return ProjectChartFoctory.createMeterChart("超支完成项目比例 ", "预算超支",
				finishedProjectOverCostRate);
	}

	protected Chart getStatusPieChartData() {
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
		return ProjectChartFoctory.createPieChart(pieChartCaption, texts,
				values);
	}

	private void redrawChart() {
		try {
			deptProjectBar.redrawChart();
			finishedProjectMeter.redrawChart();
			processProjectMeter.redrawChart();
			statusPieChart.redrawChart();
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
