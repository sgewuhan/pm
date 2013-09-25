package com.sg.business.project.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sg.business.model.Organization;
import com.sg.business.model.Project;

public class TransferUsersWizard extends Wizard {

	private List<TransferPageConfig> transferPageConfigs;
	private String userId;

	public TransferUsersWizard(List<TransferPageConfig> transferPageConfigs) {
		this.transferPageConfigs = transferPageConfigs;
	}

	@Override
	public void addPages() {
		for (int i = 0; i < transferPageConfigs.size(); i++) {
			TransferPageConfig transferPageConfig = transferPageConfigs.get(i);
			TransferUsersPage transferUsersPage = new TransferUsersPage(
					transferPageConfig.getsTitle(),
					transferPageConfig.getsDescription(),
					transferPageConfig.getNavigatorid(),
					transferPageConfig.getMaster(),i);
			addPage(transferUsersPage);
		}
	}

	public static TransferUsersWizard open(Project po) {
		Organization org = po.getFunctionOrganization();
		List<TransferPageConfig> transferPageConfigs = new ArrayList<TransferPageConfig>();
		transferPageConfigs.add(new TransferPageConfig("项目组", "选择项目组中需要移交的用户",
				"project.team", po));
		transferPageConfigs.add(new TransferPageConfig("移交用户", "选择移交的用户",
				"organization.alluser", org));
		transferPageConfigs.add(new TransferPageConfig("移交工作", "选择移交的工作",
				"project.transfer.wbs", po));
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			TransferUsersWizard tuw = new TransferUsersWizard(
					transferPageConfigs);
			Shell shell = window.getShell();
			WizardDialog wizardDialog = new WizardDialog(shell, tuw);
			wizardDialog.open();
			return tuw;
		}
		return null;

	}

	@Override
	public boolean performFinish() {
		// TODO 修改选择的相应信息
		return true;
	}

	public void setUserId(String userid) {
		this.userId = userid;
		
		TransferUsersPage page = (TransferUsersPage) getPage("移交工作");
		page.doRefresh();
	}

	public Object getUserId() {
		return userId;
	}

}
