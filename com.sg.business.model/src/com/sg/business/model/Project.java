package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.resource.BusinessResource;

public class Project extends PrimaryObject {

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

	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	/**
	 * 根工作定义
	 */
	public static final String F_WORK_ID = "work_id";

	public static final String F_BUDGET_ID = "budget_id";

	public static final String F_STANDARD_SET_OPTION = "standardset";

	public static final String F_PRODUCT_TYPE_OPTION = "producttype";

	public static final String F_PROJECT_TYPE_OPTION = "projecttype";

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
		setValue(F__ID, new ObjectId());// 需要预设ID,否则后面的get_id()取出的是空

		// 复制模板
		doSetupWithTemplate(context);
		super.doInsert(context);
	}

	/**
	 * 
	 * @param context
	 */
	public void doSetupWithTemplate(IContext context) {
		ObjectId id = getProjectTemplateId();
		if (id == null) {
			return;
		}

		// 复制角色定义
		Map<ObjectId, DBObject> roleMap = doSetupRolesWithTemplate(id, context);

		// 复制工作定义
		try {
			doSetupWBSWithTemplate(id, roleMap, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 过滤WBS,复制WBS
	}

	private void doSetupWBSWithTemplate(ObjectId projectTemplateId,
			Map<ObjectId, DBObject> roleMap, IContext context) throws Exception {
		// 创建根工作定义
		BasicDBObject wbsRootData = new BasicDBObject();
		wbsRootData.put(Work.F_DESC, getDesc());
		wbsRootData.put(Work.F_PROJECT_ID, get_id());
		ObjectId wbsRootId = new ObjectId();
		wbsRootData.put(Work.F__ID, wbsRootId);
		wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
		wbsRootData.put(Work.F__CACCOUNT, context.getAccountInfo().getUserId());
		wbsRootData.put(Work.F__CDATE, new Date());
		setValue(ProjectTemplate.F_WORK_DEFINITON_ID, wbsRootId);

		// 取出模板的根工作定义的_id
		DBCollection pjTempCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_TEMPLATE);
		DBObject pjTemp = pjTempCol.findOne(new BasicDBObject().append(
				ProjectTemplate.F__ID, projectTemplateId), new BasicDBObject()
				.append(ProjectTemplate.F_WORK_DEFINITON_ID, 1));
		ObjectId rootWorkDefId = (ObjectId) pjTemp
				.get(ProjectTemplate.F_WORK_DEFINITON_ID);

		ArrayList<DBObject> worksToBeInsert = new ArrayList<DBObject>();
		worksToBeInsert.add(wbsRootData);
		ArrayList<DBObject> documentsToBeInsert = new ArrayList<DBObject>();

		doCopyWBSTemplate(rootWorkDefId, wbsRootId, wbsRootId,get_id(),roleMap, worksToBeInsert,
				documentsToBeInsert, context);
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

	private void doCopyWBSTemplate(ObjectId srcParent, ObjectId tgtParentId,ObjectId tgtRootId,ObjectId projectId,
			Map<ObjectId, DBObject> roleMap,
			ArrayList<DBObject> worksToBeInsert,
			ArrayList<DBObject> documentsToBeInsert, IContext context) {
		// 获得src的子
		DBCollection workDefCol = getCollection(IModelConstants.C_WORK_DEFINITION);
		DBCursor cur = workDefCol.find(new BasicDBObject().append(
				WorkDefinition.F_PARENT_ID, srcParent));
		while (cur.hasNext()) {
			DBObject workdef = cur.next();

			// 检查workdef是否符合选项过滤条件
			String optionValue = checkOptionValueFromTemplate(workdef);

			if (WorkDefinition.VALUE_EXCLUDE.equals(optionValue)) {
				// 如果是排出的，就需要跳过
				continue;

			} else {
				// 创建工作
				BasicDBObject work = new BasicDBObject();

				work.put(Work.F_PROJECT_ID, projectId);
				
				work.put(Work.F_ROOT_ID, tgtRootId);
				
				work.put(Work.F_PARENT_ID, tgtParentId);
				
				work.put(Work.F__ID, new ObjectId());

				if (WorkDefinition.VALUE_MONDARY.equals(optionValue)) {
					work.put(Work.F_MONDARY, Boolean.TRUE);
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

				//设置执行工作流的活动执行人角色
				setRoleDBObjectField(work, workdef,
						IWorkCloneFields.F_WF_EXECUTE_ASSIGNMENT, roleMap);
				
				// 设置负责人角色
				setRoleField(work, workdef, IWorkCloneFields.F_CHARGER_ROLE_ID,
						roleMap);

				// 设置参与者角色
				setRoleListField(work, workdef,
						IWorkCloneFields.F_PARTICIPATE_ROLE_SET, roleMap);

				// 设置序号
				value = workdef.get(IWorkCloneFields.F_SEQ);
				if (value != null) {
					work.put(IWorkCloneFields.F_SEQ, value);
				}

				// 设置标准工时
				value = workdef.get(IWorkCloneFields.F_STANDARD_WORKS);
				if (value != null) {
					work.put(IWorkCloneFields.F_SEQ, value);
				}

				//复制设置项
				for (int i = 0; i < IWorkCloneFields.SETTING_FIELDS.length; i++) {
					value = workdef.get(IWorkCloneFields.SETTING_FIELDS[i]);
					if (value != null) {
						work.put(IWorkCloneFields.SETTING_FIELDS[i], value);
					}
				}
				
				work.put(Work.F__CACCOUNT, context.getAccountInfo().getUserId());
				
				work.put(Work.F__CDATE, new Date());
				
				//******************************复制工作属性完成
				worksToBeInsert.add(work);
			}

		}

	}

	private void setRoleDBObjectField(BasicDBObject work, DBObject workdef,
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

	private void setRoleListField(BasicDBObject work, DBObject workdef,
			String fieldName, Map<ObjectId, DBObject> roleMap) {
		Object value = workdef.get(fieldName);
		if (value instanceof BasicDBList) {
			BasicDBList participates = new BasicDBList();
			BasicDBList valueList = (BasicDBList) value;
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

	private void setRoleField(BasicDBObject work, DBObject workdef,
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

	private String checkOptionValueFromTemplate(DBObject workdef) {
		Object filters = workdef.get(WorkDefinition.F_OPTION_FILTERS);
		if (filters instanceof BasicDBList) {

			BasicDBList filtersValue = (BasicDBList) filters;
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
								WorkDefinition.VALUE_MONDARY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MONDARY;
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
								WorkDefinition.VALUE_MONDARY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MONDARY;
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
								WorkDefinition.VALUE_MONDARY);
						if (filtersValue.contains(item)) {
							return WorkDefinition.VALUE_MONDARY;
						}
					}
				}
			}
		}
		return WorkDefinition.VALUE_OPTION;
	}

	private Map<ObjectId, DBObject> doSetupRolesWithTemplate(
			ObjectId projectTemplateId, IContext context) {
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
			col_role.insert(insertData, new WriteConcern());
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

}
