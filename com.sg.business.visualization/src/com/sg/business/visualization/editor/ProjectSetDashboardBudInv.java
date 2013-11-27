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
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		sb.append(projectSetName + " ��Ŀ����״��");
		sb.append("</span>");
		return sb.toString();
	}

	private void createGraphic(Composite parent) {

		tabFolder = new TabFolder(parent, SWT.TOP);
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("Ԥ�㼰��֧״��");
		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE);
		pieTabItem.setControl(statusPieChart);

		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("�Ǳ���");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		finishedProjectMeter = new ChartCanvas(composite, SWT.NONE);
		finishedProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		processProjectMeter = new ChartCanvas(composite, SWT.NONE);
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
		
		// "Ԥ�������"
		int value1 = data.summaryData.finished_cost_normal;
		// "��Ԥ�����"
		int value2 = data.summaryData.finished_cost_over;
		// "��֧����"
		int value3 = data.summaryData.processing_cost_over;
		// "��������"
		int value4 = data.summaryData.processing_cost_normal;
		
//		// "��Ԥ��"
//		long value5 = data.summaryData.total_budget_amount;
//		// "���з��ɱ�"
//		long value6 = data.summaryData.total_investment_amount;
		
		int sum = value1 + value2;
		//�Ѿ���ɵ���Ŀ�ĳ�֧����
		double finishedProjectOverCostRate = sum == 0 ? 0
				: (100d * value2 / sum);

		//��������Ŀ�ĳ�֧����
		sum = value4 + value3;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value3 / sum);

		finishedProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"��֧�����Ŀ���� ", "Ԥ�㳬֧", finishedProjectOverCostRate));
		processProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
				"��������Ŀ��֧���� ", "��֧����", processProjectOverTimeRate));

		// *****************************************************************************************
		String pieChartCaption = "Ԥ��ʹ��ժҪ";
		String[] texts = new String[] { "Ԥ�������", "��Ԥ�����", "��֧����","��������"};
		double[] values = new double[] {value1,value2,value3,value4 };
		statusPieChart.setChart(ProjectChartFoctory.createPieChart(
				pieChartCaption, texts, values));

		// *****************************************************************************************
		String[] deptParameter = new String[data.summaryData.subOrganizationProjectProvider
				.size()];
		if (deptParameter.length != 0) {
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
				deptValue1[i] = projectProvider.summaryData.total_budget_amount/10000;
				deptValue2[i] = projectProvider.summaryData.total_investment_amount/10000;
			}

			if (deptBarTabItem == null) {
				deptBarTabItem = new TabItem(tabFolder, SWT.NONE);
				deptBarTabItem.setText("��Ŀ�е�����");
				deptProjectBar = new ChartCanvas(tabFolder, SWT.NONE);
				deptBarTabItem.setControl(deptProjectBar);
			}

			deptProjectBar.setChart(ProjectChartFoctory.createStackedBarChart(
					"������ĿԤ��ִ��״��", deptParameter, deptValue1, deptValue2,new String[]{"Ԥ��","ʵ��"}));
		}
		// *****************************************************************************************
		String[] chargerName = new String[data.summaryData.subChargerProjectProvider
				.size()];
		if (chargerName.length != 0) {
			double[] userValue1 = new double[data.summaryData.subChargerProjectProvider
					.size()];
			double[] userValue2 = new double[data.summaryData.subChargerProjectProvider
					.size()];
			for (int i = 0; i < chargerName.length; i++) {
				ProjectProvider projectProvider = data.summaryData.subChargerProjectProvider
						.get(i);
				projectProvider.setParameters(data.parameters);
				projectProvider.getData();
				chargerName[i] = projectProvider.getDesc();
				userValue1[i] = projectProvider.summaryData.total_budget_amount/10000;
				userValue2[i] = projectProvider.summaryData.total_investment_amount/10000;
			}

			if(pmBarTabItem==null){
				pmBarTabItem = new TabItem(tabFolder, SWT.NONE);
				pmBarTabItem.setText("��Ŀ����");
				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE);
				pmBarTabItem.setControl(pmProjectBar);
			}

			pmProjectBar.setChart(ProjectChartFoctory.createStackedBarChart(
					"��Ŀ����Ԥ��ִ��״��", chargerName, userValue1, userValue2,new String[]{"Ԥ��","ʵ��"}));
		}
	}

	private void redrawChart() {
		deptProjectBar.redrawChart();
		finishedProjectMeter.redrawChart();
		processProjectMeter.redrawChart();
		statusPieChart.redrawChart();
	}

}
