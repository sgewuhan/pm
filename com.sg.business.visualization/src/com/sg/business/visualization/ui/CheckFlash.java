package com.sg.business.visualization.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.commons.javascript.JavaScriptEvaluator;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MultilineInputDialog;

public class CheckFlash extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = HandlerUtil.getActiveShell(event);
		String js = "var r = project.getDesc();\n"
				+ "r+=user.getUserid();\n"
				+ "result.setValue(r);";
		MultilineInputDialog ip = new MultilineInputDialog(shell, "JS 解析演示",
				"请输入JavaScript进行解析\n 已经传入了空的project，和当前User的PO对象,返回result", js, null);
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
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_PROJECT);
		DBObject dbo = col.findOne();
		Project project = ModelService.createModelObject(dbo,Project.class);
		parameters.put("project", project);
		String userId = new CurrentAccountContext().getUserId();
		User user = UserToolkit.getUserById(userId);
		parameters.put("user", user);
		try{
			Object result = JavaScriptEvaluator.evaluate(js, parameters);
			MessageUtil.showToast("Result:"+result, SWT.ICON_INFORMATION);
		}catch(Exception e){
			MessageUtil.showToast(e);
		}
	}

}
