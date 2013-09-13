package com.sg.business.work.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.jbpm.task.I18NText;
import org.jbpm.task.Status;
import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.bpm.workflow.taskform.IWorkflowInfoProvider;
import com.sg.bpm.workflow.taskform.TaskFormConfig;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CompleteTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			try {
				CurrentAccountContext context = new CurrentAccountContext();

				Work work = (Work) selected;

				WorkflowSynchronizer sync = new WorkflowSynchronizer();
				sync.synchronizeUserTask(context.getAccountInfo()
						.getConsignerId(), work);

				// �ж��Ƿ��ȿ�ʼ
				Task task = work.getTask(Work.F_WF_EXECUTE, context);
				Status taskstatus = task.getTaskData().getStatus();
				boolean canStartTask = WorkflowService.canStartTask(taskstatus
						.name());
				if (canStartTask) {
					MessageUtil.showToast("����������������δ��ʼ��ϵͳ�Զ���ʼ����",
							SWT.ICON_INFORMATION);
					work.doStartTask(Work.F_WF_EXECUTE, context);
				}

				// 1. ����Ƿ��������̱�,��ͨ�������п���
				String procDefId = task.getTaskData().getProcessId();
				List<I18NText> names = task.getNames();
				Assert.isTrue(names.size() > 0, "ȱ����������");

				String taskName = names.get(0).getText();
				// ������������
				TaskFormConfig config = WorkflowService.getDefault()
						.getTaskFormConfig(procDefId, taskName);
				TaskForm taskForm = work.makeTaskForm();
				Map<String, Object> taskInputParameter = null;

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

				// ��ȡtaskform����Ҫ�־û����ֶ�
				String[] fields = config.getPersistentFields();
				Map<String, Object> taskFormData = new HashMap<String, Object>();
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

				// 2. ��ɹ���������
				work.doCompleteTask(Work.F_WF_EXECUTE, taskInputParameter,
						taskFormData, context);

				// 3.ˢ�±��
				ViewerControl vc = getCurrentViewerControl(event);
				vc.getViewer().update(work, null);
			} catch (Exception e) {
				MessageUtil.showToast("�����������", e);
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("��ѡ������ִ����������������", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(event);
	}

}
