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
 * ������
 * <p/>
 * ���������빤����ɺ�������ĵ�
 * 
 * @author jinxitao
 * 
 */
public class Deliverable extends PrimaryObject implements IProjectRelative {

	/**
	 * ����_id�ֶΣ����ڱ��湤��_id��ֵ
	 */
	public static final String F_WORK_ID = "work_id";

	/**
	 * 
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * �ĵ�_id�ֶΣ����ڱ����ĵ�_id��ֵ
	 */
	public static final String F_DOCUMENT_ID = "document_id";

	/**
	 * �ĵ�ģ��id
	 */
	public static final String F_DOCUMENT_DEFINITION_ID = "documentd_id";

	/**
	 * ������ı༭��
	 */
	public static final String EDITOR = "work.deliverable.create";

	public static final String EDITOR_SETTING = "work.deliverable";

	/**
	 * ������ʾͼ��
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}

	/**
	 * ������ʾ����
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
	 * ���ؽ������ĵ�
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
	 * ���ؽ������ĵ�_id
	 * 
	 * @return
	 */
	public ObjectId getDocumentId() {
		return (ObjectId) getValue(F_DOCUMENT_ID);
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "������";
	}

	/**
	 * ����Ĭ�ϱ༭��ID
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * ������Ŀ
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
	 * ���뽻���ﶨ���¼�����ݿ���
	 */
	@Override
	public void doInsert(IContext context) throws Exception {
		// ������Ӧ���ĵ�
		ObjectId doc_id = (ObjectId) getValue(F_DOCUMENT_ID);
		if (doc_id == null) {
			Document doc;
			ObjectId projectId = (ObjectId) getValue(F_PROJECT_ID);

			// �ж��Ƿ�����ĵ�ģ��
			ObjectId docd_id = (ObjectId) getValue(F_DOCUMENT_DEFINITION_ID);
			if (docd_id == null) {// �������ĵ�ģ��
				DBObject docdData = new BasicDBObject();
				docdData.put(Document.F_DESC, getDesc());

				// ��ȡ������������Ŀ
				if (projectId != null) {
					docdData.put(Document.F_PROJECT_ID, projectId);
				}
				doc = ModelService.createModelObject(docdData, Document.class);
				doc.doSave(context);
			} else {
				// �����ĵ�ģ��
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
		 * �������Ŀ�����Ľ������Ŀ�������ɾ���������ĸ����˿���ɾ��
		 * 
		 * ���������׼���С���״̬ʱ������ɾ��
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
