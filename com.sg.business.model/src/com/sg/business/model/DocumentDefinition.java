package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class DocumentDefinition extends PrimaryObject {

	/**
	 * 所属组织ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";
	
	/**
	 * 附件不能为空
	 */
	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty";
	
	/**
	 * 文档编辑器ID
	 */
	public static final String F_DOCUMENT_EDITORID = "document_editorid";
	
	/**
	 * 描述
	 */
	public static final String F_DESCRIPTION = "description";

	
	/**
	 * 文件类型的字段 <br/><code>"templatefile" : [{ "_id" : ObjectId("5209d03fe5abb85488af9c81"),
	 * "namespace" : "templatefile_file", "fileName" : "流程.xlsx", "preview" :
	 * ObjectId("5209d03fe5abb85488af9c82"), "db" : "pm2" }]<code/>
	 */
	public static final String F_TEMPLATEFILE = "templatefile";

	/**
	 * 返回显示图标
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource
				.getImage(BusinessResource.IMAGE_DOCUMENT_DEF_16);
	}

	/**
	 * 删除文档模板<br/>
	 * 文档模板没有被工作定义引用时，才能删除
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// 删除文档模板时需要检查该文档是否被交付定义引用，如果是则不能删除
		List<PrimaryObject> projectTemplates = getReferenceProjectTemplates();
		if (projectTemplates.size() > 0) {

			throw new Exception(
					"文档模板被项目模板的工作交付物引用，请在项目模板删除引用该文档模板的交付物定义后再删除文档模板。\n项目模板:\n"
							+ Utils.getLimitLengthString(
									projectTemplates.toString(), 80));
		}

		List<PrimaryObject> genericWorkDefinitions = getReferenceGenericAndStandloneWorkDefinition();
		if (genericWorkDefinitions.size() > 0) {

			throw new Exception(
					"文档模板被通用工作定义或者独立工作定义的交付物引用，请在删除引用该文档模板的交付物定义后再删除文档模板。\n工作定义:\n"
							+ Utils.getLimitLengthString(
									genericWorkDefinitions.toString(), 80));
		}

		super.doRemove(context);
	}

	/**
	 * 获得使用该文档的通用工作定义
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getReferenceGenericAndStandloneWorkDefinition() {
		List<PrimaryObject> deliverableDefinitions = getReferenceDeliverableDefinitions();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		for (PrimaryObject primaryObject : deliverableDefinitions) {
			DeliverableDefinition deliverableDefinition = (DeliverableDefinition) primaryObject;
			WorkDefinition workDefinition = deliverableDefinition
					.getWorkDefinition();
			Assert.isNotNull(workDefinition);

			int type = workDefinition.getWorkDefinitionType();
			if (type == WorkDefinition.WORK_TYPE_GENERIC
					|| type == WorkDefinition.WORK_TYPE_STANDLONE) {
				WorkDefinition root = (WorkDefinition) workDefinition.getRoot();
				if (!result.contains(root)) {
					result.add(root);
				}
			}
		}

		return result;
	}

	/**
	 * 获得使用该文档模板的交付物定义
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getReferenceDeliverableDefinitions() {
		return getRelationById(F__ID,
				DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
				DeliverableDefinition.class);
	}

	/**
	 * 获得使用该文档模板的项目模板
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getReferenceProjectTemplates() {
		List<PrimaryObject> deliverableDefinitions = getReferenceDeliverableDefinitions();
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		for (PrimaryObject primaryObject : deliverableDefinitions) {
			DeliverableDefinition deliverableDefinition = (DeliverableDefinition) primaryObject;
			WorkDefinition workDefinition = deliverableDefinition
					.getWorkDefinition();
			Assert.isNotNull(workDefinition);

			int type = workDefinition.getWorkDefinitionType();
			if (type == WorkDefinition.WORK_TYPE_PROJECT) {
				ProjectTemplate projectTemplate = workDefinition
						.getProjectTemplate();
				if (!result.contains(projectTemplate)) {
					result.add(projectTemplate);
				}
			}
		}
		return result;
	}
	
	@Override
	public String getTypeName() {
		return "文档定义";
	}

}
