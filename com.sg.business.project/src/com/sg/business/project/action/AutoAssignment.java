package com.sg.business.project.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorAction;

/**
 * @author zhonghua
 */
public class AutoAssignment extends NavigatorAction {

	public AutoAssignment() {
		setText("按角色自动指派");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_ASSIGNMENT_24));
	}

	@Override
	public void execute() throws Exception {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		int result = MessageUtil
				.showMessage(
						shell,
						"按角色自动指派",
						"将要运行按角色指派。\n工作的负责人、参与者和流程活动的执行人都将按照角色进行分配人员。\n运行完成后，将覆盖您以前的设定。\n\n选择YES继续，选择NO取消操作。\n",
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		if (result == SWT.YES) {
			// 获得项目
			Project project = (Project) getInput().getData();
			project.doAssignmentByRole(new CurrentAccountContext());
			MessageUtil.showToast("角色自动指派完成", SWT.ICON_INFORMATION);
			getNavigator().reload();
			getNavigator().getViewerControl().loadColumnGroup("team");
		}
	}
}
