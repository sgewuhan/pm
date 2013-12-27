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

public class ProjectsDuplicateWorkOrder extends SingleDBCollectionDataSetFactory {

	public ProjectsDuplicateWorkOrder() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@Override
	public DataSet getDataSet() {
		DBCollection col = getCollection();
		DataSet ds = super.getDataSet();
		List<PrimaryObject> items = ds.getDataItems();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		for (int i = 0; i < items.size(); i++) {
			Project item = (Project) items.get(i);
			String[] ws = item.getWorkOrders();
			long cnt = col.count(new BasicDBObject().append(Project.F_WORK_ORDER,
					new BasicDBObject().append("$in", ws)));// ¹¤×÷ÁîºÅÖØ¸´
			if(cnt>1&&!contains(result,item)){
				result.add(createItem(item));
			}
		}
		return new DataSet(result);
	}

	private PrimaryObject createItem(Project item) {
		List<PrimaryObject> products = item.getProduct();
		StringBuffer sb = new StringBuffer();
		if(products!=null){
			for (int i = 0; i < products.size(); i++) {
				sb.append(products.get(i));
				sb.append(" ,");
			}
		}
		item.setValue("product", sb.toString());
		
		String[] ws = item.getWorkOrders();
		sb = new StringBuffer();
		for (int i = 0; i < ws.length; i++) {
			sb.append(ws[i]);
			sb.append(" ,");
		}
		item.setValue("workorder", sb.toString());

		return item;
	}

	private boolean contains(List<PrimaryObject> result, Project item) {
		for (int i = 0; i < result.size(); i++) {
			if(result.get(i).get_id().equals(item.get_id())){
				return true;
			}
		}
		return false;
	}
}
