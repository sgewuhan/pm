package com.sg.business.commons.ui;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobnut.commons.javascript.JavaScriptEvaluator;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MultilineInputDialog;

public class CheckJS extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		String js = "var r = project.getDesc();\n" //$NON-NLS-1$
				+ "r+=user.getUserid();\n" //$NON-NLS-1$
				+ "result.setValue(r);"; //$NON-NLS-1$
		MultilineInputDialog ip = new MultilineInputDialog(shell,
				Messages.get().CheckFlash_0, Messages.get().CheckFlash_1, js,
				null);
		int ok = ip.open();
		if (ok == MultilineInputDialog.OK) {
			evaluate(ip.getValue());
		}

		// JOFCDemo d = new JOFCDemo(shell,SWT.SHELL_TRIM);
		// d.doLoadPie();
		// d.open();
		return null;
	}

	private void evaluate(String js) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		DBObject dbo = col.findOne();
		// 转换处理
		Object projectId = dbo.get(Project.F__ID);
		parameters.put(IRoleParameter.PROJECT_ID, projectId);
		if (projectId instanceof ObjectId) {
			Project project = ModelService.createModelObject(Project.class,
					(ObjectId) projectId);
			parameters.put(IRoleParameter.PROJECT, project);
			parameters.put(IRoleParameter.PROJECT_BUSINESS_ORGANIZATION,
					project.getBusinessOrganization());
			parameters.put(IRoleParameter.PROJECT_CHARGER, project.getCharger());
			parameters.put(IRoleParameter.PROJECT_FUNCTION_ORGANIZATION,
					project.getFunctionOrganization());
			parameters.put(IRoleParameter.PROJECT_LAUNCH_ORGANIZATION,
					project.getLaunchOrganization());
			parameters.put(IRoleParameter.PROJECT_PRODUCT_OPTION,
					project.getProductTypeOptions());
			parameters.put(IRoleParameter.PROJECT_STANDARD_OPTION,
					project.getStandardSetOptions());
			parameters.put(IRoleParameter.PROJECT_TYPE_OPTION,
					project.getProjectTypeOptions());
		}
		// 转换处理
		// Object workId = parameters.get(RoleParameter.WORK_ID);
		// if (workId instanceof ObjectId) {
		// Work work = ModelService.createModelObject(Work.class,
		// (ObjectId) workId);
		// parameters.put(RoleParameter.WORK, work);
		// parameters.put(RoleParameter.WORK_CHARGER,
		// work.getCharger());
		// parameters.put(RoleParameter.WORK_MILESTONE,
		// work.isMilestone());
		// parameters.put(RoleParameter.WORK_TYPE, work.getWorkType());
		// }
		// 转换处理
		// Object workId = parameters.get(RoleParameter.WORK_ID);
		// if (workId instanceof ObjectId) {
		// Work work = ModelService.createModelObject(Work.class,
		// (ObjectId) workId);
		// parameters.put(RoleParameter.WORK, work);
		// parameters.put(RoleParameter.WORK_CHARGER,
		// work.getCharger());
		// parameters.put(RoleParameter.WORK_MILESTONE,
		// work.isMilestone());
		// parameters.put(RoleParameter.WORK_TYPE, work.getWorkType());
		// // ProcessInstance executeProcess =
		// // work.getExecuteProcess();
		// //
		// // try {
		// // WorkflowProcessInstance workflowProcessInstance =
		// // WorkflowService.getDefault().getProcessInstance(
		// // executeProcess.getProcessId(),
		// // executeProcess.getId());
		// // workflowProcessInstance.getVariable(arg0)
		// // } catch (Exception e) {
		// // e.printStackTrace();
		// // }
		// // parameters.put(RoleParameter.PROCESS_INPUT, );
		// }

		String userId = new CurrentAccountContext().getUserId();
		User user = UserToolkit.getUserById(userId);
		parameters.put("user", user); //$NON-NLS-1$
		try {
			Object result = JavaScriptEvaluator.evaluate(js, parameters);
			MessageUtil.showToast("Result:" + result, SWT.ICON_INFORMATION); //$NON-NLS-1$
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
