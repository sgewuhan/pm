package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class AssignProjectRole extends AbstractNavigatorHandler {

	private static final String TITLE = "ָ�ɳ�Ա";

	@Override
	protected void execute(PrimaryObject selected, final IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		if (!(selected instanceof ProjectRole)) {
			MessageUtil
					.showToast(shell, TITLE, "ֻ�ܶ���Ŀ��ɫָ�ɳ�Ա", SWT.ICON_WARNING);
			return;
		}

		final ProjectRole rd = ((ProjectRole) selected);
		if (rd.isOrganizatioRole()) {
			MessageUtil
					.showToast(shell, TITLE, "ֻ�ܶ���Ŀ��ɫָ�ɳ�Ա", SWT.ICON_WARNING);
			return;
		}

		final Project project = rd.getProject();

		Organization org = project.getFunctionOrganization();

		// ��ʾ�û�ѡ����
		// ��ѡ����Ŀ����ְ�ܲ��ŵļ��¼����ŵ����г�Ա
		NavigatorSelector ns = new NavigatorSelector("organization.user.selector") {//organization.alluser
			@SuppressWarnings("unchecked")
			@Override
			protected void doOK(IStructuredSelection is) {
				try {
					rd.doAssignUsers(is.toList(), new CurrentAccountContext());
					vc.getViewer().refresh(rd);
					vc.expandItem(rd);

					// 4. ��������Ϣ���ݵ��༭��
						sendNavigatorActionEvent( part,
								INavigatorActionListener.CREATE, new Integer(
										INavigatorActionListener.REFRESH));
				} catch (Exception e) {
					MessageUtil.showToast(TITLE, e);
				}
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return super.isSelectEnabled(is)
						&& (is.getFirstElement() instanceof User);
			}

		};
		ns.setMaster(org);
		ns.show();

	}


}
