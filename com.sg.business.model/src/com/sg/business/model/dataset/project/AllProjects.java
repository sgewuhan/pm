package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;

public class AllProjects extends SingleDBCollectionDataSetFactory {

	public AllProjects() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		setSort(new BasicDBObject().append(PrimaryObject.F__ID, 1));
	}

	@Override
	public DataSet getDataSet() {
		DataSet ds = super.getDataSet();
		List<PrimaryObject> items = ds.getDataItems();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		for (int i = 0; i < items.size(); i++) {
			Project item = (Project) items.get(i);
			result.add(createItem(item));
		}
		return new DataSet(result);
	}

	private PrimaryObject createItem(Project item) {
		List<PrimaryObject> products = item.getProduct();
		StringBuffer sb = new StringBuffer();
		if (products != null) {
			for (int i = 0; i < products.size(); i++) {
				sb.append(products.get(i));
				sb.append(" ,");
			}
		}
		item.setValue("product_text", sb.toString());

		String[] ws = item.getWorkOrders();
		sb = new StringBuffer();
		for (int i = 0; i < ws.length; i++) {
			sb.append(ws[i]);
			sb.append(" ,");
		}
		item.setValue("workorder_text", sb.toString());

		return item;
	}
}