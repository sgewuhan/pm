package com.sg.business.management.dnd;

import java.util.List;

import org.eclipse.swt.dnd.DropTargetEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.viewer.ViewerControl;

public class DropOrganization extends DropPrimaryObjectTarget {

	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {
		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		if(event.item == null){
			return;
		}
		Object targetPo = event.item.getData();
		if (targetPo instanceof Organization) {
			Organization org = (Organization) targetPo;
			for (PrimaryObject po : dragsItems) {
				if (po instanceof ProjectTemplate) {
					org.doAddProjectTemplate(po.get_id());
				} else if (po instanceof WorkDefinition) {
					org.doAddWorkDefinition(po.get_id());
				}
			}
		}
		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

}
