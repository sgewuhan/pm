package com.sg.business.model.bpmservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.business.model.Message;
import com.sg.business.model.toolkit.MessageToolkit;

public  abstract class MessageService extends ServiceProvider{

	
	public void sendMessage(String messageTitle,String messageContent,List<String> receiverList,PrimaryObject target, String editId,IContext context){
		
		Map<String, Message> msgList=new HashMap<String, Message>();
		for(String userId:receiverList){
			msgList.put(userId, null);
			MessageToolkit.appendMessage(msgList,userId,messageTitle,messageContent,target,editId,context);
		}
		Iterator<Message> iter = msgList.values().iterator();
		while (iter.hasNext()) {
			Message message = iter.next();
			try {
				message.doSave(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
