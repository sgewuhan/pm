package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sg.bpm.service.task.ServiceProvider;
import com.sg.business.model.Message;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public  abstract class MessageService extends ServiceProvider{

	
	public void sendMessage(String messageTitle,String messageContent,String messageSender,String receiverList){
		String content=getMessageContent(messageContent);
		String sender=getMessageSender(messageSender);
		@SuppressWarnings("unchecked")
		List<String> receiver=(List<String>) getMessageReceiverId(receiverList);
		Map<String, Message> msgList=new HashMap<String, Message>();
		for(String userId:receiver){
			msgList.put(userId, null);
			MessageToolkit.appendMessage(msgList,userId,messageTitle,content,null,null,new CurrentAccountContext());
		}
		Iterator<Message> iter = msgList.values().iterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			try {
				message.doSave(new CurrentAccountContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
