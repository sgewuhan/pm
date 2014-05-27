package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.StructuredDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.model.commonlabel.WorkDefinitionCommonHTMLLable;
import com.sg.business.model.input.WorkDefinitionEditorInputFactory;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.commons.model.IEditorInputFactory;

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
	 * ֻ����ͨ�ù�������Ͷ�����������,������֯��_id�ֶ�ֵ
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * ���������Ƿ񼤻��ʹ�ã�ֻ����{@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE}
	 */
	public static final String F_ACTIVATED = "activated"; //$NON-NLS-1$

	/**
	 * ͨ�ù�������ı༭��Id
	 */
	@Deprecated
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition.1"; //$NON-NLS-1$

	/**
	 * ͨ�ù�������ı༭��Id,���ڱ༭���ڵ�
	 */
	public static final String EDITOR_GENERIC_WORK_ROOT = "editor.genericWorkDefinition"; //$NON-NLS-1$

	/**
	 * ������������ı༭��Id
	 */
	@Deprecated
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition.1"; //$NON-NLS-1$

	/**
	 * ������������ı༭��Id,���ڱ༭���ڵ�
	 */
	public static final String EDITOR_STANDLONE_WORK_ROOT = "editor.standloneWorkDefinition"; //$NON-NLS-1$

	/**
	 * ��Ŀģ�幤������ı༭��Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition"; //$NON-NLS-1$

	private static final String POSTFIX_ACTIVATED = "_activated"; //$NON-NLS-1$

	private static final String POSTFIX_ASSIGNMENT = "_assignment"; //$NON-NLS-1$

	/**
	 * �����ڲ�������id����������ı༭�������룬Ԥ���ڳ������
	 */
	public static final String F_INTERNAL_ID = "internalid";//$NON-NLS-1$

	/**
	 * �Ƿ��ֹ�ֹ�����
	 */
	public static final String F_LAUNCHABLE = "launchforbidden";//$NON-NLS-1$

	/**
	 * ������ʽ
	 */
	public static final String F_MEASUREMENT = "measurement";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_NO_ID = "no";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_NO_VALUE = Messages.get().WorkDefinitionNoMeasurement;

	public static final String MEASUREMENT_TYPE_COMMIT_ID = "commit";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_COMMIT_VALUE = Messages.get().WorkDefinitionCommitMeasurement;

	public static final String MEASUREMENT_TYPE_PLAN_ID = "plan";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_PLAN_VALUE = Messages.get().WorkDefinitionPlanMeasurement;

	public static final String F_WORKTIMETYPE = "worktimetype";

	/**
	 * ���ع�����������͡� see {@link #F_WORK_TYPE}
	 * 
	 * @return int
	 */
	public int getWorkDefinitionType() {
		Object value = getValue(F_WORK_TYPE);
		Assert.isTrue(value instanceof Integer);
		return ((Integer) value).intValue();
	}

	/**
	 * �����ӹ���������󣬰������µĴ��� <br/>
	 * 
	 * <li>���Լ���{@link #F__ID}����Ϊ�������ӹ��������{@link #F_PARENT_ID}</li> <li>���Լ���
	 * {@link #F_ROOT_ID},����Ϊ�������ӹ��������{@link #F_ROOT_ID}</li>
	 * 
	 * <li>����ͨ�ù�������Ͷ����������壬�ڹ������ʱ����������һ����{@link #F_ORGANIZATION_ID}�ֶε�ֵ</li> <li>
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

	/**
	 * ���콻���ﶨ�����
	 * 
	 * @return DeliverableDefinition
	 */
	@Override
	public DeliverableDefinition makeDeliverableDefinition(String type) {
		return makeDeliverableDefinition(null, type);
	}

	/**
	 * �����ĵ����壬���������ﶨ�����
	 * 
	 * @param docd
	 *            Ϊ��ʱ�������ĵ������Id���뽻���ﶨ�����
	 * @return �����ﶨ��
	 */
	public DeliverableDefinition makeDeliverableDefinition(
			DocumentDefinition docd, String deliverableType) {
		DBObject data = new BasicDBObject();
		data.put(DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());
		data.put(DeliverableDefinition.F_TYPE, deliverableType);
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

	/**
	 * ���ع���������������н����ﶨ��
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getDeliverableDefinitions() {
		DBObject condition = new BasicDBObject().append(
				DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());
		StructuredDBCollectionDataSetFactory dsf = getRelationDataSetFactory(
				DeliverableDefinition.class, condition);
		return dsf.getDataSet().getDataItems();
	}

	/**
	 * ���������Ƿ����ã�������ͨ�ù������壬������������
	 * 
	 * @return boolean
	 */
	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}

	/**
	 * ɾ����������
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		int type = getWorkDefinitionType();
		if ((type == WORK_TYPE_GENERIC || type == WORK_TYPE_STANDLONE)
				&& isActivated()) {
			throw new Exception(Messages.get().WorkDefinition_0);
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

	/**
	 * ���غ��ù�������
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getEnd2Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END1_ID,
				WorkDefinitionConnection.class);
	}

	/**
	 * ����ǰ�ù�������
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getEnd1Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END2_ID,
				WorkDefinitionConnection.class);
	}

	/**
	 * ��ȡ����������������Ŀģ��
	 * 
	 * @return ProjectTemplate
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
	 *            ,ͨ�ù�������
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

	@Override
	public boolean canEdit(IContext context) {
		if (getWorkDefinitionType() != Work.WORK_TYPE_PROJECT) {
			if (isActivated()) {
				return false;
			}
		}
		return super.canEdit(context);
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
			throw new IllegalArgumentException(Messages.get().WorkDefinition_1);
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
					.makeDeliverableDefinition(srcDeliverableDefinition
							.getType());
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

	/**
	 * ���ع��������������֯
	 * 
	 * @return Organization
	 */
	public Organization getOrganization() {
		ObjectId org_id = (ObjectId) getValue(F_ORGANIZATION_ID);
		if (org_id != null) {
			return ModelService.createModelObject(Organization.class, org_id);
		}
		return null;
	}

	/**
	 * ���ع����������֯_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * ���ع����Ĳ����߽�ɫ
	 * 
	 * @return List
	 */
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

	/**
	 * ���ع������������ģ��
	 * 
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProjectTemplate();
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkDefinition_2;
	}

	/**
	 * ���ع��������Ĭ�ϱ༭��ID
	 * 
	 * @return String
	 */
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

	public boolean isWorkflowActivate(String fieldKey) {
		return Boolean.TRUE.equals(getValue(fieldKey + POSTFIX_ACTIVATED));
	}

	public DroolsProcessDefinition getProcessDefinition(String fieldKey) {
		DBObject processData = (DBObject) getValue(fieldKey);
		if (processData != null) {
			return new DroolsProcessDefinition(processData);
		}
		return null;
	}

	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// ȡ����ɫָ��
		DBObject data = (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IProcessControl.class)) {
			return (T) new ProcessControl(this) {
				@Override
				protected Class<? extends PrimaryObject> getRoleDefinitionClass() {
					return RoleDefinition.class;
				}
			};
		} else if (adapter.equals(IActivateSwitch.class)) {
			if (isStandloneWork() || isGenericWork()) {
				return (T) new ActivateSwitch(this);
			} else {
				return null;
			}
		} else if (adapter == CommonHTMLLabel.class) {
			return (T) (new WorkDefinitionCommonHTMLLable(this));
		} else if (adapter == IEditorInputFactory.class) {
			return (T) (new WorkDefinitionEditorInputFactory(this));
		}
		return super.getAdapter(adapter);
	}

	public boolean hasOrganizationRole(Role role) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_DEFINITION);
		long count = col.count(new BasicDBObject().append(
				RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id()).append(
				RoleDefinition.F_WORKDEFINITION_ID, get_id()));
		return count != 0;
	}

	public RoleDefinition makeOrganizationRole(Role role) {
		RoleDefinition roled = ModelService
				.createModelObject(RoleDefinition.class);
		roled.setValue(RoleDefinition.F_ORGANIZATION_ROLE_ID, role.get_id());
		roled.setValue(RoleDefinition.F_WORKDEFINITION_ID, get_id());
		return roled;
	}

	/**
	 * ����ģ���еĿ��õĽ�ɫ����
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getRoleDefinitions() {
		if (isProjectWork()) {
			ProjectTemplate pjt = getProjectTemplate();
			return pjt.getRoleDefinitions();
		} else if (isStandloneWork()) {
			return getRelationById(F__ID, RoleDefinition.F_WORKDEFINITION_ID,
					RoleDefinition.class);
		}
		return new ArrayList<PrimaryObject>();
	}

	/**
	 * ���ݵ�ǰ�Ĺ������壬������������
	 * 
	 * @param work
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Work makeStandloneWork(Work work, IContext context) throws Exception {
		if (work == null) {
			work = ModelService.createModelObject(Work.class);
		}
		work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// ���øù�����״̬Ϊ׼���У��Ա��Զ���ʼ
		work.setValue(Work.F_WORK_CATAGORY, getValue(F_WORK_CATAGORY));
		work.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		work.setValue(Work.F_WORK_DEFINITION_ID, get_id());
		work.setValue(Work.F_WORK_DEFINITION_NAME, getDesc());
		// �����ڲ�����
		work.setValue(Work.F_INTERNAL_PARA_CHARGERID,
				getValue(F_INTERNAL_PARA_CHARGERID));
		work.setValue(Work.F_INTERNAL_PARA_NOSKIP,
				getValue(F_INTERNAL_PARA_NOSKIP));
		work.setValue(Work.F_INTERNAL_DEFAULTSELECTED,
				getValue(F_INTERNAL_DEFAULTSELECTED));
		work.setValue(Work.F_INTERNAL_ECAPARA, getValue(F_INTERNAL_ECAPARA));

		IProcessControl pc = (IProcessControl) getAdapter(IProcessControl.class);
		DBObject wfdef = pc.getWorkflowDefinition(F_WF_EXECUTE);
		work.bindingWorkflowDefinition(Work.F_WF_EXECUTE, wfdef);
		return work;
	}

	public String getMeasurementLabel() {
		String measurement =getMeasurement();
		if (measurement != null) {
			if (MEASUREMENT_TYPE_NO_ID.equals(measurement)) {
				return MEASUREMENT_TYPE_NO_VALUE;
			}else if(MEASUREMENT_TYPE_COMMIT_ID.equals(measurement)){
				return MEASUREMENT_TYPE_COMMIT_VALUE;
			}else if(MEASUREMENT_TYPE_PLAN_ID.equals(measurement)){
				return MEASUREMENT_TYPE_PLAN_VALUE;
			}
		}
		return "";
	}
	
	public String getMeasurement(){
		return getStringValue(F_MEASUREMENT); 
	}

	
	

}
