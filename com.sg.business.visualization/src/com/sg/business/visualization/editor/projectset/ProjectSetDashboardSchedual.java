package com.sg.business.visualization.editor.projectset;

import com.sg.business.visualization.nls.Messages;

public class ProjectSetDashboardSchedual extends AbstractProjectSetTableDetail {

//	private ChartCanvas statusPieChart;
//	private ChartCanvas finishedProjectMeter;
//	private ChartCanvas processProjectMeter;
//	private ChartCanvas allProjectMeter;
//	private ChartCanvas deptProjectBar;
//	private ChartCanvas pmProjectBar;
//	private CTabItem deptBarTabItem;
//	private CTabItem pmBarTabItem;
//	private CTabFolder tabFolder;
//
//	@Override
//	protected Composite createContent(Composite body) {
//		//
//		SashForm content = new SashForm(body, SWT.HORIZONTAL);
//		Composite tableContent = new Composite(content, SWT.NONE);
//		navi.createPartContent(tableContent);
//
//		Composite graphicContent = new Composite(content, SWT.NONE);
//		graphicContent.setLayout(new FillLayout());
//		createGraphic(graphicContent);
//
//		content.setWeights(new int[] { 3, 2 });
//
//		return content;
//	}
//
//	@Override
//	protected String getProjectSetPageLabel() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:13pt'>"); //$NON-NLS-1$
//		sb.append(data.getProjectSetName() + Messages.get().ProjectSetDashboardSchedual_1);
//		sb.append("</span>"); //$NON-NLS-1$
//		return sb.toString();
//	}
//
//	private void createGraphic(Composite parent) {
//
//		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.FLAT);
//		CTabItem pieTabItem = new CTabItem(tabFolder, SWT.NONE);
//		pieTabItem.setText(Messages.get().ProjectSetDashboardSchedual_3);
//		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory.getSchedualStatusPieChart(data);
//			}
//		};
//		pieTabItem.setControl(statusPieChart);
//
//		CTabItem meterTabItem = new CTabItem(tabFolder, SWT.NONE);
//		meterTabItem.setText(Messages.get().ProjectSetDashboardSchedual_4);
//		Composite composite = new Composite(tabFolder, SWT.NONE);
//		composite.setLayout(new GridLayout());
//		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory
//						.getFinishedProjectSchedualMeter(data);
//			}
//		};
//		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
//				true, true, 1, 1));
//
//		processProjectMeter = new ChartCanvas(composite, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory
//						.getProcessProjectSchedualMeterChart(data);
//			}
//		};
//		processProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
//				true, true, 1, 1));
//
//		allProjectMeter = new ChartCanvas(composite, SWT.NONE) {
//
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory
//						.getProjectSchedualMeter(data);
//			}
//
//		};
//		allProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
//				true, 1, 1));
//		meterTabItem.setControl(composite);
//		loadChartData();
//		tabFolder.setSelection(0);
//	}
//
//	@Override
//	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
//		// doquery
//		if (navi != null) {
//			ViewerControl viewerControl = navi.getViewerControl();
//			if (!viewerControl.getControl().isDisposed()) {
//				viewerControl.doReloadData(true, new Runnable() {
//
//					@Override
//					public void run() {
//						loadChartData();
//						redrawChart();
//					}
//				});
//			}
//		}
//		if (filterLabel != null && !filterLabel.isDisposed()) {
//			filterLabel.setText(getParameterText());
//			header.layout();
//		}
//
//	}
//
//	private void loadChartData() {
//		// *****************************************************************************************
//		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
//				.size()];
//		if (deptParameter.length != 0) {
//			if (deptBarTabItem == null) {
//				deptBarTabItem = new CTabItem(tabFolder, SWT.NONE);
//				deptBarTabItem.setText(Messages.get().ProjectSetDashboardSchedual_5);
//				deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
//					@Override
//					public Chart getChart() {
//						return ProjectChartFactory
//								.getDeptSchedualBar(data);
//					}
//				};
//				deptBarTabItem.setControl(deptProjectBar);
//			}
//		}
//		// *****************************************************************************************
//		String[] chargerName = new String[data.sum.subChargerProjectProvider
//				.size()];
//		if (chargerName.length != 0) {
//			if (pmBarTabItem == null) {
//				pmBarTabItem = new CTabItem(tabFolder, SWT.NONE);
//				pmBarTabItem.setText(Messages.get().ProjectSetDashboardSchedual_6);
//				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
//					@Override
//					public Chart getChart() {
//						return ProjectChartFactory
//								.getChargerSchedualBar(data);
//					}
//				};
//
//				pmBarTabItem.setControl(pmProjectBar);
//			}
//		}
//
//	}
//
//	private void redrawChart() {
//		try {
//			allProjectMeter.redrawChart();
//			deptProjectBar.redrawChart();
//			finishedProjectMeter.redrawChart();
//			processProjectMeter.redrawChart();
//			statusPieChart.redrawChart();
//		} catch (Exception e) {
//			MessageUtil.showToast(e);
//		}
//	}

	@Override
	protected String getTitle() {
		return  Messages.get().ProjectSetDashboardSchedual_1;
	}

}
