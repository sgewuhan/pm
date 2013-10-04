package com.sg.business.work.launch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.resource.BusinessResource;

public class ConfirmPage extends WizardPage {

	protected ConfirmPage() {
		super("WORKFLOW_CONFIRM_PAGE");
		setTitle("请设定是否立即开始工作");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WORKFLOW_72));
	}

	@Override
	public void createControl(Composite parent) {
		final Button content = new Button(parent,SWT.CHECK);
		content.setText("立即开始工作");
		content.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((LaunchWorkWizard)getWizard()).setStartWorkWhenFinsh(content.getSelection());
				super.widgetSelected(e);
			}
		});
		
		setControl(content);
		setPageComplete(false);
	}

}
