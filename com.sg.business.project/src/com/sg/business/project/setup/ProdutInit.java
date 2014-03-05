package com.sg.business.project.setup;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
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

public class ProdutInit implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		DBCollection col = getCol();
		DBCollection productItemCol = getCol(IModelConstants.C_PRODUCT);
		BasicDBObject append = new BasicDBObject().append("_temp_itemcode", //$NON-NLS-1$
				new BasicDBObject().append("$ne", null)); //$NON-NLS-1$
		DBCursor cursor = col.find(append);
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			ObjectId project_id = (ObjectId) object.get(Project.F__ID);
			productItemCol.remove(new BasicDBObject().append(ProductItem.F_PROJECT_ID,
					project_id));
			BasicBSONList items = (BasicBSONList) object.get("_temp_itemcode"); //$NON-NLS-1$
			for (Object item : items) {
				// long count = productItemCol.count(new BasicDBObject().append(
				// ProductItem.F_PROJECT_ID, project_id).append(
				// ProductItem.F_DESC, item.toString()));
				// if (count == 0) {
				ProductItem productItem = ModelService
						.createModelObject(ProductItem.class);
				productItem.setValue(ProductItem.F_PROJECT_ID, project_id);
				productItem.setValue(ProductItem.F_DESC, item.toString());
				try {
					productItem.doSave(new CurrentAccountContext());
				} catch (Exception e) {
					throw e;
				}
				// }
			}
		}
		col.update(append, new BasicDBObject().append("$unset", //$NON-NLS-1$
				new BasicDBObject().append("_temp_itemcode", 1)), //$NON-NLS-1$
				false, true);

		return true;
	}

	private DBCollection getCol() {
		return DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
