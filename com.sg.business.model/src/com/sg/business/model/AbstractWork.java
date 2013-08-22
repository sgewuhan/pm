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
 * ��������
 * <p/>
 * ��������������������Ŀ�������壬ͨ�ù������壬������������ <br/>
 * ������������������Ŀģ���еĹ����ֽ�ṹ
 * 
 * @author zhong hua
 * 
 */
public abstract class AbstractWork extends AbstractOptionFilterable implements
		IWorkCloneFields {
	/**
	 * ����������ϼ���������
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * ������_id�ֶΣ����ڱ����������_id��ֵ
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
	 * ȡ������������
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
	 * ��øù�������ĸ����˽�ɫ����
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
	 * ȡ���ӹ�����������
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
			throw new Exception("�������ƶ�����Ĺ���");
		}

		List<PrimaryObject> children = parent.getChildrenWork();
		int index = children.indexOf(this);
		Assert.isTrue(index != -1, "���Ƴ����޷���λ��Ҫ�ƶ��Ľڵ�");

		if ((index + 1) >= children.size()) {
			throw new Exception("�Ѿ��Ǳ�������һ��");
		}
		children.remove(index);
		children.add(index + 1, this);
		doSaveAndResetSeq(children, context);
		return new PrimaryObject[] { parent };
	}

	public PrimaryObject[] doMoveUp(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}
		List<PrimaryObject> children = parent.getChildrenWork();
		int index = children.indexOf(this);
		Assert.isTrue(index != -1, "���Ƴ����޷���λ��Ҫ�ƶ��Ľڵ�");

		if (index == 0) {
			throw new Exception("�Ѿ��Ǳ���ĵ�һ��");
		}
		children.remove(index);
		children.add(index - 1, this);
		doSaveAndResetSeq(children, context);

		return new PrimaryObject[] { parent };

	}

	public PrimaryObject[] doMoveLeft(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}

		AbstractWork grandpa = (AbstractWork) parent.getParentPrimaryObject();

		List<PrimaryObject> thisChildren = getChildrenWork();

		List<PrimaryObject> parentChildren = parent.getChildrenWork();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�����Ľڵ�");

		if (grandpa == null) {
			throw new Exception("�ڵ��Ѿ���������");
		}

		// 1 �Լ����������е��ֵܱ���Լ��Ķ��ӣ����Լ������һ�����ӿ�ʼ���
		for (int i = index + 1; i < parentChildren.size(); i++) {
			AbstractWork brother = (AbstractWork) parentChildren.get(i);
			brother.setValue(F_PARENT_ID, get_id());
			thisChildren.add(brother);
		}
		// ������ӵ����
		doSaveAndResetSeq(thisChildren, context);

		// 2 �游����Լ��ĸ���ȡ���游���¼���ø����ڵ�λ�ã�,���뵽�������λ��
		setValue(F_PARENT_ID, grandpa.get_id());
		List<PrimaryObject> grandpaChildren = grandpa.getChildrenWork();
		index = grandpaChildren.indexOf(parent);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�ƶ��ĸ��ڵ�");
		grandpaChildren.add(index + 1, this);
		doSaveAndResetSeq(grandpaChildren, context);
		return new PrimaryObject[] { this, parent, grandpa };
	}

	public PrimaryObject[] doMoveRight(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}
		List<PrimaryObject> parentChildren = parent.getChildrenWork();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�����Ľڵ�");

		// ������ֵܵ����һ�����ӣ��Ӹ����Ƴ��Լ�
		if (index == 0) {
			throw new Exception("�ڵ��Ѿ����ܽ���");
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
