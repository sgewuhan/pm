package com.sg.business.management.editor.page;

import java.util.List;

import org.bson.types.ObjectId;
import org.drools.definition.process.Process;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
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

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.bpm.workflow.utils.ProcessSelectorDialog;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.MessageUtil;

public abstract class ProcessSettingPanel extends Composite {

	private static final String F_POSTFIX_ASSIGNMENT = "_assignment";
	private static final int MARGIN = 4;
	private static final String F_POSTFIX_ACTIVATED = "_activated";
	private String key;
	private PrimaryObject primaryObject;
	private TableViewer processViewer;
	private DroolsProcessDefinition processDefinition;
	private BasicDBObject processAssignment;
	private List<PrimaryObject> roleds;
	private Label processLabel;
	private Button activeButton;

	ProcessSettingPanel(Composite parent, String fieldName, PrimaryObject po) {
		super(parent, SWT.NONE);
		this.key = fieldName;
		this.primaryObject = po;
		DBObject processData = (DBObject) primaryObject.getValue(key);
		if (processData != null) {
			processDefinition = new DroolsProcessDefinition(processData);
		}

		processAssignment = (BasicDBObject) primaryObject.getValue(key
				+ F_POSTFIX_ASSIGNMENT);
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

		processLabel = new Label(this, SWT.NONE);
		processLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		fd = new FormData();
		processLabel.setLayoutData(fd);
		// fd.left = new FormAttachment(activeButton, MARGIN);
		fd.top = new FormAttachment(0, MARGIN);
		fd.right = new FormAttachment(100);

		// 创建树
		Table control = createProcessViewer();
		fd = new FormData();
		control.setLayoutData(fd);
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
				return ((NodeAssignment) element).getNodeName();
			}

		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("指派类别");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return "动态指派";
				} else if (na.isRuleAssignment()) {
					return "规则指派";
				} else if (na.isStaticActor()) {
					return "静态执行人";
				} else if (na.isStaticGroup()) {
					return "静态执行组";
				} else {
					return "";
				}
			}
		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("规则名称");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String name = na.getRuleAssignmentName();
				if (name != null) {
					return name;
				} else {
					return "";
				}
			}
		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("指派参数");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.getNodeActorParameter();
			}

		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText("执行角色");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String ap = na.getNodeActorParameter();
				ObjectId roleId = (ObjectId) processAssignment.get(ap);
				RoleDefinition roled = ModelService.createModelObject(
						RoleDefinition.class, roleId);
				return roleId == null ? "" : roled.getLabel();
			}

		});

		String[] rolednames = new String[roleds.size() + 1];
		rolednames[0] = "";
		for (int i = 1; i < rolednames.length; i++) {
			rolednames[i] = roleds.get(i - 1).getLabel();
		}
		final ComboBoxCellEditor combo = new ComboBoxCellEditor(
				processViewer.getTable(), rolednames, SWT.READ_ONLY);

		EditingSupport editingSupport = new EditingSupport(processViewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return combo;
			}

			@Override
			protected boolean canEdit(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.isNeedAssignment();
			}

			@Override
			protected Object getValue(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				ObjectId roled_id = (ObjectId) processAssignment.get(na
						.getNodeActorParameter());
				for (int i = 0; i < roleds.size(); i++) {
					PrimaryObject roled = roleds.get(i);
					if (roled.get_id().equals(roled_id)) {
						return i + 1;
					}
				}
				return 0;
			}

			@Override
			protected void setValue(Object element, Object value) {
				int index = ((Integer) value).intValue();
				NodeAssignment na = (NodeAssignment) element;
				String nodeActorParameter = na.getNodeActorParameter();
				if (index == 0) {
					processAssignment.removeField(nodeActorParameter);
				} else {
					PrimaryObject roled = roleds.get(index - 1);
					processAssignment.put(nodeActorParameter, roled.get_id());
				}
				updateInputData();
				processViewer.refresh();
			}

		};
		column.setEditingSupport(editingSupport);
		return processViewer.getTable();
	}

	protected void updateInputData() {
		primaryObject.setValue(key + F_POSTFIX_ASSIGNMENT, processAssignment);
		setDirty(true);
	}

	private void refresh() {
		Object activated = primaryObject.getValue(key + F_POSTFIX_ACTIVATED);
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

	protected abstract void setDirty(boolean b);

	protected void activate(String key, Boolean b) {
		primaryObject.setValue(key + F_POSTFIX_ACTIVATED, b);
		setDirty(true);
	}

	public void packTableViewer(TableViewer viewer) {

		int count = viewer.getTable().getColumnCount();
		for (int i = 0; i < count; i++) {
			viewer.getTable().getColumn(i).pack();
		}
	}

}
