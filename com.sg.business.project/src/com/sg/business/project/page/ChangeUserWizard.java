package com.sg.business.project.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MessageBox;
import com.sg.widgets.part.ObjectInformationView;

public class ChangeUserWizard extends Wizard {

	private String changedUserId;
	private String changeUserId;
	private Project po;
	private Organization org;
	private List<PrimaryObject> changeWork;
	private ExecutionEvent event;

	public ChangeUserWizard(Project po, Organization org, ExecutionEvent event) {
		this.po = po;
		this.org = org;
		this.setChangeWork(new ArrayList<PrimaryObject>());
		this.event = event;
	}

	@Override
	public void addPages() {
		ChangeUserOfTeamPage changeUserOfTeamPage = new ChangeUserOfTeamPage(
				"team", "��Ŀ��", "ѡ����Ŀ������Ҫ�ƽ����û�", "project.team",
				(PrimaryObject) po);
		addPage(changeUserOfTeamPage);

		ChangeUserOfOrgUserPage changeUserOfOrgUserPage = new ChangeUserOfOrgUserPage(
				"alluser", "�ƽ��û�", "ѡ���ƽ����û�", "organization.alluser",
				(PrimaryObject) org);
		addPage(changeUserOfOrgUserPage);

		ChangeUserOfWBSPage changeUserOfWBSPage = new ChangeUserOfWBSPage(
				"wbs", "�ƽ�����", "ѡ���ƽ��Ĺ���", "project.changeuser.wbs",
				(PrimaryObject) po);
		addPage(changeUserOfWBSPage);
	}

	public static ChangeUserWizard open(Project po, ExecutionEvent event) {
		Organization org = po.getFunctionOrganization();
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			ChangeUserWizard tuw = new ChangeUserWizard(po, org, event);
			Shell shell = window.getShell();
			WizardDialog wizardDialog = new WizardDialog(shell, tuw);
			wizardDialog.open();
			return tuw;
		}
		return null;

	}

	@Override
	public boolean performFinish() {
		try {
			CurrentAccountContext context = new CurrentAccountContext();
			try {
				List<Object[]> message = po.checkChangeUser(changedUserId,
						changeUserId, changeWork);
				String name = event.getCommand().getName();
				if (hasError(message)) {
					MessageUtil.showToast(null, name,
							"��鷢����һЩ������鿴�����������޸ĺ�����ִ�С�", SWT.ICON_ERROR);
					showCheckMessages(message, po);
				} else {
					if (message != null && message.size() > 0) {
						MessageBox mb = MessageUtil.createMessageBox(null,
								name, "��鷢����һЩ���⣬��鿴�������" + "\n\n"
										+ "ѡ�� \"����\" ���Ӿ�����Ϣ��������" + "\n"
										+ "ѡ�� \"��ֹ\" ִֹͣ�б��β���" + "\n"
										+ "ѡ�� \"�鿴\" ȡ�����β������鿴�����",
								SWT.ICON_WARNING | SWT.YES | SWT.NO
										| SWT.CANCEL);
						mb.setButtonText(SWT.YES, "����");
						mb.setButtonText(SWT.NO, "��ֹ");
						mb.setButtonText(SWT.CANCEL, "�鿴");
						int result = mb.open();
						if (result == SWT.CANCEL) {
							showCheckMessages(message, po);
						} else if (result == SWT.YES) {
							po.doChangeUsers(changedUserId, changeUserId,
									changeWork, context);
						}
					}
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean hasError(List<Object[]> message) {
		if (message == null || message.size() == 0) {
			return false;
		}
		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) != null && message.get(i).length > 2) {
				Object icon = message.get(i)[2];
				if (icon instanceof Integer
						&& ((Integer) icon).intValue() == SWT.ICON_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	protected void showCheckMessages(List<Object[]> message,
			PrimaryObject selected) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			ObjectInformationView view = (ObjectInformationView) page.showView(
					"com.sg.widgets.objectinfo", "" + selected.hashCode() + "_"
							+ getClass().getName(),
					IWorkbenchPage.VIEW_ACTIVATE);
			view.setInput(message, "���" + selected);
		} catch (PartInitException e) {
			MessageUtil.showToast(e);
		}

	}

	@Override
	public boolean canFinish() {
		if (changedUserId != null && changeUserId != null
				&& changeWork.size() != 0) {
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
