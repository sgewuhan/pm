package com.sg.business.commons.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveWork extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().RemoveWork_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();

		int size = selection.size();
		int yes = MessageUtil.showMessage(shell, Messages.get().RemoveWork_1 + selected.getDesc()
				+ (size > 1 ? Messages.get().RemoveWork_2 : ""), Messages.get().RemoveWork_4 + selected.getDesc() //$NON-NLS-2$ //$NON-NLS-1$
				+ (size > 1 ? Messages.get().RemoveWork_5 : Messages.get().RemoveWork_6) + Messages.get().RemoveWork_7,
				SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		List<AbstractWork> delectWorks = new ArrayList<AbstractWork>();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			AbstractWork work = (AbstractWork) iter.next();
			if (delectWorks.size() > 0) {
				if (hasContains(delectWorks, work)) {
					continue;
				}
				if (hasChildrenContains(delectWorks, work)) {
					continue;
				}
			}
			delectWorks.add(work);
		}
		if (delectWorks.size() > 0) {
			for (AbstractWork work : delectWorks) {
				boolean b = deleteSingleWork(work, part, currentViewerControl,
						shell);
				if (!b) {
					break;
				}
			}
		}
	}

	private boolean hasChildrenContains(List<AbstractWork> delectWorks,
			AbstractWork work) {
		List<PrimaryObject> childrenWorks = work
				.getChildrenPrimaryObjectCache();
		if (childrenWorks != null && childrenWorks.size() > 0) {
			for (PrimaryObject po : childrenWorks) {
				Work childrenWork = (Work) po;
				if (delectWorks.contains(childrenWork)) {
					delectWorks.remove(childrenWork);
					continue;
				}
				if (hasChildrenContains(delectWorks, childrenWork)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasContains(List<AbstractWork> delectWorks,
			AbstractWork work) {
		if (delectWorks != null && delectWorks.size() > 0) {
			AbstractWork parentWork = (AbstractWork) work
					.getParentPrimaryObjectCache();
			if (parentWork != null) {
				if (delectWorks.contains(parentWork)) {
					return true;
				}
				if (hasContains(delectWorks, parentWork)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean deleteSingleWork(PrimaryObject selected,
			IWorkbenchPart part, ViewerControl currentViewerControl, Shell shell) {
		Assert.isNotNull(currentViewerControl);

		if (selected.getParentPrimaryObjectCache() == null) {
			MessageUtil.showToast(shell, Messages.get().RemoveWork_8 + selected.getTypeName(), Messages.get().RemoveWork_9
					+ selected.getTypeName() + Messages.get().RemoveWork_10, SWT.ICON_WARNING);
			return false;
		}

		// 如果是工作，需要刷新开始和完成时间
		List<Work> toUpdate = null;
		if (selected instanceof Work) {
			Work work = (Work) selected;
			toUpdate = work.getAllParents();
		}

		selected.addEventListener(currentViewerControl);
		try {
			//
			selected.doRemove(new CurrentAccountContext());

			// 4. 将更改消息传递到编辑器
			sendNavigatorActionEvent(part, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));

		} catch (Exception e) {
			MessageUtil.showMessage(shell, Messages.get().RemoveWork_11 + selected.getTypeName(),
					e.getMessage(), SWT.ICON_WARNING);
			return false;
		}
		selected.removeEventListener(currentViewerControl);

		if (toUpdate != null) {
			currentViewerControl.getViewer().update(toUpdate.toArray(), null);
		}

		return true;
	}

}
