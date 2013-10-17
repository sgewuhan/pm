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
		col.getColumn().setText("任务名称");
		col.getColumn().setWidth(100);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" + dbo.get(IProcessControl.F_WF_TASK_NAME);
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("执行人");
		col.getColumn().setWidth(64);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" +dbo.get("form_" + IProcessControl.F_WF_TASK_ACTOR);
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("操作");
		col.getColumn().setWidth(80);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					return "" +dbo.get("form_" + IProcessControl.F_WF_TASK_ACTION);
				}
				return super.getText(element);
			}

		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("结论");
		col.getColumn().setWidth(60);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					Object choice = dbObject.get("form_choice");
					if(choice!=null){
						return "" + choice;
					}
				}
				return "";
			}
		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("时间");
		col.getColumn().setWidth(140);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					Object startDate = dbo.get("form_"
							+ IProcessControl.F_WF_TASK_STARTDATE);
					Object finishDate = dbo.get("form_"
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
		col.getColumn().setText("说明");
		col.getColumn().setWidth(140);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbObject = (DBObject) element;
					Object comment = dbObject.get("form_comment");
					if(comment !=null){
						return "" + comment;
					}
				}
				return "";
			}
		});
		

//		col = new TableViewerColumn(viewer, SWT.LEFT);
//		col.getColumn().setText("结论");
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

}
