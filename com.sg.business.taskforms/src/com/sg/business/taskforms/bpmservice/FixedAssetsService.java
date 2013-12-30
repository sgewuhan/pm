package com.sg.business.taskforms.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class FixedAssetsService extends MessageService  {



	@Override
	public String getMessageTitle() {
		PrimaryObject host = getTarget();
		if (host instanceof Work) {
			Work work = (Work) host;
			String content =  work.getLabel();
		return content + "评审专家组成立通知";}
		return null;
		}
	

	@SuppressWarnings("unchecked")
	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("message".equals(messageOperation)) { 
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content =  work.getLabel();
					String review_convener = (String) getInputValue("review_convener");
					List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list");
					String reviewer_admin = UserToolkit.getUserById(review_convener).getUsername();
					content=content + "评审组组长是："+reviewer_admin+"<\br>评审专家有：";
					for(int i=0;i<reviewerList.size();i++)
					{
						String userid = reviewerList.get(i);
						String username= UserToolkit.getUserById(userid).getUsername();
						content=content+username+"<\br>";
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return super.getMessageContent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) { //$NON-NLS-1$
		
				List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list");
				String prj_admin = (String)getInputValue("act_prj_admin");
				String review_convener = (String)getInputValue("review_convener");
				reviewerList.add(prj_admin);
				reviewerList.add(review_convener);
				return (List<String>) reviewerList;
			}
		
		return super.getReceiverList();
	}

	@Override
	public String getEditorId() {
		return super.getEditorId();
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			return host;
		}
		return null;
	}

	public FixedAssetsService() {
	}

}
