package com.sg.sales.model.input;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.model.EditorInputFactory;

public class CompanyEditorInputFactory extends EditorInputFactory {


	public CompanyEditorInputFactory(PrimaryObject primaryObject) {
		super(primaryObject);
	}
	

	@Override
	protected String getEditorId(PrimaryObject primaryObject,Object data) {
		return "sales.company.editor";
	}
	

}
