package com.sg.business.project.editor.page;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.ProjectRole;

public abstract class ProcessViewer extends TableViewer {

	private List<PrimaryObject> roleDefinitions;
	private DBObject processAssignment;
	private PrimaryObject data;
	private String key;

	public ProcessViewer(Composite parent, String key, PrimaryObject data,
			List<PrimaryObject> roleDefinitions) {
		super(parent, SWT.FULL_SELECTION);
		this.data = data;
		this.key = key;
		processAssignment = (DBObject) data.getValue(key
				+ IProcessControlable.POSTFIX_ASSIGNMENT);
		this.roleDefinitions = roleDefinitions;
		createColumns();
	}

	public ProcessViewer(Composite parent, String key, PrimaryObject data) {
		super(parent, SWT.FULL_SELECTION);
		this.data = data;
		this.key = key;
		processAssignment = (DBObject) data.getValue(key
				+ IProcessControlable.POSTFIX_ASSIGNMENT);
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(ArrayContentProvider.getInstance());
		createColumns();
	}

	protected void createColumns() {

		createActionNameColumn();

		createAssignmentTypeColumn();

		createRuleColumn();

		createParameterColumn();

		createActorRoleColumn();
	}

	protected TableViewerColumn createActorRoleColumn() {
		TableViewerColumn column;
		column = new TableViewerColumn(this, SWT.LEFT);
		column.getColumn().setText("执行角色");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String ap = na.getNodeActorParameter();
				ObjectId roleId = (ObjectId) processAssignment.get(ap);
				if (roleId != null) {
					ProjectRole roled = ModelService.createModelObject(
							ProjectRole.class, roleId);
					return roled.getLabel();
				}
				return "";

			}

		});

		if (roleDefinitions != null) {

			String[] rolednames = new String[roleDefinitions.size() + 1];
			rolednames[0] = "";
			for (int i = 1; i < rolednames.length; i++) {
				rolednames[i] = roleDefinitions.get(i - 1).getLabel();
			}
			final ComboBoxCellEditor combo = new ComboBoxCellEditor(
					this.getTable(), rolednames, SWT.READ_ONLY);

			EditingSupport editingSupport = new EditingSupport(this) {

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
					for (int i = 0; i < roleDefinitions.size(); i++) {
						PrimaryObject roled = roleDefinitions.get(i);
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
						PrimaryObject roled = roleDefinitions.get(index - 1);
						processAssignment.put(nodeActorParameter,
								roled.get_id());
					}
					data.setValue(key + IProcessControlable.POSTFIX_ASSIGNMENT,
							processAssignment);
					processAssignmentUpdated();
					refresh();
				}

			};
			column.setEditingSupport(editingSupport);

		}
		return column;
	}

	protected void createParameterColumn() {
		TableViewerColumn column;
		column = new TableViewerColumn(this, SWT.LEFT);
		column.getColumn().setText("指派参数");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.getNodeActorParameter();
			}

		});
	}

	protected void createRuleColumn() {
		TableViewerColumn column;
		column = new TableViewerColumn(this, SWT.LEFT);
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
	}

	protected void createAssignmentTypeColumn() {
		TableViewerColumn column;
		column = new TableViewerColumn(this, SWT.LEFT);
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
	}

	protected void createActionNameColumn() {
		TableViewerColumn column = new TableViewerColumn(this, SWT.LEFT);
		column.getColumn().setText("活动名称");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return ((NodeAssignment) element).getNodeName();
			}

		});
	}

	protected abstract void processAssignmentUpdated();

}
