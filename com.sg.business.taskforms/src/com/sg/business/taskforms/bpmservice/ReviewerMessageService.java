package com.sg.business.taskforms.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.bpmservice.MessageService;

public class ReviewerMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "评审会议通知";
	}

	@Override
	public String getMessageContent() {
		Object messageConten = getInputValue("messagecontent");
		return (String)messageConten;
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