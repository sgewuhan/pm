package com.sg.business.project.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MessageBox;
import com.sg.widgets.part.ObjectInformationView;

public class ChangeUserWizard extends Wizard implements IWorkbenchWizard {

	private String changedUserId;
	private String changeUserId;
	private Project po;
	private Organization org;
	private List<PrimaryObject> changeWork = new ArrayList<PrimaryObject>();

	public ChangeUserWizard(Project po) {
		this.po = po;
		org = po.getFunctionOrganization();
	}

	public ChangeUserWizard() {

	}

	@Override
	public void addPages() {
		ChangeUserOfParticipatePage changeUserOfParticipatePage = new ChangeUserOfParticipatePage(
				"team", "请选择需要移交工作的用户", "", "project.team", (PrimaryObject) po);
		addPage(changeUserOfParticipatePage);

		ChangeUserOfOrgUserPage changeUserOfOrgUserPage = new ChangeUserOfOrgUserPage(
				"alluser", "请选择工作将移交给谁", "", "organization.alluser",
				(PrimaryObject) org);
		addPage(changeUserOfOrgUserPage);

		ChangeUserOfWBSPage changeUserOfWBSPage = new ChangeUserOfWBSPage(
				"wbs", "请选择要移交的那些工作", "", "project.changeuser.wbs",
				(PrimaryObject) po);
		addPage(changeUserOfWBSPage);
	}

	public static ChangeUserWizard open(Project po, ExecutionEvent event) {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			ChangeUserWizard tuw = new ChangeUserWizard(po);
			Shell shell = window.getShell();
			WizardDialog wizardDialog = new WizardDialog(shell, tuw) {
				@Override
				protected Point getInitialSize() {
					// TODO Auto-generated method stub
					Point size = super.getInitialSize();

					return new Point(600, size.y);
				}
			};

			wizardDialog.open();
			// Point size = wizardDialog.getShell().getSize();
			// wizardDialog.getShell().setSize(400, size.y);
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
				if (hasError(message)) {
					WizardPage page = (WizardPage) getPage("wbs");
					MessageUtil.showToast(null, "更改项目成员",
							"检查发现了一些错误，请查看检查结果，完成修改后重新执行。", SWT.ICON_ERROR);
					String newMessage = "";
					for (int i = 0; i < message.size(); i++) {
						if (message.get(i) != null && message.get(i).length > 2) {

							Object icon = message.get(i)[2];
							if (icon instanceof Integer
									&& ((Integer) icon).intValue() == SWT.ICON_ERROR) {
								String sMessage = (String) message.get(i)[0];
								PrimaryObject primaryObject = (PrimaryObject) message
										.get(i)[1];
								if (newMessage == "") {
									newMessage = primaryObject.getDesc() + ":"
											+ sMessage;
								} else {
									newMessage = newMessage + "\n"
											+ primaryObject.getDesc() + ":"
											+ sMessage;
								}
							}
						}
					}

					page.setErrorMessage(newMessage);
					return false;
				} else {
					if (message != null && message.size() > 0) {
						MessageBox mb = MessageUtil.createMessageBox(null,
								"更改项目成员", "检查发现了一些问题，请查看检查结果。" + "\n\n"
										+ "选择 \"继续\" 忽视警告信息继续操作" + "\n"
										+ "选择 \"中止\" 停止执行本次操作" + "\n"
										+ "选择 \"查看\" 取消本次操作并查看检查结果",
								SWT.ICON_WARNING | SWT.YES | SWT.NO
										| SWT.CANCEL);
						mb.setButtonText(SWT.YES, "继续");
						mb.setButtonText(SWT.NO, "中止");
						mb.setButtonText(SWT.CANCEL, "查看");
						int result = mb.open();
						if (result == SWT.CANCEL) {
							showCheckMessages(message, po);
						} else if (result == SWT.YES) {
							po.doChangeUsers(changedUserId, changeUserId,
									changeWork, context);
						} else if (result == SWT.NO) {
							ChangeUserOfWBSPage page = (ChangeUserOfWBSPage) getPage("wbs");
							String newMessage = "";
							for (int i = 0; i < message.size(); i++) {
								if (message.get(i) != null
										&& message.get(i).length > 2) {

									String sMessage = (String) message.get(i)[0];
									PrimaryObject primaryObject = (PrimaryObject) message
											.get(i)[1];
									if (newMessage == "") {
										newMessage = primaryObject.getDesc()
												+ ":" + sMessage;
									} else {
										newMessage = newMessage + "\n"
												+ primaryObject.getDesc() + ":"
												+ sMessage;
									}
								}
							}

							page.setErrorMessage(null);
							page.setMessage(newMessage, SWT.ICON_WARNING);
							return false;
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
			view.setInput(message, "检查" + selected);
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

	public String getChangedUserId() {
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

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		boolean expression = selection != null && !selection.isEmpty()
				&& selection.getFirstElement() instanceof Project;
		if(!expression){
			String message = "您需要选择项目后启动向导";
			MessageUtil.showToast(message, SWT.ICON_ERROR);
			Assert.isLegal(expression, message);
		}else{
			po = (Project) selection.getFirstElement();
			org = po.getFunctionOrganization();
		}

	}
}
