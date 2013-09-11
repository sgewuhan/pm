package com.sg.business.organization.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;

public class Consign extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final User user = (User) selected;
		// ��ʾ�û�ѡ����
		NavigatorSelector ns = new NavigatorSelector("organization.user") {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (Utils.isNullOrEmpty(is)) {
					return;
				}
				User consigner = (User) is.getFirstElement();
				try {
					user.doConsignTo(consigner, new CurrentAccountContext());
					MessageUtil.showToast("�û���" + user + "�ѱ��йܵ���" + consigner
							+", "+ user + "�ٴε�¼���й��Զ�ȡ��",
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
