package com.sg.business.model;

import java.util.ArrayList;
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
import com.mongodb.WriteResult;
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
	 * ͨ�ù�������,��������{@link #F_WORK_TYPE}��ֵ
	 */
	public static final int WORK_TYPE_GENERIC = 0;

	/**
	 * ������������,��������{@link #F_WORK_TYPE}��ֵ
	 */
	public static final int WORK_TYPE_STANDLONE = 1;

	/**
	 * ��Ŀģ�幤������,��������{@link #F_WORK_TYPE}��ֵ
	 */
	public static final int WORK_TYPE_PROJECT = 2;

	/**
	 * ������������ͣ� ����ʹ�� <br/>
	 * {@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE},
	 * {@link #WORK_TYPE_PROJECT}
	 */
	public static final String F_WORK_TYPE = "worktype";
	
	/**
	 * ����������ϼ���������
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * ������_id�ֶΣ����ڱ����������_id��ֵ
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * ������ʾͼ��
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
	}

	/**
	 * �ж��Ƿ�����¼���������
	 * 
	 * @return boolean
	 */
	public boolean hasChildrenWork() {
		DBObject condition = new BasicDBObject().append(F_PARENT_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				getClass(), condition);
		return dsf.getTotalCount() > 0;
	}

	/**
	 * �жϹ��������Ƿ�ΪժҪ��������
	 * 
	 * @return boolean
	 */
	public boolean isSummaryWork() {
		return hasChildrenWork();
	}

	/**
	 * ���ع���������WBS�еı��<br/>
	 * �㼶+���
	 * 
	 * @return String
	 */
	public String getWBSCode() {
		AbstractWork parent = (AbstractWork) getParent();
		if (parent == null) {
			return "1";
		} else {
			return parent.getWBSCode() + "." + (getSequance() + 1);
		}
	}

	/**
	 * ���ع���������ͬ���е����
	 * 
	 * @return int
	 */
	public int getSequance() {
		Object seq = getValue(F_SEQ);
		if (seq instanceof Integer) {
			return ((Integer) seq).intValue();
		}
		return -1;
	}

	/**
	 * �����ϼ�����
	 * 
	 * @return AbstractWork
	 */
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
	 * ���ع�������ĸ����˽�ɫ����
	 * 
	 * @param clas
	 * 
	 * @return T
	 */
	public <T extends PrimaryObject> T getChargerRoleDefinition(Class<T> clas) {
		ObjectId chargerRoleDefId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if (chargerRoleDefId != null) {
			return ModelService.createModelObject(clas, chargerRoleDefId);
		}
		return null;
	}

	/**
	 * ���ø����˽�ɫ
	 * 
	 * @param role
	 * @param context
	 * @throws Exception
	 */
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
		return -1;
	}

	/**
	 * ���������¼���������
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getChildrenWork() {
		ObjectId id = get_id();
		if (id != null) {
			DBObject condition = new BasicDBObject().append(F_PARENT_ID, id);
			DBObject sort = new SEQSorter().getBSON();
			StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
					getClass(), condition);
			dsf.setSort(sort);
			return dsf.getDataSet().getDataItems();
		} else {
			return new ArrayList<PrimaryObject>();
		}
	}

	/**
	 * ���沢���ù�����������
	 * 
	 * @param list
	 * @param context
	 * @throws Exception
	 */
	protected void doSaveAndResetSeq(List<PrimaryObject> list, IContext context)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			PrimaryObject item = list.get(i);
			item.setValue(F_SEQ, new Integer(i));
			item.doSave(context);
		}
	}

	/**
	 * ���󷽷����½��¼���������
	 * 
	 * @return AbstractWork
	 */
	public abstract AbstractWork makeChildWork();

	/**
	 * ���󷽷��������ϼ���������
	 * 
	 * @return PrimaryObject
	 */
	public abstract PrimaryObject getHoster();

	/**
	 * ���󷽷����½���������Ľ����ﶨ��
	 * 
	 * @return PrimaryObject
	 */
	public abstract PrimaryObject makeDeliverableDefinition();

	/**
	 * ������������
	 * 
	 * @param context
	 * @return PrimaryObject[]
	 * @throws Exception
	 */
	public PrimaryObject[] doMoveDown(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObjectCache();
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

	/**
	 * ������������
	 * 
	 * @param context
	 * @return PrimaryObject[]
	 * @throws Exception
	 */
	public PrimaryObject[] doMoveUp(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObjectCache();
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

	/**
	 * ������������
	 * 
	 * @param context
	 * @return PrimaryObject[]
	 * @throws Exception
	 */
	public PrimaryObject[] doMoveLeft(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObjectCache();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}

		AbstractWork grandpa = (AbstractWork) parent.getParentPrimaryObjectCache();

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

	/**
	 * �������彵��
	 * 
	 * @param context
	 * @return PrimaryObject[]
	 * @throws Exception
	 */
	public PrimaryObject[] doMoveRight(IContext context) throws Exception {
		AbstractWork parent = (AbstractWork) getParentPrimaryObjectCache();
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

	/**
	 * �����������
	 * 
	 * @throws Exception
	 */
	public void doArrangeWBSCode() throws Exception {
		List<PrimaryObject> children = getChildrenWork();
		DBCollection col = getCollection();
		WriteResult ws;
		for (int i = 0; i < children.size(); i++) {
			AbstractWork child = (AbstractWork) children.get(i);
			ws = col.update(
					new BasicDBObject().append(F__ID, child.get_id()),
					new BasicDBObject().append("$set",
							new BasicDBObject().append(F_SEQ, new Integer(i))));
			checkWriteResult(ws);

			child.doArrangeWBSCode();
		}
	}
}
