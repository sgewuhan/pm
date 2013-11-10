package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.forms.IFormPart;

import com.mongodb.DBObject;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class TaskFormStandlonework implements IPageDelegator {

	public TaskFormStandlonework() {
	}

	private TableViewer viewer;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		
		createTable(parent);
		List<?> records = new ArrayList<>();
		Object obj = input.getData();
		if (obj instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) obj;
			Work work = taskForm.getWork();
			Object history = work.getValue("wf_execute_history");
			if (history instanceof List) {
				records = (List<?>) history;
			}
		}

		viewer.setInput(records);
		return viewer.getTable();
	}

	private void createTable(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("工作名称");
		col.getColumn().setWidth(100);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" + dbo.get("taskname");
				}
				return super.getText(element);
			}

		});
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		Menu menu = new Menu(viewer.getTable());
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("添加科目");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		viewer.getTable().setMenu(menu);
	}
	
	

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public void refresh() {

	}

	@Override
	public IFormPart getFormPart() {
		return null;
	}

}
