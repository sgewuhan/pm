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

				// 1. ����Ƿ��������̱�,��ͨ�������п���
				String procDefId = task.getTaskData().getProcessId();
				List<I18NText> names = task.getNames();
				Assert.isTrue(names.size() > 0, "ȱ����������");

				
				String taskName = names.get(0).getText();
				// ������������
				TaskFormConfig config = WorkflowService.getDefault()
						.getTaskFormConfig(procDefId, taskName);
				if (config != null) {
					// ִ�б���ǰ��У��
					IValidationHandler validator = config
							.getValidationHandler();
					if (validator != null) {
						boolean valid = validator.validate(work);
						if (!valid) {
							throw new Exception(validator.getMessage());
						}
					}

					// �Ƿ����˱༭������������˱༭�����򿪱༭��
					String taskFormEditorId = config.getEditorId();
					DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
							.getEditorRegistry().getConfigurator(
									taskFormEditorId);
					if (ec instanceof DataEditorConfigurator) {
						// ���ʹ����input�ṩ�ߣ�����input�ṩ�ߵķ��������taskForm
						taskForm = (TaskForm) config.getTaskFormInput(taskForm);

						DataObjectDialog dialog = DataObjectDialog.openDialog(
								taskForm, ec, true, null, "���̱�");
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
		MessageUtil.showToast("��ѡ������ִ����������������", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(event);
	}
	

}
