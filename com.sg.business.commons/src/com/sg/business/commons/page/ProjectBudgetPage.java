package com.sg.business.commons.page;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.ExcelExportJob;
import com.mobnut.commons.util.file.IColumnExportDefinition;
import com.mobnut.commons.util.file.IExportValueDelegator;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProjectBudgetPage implements IPageDelegator, IFormPart {

	private TreeViewer viewer;
	private ProjectBudget root;
	private boolean isDirty;
	private IManagedForm form;

	public ProjectBudgetPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		Project project = (Project) input.getData();
		root = project.getBudget();

		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		viewer.setContentProvider(new ProjectBudgetTreeContentProvider());

		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText(Messages.get().ProjectBudgetPage_0);
		column.getColumn().setWidth(280);
		column.setLabelProvider(new ColumnLabelProvider());

		column = new TreeViewerColumn(viewer, SWT.RIGHT);
		column.getColumn().setText(Messages.get().ProjectBudgetPage_1);
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ProjectBudget budget = (ProjectBudget) element;
				Double value = budget.getBudgetValue();
				if (value != null) {
					DecimalFormat df = new DecimalFormat(Utils.NF_RMB_MONEY);
					return df.format(value);
				} else {
					return ""; //$NON-NLS-1$
				}
			}
		});

		if (input.isEditable()) {
			EditingSupport es = new EditingSupport(viewer) {

				@Override
				protected void setValue(Object element, Object value) {
					ProjectBudget budget = (ProjectBudget) element;
					Double val = Utils.getDoubleValue(value);

					budget.inputBudgetValue(val);
					setDirty(true);
					viewer.refresh();
				}

				@Override
				protected Object getValue(Object element) {
					ProjectBudget budget = (ProjectBudget) element;
					Double value = budget.getBudgetValue();
					if (value == null) {
						return ""; //$NON-NLS-1$
					} else {
						return "" + value; //$NON-NLS-1$
					}
				}

				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(viewer.getTree());
				}

				@Override
				protected boolean canEdit(Object element) {
					ProjectBudget budget = (ProjectBudget) element;
					return !budget.hasChildren();
				}
			};
			column.setEditingSupport(es);

		}

		if (root == null) {
			viewer.setInput(new ProjectBudget[0]);
		} else {
			viewer.setInput(root.getChildren());
		}

		viewer.expandAll();
		return (Composite) viewer.getControl();
	}

	
	private void doExport() {
		ExcelExportJob job = new ExcelExportJob(Messages.get().ProjectBudgetPage_5);

		IColumnExportDefinition[] columns = new IColumnExportDefinition[2];
		columns[0] = new IColumnExportDefinition() {

			@Override
			public String getName() {
				return Messages.get().ProjectBudgetPage_6;
			}

			@Override
			public String getType() {
				return Utils.TYPE_STRING;
			}

			@Override
			public int getWidth() {
				return 0;
			}

			@Override
			public String getColumn() {
				return "desc"; //$NON-NLS-1$
			}

			@Override
			public IExportValueDelegator getExportValueDelegator() {
				return new IExportValueDelegator() {
					@Override
					public Object getValue(
							Map<String, Object> dataRow,
							IColumnExportDefinition iColumnExportDefinition) {

						String dataRowValue = (String) dataRow
								.get("desc"); //$NON-NLS-1$
						Object level = dataRow.get("level"); //$NON-NLS-1$
						if (level instanceof Integer) {
							for (int i = 0; i < (Integer) level; i++) {
								dataRowValue = "   " + dataRowValue; //$NON-NLS-1$
							}
						}

						return dataRowValue;
					}

				};
			}

		};

		columns[1] = new IColumnExportDefinition() {

			@Override
			public String getName() {
				return Messages.get().ProjectBudgetPage_11;
			}

			@Override
			public String getType() {
				return Utils.TYPE_DOUBLE;
			}

			@Override
			public int getWidth() {
				return 0;
			}

			@Override
			public String getColumn() {
				return "budgetvalue"; //$NON-NLS-1$
			}

			@Override
			public IExportValueDelegator getExportValueDelegator() {
				return null;
			}

		};

		Object input = viewer.getInput();
		job.setColumnExportDefinition(columns);
		job.setInput(getExportData(((Object[]) input)[0], 0));
		job.setUser(true);
		job.setFormat(false);
		job.start(viewer.getControl().getDisplay());
	}
	

	private List<PrimaryObject> getExportData(Object obj, int level) {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (obj instanceof ProjectBudget) {
			ProjectBudget projectBudget = (ProjectBudget) obj;
			projectBudget.setValue("level", level); //$NON-NLS-1$
			result.add(projectBudget);
			ProjectBudget[] children = projectBudget.getChildren();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					ProjectBudget child = children[i];
					child.setValue("level", level); //$NON-NLS-1$
					result.addAll(getExportData(child, level + 1));
				}
			}
		}
		return result;
	}

	@Override
	public IFormPart getFormPart() {
		return this;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
		
		Action action = new Action(Messages.get().ProjectBudgetPage_15){
			@Override
			public void run() {
				doExport();
			}
			
		};
		action.setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
		form.getForm().getToolBarManager().add(action);
		form.getForm().updateToolBar();
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	private void setDirty(boolean b) {
		isDirty = b;
		form.dirtyStateChanged();
	}

	@Override
	public void commit(boolean onSave) {
		if (!onSave) {
			try {
				root.doSave(new CurrentAccountContext());
			} catch (Exception e) {
			}
		} else {
			setDirty(false);
		}
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {
		viewer.getTree().setFocus();
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public boolean createBody() {
		return false;
	}

}
