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
 * 文档模板定义
 * 
 * @author jinxitao
 * 
 */
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
	 * 文档模板编辑器ID
	 */
	public static final String F_DOCUMENT_EDITORID = "document_editorid";

	/**
	 * 描述
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * 文件类型的字段 <br/>
	 * <code>"templatefile" : [{ "_id" : ObjectId("5209d03fe5abb85488af9c81"),
	 * "namespace" : "templatefile_file", "fileName" : "流程.xlsx", "preview" :
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
	 * 返回显示图标
	 * 
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

	/**
	 * 返回类型名称
	 * 
	 * @retrun String
	 */
	@Override
	public String getTypeName() {
		return "文档定义";
	}

	/**
	 * 创建文档
	 * 
	 * @param projectId
	 *            文档所属项目_id
	 * @param context
	 *            ,上下文
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

		// 根据文档的附件创建文件
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
