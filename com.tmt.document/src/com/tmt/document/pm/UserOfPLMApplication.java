package com.tmt.document.pm;

import com.sg.business.commons.dataset.AbstractTableDataSetFactory;

public class UserOfPLMApplication extends AbstractTableDataSetFactory {

	@Override
	protected String getDataEditorFieldName() {
		return "user";
	}

}
