package com.sg.business.taskforms.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class ReviewerMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		String value = getOperation();
		if ("message".equals(value)) {
			return "评审会议通知";
		} else if ("projectapplication".equals(value)) {
			return "立项评审会议通知";
		}
		return null;
	}

	@Override
	public String getMessageContent() {
		String value = getOperation();
		if ("message".equals(value)) {
			Object messageConten = getInputValue("messagecontent");
			return (String) messageConten;
		} else if ("projectapplication".equals(value)) {
			PrimaryObject host = getTarget();
			if (host instanceof Work) {
				Work work = (Work) host;
				Object confirmdate = getInputValue("confirmdate");
				Object confirmtime = getInputValue("confirmtime");
				Object confirmaddress = getInputValue("confirmaddress");
				String content = "立项工作:" + work.getLabel() + " ";
				content = content+"请您于: "+confirmdate+" "+confirmtime +" 在 "+confirmaddress+" 参加项目立项评审会!";
				return content;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String value = getOperation();
		if ("message".equals(value)) {

			List<String> reviewerList = (ArrayList<String>) getInputValue("reviewer_list");
			String reviewer_admin = (String) getInputValue("reviewer_admin");
			if (!reviewerList.contains(reviewer_admin)) {

				reviewerList.add(reviewer_admin);
			}
			return reviewerList;
		} else if ("projectapplication".equals(value)) {
			List<String> reviewerList = (ArrayList<String>) getInputValue("reviewer_list");
			System.out.println("");
			return reviewerList;
		}
		return null;
	}

	@Override
	public String getEditorId() {
		String value = getOperation();
		if ("message".equals(value)) {
			return Project.EDITOR_CREATE_PLAN;
		} else if ("projectapplication".equals(value)) {
			return null;
		}
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		String value = getOperation();
		if ("message".equals(value)) {
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
		} else if ("projectapplication".equals(value)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		}
		return null;
	}
}