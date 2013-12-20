package com.sg.business.organization.command;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;
import com.sg.business.organization.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class Consign extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final User user = (User) selected;
		// 显示用户选择器
		NavigatorSelector ns = new NavigatorSelector("organization.user") { //$NON-NLS-1$
			@Override
			protected void doOK(IStructuredSelection is) {
				if (Utils.isNullOrEmpty(is)) {
					return;
				}
				User consigner = (User) is.getFirstElement();
				try {
					user.doConsignTo(consigner, new CurrentAccountContext());
					MessageUtil.showToast(Messages.get().Consign_1 + user + Messages.get().Consign_2 + consigner
							+ ", " + user + Messages.get().Consign_4, //$NON-NLS-1$
							SWT.ICON_INFORMATION);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				super.doOK(is);
			}
		};
		ns.show();
	}


}
