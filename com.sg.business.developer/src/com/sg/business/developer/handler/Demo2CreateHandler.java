package com.sg.business.developer.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.developer.model.Demo1;
import com.sg.business.developer.model.Demo2;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class Demo2CreateHandler extends ChildPrimaryObjectCreator {

	@Override
	protected void setParentData(PrimaryObject po) {
		Demo1 parentOrg = (Demo1) po.getParentPrimaryObject();
		parentOrg.makeDemo2((Demo2) po);		
	}

	@Override
	protected String getMessageForEmptySelection() {
		return  "您需要选择Tree后进行创建";
	}

}
