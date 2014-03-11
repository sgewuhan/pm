package com.sg.sales.model.input;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.model.EditorInputFactory;

public class OpportunityEditorInputFactory extends EditorInputFactory {


	public OpportunityEditorInputFactory(PrimaryObject primaryObject) {
		super(primaryObject);
	}
	

	@Override
	protected String getEditorId(PrimaryObject primaryObject,Object data) {
		return "sales.opportunity.editor";
	}
	

}
