package com.tmt.jszx.bpmservice;

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
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;
import com.tmt.jszx.nls.Messages;

public class ProjectReviewServiceOfJSZX extends MessageService {

	public ProjectReviewServiceOfJSZX() {
	}

	@Override
	public String getMessageTitle() {
		if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
			return Messages.get().ProjectReviewServiceOfJSZX_1;
		} else if ("workmessage".equals(getOperation())) { //$NON-NLS-1$
			return Messages.get().ProjectReviewServiceOfJSZX_3;
		} else if ("reviewermessage".equals(getOperation())) { //$NON-NLS-1$
			return Messages.get().ProjectReviewServiceOfJSZX_3;
		} else {
			return super.getMessageTitle();
		}
	}

	@Override
	public String getMessageContent() {
		Project pro = (Project) getTarget();
		if (pro != null) {
			if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
				try {
					String content = Messages.get().ProjectReviewServiceOfJSZX_5;
					Date confirmTime = Utils
							.getDateValue(getInputValue("confirmtime")); //$NON-NLS-1$
					SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE);
					content = content + sdf.format(confirmTime);
					content = content
							+ Messages.get().ProjectReviewServiceOfJSZX_7;
					String confirmAddress = (String) getInputValue("confirmaddress"); //$NON-NLS-1$
					content = content + confirmAddress;
					content = content
							+ Messages.get().ProjectReviewServiceOfJSZX_9;
					content = content + pro.getLabel();
					content = content
							+ Messages.get().ProjectReviewServiceOfJSZX_10;
					String projectadminId = (String) getInputValue("act_rule_prj_admin"); //$NON-NLS-1$
					User projectadmin = UserToolkit.getUserById(projectadminId);
					content = content + projectadmin.getUsername();
					content = content
							+ Messages.get().ProjectReviewServiceOfJSZX_12;
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("workmessage".equals(getOperation())) { //$NON-NLS-1$
				try {
					String content = Messages.get().ProjectReviewServiceOfJSZX_14
							+ pro.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("整改".equals(choice)) { //$NON-NLS-1$
						content = content
								+ Messages.get().ProjectReviewServiceOfJSZX_0;
					} else if ("通过".equals(choice)) { //$NON-NLS-1$
						content = content
								+ Messages.get().ProjectReviewServiceOfJSZX_19;
					} else if ("不通过".equals(choice)) { //$NON-NLS-1$
						content = content
								+ Messages.get().ProjectReviewServiceOfJSZX_21;
					}
					return content;
				} catch (Exception e) {
					return null;
				}
			} else if ("reviewermessage".equals(getOperation())) { //$NON-NLS-1$
				try {
					String content = Messages.get().ProjectReviewServiceOfJSZX_14
							+ pro.getLabel();
					return content;
				} catch (Exception e) {
					return null;
				}
			}
		}
		return super.getMessageContent();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		if ("meetingmessage".equals(getOperation())) { //$NON-NLS-1$
			List<String> receiverList = super.getReceiverList();
			String launcher = (String) getInputValue("act_rule_launcher"); //$NON-NLS-1$
			receiverList.add(launcher);
			return receiverList;
		} else if ("workmessage".equals(getOperation())) {
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
		}else if ("reviewermessage".equals(getOperation())) { //$NON-NLS-1$
			 Object inputValue = getInputValue("reviewer_list");
			 if (inputValue instanceof ArrayList<?>) {
					ArrayList<?> arrayList = (ArrayList<?>) inputValue;
					List<String> receivers = new ArrayList<String>();
					for (Object object : arrayList) {
						if (object instanceof String) {
							String string = (String) object;
							receivers.add(string);
						}
					}
					if (receivers != null && receivers.size() > 0) {
						return receivers;
					} else {
						return null;
					}
				}
		}
		return null;
	}
}
