package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

public class DeliverableDefinition extends AbstractOptionFilterable {

	/**
	 * �����������Ĺ���
	 */
	public static final String F_WORK_DEFINITION_ID = "workd_id";

	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id";

	/**
	 * ������ı༭��ID,����plugins.xml����һ��
	 */
	public static final String EDITOR = "editor.deliverableDefinition";

	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id";

	/**
	 * ͨ�ù�������Ͷ�����������ʹ��
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	@Override
	protected void doInsert(IContext context) throws Exception {
		// ������Ӧ���ĵ�����
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
		if (docd_id == null) {
			DBObject docdData = new BasicDBObject();
			docdData.put(DocumentDefinition.F_DESC, getDesc());

			// ��ȡģ���Ӧ����֯
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

	public ProjectTemplate getProjectTemplate() {
		ObjectId pjtempId = (ObjectId) getValue(F_PROJECTTEMPLATE_ID);
		if (pjtempId != null) {
			return ModelService.createModelObject(ProjectTemplate.class,
					pjtempId);
		}
		return null;
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
