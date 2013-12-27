package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;

public class ProjectsDuplicateProductNumber extends SingleDBCollectionDataSetFactory {

	public ProjectsDuplicateProductNumber() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DataSet getDataSet() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_PRODUCT);
		DBCursor cur = col.find();
		Set projectIds = new HashSet();
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			Object pn = dbo.get(ProductItem.F_DESC);
			DBCursor c = col.find(new BasicDBObject().append(ProductItem.F_DESC, pn));
			if(c.size()>1){
				while(c.hasNext()){
					DBObject d = c.next();
					Object projectId = d.get(ProductItem.F_PROJECT_ID);
					projectIds.add(projectId);
				}
			}
		}
		
		DBCollection pjcol = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_PROJECT);
		DBCursor c1 = pjcol.find(new BasicDBObject().append(Project.F__ID, new BasicDBObject().append("$in", projectIds.toArray())));
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		while(c1.hasNext()){
			DBObject dbo = c1.next();
			Project item = ModelService.createModelObject(dbo, Project.class);
			result.add(createItem(item));
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

//	private boolean contains(List<PrimaryObject> result, Project item) {
//		for (int i = 0; i < result.size(); i++) {
//			if(result.get(i).get_id().equals(item.get_id())){
//				return true;
//			}
//		}
//		return false;
//	}
}
