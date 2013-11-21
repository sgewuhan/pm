package com.sg.business.project.setup;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.widgets.part.CurrentAccountContext;

public class ProdutInit implements Runnable {

	@Override
	public void run() {
		DBCollection col = getCol();
		BasicDBObject append = new BasicDBObject().append("_temp_itemcode",
				new BasicDBObject().append("$ne", null));
		DBCursor cursor = col.find(append);
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			ObjectId project_id = (ObjectId) object.get(Project.F__ID);
			BasicBSONList items = (BasicBSONList) object.get("_temp_itemcode");
			for (Object item : items) {
				ProductItem productItem = ModelService
						.createModelObject(ProductItem.class);
				productItem.setValue(ProductItem.F_PROJECT_ID, project_id);
				productItem.setValue(ProductItem.F_DESC, item.toString());
				try {
					productItem.doSave(new CurrentAccountContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		col.update(
				append,
				new BasicDBObject().append("$unset",
						new BasicDBObject().append("_temp_itemcode", 1)),
				false, true);
	}

	private DBCollection getCol() {
		return DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

}
