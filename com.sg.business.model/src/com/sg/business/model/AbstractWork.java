package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.StructuredDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.resource.BusinessResource;

/**
 * <p>
 * 工作定义
 * <p/>
 * 工作定义包括三个类别：项目工作定义，通用工作定义，独立工作定义 <br/>
 * 工作定义用于描述项目模板中的工作分解结构
 * 
 * @author zhong hua
 * 
 */
public abstract class AbstractWork extends AbstractOptionFilterable implements
		IWorkCloneFields {
	/**
	 * 工作定义的上级工作定义
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * 根工作_id字段，用于保存根工作的_id的值
	 */
	public static final String F_ROOT_ID = "root_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
	}

	public boolean hasChildrenWork() {
		DBObject condition = new BasicDBObject().append(F_PARENT_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				getClass(), condition);
		return dsf.getTotalCount() > 0;
	}

	public boolean isSummaryWork() {
		return hasChildrenWork();
	}

	public String getWBSCode() {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			return "1";
		} else {
			return parent.getWBSCode() + "." + (getSequance() + 1);
		}
	}

	public int getSequance() {
		Object seq = getValue(F_SEQ);
		if (seq instanceof Integer) {
			return ((Integer) seq).intValue();
		}
		return -1;
	}

	public AbstractWork getParent() {
		ObjectId parent_id = (ObjectId) getValue(F_PARENT_ID);
		if (parent_id != null) {
			return ModelService.createModelObject(getClass(), parent_id);
		}
		return null;
	}

	/**
	 * 取出根工作定义
	 * 
	 * @return
	 */
	public AbstractWork getRoot() {
		ObjectId rootId = (ObjectId) getValue(F_ROOT_ID);
		if (rootId == null) {
			AbstractWork parent = getParent();
			if (parent == null) {
				return this;
			} else {
				return parent.getRoot();
			}
		} else {
			return ModelService.createModelObject(getClass(), rootId);
		}
	}

	/**
	 * 获得该工作定义的负责人角色定义
	 * 
	 * @param clas
	 * 
	 * @return
	 */
	public <T extends PrimaryObject> T getChargerRoleDefinition(Class<T> clas) {
		ObjectId chargerRoleDefId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if (chargerRoleDefId != null) {
			return ModelService.createModelObject(clas, chargerRoleDefId);
		}
		return null;
	}

	public void doSetChargerAssignmentRole(PrimaryObject role, IContext context)
			throws Exception {
		setValue(F_CHARGER_ROLE_ID, role.get_id());
		doSave(context);
	}

	/**
	 * 取得子工作的最大序号
	 * 
	 * @return
	 */
	public int getMaxChildSeq() {
		DBCollection col = getCollection();
		DBCursor cur = col.find(
				new BasicDBObject().append(F_PARENT_ID, get_id()),
				new BasicDBObject().append(F_SEQ, 1));
		cur.sort(new SEQSorter(-1).getBSON());
		if (cur.hasNext()) {
			Object seq = cur.next().get(F_SEQ);
			if (seq instanceof Integer) {
				return ((Integer) seq).intValue();
			}
		}
		return 0;
	}

	public List<PrimaryObject> getChildrenWork() {
		DBObject condition = new BasicDBObject().append(F_PARENT_ID, get_id());
		DBObject sort = new SEQSorter().getBSON();
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				getClass(), condition);
		dsf.setSort(sort);
		return dsf.getDataSet().getDataItems();
	}

	protected void doSaveAndResetSeq(List<PrimaryObject> list, IContext context)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			PrimaryObject item = list.get(i);
			item.setValue(F_SEQ, new Integer(i));
			item.doSave(context);
		}
	}

	public abstract AbstractWork makeChildWork();

	public abstract PrimaryObject getHoster();

	public abstract PrimaryObject makeDeliverableDefinition();

	public PrimaryObject[] doMoveDown(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}

		List<PrimaryObject> children = parent.getChildrenWork();
		int index = children.indexOf(this);
		Assert.isTrue(index != -1, "下移出错，无法定位将要移动的节点");

		if ((index + 1) >= children.size()) {
			throw new Exception("已经是本层的最后一个");
		}
		children.remove(index);
		children.add(index + 1, this);
		doSaveAndResetSeq(children, context);
		return new PrimaryObject[] { parent };
	}

	public PrimaryObject[] doMoveUp(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}
		List<PrimaryObject> children = parent.getChildrenWork();
		int index = children.indexOf(this);
		Assert.isTrue(index != -1, "上移出错，无法定位将要移动的节点");

		if (index == 0) {
			throw new Exception("已经是本层的第一个");
		}
		children.remove(index);
		children.add(index - 1, this);
		doSaveAndResetSeq(children, context);

		return new PrimaryObject[] { parent };

	}

	public PrimaryObject[] doMoveLeft(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}

		AbstractWork grandpa = (AbstractWork) parent.getParentPrimaryObject();

		List<PrimaryObject> thisChildren = getChildrenWork();

		List<PrimaryObject> parentChildren = parent.getChildrenWork();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "升级出错，无法定位将要升级的节点");

		if (grandpa == null) {
			throw new Exception("节点已经不能升级");
		}

		// 1 自己的下面所有的兄弟变成自己的儿子，从自己的最后一个儿子开始添加
		for (int i = index + 1; i < parentChildren.size(); i++) {
			AbstractWork brother = (AbstractWork) parentChildren.get(i);
			brother.setValue(F_PARENT_ID, get_id());
			thisChildren.add(brother);
		}
		// 整理儿子的序号
		doSaveAndResetSeq(thisChildren, context);

		// 2 祖父变成自己的父，取出祖父的下级获得父所在的位置，,插入到父下面的位置
		setValue(F_PARENT_ID, grandpa.get_id());
		List<PrimaryObject> grandpaChildren = grandpa.getChildrenWork();
		index = grandpaChildren.indexOf(parent);
		Assert.isTrue(index != -1, "升级出错，无法定位将要移动的父节点");
		grandpaChildren.add(index + 1, this);
		doSaveAndResetSeq(grandpaChildren, context);
		return new PrimaryObject[] { this, parent, grandpa };
	}

	public PrimaryObject[] doMoveRight(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}
		List<PrimaryObject> parentChildren = parent.getChildrenWork();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "降级出错，无法定位将要降级的节点");

		// 变成上兄弟的最后一个儿子，从父中移除自己
		if (index == 0) {
			throw new Exception("节点已经不能降级");
		}
		AbstractWork upperBrother = (AbstractWork) parentChildren
				.get(index - 1);
		List<PrimaryObject> upperBrotherChildren = upperBrother
				.getChildrenWork();
		upperBrotherChildren.add(this);
		setValue(F_PARENT_ID, upperBrother.get_id());

		parentChildren.remove(index);
		doSaveAndResetSeq(parentChildren, context);
		doSaveAndResetSeq(upperBrotherChildren, context);

		return new PrimaryObject[] { parent, upperBrother };
	}
}
