package com.tmt.tb.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;
import com.sg.business.model.toolkit.UserToolkit;


public class SupportMessageOfTB extends MessageService {

	public SupportMessageOfTB() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessageTitle() {
		// TODO Auto-generated method stub
		String choice = (String) getInputValue("choice"); 
		if ("ͨ��".equals(choice)) { //$NON-NLS-1$
			return "������֧�����֪ͨ";
		   } else if ("��ͨ��".equals(choice)) { //$NON-NLS-1$
			return "������֧�ֱ�ȡ��֪ͨ";}
		return null;
		  
		
		
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("message".equals(messageOperation)) { //$NON-NLS-1$
			try {
				if (host instanceof Work) {
					Work work = (Work) host;
					String content =  work.getLabel();
					String choice = (String) getInputValue("choice"); //$NON-NLS-1$
					if ("ͨ��".equals(choice)) {
						//$NON-NLS-1$
						String worker = (String) getInputValue("support"); 
				        String username = UserToolkit.getUserById(worker).getUsername();
						content = content +"���ɣ�"+username+"���";
					} else if ("��ͨ��".equals(choice)) { //$NON-NLS-1$
						content = content + "�ѱ�ȡ����";
					}
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return super.getMessageContent();
	}

	@Override
	public List<String> getReceiverList() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("approvemessage".equals(messageOperation)) { //$NON-NLS-1$
			if (host instanceof Work) {
				Work work = (Work) host;
				List<?> participatesIdList = work.getParticipatesIdList();
				return (List<String>) participatesIdList;
			}
		}
		return super.getReceiverList();
		// TODO Auto-generated method stub

	}

	@Override
	public String getEditorId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
