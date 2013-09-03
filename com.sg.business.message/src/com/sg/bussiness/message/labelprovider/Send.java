package com.sg.bussiness.message.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Message;

public class Send extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {

		if (element instanceof Message) {
			Message message = (Message) element;
			return message.getHTMLLabelForSend();
		}
		return "";

	}
}
