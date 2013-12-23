package com.sg.business.taskforms.bpmservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.taskforms.nls.Messages;

public class ReviewerMessageService extends MessageService {

	@Override
	public String getMessageTitle() {
		String value = getOperation();
		if ("message".equals(value)) { //$NON-NLS-1$
			return Messages.get().ReviewerMessageService_1;
		} else if ("projectapplication".equals(value)) { //$NON-NLS-1$
			return Messages.get().ReviewerMessageService_3;
		}
		return null;
	}

	@Override
	public String getMessageContent() {
		String value = getOperation();
		if ("message".equals(value)) { //$NON-NLS-1$
			Object messageConten = getInputValue("messagecontent"); //$NON-NLS-1$
			return (String) messageConten;
		} else if ("projectapplication".equals(value)) { //$NON-NLS-1$
			PrimaryObject host = getTarget();
			if (host instanceof Work) {
				Work work = (Work) host;
				Object confirmdate = getInputValue("confirmdate"); //$NON-NLS-1$
				SimpleDateFormat sdf = new SimpleDateFormat(
						Utils.SDF_DATE_COMPACT_SASH);
				if (confirmdate instanceof Date) {
					confirmdate = sdf.format(confirmdate);
				}

				Object confirmtime = getInputValue("confirmtime"); //$NON-NLS-1$
				Object confirmaddress = getInputValue("confirmaddress"); //$NON-NLS-1$
				String content = Messages.get().ReviewerMessageService_10 + work.getLabel() + "<br/>"; //$NON-NLS-2$
				content = content + Messages.get().ReviewerMessageService_12 + confirmdate + " " //$NON-NLS-2$
						+ confirmtime + Messages.get().ReviewerMessageService_14 + confirmaddress
						+ Messages.get().ReviewerMessageService_15;
				return content;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		String value = getOperation();
		if ("message".equals(value)) { //$NON-NLS-1$

			List<String> reviewerList = (ArrayList<String>) getInputValue("reviewer_list"); //$NON-NLS-1$
			String reviewer_admin = (String) getInputValue("reviewer_admin"); //$NON-NLS-1$
			if (!reviewerList.contains(reviewer_admin)) {

				reviewerList.add(reviewer_admin);
			}
			return reviewerList;
		} else if ("projectapplication".equals(value)) { //$NON-NLS-1$
			List<String> reviewerList = (ArrayList<String>) getInputValue(Messages.get().ReviewerMessageService_20);
			System.out.println(""); //$NON-NLS-1$
			return reviewerList;
		}
		return null;
	}

	@Override
	public String getEditorId() {
		String value = getOperation();
		if ("message".equals(value)) { //$NON-NLS-1$
			PrimaryObject target = getTarget();
			if (target instanceof Project) {
				return Project.EDITOR_CREATE_PLAN;
			}
		} else if ("projectapplication".equals(value)) { //$NON-NLS-1$
			return null;
		}
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		String value = getOperation();
		if ("message".equals(value)) { //$NON-NLS-1$
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
		} else if ("projectapplication".equals(value)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
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