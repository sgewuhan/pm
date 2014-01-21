package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;


/**
 * �����ﶨ��
 * @author jinxitao
 *
 */
public class DeliverableDefinition extends AbstractOptionFilterable implements IDeliverable {

	
	public static final String F_DOCUMENTNUMBER = "documentnumber"; //$NON-NLS-1$

	/**
	 * �����������Ĺ���
	 */
	public static final String F_WORK_DEFINITION_ID = "workd_id"; //$NON-NLS-1$

	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id"; //$NON-NLS-1$

	/**
	 * ������ı༭��ID,����plugins.xml����һ��
	 */
	public static final String EDITOR = "editor.deliverableDefinition"; //$NON-NLS-1$

	/**
	 * ����ģ��ID
	 */
	public static final String F_PROJECTTEMPLATE_ID = "projecttemplate_id"; //$NON-NLS-1$

	/**
	 * ͨ�ù�������Ͷ�����������ʹ��
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * ������ʾͼ��
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	/**
	 * ���뽻���ﶨ���¼�����ݿ���
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
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
			String number = generateAutoIncreaseNumer();
			docdData.put(F_DOCUMENTNUMBER, number);
			DocumentDefinition docd = ModelService.createModelObject(docdData,
					DocumentDefinition.class);
			docd.doSave(context);
			setValue(F_DOCUMENT_DEFINITION_ID, docd.get_id());
		}
		super.doInsert(context);
	}

	private String generateAutoIncreaseNumer() {
		DBCollection ids = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C__IDS);
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_DOCUMENT_TEMPLATE_NUMBER);
		return String.format("%06x", id).toUpperCase(); //$NON-NLS-1$
	}

	/**
	 * ������Ŀģ��
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
	 * ������ʾ����
	 * @return String
	 */
	@Override
	public String getLabel() {
		ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);

		if (docd_id != null) {
			DocumentDefinition docd = ModelService.createModelObject(
					DocumentDefinition.class, docd_id);
			Assert.isNotNull(docd, "DeliverableDefinition" //$NON-NLS-1$
					+ get_id().toString() + " lost documentDefinition: " //$NON-NLS-1$
					+ docd_id);
			return docd.getLabel();
		}
		return super.getLabel();
	}

	/**
	 * ���ع�������
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
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().DeliverableDefinition_0;
	}
	
	/**
	 * ����Ĭ�ϱ༭��ID
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * ��ȡ�������ĵ�
	 * @return
	 */
	public DocumentDefinition getDocumentDefinition() {
		ObjectId documentDefinitionid = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
		return ModelService.createModelObject(DocumentDefinition.class, documentDefinitionid);
	}

	public String getType() {
		return (String) getValue(F_TYPE);
	}

}
