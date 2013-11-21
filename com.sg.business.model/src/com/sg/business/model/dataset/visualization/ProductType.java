package com.sg.business.model.dataset.visualization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.CurrentAccountContext;

public class ProductType extends SingleDBCollectionDataSetFactory {

	private String userId;
	public ProductType() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		 userId= new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems=new ArrayList<PrimaryObject>();
		List<String> options=getTypeOptions();
		for(String option:options){
			ProductTypeProvider projectType = new ProductTypeProvider(option,userId);
			dataItems.add(projectType);
		}
		return new DataSet(dataItems);
	}

	private List<String> getTypeOptions() {
		List<String> typeList = new ArrayList<String>();
		DBCollection collection = getCollection();
		DBCursor cur = collection.find();
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			ProjectTemplate template = ModelService.createModelObject(dbo,
					ProjectTemplate.class);
			Object value = template
					.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
			if (value instanceof List) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				for (Object obj : list) {
					if (!typeList.contains(obj)) {
						typeList.add((String) obj);
					}
				}

			}

		}
		return typeList;
	}

}
