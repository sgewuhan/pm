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

public class ProjectSetDashboardSchedual extends AbstractProjectPage {

	private ChartCanvas statusPieChart;
	private ChartCanvas finishedProjectMeter;
	private ChartCanvas processProjectMeter;
	private ChartCanvas allProjectMeter;
	private ChartCanvas deptProjectBar;
	private ChartCanvas pmProjectBar;
	private TabItem deptBarTabItem;
	private TabItem pmBarTabItem;
	private TabFolder tabFolder;

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
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		sb.append(projectSetName + " ��Ŀ����״��");
		sb.append("</span>");
		return sb.toString();
	}

	private void createGraphic(Composite parent) {

		tabFolder = new TabFolder(parent, SWT.TOP);
		TabItem pieTabItem = new TabItem(tabFolder, SWT.NONE);
		pieTabItem.setText("����ժҪ");
		statusPieChart = new ChartCanvas(tabFolder, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getStatusPieChartData();
			}
		};
		pieTabItem.setControl(statusPieChart);

		TabItem meterTabItem = new TabItem(tabFolder, SWT.NONE);
		meterTabItem.setText("�Ǳ���");
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

		allProjectMeter = new ChartCanvas(composite, SWT.NONE) {

			@Override
			public Chart getChart() {
				return getAllProjectMeterChartData();
			}

		};
		allProjectMeter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
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
		// long start = System.currentTimeMillis();
		//
		// // "�������"
		// int value1 = data.summaryData.finished_normal;
		// // "�������",
		// int value2 = data.summaryData.finished_delay;
		// // "��ǰ���",
		// int value3 = data.summaryData.finished_advance;
		// // "�����ӳ�",
		// int value4 = data.summaryData.processing_delay;
		// // "��������"
		// int value5 = data.summaryData.processing_normal;
		// // ������ǰ
		// int value6 = data.summaryData.processing_advance;
		// int sum = value1 + value2 + value3 + value4 + value5 + value6;
		// double allProjectOverTimeRate = sum == 0 ? 0
		// : (100d * (value2 + value4) / sum);
		//
		// sum = value4 + value5 + value6;
		// double processProjectOverTimeRate = sum == 0 ? 0
		// : (100d * value4 / sum);
		//
		// sum = value1 + value2 + value3;
		// double finishProjectOverTimeRate = sum == 0 ? 0 : (100d * value2 /
		// sum);
		//
		// finishedProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
		// "�������Ŀ���� ", "�����ӳ�", finishProjectOverTimeRate));
		// processProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
		// "��������Ŀ���� ", "�����ӳ�", processProjectOverTimeRate));
		// allProjectMeter.setChart(ProjectChartFoctory.createMeterChart(
		// "������Ŀ���� ", "�����ӳ�", allProjectOverTimeRate));
		//
		// long end = System.currentTimeMillis();
		// System.out.print("Load Dash:");
		// System.out.println(end - start);
		// start = System.currentTimeMillis();
		//
		// //
		// *****************************************************************************************
		// String pieChartCaption = "����ժҪ";
		// String[] texts = new String[] { "�������", "�������", "�����ӳ�", "��������",
		// "������ǰ" };
		// double[] values = new double[] { (value1 + value3), value2, value4,
		// value5, value6 };
		// statusPieChart.setChart(ProjectChartFoctory.createPieChart(
		// pieChartCaption, texts, values));
		//
		// end = System.currentTimeMillis();
		// System.out.print("Load Pie:");
		// System.out.println(end - start);
		// start = System.currentTimeMillis();

		// *****************************************************************************************
		String[] deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		if (deptParameter.length != 0) {
			if (deptBarTabItem == null) {
				deptBarTabItem = new TabItem(tabFolder, SWT.NONE);
				deptBarTabItem.setText("��Ŀ�е�����");
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
				pmBarTabItem.setText("��Ŀ����");
				pmProjectBar = new ChartCanvas(tabFolder, SWT.NONE) {
					@Override
					public Chart getChart() {
						return getProjectChargeBarChartData();
					}
				};

				pmBarTabItem.setControl(pmProjectBar);
			}
		}

	}

	protected Chart getProjectChargeBarChartData() {
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

		return ProjectChartFoctory.createStackedBarChart("��Ŀ������Ŀִ��״��",
				chargerName, userValue1, userValue2,
				new String[] { "����", "����" });
	}

	private Chart getDeptBarChartData() {
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

		return ProjectChartFoctory.createStackedBarChart("������Ŀִ��״��",
				deptParameter, deptValue1, deptValue2, new String[] { "����",
						"����" });
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

	private Chart getAllProjectMeterChartData() {
		// "�������"
		int value1 = data.sum.finished_normal;
		// "�������",
		int value2 = data.sum.finished_delay;
		// "��ǰ���",
		int value3 = data.sum.finished_advance;
		// "�����ӳ�",
		int value4 = data.sum.processing_delay;
		// "��������"
		int value5 = data.sum.processing_normal;
		// ������ǰ
		int value6 = data.sum.processing_advance;
		int sum = value1 + value2 + value3 + value4 + value5 + value6;
		double allProjectOverTimeRate = sum == 0 ? 0
				: (100d * (value2 + value4) / sum);
		return ProjectChartFoctory.createMeterChart("������Ŀ���� ", "�����ӳ�",
				allProjectOverTimeRate);
	}

	private Chart getProcessProjectMeterChartData() {
		// "�����ӳ�",
		int value4 = data.sum.processing_delay;
		// "��������"
		int value5 = data.sum.processing_normal;
		// ������ǰ
		int value6 = data.sum.processing_advance;

		int sum = value4 + value5 + value6;
		double processProjectOverTimeRate = sum == 0 ? 0
				: (100d * value4 / sum);

		return ProjectChartFoctory.createMeterChart("��������Ŀ���� ", "�����ӳ�",
				processProjectOverTimeRate);

	}

	private Chart getFinishedProjectMeterChartData() {
		// "�������"
		int value1 = data.sum.finished_normal;
		// "�������",
		int value2 = data.sum.finished_delay;
		// "��ǰ���",
		int value3 = data.sum.finished_advance;
		int sum = value1 + value2 + value3;
		double finishProjectOverTimeRate = sum == 0 ? 0 : (100d * value2 / sum);

		return ProjectChartFoctory.createMeterChart("�������Ŀ���� ", "�����ӳ�",
				finishProjectOverTimeRate);
	}

	private Chart getStatusPieChartData() {
		// "�������"
		int value1 = data.sum.finished_normal;
		// "�������",
		int value2 = data.sum.finished_delay;
		// "��ǰ���",
		int value3 = data.sum.finished_advance;
		// "�����ӳ�",
		int value4 = data.sum.processing_delay;
		// "��������"
		int value5 = data.sum.processing_normal;
		// ������ǰ
		int value6 = data.sum.processing_advance;

		String pieChartCaption = "����ժҪ";
		String[] texts = new String[] { "�������", "�������", "�����ӳ�", "��������", "������ǰ" };
		double[] values = new double[] { (value1 + value3), value2, value4,
				value5, value6 };
		return ProjectChartFoctory.createPieChart(pieChartCaption, texts,
				values);
	}

}
