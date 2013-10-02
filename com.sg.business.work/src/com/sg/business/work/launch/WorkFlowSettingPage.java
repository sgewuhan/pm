package com.sg.business.work.launch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.resource.BusinessResource;

public class WorkFlowSettingPage extends WizardPage {

	protected WorkFlowSettingPage() {
		super("WORKFLOW_SETTING_PAGE");
		setTitle("请确定工作执行流程");
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_WORKFLOW_72));
	}

	@Override
	public void createControl(Composite parent) {
		Composite c = new Composite(parent,SWT.NONE);
		setControl(c);
	}

}
