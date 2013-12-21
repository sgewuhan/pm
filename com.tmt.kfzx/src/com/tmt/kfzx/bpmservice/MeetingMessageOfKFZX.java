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
import com.tmt.kfzx.nls.Messages;

public class MeetingMessageOfKFZX extends AbstractMessageService {

	

	@Override
	public String getMessageTitle() {
		if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
			return Messages.get().MeetingMessageOfKFZX_1;
		} else if ("workmessage".equals(getOperation())) { //$NON-NLS-1$
			return Messages.get().MeetingMessageOfKFZX_3;
		}
			return null;
	}
	

	@Override
	public String getMessageContent() {
		// TODO Auto-generated method stub
		Project pro = (Project) getTarget();
		if (pro != null) {
			if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
				try {
					String content = Messages.get().MeetingMessageOfKFZX_5;
					Date confirmTime = Utils
							.getDateValue(getInputValue("confirmdata")); //$NON-NLS-1$
					SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
					content ="   " +content + sdf.format(confirmTime ); //$NON-NLS-1$
					content = Messages.get().MeetingMessageOfKFZX_8 +content + (String) getInputValue("confirmtime"); //$NON-NLS-2$
					content = content + Messages.get().MeetingMessageOfKFZX_10;
					String confirmAddress = (String) getInputValue("confirmaddress"); //$NON-NLS-1$
					content = content + confirmAddress;
					content = content + Messages.get().MeetingMessageOfKFZX_12;
					content = content + pro.getLabel();
					content = content + Messages.get().MeetingMessageOfKFZX_13;
					String projectadminId = (String) getInputValue("act_rule_launcher"); //$NON-NLS-1$
					User projectadmin = UserToolkit.getUserById(projectadminId);
					content = content + projectadmin.getUsername();
					content = content + Messages.get().MeetingMessageOfKFZX_15;
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("workmessage".equals(getOperation())) { //$NON-NLS-1$
				try {
					String content = Messages.get().MeetingMessageOfKFZX_0 + pro.getLabel();
						content = content + Messages.get().MeetingMessageOfKFZX_2;
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
		if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
		List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list"); //$NON-NLS-1$
		String reviewer_admin=(String)getInputValue("reviewer_admin"); //$NON-NLS-1$
		if(!reviewerList.contains(reviewer_admin)){
			
			reviewerList.add(reviewer_admin);
		}
		 return reviewerList;
		} else {
			Object content = getInputValue("content"); //$NON-NLS-1$
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
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content"); //$NON-NLS-1$
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
