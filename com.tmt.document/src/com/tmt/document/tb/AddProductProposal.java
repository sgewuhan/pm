package com.tmt.document.tb;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.sg.business.model.DummyModel;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;

public class AddProductProposal implements IAddTableItemHandler {

	private static final String DOCUMENT_PRODUCTPROPOSAL_PRODUCT = "document.productproposal.product";

	@Override
	public boolean addItem(BasicDBList inputData, AbstractFieldPart part) {

		PrimaryObject po = ModelService.createModelObject(DummyModel.class);
		try {
			DataObjectDialog dialog = DataObjectDialog.openDialog(po,
					DOCUMENT_PRODUCTPROPOSAL_PRODUCT, true, null);
			if (dialog.getReturnCode() == DataObjectDialog.OK) {
				inputData.add(po.get_data());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
