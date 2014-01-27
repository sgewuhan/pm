package com.sg.business.commons.editor.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.mongodb.DBObject;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class WorkFlowRecord implements IPageDelegator {

	private TableViewer viewer;
	public WorkFlowRecord() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		
		
		
		createTable(parent);
		List<?> records = new ArrayList<>();
		Object obj = input.getData();
		if (obj instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) obj;
			Work work = taskForm.getWork();
			Object history = work.getValue("wf_execute_history"); //$NON-NLS-1$
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
		col.getColumn().setText(Messages.get().WorkFlowRecord_1);
		col.getColumn().setWidth(100);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" + dbo.get("taskname"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(Messages.get().WorkFlowRecord_4);
		col.getColumn().setWidth(64);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" +dbo.get("form_" + IProcessControl.F_WF_TASK_ACTOR); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(Messages.get().WorkFlowRecord_7);
		col.getColumn().setWidth(80);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" +dbo.get("form_" + IProcessControl.F_WF_TASK_ACTION); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(Messages.get().WorkFlowRecord_10);
		col.getColumn().setWidth(60);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					Object choice = dbObject.get("form_choice"); //$NON-NLS-1$
					if(choice!=null){
						return "" + choice; //$NON-NLS-1$
					}
				}
				return ""; //$NON-NLS-1$
			}
		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(Messages.get().WorkFlowRecord_14);
		col.getColumn().setWidth(140);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					Object startDate = dbo.get("form_" //$NON-NLS-1$
							+ IProcessControl.F_WF_TASK_STARTDATE);
					Object finishDate = dbo.get("form_" //$NON-NLS-1$
							+ IProcessControl.F_WF_TASK_FINISHDATE);
					SimpleDateFormat sdf = new SimpleDateFormat(
							Utils.SDF_DATETIME_COMPACT_SASH);
					if (startDate instanceof Date) {
						return sdf.format(startDate);
					}
					if (finishDate instanceof Date) {
						return sdf.format(finishDate);
					}
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(Messages.get().WorkFlowRecord_17);
		col.getColumn().setWidth(140);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					Object comment = dbObject.get("form_comment"); //$NON-NLS-1$
					if(comment !=null){
						return "" + comment; //$NON-NLS-1$
					}
				}
				return ""; //$NON-NLS-1$
			}
		});
		

//		col = new TableViewerColumn(viewer, SWT.LEFT);
//		col.getColumn().setText("½áÂÛ");
//		col.getColumn().setWidth(120);
//		col.setLabelProvider(new ColumnLabelProvider() {
//			@Override
//			public String getText(Object element) {
//				StringBuffer sb = new StringBuffer();
//				if (element instanceof DBObject) {
//					DBObject dbo = (DBObject) element;
//					Set<String> keySet = dbo.keySet();
//					for (String key : keySet) {
//							sb.append(key + ":"+dbo.get(key)+"  ");
//					}
//				}
//				return sb.toString();
//			}
//
//		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());
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

	@Override
	public boolean createBody() {
		return false;
	}

}
