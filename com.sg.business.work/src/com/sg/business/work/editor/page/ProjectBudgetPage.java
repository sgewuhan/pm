package com.sg.business.work.editor.page;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.project.editor.page.ProjectBudgetTreeContentProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class ProjectBudgetPage implements IPageDelegator {

	private TreeViewer viewer;
	private ProjectBudget root;

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
		column.getColumn().setText("预算科目");
		column.getColumn().setWidth(280);
		column.setLabelProvider(new ColumnLabelProvider());
		
		column = new TreeViewerColumn(viewer, SWT.RIGHT);
		column.getColumn().setText("预算金额(元)");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				ProjectBudget budget = (ProjectBudget)element;
				Double value = budget.getBudgetValue();
				if(value!=null){
					DecimalFormat df = new DecimalFormat(Utils.NF_RMB_MONEY);
					return df.format(value);
				}else{
					return "";
				}
			}
		});
		
		viewer.setInput(root.getChildren());
		viewer.expandAll();
		return (Composite) viewer.getControl();
	}

	@Override
	public IFormPart getFormPart() {
		return null;
	}




}
