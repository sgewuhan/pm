package com.sg.business.work.view;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.view.TableNavigator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class WorkFlowHistory extends TableNavigator {

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		Table table = (Table) navi.getControl();
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try{
						String _id = event.text.substring(event.text.lastIndexOf("/")+1, event.text.indexOf("@"));
						String action = event.text.substring(event.text.indexOf("@")+1 );
						UserTask userTask = ModelService.createModelObject(UserTask.class, new ObjectId(_id));
						if("open".equals(action)){
							doOpenTaskForm(userTask);
						}
					}catch(Exception e){
					}
				}
			}
		});
	}

	protected void doOpenTaskForm(UserTask userTask) {
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

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master != null) {
			String workname = ((UserTask)master).getWorkName();
			String name = ((UserTask)master).getProcessName();
			setPartName(workname+"|"+name); //$NON-NLS-1$
		} else {
			setPartName(originalPartName);
		}
	}
}
