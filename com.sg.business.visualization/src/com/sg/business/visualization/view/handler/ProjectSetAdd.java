package com.sg.business.visualization.view.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.ComboInputDialog;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetAdd extends AbstractNavigatorHandler {

	private static final String TITLE = "添加项目至项目组合";

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		final Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, "您至少需要选择一个项目", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		String userid = new CurrentAccountContext().getUserId();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERPROJECTPERF);
		List desclist = col.distinct(UserProjectPerf.F_DESC,
				new BasicDBObject().append(UserProjectPerf.F_USERID, userid));
		String projectSetName = getProjectSetName(shell, userid, desclist);

		if(projectSetName == null){
			return;
		}
		
		ObjectId[] projectIds = new ObjectId[selection.size()];

		Iterator<?> iter = selection.iterator();
		int i = 0;
		while (iter.hasNext()) {
			projectIds[i++] = ((Project) iter.next()).get_id();
		}

		DBObject condition = new BasicDBObject();
		condition.put(UserProjectPerf.F_DESC, projectSetName);
		DBObject update = new BasicDBObject();
		update.put("$addToSet", new BasicDBObject().append(
				UserProjectPerf.F_PROJECT_ID,
				new BasicDBObject().append("$each", projectIds)));
		update.put(
				"$set",
				new BasicDBObject()
						.append(UserProjectPerf.F_USERID, userid)
						.append(UserProjectPerf.F_DESC, projectSetName));

		col.update(condition, update,true,false);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getProjectSetName(Shell parentShell, String userid,
			List desclist) {
		String[] items = (String[]) desclist.toArray(new String[0]);
		IInputValidator val = new IInputValidator() {
			
			@Override
			public String isValid(String newText) {
				return Utils.isNullOrEmpty(newText)?"您需要输入项目集合名称":null;
			}
		};
		ComboInputDialog input = new ComboInputDialog(parentShell, TITLE,
				"请选择现有的项目组合名称\n或输入新的项目组合名称", "", items, val);
		int ok = input.open();
		if (ok == ComboInputDialog.OK) {
			return input.getValue();
		} else {
			return null;
		}
	}

}
