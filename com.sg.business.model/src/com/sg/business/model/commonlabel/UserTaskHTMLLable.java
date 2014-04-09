package com.sg.business.model.commonlabel;

import java.util.Date;

import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class UserTaskHTMLLable extends CommonHTMLLabel {

	private UserTask userTask;

	public UserTaskHTMLLable(UserTask userTask) {
		this.userTask = userTask;
	}

	@Override
	public String getHTML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		// 显示否决图标
		String choice = userTask.getChoice();
		if (choice != null) {
			if (Utils.isDenied(choice)) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_DENIED_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />"); //$NON-NLS-1$
			} else if (Utils.isAdmit(choice)) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_PASS_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />"); //$NON-NLS-1$
			}
		}

		// 显示流程任务的内容
		String status = userTask.getStatus();
		sb.append("<span style='float:right;padding-right:14px'>"); //$NON-NLS-1$
		User owner = userTask.getActualOwner();
		sb.append(owner.getUsername());
		sb.append(" "); //$NON-NLS-1$
		Date createOn = userTask.get_cdate();
		sb.append(String.format("%1$ty/%1$tm/%1$te %1$tH:%1$tM", createOn)); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$

		if (Status.Reserved.name().equals(status)) {
			sb.append(Messages.get().UserTask_7);
		} else if (Status.InProgress.name().equals(status)) {
			sb.append(Messages.get().UserTask_8);
		} else if (Status.Completed.name().equals(status)) {
			sb.append(Messages.get().UserTask_9);
		} else if (Status.Created.name().equals(status)) {
			sb.append(Messages.get().UserTask_10);
		} else if (Status.Ready.name().equals(status)) {
			sb.append(Messages.get().UserTask_11);
		} else if (Status.Suspended.name().equals(status)) {
			sb.append(Messages.get().UserTask_12);
		} else if (Status.Exited.name().equals(status)) {
			sb.append(Messages.get().UserTask_13);
		}

		sb.append("</span>"); //$NON-NLS-1$

		if (Status.Completed.name().equals(status)) {
			String editorId = (String) userTask
					.getStringValue(TaskForm.F_EDITOR);
			if (editorId != null) {
				sb.append("<a href=\"" + userTask.get_id().toString() + "@" //$NON-NLS-1$ //$NON-NLS-2$
						+ "open" + "\" target=\"_rwt\">"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_BULLETING_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />"); //$NON-NLS-1$
				sb.append("</a>"); //$NON-NLS-1$
			}
		}

		String desc = userTask.getTaskName();
		sb.append(desc);

		if (choice != null) {
			sb.append(" "); //$NON-NLS-1$
			String coloredString = Utils.appendColor(choice);
			sb.append(coloredString);
			sb.append(""); //$NON-NLS-1$
		}

		sb.append("<br/>"); //$NON-NLS-1$

		// 显示任务详细信息
		sb.append("<small style='color:#b0b0b0'>"); //$NON-NLS-1$
		String comment = userTask.getComment();
		if (comment != null) {
			sb.append(" "); //$NON-NLS-1$
			comment = Utils.getPlainText(comment);
			comment = Utils.getLimitLengthString(comment, 40);
			sb.append(comment);
		}
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

}
