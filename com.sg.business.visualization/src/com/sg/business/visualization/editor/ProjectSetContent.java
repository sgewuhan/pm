package com.sg.business.visualization.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sg.business.model.ProjectProvider;

public class ProjectSetContent extends AbstractProjectPage {

	
	private Label projectStatusSummary;
	private Label schedualSummary;
	private Label costSummary;
	
	public ProjectSetContent() {

	}

	@Override
	protected Composite createContent(Composite body) {
		Composite content = new Composite(body, SWT.NONE);
		navi.createPartContent(content);
		
		//设置加载事件
		navi.handleReloadHandle(new Runnable() {

			@Override
			public void run() {
				setSummaryText(header, data);
			}

		});
		return content;
	}
	
	@Override
	protected Composite createHeader(Composite body) {
		Composite header = super.createHeader(body);
		
		// 显示右侧的第一摘要字段，数量
		projectStatusSummary = new Label(header, SWT.NONE);
		FormData fd = new FormData();
		projectStatusSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(0, 4);

		// 右侧第二摘要字段，进度
		schedualSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		schedualSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(projectStatusSummary, 2);

		// 右侧第三摘要字段，成本
		costSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		costSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(schedualSummary, 2);
		setSummaryText(header, data);
		
		return header;
	}

	protected boolean displaySummary() {
		return false;
	}


	@Override
	protected String getProjectSetPageLabel() {
		String projectSetName = data.getProjectSetName();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:13pt'>");
		sb.append(projectSetName + " 项目综合运营状态");
		sb.append("</span>");
		return sb.toString();
	}
	
	private void setSummaryText(Composite header, ProjectProvider data) {
		int value1 = data.sum.processing;
		int value2 = data.sum.finished;
		int value3 = data.sum.total;
		projectStatusSummary.setText("进行/完成/总数：" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.sum.processing_normal;
		value2 = data.sum.processing_delay;
		value3 = data.sum.processing_advance;
		schedualSummary.setText("正常/超期/提前：" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.sum.finished_cost_normal;
		value2 = data.sum.finished_cost_over;
		costSummary.setText("正常/超支：" + value1 + "/" + value2);

		header.layout();
	}

}
