package com.sg.business.commons.page;

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
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.ProjectRole;

public abstract class ProcessViewer extends TableViewer {

	private List<PrimaryObject> roleDefinitions;
	private DBObject processAssignment;
	private PrimaryObject data;
	private String key;
	private boolean editable;

	public ProcessViewer(Composite parent, String key, PrimaryObject data,
			List<PrimaryObject> roleDefinitions, boolean editable) {
		super(parent, SWT.FULL_SELECTION);
		this.editable = editable;
		this.data = data;
		this.key = key;
		processAssignment = (DBObject) data.getValue(key
				+ IProcessControl.POSTFIX_ASSIGNMENT);
		if (processAssignment == null) {
			processAssignment = new BasicDBObject();
			data.setValue(key + IProcessControl.POSTFIX_ASSIGNMENT,
					processAssignment);
		}
		this.roleDefinitions = roleDefinitions;
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(ArrayContentProvider.getInstance());
		createColumns(this);
	}

	public ProcessViewer(Composite parent, String key, PrimaryObject data,
			boolean editable) {
		super(parent, SWT.FULL_SELECTION);
		this.editable = editable;
		this.data = data;
		this.key = key;
		processAssignment = (DBObject) data.getValue(key
				+ IProcessControl.POSTFIX_ASSIGNMENT);
		if (processAssignment == null) {
			processAssignment = new BasicDBObject();
			data.setValue(key + IProcessControl.POSTFIX_ASSIGNMENT,
					processAssignment);
		}
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setContentProvider(ArrayContentProvider.getInstance());
		createColumns(this);
	}

	protected void createColumns(ProcessViewer processViewer) {

		createActionNameColumn(processViewer);

		createAssignmentTypeColumn(processViewer);

		createRuleColumn(processViewer);

		createParameterColumn(processViewer);

		createActorRoleColumn(processViewer);
	}

	protected TableViewerColumn createActorRoleColumn(
			ProcessViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessViewer_0);
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
				return ""; //$NON-NLS-1$

			}

		});

		if (editable && roleDefinitions != null) {

			String[] rolednames = new String[roleDefinitions.size() + 1];
			rolednames[0] = ""; //$NON-NLS-1$
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
					data.setValue(key + IProcessControl.POSTFIX_ASSIGNMENT,
							processAssignment);
					processAssignmentUpdated();
					refresh();
				}

			};
			column.setEditingSupport(editingSupport);

		}
		return column;
	}

	protected void createParameterColumn(ProcessViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessViewer_3);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				return na.getNodeActorParameter();
			}

		});
	}

	protected void createRuleColumn(ProcessViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessViewer_4);
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
	}

	protected void createAssignmentTypeColumn(ProcessViewer processViewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessViewer_6);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				if (na.isDyanmic()) {
					return Messages.get().ProcessViewer_7;
				} else if (na.isRuleAssignment()) {
					return Messages.get().ProcessViewer_8;
				} else if (na.isStaticActor()) {
					return Messages.get().ProcessViewer_9;
				} else if (na.isStaticGroup()) {
					return Messages.get().ProcessViewer_10;
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});
	}

	protected void createActionNameColumn(ProcessViewer processViewer) {
		TableViewerColumn column = new TableViewerColumn(processViewer,
				SWT.LEFT);
		column.getColumn().setText(Messages.get().ProcessViewer_12);
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
