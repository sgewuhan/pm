package com.sg.business.developer.handler;

import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.developer.model.hy;
import com.sg.business.developer.model.hy1;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;
import com.sg.widgets.viewer.ICreateEditorDelegator;
import com.sg.widgets.viewer.ViewerControl;

public class hy1CreateHandler extends ChildPrimaryObjectCreator  {

	public hy1CreateHandler() {
		// TODO Auto-generated constructor stub
	}

	
	protected void setParentData(PrimaryObject po) {
		// TODO Auto-generated method stub
		hy parentOrg = (hy) po.getParentPrimaryObject();
		parentOrg.makeDemo2((hy1) po);		

	}

	@Override
	protected String getMessageForEmptySelection() {
		// TODO Auto-generated method stub
		return "您需要选择Tree后进行创建";
	}
	protected boolean needHostPartListenSaveEvent() {
		return false;
	}

}
