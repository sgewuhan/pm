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
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.MultilineInputDialog;

public class CheckFlash extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = HandlerUtil.getActiveShell(event);
		String js = "var r = project.getDesc();\n" //$NON-NLS-1$
				+ "r+=user.getUserid();\n" //$NON-NLS-1$
				+ "result.setValue(r);"; //$NON-NLS-1$
		MultilineInputDialog ip = new MultilineInputDialog(shell, Messages.get().CheckFlash_0,
				Messages.get().CheckFlash_1, js, null);
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
		parameters.put("project", project); //$NON-NLS-1$
		String userId = new CurrentAccountContext().getUserId();
		User user = UserToolkit.getUserById(userId);
		parameters.put("user", user); //$NON-NLS-1$
		try{
			Object result = JavaScriptEvaluator.evaluate(js, parameters);
			MessageUtil.showToast("Result:"+result, SWT.ICON_INFORMATION); //$NON-NLS-1$
		}catch(Exception e){
			MessageUtil.showToast(e);
		}
	}

}
