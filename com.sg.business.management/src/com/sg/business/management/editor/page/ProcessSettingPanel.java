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

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.bpm.workflow.utils.ProcessSelectorDialog;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;

@Deprecated
public class ProcessSettingPanel extends Composite {

	private static final String F_POSTFIX_ASSIGNMENT = "_assignment"; //$NON-NLS-1$
	private static final int MARGIN = 4;
	private static final String F_POSTFIX_ACTIVATED = "_activated"; //$NON-NLS-1$
	private String key;
	private PrimaryObject primaryObject;
	private TableViewer processViewer;
	private DroolsProcessDefinition processDefinition;
	private BasicDBObject processAssignment;
	private List<PrimaryObject> roleds;
	private Label processLabel;
	private Button activeButton;

	@Deprecated
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
	
	private boolean isGenericWorkDefinition(){
		if( primaryObject instanceof WorkDefinition){
			return ((WorkDefinition)primaryObject).getWorkDefinitionType()==WorkDefinition.WORK_TYPE_GENERIC;
		}
		return false;
	}
	
	private boolean isStandloneWorkDefinition(){
		if( primaryObject instanceof WorkDefinition){
			return ((WorkDefinition)primaryObject).getWorkDefinitionType()==WorkDefinition.WORK_TYPE_STANDLONE;
		}
		return false;
	}

	public void setRoleDefinitions(List<PrimaryObject> roleDefinitions) {
		this.roleds = roleDefinitions;
	}

	public void createContent() {
		this.setLayout(new FormLayout());
		// 是否启用
		activeButton = new Button(this, SWT.CHECK);
		activeButton.setText(Messages.get().ProcessSettingPanel_2);
		FormData fd = new FormData();
		activeButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		// 流程库
		final Text text = new Text(this, SWT.BORDER);
		text.setMessage(Messages.get().ProcessSettingPanel_3);
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(0, MARGIN);

		Button queryButton = new Button(this, SWT.PUSH);
		queryButton.setText(Messages.get().ProcessSettingPanel_4);
		fd = new FormData();
		queryButton.setLayoutData(fd);
		fd.top = new FormAttachment(activeButton, MARGIN);
		fd.left = new FormAttachment(text, MARGIN);

		processLabel = new Label(this, SWT.NONE);
		HtmlUtil.enableMarkup(processLabel);

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
		processViewer.getTable().setLinesVisible(true);
		processViewer.setContentProvider(ArrayContentProvider.getInstance());
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessSettingPanel_5);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((NodeAssignment) element).getNodeName();
			}

		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessSettingPanel_6);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return Messages.get().ProcessSettingPanel_7;
				} else if (na.isRuleAssignment()) {
					return Messages.get().ProcessSettingPanel_8;
				} else if (na.isStaticActor()) {
					return Messages.get().ProcessSettingPanel_9;
				} else if (na.isStaticGroup()) {
					return Messages.get().ProcessSettingPanel_10;
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessSettingPanel_0);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String name = na.getRuleAssignmentName();
				if (name != null) {
					return name;
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessSettingPanel_14);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.getNodeActorParameter();
			}

		});

		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessSettingPanel_15);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String ap = na.getNodeActorParameter();
				ObjectId roleId = (ObjectId) processAssignment.get(ap);
				if(roleId == null){
					return ""; //$NON-NLS-1$
				}
				//如果是通用工作定义或者是独立工作定义，取出的是角色
				if(isGenericWorkDefinition()||isStandloneWorkDefinition()){
					Role roled = ModelService.createModelObject(
							Role.class, roleId);
					return roled == null ? "" : roled.getLabel(); //$NON-NLS-1$
				}else{//取出的是角色定义
					RoleDefinition roled = ModelService.createModelObject(
							RoleDefinition.class, roleId);
					return roled == null ? "" : roled.getLabel(); //$NON-NLS-1$
				}
				
			}

		});

		String[] rolednames = new String[roleds.size() + 1];
		rolednames[0] = ""; //$NON-NLS-1$
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

	private void updateInputData() {
		primaryObject.setValue(key + F_POSTFIX_ASSIGNMENT, processAssignment);
		setDirty(true);
	}

	private void refresh() {
		Object activated = primaryObject.getValue(key + F_POSTFIX_ACTIVATED);
		activeButton.setSelection(Boolean.TRUE.equals(activated));

		if (processDefinition != null) {
			processLabel.setText("<big >" + processDefinition.getProcessName() //$NON-NLS-1$
					+ "</big>"); //$NON-NLS-1$
			processViewer.setInput(processDefinition.getNodesAssignment());
			packTableViewer(processViewer);
		} else {
			processLabel.setText("<big>" + Messages.get().ProcessSettingPanel_23 + "</big>"); //$NON-NLS-1$ //$NON-NLS-3$
			processViewer.setInput(null);
		}
		layout(true);

	}

	private void queryProcess(Label processLabel, String kbase, String key) {
		if (kbase.isEmpty()) {
			MessageUtil.showToast(Messages.get().ProcessSettingPanel_25, SWT.ERROR);
			return;
		}
		ProcessSelectorDialog d = new ProcessSelectorDialog(getShell(), kbase);
		Process process = null;
		int ok = d.open();
		if (ok == Window.OK) {
			process = d.getSelection();
			if (process == null) {
				MessageUtil.showToast(Messages.get().ProcessSettingPanel_26, SWT.ERROR);
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
		primaryObject.setValue(key + F_POSTFIX_ACTIVATED, b);
		setDirty(true);
	}

	private void packTableViewer(TableViewer viewer) {

		int count = viewer.getTable().getColumnCount();
		for (int i = 0; i < count; i++) {
			viewer.getTable().getColumn(i).pack();
		}
	}

}
