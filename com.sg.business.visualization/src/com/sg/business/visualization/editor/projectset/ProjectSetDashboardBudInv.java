package com.sg.business.visualization.editor.projectset;

import java.util.List;

import com.sg.business.resource.nls.Messages;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ProjectSetDashboardBudInv extends AbstractProjectSetTableDetail {

//	private ChartCanvas statusPieChart;
//	private ChartCanvas finishedProjectMeter;
//	private ChartCanvas processProjectMeter;
//	private ChartCanvas deptProjectBar;
//	private ChartCanvas pmProjectBar;
//	private CTabItem deptBarTabItem;
//	private CTabItem pmBarTabItem;
//	private CTabFolder tabFolder;

//	@Override
//	protected Composite createContent(Composite body) {
//		SashForm content = new SashForm(body, SWT.HORIZONTAL);
//		Composite tableContent = new Composite(content, SWT.NONE);
//		navi.createPartContent(tableContent);
//
//		Composite graphicContent = new Composite(content, SWT.NONE);
//		graphicContent.setLayout(new FillLayout());
//		createGraphic(graphicContent);
//
//		content.setWeights(new int[] { 3, 2 });
//		return content;
//	}

//	@Override
//	protected String getProjectSetPageLabel() {
//		String projectSetName = data.getProjectSetName();
//		StringBuffer sb = new StringBuffer();
//		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:13pt'>"); //$NON-NLS-1$
//		sb.append(projectSetName + Messages.get().ProjectSetDashboardBudInv_1);
//		sb.append("</span>"); //$NON-NLS-1$
//		return sb.toString();
//	}

//	private void createGraphic(Composite parent) {
//
//		tabFolder = new CTabFolder(parent, SWT.TOP);
//		CTabItem pieTabItem = new CTabItem(tabFolder, SWT.NONE);
//		pieTabItem.setText(Messages.get().ProjectSetDashboardBudInv_3);
//		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory.getProjectBudgetAndCostPie(data);
//			}
//		};
//		pieTabItem.setControl(statusPieChart);
//
//		CTabItem meterTabItem = new CTabItem(tabFolder, SWT.NONE);
//		meterTabItem.setText(Messages.get().ProjectSetDashboardBudInv_4);
//		Composite composite = new Composite(tabFolder, SWT.NONE);
//		composite.setLayout(new GridLayout());
//		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory.getFinishedProjectBudgetAndCostMeter(data);
//			}
//		};
//		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
//				true, true, 1, 1));
//
//		processProjectMeter = new ChartCanvas(composite, SWT.NONE) {
//			@Override
//			public Chart getChart() {
//				return ProjectChartFactory.getProcessProjectBudgetAndCostMeter(data);
//			}
//		};
//		processProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
//				true, true, 1, 1));
//
//		meterTabItem.setControl(composite);
//
//		
//		loadChartData();
//		tabFolder.setSelection(0);
//	}

//	@Override
//	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
//		// doquery
//		if (navi != null) {
//			ViewerControl viewerControl = navi.getViewerControl();
//			if (!viewerControl.getControl().isDisposed()) {
//				viewerControl.doReloadData(true, null);
//			}
//		}
//		if (filterLabel != null && !filterLabel.isDisposed()) {
//			filterLabel.setText(getParameterText());
//			header.layout();
//		}
//
//	}

//	private void loadChartData() {
//
//		// *****************************************************************************************
//		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
//				.size()];
//		if (deptParameter.length != 0) {
//			if (deptBarTabItem == null) {
//				deptBarTabItem = new CTabItem(tabFolder, SWT.NONE);
//				deptBarTabItem.setText(Messages.get().ProjectSetDashboardBudInv_5);
//				deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
//					@Override
//					public Chart getChart() {
//						return ProjectChartFactory.getDeptBudgetAndCostBar(data);
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
//				pmBarTabItem.setText(Messages.get().ProjectSetDashboardBudInv_6);
//				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
//					@Override
//					public Chart getChart() {
//						return ProjectChartFactory.getChargerBudgetAndCostBar(data);
//					}
//				};
//				pmBarTabItem.setControl(pmProjectBar);
//			}
//		}
//	}

//	private void redrawChart() {
//		try {
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
		return  Messages.get().ProjectSetDashboardBudInv_3;
	}
	
	@Override
	protected List<ColumnSorter> getColumnSorters(ColumnConfigurator conf) {
		if(conf.getColumn().equals("budget")){
			return getSortOfBudgetColumn();
		}
		return null;
	}

}
