package com.tmt.kfzx.bpmservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.bpmservice.AbstractMessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class projectstartMessageOfKFZX extends AbstractMessageService {

	@Override
	public String getMessageTitle() {
		return "��Ŀ��������֪ͨ";
	}

	@Override
	public String getMessageContent() {
		Project pro = (Project) getTarget();
		String content = "���λ����ר����";
		Date confirmTime = Utils
				.getDateValue(getInputValue("confirmdata"));
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
		content ="  "+ content + sdf.format(confirmTime);
		content = "��  " +content + (String) getInputValue("confirmtime")+"  ";
		content = content + "�ڣ�";
		String confirmAddress = (String) getInputValue("confirmaddress");
		content = content + confirmAddress;
		content = content + "�μ�";
		content = content + pro.getLabel();
		content = content + " ��Ŀ������ᣡ<br/>������������";
		String projectadminId = (String) getInputValue("act_rule_prj_admin");
		User projectadmin = UserToolkit.getUserById(projectadminId);
		content = content + projectadmin.getUsername();
		content = content + "��ϵ!";
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list");
		String reviewer_admin=(String)getInputValue("reviewer_admin");
		if(!reviewerList.contains(reviewer_admin)){
			
			reviewerList.add(reviewer_admin);
		}
		 return reviewerList;
	}

	@Override
	public String getEditorId() {
		return null;
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
