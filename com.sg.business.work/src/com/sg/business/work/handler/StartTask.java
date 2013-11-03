package com.sg.business.work.handler;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class StartTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {
			UserTask userTask = null;
			Object _usertask = parameters.get("runtimework.usertask");
			try {
				DBObject userTaskData = (DBObject) JSON
						.parse((String) _usertask);
				userTask = ModelService.createModelObject(userTaskData,
						UserTask.class);

			} catch (Exception e) {
			}
			if (userTask == null) {
				Object _userTaskId = parameters.get("runtimework.usertask_id");
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
						MessageUtil.showToast("û������Ҫִ�е���������",
								SWT.ICON_INFORMATION);
						return;
					}

					userTask = userTasks.get(0);
				}

				work.doStartTask(Work.F_WF_EXECUTE, userTask, context);
				vc.getViewer().update(work, null);
				vc.getViewer().setSelection(new StructuredSelection());
			} catch (Exception e) {
				MessageUtil.showToast("��ʼ��������", e);
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("��ѡ������ִ�п�ʼ�����������", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(part, vc, command);
	}

}
