package com.sg.bussiness.message.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.message.nls.Messages;
import com.sg.business.model.Message;
import com.sg.widgets.part.CurrentAccountContext;

public class ReadMark extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof Message){
			Message message = (Message) element;
			Boolean isRead = message.isRead(new CurrentAccountContext());
			return isRead?Messages.get().ReadMark_0:""; //$NON-NLS-2$
		}
		return ""; //$NON-NLS-1$
	}

	
}
