package com.sg.business.model.input;

import com.sg.business.model.BulletinBoard;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class BulletinBoardEditorInputFactory implements IEditorInputFactory {

	private BulletinBoard bulletinBoard;

	public BulletinBoardEditorInputFactory(BulletinBoard bulletinBoard) {
		this.bulletinBoard = bulletinBoard;
	}

	@Override
	public PrimaryObjectEditorInput getInput(Object data) {
		DataEditorConfigurator conf = getEditorConfig(data);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
				bulletinBoard, conf, null);
		return editorInput;
	}

	@Override
	public DataEditorConfigurator getEditorConfig(Object data) {
		String editorId;
		editorId = "bulletinboard.editor.create";
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		return conf;
	}

}
