package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IProjectRelative;

public class MessageServiceTest001 extends MessageService {

	@Override
	public Map<String, Object> run(Object parameter) {

		HashMap<String, Object> result = new HashMap<String, Object>();

		String messageTitle="yyyyyy";
		String messageContent="yyyy";
		@SuppressWarnings("unchecked")
		List<String> receivers=(List<String>) getMessageReceiverId("reviewer_list");
		
		Object content = getInputValue("content");
		if (content instanceof String) {
			String jsonContent = (String) content;
			PrimaryObject host = WorkflowUtils.getHostFromJSON(jsonContent);
			if (host instanceof IProjectRelative) {
				IProjectRelative lp = (IProjectRelative) host;
				try {
					DBObject processData = WorkflowUtils
							.getProcessInfoFromJSON(jsonContent);
					String processId = (String) processData.get("processId");
					String processName = (String) processData
							.get("processName");
					if ("message".equals(getOperation())) {
						sendMessage(messageTitle,messageContent,receivers,new BPMServiceContext(processName,
								processId));
					}
				} catch (Exception e) {
					result.put("returnCode", "ERROR");
					result.put("returnMessage", e.getMessage());
				}
			}
		}
		return result;
	

		
		
		
		
	}
	
	
	public String getMessageContent(String messageContent){
		Object content = getInputValue(messageContent);
		if(content instanceof String){
			return (String)content;
		}
		else return null;
	}
	
	public String getMessageSender(String messageSender){
		Object sender = getInputValue(messageSender);
		if(sender instanceof String){
			return (String)sender;
		}
		else return null;
		
	}
	
	public List<?> getMessageReceiverId(String receiverList){
		List<String> receivers=new ArrayList<String>();
		Object receiver = getInputValue(receiverList);
		if(receivers instanceof List){
			return (List<?>)receivers;
		}else if(receiver instanceof String){
			receivers.add((String)receiver);
			return receivers;
		}
		else return null;
	}


}
