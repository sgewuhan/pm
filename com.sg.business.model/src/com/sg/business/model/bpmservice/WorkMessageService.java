package com.sg.business.model.bpmservice;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;

public class WorkMessageService extends AbstractMessageService {

	@Override
	public String getMessageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessageContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReceiverList() {
		// TODO Auto-generated method stub
		return null;
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
//
//	@Override
//	public Map<String, Object> run(Object parameter) {
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		Object content = getInputValue("content");
//		if (content instanceof String) {
//			String jsonContent = (String) content;
//			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
//			if (host instanceof IProjectRelative) {
//				IProjectRelative lp = (IProjectRelative) host;
//				Project project = lp.getProject();
//				try {
//					DBObject processData = WorkflowUtils
//							.getProcessInfoFromJSON(jsonContent);
//					String processId = (String) processData.get("processId");
//					String processName = (String) processData
//							.get("processName");
//					if ("meetingnotice".equals(getOperation())) {
//						String messageTitle="评审会议通知";
//						String messageContent="会议评审";
//						@SuppressWarnings("unchecked")
//						List<String> receivers=(List<String>) getMessageReceiverId("reviewer_list");
//						sendMessage(messageTitle,messageContent,receivers,project,"project.editor",new BPMServiceContext(processName,
//								processId));
//					}else if("worknotification".equals(getOperation())){
//						String messageTitle="工作完成通知";
//						String messageContent="工作完成";
//						@SuppressWarnings("unchecked")
//						List<String> receivers=(List<String>) getMessageReceiverId("reviewer_list");
//						sendMessage(messageTitle,messageContent,receivers,project,"project.editor",new BPMServiceContext(processName,
//								processId));
//					}
//				} catch (Exception e) {
//					result.put("returnCode", "ERROR");
//					result.put("returnMessage", e.getMessage());
//				}
//			}
//		}
//		return result;
//	}
//	
//	public void sendMessage(List<?> recievers, String title, String content,
//			IContext context) {
//
//		Message message = MessageToolkit.makeMessage(recievers, title, context
//				.getAccountInfo().getConsignerId(), content);
//		try {
//			message.doSave(context);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getMessageTitle(String messageTitle) {
//		Object title = getInputValue(messageTitle);
//		if (title instanceof String) {
//			return (String) title;
//		} else
//			return null;
//
//	}
//
//	public String getMessageContent(String messageContent) {
//		Object content = getInputValue(messageContent);
//		if (content instanceof String) {
//			return (String) content;
//		} else
//			return null;
//	}
//
//	public String getMessageSender(String messageSender) {
//		Object sender = getInputValue(messageSender);
//		if (sender instanceof String) {
//			return (String) sender;
//		} else
//			return null;
//
//	}
//
//	public List<?> getMessageReceiverId(String receiverList) {
//		List<String> receivers = new ArrayList<String>();
//		Object receiver = getInputValue(receiverList);
//		if (receiver instanceof List) {
//			return (List<?>) receiver;
//		} else if (receiver instanceof String) {
//			receivers.add((String) receiver);
//			return receivers;
//		} else
//			return null;
//	}
//	

}
