package com.sg.business.project.wizards;

import java.util.ArrayList;
import java.util.List;

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
import com.sg.business.resource.nls.Messages;
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
				"team", Messages.get().ChangeUserWizard_1, "", "project.team", (PrimaryObject) po); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
		addPage(changeUserOfParticipatePage);

		ChangeUserOfOrgUserPage changeUserOfOrgUserPage = new ChangeUserOfOrgUserPage(
				"alluser", Messages.get().ChangeUserWizard_5, "", "organization.alluser", //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
				(PrimaryObject) org);
		addPage(changeUserOfOrgUserPage);

		ChangeUserOfWBSPage changeUserOfWBSPage = new ChangeUserOfWBSPage(
				"wbs", Messages.get().ChangeUserWizard_9, "", "project.changeuser.wbs", //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
				(PrimaryObject) po);
		addPage(changeUserOfWBSPage);
	}

	public static ChangeUserWizard open(Project po) {
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
					WizardPage page = (WizardPage) getPage("wbs"); //$NON-NLS-1$
					MessageUtil.showToast(null, Messages.get().ChangeUserWizard_13,
							Messages.get().ChangeUserWizard_14, SWT.ICON_ERROR);
					String newMessage = ""; //$NON-NLS-1$
					for (int i = 0; i < message.size(); i++) {
						if (message.get(i) != null && message.get(i).length > 2) {

							Object icon = message.get(i)[2];
							if (icon instanceof Integer
									&& ((Integer) icon).intValue() == SWT.ICON_ERROR) {
								String sMessage = (String) message.get(i)[0];
								PrimaryObject primaryObject = (PrimaryObject) message
										.get(i)[1];
								if (newMessage == "") { //$NON-NLS-1$
									newMessage = primaryObject.getDesc() + ":" //$NON-NLS-1$
											+ sMessage;
								} else {
									newMessage = newMessage + "\n" //$NON-NLS-1$
											+ primaryObject.getDesc() + ":" //$NON-NLS-1$
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
								Messages.get().ChangeUserWizard_20, Messages.get().ChangeUserWizard_21 + "\n\n" //$NON-NLS-3$
										+ Messages.get().ChangeUserWizard_23 + "\n" //$NON-NLS-2$
										+ Messages.get().ChangeUserWizard_25 + "\n" //$NON-NLS-2$
										+ Messages.get().ChangeUserWizard_27,
								SWT.ICON_WARNING | SWT.YES | SWT.NO
										| SWT.CANCEL);
						mb.setButtonText(SWT.YES, Messages.get().ChangeUserWizard_28);
						mb.setButtonText(SWT.NO, Messages.get().ChangeUserWizard_29);
						mb.setButtonText(SWT.CANCEL, Messages.get().ChangeUserWizard_30);
						int result = mb.open();
						if (result == SWT.CANCEL) {
							showCheckMessages(message, po);
						} else if (result == SWT.YES) {
							po.doChangeUsers(changedUserId, changeUserId,
									changeWork, context);
						} else if (result == SWT.NO) {
							ChangeUserOfWBSPage page = (ChangeUserOfWBSPage) getPage("wbs"); //$NON-NLS-1$
							String newMessage = ""; //$NON-NLS-1$
							for (int i = 0; i < message.size(); i++) {
								if (message.get(i) != null
										&& message.get(i).length > 2) {

									String sMessage = (String) message.get(i)[0];
									PrimaryObject primaryObject = (PrimaryObject) message
											.get(i)[1];
									if (newMessage == "") { //$NON-NLS-1$
										newMessage = primaryObject.getDesc()
												+ ":" + sMessage; //$NON-NLS-1$
									} else {
										newMessage = newMessage + "\n" //$NON-NLS-1$
												+ primaryObject.getDesc() + ":" //$NON-NLS-1$
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
					"com.sg.widgets.objectinfo", "" + selected.hashCode() + "_" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ getClass().getName(),
					IWorkbenchPage.VIEW_ACTIVATE);
			view.setInput(message, Messages.get().ChangeUserWizard_40 + selected);
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

		ChangeUserOfWBSPage page = (ChangeUserOfWBSPage) getPage("wbs"); //$NON-NLS-1$
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
			String message = Messages.get().ChangeUserWizard_42;
			MessageUtil.showToast(message, SWT.ICON_ERROR);
			Assert.isLegal(expression, message);
		}else{
			po = (Project) selection.getFirstElement();
			org = po.getFunctionOrganization();
		}

	}
}
