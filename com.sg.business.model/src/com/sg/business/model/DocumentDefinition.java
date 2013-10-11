package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * �ĵ�ģ�嶨��
 * 
 * @author jinxitao
 * 
 */
public class DocumentDefinition extends PrimaryObject {

	/**
	 * ������֯ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * ��������Ϊ��
	 */
	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty";

	/**
	 * �ĵ�ģ��༭��ID
	 */
	public static final String F_DOCUMENT_EDITORID = "document_editorid";

	/**
	 * ����
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * �ļ����͵��ֶ� <br/>
	 * <code>"templatefile" : [{ "_id" : ObjectId("5209d03fe5abb85488af9c81"),
	 * "namespace" : "templatefile_file", "fileName" : "����.xlsx", "preview" :
	 * ObjectId("5209d03fe5abb85488af9c82"), "db" : "pm2" }]<code/>
	 */
	public static final String F_TEMPLATEFILE = "templatefile";

	public DocumentDefinition() {
		super();
		setVersionControledFields(new String[] { F_DESC,
				F_ATTACHMENT_CANNOT_EMPTY, F_DESCRIPTION, F_DOCUMENT_EDITORID,
				F_ORGANIZATION_ID, F_TEMPLATEFILE });
	}

	/**
	 * ������ʾͼ��
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource
				.getImage(BusinessResource.IMAGE_DOCUMENT_DEF_16);
	}

	/**
	 * ɾ���ĵ�ģ��<br/>
	 * �ĵ�ģ��û�б�������������ʱ������ɾ��
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// ɾ���ĵ�ģ��ʱ��Ҫ�����ĵ��Ƿ񱻽����������ã����������ɾ��
		List<PrimaryObject> projectTemplates = getReferenceProjectTemplates();
		if (projectTemplates.size() > 0) {

			throw new Exception(
					"�ĵ�ģ�屻��Ŀģ��Ĺ������������ã�������Ŀģ��ɾ�����ø��ĵ�ģ��Ľ����ﶨ�����ɾ���ĵ�ģ�塣\n��Ŀģ��:\n"
							+ Utils.getLimitLengthString(
									projectTemplates.toString(), 80));
		}

		List<PrimaryObject> genericWorkDefinitions = getReferenceGenericAndStandloneWorkDefinition();
		if (genericWorkDefinitions.size() > 0) {

			throw new Exception(
					"�ĵ�ģ�屻ͨ�ù���������߶�����������Ľ��������ã�����ɾ�����ø��ĵ�ģ��Ľ����ﶨ�����ɾ���ĵ�ģ�塣\n��������:\n"
							+ Utils.getLimitLengthString(
									genericWorkDefinitions.toString(), 80));
		}

		super.doRemove(context);
	}

	/**
	 * ���ʹ�ø��ĵ���ͨ�ù�������
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
	 * ���ʹ�ø��ĵ�ģ��Ľ����ﶨ��
	 * 
	 * @return List
	 */
	public List<PrimaryObject> getReferenceDeliverableDefinitions() {
		return getRelationById(F__ID,
				DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
				DeliverableDefinition.class);
	}

	/**
	 * ���ʹ�ø��ĵ�ģ�����Ŀģ��
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

	/**
	 * ������������
	 * 
	 * @retrun String
	 */
	@Override
	public String getTypeName() {
		return "�ĵ�����";
	}

	/**
	 * �����ĵ�
	 * 
	 * @param projectId
	 *            �ĵ�������Ŀ_id
	 * @param context
	 *            ,������
	 * @return Document
	 * @throws Exception
	 */
	public Document doCreateDocument(ObjectId projectId, IContext context)
			throws Exception {
		BasicDBObject documentData = new BasicDBObject();
		documentData.put(Document.F__ID, new ObjectId());
		documentData.put(Document.F_PROJECT_ID, projectId);

		Project project = ModelService.createModelObject(Project.class,
				projectId);
		documentData.put(Document.F_FOLDER_ID, project.getFolderRootId());

		Object value = getValue(F_DESC);
		if (value != null) {
			documentData.put(Document.F_DESC, value);
		}
		value = getValue(F_DESC_EN);
		if (value != null) {
			documentData.put(Document.F_DESC_EN, value);
		}
		value = new Boolean(
				Boolean.TRUE.equals(getValue(F_ATTACHMENT_CANNOT_EMPTY)));
		documentData.put(Document.F_ATTACHMENT_CANNOT_EMPTY, value);

		value = getValue(F_DESCRIPTION);
		if (value != null) {
			documentData.put(Document.F_DESCRIPTION, value);
		}
		value = getValue(F_DOCUMENT_EDITORID);
		if (value != null) {
			documentData.put(Document.F__EDITOR, value);
		}

		// �����ĵ��ĸ��������ļ�
		BasicBSONList tfiles = (BasicBSONList) getValue(DocumentDefinition.F_TEMPLATEFILE);
		if (tfiles != null) {
			BasicBSONList documentFiles = new BasicBSONList();
			for (int i = 0; i < tfiles.size(); i++) {
				DBObject tFile = (DBObject) tfiles.get(i);
				DBObject dFile = new BasicDBObject();
				dFile.put(RemoteFile.F_ID, new ObjectId());
				dFile.put(RemoteFile.F_FILENAME,
						tFile.get(RemoteFile.F_FILENAME));
				dFile.put(RemoteFile.F_NAMESPACE, Document.FILE_NAMESPACE);
				dFile.put(RemoteFile.F_DB, Document.FILE_DB);
				documentFiles.add(dFile);

				FileUtil.copyGridFSFile((ObjectId) tFile.get(RemoteFile.F_ID),
						(String) tFile.get(RemoteFile.F_DB),
						(String) tFile.get(RemoteFile.F_FILENAME),
						(String) tFile.get(RemoteFile.F_NAMESPACE),

						(ObjectId) dFile.get(RemoteFile.F_ID),
						(String) dFile.get(RemoteFile.F_DB),
						(String) dFile.get(RemoteFile.F_FILENAME),
						(String) dFile.get(RemoteFile.F_NAMESPACE));
			}
			documentData.put(Document.F_VAULT, documentFiles);
		}

		Document doc = ModelService.createModelObject(documentData,
				Document.class);
		doc.doInsert(context);
		return doc;
	}

}
