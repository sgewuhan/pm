package com.sg.business.commons;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.portal.Portal;
import com.mobnut.portal.page.LoginPage;
import com.mobnut.portal.user.UserSessionContext;

public class Direct implements EntryPoint {

	public Direct() {
	}

	@Override
	public int createUI() {
		String _id = RWT.getRequest().getParameter("id"); //$NON-NLS-1$
		String _class = RWT.getRequest().getParameter("class"); //$NON-NLS-1$
		String _editable = RWT.getRequest().getParameter("editable"); //$NON-NLS-1$
		String _edittype = RWT.getRequest().getParameter("edittype"); //$NON-NLS-1$
		String _editor = RWT.getRequest().getParameter("editor"); //$NON-NLS-1$

		Display display = PlatformUI.createDisplay();
		
		boolean useAd = Portal.getDefault().isAdEnable();
		Object args = RWT.getRequest().getParameter("ad");
		if ("off".equals(args)) {
			useAd = false;
		} else if ("on".equals(args)) {
			useAd = true;
		}
		int returnCode = LoginPage.OPEN(display,useAd);
		if (returnCode == 1) {
			return 0;
		}
		UserSessionContext.getSession().initialize(display);

		// ExitConfirmation confirmation = RWT.getClient().getService(
		// ExitConfirmation.class );
		// confirmation.setMessage( "离开本页面后，您未保存的数据将被丢失。" );

		try {
			Commons.validLicense();
		} catch (Exception e) {
			// MessageDialog.openWarning(null, "Login",
			// e.getMessage()+"\n请与系统管理人员联系。系统进入演示运行状态。");
		}

		WorkbenchAdvisor advisor = new DirectApplicationWorkbenchAdvisor();

		
		if (!Utils.isNullOrEmpty(_id) && !Utils.isNullOrEmpty(_class)
				&& !Utils.isNullOrEmpty(_editable) && !Utils.isNullOrEmpty(_edittype)
				&& !Utils.isNullOrEmpty(_editor)) {
			
			IMemento memento = new DirectOpenMemento(_id,_class,_editable,_edittype,_editor);
			advisor.saveState(memento );
		}
		
		returnCode = PlatformUI.createAndRunWorkbench(display, advisor);


		return returnCode;
	}

}
