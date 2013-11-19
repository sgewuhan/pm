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
		MessageUtil.showToast("您需要选择一项执行删除", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();

		int size = selection.size();
		int yes = MessageUtil.showMessage(shell, "删除" + selected.getDesc()
				+ (size > 1 ? " 等工作。" : ""), "您确定要删除 " + selected.getDesc()
				+ (size > 1 ? " 等工作" : " 工作") + "吗？\n该操作将不可恢复，选择YES确认删除。",
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
			MessageUtil.showToast(shell, "删除" + selected.getTypeName(), "顶级"
					+ selected.getTypeName() + "不可删除", SWT.ICON_WARNING);
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
			MessageUtil.showMessage(shell, "删除" + selected.getTypeName(),
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
