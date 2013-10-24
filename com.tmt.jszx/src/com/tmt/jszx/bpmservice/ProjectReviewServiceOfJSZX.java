package com.tmt.jszx.bpmservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class ProjectReviewServiceOfJSZX extends MessageService {

	private String newOperation;

	public ProjectReviewServiceOfJSZX() {
		newOperation = getOperation();
	}

	@Override
	public String getMessageTitle() {
		if ("meetingmessage".equals(newOperation)) {
			return "��Ŀ�������֪ͨ";
		} else if ("workmessage".equals(newOperation)) {
			return "��Ŀ����֪ͨ";
		} else {
			return super.getMessageTitle();
		}
	}

	@Override
	public String getMessageContent() {
		Project pro = (Project) getTarget();
		if (pro != null) {
			if ("meetingmessage".equals(newOperation)) {
				try {
					String content = "���λ����ר����";
					Date confirmTime = Utils
							.getDateValue(getInputValue("confirmtime"));
					SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
					content = content + sdf.format(confirmTime);
					content = content + "�ڣ�";
					String confirmAddress = (String) getInputValue("confirmaddress");
					content = content + confirmAddress;
					content = content + "�μ�";
					content = content + pro.getLabel();
					content = content + "��Ŀ������ᣡ<br/>��������������Ŀ����Ա��";
					String projectadminId = (String) getInputValue("act_rule_prj_admin");
					User projectadmin = UserToolkit.getUserById(projectadminId);
					content = content + projectadmin.getUsername();
					content = content + "��ϵ";
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("workmessage".equals(newOperation)) {
				try {
					String content= "�Ŀ:"+pro.getLabel();
					String choice = (String) getInputValue("choice");
					if ("����".equals(choice)) {
						content = content+ "����ͨ�������������!";
					} else if ("ͨ��".equals(choice)) {
						content = content+ "����ͨ��!";
					} else if ("��ͨ��".equals(choice)) {
						content = content+ "����ͨ��������������������.";
					}
					return content;
				} catch (Exception e) {
					return null;
				}
			}
		}
		return super.getMessageContent();
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof IProjectRelative) {
				IProjectRelative lp = (IProjectRelative) host;
				Project project = lp.getProject();
				return project;
			}
		}
		return null;
	}

}