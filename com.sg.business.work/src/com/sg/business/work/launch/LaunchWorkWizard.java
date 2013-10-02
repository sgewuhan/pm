package com.sg.business.work.launch;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sg.business.model.WorkDefinition;

public class LaunchWorkWizard extends Wizard {

	private SelectWorkDefinitionPage selectWorkDefinitionPage;
	private SchedualePage schedualPage;
	private WorkFlowSettingPage flowSettingPage;
	private WorkDefinition workd;

	public static LaunchWorkWizard OPEN() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			LaunchWorkWizard wiz = new LaunchWorkWizard();
			Shell shell = window.getShell();
			WizardDialog wizardDialog = new WizardDialog(shell, wiz) {
				@Override
				protected Point getInitialSize() {
					// Point size = super.getInitialSize();
					return new Point(600, 800);
				}
			};

			wizardDialog.open();
			return wiz;
		}
		return null;

	}

	public LaunchWorkWizard() {
		setWindowTitle("发起工作");
	}

	@Override
	public void addPages() {
		selectWorkDefinitionPage = new SelectWorkDefinitionPage();
		schedualPage = new SchedualePage();
		flowSettingPage = new WorkFlowSettingPage();
		addPage(selectWorkDefinitionPage);
		addPage(schedualPage);
		addPage(flowSettingPage);
	}

	@Override
	public boolean performFinish() {

		return true;
	}

	public void setWorkDefinition(WorkDefinition workd) {
		this.workd = workd;
	}

}
