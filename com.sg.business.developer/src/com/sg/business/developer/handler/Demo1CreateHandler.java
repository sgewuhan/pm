package com.sg.business.developer.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.developer.model.Demo1;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class Demo1CreateHandler extends ChildPrimaryObjectCreator{

	@Override
	protected void setParentData(PrimaryObject po) {
		Demo1 parentOrg = (Demo1) po.getParentPrimaryObject();
		parentOrg.makeChildDemo1((Demo1) po);
	}

	@Override
	protected String getMessageForEmptySelection() {
		return  "您需要选择上级后进行创建";
	}

}
