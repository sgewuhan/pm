package com.sg.business.visualization.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.sg.business.commons.ui.chart.ProjectChartFactory;
import com.sg.business.model.Organization;
import com.sg.business.model.OrganizationProjectProvider;
import com.sg.business.model.ProjectProvider;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.part.NavigatablePartAdapter;
import com.sg.widgets.part.NavigatorControl;

public class DashDepartment extends AbstractDashWidgetView {

	
	private static final int unit = 10000;

	private static final int Y1_PROCESS_SCHEDUAL = 0;

	private static final int Y1_PROJECT_COUNT = 1;

	private static final int Y1_PROCESS_BUDGET = 2;

	private static final int Y2_SALES_REVENUE = 3;

	private static final int Y2_SALES_PROFIT = 4;

	private static final int Y2_RND_INVESTMENT = 5;

	private static final int Y2_OVER_SCHEDUAL = 6;

	private static final int Y2_OVER_BUDGET = 7;

	private static final int Y2_BUDGET = 8;

	private static final int Y2_OVERCOST = 9;

	private NavigatorControl navi;

	private Tree tree;

	private double[] deptValue1;

	private String[] deptParameter;

	private double[] deptValue2;

	private double[] deptValue3;

	protected int y1Type = 0;

	protected int y2Type = 4;

	private ChartCanvas chart;

	private String title1;

	private String title2;

	private String title3;

	@Override
	protected void drawContent(Composite parent) {
		initData();

		parent.setLayout(new FillLayout());

		SashForm sh = new SashForm(parent, SWT.HORIZONTAL);

		chart = createChart(sh);

		createControl(sh);

		sh.setWeights(new int[] { 3, 1 });

		parent.layout();
	}

	private void initData() {
		ProjectProvider data = this.projectProvider;
		deptParameter = new String[data.sum.subOrganizationProjectProvider
				.size()];
		deptValue1 = new double[data.sum.subOrganizationProjectProvider.size()];
		deptValue2 = new double[data.sum.subOrganizationProjectProvider.size()];
		deptValue3 = new double[data.sum.subOrganizationProjectProvider.size()];
		for (int i = 0; i < deptParameter.length; i++) {
			ProjectProvider subProjectProvider = data.sum.subOrganizationProjectProvider
					.get(i);
			subProjectProvider.setParameters(data.parameters);
			subProjectProvider.getData();
			deptParameter[i] = subProjectProvider.getDesc();
			deptValue1[i] = subProjectProvider.sum.processing_normal
					+ subProjectProvider.sum.processing_advance;
			deptValue2[i] = subProjectProvider.sum.processing_delay;
			deptValue3[i] = (subProjectProvider.sum.total_sales_revenue 
					- subProjectProvider.sum.total_sales_cost-subProjectProvider.sum.total_investment_amount)/unit;
		}

		title1 = Messages.get().DashDepartment_0;

		title2 = Messages.get().DashDepartment_1;

		title3 = Messages.get().DashDepartment_2;
	}

	private void createControl(Composite parent) {
		ExpandBar expandBar = new ExpandBar(parent, SWT.NONE);

		Composite composite = new Composite(expandBar, SWT.NONE);
		composite.setLayout(new FillLayout());
		createDeptSelector(composite);
		ExpandItem item = new ExpandItem(expandBar, SWT.NONE, 0);
		item.setText(Messages.get().DashDepartment_3);
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
		item.setExpanded(true);

		composite = new Composite(expandBar, SWT.NONE);
		composite.setLayout(new FillLayout());
		createBarDataSelector(composite);
		item = new ExpandItem(expandBar, SWT.NONE, 1);
		item.setText(Messages.get().DashDepartment_4);
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);

		composite = new Composite(expandBar, SWT.NONE);
		composite.setLayout(new GridLayout());
		createLineDataSelector(composite);
		item = new ExpandItem(expandBar, SWT.NONE, 2);
		item.setText(Messages.get().DashDepartment_5);
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);

		expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void createLineDataSelector(Composite parent) {
		createY2Button(parent,Messages.get().DashDepartment_6,Y2_SALES_REVENUE);
		Button button = createY2Button(parent,Messages.get().DashDepartment_7,Y2_SALES_PROFIT);
		button.setSelection(true);

		createY2Button(parent,Messages.get().DashDepartment_8,Y2_RND_INVESTMENT);
		createY2Button(parent,Messages.get().DashDepartment_9,Y2_OVER_SCHEDUAL);
		createY2Button(parent,Messages.get().DashDepartment_10,Y2_OVER_BUDGET);
	}

	private Button createY2Button(Composite parent, String text,final int key) {
		Button button = new Button(parent, SWT.RADIO);
		button.setText(text);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.getSource()).getSelection()){
					y2Type = key;
					redrawChart();
				}
			}
		});
		return button;
	}
	
	private Button createY1Button(Composite parent, String text,final int key) {
		Button button = new Button(parent, SWT.RADIO);
		button.setText(text);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.getSource()).getSelection()){
					y1Type = key;
					redrawChart();
				}
			}
		});
		return button;
	}


	protected void redrawChart() {
		List<Organization> orgList = getCheckOrganization(tree.getItems());
		deptParameter = new String[orgList.size()];
		deptValue1 = new double[orgList.size()];
		deptValue2 = new double[orgList.size()];
		deptValue3 = new double[orgList.size()];

		for (int i = 0; i < orgList.size(); i++) {
			ProjectProvider sub = orgList.get(i).getAdapter(
					ProjectProvider.class);

			sub.setParameters(projectProvider.parameters);
			sub.getData();
			deptParameter[i] = sub.getDesc();

			switch (y1Type) {
			case Y1_PROCESS_BUDGET:
				title1 = Messages.get().DashDepartment_11;
				title2 = Messages.get().DashDepartment_12;
				deptValue1[i] = sub.sum.processing_cost_normal;
				deptValue2[i] = sub.sum.processing_cost_over;
				break;
			case Y1_PROCESS_SCHEDUAL:
				title1 = Messages.get().DashDepartment_13;
				title2 = Messages.get().DashDepartment_14;
				deptValue1[i] = sub.sum.processing_normal
						+ sub.sum.processing_advance;
				deptValue2[i] = sub.sum.processing_delay;

				break;
			case Y1_PROJECT_COUNT:
				title1 = Messages.get().DashDepartment_15;
				title2 = Messages.get().DashDepartment_16;
				deptValue1[i] = sub.sum.processing;
				deptValue2[i] = sub.sum.finished;

				break;

			default:
				break;
			}

			switch (y2Type) {
			case Y2_RND_INVESTMENT:
				title3 = Messages.get().DashDepartment_17;
				deptValue3[i] = sub.sum.total_investment_amount / unit;
				break;
			case Y2_BUDGET:
				title3 = Messages.get().DashDepartment_18;
				deptValue3[i] = sub.sum.total_budget_amount / unit;
				break;
			case Y2_OVERCOST:
				title3 = Messages.get().DashDepartment_19;
				deptValue3[i] = sub.sum.total_investment_amount / unit
						- sub.sum.total_budget_amount / unit;
				break;
			case Y2_SALES_PROFIT:
				title3 = Messages.get().DashDepartment_20;
				deptValue3[i] = (sub.sum.total_sales_revenue 
						- sub.sum.total_sales_cost-sub.sum.total_investment_amount)/unit;
				break;
			case Y2_SALES_REVENUE:
				title3 = Messages.get().DashDepartment_21;
				deptValue3[i] = sub.sum.total_sales_revenue / unit;
				break;

			default:
				break;
			}
		}

		Chart data = getChartData(projectProvider);

		chart.setChart(data);
		try {
			chart.redrawChart();
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	private List<Organization> getCheckOrganization(TreeItem[] items) {
		List<Organization> result = new ArrayList<Organization>();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked()) {
				result.add((Organization) items[i].getData());
			}

			if (items[i].getItemCount() > 0) {
				result.addAll(getCheckOrganization(items[i].getItems()));
			}
		}
		return result;
	}

	private void createBarDataSelector(Composite parent) {
		Button button = createY1Button(parent, Messages.get().DashDepartment_22, Y1_PROCESS_SCHEDUAL);
		button.setToolTipText(Messages.get().DashDepartment_23);
		button.setSelection(true);

		button = createY1Button(parent, Messages.get().DashDepartment_24, Y1_PROCESS_BUDGET);
		button.setToolTipText(Messages.get().DashDepartment_25);

		button = createY1Button(parent, Messages.get().DashDepartment_26, Y1_PROJECT_COUNT);
		button.setText(Messages.get().DashDepartment_27);
		button.setToolTipText(Messages.get().DashDepartment_28);
	}

	private void createDeptSelector(Composite parent) {
		navi = new NavigatorControl("vis.org.selector.managerrole", //$NON-NLS-1$
				new NavigatablePartAdapter() {
				});
		navi.createPartContent(parent);
		tree = (Tree) navi.getControl();
		// 选择第一级的节点
		TreeItem[] items = tree.getItems();
		if (items != null && items.length > 0) {
			items = items[0].getItems();
			for (int i = 0; i < items.length; i++) {
				((TreeItem) items[i]).setChecked(true);
			}
		}
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				redrawChart();
			}
		});
	}

	private ChartCanvas createChart(Composite parent) {
		return new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getChartData(projectProvider);
			}
		};
	}

	protected Chart getChartData(ProjectProvider data) {
		return ProjectChartFactory.createCombinnationStackedBarChart(
				data.getProjectSetName() + Messages.get().DashDepartment_30, deptParameter,
				deptValue1, deptValue2, deptValue3, new String[] { title1,
						title2, title3 });
	}

	@Override
	public void projectProviderChanged(ProjectProvider newProjectProvider,
			ProjectProvider oldProjectProvider) {
		super.projectProviderChanged(newProjectProvider, oldProjectProvider);
		if (newProjectProvider instanceof OrganizationProjectProvider) {
			navi.masterChanged(
					((OrganizationProjectProvider) newProjectProvider)
							.getOrganization(), null, null);
		}

	}

}
