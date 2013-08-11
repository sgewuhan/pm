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

public class WorkDefinition extends PrimaryObject {

	/**
	 * ͨ�ù�������
	 */
	public static final int WORK_TYPE_GENERIC = 0;

	/**
	 * ������������
	 */
	public static final int WORK_TYPE_STANDLONE = 1;

	/**
	 * ��Ŀģ�幤������
	 */
	public static final int WORK_TYPE_PROJECT = 2;

	/**
	 * ������������ͣ� ����ʹ��
	 * <p>
	 * {@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE},
	 * {@link #WORK_TYPE_PROJECT}
	 */
	public static final String F_WORK_TYPE = "worktype";
	/**
	 * ֻ����ͨ�ù�������Ͷ�����������
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * ֻ������Ŀģ�幤������
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

	/**
	 * ����������ϼ���������
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * ���������Ƿ񼤻��ʹ�ã�ֻ����{@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE}
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * ��������ĸ����ɫ���壬{@link RoleDefinition},�����˽�ɫ�����Id
	 */
	public static final String F_CHARGER_ROLE_ID = "charger_roled_id";

	/**
	 * ���������ͬ�����
	 */
	public static final String F_SEQ = "seq";

	/**
	 * ͨ�ù�������ı༭��Id
	 */
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition";

	/**
	 * ������������ı༭��Id
	 */
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition";

	/**
	 * ��Ŀģ�幤������ı༭��Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
	}


	/**
	 * �����ӹ����������
	 * 
	 * @return
	 */
	public WorkDefinition makeChildWorkDefinition() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq));
		data.put(F_PROJECTTEMPLATE_ID, getValue(F_PROJECTTEMPLATE_ID));
		data.put(F_WORK_TYPE, getValue(F_WORK_TYPE));
		
		WorkDefinition po = ModelService.createModelObject(data,
				WorkDefinition.class);
		return po;

	}

	public List<PrimaryObject> getChildrenWorkDefinition() {
		DBObject condition = new BasicDBObject().append(F_PARENT_ID, get_id());
		DBObject sort = new SEQSorter().getBSON();
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				WorkDefinition.class, condition);
		dsf.setSort(sort);
		return dsf.getDataSet().getDataItems();
	}

	public PrimaryObject[] doMoveDown(IContext context) throws Exception {
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}

		List<PrimaryObject> children = parent.getChildrenWorkDefinition();
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
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}
		List<PrimaryObject> children = parent.getChildrenWorkDefinition();
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
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}

		WorkDefinition grandpa = (WorkDefinition) parent
				.getParentPrimaryObject();

		List<PrimaryObject> thisChildren = getChildrenWorkDefinition();

		List<PrimaryObject> parentChildren = parent.getChildrenWorkDefinition();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�����Ľڵ�");

		if (grandpa == null) {
			throw new Exception("�ڵ��Ѿ���������");
		}

		// 1 �Լ����������е��ֵܱ���Լ��Ķ��ӣ����Լ������һ�����ӿ�ʼ���
		for (int i = index + 1; i < parentChildren.size(); i++) {
			WorkDefinition brother = (WorkDefinition) parentChildren.get(i);
			brother.setValue(F_PARENT_ID, get_id());
			thisChildren.add(brother);
		}
		// ������ӵ����
		doSaveAndResetSeq(thisChildren, context);

		// 2 �游����Լ��ĸ���ȡ���游���¼���ø����ڵ�λ�ã�,���뵽�������λ��
		setValue(F_PARENT_ID, grandpa.get_id());
		List<PrimaryObject> grandpaChildren = grandpa
				.getChildrenWorkDefinition();
		index = grandpaChildren.indexOf(parent);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�ƶ��ĸ��ڵ�");
		grandpaChildren.add(index + 1, this);
		doSaveAndResetSeq(grandpaChildren, context);
		return new PrimaryObject[] { this, parent, grandpa };
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

	public int getSequance() {
		Object seq = getValue(F_SEQ);
		if (seq instanceof Integer) {
			return ((Integer) seq).intValue();
		}
		return -1;
	}

	public PrimaryObject[] doMoveRight(IContext context) throws Exception {
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("�������ƶ�����Ĺ���");
		}
		List<PrimaryObject> parentChildren = parent.getChildrenWorkDefinition();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "���������޷���λ��Ҫ�����Ľڵ�");

		// ������ֵܵ����һ�����ӣ��Ӹ����Ƴ��Լ�
		if (index == 0) {
			throw new Exception("�ڵ��Ѿ����ܽ���");
		}
		WorkDefinition upperBrother = (WorkDefinition) parentChildren
				.get(index - 1);
		List<PrimaryObject> upperBrotherChildren = upperBrother
				.getChildrenWorkDefinition();
		upperBrotherChildren.add(this);
		setValue(F_PARENT_ID, upperBrother.get_id());

		parentChildren.remove(index);
		doSaveAndResetSeq(parentChildren, context);
		doSaveAndResetSeq(upperBrotherChildren, context);

		return new PrimaryObject[] { parent, upperBrother };
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		super.doRemove(context);
		WorkDefinition parent = getParent();
		Assert.isNotNull(parent);

		List<PrimaryObject> children = getChildrenWorkDefinition();
		doSaveAndResetSeq(children, context);
	}

	public WorkDefinition getParent() {
		ObjectId parent_id = (ObjectId) getValue(F_PARENT_ID);
		if (parent_id != null) {
			return ModelService.createModelObject(WorkDefinition.class,
					parent_id);
		}
		return null;
	}

	private void doSaveAndResetSeq(List<PrimaryObject> list, IContext context)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			PrimaryObject item = list.get(i);
			item.setValue(F_SEQ, new Integer(i));
			item.doSave(context);
		}
	}

	public String getWBSCode() {
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			return "1";
		} else {
			return parent.getWBSCode() + "." + (getSequance() + 1);
		}
	}

	public void doSetChargerAssignmentRole(RoleDefinition roled,
			IContext context) throws Exception {
		setValue(F_CHARGER_ROLE_ID, roled.get_id());
		doSave(context);
	}

	/**
	 * ��ȡ����������������Ŀģ��
	 * @return
	 */
	public ProjectTemplate getProjectTemplate() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECTTEMPLATE_ID);
		if(ptId!=null){
			return ModelService.createModelObject(ProjectTemplate.class, ptId);
		}else{
			return null;
		}
	}


	/**
	 * ��øù�������ĸ����˽�ɫ����
	 * @return
	 */
	public RoleDefinition getChargerRoleDefinition() {
		ObjectId chargerRoleDefId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if(chargerRoleDefId!=null){
			return ModelService.createModelObject(RoleDefinition.class, chargerRoleDefId);
		}
		return null;
	}

}
