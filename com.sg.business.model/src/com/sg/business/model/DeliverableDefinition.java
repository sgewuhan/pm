package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

public class DeliverableDefinition extends AbstractOptionFilterableItem {

	/**
	 * 交付物所属的工作
	 */
	public static final String F_WORK_DEFINITION_ID = "workd_id";

	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id";

	/**
	 * 交付物的编辑器ID,请与plugins.xml保持一致
	 */
	public static final String EDITOR = "editor.deliverableDefinition";

	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	@Override
	protected void doInsert(IContext context) throws Exception {
		// 创建对应的文档定义
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
		if (docd_id == null) {
			DBObject docdData = new BasicDBObject();
			docdData.put(DocumentDefinition.F_DESC, getDesc());
			docdData.put(DocumentDefinition.F_PROJECTTEMPLATE_ID, getValue(F_PROJECTTEMPLATE_ID));

			DocumentDefinition docd = ModelService.createModelObject(docdData,
					DocumentDefinition.class);
			docd.doSave(context);
			setValue(F_DOCUMENT_DEFINITION_ID, docd.get_id());
		}
		super.doInsert(context);
	}

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
}
