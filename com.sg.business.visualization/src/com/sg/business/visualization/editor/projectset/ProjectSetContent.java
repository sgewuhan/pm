package com.sg.business.visualization.editor.projectset;

import java.util.ArrayList;
import java.util.List;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
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
	// //���ü����¼�
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
	// // ��ʾ�Ҳ�ĵ�һժҪ�ֶΣ�����
	// projectStatusSummary = new Label(header, SWT.NONE);
	// FormData fd = new FormData();
	// projectStatusSummary.setLayoutData(fd);
	// fd.right = new FormAttachment(100, -4);
	// fd.top = new FormAttachment(0, 4);
	//
	// // �Ҳ�ڶ�ժҪ�ֶΣ�����
	// schedualSummary = new Label(header, SWT.NONE);
	// fd = new FormData();
	// schedualSummary.setLayoutData(fd);
	// fd.right = new FormAttachment(100, -4);
	// fd.top = new FormAttachment(projectStatusSummary, 2);
	//
	// // �Ҳ����ժҪ�ֶΣ��ɱ�
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
	//		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>"); //$NON-NLS-1$
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
	protected List<ColumnSorters> getColumnSorters(ColumnConfigurator conf) {
		if (conf.getColumn().equals("planstart")) {
			ArrayList<ColumnSorters> result = new ArrayList<ColumnSorters>();
			result.add(new ProjectColumnSorter("�ƻ���ʼ", Project.F_PLAN_START));
			result.add(new ProjectColumnSorter("�ƻ����", Project.F_PLAN_FINISH));
			result.add(new ProjectColumnSorter("ʵ�ʿ�ʼ", Project.F_ACTUAL_START));
			result.add(new ProjectColumnSorter("ʵ�����", Project.F_ACTUAL_FINISH));
			result.add(new ProjectColumnSorter("����") {
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

		return null;
	}

}
