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
	 * 只用于通用工作定义和独立工作定义
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 只用于项目模板工作定义
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

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
	 * 工作定义的同层序号
	 */
	public static final String F_SEQ = "seq";

	/**
	 * 通用工作定义的编辑器Id
	 */
	public static final String EDITOR_GENERIC_WORK = "editor.genericWorkDefinition";

	/**
	 * 独立工作定义的编辑器Id
	 */
	public static final String EDITOR_STANDLONE_WORK = "editor.standloneWorkDefinition";

	/**
	 * 项目模板工作定义的编辑器Id
	 */
	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_WORK_16);
	}


	/**
	 * 构造子工作定义对象
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
	 * 获取工作定义所属的项目模板
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
	 * 获得该工作定义的负责人角色定义
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
