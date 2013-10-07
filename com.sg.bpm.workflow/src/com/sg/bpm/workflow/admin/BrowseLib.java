package com.sg.bpm.workflow.admin;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.eclipse.swt.SWT;

import com.mobnut.admin.IFunctionExcutable;
import com.mobnut.admin.dataset.Setting;
import com.sg.widgets.MessageUtil;

public class BrowseLib implements IFunctionExcutable {

	public BrowseLib() {
	}

	@Override
	public void run() {
		// 获得流程库的地址
		Object url = Setting.getSystemSetting("SYSTEM.PROCESS_BASE_URL");
		if (url instanceof String) {
			UrlLauncher launcher = RWT.getClient()
					.getService(UrlLauncher.class);
			launcher.openURL((String) url);
		} else {
			MessageUtil.showToast(
					"没有设定流程库的地址，请在全局设置中设定参数:\nSYSTEM.PROCESS_BASE_URL",
					SWT.ICON_ERROR);
		}
	}

}
