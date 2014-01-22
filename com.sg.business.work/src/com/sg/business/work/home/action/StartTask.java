package com.sg.business.work.home.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.mobnut.db.model.IContext;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class StartTask extends AbstractWorkDetailPageAction {

	@Override
	protected void run(Work work, Control control) {
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getConsignerId();
		// 检查是否是多个流程任务，如果是多个显示菜单
		List<UserTask> tasksReserved = work.getReservedUserTasks(userId);
		ArrayList<UserTask> tasks = new ArrayList<UserTask>();
		tasks.addAll(tasksReserved);
		if (tasks.isEmpty()) {
			MessageUtil.showToast(
					Messages.get(control.getDisplay()).CompleteTask_2,
					SWT.ICON_INFORMATION);
		} else if (tasks.size() > 1) {
			showMenu(work, tasks, control, context);
		} else {
			try {
				start(work, tasks.get(0), context);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

	private void start(Work work, UserTask userTask, IContext context)
			throws Exception {
		// 判断是否先开始
		String taskstatus = userTask.getStatus();
		boolean canStartTask = WorkflowService.canStartTask(taskstatus);
		if (canStartTask) {
			work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
		}

		pageClear();
	}

	private void showMenu(final Work work, ArrayList<UserTask> tasks,
			Control control, final IContext context) {
		final Menu dropDownMenu = new Menu(control.getShell(), SWT.POP_UP);
		for (int i = 0; i < tasks.size(); i++) {
			final UserTask userTask = tasks.get(i);
			MenuItem item = new MenuItem(dropDownMenu, SWT.PUSH);
			item.setText(userTask.getTaskName());
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						start(work, userTask, context);
					} catch (Exception e1) {
						MessageUtil.showToast(e1);
					}
				}
			});
		}
		Point point = control.toDisplay(0,
				control.getBounds().y + control.getBounds().height);
		dropDownMenu.setLocation(point);
		dropDownMenu.setVisible(true);
	}

	@Override
	protected boolean visiableWhen(Work work) {
		String userId = getContext().getAccountInfo().getUserId();
		long count = work.countReservedUserTasks(userId);
		return count>=1;
	}

}
