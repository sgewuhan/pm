package com.sg.business.commons;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.portal.page.LoginPage;
import com.mobnut.portal.user.UserSessionContext;

public class Direct implements EntryPoint {

	public Direct() {
	}

	@Override
	public int createUI() {
		String _id = RWT.getRequest().getParameter("id");
		String _class = RWT.getRequest().getParameter("class");
		String _editable = RWT.getRequest().getParameter("editable");
		String _edittype = RWT.getRequest().getParameter("edittype");
		String _editor = RWT.getRequest().getParameter("editor");

		Display display = PlatformUI.createDisplay();

		int returnCode = LoginPage.OPEN(display);
		if (returnCode == 1) {
			return 0;
		}
		UserSessionContext.getSession().setDisplay(display);

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
