package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class BudgetItem extends PrimaryObject {

	public static final String F_CHILDREN = "children";
	public static final String F_ISDEFAULT = "isdefault";
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

	public static BudgetItem GET_DEFAULT_BUDGET_ITEM() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_BUDGET_ITEM);
		DBObject data = col.findOne(new BasicDBObject().append(F_ISDEFAULT,
				Boolean.TRUE));
		if (data == null) {
			data = new BasicDBObject().append(F_ISDEFAULT, Boolean.TRUE)
					.append(F__ID, new ObjectId()).append(F_DESC, "ƒ¨»œ‘§À„");
			col.insert(data);
		}
		return ModelService.createModelObject(data, BudgetItem.class);
	}

	public static BudgetItem COPY_DEFAULT_BUDGET_ITEM() {
		BudgetItem defaultBi = GET_DEFAULT_BUDGET_ITEM();
		DBObject data = new BasicDBObject();
		data.put(F_DESC, defaultBi.getValue(F_DESC));
		data.put(F_DESC_EN, defaultBi.getValue(F_DESC_EN));
		data.put(F_CHILDREN, defaultBi.getValue(F_CHILDREN));
		return ModelService.createModelObject(data, BudgetItem.class);
	}
	
	public static PrimaryObject COPY_DEFAULT_BUDGET_ITEM(Class<? extends PrimaryObject> t) {
		BudgetItem defaultBi = GET_DEFAULT_BUDGET_ITEM();
		DBObject data = new BasicDBObject();
		data.put(F_DESC, defaultBi.getValue(F_DESC));
		data.put(F_DESC_EN, defaultBi.getValue(F_DESC_EN));
		data.put(F_CHILDREN, defaultBi.getValue(F_CHILDREN));
		return ModelService.createModelObject(data, t);
	}

	private BudgetItem parent;

	public BudgetItem[] getChildren() {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData != null) {
			BudgetItem[] result = new BudgetItem[childrenData.size()];
			for (int i = 0; i < childrenData.size(); i++) {
				result[i] = ModelService.createModelObject(
						(DBObject) childrenData.get(i), BudgetItem.class);
				result[i].setParent(this);
			}
			return result;
		} else {
			return new BudgetItem[0];
		}
	}

	private void setParent(BudgetItem budgetItem) {
		this.parent = budgetItem;
	}

	public BudgetItem getParent() {
		return parent;
	}

	public boolean hasChildren() {

		return getChildren().length > 0;
	}

	public void createChild(String budgetItemName) {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData == null) {
			childrenData = new BasicDBList();
		}
		childrenData.add(new BasicDBObject().append(F_DESC, budgetItemName)
				.append(F__ID, new ObjectId()));
		setValue(F_CHILDREN, childrenData);
	}

	public void createChild(BudgetItem srcPo, int index) {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData == null) {
			childrenData = new BasicDBList();
		}
		childrenData.add(index, srcPo.get_data());
		setValue(F_CHILDREN, childrenData);
	}

	public void removeChild(BudgetItem budgetItem) {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData != null) {
			for (int i = 0; i < childrenData.size(); i++) {
				DBObject child = (DBObject) (childrenData.get(i));
				if (child.get(F__ID).equals(budgetItem.get_id())) {
					childrenData.remove(child);
					return;
				}
			}

		}
	}

	public void editBudgetItem(String budgetItemName) {
		this.setValue(F_DESC, budgetItemName);
	}

}
