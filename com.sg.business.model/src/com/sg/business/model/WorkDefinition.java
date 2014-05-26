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
 * 工作定义
 * <p/>
 * 工作定义包括三个类别：项目工作定义，通用工作定义，独立工作定义 <br/>
 * 工作定义用于描述项目模板中的工作分解结构
 * 
 * @author zhong hua
 * 
 */
public class WorkDefinition extends AbstractWork implements
		IProjectTemplateRelative {

	/**
	 * 只用于通用工作定义和独立工作定义,保存组织的_id字段值
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * 工作定义是否激活，可使用，只用于{@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE}
	 */
	public static final String F_ACTIVATED = "activated"; //$NON-NLS-1$

	/**
	 * 通用工作定义的编辑器Id
	 */
	@Deprecated
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition.1"; //$NON-NLS-1$

	/**
	 * 通用工作定义的编辑器Id,用于编辑根节点
	 */
	public static final String EDITOR_GENERIC_WORK_ROOT = "editor.genericWorkDefinition"; //$NON-NLS-1$

	/**
	 * 独立工作定义的编辑器Id
	 */
	@Deprecated
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition.1"; //$NON-NLS-1$

	/**
	 * 独立工作定义的编辑器Id,用于编辑根节点
	 */
	public static final String EDITOR_STANDLONE_WORK_ROOT = "editor.standloneWorkDefinition"; //$NON-NLS-1$

	/**
	 * 项目模板工作定义的编辑器Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition"; //$NON-NLS-1$

	private static final String POSTFIX_ACTIVATED = "_activated"; //$NON-NLS-1$

	private static final String POSTFIX_ASSIGNMENT = "_assignment"; //$NON-NLS-1$

	/**
	 * 用于内部工作的id，工作定义的编辑器中输入，预留于程序控制
	 */
	public static final String F_INTERNAL_ID = "internalid";//$NON-NLS-1$

	/**
	 * 是否禁止手工发起
	 */
	public static final String F_LAUNCHABLE = "launchforbidden";//$NON-NLS-1$

	/**
	 * 计量方式
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
	 * 返回工作定义的类型。 see {@link #F_WORK_TYPE}
	 * 
	 * @return int
	 */
	public int getWorkDefinitionType() {
		Object value = getValue(F_WORK_TYPE);
		Assert.isTrue(value instanceof Integer);
		return ((Integer) value).intValue();
	}

	/**
	 * 构造子工作定义对象，包括以下的处理： <br/>
	 * 
	 * <li>将自己的{@link #F__ID}设置为创建的子工作定义的{@link #F_PARENT_ID}</li> <li>将自己的
	 * {@link #F_ROOT_ID},设置为创建的子工作定义的{@link #F_ROOT_ID}</li>
	 * 
	 * <li>对于通用工作定义和独立工作定义，在构造对象时将保存其上一级的{@link #F_ORGANIZATION_ID}字段的值</li> <li>
	 * 对于项目工作定义，在构造时保存上一级的{@link #F_PROJECT_TEMPLATE_ID}字段的值</li>
	 * 
	 * @return 未保存的{@link #WorkDefinition}, 用于编辑器使用
	 */
	@Override
	public WorkDefinition makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		data.put(WorkDefinition.F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		// 针对不同类型的工作定义的预处理
		int type = getWorkDefinitionType();
		switch (type) {
		// 通用工作定义和独立工作定义需要设定组织Id
		case WORK_TYPE_GENERIC:
		case WORK_TYPE_STANDLONE:
			data.put(F_ORGANIZATION_ID, getValue(F_ORGANIZATION_ID));
			break;
		// 项目工作定义需要设定项目模板Id
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
	 * 构造交付物定义对象
	 * 
	 * @return DeliverableDefinition
	 */
	@Override
	public DeliverableDefinition makeDeliverableDefinition(String type) {
		return makeDeliverableDefinition(null, type);
	}

	/**
	 * 传入文档定义，构建交付物定义对象
	 * 
	 * @param docd
	 *            为空时，不将文档定义的Id放入交付物定义对象
	 * @return 交付物定义
	 */
	public DeliverableDefinition makeDeliverableDefinition(
			DocumentDefinition docd, String deliverableType) {
		DBObject data = new BasicDBObject();
		data.put(DeliverableDefinition.F_WORK_DEFINITION_ID, get_id());
		data.put(DeliverableDefinition.F_TYPE, deliverableType);
		// 针对不同类型的工作定义的预处理
		int type = getWorkDefinitionType();
		switch (type) {
		case WORK_TYPE_GENERIC:// 通用工作定义和独立工作定义需要设定组织Id
		case WORK_TYPE_STANDLONE:
			data.put(DeliverableDefinition.F_ORGANIZATION_ID,
					getValue(F_ORGANIZATION_ID));
			break;
		case WORK_TYPE_PROJECT:// 项目工作定义需要设定项目模板Id
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
	 * 返回工作定义关联的所有交付物定义
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
	 * 工作定义是否启用，适用于通用工作定义，独立工作定义
	 * 
	 * @return boolean
	 */
	public boolean isActivated() {
		return Boolean.TRUE.equals(getValue(F_ACTIVATED));
	}

	/**
	 * 删除工作定义
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		int type = getWorkDefinitionType();
		if ((type == WORK_TYPE_GENERIC || type == WORK_TYPE_STANDLONE)
				&& isActivated()) {
			throw new Exception(Messages.get().WorkDefinition_0);
		}

		// 删除前后顺序关系
		List<PrimaryObject> connections = getEnd2Connections();
		for (PrimaryObject primaryObject : connections) {
			primaryObject.doRemove(context);
		}
		connections = getEnd2Connections();
		for (PrimaryObject primaryObject : connections) {
			primaryObject.doRemove(context);
		}

		// 删除交付物定义
		List<PrimaryObject> deliverableDefinitions = getDeliverableDefinitions();
		for (PrimaryObject primaryObject : deliverableDefinitions) {
			primaryObject.doRemove(context);
		}

		// 删除下级
		List<PrimaryObject> childrenWorkDefinitions = getChildrenWork();
		for (PrimaryObject primaryObject : childrenWorkDefinitions) {
			primaryObject.doRemove(context);
		}
		// 删除自己
		WorkDefinition parent = (WorkDefinition) getParent();

		super.doRemove(context);

		if (parent != null) {

			// 对平级的重新排列序号
			List<PrimaryObject> children = parent.getChildrenWork();
			doSaveAndResetSeq(children, context);
		}

	}

	/**
	 * 返回后置工作定义
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getEnd2Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END1_ID,
				WorkDefinitionConnection.class);
	}

	/**
	 * 返回前置工作定义
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getEnd1Connections() {
		return getRelationById(F__ID, WorkDefinitionConnection.F_END2_ID,
				WorkDefinitionConnection.class);
	}

	/**
	 * 获取工作定义所属的项目模板
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
	 * 导入一个通用工作定义
	 * 
	 * @param genericWorkDefinition
	 *            ,通用工作定义
	 * @param context
	 * @throws Exception
	 */
	public void doImportGenericWorkDefinition(
			WorkDefinition genericWorkDefinition, IContext context)
			throws Exception {
		doClone(genericWorkDefinition, context);
	}

	/**
	 * 导出一个通用工作定义
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
	 * 复制创建
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

		// 创建子工作定义
		WorkDefinition child = makeChildWork();
		child.setValue(F_DESC, srcWorkDefinition.getValue(F_DESC));
		child.setValue(F_WF_CHANGE, srcWorkDefinition.getValue(F_WF_CHANGE));
		child.setValue(F_WF_EXECUTE, srcWorkDefinition.getValue(F_WF_EXECUTE));

		child.doSave(context);

		// 获取交付物定义
		List<PrimaryObject> srcDeliverableDefinitions = srcWorkDefinition
				.getDeliverableDefinitions();
		for (PrimaryObject po : srcDeliverableDefinitions) {
			DeliverableDefinition srcDeliverableDefinition = (DeliverableDefinition) po;
			DeliverableDefinition childDeliverable = child
					.makeDeliverableDefinition(srcDeliverableDefinition
							.getType());
			// 复制文档模板的Id
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

		// 处理子工作
		List<PrimaryObject> sourceChildren = srcWorkDefinition
				.getChildrenWork();
		for (PrimaryObject po : sourceChildren) {
			child.doClone((WorkDefinition) po, context);
		}
	}

	/**
	 * 返回工作定义归属的组织
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
	 * 返回工作定义的组织_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * 返回工作的参与者角色
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
	 * 返回工作定义归属的模板
	 * 
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProjectTemplate();
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkDefinition_2;
	}

	/**
	 * 返回工作定义的默认编辑器ID
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
		// 取出角色指派
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
	 * 返回模板中的可用的角色定义
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
	 * 根据当前的工作定义，创建独立工作
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
		work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// 设置该工作的状态为准备中，以便自动开始
		work.setValue(Work.F_WORK_CATAGORY, getValue(F_WORK_CATAGORY));
		work.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		work.setValue(Work.F_WORK_DEFINITION_ID, get_id());
		work.setValue(Work.F_WORK_DEFINITION_NAME, getDesc());
		// 传递内部参数
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
