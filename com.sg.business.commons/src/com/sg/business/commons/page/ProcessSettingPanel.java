package com.sg.business.commons.page;

import java.util.List;

import org.drools.definition.process.Process;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Text;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.utils.ProcessSelectorDialog;
import com.sg.business.model.IProcessControlable;
import com.sg.widgets.MessageUtil;

public class ProcessSettingPanel extends Composite {

	private static final int MARGIN = 4;
	private String key;
	private PrimaryObject primaryObject;
	private ProcessViewer processViewer;
	private DroolsProcessDefinition processDefinition;
	private BasicDBObject processAssignment;
	private List<PrimaryObject> roleds;
	private Label processLabel;
	private Button activeButton;
	private boolean editable;

	ProcessSettingPanel(Composite parent, String fieldName, PrimaryObject po,
			boolean editable) {
		super(parent, SWT.NONE);
		this.key = fieldName;
		this.primaryObject = po;
		this.editable = editable;
		DBObject processData = (DBObject) primaryObject.getValue(key);
		if (processData != null) {
			processDefinition = new DroolsProcessDefinition(processData);
		}

		processAssignment = (BasicDBObject) primaryObject.getValue(key
				+ IProcessControlable.POSTFIX_ASSIGNMENT);
		if (processAssignment == null) {
			processAssignment = new BasicDBObject();
		}

	}

	public void setRoleDefinitions(List<PrimaryObject> roleDefinitions) {
		this.roleds = roleDefinitions;
	}

	public void createContent() {
		this.setLayout(new FormLayout());
		// 是否启用
		activeButton = new Button(this, SWT.CHECK);
		activeButton.setEnabled(editable);
		activeButton.setText("启用");
		FormData fd = new FormData();
		activeButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		// 流程库
		final Text text = new Text(this, SWT.BORDER);
		text.setEnabled(editable);

		text.setMessage("<请输入流程库的名称，查询可用流程>");
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		Button queryButton = new Button(this, SWT.PUSH);
		queryButton.setEnabled(editable);

		queryButton.setText("查询");
		fd = new FormData();
		queryButton.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(text, MARGIN);

		processLabel = new Label(this, SWT.NONE);
		processLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		fd = new FormData();
		processLabel.setLayoutData(fd);
		// fd.left = new FormAttachment(activeButton, MARGIN);
		fd.top = new FormAttachment(0, MARGIN);
		fd.right = new FormAttachment(100);

		// 创建表
		processViewer = createProcessViewer(this, key, primaryObject, roleds,
				editable);
		fd = new FormData();
		processViewer.getTable().setLayoutData(fd);
		fd.top = new FormAttachment(text, MARGIN);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		refresh();

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

	protected ProcessViewer createProcessViewer(Composite parent, String key,
			PrimaryObject primaryObject, List<PrimaryObject> roleds,
			boolean editable) {
		processViewer = new ProcessViewer(parent, key, primaryObject, roleds,
				editable) {

			@Override
			protected void processAssignmentUpdated() {
				setDirty(true);
			}

		};

		return processViewer;
	}

	private void refresh() {
		Object activated = primaryObject.getValue(key
				+ IProcessControlable.POSTFIX_ACTIVATED);
		activeButton.setSelection(Boolean.TRUE.equals(activated));

		if (processDefinition != null) {
			processLabel.setText("<big >" + processDefinition.getProcessName()
					+ "</big>");
			processViewer.setInput(processDefinition.getNodesAssignment());
			packTableViewer(processViewer);
		} else {
			processLabel.setText("<big>" + "未定义流程" + "</big>");
			processViewer.setInput(null);
		}
		layout(true);

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
				if (processDefinition != null
						&& processDefinition.getKbase().equals(kbase)
						&& processDefinition.getProcessId().equals(
								process.getId())) {
					return;
				}
				processDefinition = new DroolsProcessDefinition(kbase, process);
				processAssignment.clear();
				primaryObject.setValue(key, processDefinition.getData());
				refresh();
				setDirty(true);
			}
		}
	}

	protected void setDirty(boolean b) {
	}

	private void activate(String key, Boolean b) {
		primaryObject.setValue(key + IProcessControlable.POSTFIX_ACTIVATED, b);
		setDirty(true);
	}

	private void packTableViewer(TableViewer viewer) {

		int count = viewer.getTable().getColumnCount();
		for (int i = 0; i < count; i++) {
			viewer.getTable().getColumn(i).pack();
		}
	}

}
