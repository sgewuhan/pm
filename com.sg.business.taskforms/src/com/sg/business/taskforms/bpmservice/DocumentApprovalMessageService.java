package com.sg.business.taskforms.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class DocumentApprovalMessageService extends MessageService {

	public DocumentApprovalMessageService() {
	}

	@Override
	public String getMessageTitle() {
		// TODO Auto-generated method stub
		return "�ļ�����ͨ����Ϣ";
	}

	@Override
	public String getMessageContent() {
		// TODO Auto-generated method stub
		  return "������" + getTarget().getLabel() + "�������";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReceiverList() {
		PrimaryObject target= getTarget();
		if(target instanceof Work )
		{
			Work work = (Work) target;
			List<?> participatesIdList = work.getParticipatesIdList();
			return (List<String>) participatesIdList;
		} else
			return null;
	}

	@Override
	public String getEditorId() {
		return null;
	}

	@Override
	public PrimaryObject getTarget() {
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof Work) {
				Work work = (Work) host;
				return work;
			}
		}
		return null;
	}

}
