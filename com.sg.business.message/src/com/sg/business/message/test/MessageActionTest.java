package com.sg.business.message.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.Message;
import com.sg.widgets.part.CurrentAccountContext;

public class MessageActionTest extends PropertyTester {

	private static final String PROPERTY_ACTION = "action"; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Message) {
			Message message = (Message) receiver;
			if (property.equals(PROPERTY_ACTION)) {
				if (args[0].equals("markRead")) { //$NON-NLS-1$
					return !message.isRead(new CurrentAccountContext());
				} else if (args[0].equals("unmarkread")) { //$NON-NLS-1$
					return message.isRead(new CurrentAccountContext());

				} else if (args[0].equals("markstar")) { //$NON-NLS-1$
					return !message.isStar(new CurrentAccountContext());

				} else if (args[0].equals("unmarkstar")) { //$NON-NLS-1$
					return message.isStar(new CurrentAccountContext());

				}
			}

		}
		return true;
	}
}
