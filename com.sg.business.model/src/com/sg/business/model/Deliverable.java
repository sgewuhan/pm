package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * 交付物
 * <p/>
 * 关联工作与工作完成后产生的文档
 * 
 * @author jinxitao
 * 
 */
public class Deliverable extends PrimaryObject implements IProjectRelative {

	/**
	 * 工作_id字段，用于保存工作_id的值
	 */
	public static final String F_WORK_ID = "work_id";

	/**
	 * 
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * 文档_id字段，用于保存文档_id的值
	 */
	public static final String F_DOCUMENT_ID = "document_id";

	/**
	 * 文档模板id
	 */
	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id";

	/**
	 * 交付物的编辑器
	 */
	public static final String EDITOR = "work.deliverable.create";

	public static final String EDITOR_SETTING = "work.deliverable";

	/**
	 * 返回显示图标
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	/**
	 * 返回显示内容
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		Document document = getDocument();
		if (document != null) {
			return document.getLabel();
		} else {
			return super.getLabel();
		}
	}

	/**
	 * 返回交付物文档
	 * 
	 * @return Document
	 */
	public Document getDocument() {
		ObjectId _id = getDocumentId();
		if (_id == null) {
			return null;
		}
		return ModelService.createModelObject(Document.class, _id);
	}

	/**
	 * 返回交付物文档_id
	 * 
	 * @return
	 */
	public ObjectId getDocumentId() {
		return (ObjectId) getValue(F_DOCUMENT_ID);
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "交付物";
	}

	/**
	 * 返回默认编辑器ID
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * 返回项目
	 * 
	 * @return Project
	 */
	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * 插入交付物定义记录到数据库中
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		// 创建对应的文档
		ObjectId doc_id = (ObjectId) getValue(F_DOCUMENT_ID);
		if (doc_id == null) {
			Document doc;
			ObjectId projectId = (ObjectId) getValue(F_PROJECT_ID);

			// 判断是否存在文档模板
			ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
			if (docd_id == null) {// 不存在文档模板
				DBObject docdData = new BasicDBObject();
				docdData.put(Document.F_DESC, getDesc());

				// 获取交付物所属项目
				if (projectId != null) {
					docdData.put(Document.F_PROJECT_ID, projectId);
				}
				doc = ModelService.createModelObject(docdData, Document.class);
				doc.doSave(context);
			} else {
				// 存在文档模板
				DocumentDefinition docd = ModelService.createModelObject(
						DocumentDefinition.class, docd_id);
				doc = docd.doCreateDocument(projectId, context);
			}

			setValue(F_DOCUMENT_ID, doc.get_id());
		}
		super.doInsert(context);
	}

	public Work getWork() {
		ObjectId id = (ObjectId) getValue(F_WORK_ID);
		if (id != null) {
			return ModelService.createModelObject(Work.class, id);
		} else {
			return null;
		}
	}

	@Override
	public boolean canDelete(IContext context) {
		/**
		 * 如果是项目工作的交付物，项目经理可以删除，工作的负责人可以删除
		 * 
		 * 如果工作在准备中、无状态时，可以删除
		 */

		Work work = getWork();
		String lc = work.getLifecycleStatus();

		if (!Work.STATUS_NONE_VALUE.equals(lc)
				&& !Work.STATUS_ONREADY_VALUE.equals(lc)) {
			return false;
		}

		if (!work.hasPermission(context)) {
			return false;
		}

		return super.canDelete(context);
	}

	@Override
	public boolean canEdit(IContext context) {

		Work work = getWork();
		String lc = work.getLifecycleStatus();

		if (!Work.STATUS_NONE_VALUE.equals(lc)
				&& !Work.STATUS_ONREADY_VALUE.equals(lc)) {
			return false;
		}

		if (!work.hasPermission(context)) {
			return false;
		}
		return super.canEdit(context);
	}
}
