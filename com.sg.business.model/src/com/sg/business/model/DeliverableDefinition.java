package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;


/**
 * 交付物定义
 * @author jinxitao
 *
 */
public class DeliverableDefinition extends AbstractOptionFilterable {

	/**
	 * 交付物所属的工作
	 */
	public static final String F_WORK_DEFINITION_ID = "workd_id";

	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id";

	/**
	 * 交付物的编辑器ID,请与plugins.xml保持一致
	 */
	public static final String EDITOR = "editor.deliverableDefinition";

	/**
	 * 所属模板ID
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

	/**
	 * 通用工作定义和独立工作定义使用
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 返回显示图标
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	/**
	 * 插入交付物定义记录到数据库中
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		// 创建对应的文档定义
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
		if (docd_id == null) {
			DBObject docdData = new BasicDBObject();
			docdData.put(DocumentDefinition.F_DESC, getDesc());

			// 获取模板对应的组织
			ObjectId orgId = (ObjectId) getValue(F_ORGANIZATION_ID);
			if(orgId==null){
				ProjectTemplate projectTemplate = getProjectTemplate();
				if(projectTemplate!=null){
					orgId = (ObjectId)projectTemplate.getValue(ProjectTemplate.F_ORGANIZATION_ID);
				}
			}
			docdData.put(DocumentDefinition.F_ORGANIZATION_ID,orgId);

			DocumentDefinition docd = ModelService.createModelObject(docdData,
					DocumentDefinition.class);
			docd.doSave(context);
			setValue(F_DOCUMENT_DEFINITION_ID, docd.get_id());
		}
		super.doInsert(context);
	}

	/**
	 * 返回项目模板
	 * @return ProjectTemplate
	 */
	public ProjectTemplate getProjectTemplate() {
		ObjectId pjtempId = (ObjectId) getValue(F_PROJECTTEMPLATE_ID);
		if (pjtempId != null) {
			return ModelService.createModelObject(ProjectTemplate.class,
					pjtempId);
		}
		return null;
	}

	/**
	 * 返回显示内容
	 * @return String
	 */
	@Override
	public String getLabel() {
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);

		if (docd_id != null) {
			DocumentDefinition docd = ModelService.createModelObject(
					DocumentDefinition.class, docd_id);
			Assert.isNotNull(docd, "DeliverableDefinition"
					+ get_id().toString() + " lost documentDefinition: "
					+ docd_id);
			return docd.getLabel();
		}
		return super.getLabel();
	}

	/**
	 * 返回工作定义
	 * @return WorkDefinition
	 */
	public WorkDefinition getWorkDefinition() {
		List<PrimaryObject> result = getRelationById(F_WORK_DEFINITION_ID, F__ID, WorkDefinition.class);
		if(result.size()>0){
			return (WorkDefinition) result.get(0);
		}
		return null;
	}
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "交付定义";
	}

}
