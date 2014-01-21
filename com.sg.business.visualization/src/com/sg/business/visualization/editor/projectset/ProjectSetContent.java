package com.sg.business.visualization.editor.projectset;

import java.util.List;

import com.sg.business.resource.nls.Messages;
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

}
