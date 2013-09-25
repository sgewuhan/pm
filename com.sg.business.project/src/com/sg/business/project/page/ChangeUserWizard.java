package com.sg.business.project.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.widgets.part.CurrentAccountContext;

public class ChangeUserWizard extends Wizard {
	
	private String changedUserId;
	private String changeUserId;
	private Project po;
	private Organization org;
	private List<PrimaryObject > changeWork;

	public ChangeUserWizard(Project po, Organization org) {
		this.po = po;
		this.org = org;
		this.setChangeWork(new ArrayList<PrimaryObject>());
	}

	@Override
	public void addPages() {
		ChangeUserOfTeamPage changeUserOfTeamPage = new ChangeUserOfTeamPage(
				"team", "项目组", "选择项目组中需要移交的用户", "project.team",
				(PrimaryObject) po);
		addPage(changeUserOfTeamPage);

		ChangeUserOfOrgUserPage changeUserOfOrgUserPage = new ChangeUserOfOrgUserPage(
				"alluser", "移交用户", "选择移交的用户", "organization.alluser",
				(PrimaryObject) org);
		addPage(changeUserOfOrgUserPage);

		ChangeUserOfWBSPage changeUserOfWBSPage = new ChangeUserOfWBSPage(
				"wbs", "移交工作", "选择移交的工作", "project.changeuser.wbs",
				(PrimaryObject) po);
		addPage(changeUserOfWBSPage);
	}

	public static ChangeUserWizard open(Project po) {
		Organization org = po.getFunctionOrganization();
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			ChangeUserWizard tuw = new ChangeUserWizard(po, org);
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
		try {
			po.doChangeUsers(changedUserId,changeUserId,changeWork,new CurrentAccountContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	

	@Override
	public boolean canFinish() {
		if(changedUserId != null && changeUserId != null && changeWork.size() != 0){
			return true;
		} else {
			return false;
		}
	}
	
	public void setChangedUserId(String changedUserId) {
		this.changedUserId = changedUserId;

		ChangeUserOfWBSPage page = (ChangeUserOfWBSPage) getPage("wbs");
		page.doRefresh();
	}

	public Object getChangedUserId() {
		return changedUserId;
	}

	public void setChangeUserId(String changeUserId) {
		this.changeUserId = changeUserId;
	}

	public String getChangeUserId() {
		return changeUserId;
	}

	public List<PrimaryObject> getChangeWork() {
		return changeWork;
	}

	public void setChangeWork(List<PrimaryObject> changeWork) {
		this.changeWork = changeWork;
	}
}
