package com.sg.business.visualization.view.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.visualization.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetRemove extends AbstractNavigatorHandler {
	
	
	private static final String TITLE = Messages.get().ProjectSetRemove_0;
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		final Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, Messages.get().ProjectSetRemove_1, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {

		PrimaryObject master = vc.getMaster();
		if (master instanceof UserProjectPerf) {
			UserProjectPerf userProjectPerf = (UserProjectPerf) master;
			DataSet dataset = vc.getDataSet();

			List<ObjectId> projectids = new ArrayList<ObjectId>();
			Object[] array = selection.toArray();
			for (Object obj : array) {
				if (obj instanceof Project) {
					projectids.add(((Project) obj).get_id());
					dataset.remove((Project)obj);
				}
			}

			DBCollection perfcol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_USERPROJECTPERF);
			DBObject update = new BasicDBObject();
			update.put("$pullAll", new BasicDBObject().append( //$NON-NLS-1$
					UserProjectPerf.F_PROJECT_ID, projectids));
			perfcol.update(new BasicDBObject().append(UserProjectPerf.F__ID,
					userProjectPerf.get_id()), update);
			
			

		} else {
			MessageUtil.showToast(Messages.get().ProjectSetRemove_3, SWT.ICON_WARNING);
		}

	}

}
