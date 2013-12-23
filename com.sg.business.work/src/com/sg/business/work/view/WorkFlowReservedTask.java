package com.sg.business.work.view;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.work.handler.CompleteTask;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.view.AutoRefreshableTableView;

public class WorkFlowReservedTask extends AutoRefreshableTableView {

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		Table table = (Table) navi.getControl();
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String _id = event.text.substring(
								event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
								event.text.indexOf("@")); //$NON-NLS-1$
						String action = event.text.substring(event.text
								.indexOf("@") + 1); //$NON-NLS-1$
						UserTask userTask = ModelService.createModelObject(
								UserTask.class, new ObjectId(_id));
						if ("start".equals(action)) { //$NON-NLS-1$
							doStart(userTask);
						} else {
							doComplete(userTask);
						}
						doRefresh();
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

	protected void doComplete(UserTask userTask) throws Exception {
		IContext context = getContext();
		Work work = userTask.getWork();
		CompleteTask handler = new CompleteTask();
		handler.doComplete(userTask, work, context);
	}

	protected void doStart(UserTask userTask) throws Exception {
		IContext context = getContext();
		Work work = userTask.getWork();
		work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
	}

	@Override
	protected int getInterval() {
		IContext context = getContext();
		Object val = Setting.getUserSetting(context.getAccountInfo()
				.getUserId(),
				IModelConstants.S_U_TASK_RESERVED_REFRESH_INTERVAL);
		Integer value = Utils.getIntegerValue(val);
		if (value != null) {
			return value.intValue() * 60000;// 分钟为单位
		} else {
			return 600000;// 间隔10分钟刷新
		}
	}
}
