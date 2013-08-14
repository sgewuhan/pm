package com.sg.business.management.editor.page;

import org.drools.definition.process.Process;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.utils.ProcessSelectorDialog;
import com.sg.widgets.MessageUtil;

public abstract class ProcessSettingPanel extends Composite {

	private static final int MARGIN = 4;
	private String key;
	private PrimaryObject primaryObject;
	private TableViewer processViewer;
	private DroolsProcessDefinition processDefinition;

	ProcessSettingPanel(Composite parent, String fieldName, PrimaryObject po) {
		super(parent, SWT.NONE);
		this.key = fieldName;
		this.primaryObject = po;
		DBObject processData = (DBObject) primaryObject.getValue(key);
		if(processData!=null){
			processDefinition = new DroolsProcessDefinition(processData);
		}
		createContent();
	}

	private void createContent() {
		this.setLayout(new FormLayout());
		// 是否启用
		final Button activeButton = new Button(this, SWT.CHECK);
		activeButton.setText("启用");
		FormData fd = new FormData();
		activeButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		// 流程库
		final Text text = new Text(this, SWT.BORDER);
		text.setMessage("<请输入流程库的名称，查询可用流程>");
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		Button queryButton = new Button(this, SWT.PUSH);
		queryButton.setText("查询");
		fd = new FormData();
		queryButton.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(text, MARGIN);

		final Label processLabel = new Label(this, SWT.NONE);
		processLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		processLabel.setText("<strong>未定义流程</strong>");
		fd = new FormData();
		processLabel.setLayoutData(fd);
		fd.left = new FormAttachment(activeButton, MARGIN);
		fd.bottom = new FormAttachment(queryButton, -MARGIN);

		// 创建树
		Table control = createProcessViewer();
		fd = new FormData();
		control.setLayoutData(fd);
		fd.top = new FormAttachment(text, MARGIN);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		refresh(processLabel, key);

		queryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				queryProcess(processLabel, text.getText(), key);
			}
		});

		activeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				activate(key, new Boolean(activeButton.getSelection()));
			}
		});
	}

	private Table createProcessViewer() {
		processViewer = new TableViewer(this, SWT.FULL_SELECTION);
		processViewer.getTable().setHeaderVisible(true);
		processViewer.setContentProvider(ArrayContentProvider.getInstance());
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText("活动名称");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				return null;

			}

		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("执行角色");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				return "";
			}

		});
		return processViewer.getTable();
	}

	private void refresh(Label processLabel, String key) {
		processLabel.setText("<strong>"+processDefinition.getProcessName()+"</strong>");
		layout(true);

		processViewer.setInput(processDefinition.getHumanNodes());
	}

	private void queryProcess(Label processLabel, String kbase, String key) {
		if (kbase.isEmpty()) {
			MessageUtil.showToast("请输入流程库名称", SWT.ERROR);
			return;
		}
		ProcessSelectorDialog d = new ProcessSelectorDialog(getShell(), kbase);
		Process process = null;
		int ok = d.open();
		if (ok == Window.OK) {
			process = d.getSelection();
			if (process == null) {
				MessageUtil.showToast("无法获得流程库包含的流程", SWT.ERROR);
				return;
			} else {
				processDefinition = new DroolsProcessDefinition(kbase, process);
				primaryObject.setValue(key, processDefinition.getData());
				refresh(processLabel, key);
				setDirty(true);
			}
		}
	}

	protected abstract void setDirty(boolean b);

	protected void activate(String key, Boolean b) {
		primaryObject.setValue(key + "_activated", b);
		setDirty(true);
	}

}
