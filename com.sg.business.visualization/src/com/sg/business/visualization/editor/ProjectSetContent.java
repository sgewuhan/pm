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
		
		//���ü����¼�
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
		
		// ��ʾ�Ҳ�ĵ�һժҪ�ֶΣ�����
		projectStatusSummary = new Label(header, SWT.NONE);
		FormData fd = new FormData();
		projectStatusSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(0, 4);

		// �Ҳ�ڶ�ժҪ�ֶΣ�����
		schedualSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		schedualSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(projectStatusSummary, 2);

		// �Ҳ����ժҪ�ֶΣ��ɱ�
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
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		sb.append(projectSetName + " ժҪ");
		sb.append("</span>");
		return sb.toString();
	}
	
	private void setSummaryText(Composite header, ProjectProvider data) {
		Object value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_PROCESSING);
		value1 = value1 == null ? 0 : value1;
		Object value2 = data
				.getSummaryValue(ProjectProvider.F_SUMMARY_FINISHED);
		value2 = value2 == null ? 0 : value2;
		Object value3 = data
				.getSummaryValue(ProjectProvider.F_SUMMARY_TOTAL);
		value3 = value3 == null ? 0 : value3;
		projectStatusSummary.setText("����/���/������" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_PROCESSING_NORMAL);
		value1 = value1 == null ? 0 : value1;
		value2 = data.getSummaryValue(ProjectProvider.F_SUMMARY_PROCESSING_DELAY);
		value2 = value2 == null ? 0 : value2;
		value3 = data.getSummaryValue(ProjectProvider.F_SUMMARY_PROCESSING_ADVANCE);
		value3 = value3 == null ? 0 : value3;
		schedualSummary.setText("����/����/��ǰ��" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_NORMAL_COST);
		value1 = value1 == null ? 0 : value1;
		value2 = data.getSummaryValue(ProjectProvider.F_SUMMARY_OVER_COST);
		value2 = value2 == null ? 0 : value2;
		costSummary.setText("����/��֧��" + value1 + "/" + value2);

		filterLabel.setText(getParameterText());
		header.layout();
	}

	private String getParameterText() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		sb.append(getHeadParameterText());
		sb.append("</span>");
		return sb.toString();
	}
}
