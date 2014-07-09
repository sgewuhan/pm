package com.sg.business.commons.field.action;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.sg.business.model.DummyModel;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;

public abstract class AbstractTableItemHandler implements IAddTableItemHandler {

	@Override
	public boolean addItem(BasicDBList inputData, AbstractFieldPart part) {

		PrimaryObject po = ModelService.createModelObject(DummyModel.class);
		try {
			DataObjectDialog dialog = DataObjectDialog.openDialog(po,
					getDataEditorId(), true, null);
			if (dialog.getReturnCode() == DataObjectDialog.OK) {
				inputData.add(po.get_data());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	protected abstract String getDataEditorId() ;

}
