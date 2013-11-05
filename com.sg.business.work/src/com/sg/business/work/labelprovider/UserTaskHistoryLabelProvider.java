package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class UserTaskHistoryLabelProvider extends
		ConfiguratorColumnLabelProvider {

	public UserTaskHistoryLabelProvider(ColumnConfigurator configurator) {
		super(configurator);
	}

	public UserTaskHistoryLabelProvider() {
		super();
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:9pt'>");
		// ��ʾ���ͼ��
		appendDeniedMark((UserTask) element, sb);

		// ��ʾ�������������
		appendUserTaskInfo((UserTask) element, sb);

		sb.append("<br/>");

		// ��ʾ������ϸ��Ϣ
		sb.append("<small>");
		appendTaskDetailInfo((UserTask) element, sb);
		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
	}

	private void appendDeniedMark(UserTask userTask, StringBuffer sb) {
		String choice = userTask.getChoice();
		if (choice != null) {
			if (Utils.isDenied(choice)) {
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_DENIED_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />");
			} else if (Utils.isAdmit(choice)) {
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_PASS_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='position:absolute; right:0; top:0; display:block;' width='32' height='32' />");
			}
		}
	}

	private void appendTaskDetailInfo(UserTask userTask, StringBuffer sb) {
		String comment = userTask.getComment();
		if (comment != null) {
			sb.append(" ");
			comment = Utils.getPlainText(comment);
			comment = Utils.getLimitLengthString(comment, 40);
			sb.append(comment);
		}

	}

	private void appendUserTaskInfo(UserTask userTask, StringBuffer sb) {
		String status = userTask.getStatus();
		sb.append("<span style='float:right;padding-right:14px'>");
		User owner = userTask.getActualOwner();
		sb.append(owner);
		sb.append(" ");

		Date createOn = userTask.get_cdate();
		sb.append(String.format("%1$tm/%1$te %1$tH:%1$tM", createOn));
		sb.append(" ");

		if (Status.Reserved.name().equals(status)) {
			sb.append("����");
		} else if (Status.InProgress.name().equals(status)) {
			sb.append("��ʼ");
		} else if (Status.Completed.name().equals(status)) {
			sb.append("���");
		} else if (Status.Created.name().equals(status)) {
			sb.append("����");
		} else if (Status.Ready.name().equals(status)) {
			sb.append("Ԥ��");
		} else if (Status.Suspended.name().equals(status)) {
			sb.append("��ͣ");
		} else if (Status.Exited.name().equals(status)) {
			sb.append("�˳�");
		}

		sb.append("</span>");

		if (Status.Completed.name().equals(status)) {
			String editorId = (String) userTask
					.getStringValue(TaskForm.F_EDITOR);
			if (editorId != null) {
				sb.append("<a href=\"" + userTask.get_id().toString() + "@"
						+ "open" + "\" target=\"_rwt\">");
				sb.append("<img src='");
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_BULLETING_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />");
				sb.append("</a>");
			}
		}

		String desc = userTask.getTaskName();
		sb.append(desc);

		String choice = userTask.getChoice();
		if (choice != null) {
			sb.append(" ");
			String coloredString = Utils.appendColor(choice);
			sb.append(coloredString);
			sb.append("");
		}
	}

}
