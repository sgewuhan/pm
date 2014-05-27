package com.tmt.document.exporttool;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.jbpm.task.Status;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.DummyModel;
import com.sg.business.model.IDocumentProcess;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ExportUserTaskAction extends Action {

	private PrimaryObjectEditorInput input;

	public ExportUserTaskAction(PrimaryObjectEditorInput input) {
		this.input = input;
		setId("documentexport");
		setText("导出");
	}

	@Override
	public void run() {
		final PrimaryObject po = input.getData();

		if (po instanceof Document) {
			NavigatorSelector ns = new NavigatorSelector(
					"document.editor.wfhistory", "请选择需打印的流程") {
				@Override
				protected void doOK(IStructuredSelection is) {
					try {
						if (is != null && !is.isEmpty()) {
							DummyModel dummyModel = (DummyModel) is
									.getFirstElement();
							String input = "";
							DBObject processItem = dummyModel.get_data();
							List<?> history = (List<?>) processItem
									.get(IDocumentProcess.F_HISTORY);
							for (Object object : history) {
								if (Status.Completed.name().equals(
										((DBObject) object)
												.get(UserTask.F_STATUS))) {
									UserTask userTask = ModelService
											.createModelObject(
													(DBObject) object,
													UserTask.class);
									if (input != "") {
										input += "\n";
									}
									Date createOn = userTask.get_cdate();
									String status = userTask.getStatus();
									User owner = userTask.getActualOwner();
									String statusName = "";
									if (Status.Reserved.name().equals(status)) {
										statusName = "接收";
									} else if (Status.InProgress.name().equals(
											status)) {
										statusName = "开始";
									} else if (Status.Completed.name().equals(
											status)) {
										statusName = "完成";
									} else if (Status.Created.name().equals(
											status)) {
										statusName = "创建";
									} else if (Status.Ready.name().equals(
											status)) {
										statusName = "预备";
									} else if (Status.Suspended.name().equals(
											status)) {
										statusName = "暂停";
									} else if (Status.Exited.name().equals(
											status)) {
										statusName = "退出";
									}

									input += "环节名称:" + userTask.getTaskName();
									input += "执行人:" + owner.getUsername();
									input += "执行时间："
											+ String.format(
													"%1$ty/%1$tm/%1$te %1$tH:%1$tM",
													createOn) + " "
											+ statusName;

								}
							}
							if(input != ""){
								po.setValue("wfhistory", input);
								po.doSave(new CurrentAccountContext());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					UrlLauncher launcher = RWT.getClient().getService(UrlLauncher.class);
					// launcher.openURL("http://127.0.0.1:8099/reportpdf?id=111&db=1123&col=1d&editor=document.productproposal");
					launcher.openURL("/reportpdf?id=" + po.get_id() + "&editor="
							+ input.getEditorConfigurator().getId());
					ExportUserTaskAction.this.run();
					super.doOK(is);
				}
			};
			ns.setMaster(po);
			ns.show();
		} else {
			UrlLauncher launcher = RWT.getClient().getService(UrlLauncher.class);
			// launcher.openURL("http://127.0.0.1:8099/reportpdf?id=111&db=1123&col=1d&editor=document.productproposal");
			launcher.openURL("/reportpdf?id=" + po.get_id() + "&editor="
					+ input.getEditorConfigurator().getId());
			super.run();
		}

		
	}
}
