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
 * Ԥ��
 * @author jinxitao
 *
 */
public class BudgetItem extends PrimaryObject {

	/**
	 * �¼�Ԥ��
	 */
	public static final String F_CHILDREN = "children"; //$NON-NLS-1$
	
	/**
	 * Ĭ��Ԥ�㣬�����ϼ���Ԥ��
	 */
	public static final String F_ISDEFAULT = "isdefault"; //$NON-NLS-1$
	
	/**
	 * ��Ŀģ��_id�ֶ�
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id"; //$NON-NLS-1$

	/**
	 * ����Ĭ��Ԥ�㣬Ĭ��Ԥ��Ϊ���ϲ�Ԥ�㡣���û�У���������ϼ�Ԥ�㣬�ֶ�descΪĬ��Ԥ��
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
	 * ����Ĭ��Ԥ�㣬���ظ��ƺ�Ķ���
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
	 * ����PrimaryObject���󣬸���Ĭ��Ԥ�㵽���ݵĶ����в����ء�
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
	 * �ϼ�Ԥ��
	 */
	private BudgetItem parent;

	/**
	 * �����¼�Ԥ��
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
	 * ����Ԥ���������Ϊ������ϼ�Ԥ��
	 * @param budgetItem
	 */
	private void setParent(BudgetItem budgetItem) {
		this.parent = budgetItem;
	}

	/**
	 * �����ϼ�Ԥ��
	 * @return BudgetItem
	 */
	public BudgetItem getParent() {
		return parent;
	}

	/**
	 * �ж��Ƿ�����¼�Ԥ��
	 * @return boolean
	 */
	public boolean hasChildren() {

		return getChildren().length > 0;
	}

	/**
	 * ����Ԥ�����ƣ��½��¼�Ԥ��
	 * @param budgetItemName
	 *            ,Ԥ������
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
	 * �����¼�Ԥ�㣬���������¼�Ԥ�㣬�ҵ��������ȵ��¼�Ԥ�㣬������ɾ��
	 * @param budgetItem
	 *            ,�¼�Ԥ��
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
	 * �༭Ԥ�㣬�޸�Ԥ����
	 * @param budgetItemName
	 *            ,Ԥ������
	 */
	public void editBudgetItem(String budgetItemName) {
		this.setValue(F_DESC, budgetItemName);
	}

	@Override
	public String getTypeName() {
		return Messages.get().BudgetItem_4;
	}
}
