package com.sg.business.work.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.jbpm.task.I18NText;
import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class CompleteTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			try {
				Work work = (Work) selected;
				TaskForm taskForm = work.makeTaskForm();
				Map<String, Object> taskInputParameter = null;

				Task task = work.getTask(Work.F_WF_EXECUTE, true);

				// 1. 检查是否定义了流程表单,并通过表单进行控制
				String procDefId = task.getTaskData().getProcessId();
				List<I18NText> names = task.getNames();
				Assert.isTrue(names.size() > 0, "缺少任务名称");

				
				String taskName = names.get(0).getText();
				// 获得任务表单配置
				TaskFormConfig config = WorkflowService.getDefault()
						.getTaskFormConfig(procDefId, taskName);
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
						taskForm = (TaskForm) config.getTaskFormInput(taskForm);

						DataObjectDialog dialog = DataObjectDialog.openDialog(
								taskForm, ec, true, null, "流程表单");
						int code = dialog.getReturnCode();
						if (code != DataObjectDialog.OK) {
							return;
						}
					}
					
					taskInputParameter = config.getInputParameter(taskForm);
				}

				work.doCompleteTask(Work.F_WF_EXECUTE,
						taskInputParameter, new CurrentAccountContext());
				
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("请选择工作后执行完成流程任务操作", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(event);
	}
	

}
