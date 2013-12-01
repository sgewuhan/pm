package com.sg.business.visualization.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.model.ProjectProvider;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.birtcharts.ChartCanvas;

public class Dash extends Composite {

	private ProjectProvider projectProvider;

	public Dash(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(4, true));
	}

	public void setProjectProvider(ProjectProvider projectProvider) {
		if (projectProvider == null) {
			MessageUtil.showToast("�ܱ�Ǹ, ��û�л����֯��Ȩ���ʱ�ҳ��ҵ������", SWT.ICON_WARNING);
		}
		if (projectProvider.equals(this.projectProvider)) {
			return;
		}
		this.projectProvider = projectProvider;
		loadData(projectProvider);
	}

	private void loadData(ProjectProvider projectProvider) {
		this.projectProvider = projectProvider;
		Job job = new Job("����ҵ������") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Dash.this.projectProvider.getData();
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {

				Dash.this.getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						redrawContent();
					}
				});
				super.done(event);
			}
		});
		job.schedule();
	}

	protected void redrawContent() {
		Control[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		// 1.1. ��Ŀ����״̬
		ChartCanvas schedualPieChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getSchedualPieChartData();
			}
		};
		
		// 3.1. �������Ǳ�
		ChartCanvas schedualMeterChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getSchedualMeterChartData();
			}
		};
		
		// 4. ��ʾ�ۺ�����ͼ
		ChartCanvas combinatedBarChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getCombinatedBarChartData();
			}
		};

		// 1.2. Ԥ�����ժҪ
		ChartCanvas budgetAndRNDCostPieChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getBudgetAndRNDCostPieChartData();
			}
		};
		
		// 3.2. �з�Ͷ�����Ǳ�
		ChartCanvas budgetAndRNDCostMeterChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getBudgetAndRNDCostMeterChartData();
			}
		};

		// 1.3. ���������ժҪ
		ChartCanvas revenuePieChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getRevenuePieChartData();
			}
		};


		// 3.3. �������Ǳ�
		ChartCanvas revenueMeterChart = new ChartCanvas(this, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getRevenueMeterChartData();
			}
		};



		layout(schedualPieChart, 1, 1);
		layout(schedualMeterChart, 1, 1);
		layout(combinatedBarChart, 2, 3);
		layout(budgetAndRNDCostPieChart, 1, 1);
		layout(budgetAndRNDCostMeterChart, 1, 1);
		layout(revenuePieChart, 1, 1);
		layout(revenueMeterChart, 1, 1);
		layout();
	}

	protected Chart getCombinatedBarChartData() {
		// TODO Auto-generated method stub
		return ProjectChartFactory.getDeptSchedualBar(projectProvider);
	}

	protected Chart getRevenueMeterChartData() {
		// TODO Auto-generated method stub
		return ProjectChartFactory
				.getProcessProjectBudgetAndCostMeter(projectProvider);
	}

	protected Chart getBudgetAndRNDCostMeterChartData() {
		return ProjectChartFactory
				.getFinishedProjectBudgetAndCostMeter(projectProvider);
	}

	protected Chart getSchedualMeterChartData() {
		return ProjectChartFactory.getProjectSchedualMeter(projectProvider);
	}

	protected Chart getRevenuePieChartData() {
		// TODO
		return ProjectChartFactory.getProjectBudgetAndCostPie(projectProvider);
	}

	protected Chart getBudgetAndRNDCostPieChartData() {
		return ProjectChartFactory.getProjectBudgetAndCostPie(projectProvider);
	}

	protected Chart getSchedualPieChartData() {
		return ProjectChartFactory.getSchedualStatusPieChart(projectProvider);
	}

	private void layout(Composite chart, int horizontalSpan, int verticalSpan) {
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				horizontalSpan, verticalSpan));
	}

}
