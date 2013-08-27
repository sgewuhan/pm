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
 * 交付物<p/>
 * 关联工作与工作完成后产生的文档
 * @author jinxitao
 *
 */
public class Deliverable extends PrimaryObject implements IProjectRelative{

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
	 * 交付物的编辑器
	 */
	public static final String EDITOR = "work.deliverable.create";

	public static final String EDITOR_SETTING = "work.deliverable";

	/**
	 * 返回显示图标
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}
	
	@Override
	public String getLabel() {
		Document document = getDocument();
		if(document!=null){
			return document.getLabel();
		}else{
			return super.getLabel();
		}
	}
	
	public Document getDocument() {
		ObjectId _id = getDocumentId();
		if(_id==null){
			return null;
		}
		return ModelService.createModelObject(Document.class, _id);
	}

	public ObjectId getDocumentId() {
		return (ObjectId) getValue(F_DOCUMENT_ID);
	}

	@Override
	public String getTypeName() {
		return "交付物";
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}
	
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
		// 创建对应的文档定义
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_ID);
		if (docd_id == null) {
			DBObject docdData = new BasicDBObject();
			docdData.put(Document.F_DESC, getDesc());

			//获取交付物所属项目
			ObjectId projectId = (ObjectId) getValue(F_PROJECT_ID);
			if(projectId!=null){
				docdData.put(Document.F_PROJECT_ID,projectId);
			}

			Document docd = ModelService.createModelObject(docdData,
					Document.class);
			docd.doSave(context);
			setValue(F_DOCUMENT_ID, docd.get_id());
		}
		super.doInsert(context);
	}

}
