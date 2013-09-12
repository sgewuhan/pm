package com.sg.business.commons.page;

import com.sg.business.model.Work;

public class WorkExecuteProcessPage extends AbstractWorkProcessPage {

	@Override
	protected String getWorkflowKey() {
		return Work.F_WF_EXECUTE;
	}

}
