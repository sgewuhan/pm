package com.tmt.tb.bpmservice;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.tmt.tb.nls.Messages;

public class ApplicationMessageOfTB extends MessageService {

	public ApplicationMessageOfTB() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("applicationcancelmessage".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApplicationMessageOfTB_1 + work.getLabel();
					content = content + Messages.get().ApplicationMessageOfTB_2;
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content = Messages.get().ApplicationMessageOfTB_4 + work.getLabel();
					content = content + Messages.get().ApplicationMessageOfTB_5;
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("financialmessage".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					Object projectid = getInputValue("project_id"); //$NON-NLS-1$
					if (projectid instanceof String) {
						ObjectId _id = new ObjectId((String) projectid);
						Project project = ModelService.createModelObject(Project.class, _id);
						String content = Messages.get().ApplicationMessageOfTB_8 + work.getLabel() + " "; //$NON-NLS-2$
						content = content+Messages.get().ApplicationMessageOfTB_10+project.getDesc()+Messages.get().ApplicationMessageOfTB_11;
						return content;
					}
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
		PrimaryObject host = getTarget();
		if ("applicationcancelmessage".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		} else if ("financialmessage".equals(messageOperation)) { //$NON-NLS-1$
			Object financial = getInputValue("act_prj_financial"); //$NON-NLS-1$
			if (financial instanceof String) {
				List<String> financialList = new ArrayList<String>();
				financialList.add((String) financial);
				return financialList;
			}
		}
		return super.getReceiverList();
	}

	@Override
	public PrimaryObject getTarget() {
		String messageOperation = getOperation();
		if ("applicationcancelmessage".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("applicationfinishmessage".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("financialmessage".equals(messageOperation)) { //$NON-NLS-1$
			Object content = getInputValue("content"); //$NON-NLS-1$
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		}
		return super.getTarget();
	}

	@Override
	public String getMessageTitle() {
		String messageOperation = getOperation();
		if ("applicationcancelmessage".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApplicationMessageOfTB_23;
		} else if ("applicationfinishmessage".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApplicationMessageOfTB_25;
		} else if ("financialmessage".equals(messageOperation)) { //$NON-NLS-1$
			return Messages.get().ApplicationMessageOfTB_27;
		}
		return super.getMessageTitle();
	}

}
