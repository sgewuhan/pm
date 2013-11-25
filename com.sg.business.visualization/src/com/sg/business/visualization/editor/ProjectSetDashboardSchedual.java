package com.sg.business.visualization.editor;

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
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetDashboardSchedual extends AbstractProjectPage {

	private ChartCanvas statusPieChart;
	private ChartCanvas finishedProjectMeter;
	private ChartCanvas processProjectMeter;
	private ChartCanvas allProjectMeter;
	private ChartCanvas deptProjectBar;

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

		TabFolder tabFolder = new TabFolder(parent, SWT.TOP);
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("进度摘要");
		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE);
		pieTabItem.setControl(statusPieChart);
		
		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("仪表盘");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE);
		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		processProjectMeter = new ChartCanvas(composite, SWT.NONE);
		processProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		allProjectMeter = new ChartCanvas(composite, SWT.NONE);
		allProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		meterTabItem.setControl(composite);



		TabItem barTabItem = new TabItem(tabFolder, SWT.NONE);
		barTabItem.setText("部门");
		deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE);
		barTabItem.setControl(deptProjectBar);

		loadChartData();
	}

	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		// doquery
		if (navi != null) {
			ViewerControl viewerControl = navi.getViewerControl();
			if (!viewerControl.getControl().isDisposed()) {
				viewerControl.doReloadData(true,new Runnable() {
					
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
		int sum = value1 + value2 + value3 + value4 + value5 + value6;
		double allProjectOverTimeRate = sum==0?0:(100d * (value2 + value4) /sum);
		
		sum = value4 + value5 + value6;
		double processProjectOverTimeRate = sum==0?0:(100d *  value4 /sum);

		sum = value1 + value2 + value3;
		double finishProjectOverTimeRate  = sum==0?0:(100d *  value2 /sum);
		
		finishedProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"已完成项目超期 ", "进度延迟", finishProjectOverTimeRate));
		processProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"进行中项目超期 ", "进度延迟", processProjectOverTimeRate));
		allProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"整体项目超期 ", "进度延迟", allProjectOverTimeRate));

		
		String pieChartCaption = "进度摘要";
		String[] texts = new String[] { "正常完成", "超期完成", "进度延迟", "正常进行", "进度提前" };
		double[] values = new double[] { (value1 + value3), value2, value4, value5, value6 };
		statusPieChart.setChart(ProjectChartFoctory.createPieChart(
				pieChartCaption, texts, values));

		String[] deptParameter = new String[data.summaryData.subOrganizationProjectProvider
				.size()];
		double[] deptValue1 = new double[data.summaryData.subOrganizationProjectProvider
				.size()];
		double[] deptValue2 = new double[data.summaryData.subOrganizationProjectProvider
				.size()];
		for (int i = 0; i < deptParameter.length; i++) {
			ProjectProvider projectProvider = data.summaryData.subOrganizationProjectProvider
					.get(i);
			projectProvider.setParameters(data.parameters);
			projectProvider.getData();
			deptParameter[i] = projectProvider.getDesc();
			deptValue1[i] = projectProvider.summaryData.processing_normal
					+ projectProvider.summaryData.processing_advance;
			deptValue2[i] = projectProvider.summaryData.processing_delay;
		}
		deptProjectBar.setChart(ProjectChartFoctory.createStackedBarChart(
				deptParameter, deptValue1, deptValue2));
	}


	private void redrawChart() {
		allProjectMeter.redrawChart();
		deptProjectBar.redrawChart();
		finishedProjectMeter.redrawChart();
		processProjectMeter.redrawChart();
		statusPieChart.redrawChart();
	}

}
