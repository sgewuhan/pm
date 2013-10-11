package com.sg.business.model.toolkit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Deliverable;
import com.sg.business.model.DeliverableDefinition;
import com.sg.business.model.Document;
import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;

public class ProjectToolkit {
	public static boolean checkProcessInternal(Project project,
			IProcessControl pc, List<ICheckListItem> result,
			Map<ObjectId, List<PrimaryObject>> raMap, String title,
			String process, String editorId, String pageId) {
		boolean passed = true;
		List<PrimaryObject> ra;
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				String userId = pc.getProcessActionActor(process, nap);
				if (userId == null) {
					// 检查角色
					AbstractRoleDefinition role = pc
							.getProcessActionAssignment(process, nap);
					if (role == null) {
						CheckListItem checkItem = new CheckListItem(title,
								"流程活动无法确定执行人。" + "活动名称：[" + na.getNodeName()
										+ "]", "请在提交前设定。", ICheckListItem.ERROR);
						checkItem.setData(project);
						checkItem.setEditorId(editorId);
						checkItem.setEditorPageId(pageId);
						checkItem.setSelection(project);
						result.add(checkItem);
						passed = false;
					} else {
						ra = raMap.get(role.get_id());
						if (ra == null || ra.isEmpty()) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程活动执行角色没有对应人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"\n请在提交前设定。", ICheckListItem.ERROR);
							checkItem.setData(project);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection(project);
							result.add(checkItem);
							passed = false;
						} else if (ra.size() > 1) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程的活动指定了多名人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"流程启动后这些人员中的任一人都将可执行该活动。"

									+ "\n如果您不希望这样，请在提交前设定",
									ICheckListItem.WARRING);
							checkItem.setData(project);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection(project);
							result.add(checkItem);
							passed = false;
						}
					}
				}
			}
		}
		return passed;
	}

	public static boolean checkProcessInternal(IProcessControl pc,
			String process) {
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				String userId = pc.getProcessActionActor(process, nap);
				if (userId == null) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkProcessInternal(IProcessControl pc,
			String process, ProjectRole projectRole) {
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// 检查角色
				AbstractRoleDefinition role = pc.getProcessActionAssignment(
						process, nap);
				if (projectRole.getOrganizationRoleId().equals(
						role.getOrganizationRoleId())) {
					return true;
				}

			}
		}
		return false;
	}

	public static boolean checkProcessInternal(IProcessControl pc,
			String process, String userId) {
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				String processUserId = pc.getProcessActionActor(process, nap);
				if (userId.equals(processUserId)) {
					return true;
				}
			}
		}
		return false;
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
	 * @param folderRootId
	 *            项目目标文档的根Id
	 * @param documentsToInsert
	 *            将要插入数据库的文档, {文档模板id:目标文档}
	 * @param dilerverableToInsert
	 *            将要插入数据库的交付物关系
	 * @param fileToCopy
	 *            需要复制的文件
	 * @param context
	 *            操作上下文
	 */
	public static void copyWBSTemplate(ObjectId srcParent,
			ObjectId tgtParentId, ObjectId tgtRootId, Project project,
			Map<ObjectId, DBObject> roleMap,
			Map<ObjectId, DBObject> worksToInsert, ObjectId folderRootId,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context) {
		ObjectId projectId = project.get_id();
		// 获得src的子
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(
				new BasicDBObject().append(WorkDefinition.F_PARENT_ID,
						srcParent)).sort(new SEQSorter().getBSON());
		int seq = 0;
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// 检查workdef是否符合选项过滤条件
			String optionValue = checkOptionValueFromTemplate(workdef, project);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// 如果是排除的，就需要跳过
				continue;

			} else {
				DBObject work = getWorkFromWorkDefinition(tgtParentId,
						tgtRootId, project, roleMap, folderRootId,
						documentsToInsert, dilerverableToInsert, fileToCopy,
						context, projectId, seq, workdef, optionValue);

				worksToInsert.put((ObjectId) workdef.get(WorkDefinition.F__ID),
						work);

				seq++;

				// 处理子工作
				copyWBSTemplate((ObjectId) workdef.get(WorkDefinition.F__ID),
						(ObjectId) work.get(Work.F__ID), tgtRootId, project,
						roleMap, worksToInsert, folderRootId,
						documentsToInsert, dilerverableToInsert, fileToCopy,
						context);
			}

		}
	}

	public static DBObject getWorkFromWorkDefinition(ObjectId tgtParentId,
			ObjectId tgtRootId, Project project,
			Map<ObjectId, DBObject> roleMap, ObjectId folderRootId,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context, ObjectId projectId, int seq, DBObject workdef,
			String optionValue) {
		// ************************************************************************
		// 创建工作
		DBObject work = new BasicDBObject();

		work.put(Work.F_PROJECT_ID, projectId);

		work.put(Work.F_ROOT_ID, tgtRootId);

		work.put(Work.F_PARENT_ID, tgtParentId);

		work.put(Work.F_WORK_TYPE, Work.WORK_TYPE_PROJECT);

		work.put(Work.F__ID, new ObjectId());
		/**
		 * BUG:10006
		 */
		work.put(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);

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
		setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID, roleMap);

		// 设置指派人角色
		setRoleField(work, workdef,
				IWorkCloneFields.F_ASSIGNMENT_CHARGER_ROLE_ID, roleMap);

		// 设置参与者角色
		setRoleListField(work, workdef,
				IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

		// 设置序号
		work.put(IWorkCloneFields.F_SEQ, new Integer(seq));

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

		BasicDBObject accountData = new BasicDBObject().append("userid",
				context.getAccountInfo().getUserId()).append("username",
				context.getAccountInfo().getUserName());
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
						DeliverableDefinition.F_DOCUMENT_DEFINITION_ID, 1)
						.append(DeliverableDefinition.F_OPTION_FILTERS, 1));
		while (deliCur.hasNext()) {
			DBObject delidata = deliCur.next();
			// 检查交付物是否符合选项过滤条件
			String documentOptionValue = checkOptionValueFromTemplate(delidata,
					project);
			if (DeliverableDefinition.VALUE_EXCLUDE.equals(documentOptionValue)) {
				// 如果是排除的，就需要跳过
				continue;
			} else {
				// 根据模板的交付物定义创建交付物关系
				DBObject deliverableData = new BasicDBObject();

				// 设置项目Id
				deliverableData.put(Deliverable.F_PROJECT_ID, projectId);

				// 设置工作Id
				deliverableData
						.put(Deliverable.F_WORK_ID, work.get(Work.F__ID));

				// 设置是否必须
				if (DeliverableDefinition.VALUE_MANDATORY
						.equals(documentOptionValue)) {
					deliverableData.put(Deliverable.F_MANDATORY, Boolean.TRUE);
				}

				// 获得文档模板
				ObjectId documentTemplateId = (ObjectId) delidata
						.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
				// 检查该文档模板是否已经创建了文档
				DBObject documentData = documentsToInsert
						.get(documentTemplateId);
				if (documentData == null) {
					// 如果没有创建，需要创建该文档
					documentData = copyDocumentFromTemplate(documentsToInsert,
							fileToCopy, documentTemplateId, projectId,
							folderRootId);
				}
				documentsToInsert.put(documentTemplateId, documentData);
				ObjectId documentId = (ObjectId) documentData
						.get(Document.F__ID);
				deliverableData.put(Deliverable.F_DOCUMENT_ID, documentId);
				dilerverableToInsert.add(deliverableData);
			}
		}
		return work;
	}

	private static String checkOptionValueFromTemplate(DBObject optionHost,
			Project project) {
		Object filters = optionHost.get(WorkDefinition.F_OPTION_FILTERS);
		if (filters instanceof BasicBSONList) {

			BasicBSONList filtersValue = (BasicBSONList) filters;
			// 检查标准集
			List<String> optionValueSet = project.getStandardSetOptions();
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
			optionValueSet = project.getProductTypeOptions();
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
			optionValueSet = project.getProjectTypeOptions();
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

	/**
	 * 
	 * @param work
	 * @param workdef
	 * @param fieldName
	 * @param roleMap
	 */
	public static void setRoleDBObjectField(DBObject work, DBObject workdef,
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

	private static void setRoleListField(DBObject work, DBObject workdef,
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
						participates.add(new BasicDBObject().append("_id",
								tgtRoleId));
					}
				}
			}
			work.put(fieldName, participates);
		}
	}

	private static void setRoleField(DBObject work, DBObject workdef,
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

	private static DBObject copyDocumentFromTemplate(
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject[]> fileToCopy, ObjectId documentTemplateId,
			ObjectId projectId, ObjectId folderRootId) {
		DBObject documentData;
		DBCollection docdCol = getCollection(IModelConstants.C_DOCUMENT_DEFINITION);
		DBObject documentTemplate = docdCol.findOne(new BasicDBObject().append(
				Document.F__ID, documentTemplateId));
		documentData = new BasicDBObject();

		documentData.put(Document.F__ID, new ObjectId());

		documentData.put(Document.F_PROJECT_ID, projectId);

		documentData.put(Document.F_FOLDER_ID, folderRootId);

		Object value = documentTemplate.get(DocumentDefinition.F_DESC);
		if (value != null) {
			documentData.put(Document.F_DESC, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DESC_EN);
		if (value != null) {
			documentData.put(Document.F_DESC_EN, value);
		}
		
		value = documentTemplate.get(DocumentDefinition.F_DOCUMENT_TYPE);
		if (value != null) {
			documentData.put(Document.F_DOCUMENT_TYPE, value);
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

	private static DBCollection getCollection(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
