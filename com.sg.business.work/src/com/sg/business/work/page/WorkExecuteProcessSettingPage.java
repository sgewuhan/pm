package com.sg.business.work.page;

import com.sg.business.model.Work;

public class WorkExecuteProcessSettingPage extends AbstractWorkProcessSettingPage {

	@Override
	protected String getProcessKey() {
		return Work.F_WF_EXECUTE;
	}


}
