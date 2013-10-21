package com.sg.business.developer.handler;

import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.developer.model.hy;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class hyCreateHandler extends  ChildPrimaryObjectCreator {

	public hyCreateHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	

	protected void setParentData(PrimaryObject po) {
		// TODO Auto-generated method stub
		hy parentOrg = (hy) po.getParentPrimaryObject();
		parentOrg.makeChildDemo1((hy) po);
	}

	@Override
	protected String getMessageForEmptySelection() {
		// TODO Auto-generated method stub
		return "您需要选择上级后进行创建";
	}

}
