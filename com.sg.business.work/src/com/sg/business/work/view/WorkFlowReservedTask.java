package com.sg.business.work.view;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.work.handler.CompleteTask;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.view.TableNavigator;

public class WorkFlowReservedTask extends TableNavigator {

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
						if("start".equals(action)){
							doStart(userTask);
						}else{
							doComplete(userTask);
						}
						doRefresh();
					}catch(Exception e){
					}
				}
			}
		});
	}

	protected void doComplete(UserTask userTask) {
		IContext context = getContext();
		Work work = userTask.getWork();
		CompleteTask handler = new CompleteTask();
		try {
			handler.doComplete(userTask, work, context);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	protected void doStart(UserTask userTask) {
		IContext context = getContext();
		Work work = userTask.getWork();
		try {
			work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}
}
