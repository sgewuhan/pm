package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

public class ProductItem extends PrimaryObject implements IProjectRelative {

	private static final String F_IS_FINAL = "isfinal";

	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	public boolean isFinal() {
		return Boolean.TRUE.equals(getValue(F_IS_FINAL));
	}

	public boolean canFinal() {
		boolean b = isFinal();
		return !b;
	}

	public void doFinal(IContext context) throws Exception {
		if (isFinal()) {
			return;
		}
		setValue(F_IS_FINAL, Boolean.TRUE);
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_IS_FINAL, Boolean.TRUE)));
		checkWriteResult(ws);
	}

	public ObjectId getProject_id() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	@Override
	public boolean canEdit(IContext context) {
		return canFinal();
	}
}
