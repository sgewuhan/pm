package com.sg.business.model;

import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public interface IEditorInputFactory {

	PrimaryObjectEditorInput getInput(Object data);

}
