package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.nls.Messages;

/**
 * 预算
 * @author jinxitao
 *
 */
public class BudgetItem extends PrimaryObject {

	/**
	 * 下级预算
	 */
	public static final String F_CHILDREN = "children"; //$NON-NLS-1$
	
	/**
	 * 默认预算，是最上级的预算
	 */
	public static final String F_ISDEFAULT = "isdefault"; //$NON-NLS-1$
	
	/**
	 * 项目模板_id字段
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id"; //$NON-NLS-1$

	/**
	 * 返回默认预算，默认预算为最上层预算。如果没有，则插入最上级预算，字段desc为默认预算
	 * @return BudgetItem
	 */
	public static BudgetItem GET_DEFAULT_BUDGET_ITEM() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_BUDGET_ITEM);
		DBObject data = col.findOne(new BasicDBObject().append(F_ISDEFAULT,
				Boolean.TRUE));
		if (data == null) {
			data = new BasicDBObject().append(F_ISDEFAULT, Boolean.TRUE)
					.append(F__ID, new ObjectId()).append(F_DESC, Messages.get().BudgetItem_3);
			col.insert(data);
		}
		return ModelService.createModelObject(data, BudgetItem.class);
	}

	/**
	 * 复制默认预算，返回复制后的对象
	 * @return BudgetItem
	 */
	public static BudgetItem COPY_DEFAULT_BUDGET_ITEM() {
		BudgetItem defaultBi = GET_DEFAULT_BUDGET_ITEM();
		DBObject data = new BasicDBObject();
		data.put(F_DESC, defaultBi.getValue(F_DESC));
		data.put(F_DESC_EN, defaultBi.getValue(F_DESC_EN));
		data.put(F_CHILDREN, defaultBi.getValue(F_CHILDREN));
		return ModelService.createModelObject(data, BudgetItem.class);
	}

	/**
	 * 传入PrimaryObject对象，复制默认预算到传递的对象中并返回。
	 * @param t
	 * @return
	 */
	public static PrimaryObject COPY_DEFAULT_BUDGET_ITEM(
			Class<? extends PrimaryObject> t) {
		BudgetItem defaultBi = GET_DEFAULT_BUDGET_ITEM();
		DBObject data = new BasicDBObject();
		data.put(F_DESC, defaultBi.getValue(F_DESC));
		data.put(F_DESC_EN, defaultBi.getValue(F_DESC_EN));
		data.put(F_CHILDREN, defaultBi.getValue(F_CHILDREN));
		return ModelService.createModelObject(data, t);
	}

	/**
	 * 上级预算
	 */
	private BudgetItem parent;

	/**
	 * 返回下级预算
	 * @return
	 */
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

	/**
	 * 传入预算对象，设置为自身的上级预算
	 * @param budgetItem
	 */
	private void setParent(BudgetItem budgetItem) {
		this.parent = budgetItem;
	}

	/**
	 * 返回上级预算
	 * @return BudgetItem
	 */
	public BudgetItem getParent() {
		return parent;
	}

	/**
	 * 判断是否具有下级预算
	 * @return boolean
	 */
	public boolean hasChildren() {

		return getChildren().length > 0;
	}

	/**
	 * 传入预算名称，新建下级预算
	 * @param budgetItemName
	 *            ,预算名称
	 */
	public void createChild(String budgetItemName) {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData == null) {
			childrenData = new BasicDBList();
		}
		childrenData.add(new BasicDBObject().append(F_DESC, budgetItemName)
				.append(F__ID, new ObjectId()));
		setValue(F_CHILDREN, childrenData);
	}

	/**
	 * 
	 * @param srcPo
	 * @param index
	 */
	public void createChild(BudgetItem srcPo, int index) {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData == null) {
			childrenData = new BasicDBList();
		}
		childrenData.add(index, srcPo.get_data());
		setValue(F_CHILDREN, childrenData);
	}

	/**
	 * 传入下级预算，遍历所有下级预算，找到与参数相等的下级预算，并将其删除
	 * @param budgetItem
	 *            ,下级预算
	 */
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

	/**
	 * 编辑预算，修改预算名
	 * @param budgetItemName
	 *            ,预算名称
	 */
	public void editBudgetItem(String budgetItemName) {
		this.setValue(F_DESC, budgetItemName);
	}

	@Override
	public String getTypeName() {
		return Messages.get().BudgetItem_4;
	}
}
