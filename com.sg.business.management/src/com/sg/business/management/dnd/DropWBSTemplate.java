package com.sg.business.management.dnd;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.CTreeViewer;
import com.sg.widgets.viewer.ViewerControl;

public class DropWBSTemplate extends DropPrimaryObjectTarget {

	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {
		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		if (event.item == null) {
			return;
		}
		Object targetPo = event.item.getData();
		if (targetPo instanceof WorkDefinition) {
			WorkDefinition workDefinition = (WorkDefinition) targetPo;
			doCreateDeliverableDefinition(workDefinition, dragsItems,
					targetViewerControl);
		}
		// targetViewerControl.getViewer().refresh(role);
		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

	private void doCreateDeliverableDefinition(WorkDefinition targetWorkDefinition,
			List<PrimaryObject> dragsItems, ViewerControl targetViewerControl) {
		for (PrimaryObject po : dragsItems) {
			if (po instanceof WorkDefinition) {
				WorkDefinition srcWorkDefinition = (WorkDefinition) po;
				try {
					targetWorkDefinition.doImportGenericWorkDefinition(srcWorkDefinition,new CurrentAccountContext());
				} catch (Exception e) {
					MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
					return;
				}
				CTreeViewer viewer = (CTreeViewer) targetViewerControl.getViewer();
				viewer.refresh(targetWorkDefinition);
				viewer.expandToLevel(targetWorkDefinition, CTreeViewer.ALL_LEVELS);

			}
		}
	}
}
