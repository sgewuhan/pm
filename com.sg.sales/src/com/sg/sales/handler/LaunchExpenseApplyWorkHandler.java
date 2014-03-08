package com.sg.sales.handler;

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
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.handler.LifeCycleActionStart;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.nls.Messages;
import com.sg.sales.model.WorkCost;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MessageBox;
import com.sg.widgets.viewer.ViewerControl;

/**
 * 创建报销申请的工作
 * 
 * @author Administrator
 * 
 */
public class LaunchExpenseApplyWorkHandler extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {

		CurrentAccountContext context = new CurrentAccountContext();
		WorkDefinition workd = getWorkDefinifion(context);
		if (workd == null) {
			MessageUtil.showToast("您所在的组织及上级组织没有可用的费用报销工作定义",
					SWT.ICON_INFORMATION);
			return;
		}
		Object[] workCosts = selection.toArray();

		try {
			Work work = WorkCost.createExpenseApplyWork(workCosts, workd,
					context);
			if (work != null) {
				Shell shell = part.getSite().getShell();
				MessageBox d = MessageUtil
						.createMessageBox(
								shell,
								"创建费用报销工作",
								"已为您选择的费用项创建报销工作，您是否立刻提交？\n选择YES,立刻提交费用报销申请。\n选择NO,您可在我的工作中查看该费用报销申请，并提交。",
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				int code = d.open();
				if (code == SWT.YES) {
					LifeCycleActionStart start = new LifeCycleActionStart();
					try {
						start.execute(work,context,Messages.get(shell.getDisplay()).StartWork,work,null);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
				// DataObjectEditor.open(work, "work.plan.sales.applyexpense",
				// true, null);
			} else {
				MessageUtil.showToast("创建费用报销工作失败", SWT.ICON_ERROR);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		vc.doReloadData(true);
	}

	private WorkDefinition getWorkDefinifion(CurrentAccountContext context) {
		String userId = context.getUserId();
		User user = UserToolkit.getUserById(userId);
		DBCollection workdCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK_DEFINITION);

		List<ObjectId> orgidList = new ArrayList<ObjectId>();
		Organization org = user.getOrganization();
		while (org != null) {
			ObjectId id = org.get_id();
			orgidList.add(id);
			org = (Organization) org.getParentOrganization();
		}
		DBObject data = workdCol.findOne(new BasicDBObject()
				.append(WorkDefinition.F_ORGANIZATION_ID,
						new BasicDBObject().append("$in", orgidList))
				.append(WorkDefinition.F_ACTIVATED, Boolean.TRUE)
				.append(WorkDefinition.F_WORK_TYPE,
						WorkDefinition.WORK_TYPE_STANDLONE)
				.append(WorkDefinition.F_INTERNAL_ID, "expense_apply"));

		if (data != null) {
			return ModelService.createModelObject(data, WorkDefinition.class);
		}

		return null;
	}

}
