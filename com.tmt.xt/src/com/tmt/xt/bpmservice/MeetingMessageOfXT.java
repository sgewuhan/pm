package com.tmt.xt.bpmservice;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.MessageService;

public class MeetingMessageOfXT extends MessageService {

	public MeetingMessageOfXT() {
	}

	@Override
	public String getMessageContent() {
		String messageOperation = getOperation();
		PrimaryObject host = getTarget();
		if ("projectapplication".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Object confirmDate = getInputValue("confirmdate");
					Calendar cal = Calendar.getInstance();
					cal.setTime((Date) confirmDate);
					Object confirmTime = getInputValue("confirmtime");
					Object confirmAddress = getInputValue("confirmaddress");
					Object confirmType = getInputValue("confirmtype");
					Work work = (Work) host;
					String content = "�ȶ���" + cal.get(Calendar.YEAR) + "��"
							+ cal.get(Calendar.MONTH) + "��"
							+ cal.get(Calendar.DAY_OF_MONTH) + "�� "
							+ confirmTime + "��" + confirmAddress + "�ٿ�"
							+ work.getDesc() + confirmType + "������׼ʱ�μӣ�";
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("projectapprove".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Object confirmDate = getInputValue("confirmdate");
					Calendar cal = Calendar.getInstance();
					cal.setTime((Date) confirmDate);
					Object confirmTime = getInputValue("confirmtime");
					Object confirmAddress = getInputValue("confirmaddress");
					Object confirmType = getInputValue("confirmtype");
					Work work = (Work) host;
					String content = "�ȶ���" + cal.get(Calendar.YEAR) + "��"
							+ cal.get(Calendar.MONTH) + "��"
							+ cal.get(Calendar.DAY_OF_MONTH) + "�� "
							+ confirmTime + "��" + confirmAddress + "�ٿ�"
							+ work.getProject().getDesc() + confirmType
							+ "������׼ʱ�μӣ�";
					return content;
				}
			} catch (Exception e) {
				return null;
			}
		} else if ("subconcessions".equals(messageOperation)) {
			try {
				if (host instanceof Work) {
					Object confirmDate = getInputValue("confirmdate");
					Calendar cal = Calendar.getInstance();
					cal.setTime((Date) confirmDate);
					Object confirmTime = getInputValue("confirmtime");
					Object confirmAddress = getInputValue("confirmaddress");
					Object confirmType = getInputValue("confirmtype");
					String projectid =  (String) getInputValue("projectid");
					ObjectId _id = new ObjectId(projectid);
					Project project = ModelService.createModelObject(Project.class, _id);
					String content = "�ȶ���" + cal.get(Calendar.YEAR) + "��"
							+ cal.get(Calendar.MONTH) + "��"
							+ cal.get(Calendar.DAY_OF_MONTH) + "�� "
							+ confirmTime + "��" + confirmAddress + "�ٿ�"
							+ project.getDesc() + confirmType
							+ "������׼ʱ�μӣ�";
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
		PrimaryObject host = getTarget();
		if ("projectapplication".equals(messageOperation)) {
			if (host instanceof Work) {
				List<String> participatesIdList = (List<String>) getInputValue("reviewer_list");
				return participatesIdList;
			}
		} else if ("projectapprove".equals(messageOperation)) {
			if (host instanceof Work) {
				List<String> participatesIdList = (List<String>) getInputValue("reviewer_list");
				participatesIdList
						.add((String) getInputValue("reviewer_admin"));
				return participatesIdList;
			}
		} else if ("subconcessions".equals(messageOperation)) {
			if (host instanceof Work) {
				List<String> participatesIdList = (List<String>) getInputValue("reviewer_list");
				participatesIdList
						.add((String) getInputValue("reviewer_admin"));
				return participatesIdList;
			}
		}
		return super.getReceiverList();
	}

	@Override
	public PrimaryObject getTarget() {
		String messageOperation = getOperation();
		if ("projectapplication".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("projectapprove".equals(messageOperation)) {
			Object content = getInputValue("content");
			if (content instanceof String) {
				String jsonContent = (String) content;
				PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
				if (host instanceof Work) {
					return (Work) host;
				}
			}
		} else if ("subconcessions".equals(messageOperation)) {
			Object content = getInputValue("content");
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
		if ("projectapplication".equals(messageOperation)) {
			return "��������֪ͨ";
		} else if ("projectapprove".equals(messageOperation)) {
			return "����֪ͨ";
		} else if ("subconcessions".equals(messageOperation)) {
			return "ת������֪ͨ";
		}
		return super.getMessageTitle();
	}

}
