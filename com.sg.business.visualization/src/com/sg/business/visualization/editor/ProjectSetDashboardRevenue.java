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

public class ProjectSetDashboardRevenue extends AbstractProjectPage {

	private ChartCanvas revenuePieChart;

	// 利润率
	private ChartCanvas profitMarginMeter;
	// 成本
	private ChartCanvas salesCostMeter;
	// ROI
	private ChartCanvas ROIMeter;

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
		sb.append(projectSetName + "项目销售收入和利润状况");
		sb.append("</span>");
		return sb.toString();
	}

	private void createGraphic(Composite parent) {

		tabFolder = new TabFolder(parent, SWT.TOP);
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("成本及利润摘要");
		revenuePieChart = new ChartCanvas(tabFolder, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getRevenuePieChartData();
			}
		};
		pieTabItem.setControl(revenuePieChart);

		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("仪表盘");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		profitMarginMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getProfitMarginChartData();
			}
		};
		profitMarginMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		salesCostMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getSalesCostMeterChartData();
			}
		};
		salesCostMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		ROIMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getROIMeterChartData();
			}
		};
		ROIMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		meterTabItem.setControl(composite);

		loadChartData();
	}

	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
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

		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long value3 = value1 - value2;
		double ProfitMargin = value1 == 0 ? 0 : value3 / value1;
		double costOverRevenueRate = value1 == 0 ? 0 : value2 / value1;

		profitMarginMeter.setChart(ProjectChartFoctory.createMeterChart("利润",
				"销售收入", ProfitMargin));
		salesCostMeter.setChart(ProjectChartFoctory.createMeterChart("销售成本",
				"销售收入", costOverRevenueRate));

		// *****************************************************************************************
		String pieChartCaption = "成本利润摘要";
		String[] texts = new String[] { "成本", "利润" };
		double[] values = new double[] { value2, value3 };
		revenuePieChart.setChart(ProjectChartFoctory.createPieChart(
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
			userValue1[i] = projectProvider.sum.total_sales_revenue / 10000;
			userValue2[i] = (projectProvider.sum.total_sales_revenue-projectProvider.sum.total_sales_cost )/ 10000;
		}
		return ProjectChartFoctory.createStackedBarChart("项目经理项目收入情况",
				chargerName, userValue1, userValue2,
				new String[] { "收入", "利润" });
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
		return ProjectChartFoctory.createStackedBarChart("部门项目收入情况",
				deptParameter, deptValue1, deptValue2, new String[] { "收入",
						"利润" });
	}

	protected Chart getROIMeterChartData() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Chart getSalesCostMeterChartData() {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;

		double costOverRevenueRate = value1 == 0 ? 0 : value2 / value1;

		return ProjectChartFoctory.createMeterChart("销售成本", "销售收入",
				costOverRevenueRate);
	}

	protected Chart getProfitMarginChartData() {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long value3 = value1 - value2;
		// 利润率
		double ProfitMargin = value1 == 0 ? 0 : value3 / value1;

		return ProjectChartFoctory.createMeterChart("利润", "销售收入", ProfitMargin);
	}

	protected Chart getRevenuePieChartData() {
		// "销售收入"
		long value1 = data.sum.total_sales_revenue;
		// "销售成本
		long value2 = data.sum.total_sales_cost;
		// "利润"
		long value3 = value1 - value2;

		String pieChartCaption = "成本利润摘要";
		String[] texts = new String[] { "成本", "利润" };
		double[] values = new double[] { value2, value3 };
		return ProjectChartFoctory.createPieChart(pieChartCaption, texts,
				values);
	}

	private void redrawChart() {
		try {
			ROIMeter.redrawChart();
			deptProjectBar.redrawChart();
			profitMarginMeter.redrawChart();
			salesCostMeter.redrawChart();
			revenuePieChart.redrawChart();
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
