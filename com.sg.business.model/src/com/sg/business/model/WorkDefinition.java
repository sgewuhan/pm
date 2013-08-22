package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.StructuredDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
public class WorkDefinition extends AbstractWork implements
		IProjectTemplateRelative {

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
	 * ֻ����ͨ�ù�������Ͷ�����������,������֯��_id�ֶ�ֵ
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * ���������Ƿ񼤻��ʹ�ã�ֻ����{@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE}
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * ͨ�ù�������ı༭��Id
	 */
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition.1";

	public static final String EDITOR_GENERIC_WORK_ROOT = "editor.genericWorkDefinition";

	/**
	 * ������������ı༭��Id
	 */
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition.1";

	public static final String EDITOR_STANDLONE_WORK_ROOT = "editor.standloneWorkDefinition";

	/**
	 * ��Ŀģ�幤������ı༭��Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition";

	/**
	 * ���ع�����������͡� see {@link #F_WORK_TYPE}
	 * 
	 * @return
	 */
	public int getWorkDefinitionType() {
		Object value = getValue(F_WORK_TYPE);
		Assert.isTrue(value instanceof Integer);
		return ((Integer) value).intValue();
	}

	/**
	 * �����ӹ���������󣬰������µĴ���<br/>
	 * <li>���Լ���{@link #F__ID}����Ϊ�������ӹ��������{@link #F_PARENT_ID}</li> <li>���Լ���
	 * {@link #F_ROOT_ID},����Ϊ�������ӹ��������{@link #F_ROOT_ID}</li> <li>
	 * ����ͨ�ù�������Ͷ����������壬�ڹ������ʱ����������һ����{@link #F_ORGANIZATION_ID}�ֶε�ֵ</li> <li>
	 * ������Ŀ�������壬�ڹ���ʱ������һ����{@link #F_PROJECT_TEMPLATE_ID}�ֶε�ֵ</li>
	 * 
	 * @return δ�����{@link #WorkDefinition}, ���ڱ༭��ʹ��
	 */
	@Override
	public WorkDefinition makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		data.put(WorkDefinition.F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		// ��Բ�ͬ���͵Ĺ��������Ԥ����
		int type = getWorkDefinitionType();
		switch (type) {
		// ͨ�ù�������Ͷ�������������Ҫ�趨��֯Id
		case WORK_TYPE_GENERIC:
		case WORK_TYPE_STANDLONE:
			data.put(F_ORGANIZATION_ID, getValue(F_ORGANIZATION_ID));
			break;
		// ��Ŀ����������Ҫ�趨��Ŀģ��Id
		case WORK_TYPE_PROJECT:
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

	@Override
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

	public List<PrimaryObject> getDeliverableDefinitions() {
		DBObject condition = new BasicDBObject().append(
				DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				DeliverableDefinition.class, condition);
		return dsf.getDataSet().getDataItems();
	}

	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
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
		List<PrimaryObject> childrenWorkDefinitions = getChildrenWork();
		for (PrimaryObject primaryObject : childrenWorkDefinitions) {
			primaryObject.doRemove(context);
		}
		// ɾ���Լ�
		WorkDefinition parent = (WorkDefinition) getParent();

		super.doRemove(context);

		if (parent != null) {

			// ��ƽ���������������
			List<PrimaryObject> children = parent.getChildrenWork();
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
		WorkDefinition child = makeChildWork();
		child.setValue(F_DESC, srcWorkDefinition.getValue(F_DESC));
		child.setValue(F_WF_CHANGE, srcWorkDefinition.getValue(F_WF_CHANGE));
		child.setValue(F_WF_EXECUTE, srcWorkDefinition.getValue(F_WF_EXECUTE));

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
				.getChildrenWork();
		for (PrimaryObject po : sourceChildren) {
			child.doClone((WorkDefinition) po, context);
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
					Role po = ModelService.createModelObject(data, Role.class);
					result.add(po);
				}
			}
		}
		return result;
	}

	@Override
	public PrimaryObject getHoster() {
		return getProjectTemplate();
	}

	@Override
	public String getTypeName() {
		return "��������";
	}

	@Override
	public String getDefaultEditorId() {
		int type = getWorkDefinitionType();
		switch (type) {
		case WorkDefinition.WORK_TYPE_GENERIC:
			return WorkDefinition.EDITOR_GENERIC_WORK;
		case WorkDefinition.WORK_TYPE_STANDLONE:
			return WorkDefinition.EDITOR_STANDLONE_WORK;
		case WorkDefinition.WORK_TYPE_PROJECT:
			return WorkDefinition.EDITOR_PROJECT_WORK;
		default:
		}
		return super.getDefaultEditorId();
	}
}
