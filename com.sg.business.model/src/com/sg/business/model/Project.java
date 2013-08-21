package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.resource.BusinessResource;

public class Project extends PrimaryObject implements IProjectTemplateRelative,
		ILifecycle {

	/**
	 * 项目负责人字段，保存项目负责人的userid {@link User} ,
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * 数组类型字段，字段中的每个元素为 userData
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * 项目所属的项目职能组织
	 */
	public static final String F_FUNCTION_ORGANIZATION = "org_id";

	/**
	 * 项目发起部门
	 */
	public static final String F_LAUNCH_ORGANIZATION = "launchorg_id";

	/**
	 * 根工作定义
	 */
	public static final String F_WORK_ID = "work_id";

	public static final String F_BUDGET_ID = "budget_id";

	public static final String F_STANDARD_SET_OPTION = "standardset";

	public static final String F_PRODUCT_TYPE_OPTION = "producttype";

	public static final String F_PROJECT_TYPE_OPTION = "projecttype";

	/**
	 * 列表类型的字段，工作令号
	 */
	public static final String F_WORK_ORDER = "workorder";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}

	public User getCharger() {
		String chargerId = (String) getValue(F_CHARGER);
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public Organization getFunctionOrganization() {
		ObjectId orgId = getFunctionOrganizationId();
		if (orgId != null) {
			return ModelService.createModelObject(Organization.class,
					(ObjectId) orgId);
		}
		return null;
	}

	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	public ProjectTemplate getProjectTemplate() {
		ObjectId id = getProjectTemplateId();
		if (id != null) {
			return ModelService.createModelObject(ProjectTemplate.class, id);
		}
		return null;
	}

	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		setValue(F__ID, new ObjectId());

		// 创建根工作定义
		Work root = makeWBSRoot();
		root.doInsert(context);
		setValue(ProjectTemplate.F_WORK_DEFINITON_ID, root.get_id());

		// 预算
		ProjectBudget budget = makeBudget(context);
		budget.doInsert(context);

		super.doInsert(context);

		// 复制模板
		doSetupWithTemplate(root.get_id(), context);

	}

	public ProjectBudget makeBudget(IContext context) {
		ObjectId id = getProjectTemplateId();
		if (id != null) {
			return makeBudgetWithTemplate(id, context);
		} else {
			// 从全局复制
			return (ProjectBudget) BudgetItem
					.COPY_DEFAULT_BUDGET_ITEM(ProjectBudget.class);
		}
	}

	private Work makeWBSRoot() {
		BasicDBObject wbsRootData = new BasicDBObject();
		wbsRootData.put(Work.F_DESC, getDesc());
		wbsRootData.put(Work.F_PROJECT_ID, get_id());
		ObjectId wbsRootId = new ObjectId();
		wbsRootData.put(Work.F__ID, wbsRootId);
		wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
		return ModelService.createModelObject(wbsRootData, Work.class);
	}

	/**
	 * 
	 * @param context
	 * @param wbsRootId
	 * @throws Exception
	 */
	public void doSetupWithTemplate(ObjectId wbsRootId, IContext context)
			throws Exception {
		ObjectId id = getProjectTemplateId();
		if (id == null) {
			return;
		}

		// 复制角色定义
		Map<ObjectId, DBObject> roleMap = doSetupRolesWithTemplate(id, context);

		// 复制工作定义
		Map<ObjectId, DBObject> workMap = doSetupWBSWithTemplate(id, wbsRootId,
				roleMap, context);

		// 复制工作的前后序关系
		doSetupWorkConnectionWithTemplate(id, workMap, context);

	}

	public ProjectBudget makeBudgetWithTemplate(ObjectId projectTemplateId,
			IContext context) {
		DBCollection col = getCollection(IModelConstants.C_BUDGET_ITEM);
		DBObject srcdata = col.findOne(new BasicDBObject().append(
				WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
		DBObject tgtData = new BasicDBObject();
		tgtData.put(ProjectBudget.F_PROJECT_ID, get_id());
		tgtData.put(ProjectBudget.F_DESC, getDesc());
		tgtData.put(ProjectBudget.F_DESC_EN, getDesc_e());
		tgtData.put(ProjectBudget.F_CHILDREN,
				srcdata.get(BudgetItem.F_CHILDREN));

		return ModelService.createModelObject(tgtData, ProjectBudget.class);
	}

	private void doSetupWorkConnectionWithTemplate(ObjectId projectTemplateId,
			Map<ObjectId, DBObject> workMap, IContext context) throws Exception {
		DBCollection connDefCol = getCollection(IModelConstants.C_WORK_DEFINITION_CONNECTION);
		DBCursor cur = connDefCol.find(new BasicDBObject().append(
				WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
		List<DBObject> result = new ArrayList<DBObject>();
		while (cur.hasNext()) {
			DBObject connD = cur.next();
			ObjectId srcEnd1 = (ObjectId) connD
					.get(WorkDefinitionConnection.F_END1_ID);
			DBObject tgtEnd1Data = workMap.get(srcEnd1);
			ObjectId tgtEnd1 = null;
			if (tgtEnd1Data != null) {
				tgtEnd1 = (ObjectId) tgtEnd1Data.get(Work.F__ID);
			}
			ObjectId srcEnd2 = (ObjectId) connD
					.get(WorkDefinitionConnection.F_END2_ID);
			DBObject tgtEnd2Data = workMap.get(srcEnd2);
			ObjectId tgtEnd2 = null;
			if (tgtEnd2Data != null) {
				tgtEnd2 = (ObjectId) tgtEnd2Data.get(Work.F__ID);
			}
			if (tgtEnd1 == null || tgtEnd2 == null) {
				continue;
			}

			BasicDBObject conn = new BasicDBObject();
			conn.put(WorkConnection.F_PROJECT_ID, get_id());
			conn.put(WorkConnection.F__EDITOR, WorkConnection.EDITOR);
			conn.put(WorkConnection.F_CONNECTIONTYPE,
					connD.get(WorkDefinitionConnection.F_CONNECTIONTYPE));
			conn.put(WorkConnection.F_INTERVAL,
					connD.get(WorkDefinitionConnection.F_INTERVAL));
			conn.put(WorkConnection.F_OPERATOR,
					connD.get(WorkDefinitionConnection.F_OPERATOR));
			conn.put(WorkConnection.F_UNIT,
					connD.get(WorkDefinitionConnection.F_UNIT));
			conn.put(WorkConnection.F_END1_ID, tgtEnd1);
			conn.put(WorkConnection.F_END2_ID, tgtEnd2);
			result.add(conn);
		}

		DBCollection col = getCollection(IModelConstants.C_WORK_CONNECTION);
		WriteResult ws = col.insert(result);
		checkError(ws);
	}

	private HashMap<ObjectId, DBObject> doSetupWBSWithTemplate(
			ObjectId projectTemplateId, ObjectId wbsRootId,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {

		// 取出模板的根工作定义的_id
		DBCollection pjTempCol = getCollection(IModelConstants.C_PROJECT_TEMPLATE);

		DBObject pjTemp = pjTempCol.findOne(new BasicDBObject().append(
				ProjectTemplate.F__ID, projectTemplateId), new BasicDBObject()
				.append(ProjectTemplate.F_WORK_DEFINITON_ID, 1));
		ObjectId rootWorkDefId = (ObjectId) pjTemp
				.get(ProjectTemplate.F_WORK_DEFINITON_ID);

		HashMap<ObjectId, DBObject> worksToBeInsert = new HashMap<ObjectId, DBObject>();

		HashMap<ObjectId, DBObject> documentsToBeInsert = new HashMap<ObjectId, DBObject>();

		List<DBObject> deliverableToInsert = new ArrayList<DBObject>();

		List<DBObject[]> fileToCopy = new ArrayList<DBObject[]>();

		copyWBSTemplate(rootWorkDefId, wbsRootId, wbsRootId, get_id(), roleMap,
				worksToBeInsert, documentsToBeInsert, deliverableToInsert,
				fileToCopy, context);

		// 保存工作
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// 清除已有的非根工作定义
		WriteResult ws = workCol.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()).append(Work.F__ID,
				new BasicDBObject().append("$ne", wbsRootId)));
		checkError(ws);
		ws = workCol.insert(worksToBeInsert.values().toArray(new DBObject[0]),
				WriteConcern.NORMAL);
		checkError(ws);

		// 保存文档
		DBCollection docCol = getCollection(IModelConstants.C_DOCUMENT);
		ws = docCol.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkError(ws);

		ws = docCol.insert(documentsToBeInsert.values()
				.toArray(new DBObject[0]), WriteConcern.NORMAL);
		checkError(ws);

		// 保存交付物
		DBCollection deliCol = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = deliCol.remove(new BasicDBObject().append(
				Delieverable.F_PROJECT_ID, get_id()));
		checkError(ws);

		ws = deliCol.insert(deliverableToInsert, WriteConcern.NORMAL);
		checkError(ws);

		// 保存文件
		for (DBObject[] dbObjects : fileToCopy) {
			DBObject src = dbObjects[0];
			DBObject tgt = dbObjects[1];

			String srcDB = (String) src.get(RemoteFile.F_DB);
			String srcFilename = (String) src.get(RemoteFile.F_FILENAME);
			String srcNamespace = (String) src.get(RemoteFile.F_NAMESPACE);
			ObjectId srcID = (ObjectId) src.get(RemoteFile.F_ID);

			String tgtDB = (String) tgt.get(RemoteFile.F_DB);
			String tgtFilename = (String) tgt.get(RemoteFile.F_FILENAME);
			String tgtNamespace = (String) tgt.get(RemoteFile.F_NAMESPACE);
			ObjectId tgtID = (ObjectId) tgt.get(RemoteFile.F_ID);

			FileUtil.copyGridFSFile(srcID, srcDB, srcFilename, srcNamespace,
					tgtID, tgtDB, tgtFilename, tgtNamespace);
		}

		return worksToBeInsert;
	}

	private void checkError(WriteResult ws) throws Exception {
		String error = ws.getError();
		if (!Utils.isNullOrEmpty(error)) {
			throw new Exception(error);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getStandardSetOptions() {
		return (List<String>) getValue(F_STANDARD_SET_OPTION);
	}

	@SuppressWarnings("unchecked")
	public List<String> getProductTypeOptions() {
		return (List<String>) getValue(F_PRODUCT_TYPE_OPTION);
	}

	@SuppressWarnings("unchecked")
	public List<String> getProjectTypeOptions() {
		return (List<String>) getValue(F_PROJECT_TYPE_OPTION);
	}

	/**
	 * 
	 * @param srcParent
	 *            模板工作的Id
	 * @param tgtParentId
	 *            项目目标工作的Id
	 * @param tgtRootId
	 *            项目目标工作的根Id
	 * @param projectId
	 *            项目Id
	 * @param roleMap
	 *            角色映射，{模板角色Id:目标角色}
	 * @param worksToInsert
	 *            将要插入数据库的工作，添加的工作都需要放置到ArrayList中
	 * @param documentsToInsert
	 *            将要插入数据库的文档, {文档模板id:目标文档}
	 * @param dilerverableToInsert
	 *            将要插入数据库的交付物关系
	 * @param fileToCopy
	 *            需要复制的文件
	 * @param context
	 *            操作上下文
	 */
	private void copyWBSTemplate(ObjectId srcParent, ObjectId tgtParentId,
			ObjectId tgtRootId, ObjectId projectId,
			Map<ObjectId, DBObject> roleMap,
			Map<ObjectId, DBObject> worksToInsert,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context) {
		// 获得src的子
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(
				new BasicDBObject().append(WorkDefinition.F_PARENT_ID,
						srcParent)).sort(new SEQSorter().getBSON());
		int seq = 0;
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// 检查workdef是否符合选项过滤条件
			String optionValue = checkOptionValueFromTemplate(workdef);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// 如果是排除的，就需要跳过
				continue;

			} else {
				// ************************************************************************
				// 创建工作
				DBObject work = new BasicDBObject();

				work.put(Work.F_PROJECT_ID, projectId);

				work.put(Work.F_ROOT_ID, tgtRootId);

				work.put(Work.F_PARENT_ID, tgtParentId);

				work.put(Work.F__ID, new ObjectId());

				if (WorkDefinition.VALUE_MANDATORY.equals(optionValue)) {
					work.put(Work.F_MANDATORY, Boolean.TRUE);
				}

				// 设置工作的描述字段
				Object value = workdef.get(WorkDefinition.F_DESC);
				if (value != null) {
					work.put(Work.F_DESC, value);
				}
				value = workdef.get(WorkDefinition.F_DESC_EN);
				if (value != null) {
					work.put(Work.F_DESC_EN, value);
				}

				// 设置变更工作流
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE, value);
				}

				// 设置变更工作流是否激活
				value = workdef.get(IWorkCloneFields.F_WF_CHANGE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_CHANGE_ACTIVATED, value);
				}

				// 设置变更流程的活动执行人
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_CHANGE_ASSIGNMENT, roleMap);

				// 设置执行工作流
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE, value);
				}

				// 设置执行工作流是否激活
				value = workdef.get(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED);
				if (value != null) {
					work.put(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED, value);
				}

				// 设置执行工作流的活动执行人角色
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_EXECUTE_ASSIGNMENT, roleMap);

				// 设置负责人角色
				setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID,
						roleMap);

				// 设置参与者角色
				setRoleListField(work, workdef,
						IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

				// 设置序号
				work.put(IWorkCloneFields.F_SEQ, new Integer(seq++));

				// 设置标准工时
				value = workdef.get(IWorkCloneFields.F_STANDARD_WORKS);
				if (value != null) {
					work.put(IWorkCloneFields.F_STANDARD_WORKS, value);
				}

				// 复制设置项
				for (int i = 0; i < IWorkCloneFields.SETTING_FIELDS.length; i++) {
					value = workdef.get(IWorkCloneFields.SETTING_FIELDS[i]);
					if (value != null) {
						work.put(IWorkCloneFields.SETTING_FIELDS[i], value);
					}
				}

				BasicDBObject accountData = new BasicDBObject().append(
						"userid", context.getAccountInfo().getUserId()).append(
						"username", context.getAccountInfo().getUserName());
				work.put(Work.F__CACCOUNT, accountData);

				work.put(Work.F__CDATE, new Date());

				// 完成工作属性复制
				// ************************************************************

				// ************************************************************
				// 添加工作交付物
				// 获得模板定义的交付物定义
				DBCollection deliveryDefCol = getCollection(IModelConstants.C_DELIEVERABLE_DEFINITION);
				DBCursor deliCur = deliveryDefCol.find(
						new BasicDBObject().append(
								DeliverableDefinition.F_WORK_DEFINITION_ID,
								workdef.get(WorkDefinition.F__ID)),
						new BasicDBObject().append(
								DeliverableDefinition.F_DOCUMENT_DEFINITION_ID,
								1).append(
								DeliverableDefinition.F_OPTION_FILTERS, 1));
				while (deliCur.hasNext()) {
					DBObject delidata = deliCur.next();
					// 检查交付物是否符合选项过滤条件
					String documentOptionValue = checkOptionValueFromTemplate(delidata);
					if (DeliverableDefinition.VALUE_EXCLUDE
							.equals(documentOptionValue)) {
						// 如果是排除的，就需要跳过
						continue;
					} else {
						// 根据模板的交付物定义创建交付物关系
						DBObject deliverableData = new BasicDBObject();

						// 设置项目Id
						deliverableData.put(Delieverable.F_PROJECT_ID,
								projectId);

						// 设置工作Id
						deliverableData.put(Delieverable.F_WORK_ID,
								work.get(Work.F__ID));

						// 设置是否必须
						if (DeliverableDefinition.VALUE_MANDATORY
								.equals(documentOptionValue)) {
							deliverableData.put(Delieverable.F_MANDATORY,
									Boolean.TRUE);
						}

						// 获得文档模板
						ObjectId documentTemplateId = (ObjectId) delidata
								.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
						// 检查该文档模板是否已经创建了文档
						DBObject documentData = documentsToInsert
								.get(documentTemplateId);
						if (documentData == null) {
							// 如果没有创建，需要创建该文档
							documentData = copyDocumentFromTemplate(
									documentsToInsert, fileToCopy,
									documentTemplateId, projectId);
						}
						documentsToInsert.put(documentTemplateId, documentData);
						ObjectId documentId = (ObjectId) documentData
								.get(Document.F__ID);
						deliverableData.put(Delieverable.F_DOCUMENT_ID,
								documentId);
						dilerverableToInsert.add(deliverableData);
					}
				}

				worksToInsert.put((ObjectId) workdef.get(WorkDefinition.F__ID),
						work);

				// 处理子工作
				copyWBSTemplate((ObjectId) workdef.get(WorkDefinition.F__ID),
						(ObjectId) work.get(Work.F__ID), tgtRootId, projectId,
						roleMap, worksToInsert, documentsToInsert,
						dilerverableToInsert, fileToCopy, context);
			}

		}

	}

	private DBObject copyDocumentFromTemplate(
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject[]> fileToCopy, ObjectId documentTemplateId,
			ObjectId projectId) {
		DBObject documentData;
		DBCollection docdCol = getCollection(IModelConstants.C_DOCUMENT_DEFINITION);
		DBObject documentTemplate = docdCol.findOne(new BasicDBObject().append(
				Document.F__ID, documentTemplateId));
		documentData = new BasicDBObject();

		documentData.put(Document.F__ID, new ObjectId());

		documentData.put(Document.F_PROJECT_ID, projectId);

		Object value = documentTemplate.get(DocumentDefinition.F_DESC);
		if (value != null) {
			documentData.put(Document.F_DESC, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DESC_EN);
		if (value != null) {
			documentData.put(Document.F_DESC_EN, value);
		}

		value = new Boolean(Boolean.TRUE.equals(documentTemplate
				.get(DocumentDefinition.F_ATTACHMENT_CANNOT_EMPTY)));
		documentData.put(Document.F_ATTACHMENT_CANNOT_EMPTY, value);

		value = documentTemplate.get(DocumentDefinition.F_DESCRIPTION);
		if (value != null) {
			documentData.put(Document.F_DESCRIPTION, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DOCUMENT_EDITORID);
		if (value != null) {
			documentData.put(Document.F__EDITOR, value);
		}

		// 根据文档的附件创建文件
		BasicBSONList templateFiles = (BasicBSONList) documentTemplate
				.get(DocumentDefinition.F_TEMPLATEFILE);
		if (templateFiles != null) {
			BasicBSONList documentFiles = new BasicBSONList();
			for (int i = 0; i < templateFiles.size(); i++) {
				DBObject templateFile = (DBObject) templateFiles.get(i);
				DBObject documentFile = new BasicDBObject();
				documentFile.put(RemoteFile.F_ID, new ObjectId());
				documentFile.put(RemoteFile.F_FILENAME,
						templateFile.get(RemoteFile.F_FILENAME));
				documentFile.put(RemoteFile.F_NAMESPACE,
						Document.FILE_NAMESPACE);
				documentFile.put(RemoteFile.F_DB, Document.FILE_DB);
				documentFiles.add(documentFile);
				fileToCopy.add(new DBObject[] { templateFile, documentFile });
			}
			documentData.put(Document.F_VAULT, documentFiles);
		}
		// 完成文档创建
		documentsToInsert.put(documentTemplateId, documentData);

		return documentData;
	}

	private void setRoleDBObjectField(DBObject work, DBObject workdef,
			String fieldName, Map<ObjectId, DBObject> roleMap) {
		Object value = workdef.get(fieldName);
		if (value instanceof DBObject) {
			DBObject dbo = (DBObject) value;
			BasicDBObject actors = new BasicDBObject();
			Iterator<String> iter = dbo.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				ObjectId roleId = (ObjectId) dbo.get(key);
				DBObject tgtRole = roleMap.get(roleId);
				ObjectId tgtRoleId = (ObjectId) tgtRole.get(ProjectRole.F__ID);
				actors.put(key, tgtRoleId);
			}
			work.put(fieldName, actors);
		}
	}

	private void setRoleListField(DBObject work, DBObject workdef,
			String fieldName, Map<ObjectId, DBObject> roleMap) {
		Object value = workdef.get(fieldName);
		if (value instanceof BasicBSONList) {
			BasicBSONList participates = new BasicBSONList();
			BasicBSONList valueList = (BasicBSONList) value;
			for (int i = 0; i < valueList.size(); i++) {
				DBObject srcRole = (DBObject) valueList.get(i);
				ObjectId srcRoleId = (ObjectId) srcRole.get(ProjectRole.F__ID);
				DBObject tgtRole = roleMap.get(srcRoleId);
				if (tgtRole != null) {
					Object tgtRoleId = tgtRole.get(ProjectRole.F__ID);
					if (tgtRoleId != null) {
						participates.add(tgtRoleId);
					}
				}
			}
			work.put(fieldName, participates);
		}
	}

	private void setRoleField(DBObject work, DBObject workdef,
			String roleFieldName, Map<ObjectId, DBObject> roleMap) {
		ObjectId srcRoleId = (ObjectId) workdef.get(roleFieldName);
		if (srcRoleId != null) {
			DBObject tgtRole = roleMap.get(srcRoleId);
			if (tgtRole != null) {
				Object value = tgtRole.get(ProjectRole.F__ID);
				if (value != null) {
					work.put(roleFieldName, value);
				}
			}
		}
	}

	private String checkOptionValueFromTemplate(DBObject optionHost) {
		Object filters = optionHost.get(WorkDefinition.F_OPTION_FILTERS);
		if (filters instanceof BasicBSONList) {

			BasicBSONList filtersValue = (BasicBSONList) filters;
			// 检查标准集
			List<String> optionValueSet = getStandardSetOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_STANDARD);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}

			// 检查产品选项集
			optionValueSet = getProductTypeOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_PRODUCTTYPE);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}

			// 检查项目选项集
			optionValueSet = getProjectTypeOptions();
			if (optionValueSet != null) {
				for (int i = 0; i < optionValueSet.size(); i++) {
					String optionValueItem = optionValueSet.get(i);
					BasicDBObject item = new BasicDBObject();
					item.put(WorkDefinition.SF_OPTIONSET,
							WorkDefinition.OPTIONSET_NAME_PROJECTTYPE);
					item.put(WorkDefinition.SF_OPTION, optionValueItem);
					item.put(WorkDefinition.SF_VALUE,
							WorkDefinition.VALUE_EXCLUDE);
					if (filtersValue.contains(item)) {
						return WorkDefinition.VALUE_EXCLUDE;
					} else {
						item.put(WorkDefinition.SF_VALUE,
								WorkDefinition.VALUE_MANDATORY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MANDATORY;
						}
					}
				}
			}
		}
		return WorkDefinition.VALUE_OPTION;
	}

	private Map<ObjectId, DBObject> doSetupRolesWithTemplate(
			ObjectId projectTemplateId, IContext context) throws Exception {
		DBCollection col_roled = getCollection(IModelConstants.C_ROLE_DEFINITION);
		DBCollection col_role = getCollection(IModelConstants.C_PROJECT_ROLE);
		// 删除项目现有的角色
		col_role.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
				get_id()));

		// 准备返回值
		HashMap<ObjectId, DBObject> result = new HashMap<ObjectId, DBObject>();

		// 查找模板的角色定义
		DBCursor cur = col_roled.find(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplateId));
		while (cur.hasNext()) {
			DBObject roleddata = cur.next();

			// 创建项目角色对象
			ProjectRole prole = makeProjectRole(null);

			// 给出将要创建的项目角色的_id
			ObjectId proleId = new ObjectId();
			prole.setValue(F__ID, proleId);
			// 如果是组织角色
			Object roleId = roleddata
					.get(RoleDefinition.F_ORGANIZATION_ROLE_ID);
			if (roleId != null) {
				// 设置为组织角色
				prole.setValue(ProjectRole.F_ORGANIZATION_ROLE_ID, roleId);
			} else {
				// 设置为项目角色
				prole.setValue(ProjectRole.F_ROLE_NUMBER,
						roleddata.get(RoleDefinition.F_ROLE_NUMBER));
				prole.setValue(ProjectRole.F_DESC,
						roleddata.get(RoleDefinition.F_DESC));
			}
			prole.setValue(ProjectRole.F__CACCOUNT, context.getAccountInfo()
					.getUserId());
			prole.setValue(ProjectRole.F__CDATE, new Date());

			result.put((ObjectId) roleddata.get(RoleDefinition.F__ID),
					prole.get_data());
		}

		if (!result.isEmpty()) {
			DBObject[] insertData = result.values().toArray(new DBObject[0]);

			// 插入到数据库
			WriteResult ws = col_role.insert(insertData, WriteConcern.NORMAL);
			checkError(ws);
		}

		return result;
	}

	public ProjectRole makeProjectRole(ProjectRole po) {
		if (po == null) {
			po = ModelService.createModelObject(ProjectRole.class);
		}
		po.setValue(ProjectRole.F_PROJECT_ID, get_id());
		return po;
	}

	public ProjectBudget getBudget() {
		DBCollection col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		DBObject data = col.findOne(new BasicDBObject().append(
				ProjectBudget.F_PROJECT_ID, get_id()));
		return ModelService.createModelObject(data, ProjectBudget.class);
	}

	@Override
	public boolean canDelete(IContext context) {
		
		// TODO Auto-generated method stub
		return super.canDelete(context);
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		// 删除work
		DBCollection col = getCollection(IModelConstants.C_WORK);
		WriteResult ws = col.remove(new BasicDBObject().append(
				Work.F_PROJECT_ID, get_id()));
		checkError(ws);

		// 删除workconnection
		col = getCollection(IModelConstants.C_WORK_CONNECTION);
		ws = col.remove(new BasicDBObject().append(Work.F_PROJECT_ID, get_id()));
		checkError(ws);

		// 删除预算
		col = getCollection(IModelConstants.C_PROJECT_BUDGET);
		ws = col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
				get_id()));
		checkError(ws);

		// 删除role
		col = getCollection(IModelConstants.C_PROJECT_ROLE);
		ws = col.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
				get_id()));
		checkError(ws);

		// 删除交付物
		col = getCollection(IModelConstants.C_DELIEVERABLE);
		ws = col.remove(new BasicDBObject().append(Delieverable.F_PROJECT_ID,
				get_id()));
		checkError(ws);

		// 删除文档
		col = getCollection(IModelConstants.C_DOCUMENT);
		ws = col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
				get_id()));
		checkError(ws);

		super.doRemove(context);
	}
}
