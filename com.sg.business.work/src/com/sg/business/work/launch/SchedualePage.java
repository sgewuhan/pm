package com.sg.business.work.launch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.resource.BusinessResource;

public class SchedualePage extends WizardPage {

	protected SchedualePage() {
		super("SCHEDUAL_PAGE");
		setTitle("请填写工作基本信息和计划");
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_SCHEDULE_72));
	}

	@Override
	public void createControl(Composite parent) {
		Composite c = new Composite(parent,SWT.NONE);
		setControl(c);	}

}
