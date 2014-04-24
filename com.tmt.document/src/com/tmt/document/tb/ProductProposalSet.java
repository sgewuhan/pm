package com.tmt.document.tb;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProductProposalSet extends MasterDetailDataSetFactory {

	private static final String PRODUCT = "product";

	public ProductProposalSet() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof Document) {
				Document document = (Document) master;
				List<PrimaryObject> productList = new ArrayList<PrimaryObject>();
				List<DBObject> product = (List<DBObject>) document
						.getListValue(PRODUCT);
				for (DBObject object : product) {
					PrimaryObject po = ModelService.createModelObject(object, PrimaryObject.class);
					productList.add(po);
				}

				return new DataSet(productList);
			}
		}
		return super.getDataSet();
	}

}
