package com.sg.business.management.editor.page;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.sg.business.management.wizard.ProjectPreviewer;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.resource.BusinessResource;

public class PreviewOptionAction extends Action {

	private ProjectTemplate projectTemplate;

	public PreviewOptionAction(ProjectTemplate projectTemplate) {
		this.projectTemplate = projectTemplate;
		setText("查看选项预览");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_PREVIEW_24));
	}

	@Override
	public void run() {

		IWizard wiz = new ProjectPreviewer(projectTemplate);
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog wizardDialog = new WizardDialog(shell, wiz);
		wizardDialog.open();
	}

}
