package com.sg.business.project.action;

import org.eclipse.swt.SWT;

import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorAction;

public class RecalculateSEQ extends NavigatorAction {

	public RecalculateSEQ() {
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WBS_SORT_24));
		setText("重新排列序号");
	}

	@Override
	public void execute() throws Exception {
		Project project = (Project) getInput().getData();
		project.doArrangeWBSCode(new CurrentAccountContext());
		MessageUtil.showToast("重新排列序号完成", SWT.ICON_INFORMATION);
		getNavigator().reload();
	}

}
