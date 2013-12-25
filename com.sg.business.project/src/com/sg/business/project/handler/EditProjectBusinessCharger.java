package com.sg.business.project.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.viewer.ViewerControl;

public class EditProjectBusinessCharger extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, final IStructuredSelection selection) {
		if (selection.isEmpty()) {
			return;
		}
		// 显示用户选择器
		NavigatorSelector ns = new NavigatorSelector(
				"organization.user.selector") { //$NON-NLS-1$
			@Override
			protected void doOK(IStructuredSelection is) {
				if (Utils.isNullOrEmpty(is)) {
					return;
				}
				User consigner = (User) is.getFirstElement();
				try {
					processSetting(selection, consigner, vc);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				super.doOK(is);
			}

		};
		ns.show();
	}

	private void processSetting(IStructuredSelection selection, User consigner,
			ViewerControl vc) throws Exception {
		Iterator<?> iter = selection.iterator();
		ColumnViewer viewer = vc.getViewer();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (o instanceof Project) {
				((Project) o).doModifyValueWithETL(Project.F_BUSINESS_CHARGER,
						consigner.getUserid());
				viewer.update(o, null);
			}
		}
	}
}
