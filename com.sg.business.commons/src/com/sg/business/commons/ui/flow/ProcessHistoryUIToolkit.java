package com.sg.business.commons.ui.flow;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProcessHistoryUIToolkit {

	public static void handleProcessHistoryTable(Table table){
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try{
						String _id = event.text.substring(event.text.lastIndexOf("/")+1, event.text.indexOf("@"));
						String action = event.text.substring(event.text.indexOf("@")+1 );
						if("open".equals(action)){
							UserTask userTask = ModelService.createModelObject(UserTask.class, new ObjectId(_id));
							doOpenTaskForm(userTask);
						}
					}catch(Exception e){
					}
				}
			}
		});
	}
	
	private static void doOpenTaskForm(UserTask userTask) {
		String editorId = (String) userTask.getStringValue(TaskForm.F_EDITOR);
		if (editorId == null) {
			return;
		}
		
		BasicDBObject taskData = new BasicDBObject();
		Iterator<String> iter = userTask.get_data().keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			if(key .startsWith("form_")){
				String nkey = key.substring(5);
				taskData.put(nkey, userTask.getValue(key));
			}
		}
		taskData.put(TaskForm.F_WORK_ID, userTask.getWorkId());
		DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		TaskForm taskForm = ModelService.createModelObject(taskData,
				TaskForm.class);
		try {
			DataObjectDialog.openDialog(taskForm, ec, false, null,
					"流程表单");
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}
	
}
