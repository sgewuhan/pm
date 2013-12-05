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
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
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
					// ����ɫ
					AbstractRoleDefinition role = pc
							.getProcessActionAssignment(process, nap);
					if (role == null) {
						CheckListItem checkItem = new CheckListItem(title,
								"���̻�޷�ȷ��ִ���ˡ�" + "����ƣ�[" + na.getNodeName()
										+ "]", "�����ύǰ�趨��", ICheckListItem.ERROR);
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
									"���̻ִ�н�ɫû�ж�Ӧ��Ա��" + "����ƣ�["
											+ na.getNodeName() + "]",
									"\n�����ύǰ�趨��", ICheckListItem.ERROR);
							checkItem.setData(data);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection(selection);
							result.add(checkItem);
							passed = false;
						} else if (ra.size() > 1) {
							CheckListItem checkItem = new CheckListItem(title,
									"���̵Ļָ���˶�����Ա��" + "����ƣ�["
											+ na.getNodeName() + "]",
									"������������Щ��Ա�е���һ�˶�����ִ�иû��"

									+ "\n�������ϣ�������������ύǰ�趨",
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
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
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
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// ����ɫ
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
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
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
	 *            ģ�幤����Id
	 * @param tgtParentId
	 *            ��ĿĿ�깤����Id
	 * @param tgtRootId
	 *            ��ĿĿ�깤���ĸ�Id
	 * @param projectId
	 *            ��ĿId
	 * @param roleMap
	 *            ��ɫӳ�䣬{ģ���ɫId:Ŀ���ɫ}
	 * @param worksToInsert
	 *            ��Ҫ�������ݿ�Ĺ�������ӵĹ�������Ҫ���õ�ArrayList��
	 * @param folderRootId
	 *            ��ĿĿ���ĵ��ĸ�Id
	 * @param documentsToInsert
	 *            ��Ҫ�������ݿ���ĵ�, {�ĵ�ģ��id:Ŀ���ĵ�}
	 * @param dilerverableToInsert
	 *            ��Ҫ�������ݿ�Ľ������ϵ
	 * @param fileToCopy
	 *            ��Ҫ���Ƶ��ļ�
	 * @param context
	 *            ����������
	 */
	public static void copyWBSTemplate(ObjectId srcParent,
			ObjectId tgtParentId, ObjectId tgtRootId, Project project,
			Map<ObjectId, DBObject> roleMap,
			Map<ObjectId, DBObject> worksToInsert, ObjectId folderRootId,
			Map<ObjectId, DBObject> documentsToInsert,
			List<DBObject> dilerverableToInsert, List<DBObject[]> fileToCopy,
			IContext context) {
		ObjectId projectId = project.get_id();
		// ���src����
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(
				new BasicDBObject().append(WorkDefinition.F_PARENT_ID,
						srcParent)).sort(new SEQSorter().getBSON());
		int seq = 0;
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// ���workdef�Ƿ����ѡ���������
			String optionValue = checkOptionValueFromTemplate(workdef, project);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// ������ų��ģ�����Ҫ����
				continue;

			} else {
				DBObject work = getWorkFromWorkDefinition(tgtParentId,
						tgtRootId, project, roleMap, folderRootId,
						documentsToInsert, dilerverableToInsert, fileToCopy,
						context, projectId, seq, workdef, optionValue);

				worksToInsert.put((ObjectId) workdef.get(WorkDefinition.F__ID),
						work);

				seq++;

				// �����ӹ���
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
		// ��������
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

		// ���ù����������ֶ�
		Object value = workdef.get(WorkDefinition.F_DESC);
		if (value != null) {
			work.put(Work.F_DESC, value);
		}
		value = workdef.get(WorkDefinition.F_DESC_EN);
		if (value != null) {
			work.put(Work.F_DESC_EN, value);
		}
		// ������̱�����
		value = workdef.get(WorkDefinition.F_MILESTONE);
		if (value != null) {
			work.put(Work.F_MILESTONE, value);
		}

		// ���ñ��������
		value = workdef.get(IWorkCloneFields.F_WF_CHANGE);
		if (value != null) {
			work.put(IWorkCloneFields.F_WF_CHANGE, value);
		}

		// ���ñ���������Ƿ񼤻�
		value = workdef.get(IWorkCloneFields.F_WF_CHANGE_ACTIVATED);
		if (value != null) {
			work.put(IWorkCloneFields.F_WF_CHANGE_ACTIVATED, value);
		}

		// ���ñ�����̵Ļִ����
		setRoleDBObjectField(work, workdef,
				IWorkCloneFields.F_WF_CHANGE_ASSIGNMENT, roleMap);

		// ����ִ�й�����
		value = workdef.get(IWorkCloneFields.F_WF_EXECUTE);
		if (value != null) {
			work.put(IWorkCloneFields.F_WF_EXECUTE, value);
		}

		// ����ִ�й������Ƿ񼤻�
		value = workdef.get(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED);
		if (value != null) {
			work.put(IWorkCloneFields.F_WF_EXECUTE_ACTIVATED, value);
		}

		// ����ִ�й������Ļִ���˽�ɫ
		setRoleDBObjectField(work, workdef,
				IWorkCloneFields.F_WF_EXECUTE_ASSIGNMENT, roleMap);

		// ���ø����˽�ɫ
		setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID, roleMap);

		// ����ָ���˽�ɫ
		setRoleField(work, workdef,
				IWorkCloneFields.F_ASSIGNMENT_CHARGER_ROLE_ID, roleMap);

		// ���ò����߽�ɫ
		setRoleListField(work, workdef,
				IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

		// �������
		work.put(IWorkCloneFields.F_SEQ, new Integer(seq));

		// ���ñ�׼��ʱ
		value = workdef.get(IWorkCloneFields.F_STANDARD_WORKS);
		if (value != null) {
			work.put(IWorkCloneFields.F_STANDARD_WORKS, value);
		}

		// ��������ʱ��
		value = workdef.get(IWorkCloneFields.F_REMIND_BEFORE);
		if (value != null) {
			work.put(IWorkCloneFields.F_REMIND_BEFORE, value);
		}

		// ����������
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

		// ��ɹ������Ը���
		// ************************************************************

		// ************************************************************
		// ��ӹ���������
		// ���ģ�嶨��Ľ����ﶨ��
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
			// ��齻�����Ƿ����ѡ���������
			String documentOptionValue = checkOptionValueFromTemplate(delidata,
					project);
			if (DeliverableDefinition.VALUE_EXCLUDE.equals(documentOptionValue)) {
				// ������ų��ģ�����Ҫ����
				continue;
			} else {
				// ����ģ��Ľ����ﶨ�崴���������ϵ
				DBObject deliverableData = new BasicDBObject();

				// ������ĿId
				deliverableData.put(Deliverable.F_PROJECT_ID, projectId);

				// ���ù���Id
				deliverableData
						.put(Deliverable.F_WORK_ID, work.get(Work.F__ID));

				// ��������
				String deliverableType = (String) delidata
						.get(DeliverableDefinition.F_TYPE);
				deliverableData.put(Deliverable.F_TYPE, deliverableType);

				// �����Ƿ����
				if (DeliverableDefinition.VALUE_MANDATORY
						.equals(documentOptionValue)) {
					deliverableData.put(Deliverable.F_MANDATORY, Boolean.TRUE);
				}

				// ����ĵ�ģ��
				ObjectId documentTemplateId = (ObjectId) delidata
						.get(DeliverableDefinition.F_DOCUMENT_DEFINITION_ID);
				// �����ĵ�ģ���Ƿ��Ѿ��������ĵ�
				DBObject documentData = documentsToInsert
						.get(documentTemplateId);
				if (documentData == null) {
					// ���û�д�������Ҫ�������ĵ�
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
			// ����׼��
			List<String> optionValueSet = project.getStandardSetOptions();
			String set = checkOptionValueFromSet(filtersValue, optionValueSet,
					WorkDefinition.OPTIONSET_NAME_STANDARD);
			if (WorkDefinition.VALUE_EXCLUDE.equals(set)) {
				return WorkDefinition.VALUE_EXCLUDE;
			} else if (WorkDefinition.VALUE_MANDATORY.equals(set)) {
				setOption = WorkDefinition.VALUE_MANDATORY;
			}

			// ����Ʒѡ�
			optionValueSet = project.getProductTypeOptions();
			set = checkOptionValueFromSet(filtersValue, optionValueSet,
					WorkDefinition.OPTIONSET_NAME_PRODUCTTYPE);
			if (WorkDefinition.VALUE_EXCLUDE.equals(set)) {
				return WorkDefinition.VALUE_EXCLUDE;
			} else if (WorkDefinition.VALUE_MANDATORY.equals(set)) {
				setOption = WorkDefinition.VALUE_MANDATORY;
			}

			// �����Ŀѡ�
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
						"Ŀ���ɫ�����ڣ�������Ŀģ��Ĺ�����" + work.get(Work.F_DESC));
				ObjectId tgtRoleId = (ObjectId) tgtRole.get(ProjectRole.F__ID);
				actors.put(key, tgtRoleId);
				// ��Ǹý�ɫ��ʹ��
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
						"Ŀ���ɫ�����ڣ�������Ŀģ��Ĺ�����" + work.get(Work.F_DESC));
				if (tgtRole != null) {
					Object tgtRoleId = tgtRole.get(ProjectRole.F__ID);
					if (tgtRoleId != null) {
						participates.add(new BasicDBObject().append("_id",
								tgtRoleId));
						// ��Ǹý�ɫ��ʹ��
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
					"Ŀ���ɫ�����ڣ�������Ŀģ��Ĺ�����" + work.get(Work.F_DESC));
			if (tgtRole != null) {
				Object value = tgtRole.get(ProjectRole.F__ID);
				if (value != null) {
					work.put(roleFieldName, value);
					// ��Ǹý�ɫ��ʹ��
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

		// �����ĵ��ĸ��������ļ�
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

		// �����ĵ���Ĭ��ֵ
		document.initVerStatus();
		document.initVersionNumber();

		// �����ĵ���Ĭ��ֵ
		document.initInsertDefault(document.get_data(), context);

		// ����ĵ�����
		documentsToInsert.put(documentTemplateId, document.get_data());

		return document.get_data();
	}

	private static DBCollection getCollection(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

	/**
	 * ������Ŀ�������ù�����ӵ���Ŀ�ĸ������¡������ش�������Ŀ
	 * 
	 * @param work
	 *            :Ҫ��ӵĹ������ù����ѱ��־û���
	 * @param desc
	 *            :��Ŀ����
	 * @param description
	 *            :��Ŀ˵��
	 * @param launchorg_id
	 *            :������ID
	 * @param org_id
	 *            :��Ŀ������ID
	 * @param prj_manager
	 *            :��Ŀ����
	 * @param workOrder
	 *            :�������
	 * @param projecttemplate_id
	 *            :��Ŀģ��ID
	 * @param planfinish
	 *            :�ƻ����ʱ��
	 * @param planstart
	 *            :�ƻ���ʼʱ��
	 * @param projecttype
	 *            :��Ŀ����ѡ�
	 * @param standardset
	 *            :���ñ�׼��
	 * @param producttype
	 *            :��Ʒ����ѡ�
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

		// У���Ƿ�������ݴ���Ĳ���������Ŀ
		if (workOrder == null) {
			throw new Exception("δ¼�빤������޷��½���Ŀ");
		}
		if (prj_manager == null) {
			throw new Exception("δ¼����Ŀ�����޷��½���Ŀ");
		}
		if (desc == null) {
			throw new Exception("δ¼����Ŀ�����޷��½���Ŀ");
		}
		if (launchorg_id == null || launchorg_id.size() == 0) {
			throw new Exception("δ¼����Ŀ�������޷��½���Ŀ");
		}
		if (org_id == null) {
			throw new Exception("δ¼����Ŀ�������޷��½���Ŀ");
		}
		if (planfinish == null) {
			throw new Exception("δ¼����Ŀ�ƻ����ʱ���޷��½���Ŀ");
		}
		if (planstart == null) {
			throw new Exception("δ¼����Ŀ�ƻ���ʼʱ���޷��½���Ŀ");
		}

		// ���ݹ���������Ŀ
		BasicDBObject projectObject = new BasicDBObject();
		projectObject.put(Project.F_DESC, desc);
		projectObject.put(Project.F_DESCRIPTION, description);
		projectObject.put(Project.F_LAUNCH_ORGANIZATION, launchorg_id);
		projectObject.put(Project.F_FUNCTION_ORGANIZATION, org_id);
		projectObject.put(Project.F_CHARGER, prj_manager);

		/**
		 * BUG: ZHONGHUA ��������ֶ��� ��������
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

		// ��������ӵ���������Ŀ��
		ProjectToolkit.doProjectAddStandloneWork(work, project, context);

		return project;
	}

	/**
	 * ��������ӵ���Ŀ�ĸ�������
	 * 
	 * @param work
	 *            :Ҫ��ӵĹ������ù����ѱ��־û���
	 * @param project
	 *            :���繤������Ŀ������Ŀ�ѱ��־û���
	 * @param context
	 * @throws Exception
	 */
	public static void doProjectAddStandloneWork(Work work, Project project,
			IContext context) throws Exception {
		if (project == null) {
			throw new Exception("�����Ŀδ����");
		}

		ObjectId projectId = project.get_id();
		Work wbsRoot = project.getWBSRoot();

		// ��������ӵ���Ŀ��������
		work.setValue(Work.F_ROOT_ID, wbsRoot.get_id());
		work.setValue(Work.F_PARENT_ID, wbsRoot.get_id());
		work.setValue(Work.F_PROJECT_ID, projectId);
		int seq = wbsRoot.getMaxChildSeq();
		work.setValue(Work.F_SEQ, new Integer(seq + 1));
		work.doSave(context);

		// ��ӹ����Ľ�����
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

		// ��ӹ������ĵ�
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
