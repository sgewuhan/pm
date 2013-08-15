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
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.resource.BusinessResource;

public class WorkDefinition extends AbstractOptionFilterable {


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
	 * ��Ŀģ�幤������ͽ����ﶨ��
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	/**
	 * ֻ����ͨ�ù�������Ͷ�����������
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

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
	 * �е��߽�ɫ����
	 */
	public static final String F_PARTICIPATE_ROLE_SET = "participate_roled_set";

	
	/**
	 * ���������ͬ�����
	 */
	public static final String F_SEQ = "seq";

	/**
	 * ͨ�ù�������ı༭��Id
	 */
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition.1";
	
	public static final String EDITOR_GENERIC_WORK_ROOT =  "editor.genericWorkDefinition";

	/**
	 * ������������ı༭��Id
	 */
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition.1";

	public static final String EDITOR_STANDLONE_WORK_ROOT = "editor.standloneWorkDefinition";

	/**
	 * ��Ŀģ�幤������ı༭��Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition";

	public static final String F_ROOT_ID = "root_id";

	public static final String F_WF_EXECUTE = "wf_execute";

	public static final String F_WF_CHANGE = "wf_change";


	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
	}

	public int getWorkDefinitionType() {
		Object value = getValue(F_WORK_TYPE);
		Assert.isTrue(value instanceof Integer);
		return ((Integer) value).intValue();
	}

	/**
	 * �����ӹ����������
	 * 
	 * @return
	 */
	public WorkDefinition makeChildWorkDefinition() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		data.put(WorkDefinition.F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		// ��Բ�ͬ���͵Ĺ��������Ԥ����
		int type = getWorkDefinitionType();
		switch (type) {
		case WORK_TYPE_GENERIC:// ͨ�ù�������Ͷ�������������Ҫ�趨��֯Id
		case WORK_TYPE_STANDLONE:
			data.put(F_ORGANIZATION_ID, getValue(F_ORGANIZATION_ID));
			break;
		case WORK_TYPE_PROJECT:// ��Ŀ����������Ҫ�趨��Ŀģ��Id
			data.put(F_PROJECT_TEMPLATE_ID, getValue(F_PROJECT_TEMPLATE_ID));

			break;
		default:
			break;
		}
		data.put(F_WORK_TYPE, new Integer(type));

		WorkDefinition po = ModelService.createModelObject(data,
				WorkDefinition.class);
		return po;

	}

	public DeliverableDefinition makeDeliverableDefinition() {
		return makeDeliverableDefinition(null);
	}

	public DeliverableDefinition makeDeliverableDefinition(
			DocumentDefinition docd) {
		DBObject data = new BasicDBObject();
		data.put(DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());

		// ��Բ�ͬ���͵Ĺ��������Ԥ����
		int type = getWorkDefinitionType();
		switch (type) {
		case WORK_TYPE_GENERIC:// ͨ�ù�������Ͷ�������������Ҫ�趨��֯Id
		case WORK_TYPE_STANDLONE:
			data.put(DeliverableDefinition.F_ORGANIZATION_ID,
					getValue(F_ORGANIZATION_ID));
			break;
		case WORK_TYPE_PROJECT:// ��Ŀ����������Ҫ�趨��Ŀģ��Id
			data.put(DeliverableDefinition.F_PROJECTTEMPLATE_ID,
					getValue(F_PROJECT_TEMPLATE_ID));
			break;
		default:
			break;
		}

		data.put(F_WORK_TYPE, new Integer(type));

		if (docd != null) {
			data.put(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
					docd.get_id());
			data.put(DeliverableDefinition.F_DESC, docd.getDesc());
		}

		DeliverableDefinition po = ModelService.createModelObject(data,
				DeliverableDefinition.class);

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

	public List<PrimaryObject> getDeliverableDefinitions() {
		DBObject condition = new BasicDBObject().append(
				DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				DeliverableDefinition.class, condition);
		return dsf.getDataSet().getDataItems();
	}

	public boolean isSummaryWorkDefinition() {
		return hasChildrenWorkDefinition();
	}

	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}

	public boolean hasChildrenWorkDefinition() {
		DBObject condition = new BasicDBObject().append(F_PARENT_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				WorkDefinition.class, condition);
		return dsf.getTotalCount() > 0;
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
		int type = getWorkDefinitionType();
		if ((type == WORK_TYPE_GENERIC || type == WORK_TYPE_STANDLONE)
				&& isActivated()) {
			throw new Exception("�������崦������״̬������ɾ��");
		}

		// ɾ��ǰ��˳���ϵ
		List<PrimaryObject> connections = getEnd2Connections();
		for (PrimaryObject primaryObject : connections) {
			primaryObject.doRemove(context);
		}
		connections = getEnd2Connections();
		for (PrimaryObject primaryObject : connections) {
			primaryObject.doRemove(context);
		}

		// ɾ�������ﶨ��
		List<PrimaryObject> deliverableDefinitions = getDeliverableDefinitions();
		for (PrimaryObject primaryObject : deliverableDefinitions) {
			primaryObject.doRemove(context);
		}

		// ɾ���¼�
		List<PrimaryObject> childrenWorkDefinitions = getChildrenWorkDefinition();
		for (PrimaryObject primaryObject : childrenWorkDefinitions) {
			primaryObject.doRemove(context);
		}
		// ɾ���Լ�
		WorkDefinition parent = getParent();

		super.doRemove(context);

		if (parent != null) {

			// ��ƽ���������������
			List<PrimaryObject> children = parent.getChildrenWorkDefinition();
			doSaveAndResetSeq(children, context);
		}

	}

	public List<PrimaryObject> getEnd2Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END1_ID,
				WorkDefinitionConnection.class);
	}

	public List<PrimaryObject> getEnd1Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END2_ID,
				WorkDefinitionConnection.class);
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
	 * 
	 * @return
	 */
	public ProjectTemplate getProjectTemplate() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
		if (ptId != null) {
			return ModelService.createModelObject(ProjectTemplate.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * ��øù�������ĸ����˽�ɫ����
	 * 
	 * @return
	 */
	public RoleDefinition getChargerRoleDefinition() {
		ObjectId chargerRoleDefId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if (chargerRoleDefId != null) {
			return ModelService.createModelObject(RoleDefinition.class,
					chargerRoleDefId);
		}
		return null;
	}

	/**
	 * ����һ��ͨ�ù�������
	 * 
	 * @param genericWorkDefinition
	 * @param context
	 * @throws Exception
	 */
	public void doImportGenericWorkDefinition(
			WorkDefinition genericWorkDefinition, IContext context)
			throws Exception {
		doClone(genericWorkDefinition, context);
	}

	/**
	 * ����һ��ͨ�ù�������
	 * 
	 * @param workd
	 * @param context
	 * @throws Exception
	 */
	public void doExportGenericWorkDefinition(
			WorkDefinition genericWorkDefinition, IContext context)
			throws Exception {
		genericWorkDefinition.doClone(this, context);

	}

	/**
	 * ���ƴ���
	 * 
	 * @param srcWorkDefinition
	 * @param context
	 * @throws Exception
	 */
	public void doClone(WorkDefinition srcWorkDefinition, IContext context)
			throws Exception {
		if (srcWorkDefinition == null) {
			throw new IllegalArgumentException("Դ����Ϊ��");
		}

		// �����ӹ�������
		WorkDefinition child = makeChildWorkDefinition();
		child.setValue(F_DESC, srcWorkDefinition.getValue(F_DESC));
		child.doSave(context);

		// ��ȡ�����ﶨ��
		List<PrimaryObject> srcDeliverableDefinitions = srcWorkDefinition
				.getDeliverableDefinitions();
		for (PrimaryObject po : srcDeliverableDefinitions) {
			DeliverableDefinition srcDeliverableDefinition = (DeliverableDefinition) po;
			DeliverableDefinition childDeliverable = child
					.makeDeliverableDefinition();
			// �����ĵ�ģ���Id
			childDeliverable
					.setValue(
							DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
							srcDeliverableDefinition
									.getValue(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID));
			childDeliverable.setValue(DeliverableDefinition.F_DESC,
					srcDeliverableDefinition
							.getValue(DeliverableDefinition.F_DESC));
			childDeliverable.doSave(context);
		}

		// �����ӹ���
		List<PrimaryObject> sourceChildren = srcWorkDefinition
				.getChildrenWorkDefinition();
		for (PrimaryObject po : sourceChildren) {
			child.doClone((WorkDefinition) po, context);
		}
	}

	/**
	 * ȡ������������
	 * 
	 * @return
	 */
	public WorkDefinition getRoot() {
		ObjectId rootId = (ObjectId) getValue(F_ROOT_ID);
		if (rootId == null) {
			WorkDefinition parent = getParent();
			if (parent == null) {
				return this;
			} else {
				return parent.getRoot();
			}
		} else {
			return ModelService.createModelObject(WorkDefinition.class, rootId);
		}
	}

	public Organization getOrganization() {
		ObjectId org_id = (ObjectId) getValue(F_ORGANIZATION_ID);
		if (org_id != null) {
			return ModelService.createModelObject(Organization.class, org_id);
		}
		return null;
	}

	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	public List<PrimaryObject> getParticipateRoles() {
		List<?> list = (List<?>) getValue(F_PARTICIPATE_ROLE_SET);
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (list != null) {
			if (WORK_TYPE_PROJECT == getWorkDefinitionType()) {
				for (int i = 0; i < list.size(); i++) {
					DBObject data = (DBObject) list.get(i);
					RoleDefinition po = ModelService.createModelObject(data,
							RoleDefinition.class);
					result.add(po);
				}
			} else if (WORK_TYPE_GENERIC == getWorkDefinitionType()
					|| WORK_TYPE_STANDLONE == getWorkDefinitionType()) {
				for (int i = 0; i < list.size(); i++) {
					DBObject data = (DBObject) list.get(i);
					Role po = ModelService.createModelObject(data,
							Role.class);
					result.add(po);
				}
			}
		}
		return result;
	}
}
