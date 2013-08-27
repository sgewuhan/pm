package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ������<p/>
 * ���������빤����ɺ�������ĵ�
 * @author jinxitao
 *
 */
public class Deliverable extends PrimaryObject implements IProjectRelative{

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
	 * ������ı༭��
	 */
	public static final String EDITOR = "work.deliverable.create";

	public static final String EDITOR_SETTING = "work.deliverable";

	/**
	 * ������ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}
	
	@Override
	public String getLabel() {
		Document document = getDocument();
		return document.getLabel();
	}
	
	public Document getDocument() {
		ObjectId _id = getDocumentId();
		return ModelService.createModelObject(Document.class, _id);
	}

	public ObjectId getDocumentId() {
		return (ObjectId) getValue(F_DOCUMENT_ID);
	}

	@Override
	public String getTypeName() {
		return "������";
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
}
