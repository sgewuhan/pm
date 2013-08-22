package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * <p>工作<p/>
 * 工作用于描述项目中的工作分解结构
 * @author jinxitao
 *
 */
public class Work extends AbstractWork implements IProjectRelative,ISchedual {

	/**
	 * 工作的编辑器ID
	 */
	public static final String EDITOR = "editor.work";

	/**
	 * 必需的，不可删除，布尔类型的字段
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * 返回工作所属项目
	 * @return Project
	 */
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * 新建下级工作
	 * @return Work
	 */
	@Override
	public Work makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(F_PARENT_ID, get_id());
		data.put(F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		data.put(F_PROJECT_ID, getValue(F_PROJECT_ID));

		Work po = ModelService.createModelObject(data, Work.class);
		return po;
	}

	/**
	 * 返回工作所属项目
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProject();
	}

	/**
	 * 新建工作交付物
	 * @return Deliverable
	 */
	@Override
	public Deliverable makeDeliverableDefinition() {
		return makeDeliverableDefinition(null);
	}

	/**
	 * 新建工作交付物
	 * @param docd
	 *         ,交付物文档定义
	 * @return
	 */
	public Deliverable makeDeliverableDefinition(DocumentDefinition docd) {
		DBObject data = new BasicDBObject();
		data.put(Deliverable.F_WORK_ID, get_id());

		data.put(Deliverable.F_PROJECT_ID, getValue(F_PROJECT_ID));

		if (docd != null) {
			data.put(Deliverable.F_DOCUMENT_ID, docd.get_id());
			data.put(Deliverable.F_DESC, docd.getDesc());
		}

		Deliverable po = ModelService
				.createModelObject(data, Deliverable.class);

		return po;
	}
	

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "工作";
	}
	
	/**
	 * 返回工作默认编辑器ID
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * 判断工作的属性是否只读
	 * @param column
	 *          ,工作的属性
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String column,
			IContext context) {
		return true;
	}

	/**
	 * 更新工作
	 * @param key
	 *        ,更新内容的KEY
	 * @param value
	 *        ,更改后的Value
	 * @param context
	 * @return Work[]
	 */
	public Work[] doUpdateSchedual(String key, Object value,
			IContext context) {
		setValue(key, value);
		return new Work[]{this};
	}
}
