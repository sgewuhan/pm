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
	 * 通用工作定义
	 */
	public static final int WORK_TYPE_GENERIC = 0;

	/**
	 * 独立工作定义
	 */
	public static final int WORK_TYPE_STANDLONE = 1;

	/**
	 * 项目模板工作定义
	 */
	public static final int WORK_TYPE_PROJECT = 2;

	/**
	 * 工作定义的类型， 可以使用
	 * <p>
	 * {@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE},
	 * {@link #WORK_TYPE_PROJECT}
	 */
	public static final String F_WORK_TYPE = "worktype";

	/**
	 * 项目模板工作定义和交付物定义
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	/**
	 * 只用于通用工作定义和独立工作定义
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 工作定义的上级工作定义
	 */
	public static final String F_PARENT_ID = "parent_id";

	/**
	 * 工作定义是否激活，可使用，只用于{@link #WORK_TYPE_GENERIC}, {@link #WORK_TYPE_STANDLONE}
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * 工作定义的负责角色定义，{@link RoleDefinition},保存了角色定义的Id
	 */
	public static final String F_CHARGER_ROLE_ID = "charger_roled_id";

	/**
	 * 承担者角色定义
	 */
	public static final String F_PARTICIPATE_ROLE_SET = "participate_roled_set";

	
	/**
	 * 工作定义的同层序号
	 */
	public static final String F_SEQ = "seq";

	/**
	 * 通用工作定义的编辑器Id
	 */
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition.1";
	
	public static final String EDITOR_GENERIC_WORK_ROOT =  "editor.genericWorkDefinition";

	/**
	 * 独立工作定义的编辑器Id
	 */
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition.1";

	public static final String EDITOR_STANDLONE_WORK_ROOT = "editor.standloneWorkDefinition";

	/**
	 * 项目模板工作定义的编辑器Id
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
	 * 构造子工作定义对象
	 * 
	 * @return
	 */
	public WorkDefinition makeChildWorkDefinition() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		data.put(WorkDefinition.F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		// 针对不同类型的工作定义的预处理
		int type = getWorkDefinitionType();
		switch (type) {
		case WORK_TYPE_GENERIC:// 通用工作定义和独立工作定义需要设定组织Id
		case WORK_TYPE_STANDLONE:
			data.put(F_ORGANIZATION_ID, getValue(F_ORGANIZATION_ID));
			break;
		case WORK_TYPE_PROJECT:// 项目工作定义需要设定项目模板Id
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
			throw new Exception("您不能移动顶层的工作");
		}

		List<PrimaryObject> children = parent.getChildrenWorkDefinition();
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
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}
		List<PrimaryObject> children = parent.getChildrenWorkDefinition();
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
		WorkDefinition parent = (WorkDefinition) getParentPrimaryObject();
		if (parent == null) {
			throw new Exception("您不能移动顶层的工作");
		}

		WorkDefinition grandpa = (WorkDefinition) parent
				.getParentPrimaryObject();

		List<PrimaryObject> thisChildren = getChildrenWorkDefinition();

		List<PrimaryObject> parentChildren = parent.getChildrenWorkDefinition();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "升级出错，无法定位将要升级的节点");

		if (grandpa == null) {
			throw new Exception("节点已经不能升级");
		}

		// 1 自己的下面所有的兄弟变成自己的儿子，从自己的最后一个儿子开始添加
		for (int i = index + 1; i < parentChildren.size(); i++) {
			WorkDefinition brother = (WorkDefinition) parentChildren.get(i);
			brother.setValue(F_PARENT_ID, get_id());
			thisChildren.add(brother);
		}
		// 整理儿子的序号
		doSaveAndResetSeq(thisChildren, context);

		// 2 祖父变成自己的父，取出祖父的下级获得父所在的位置，,插入到父下面的位置
		setValue(F_PARENT_ID, grandpa.get_id());
		List<PrimaryObject> grandpaChildren = grandpa
				.getChildrenWorkDefinition();
		index = grandpaChildren.indexOf(parent);
		Assert.isTrue(index != -1, "升级出错，无法定位将要移动的父节点");
		grandpaChildren.add(index + 1, this);
		doSaveAndResetSeq(grandpaChildren, context);
		return new PrimaryObject[] { this, parent, grandpa };
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
			throw new Exception("您不能移动顶层的工作");
		}
		List<PrimaryObject> parentChildren = parent.getChildrenWorkDefinition();
		int index = parentChildren.indexOf(this);
		Assert.isTrue(index != -1, "降级出错，无法定位将要降级的节点");

		// 变成上兄弟的最后一个儿子，从父中移除自己
		if (index == 0) {
			throw new Exception("节点已经不能降级");
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
			throw new Exception("工作定义处于启用状态，不可删除");
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
		List<PrimaryObject> childrenWorkDefinitions = getChildrenWorkDefinition();
		for (PrimaryObject primaryObject : childrenWorkDefinitions) {
			primaryObject.doRemove(context);
		}
		// 删除自己
		WorkDefinition parent = getParent();

		super.doRemove(context);

		if (parent != null) {

			// 对平级的重新排列序号
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
	 * 获取工作定义所属的项目模板
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
	 * 获得该工作定义的负责人角色定义
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
	 * 导入一个通用工作定义
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
			throw new IllegalArgumentException("源对象为空");
		}

		// 创建子工作定义
		WorkDefinition child = makeChildWorkDefinition();
		child.setValue(F_DESC, srcWorkDefinition.getValue(F_DESC));
		child.doSave(context);

		// 获取交付物定义
		List<PrimaryObject> srcDeliverableDefinitions = srcWorkDefinition
				.getDeliverableDefinitions();
		for (PrimaryObject po : srcDeliverableDefinitions) {
			DeliverableDefinition srcDeliverableDefinition = (DeliverableDefinition) po;
			DeliverableDefinition childDeliverable = child
					.makeDeliverableDefinition();
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
				.getChildrenWorkDefinition();
		for (PrimaryObject po : sourceChildren) {
			child.doClone((WorkDefinition) po, context);
		}
	}

	/**
	 * 取出根工作定义
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
