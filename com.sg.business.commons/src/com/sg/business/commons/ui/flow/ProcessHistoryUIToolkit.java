package com.sg.business.commons.ui.flow;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.file.GridServerFile;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.Document;
import com.sg.business.model.IDocumentProcess;
import com.sg.business.model.TaskForm;
import com.sg.business.model.UserTask;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProcessHistoryUIToolkit {

	public static void handleProcessHistoryTable(Table table) {
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String _id = event.text.substring(
								event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
								event.text.indexOf("@")); //$NON-NLS-1$
						String action = event.text.substring(event.text
								.indexOf("@") + 1); //$NON-NLS-1$
						if ("open".equals(action)) { //$NON-NLS-1$
							UserTask userTask = ModelService.createModelObject(
									UserTask.class, new ObjectId(_id));
							doOpenTaskForm(userTask);
						}
					} catch (Exception e) {
					}
				}
			}
		});
	}

	private static void doOpenTaskForm(UserTask userTask) {
		String editorId = (String) userTask.getStringValue(TaskForm.F_EDITOR);
		if (editorId == null) {
			return;
		}

		BasicDBObject taskData = new BasicDBObject();
		Iterator<String> iter = userTask.get_data().keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (key.startsWith("form_")) { //$NON-NLS-1$
				String nkey = key.substring(5);
				taskData.put(nkey, userTask.getValue(key));
			}
		}
		taskData.put(TaskForm.F_WORK_ID, userTask.getWorkId());
		DataEditorConfigurator ec = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		TaskForm taskForm = ModelService.createModelObject(taskData,
				TaskForm.class);
		try {
			DataObjectDialog.openDialog(taskForm, ec, false, null, Messages.get().ProcessHistoryUIToolkit_5);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void doPrint(long processId, ObjectId docId) {
		// doc_name_number
		// doc_rev
		// doc_createby
		// doc_createon
		// doc_releaseon
		// doc_status
		// doc_att
		// doc_flow
		// doc_printrec
		Document doc = ModelService.createModelObject(Document.class, docId);
		String doc_rev = doc.getRevId();
		Date date = doc.get_mdate();
		String doc_createon = date == null ? "" : String.format( //$NON-NLS-1$
				Utils.FORMATE_DATE_FULL, date);
		AccountInfo _account = doc.get_maccount();
		String doc_createby = _account == null ? "" : _account.getUserName(); //$NON-NLS-1$
		date = doc.getReleaseOn();
		String doc_releaseon = date == null ? "" : String.format( //$NON-NLS-1$
				Utils.FORMATE_DATE_FULL, date);
		String doc_status = doc.getLifecycleName();
		StringBuffer sb = new StringBuffer();
		List<RemoteFile> fv = doc.getGridFSFileValue(Document.F_VAULT);
		for (int i = 0; i < fv.size(); i++) {
			RemoteFile rf = fv.get(i);
			GridServerFile serverFile = new GridServerFile(rf);
			sb.append(i + ".   "); //$NON-NLS-1$
			sb.append(serverFile.getFileName());
			sb.append(" rev:"); //$NON-NLS-1$
			sb.append(serverFile.getVersion());
			sb.append("^p"); //$NON-NLS-1$
		}
		String doc_att = sb.toString();
		String username = new CurrentAccountContext().getAccountInfo()
				.getUserName();
		String doc_printrec = username + "|" //$NON-NLS-1$
				+ String.format(Utils.FORMATE_DATE_FULL, new Date());

		// 获得流程记录
		sb = new StringBuffer();
		DBObject processHistory = doc.getProcessHistory(processId);
		if (processHistory == null) {
			return;
		}
		String doc_name_number = doc.getDesc() + "|" //$NON-NLS-1$
				+ processHistory.get(IDocumentProcess.F_MAJOR_VID) + "." //$NON-NLS-1$
				+ processHistory.get(IDocumentProcess.F_SECOND_VID);

		List<DBObject> history = (List<DBObject>) processHistory
				.get(IDocumentProcess.F_HISTORY);
		if (history == null) {
			return;
		}
		DBUtil.sort(history, UserTask.F_CREATEDON, 1);
		for (Object object : history) {
			if (Status.Completed.name().equals(
					((DBObject) object).get(UserTask.F_STATUS))) {
				UserTask userTask = ModelService.createModelObject(
						(DBObject) object, UserTask.class);
				sb.append(userTask.getLabel() + " "); //$NON-NLS-1$

				Object userId = userTask.getValue(UserTask.F_CREATEDBY);
				sb.append(UserToolkit.getUserById((String) userId)
						.getUsername());
				sb.append(" "); //$NON-NLS-1$
				String choice = userTask.getChoice();
				if (choice != null) {
					sb.append(choice);
				}
				sb.append(" "); //$NON-NLS-1$
				date = (Date) userTask.getValue(UserTask.F_CREATEDON);
				sb.append(String.format(Utils.FORMATE_DATE_FULL, date));
				String comment = userTask.getComment();
				if (comment != null) {
					sb.append(comment);
				}
				sb.append("^p^p"); //$NON-NLS-1$
			}
		}
		String doc_flow = sb.toString();

		// TODO 用户设置 打印机名称
		String printerName = "PM"; //$NON-NLS-1$

		String template = "docflowrec.pdf"; //$NON-NLS-1$

		StringBuilder url = new StringBuilder();
		url.append("/form/print"); //$NON-NLS-1$
		url.append("?form=" + template); //$NON-NLS-1$
		url.append("&printer=" + printerName); //$NON-NLS-1$
		url.append("&font=" + "simhei.ttf"); //$NON-NLS-1$ //$NON-NLS-2$

		url.append("&doc_name_number=" + doc_name_number); //$NON-NLS-1$
		url.append("&doc_rev=" + doc_rev); //$NON-NLS-1$
		url.append("&doc_createon=" + doc_createon); //$NON-NLS-1$
		url.append("&doc_createby=" + doc_createby); //$NON-NLS-1$
		url.append("&doc_releaseon=" + doc_releaseon); //$NON-NLS-1$
		url.append("&doc_att=" + doc_att); //$NON-NLS-1$
		url.append("&doc_status=" + doc_status); //$NON-NLS-1$
		url.append("&doc_printrec=" + doc_printrec); //$NON-NLS-1$
		url.append("&doc_flow=" + doc_flow); //$NON-NLS-1$

		String encodedURL = RWT.getResponse().encodeURL(url.toString());
		UrlLauncher launcher = RWT.getClient().getService(UrlLauncher.class);
		launcher.openURL(encodedURL);

	}

}
