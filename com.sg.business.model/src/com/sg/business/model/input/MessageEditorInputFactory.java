package com.sg.business.model.input;

import com.sg.business.model.IEditorInputFactory;
import com.sg.business.model.Message;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class MessageEditorInputFactory implements IEditorInputFactory {

	private Message message;

	public MessageEditorInputFactory(Message message) {
		this.message = message;
	}

	@Override
	public PrimaryObjectEditorInput getInput(Object data) {
		CurrentAccountContext context;
		try {
			context = new CurrentAccountContext();
			message.doMarkRead(context, Boolean.TRUE);
		} catch (Exception e) {
			return null;
		}
		String editorId = "message.editor.view";

		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
				message, conf, null);
		editorInput.setEditable(true);
		editorInput.setNeedHostPartListenSaveEvent(false);
		editorInput.setContext(context);
		return editorInput;
	}
}
