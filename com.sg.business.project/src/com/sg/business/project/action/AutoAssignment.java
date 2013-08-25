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
		setText("����ɫ�Զ�ָ��");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_ASSIGNMENT_24));
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		MessageUtil
				.showMessage(
						shell,
						"����ɫ�Զ�ָ��",
						"��Ҫ���а���ɫָ�ɡ�\n�����ĸ����ˡ������ߺ����̻��ִ���˶������ս�ɫ���з�����Ա��\n������ɺ󣬽���������ǰ���趨��\n\nѡ��YES������ѡ��NOȡ��������\n",
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		// �����Ŀ
		Project project = (Project) getInput().getData();
		try {
			project.doAssignmentByRole(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
		}
		MessageUtil.showToast("��ɫ�Զ�ָ�����", SWT.ICON_INFORMATION);
		getNavigator().reload();
	}
}
