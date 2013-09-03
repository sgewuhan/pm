package com.sg.bussiness.message.handler;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.sg.business.model.Message;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;

public class SendToSelectedUser extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
      IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
      if(selection==null||selection.isEmpty()){
    	  MessageUtil.showToast("请选中用户", SWT.ICON_WARNING);
    	  return;
      }	
      Message message = ModelService.createModelObject(Message.class);
      BasicDBList recieverList=new BasicDBList();
      
      Iterator<?> iter = selection.iterator();
	  while(iter.hasNext()){
		  User user=(User) iter.next();
		  recieverList.add(user.getUserid());
	  }
	  
	  message.setValue(Message.F_RECIEVER, recieverList);
	  try {
		DataObjectEditor.open(message,Message.EDITOR_SEND, true, null);
	} catch (Exception e) {
		MessageUtil.showToast(e);
		e.printStackTrace();
	}
	}

}
