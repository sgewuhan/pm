package com.sg.business.work.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.services.IServiceLocator;

import com.mongodb.util.JSON;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

public abstract class AbstractUserTaskSelectMenu extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				IWorkbenchPart part = activePage.getActivePart();
				if (part != null) {
					IStructuredSelection selection = (IStructuredSelection) activePage
							.getSelection(part.getSite().getId());
					if (!selection.isEmpty()
							&& (selection.getFirstElement() instanceof Work)) {
						return getContributionItems(
								(Work) selection.getFirstElement(), part);
					}
				}

			}
		}

		return new IContributionItem[0];
	}

	// private String getPartId() {
	// return "work.processing";
	// }

	private IContributionItem[] getContributionItems(Work work,
			IWorkbenchPart part) {
		String userId = new CurrentAccountContext().getConsignerId();
		List<UserTask> userTasks = getUserTask(work, userId);

		IContributionItem[] result = new IContributionItem[userTasks.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = createContributionItem(userTasks.get(i), part);
		}
		return result;
	}

	protected abstract List<UserTask> getUserTask(Work work, String userId);

	private IContributionItem createContributionItem(UserTask userTask,
			IWorkbenchPart part) {
		IServiceLocator serviceLocator = ((WorkbenchPart) part).getSite();
		Map<String, Object> para = new HashMap<String, Object>();
		try {
			String _userTask = JSON.serialize(userTask.get_data());
			para.put("runtimework.usertask", _userTask);
		} catch (Exception e) {
		}
		para.put("runtimework.usertask_id", userTask.get_id().toString());
		ImageDescriptor icon = BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_24_BLANK);
		String label = userTask.getTaskName();
		String mnemonic = null;
		String tooltip = null;
		int style = CommandContributionItem.STYLE_PUSH;
		String helpContextId = null;
		boolean visiableEnabled = false;
		CommandContributionItemParameter parameter = new CommandContributionItemParameter(
				serviceLocator, getContributionId(), getCommandId(), para, icon, null,
				null, label, mnemonic, tooltip, style, helpContextId,
				visiableEnabled);
		return new CommandContributionItem(parameter);

	}

	protected abstract String getCommandId();

	protected abstract String getContributionId();
}
