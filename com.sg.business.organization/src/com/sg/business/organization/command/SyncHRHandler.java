package com.sg.business.organization.command;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class SyncHRHandler extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		final SyncHR syncHR = new SyncHR();
		syncHR.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				shell.getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						doSyncDone(syncHR, shell, vc);
					}
				});
			}
		});

		syncHR.schedule();
	}

	protected void doSyncDone(SyncHR syncHR, Shell shell, ViewerControl vc) {
		// 变量，存放PM系统比HR系统少的组织，将会作为PM系统需要插入的组织使用
		Set<OrgExchange> insertSet = syncHR.getInsertSet();
		// 变量，存放PM系统比HR系统多的组织，将会作为PM系统需要删除的组织使用
		Set<OrgExchange> removeSet = syncHR.getRemoveSet();
		// 变量，存放PM系统和HR系统名称不一致的组织，将会作为PM系统需要修改全称的组织使用
		Set<OrgExchange> renameSet = syncHR.getRenameSet();

		SyncHROrganizationDialog dialog = new SyncHROrganizationDialog(shell);
		dialog.setInputForNewOrganization(insertSet);
		dialog.setInputForRemoveOrganization(removeSet);
		dialog.setInputForRenameOrganization(renameSet);
		int ret = dialog.open();
		if (ret == IDialogConstants.OK_ID) {
			// 插入PM系统比HR系统少的组织
			for (OrgExchange orgExchange : insertSet) {
				orgExchange.doAddAllHR();
			}
			// 修改PM系统和HR系统名称不一致的组织
			for (OrgExchange orgExchange : renameSet) {
				orgExchange.doRenameHR();
			}

			try {
				syncHR.doSyscHRUser();
			} catch (Exception e) {
				e.printStackTrace();
			}

			vc.refreshViewer();
		}

	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

}
