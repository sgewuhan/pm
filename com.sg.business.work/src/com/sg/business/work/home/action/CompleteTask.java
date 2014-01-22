package com.sg.business.work.home.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jbpm.task.Task;

import com.mobnut.db.model.IContext;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.bpm.workflow.taskform.IWorkflowInfoProvider;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class CompleteTask extends AbstractWorkDetailPageAction {

	@Override
	protected void run(Work work, Control control) {
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getConsignerId();
		// ����Ƿ��Ƕ��������������Ƕ����ʾ�˵�
		List<UserTask> tasksReserved = work.getReservedUserTasks(userId);
		List<UserTask> tasksInProgress = work.getInprogressUserTasks(userId);
		ArrayList<UserTask> tasks = new ArrayList<UserTask>();
		tasks.addAll(tasksReserved);
		tasks.addAll(tasksInProgress);
		if (tasks.isEmpty()) {
			MessageUtil.showToast(
					Messages.get(control.getDisplay()).CompleteTask_2,
					SWT.ICON_INFORMATION);
		} else if (tasks.size() > 1) {
			showMenu(work, tasks, control, context);
		} else {
//			showMenu(work, tasks, control, context);
			try {
				complete(work, tasks.get(0), context);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

	private void complete(Work work, UserTask userTask, IContext context)
			throws Exception {
		// �ж��Ƿ��ȿ�ʼ
		String taskstatus = userTask.getStatus();
		boolean canStartTask = WorkflowService.canStartTask(taskstatus);
		if (canStartTask) {
			work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
		}

		// 1. ����Ƿ��������̱�,��ͨ�������п���
		// ������������
		TaskFormConfig config = userTask.getTaskFormConfig();
		Task task = userTask.getTask();
		TaskForm taskForm = userTask.makeTaskForm();

		Map<String, Object> taskInputParameter = null;
		Map<String, Object> taskFormData = new HashMap<String, Object>();

		if (config != null) {
			// ִ�б���ǰ��У��
			IValidationHandler validator = config.getValidationHandler();
			if (validator != null) {
				boolean valid = validator.validate(work);
				if (!valid) {
					throw new Exception(validator.getMessage());
				}
			}

			// �Ƿ����˱༭������������˱༭�����򿪱༭��
			String taskFormEditorId = config.getEditorId();
			DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
					.getEditorRegistry().getConfigurator(taskFormEditorId);
			if (ec instanceof DataEditorConfigurator) {
				// ���ʹ����input�ṩ�ߣ�����input�ṩ�ߵķ��������taskForm
				taskForm = (TaskForm) config.getTaskFormInput(taskForm, task);

				DataObjectDialog dialog = DataObjectDialog.openDialog(taskForm,
						ec, true, config.getSaveHandler(), taskForm
								.getUserTask().getTaskName());
				int code = dialog.getReturnCode();
				if (code != DataObjectDialog.OK) {
					return;
				}
			}

			taskInputParameter = config.getInputParameter(taskForm);
			// ��ȡtaskform����Ҫ�־û����ֶ�
			String[] fields = config.getPersistentFields();
			for (int i = 0; i < fields.length; i++) {
				Object value = taskForm.getValue(fields[i]);
				taskFormData.put(fields[i], value);
			}
			IWorkflowInfoProvider infoProvider = config
					.getWorkflowInformationProvider();
			if (infoProvider != null) {
				taskFormData = infoProvider.getWorkflowInformation(taskForm);
			}

			taskFormData.put("editor", taskFormEditorId); //$NON-NLS-1$
		}

		// 2. ��ɹ���������
		work.doCompleteTask(Work.F_WF_EXECUTE, userTask, taskInputParameter,
				taskFormData, context);
		
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
						complete(work, userTask, context);
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
		long count = work.countReservedAndInprogressUserTasks(userId);
		return count>=1;
	}
}
