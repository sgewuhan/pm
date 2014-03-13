package com.sg.business.work.page;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Work;
import com.sg.business.model.WorkRecord;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.viewer.ColumnAutoResizer;

public class WorkRecordPage extends AbstractFormPageDelegator {

	private TableViewer viewer;

	public WorkRecordPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		createTable(parent);
		Work work = (Work) input.getData();
		List<WorkRecord> records = work.getWorkRecord();
		viewer.setInput(records);
		return viewer.getTable();
	}

	private void createTable(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION );
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		UIFrameworkUtils.enableMarkup(table);
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(40));

		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		new ColumnAutoResizer(table, col.getColumn());
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((WorkRecord) element).getHTMLLabel();
			}

		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event
						.getSelection();
				WorkRecord wr = (WorkRecord) is.getFirstElement();
				try {
					DataObjectDialog.openDialog(wr, "editor.create.workrecord", //$NON-NLS-1$
							false, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}

			}
		});
	}

	@Override
	public void commit(boolean onSave) {

	}

	@Override
	public void setFocus() {

	}

}
