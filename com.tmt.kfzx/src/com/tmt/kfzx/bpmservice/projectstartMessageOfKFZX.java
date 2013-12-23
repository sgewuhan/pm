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
import com.tmt.kfzx.nls.Messages;

public class projectstartMessageOfKFZX extends AbstractMessageService {

	@Override
	public String getMessageTitle() {
		return Messages.get().projectstartMessageOfKFZX_0;
	}

	@Override
	public String getMessageContent() {
		Project pro = (Project) getTarget();
		String content = Messages.get().projectstartMessageOfKFZX_1;
		Date confirmTime = Utils
				.getDateValue(getInputValue("confirmdata")); //$NON-NLS-1$
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
		content ="  "+ content + sdf.format(confirmTime); //$NON-NLS-1$
		content = Messages.get().projectstartMessageOfKFZX_4 +content + (String) getInputValue("confirmtime")+"  "; //$NON-NLS-2$ //$NON-NLS-3$
		content = content + Messages.get().projectstartMessageOfKFZX_7;
		String confirmAddress = (String) getInputValue("confirmaddress"); //$NON-NLS-1$
		content = content + confirmAddress;
		content = content + Messages.get().projectstartMessageOfKFZX_9;
		content = content + pro.getLabel();
		content = content + Messages.get().projectstartMessageOfKFZX_10;
		String projectadminId = (String) getInputValue("act_rule_prj_admin"); //$NON-NLS-1$
		User projectadmin = UserToolkit.getUserById(projectadminId);
		content = content + projectadmin.getUsername();
		content = content + Messages.get().projectstartMessageOfKFZX_12;
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		List<String> reviewerList =(ArrayList<String>) getInputValue("reviewer_list"); //$NON-NLS-1$
		String reviewer_admin=(String)getInputValue("reviewer_admin"); //$NON-NLS-1$
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
