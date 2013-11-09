package com.tmt.tb.editor.page;

import java.text.DecimalFormat;
import java.util.List;

import org.bson.types.ObjectId;
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
import com.mobnut.db.model.IPrimaryObjectValueChangeListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.commons.page.ProjectBudgetTreeContentProvider;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.TaskForm;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProjectBudgetOfCreateProjectPage implements IPageDelegator,
		IFormPart, IPrimaryObjectValueChangeListener {
	private TreeViewer viewer;
	private ProjectBudget root;
	private boolean isDirty;
	private IManagedForm form;
	private TaskForm taskForm;

	// TODO 需要修改

	public ProjectBudgetOfCreateProjectPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		taskForm = (TaskForm) input.getData();

		taskForm.addFieldValueListener("projecttemplate_id", this);

		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		viewer.setContentProvider(new ProjectBudgetTreeContentProvider());

		TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("预算科目");
		column.getColumn().setWidth(280);
		column.setLabelProvider(new ColumnLabelProvider());

		column = new TreeViewerColumn(viewer, SWT.RIGHT);
		column.getColumn().setText("预算金额(元)");
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
					return "";
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
						return "";
					} else {
						return "" + value;
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

		setViewerData();
		return (Composite) viewer.getControl();
	}

	private void setViewerData() {
		Object projectTemplateId = taskForm.getValue("projecttemplate_id");
		if(projectTemplateId == null){
			return;
		}
		
		ProjectTemplate template = ModelService.createModelObject(
				ProjectTemplate.class,
				(ObjectId) projectTemplateId);
		
		PrimaryObject budgetItem;
		List<PrimaryObject> budgetItems = template.getBudgetItems();
		if(budgetItems != null && budgetItems.size() >0){
			budgetItem = budgetItems.get(0);
		} else {
			return;
		}
		
		DBObject tgtData = new BasicDBObject();
		tgtData.put(ProjectBudget.F_CHILDREN,
				budgetItem.getValue(BudgetItem.F_CHILDREN));

		ModelService.createModelObject(tgtData, ProjectBudget.class);
		
		viewer.setInput(root.getChildren());
		viewer.expandAll();
	}

	@Override
	public IFormPart getFormPart() {
		return this;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
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
	public void valueChanged(String key, Object oldValue, Object newValue) {
		
	}
	


}
