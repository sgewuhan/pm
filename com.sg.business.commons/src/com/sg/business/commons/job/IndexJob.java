package com.sg.business.commons.job;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.index.Index;

public class IndexJob implements ISchedualJobRunnable {

	public IndexJob() {
	}

	@Override
	public boolean run() throws Exception {
		Index.extractSummaryAndKeyWord(IModelConstants.DB,
				IModelConstants.C_DOCUMENT, Document.F_VAULT, Document.F_TAG,
				Document.F_CONTENT);
		return true;
	}

}
