package com.sg.business.project.action;

import org.eclipse.swt.SWT;

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
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_ASSIGNMENT_24));
	}

	
	@Override
	public void run() {
		//�����Ŀ
		Project project = (Project) getInput().getData();
		try {
			project.doAssignmentByRole(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
		}
		// TODO Auto-generated method stub
		super.run();
	}
}
