package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * �ĵ�
 * ������ɺ�������ĵ�
 * @author jinxitao
 *
 */
public class Document extends PrimaryObject implements IProjectRelative{

	/**
	 * �ĵ�����
	 */
	public static final String F_DOCUMENT_TYPE = "documenttype";
	
	/**
	 * �ĵ����
	 */
	public static final String F_DOCUMENT_NUMBER = "documentnumber";

	/**
	 * ժҪ��Ϣ
	 */
	public static final String F__SUMMARY = "_summary";

	/**
	 * �ļ���_id�ֶ�ֵ
	 */
	public static final String F_FOLDER_ID = "folder_id";

	/**
	 * �ĵ������Ĵ洢λ��
	 */
	public static final String F_VAULT = "vault";

	/**
	 * ��������Ϊ��
	 */
	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty";

	public static final String F_DESCRIPTION = "description";

	public static final String FILE_NAMESPACE = "vault_file";

	/**
	 * ���ݿ�����
	 */
	public static final String FILE_DB = "pm2";

	/**
	 * ���ش��λ�ö����_id
	 * @return  ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_FOLDER_ID);
	}
	
	/**
	 * �����ĵ�����
	 * @return String
	 */
	public String getDocumentType(){
		return (String) getValue(F_DOCUMENT_TYPE);
	}
	
	/**
	 * ����ժҪ��Ϣ
	 * @return String
	 */
	public String getSummary(){
		return (String) getValue(F__SUMMARY);
	}
	
	/**
	 * �����ĵ���� 
	 * @return String
	 */
	public String getDocumentNumber(){
		return (String) getValue(F_DOCUMENT_NUMBER);
	}

	/**
	 * ���ع鵵���ĵ���
	 * @return
	 */
	public RemoteFileSet getVault(){
		List<RemoteFile> files = getFileValue(F_VAULT);
		RemoteFileSet rs = new RemoteFileSet(getDbName(), getCollectionName());
		rs.setOriginalRemoteFileSet(files);
		rs.setUpdatedRemoteFileSet(files);
		return rs;
	}

	/**
	 * ������ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}

	/**
	 * �ж��Ƿ���Ա༭
	 */
	@Override
	public boolean canEdit(IContext context) {
		return super.canEdit(context);
	}

	/**
	 * �ж��Ƿ�ֻ��
	 */
	@Override
	public boolean canRead(IContext context) {
		return super.canRead(context);
	}

	
	@Override
	public String getTypeName() {
		return "�ĵ�";
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
