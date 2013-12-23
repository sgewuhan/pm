package com.sg.business.work.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.jbpm.task.Task;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.bpm.workflow.taskform.IWorkflowInfoProvider;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.business.work.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CompleteTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {

			UserTask userTask = null;
			Object _usertask = parameters.get("runtimework.usertask"); //$NON-NLS-1$
			try {
				DBObject userTaskData = (DBObject) JSON
						.parse((String) _usertask);
				userTask = ModelService.createModelObject(userTaskData,
						UserTask.class);

			} catch (Exception e) {
			}
			if (userTask == null) {
				Object _userTaskId = parameters.get("runtimework.usertask_id"); //$NON-NLS-1$
				if (_userTaskId != null) {
					ObjectId _id = new ObjectId((String) _userTaskId);
					userTask = ModelService.createModelObject(UserTask.class,
							_id, false);
				}
			}

			try {
				Work work = (Work) selected;
				WorkflowSynchronizer sync = new WorkflowSynchronizer();
				CurrentAccountContext context = new CurrentAccountContext();
				String userid = context.getAccountInfo().getConsignerId();

				if (userTask == null) {

					List<UserTask> userTasks = sync.synchronizeUserTask(userid,
							work);

					if (userTasks.isEmpty()) {
						MessageUtil.showToast(Messages.get().CompleteTask_2,
								SWT.ICON_INFORMATION);
						return;
					}

					userTask = userTasks.get(0);
				}
				// 判断是否先开始
				String taskstatus = userTask.getStatus();
				boolean canStartTask = WorkflowService.canStartTask(taskstatus);
				if (canStartTask) {
					MessageUtil.showToast(Messages.get().CompleteTask_3,
							SWT.ICON_INFORMATION);
					work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
				}

				doComplete(userTask, work, context);

				// 3.刷新表格
				vc.getViewer().update(work, null);
				vc.getViewer().setSelection(new StructuredSelection());
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(Messages.get().CompleteTask_4, e);
			}
		}
	}

	public void doComplete(UserTask userTask, Work work,
			IContext context) throws Exception {
		// 1. 检查是否定义了流程表单,并通过表单进行控制
		// 获得任务表单配置
		TaskFormConfig config = userTask.getTaskFormConfig();
		Task task = userTask.getTask();
		TaskForm taskForm = userTask.makeTaskForm();

		Map<String, Object> taskInputParameter = null;
		Map<String, Object> taskFormData = new HashMap<String, Object>();

		if (config != null) {
			// 执行表单打开前的校验
			IValidationHandler validator = config
					.getValidationHandler();
			if (validator != null) {
				boolean valid = validator.validate(work);
				if (!valid) {
					throw new Exception(validator.getMessage());
				}
			}

			// 是否定义了编辑器，如果定义了编辑器，打开编辑器
			String taskFormEditorId = config.getEditorId();
			DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
					.getEditorRegistry().getConfigurator(
							taskFormEditorId);
			if (ec instanceof DataEditorConfigurator) {
				// 如果使用了input提供者，调用input提供者的方法来获得taskForm
				taskForm = (TaskForm) config.getTaskFormInput(taskForm,
						task);

				DataObjectDialog dialog = DataObjectDialog.openDialog(
						taskForm, ec, true, config.getSaveHandler(),
						taskForm.getUserTask().getTaskName());
				int code = dialog.getReturnCode();
				if (code != DataObjectDialog.OK) {
					return;
				}
			}

			taskInputParameter = config.getInputParameter(taskForm);
			// 获取taskform中需要持久化的字段
			String[] fields = config.getPersistentFields();
			for (int i = 0; i < fields.length; i++) {
				Object value = taskForm.getValue(fields[i]);
				taskFormData.put(fields[i], value);
			}
			IWorkflowInfoProvider infoProvider = config
					.getWorkflowInformationProvider();
			if (infoProvider != null) {
				taskFormData = infoProvider
						.getWorkflowInformation(taskForm);
			}

			taskFormData.put("editor", taskFormEditorId); //$NON-NLS-1$
		}

		// 2. 完成工作流任务
		work.doCompleteTask(Work.F_WF_EXECUTE, userTask,
				taskInputParameter, taskFormData, context);
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().CompleteTask_6, SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(part, vc, command);
	}

}
