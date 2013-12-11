package com.sg.business.visualization.editor.projectset;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.ProjectChartFactory;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetDashboardSchedual extends AbstractProjectSetPage {

	private ChartCanvas statusPieChart;
	private ChartCanvas finishedProjectMeter;
	private ChartCanvas processProjectMeter;
	private ChartCanvas allProjectMeter;
	private ChartCanvas deptProjectBar;
	private ChartCanvas pmProjectBar;
	private CTabItem deptBarTabItem;
	private CTabItem pmBarTabItem;
	private CTabFolder tabFolder;

	@Override
	protected Composite createContent(Composite body) {
		//
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

		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.FLAT);
		CTabItem pieTabItem = new CTabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("进度摘要");
		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory.getSchedualStatusPieChart(data);
			}
		};
		pieTabItem.setControl(statusPieChart);

		CTabItem meterTabItem = new CTabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("仪表盘");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory
						.getFinishedProjectSchedualMeter(data);
			}
		};
		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		processProjectMeter = new ChartCanvas(composite, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory
						.getProcessProjectSchedualMeterChart(data);
			}
		};
		processProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		allProjectMeter = new ChartCanvas(composite, SWT.NONE) {

			@Override
			public Chart getChart() {
				return ProjectChartFactory
						.getProjectSchedualMeter(data);
			}

		};
		allProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		meterTabItem.setControl(composite);
		loadChartData();
		tabFolder.setSelection(0);
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
		// *****************************************************************************************
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		if (deptParameter.length != 0) {
			if (deptBarTabItem == null) {
				deptBarTabItem = new CTabItem(tabFolder, SWT.NONE);
				deptBarTabItem.setText("项目承担部门");
				deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
					@Override
					public Chart getChart() {
						return ProjectChartFactory
								.getDeptSchedualBar(data);
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
				pmBarTabItem = new CTabItem(tabFolder, SWT.NONE);
				pmBarTabItem.setText("项目经理");
				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
					@Override
					public Chart getChart() {
						return ProjectChartFactory
								.getChargerSchedualBar(data);
					}
				};

				pmBarTabItem.setControl(pmProjectBar);
			}
		}

	}

	private void redrawChart() {
		try {
			allProjectMeter.redrawChart();
			deptProjectBar.redrawChart();
			finishedProjectMeter.redrawChart();
			processProjectMeter.redrawChart();
			statusPieChart.redrawChart();
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
