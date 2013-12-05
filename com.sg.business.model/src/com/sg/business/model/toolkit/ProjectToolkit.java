package com.sg.business.model.toolkit;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
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
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;

public class ProjectToolkit {
	public static boolean checkProcessInternal(PrimaryObject po,
			IProcessControl pc, List<ICheckListItem> result,
			Map<ObjectId, List<PrimaryObject>> raMap, String title,
			String process, String editorId, String pageId) {
		boolean passed = true;
		List<PrimaryObject> ra;
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			Object data;// project
			PrimaryObject selection;// work or project
			if (po instanceof Work) {
				data = ((Work) po).getProject();
				selection = po;
			} else {
				data = po;
				selection = po;
			}
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
						checkItem.setData(data);
						checkItem.setEditorId(editorId);
						checkItem.setEditorPageId(pageId);
						checkItem.setSelection(selection);
						result.add(checkItem);
						passed = false;
					} else {
						ra = raMap.get(role.get_id());
						if (ra == null || ra.isEmpty()) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程活动执行角色没有对应人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"\n请在提交前设定。", ICheckListItem.ERROR);
							checkItem.setData(data);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection(selection);
							result.add(checkItem);
							passed = false;
						} else if (ra.size() > 1) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程的活动指定了多名人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"流程启动后这些人员中的任一人都将可执行该活动。"

									+ "\n如果您不希望这样，请在提交前设定",
									ICheckListItem.WARRING);
							checkItem.setData(data);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection(selection);
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
				if (na.forceAssignment()) {
					String nap = na.getNodeActorParameter();
					String userId = pc.getProcessActionActor(process, nap);
					if (userId == null) {
						return false;
					}
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
		// 设置里程碑任务
		value = workdef.get(WorkDefinition.F_MILESTONE);
		if (value != null) {
			work.put(Work.F_MILESTONE, value);
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

		// 设置提醒时间
		value = workdef.get(IWorkCloneFields.F_REMIND_BEFORE);
		if (value != null) {
			work.put(IWorkCloneFields.F_REMIND_BEFORE, value);
		}

		// 复制设置项
		if (new Integer(WorkDefinition.WORK_TYPE_GENERIC).equals(workdef
				.get(WorkDefinition.F_WORK_TYPE))) {
			work.put(IWorkCloneFields.F_SETTING_CAN_ADD_DELIVERABLES,
					Boolean.TRUE);
			work.put(IWorkCloneFields.F_SETTING_CAN_BREAKDOWN, Boolean.TRUE);
			work.put(IWorkCloneFields.F_SETTING_CAN_MODIFY_PLANWORKS,
					Boolean.TRUE);
		} else {
			for (int i = 0; i < IWorkCloneFields.SETTING_FIELDS.length; i++) {
				value = workdef.get(IWorkCloneFields.SETTING_FIELDS[i]);
				if (value != null) {
					work.put(IWorkCloneFields.SETTING_FIELDS[i], value);
				}
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

				// 设置类型
				String deliverableType = (String) delidata
						.get(DeliverableDefinition.F_TYPE);
				deliverableData.put(Deliverable.F_TYPE, deliverableType);

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
							folderRootId, context);
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
		String setOption = WorkDefinition.VALUE_OPTION;
		Object filters = optionHost.get(WorkDefinition.F_OPTION_FILTERS);
		if (filters instanceof BasicBSONList) {
			BasicBSONList filtersValue = (BasicBSONList) filters;
			// 检查标准集
			List<String> optionValueSet = project.getStandardSetOptions();
			String set = checkOptionValueFromSet(filtersValue, optionValueSet,
					WorkDefinition.OPTIONSET_NAME_STANDARD);
			if (WorkDefinition.VALUE_EXCLUDE.equals(set)) {
				return WorkDefinition.VALUE_EXCLUDE;
			} else if (WorkDefinition.VALUE_MANDATORY.equals(set)) {
				setOption = WorkDefinition.VALUE_MANDATORY;
			}

			// 检查产品选项集
			optionValueSet = project.getProductTypeOptions();
			set = checkOptionValueFromSet(filtersValue, optionValueSet,
					WorkDefinition.OPTIONSET_NAME_PRODUCTTYPE);
			if (WorkDefinition.VALUE_EXCLUDE.equals(set)) {
				return WorkDefinition.VALUE_EXCLUDE;
			} else if (WorkDefinition.VALUE_MANDATORY.equals(set)) {
				setOption = WorkDefinition.VALUE_MANDATORY;
			}

			// 检查项目选项集
			optionValueSet = project.getProjectTypeOptions();
			set = checkOptionValueFromSet(filtersValue, optionValueSet,
					WorkDefinition.OPTIONSET_NAME_PROJECTTYPE);
			if (WorkDefinition.VALUE_EXCLUDE.equals(set)) {
				return WorkDefinition.VALUE_EXCLUDE;
			} else if (WorkDefinition.VALUE_MANDATORY.equals(set)) {
				setOption = WorkDefinition.VALUE_MANDATORY;
			}
		}
		return setOption;
	}

	private static String checkOptionValueFromSet(BasicBSONList filtersValue,
			List<String> optionValueSet, String optionsetSetName) {
		if (optionValueSet != null) {
			for (int i = 0; i < optionValueSet.size(); i++) {
				String optionValueItem = optionValueSet.get(i);
				BasicDBObject item = new BasicDBObject();
				item.put(WorkDefinition.SF_OPTIONSET, optionsetSetName);
				item.put(WorkDefinition.SF_OPTION, optionValueItem);
				item.put(WorkDefinition.SF_VALUE, WorkDefinition.VALUE_EXCLUDE);
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
				Assert.isNotNull(tgtRole,
						"目标角色不存在，请检查项目模板的工作：" + work.get(Work.F_DESC));
				ObjectId tgtRoleId = (ObjectId) tgtRole.get(ProjectRole.F__ID);
				actors.put(key, tgtRoleId);
				// 标记该角色已使用
				tgtRole.put("used", Boolean.TRUE);
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
				Assert.isNotNull(tgtRole,
						"目标角色不存在，请检查项目模板的工作：" + work.get(Work.F_DESC));
				if (tgtRole != null) {
					Object tgtRoleId = tgtRole.get(ProjectRole.F__ID);
					if (tgtRoleId != null) {
						participates.add(new BasicDBObject().append("_id",
								tgtRoleId));
						// 标记该角色已使用
						tgtRole.put("used", Boolean.TRUE);
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
			Assert.isNotNull(tgtRole,
					"目标角色不存在，请检查项目模板的工作：" + work.get(Work.F_DESC));
			if (tgtRole != null) {
				Object value = tgtRole.get(ProjectRole.F__ID);
				if (value != null) {
					work.put(roleFieldName, value);
					// 标记该角色已使用
					tgtRole.put("used", Boolean.TRUE);
				}
			}
		}
	}

	private static DBObject copyDocumentFromTemplate(
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject[]> fileToCopy, ObjectId documentTemplateId,
			ObjectId projectId, ObjectId folderRootId, IContext context) {
		DBCollection docdCol = getCollection(IModelConstants.C_DOCUMENT_DEFINITION);
		DBObject documentTemplate = docdCol.findOne(new BasicDBObject().append(
				Document.F__ID, documentTemplateId));

		Document document = ModelService.createModelObject(Document.class);

		document.setValue(Document.F__ID, new ObjectId());

		document.setValue(Document.F_PROJECT_ID, projectId);

		document.setValue(Document.F_FOLDER_ID, folderRootId);

		Object value = documentTemplate.get(DocumentDefinition.F_DESC);
		if (value != null) {
			document.setValue(Document.F_DESC, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DESC_EN);
		if (value != null) {
			document.setValue(Document.F_DESC_EN, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DOCUMENT_TYPE);
		if (value != null) {
			document.setValue(Document.F_DOCUMENT_TYPE, value);
		}

		value = new Boolean(Boolean.TRUE.equals(documentTemplate
				.get(DocumentDefinition.F_ATTACHMENT_CANNOT_EMPTY)));
		document.setValue(Document.F_ATTACHMENT_CANNOT_EMPTY, value);

		value = documentTemplate.get(DocumentDefinition.F_DESCRIPTION);
		if (value != null) {
			document.setValue(Document.F_DESCRIPTION, value);
		}

		value = documentTemplate.get(DocumentDefinition.F_DOCUMENT_EDITORID);
		if (value != null) {
			document.setValue(Document.F__EDITOR, value);
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
			document.setValue(Document.F_VAULT, documentFiles);
		}

		// 处理文档的默认值
		document.initVerStatus();
		document.initVersionNumber();

		// 处理文档的默认值
		document.initInsertDefault(document.get_data(), context);

		// 完成文档创建
		documentsToInsert.put(documentTemplateId, document.get_data());

		return document.get_data();
	}

	private static DBCollection getCollection(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

	/**
	 * 创建项目，并将该工作添加到项目的根工作下。并返回创建的项目
	 * 
	 * @param work
	 *            :要添加的工作（该工作已被持久化）
	 * @param desc
	 *            :项目名称
	 * @param description
	 *            :项目说明
	 * @param launchorg_id
	 *            :发起部门ID
	 * @param org_id
	 *            :项目管理部门ID
	 * @param prj_manager
	 *            :项目经理
	 * @param workOrder
	 *            :工作令号
	 * @param projecttemplate_id
	 *            :项目模板ID
	 * @param planfinish
	 *            :计划完成时间
	 * @param planstart
	 *            :计划开始时间
	 * @param projecttype
	 *            :项目类型选项集
	 * @param standardset
	 *            :适用标准集
	 * @param producttype
	 *            :产品类型选项集
	 * @param context
	 *            :
	 * @return :
	 * @throws Exception
	 */
	public static Project doCreateNewProject(Work work, String desc,
			String description, List<ObjectId> launchorg_id, ObjectId org_id,
			String prj_manager, String workOrder, ObjectId projecttemplate_id,
			Object planfinish, Object planstart, Object projecttype,
			Object standardset, Object producttype, IContext context)
			throws Exception {

		// 校验是否可以依据传入的参数创建项目
		if (workOrder == null) {
			throw new Exception("未录入工作令号无法新建项目");
		}
		if (prj_manager == null) {
			throw new Exception("未录入项目经理无法新建项目");
		}
		if (desc == null) {
			throw new Exception("未录入项目名称无法新建项目");
		}
		if (launchorg_id == null || launchorg_id.size() == 0) {
			throw new Exception("未录入项目发起部门无法新建项目");
		}
		if (org_id == null) {
			throw new Exception("未录入项目管理部门无法新建项目");
		}
		if (planfinish == null) {
			throw new Exception("未录入项目计划完成时间无法新建项目");
		}
		if (planstart == null) {
			throw new Exception("未录入项目计划开始时间无法新建项目");
		}

		// 依据工作创建项目
		BasicDBObject projectObject = new BasicDBObject();
		projectObject.put(Project.F_DESC, desc);
		projectObject.put(Project.F_DESCRIPTION, description);
		projectObject.put(Project.F_LAUNCH_ORGANIZATION, launchorg_id);
		projectObject.put(Project.F_FUNCTION_ORGANIZATION, org_id);
		projectObject.put(Project.F_CHARGER, prj_manager);

		/**
		 * BUG: ZHONGHUA 工作令号字段是 数组类型
		 */
		// projectObject.put(Project.F_WORK_ORDER, workOrder);
		projectObject.put(Project.F_WORK_ORDER, new String[] { workOrder });

		projectObject.put(Project.F_PROJECT_TEMPLATE_ID, projecttemplate_id);
		projectObject.put(Project.F_PLAN_FINISH, planfinish);
		projectObject.put(Project.F_PLAN_START, planstart);
		projectObject.put(Project.F__EDITOR, Project.EDITOR_CREATE_PLAN);

		if (projecttype != null) {
			projectObject.put(Project.F_PROJECT_TYPE_OPTION, projecttype);
		}
		if (standardset != null) {
			projectObject.put(Project.F_STANDARD_SET_OPTION, standardset);
		}
		if (producttype != null) {
			projectObject.put(Project.F_PRODUCT_TYPE_OPTION, producttype);
		}
		List<PrimaryObject> projectList = work.getRelationByCondition(
				Project.class, projectObject);
		if (projectList != null && projectList.size() > 0) {
			return null;
		}
		Project project = ModelService.createModelObject(projectObject,
				Project.class);
		project.doSave(context);

		// 将工作添加到创建的项目中
		ProjectToolkit.doProjectAddStandloneWork(work, project, context);

		return project;
	}

	/**
	 * 将工作添加到项目的根工作中
	 * 
	 * @param work
	 *            :要添加的工作（该工作已被持久化）
	 * @param project
	 *            :加如工作的项目（该项目已被持久化）
	 * @param context
	 * @throws Exception
	 */
	public static void doProjectAddStandloneWork(Work work, Project project,
			IContext context) throws Exception {
		if (project == null) {
			throw new Exception("请该项目未创建");
		}

		ObjectId projectId = project.get_id();
		Work wbsRoot = project.getWBSRoot();

		// 将工作添加到项目根工作中
		work.setValue(Work.F_ROOT_ID, wbsRoot.get_id());
		work.setValue(Work.F_PARENT_ID, wbsRoot.get_id());
		work.setValue(Work.F_PROJECT_ID, projectId);
		int seq = wbsRoot.getMaxChildSeq();
		work.setValue(Work.F_SEQ, new Integer(seq + 1));
		work.doSave(context);

		// 添加工作的交付物
		DBCollection col;
		List<PrimaryObject> deliverableList = work.getDeliverable();
		if (deliverableList != null && deliverableList.size() > 0) {
			col = getCollection(IModelConstants.C_DELIEVERABLE);
			ObjectId[] deliverableIdList = new ObjectId[deliverableList.size()];
			for (int i = 0; i < deliverableList.size(); i++) {
				PrimaryObject po = deliverableList.get(i);
				deliverableIdList[i] = po.get_id();
			}
			WriteResult ws = col.update(new BasicDBObject().append(
					Deliverable.F__ID,
					new BasicDBObject().append("$in", deliverableIdList)),
					new BasicDBObject().append(
							"$set",
							new BasicDBObject().append(
									Deliverable.F_PROJECT_ID, projectId)
									.append(Deliverable.F_TYPE,
											Deliverable.TYPE_REFERENCE)),
					false, true);
			String error = ws.getError();
			if (error != null) {
				throw new Exception(error);
			}
		}

		// 添加工作的文档
		List<PrimaryObject> documentList = work.getDeliverableDocuments();
		if (documentList != null && documentList.size() > 0) {
			col = getCollection(IModelConstants.C_DOCUMENT);
			ObjectId[] documentIdList = new ObjectId[documentList.size()];
			for (int i = 0; i < documentList.size(); i++) {
				PrimaryObject po = documentList.get(i);
				documentIdList[i] = po.get_id();
			}
			WriteResult ws = col.update(new BasicDBObject().append(
					Document.F__ID,
					new BasicDBObject().append("$in", documentIdList)),
					new BasicDBObject().append(
							"$set",
							new BasicDBObject().append(Document.F_PROJECT_ID,
									projectId).append(Document.F_FOLDER_ID,
									project.getFolderRootId())), false, true);
			String error = ws.getError();
			if (error != null) {
				throw new Exception(error);
			}
		}
	}

	public static void updateProjectSalesData() {
		DBCollection colPd = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PRODUCT);
		String dateCode = String.format("%1$tY%1$tm", Calendar.getInstance());
		DBCursor cur = colPd.find(new BasicDBObject().append(
				ProductItem.F_SALES_DATA_UPDATE,
				new BasicDBObject().append("$ne", dateCode)));
		while (cur.hasNext()) {
			DBObject prodData = cur.next();
			ProductItem pd = ModelService.createModelObject(prodData,
					ProductItem.class);
			pd.doCalculateSalesData();
		}

	}

}
