package com.sg.business.organization.view;

import java.util.List;

import org.eclipse.swt.dnd.DropTargetEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.viewer.ViewerControl;


public class DropMember  extends DropPrimaryObjectTarget{
	
	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {
		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		Organization org = (Organization) targetViewerControl.getMaster();
		if(org==null){
			return;
		}
		org.doAddMembers(dragsItems);
		// 刷新目标表
		targetViewerControl.doReloadData();
		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

}
