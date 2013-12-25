package com.sg.business.visualization.view;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.visualization.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.viewer.ViewerControl;

public class DropProjectToOwnerSet extends DropPrimaryObjectTarget {

	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {
		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		if (event.item == null) {
			return;
		}
		Object object = event.item.getData();
		if (object instanceof UserProjectPerf) {
			UserProjectPerf userProjectPerf = (UserProjectPerf) object;
			addProjectSet(userProjectPerf, dragsItems);
			targetViewerControl.getViewer().refresh();
		} else {
			MessageUtil.showToast(Messages.get().DropProjectToOwnerSet_0, SWT.ICON_WARNING);
		}

		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

	private void addProjectSet(UserProjectPerf userProjectPerf,
			List<PrimaryObject> dragsItems) {

		ObjectId[] projectIds = new ObjectId[dragsItems.size()];
		int i = 0;
		for (PrimaryObject po : dragsItems) {
			projectIds[i++] = po.get_id();
		}
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERPROJECTPERF);
		DBObject update = new BasicDBObject();
		update.put("$addToSet", new BasicDBObject().append( //$NON-NLS-1$
				UserProjectPerf.F_PROJECT_ID,
				new BasicDBObject().append("$each", projectIds))); //$NON-NLS-1$
		col.update(new BasicDBObject().append(UserProjectPerf.F__ID,
				userProjectPerf.get_id()), update, true, false);
	}

}
