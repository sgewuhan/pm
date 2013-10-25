package com.tmt.jszx.bpmservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class ProjectReviewServiceOfJSZX extends MessageService {

	public ProjectReviewServiceOfJSZX() {
	}

	@Override
	public String getMessageTitle() {
		if ("meetingmessage".equals(getOperation())) {
			return "项目评审会议通知";
		} else if ("workmessage".equals(getOperation())) {
			return "项目评审通知";
		} else {
			return super.getMessageTitle();
		}
	}

	@Override
	public String getMessageContent() {
		Project pro = (Project) getTarget();
		if (pro != null) {
			if ("meetingmessage".equals(getOperation())) {
				try {
					String content = "请各位评审专家于";
					Date confirmTime = Utils
							.getDateValue(getInputValue("confirmtime"));
					SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
					content = content + sdf.format(confirmTime);
					content = content + "在：";
					String confirmAddress = (String) getInputValue("confirmaddress");
					content = content + confirmAddress;
					content = content + "参加";
					content = content + pro.getLabel();
					content = content + "项目的评审会！<br/>如有问题请与项目管理员：";
					String projectadminId = (String) getInputValue("act_rule_prj_admin");
					User projectadmin = UserToolkit.getUserById(projectadminId);
					content = content + projectadmin.getUsername();
					content = content + "联系";
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("workmessage".equals(getOperation())) {
				try {
					String content = "項目:" + pro.getLabel();
					String choice = (String) getInputValue("choice");
					if ("整改".equals(choice)) {
						content = content + "评审通过并且整改完成!";
					} else if ("通过".equals(choice)) {
						content = content + "评审通过!";
					} else if ("不通过".equals(choice)) {
						content = content + "评审不通过，请重新添加评审工作.";
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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		if ("meetingmessage".equals(getOperation())) {
			return super.getReceiverList();
		} else {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					Work work = (Work) host;
					List<?> participatesIdList = work.getParticipatesIdList();
					return (List<String>) participatesIdList;
				}
			}
			return null;
		}
	}
}
