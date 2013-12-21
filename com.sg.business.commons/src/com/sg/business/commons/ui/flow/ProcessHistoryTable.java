package com.sg.business.commons.ui.flow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.TaskForm;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProcessHistoryTable extends TableViewer {

	// private BasicBSONList procHistory;
	// private DroolsProcessDefinition procDefinition;

	public ProcessHistoryTable(Composite parent) {
		super(parent, SWT.FULL_SELECTION | SWT.BORDER);

		createTable();

	}

	private void createTable() {
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);
		

		// 创建任务名称列
		TableViewerColumn col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_0);
		col.getColumn().setWidth(100);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					return "" + dbObject.get("taskname"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}
		});

		// 任务执行人
		col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_3);
		col.getColumn().setWidth(64);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					return "" + dbObject.get("form_" + IProcessControl.F_WF_TASK_ACTOR); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}
		});

		// 操作
		col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_6);
		col.getColumn().setWidth(80);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					return "" + dbObject.get("form_" + IProcessControl.F_WF_TASK_ACTION); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return super.getText(element);
			}
		});
		
		col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_9);
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

		// 操作时间
		col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_13);
		col.getColumn().setWidth(140);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					Object startDate = dbObject.get("form_" //$NON-NLS-1$
							+ IProcessControl.F_WF_TASK_STARTDATE);
					Object finishDate = dbObject.get("form_" //$NON-NLS-1$
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
		
		col = new TableViewerColumn(this, SWT.LEFT);
		col.getColumn().setText(Messages.get().ProcessHistoryTable_16);
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

		setContentProvider(ArrayContentProvider.getInstance());

		addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event
						.getSelection();
				if (is != null && !is.isEmpty()) {
					DBObject dbo = (DBObject) is.getFirstElement();
					String editorId = (String) dbo.get(TaskForm.F_EDITOR);
					if (editorId == null) {
						return;
					}
					
					BasicDBObject taskData = new BasicDBObject();
					Iterator<String> iter = dbo.keySet().iterator();
					while(iter.hasNext()){
						String key = iter.next();
						if(key .startsWith("form_")){ //$NON-NLS-1$
							String nkey = key.substring(5);
							taskData.put(nkey, dbo.get(key));
						}
					}
					DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
							.getEditorRegistry().getConfigurator(editorId);
					TaskForm taskForm = ModelService.createModelObject(taskData,
							TaskForm.class);
					try {
						DataObjectDialog.openDialog(taskForm, ec, false, null,
								Messages.get().ProcessHistoryTable_21);
					} catch (Exception e) {
					}

				}
			}
		});
	}

	public void setInput(DroolsProcessDefinition procDefinition,
			BasicBSONList procHistory) {
		// this.procDefinition = procDefinition;
		// this.procHistory = procHistory;
		setInput(procHistory);
	}

	public void setInput(Object procDefinition,
			List<Map<String, Object>> history) {

		BasicBSONList list = new BasicBSONList();
		if(history!=null){
			Iterator<Map<String, Object>> iter = history.iterator();
			while(iter.hasNext()){
				Map<String, Object> map = iter.next();
				list.add(new BasicDBObject(map));
			}
		}
		setInput(list);

		
	}
	
	
}
