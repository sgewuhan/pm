package com.sg.business.model.bpmservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DocumentModelDefinition;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class MessageService extends AbstractMessageService {

	private static final String TITLE = "msg_title";
	private static final String CONTENT = "msg_content";
	private static final String RECEIVERS = "msg_receivers";
	private static final String EDITOR = "msg_editor";
	private static final String CLASSNAME = "msg_class";
	private static final String TARGET = "msg_target";

	@Override
	public String getMessageTitle() {
		try {
			return (String) getInputValue(TITLE);

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getMessageContent() {
		try {
			return (String) getInputValue(CONTENT);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> getReceiverList() {
		try {
			Object inputValue = getInputValue(RECEIVERS);
			System.out.println(1);
			if (inputValue instanceof String) {
				String receiverList = (String) getInputValue(RECEIVERS);
				String[] receivers = receiverList.split(",");
				return Arrays.asList(receivers);
			} else if (inputValue instanceof ArrayList<?>) {
				ArrayList<?> arrayList = (ArrayList<?>) inputValue;
				List<String> receivers = new ArrayList<String>();
				for (Object object : arrayList) {
					if (object instanceof String) {
						String string = (String) object;
						receivers.add(string);
					}
				}
				if (receivers != null && receivers.size() > 0) {
					return receivers;
				} else {
					return null;
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getEditorId() {
		try {
			return (String) getInputValue(EDITOR);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public PrimaryObject getTarget() {
		try {
			String target = (String) getInputValue(TARGET);
			String className = (String) getInputValue(CLASSNAME);
			DocumentModelDefinition documentModelDefinition = ModelService
					.getDocumentModelDefinition(className);
			Class<? extends PrimaryObject> class1 = documentModelDefinition
					.getModelClass();
			ObjectId _id = new ObjectId(target);
			return ModelService.createModelObject(class1, _id);
		} catch (Exception e) {
			return null;

		}
	}

}
