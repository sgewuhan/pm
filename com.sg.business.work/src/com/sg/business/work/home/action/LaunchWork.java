package com.sg.business.work.home.action;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.sg.business.model.IEditorInputFactory;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.MessageBox;
import com.sg.widgets.part.editor.fields.IValidable;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;
import com.sg.widgets.part.view.SideBarNavigator;

public class LaunchWork extends AbstractWorkDetailPageAction {

	@Override
	protected void run(Work work, Control control) {
		IValidable val = getValidable();
		IContext context = getContext();
		boolean b = val.checkValidOnSave();
		if (b) {
			try {
				work.doSave(context);
			} catch (Exception e) {
				MessageUtil.showToast(e);
				return;
			}
		} else {
			MessageUtil.showToast("没有通过合法性检查。", SWT.ICON_ERROR);
			return;
		}
		if (Boolean.TRUE.equals(work.getValue(Work.F_STARTIMMEDIATELY))) {
			IProcessControl ipc = work.getAdapter(IProcessControl.class);
			List<String[]> result = ipc.checkProcessRunnable(Work.F_WF_EXECUTE);
			boolean hasError = false;
			boolean hasWarning = false;
			StringBuffer message = new StringBuffer();

			for (int i = 0; i < result.size(); i++) {
				String[] res = result.get(i);
				if (res[0].equals("error")) { //$NON-NLS-1$
					hasError = true;
					message.append(res[1] + "\n");
				} else if (res[0].equals("info")) { //$NON-NLS-1$

				} else if (res[0].equals("warning")) { //$NON-NLS-1$
					hasWarning = true;
					message.append(res[1] + "\n");
				}
			}
			if (hasError) {
				MessageUtil.showToast("工作已经保存，但存在一些问题，无法立刻启动。" + ":\n"
						+ message.toString(), SWT.ICON_ERROR);
				sidebarSelected("1", work);
				return;
			}

			if (hasWarning) {
				MessageBox mb = MessageUtil.createMessageBox(null, "启动工作",
						"存在一些问题，您是否需要立刻启动" + ":\n" + message.toString(),
						SWT.YES | SWT.NO);
				int ret = mb.open();
				if (ret != SWT.YES) {
					sidebarSelected("1", work);
					return;
				}
			}

			try {
				work.doStart(context);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

		sidebarSelected("1", work);
	}

	private void sidebarSelected(String id, Work work) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		SideBarNavigator view = (SideBarNavigator) page
				.findView("homenavigator");
		if (view != null) {
			view.doRefresh();
		}
		view.setSelection(id, work);
		
		PrimaryObjectDetailFormView view2 = (PrimaryObjectDetailFormView) page.findView("pm2.work.detail");
		IEditorInputFactory inputFactory = work.getAdapter(IEditorInputFactory.class);
		view2.setInput(inputFactory.getInput(null));
		view2.loadMaster();
	}
}
