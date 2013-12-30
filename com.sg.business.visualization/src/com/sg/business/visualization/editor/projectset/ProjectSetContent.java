package com.sg.business.visualization.editor.projectset;

import java.util.ArrayList;
import java.util.List;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.visualization.nls.Messages;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ProjectSetContent extends AbstractProjectSetTableDetail {

	//
	// private Label projectStatusSummary;
	// private Label schedualSummary;
	// private Label costSummary;
	//
	// public ProjectSetContent() {
	//
	// }
	//
	// @Override
	// protected Composite createContent(Composite body) {
	// Composite content = new Composite(body, SWT.NONE);
	// navi.createPartContent(content);
	//
	// //设置加载事件
	// navi.handleReloadHandle(new Runnable() {
	//
	// @Override
	// public void run() {
	// setSummaryText(header, data);
	// }
	//
	// });
	// return content;
	// }
	//
	// @Override
	// protected Composite createHeader(Composite body) {
	// Composite header = super.createHeader(body);
	//
	// // 显示右侧的第一摘要字段，数量
	// projectStatusSummary = new Label(header, SWT.NONE);
	// FormData fd = new FormData();
	// projectStatusSummary.setLayoutData(fd);
	// fd.right = new FormAttachment(100, -4);
	// fd.top = new FormAttachment(0, 4);
	//
	// // 右侧第二摘要字段，进度
	// schedualSummary = new Label(header, SWT.NONE);
	// fd = new FormData();
	// schedualSummary.setLayoutData(fd);
	// fd.right = new FormAttachment(100, -4);
	// fd.top = new FormAttachment(projectStatusSummary, 2);
	//
	// // 右侧第三摘要字段，成本
	// costSummary = new Label(header, SWT.NONE);
	// fd = new FormData();
	// costSummary.setLayoutData(fd);
	// fd.right = new FormAttachment(100, -4);
	// fd.top = new FormAttachment(schedualSummary, 2);
	// setSummaryText(header, data);
	//
	// return header;
	// }
	//
	// protected boolean displaySummary() {
	// return false;
	// }
	//
	//
	// @Override
	// protected String getProjectSetPageLabel() {
	// String projectSetName = data.getProjectSetName();
	// StringBuffer sb = new StringBuffer();
	//		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:13pt'>"); //$NON-NLS-1$
	// sb.append(projectSetName + Messages.get().ProjectSetContent_1);
	//		sb.append("</span>"); //$NON-NLS-1$
	// return sb.toString();
	// }
	//
	// private void setSummaryText(Composite header, ProjectProvider data) {
	// int value1 = data.sum.processing;
	// int value2 = data.sum.finished;
	// int value3 = data.sum.total;
	//		projectStatusSummary.setText(Messages.get().ProjectSetContent_3 + value1 + "/" + value2 + "/" //$NON-NLS-2$ //$NON-NLS-3$
	// + value3);
	// value1 = data.sum.processing_normal;
	// value2 = data.sum.processing_delay;
	// value3 = data.sum.processing_advance;
	//		schedualSummary.setText(Messages.get().ProjectSetContent_6 + value1 + "/" + value2 + Messages.get().ProjectSetContent_8 //$NON-NLS-2$
	// + value3);
	// value1 = data.sum.finished_cost_normal;
	// value2 = data.sum.finished_cost_over;
	// costSummary.setText(Messages.get().ProjectSetContent_9 + value1 +
	// Messages.get().ProjectSetContent_10 + value2);
	//
	// header.layout();
	// }

	@Override
	protected String getTitle() {
		return Messages.get().ProjectSetContent_1;
	}

	@Override
	protected boolean displaySummary() {
		return true;
	}

	@Override
	protected List<ColumnSorter> getColumnSorters(ColumnConfigurator conf) {
		if (conf.getColumn().equals("planstart")) {
			return getSortOfPlanStartColumn();
		}else if(conf.getColumn().equals("budget")){
			return getSortOfBudgetColumn();
		}else if(conf.getColumn().equals("revenue")){
			return getSortOfRevenueColumn();
		}
		return null;
	}

	private List<ColumnSorter> getSortOfRevenueColumn() {
		ArrayList<ColumnSorter> result = new ArrayList<ColumnSorter>();
		result.add(new ProjectColumnSorter("预算"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getBudgetValue();
				double v2 = ps2.getBudgetValue();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		result.add(new ProjectColumnSorter("实际发生"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getDesignatedInvestment();
				double v2 = ps2.getDesignatedInvestment();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		result.add(new ColumnSorter());//分割符
		result.add(new ProjectColumnSorter("超支金额"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getDesignatedInvestment()-ps1.getBudgetValue();
				double v2 = ps2.getDesignatedInvestment()-ps2.getBudgetValue();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		

		return result;
	}

	private List<ColumnSorter> getSortOfBudgetColumn() {
		ArrayList<ColumnSorter> result = new ArrayList<ColumnSorter>();
		result.add(new ProjectColumnSorter("预算"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getBudgetValue();
				double v2 = ps2.getBudgetValue();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		result.add(new ProjectColumnSorter("实际发生"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getDesignatedInvestment();
				double v2 = ps2.getDesignatedInvestment();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		result.add(new ColumnSorter());//分割符
		result.add(new ProjectColumnSorter("超支金额"){
			@Override
			protected int doCompare(Project p1, Project p2) {
				ProjectPresentation ps1 = p1.getPresentation();
				ProjectPresentation ps2 = p2.getPresentation();
				double v1 = ps1.getDesignatedInvestment()-ps1.getBudgetValue();
				double v2 = ps2.getDesignatedInvestment()-ps2.getBudgetValue();
				return new Double(v1).compareTo(new Double(v2));
			}
		});
		

		return result;
	}

	private List<ColumnSorter> getSortOfPlanStartColumn() {
		ArrayList<ColumnSorter> result = new ArrayList<ColumnSorter>();
		result.add(new ProjectColumnSorter("计划开始", Project.F_PLAN_START));
		result.add(new ProjectColumnSorter("计划完成", Project.F_PLAN_FINISH));
		result.add(new ProjectColumnSorter("实际开始", Project.F_ACTUAL_START));
		result.add(new ProjectColumnSorter("实际完成", Project.F_ACTUAL_FINISH));
		result.add(new ColumnSorter());//分割符
		result.add(new ProjectColumnSorter("进度延迟天数") {
			@Override
			protected int doCompare(Project p1, Project p2) {
				String pl1 = p1.getLifecycleStatus();
				String pl2 = p2.getLifecycleStatus();
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(pl1)
						&& ILifecycle.STATUS_FINIHED_VALUE.equals(pl2)) {
					return 0;
				}else if(ILifecycle.STATUS_FINIHED_VALUE.equals(pl1)
						&& !ILifecycle.STATUS_FINIHED_VALUE.equals(pl2)){
					return -1;
				}else if(!ILifecycle.STATUS_FINIHED_VALUE.equals(pl1)
						&& ILifecycle.STATUS_FINIHED_VALUE.equals(pl2)){
					return 1;
				}else{
					long d1 = p1.getDelayDays();
					long d2 = p2.getDelayDays();
					
					return new Long(d1).compareTo(new Long(d2));
				}
			}
		});
		return result;
	}

}
