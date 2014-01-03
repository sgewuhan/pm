package com.sg.business.taskforms.bpmservice;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;

public class FixedAssetsService extends MessageService {

	@Override
	public String getMessageTitle() {
		return "评审专家组成立通知";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) {
			
			String review_convener = (String) getInputValue("review_convener");
			String review_convener_name = UserToolkit.getUserById(review_convener).getUsername();
			String content = "评审组组长是：" + review_convener_name + "<\br>评审专家有：";
			List<String> reviewerList = (ArrayList<String>) getInputValue("reviewer_list");
			for (String userid : reviewerList) {
				String username = UserToolkit.getUserById(userid).getUsername();
				content = content + username + "<\br>";
				return content;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		if ("message".equals(messageOperation)) {
			List<String> reviewerList = (ArrayList<String>) getInputValue("reviewer_list"); //$NON-NLS-1$
			String review_convener = (String) getInputValue("review_convener"); //$NON-NLS-1$
			if (!reviewerList.contains(review_convener)) {
				reviewerList.add(review_convener);
			}
			return reviewerList;
		}

		return null;
	}

	@Override
	public String getEditorId() {
		PrimaryObject target = getTarget();
		if (target instanceof Work) {
			return Work.EDITOR;
		}
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content"); //$NON-NLS-1$
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				return (Work) host;
			}
		}
		return null;
	}

}
