package com.tmt.kfzx.bpmservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.AbstractMessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class MeetingMessageOfKFZX extends AbstractMessageService {

	

	@Override
	public String getMessageTitle() {
		if ("meetingmessage".equals(getOperation())) {
			return "��Ŀ�������֪ͨ";
		} else if ("workmessage".equals(getOperation())) {
			return "��Ŀ�������֪ͨ";
		}
			return null;
	}
	

	@Override
	public String getMessageContent() {
		// TODO Auto-generated method stub
		Project pro = (Project) getTarget();
		if (pro != null) {
			if ("meetingmessage".equals(getOperation())) {
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
					content = content + " ��Ŀ������ᣡ<br/>������������";
					String projectadminId = (String) getInputValue("act_rule_launcher");
					User projectadmin = UserToolkit.getUserById(projectadminId);
					content = content + projectadmin.getUsername();
					content = content + "��ϵ";
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("workmessage".equals(getOperation())) {
				try {
					String content = "�Ŀ:" + pro.getLabel();
						content = content + "����ͨ��!";
					return content;
				} catch (Exception e) {
					return null;
				}
			}
			
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		// TODO Auto-generated method stub
		if ("meetingmessage".equals(getOperation())) {
		List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list");
		String reviewer_admin=(String)getInputValue("reviewer_admin");
		if(!reviewerList.contains(reviewer_admin)){
			
			reviewerList.add(reviewer_admin);
		}
		 return reviewerList;
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
	

	@Override
	public String getEditorId() {
		return Project.EDITOR_CREATE_PLAN;
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
