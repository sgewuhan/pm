package com.sg.business.pm2.setup;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.Deliverable;
import com.sg.business.model.DeliverableDefinition;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.ProjectRoleAssignment;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RNDPeriodCost;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.WorkConnection;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.business.pm2.nls.Messages;

public class DBInit implements ISchedualJobRunnable {

	public DBInit() {
	}

	@Override
	public boolean run() throws Exception {
		// 创建索引
		ensureIndex();
		// syncUser 同步用户
		// syncUser();

		// 初始化设置
		// initSetting();

		return true;

	}

	private void initSetting() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SETTING);

		BasicDBObject setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_PROCESS_BASE_URL); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_1); //$NON-NLS-1$
		setting.put("value", //$NON-NLS-1$
				"http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/Guvnor.jsp?"); //$NON-NLS-1$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_DEFAULT_PROJECT_COMMIT_DURATION); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_2); //$NON-NLS-1$
		setting.put("value", "5"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_S_TASK_DELAY); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_3); //$NON-NLS-1$
		setting.put("value", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_S_WORK_RESERVED_REFRESH_INTERVAL); //$NON-NLS-1$
		setting.put("desc", "待办工作刷新周期(分钟)"); //$NON-NLS-1$ //$NON-NLS-2$
		setting.put("value", "5"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", //$NON-NLS-1$
				IModelConstants.S_U_MESSAGE_RESERVED_REFRESH_INTERVAL);
		setting.put("desc", Messages.get().DBInit_4); //$NON-NLS-1$
		setting.put("value", "30"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_MAXCONN); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_5); //$NON-NLS-1$
		setting.put("value", "20"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_CLIENT); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_6); //$NON-NLS-1$
		setting.put("value", "700"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_USERID); //$NON-NLS-1$
		setting.put("desc", "SAP User Id"); //$NON-NLS-1$ //$NON-NLS-2$
		setting.put("value", "ITFSAP"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_PASSWORD); //$NON-NLS-1$
		setting.put("desc", "SAP User password"); //$NON-NLS-1$ //$NON-NLS-2$
		setting.put("value", "12392008"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_LANGUAGE); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_7); //$NON-NLS-1$
		setting.put("value", "ZH"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_HOST); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_8); //$NON-NLS-1$
		setting.put("value", "172.16.9.74");// 90 //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_INSTANCENUMBER); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_9); //$NON-NLS-1$
		setting.put("value", "00");// 01 //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_MAJOR_VID_SEQ); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_10); //$NON-NLS-1$
		setting.put("value", //$NON-NLS-1$
				"A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"); //$NON-NLS-1$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_S_BI_OVER_COST_ESTIMATE); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_11); //$NON-NLS-1$
		setting.put("value", "0.3"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_U_TASK_RESERVED_REFRESH_INTERVAL); //$NON-NLS-1$
		setting.put("desc", Messages.get().DBInit_0); //$NON-NLS-1$
		setting.put("value", "30"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

	}

	private void syncUser() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		DBCursor cur = col.find(null,
				new BasicDBObject().append(User.F_USER_ID, 1));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			String userid = (String) dbo.get(User.F_USER_ID);
			HTService ts = BPM.getHumanTaskService();
			ts.addParticipateUser(userid);
		}
	}

	private void ensureIndex() {
		DB db = DBActivator.getDB(IModelConstants.DB);

		// 此处添加程序用于创建唯一索引

		// 文档编号不能相同,改为文档保存时校验编号是否重复
		// ensureUniqureIndex(db, IModelConstants.C_DOCUMENT, new
		// BasicDBObject()
		// .append(Document.F_DOCUMENT_NUMBER, 1));

		// 全局设置和用户设置ID和用户ID需要保持唯一
		ensureUniqureIndex(db, IModelConstants.C_SETTING, new BasicDBObject()
				.append("varid", 1).append("userid", 1)); //$NON-NLS-1$ //$NON-NLS-2$

		// 同一组织下角色编号不可重复
		ensureUniqureIndex(db, IModelConstants.C_ROLE, new BasicDBObject()
				.append(Role.F_ORGANIZATION_ID, 1)
				.append(Role.F_ROLE_NUMBER, 1));

		// 同一角色指派用户ID不能重复
		ensureUniqureIndex(
				db,
				IModelConstants.C_ROLE_ASSIGNMENT,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1).append(
						RoleAssignment.F_USER_ID, 1));

		// 同一个工作定义不能出现两个相同的交付物定义
		ensureUniqureIndex(
				db,
				IModelConstants.C_DELIEVERABLE_DEFINITION,
				new BasicDBObject().append(
						DeliverableDefinition.F_WORK_DEFINITION_ID, 1).append(
						DeliverableDefinition.F_DOCUMENT_DEFINITION_ID, 1));

		// 项目组，同一角色下不能出现相同的用户ID
		ensureUniqureIndex(db, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT,
				new BasicDBObject().append(ProjectRoleAssignment.F_ROLE_ID, 1)
						.append(ProjectRoleAssignment.F_USER_ID, 1));

		// 同一项目角色不允许编号相同
		ensureUniqureIndex(
				db,
				IModelConstants.C_PROJECT_ROLE,
				new BasicDBObject().append(ProjectRole.F_PROJECT_ID, 1)
						.append(ProjectRole.F_ROLE_NUMBER, 1)
						.append(ProjectRole.F_ORGANIZATION_ROLE_ID, 1));

		// 同一项目模板角色不允许编号相同
		ensureUniqureIndex(
				db,
				IModelConstants.C_ROLE_DEFINITION,
				new BasicDBObject()
						.append(RoleDefinition.F_PROJECT_TEMPLATE_ID, 1)
						.append(RoleDefinition.F_ROLE_NUMBER, 1)
						.append(RoleDefinition.F_ORGANIZATION_ROLE_ID, 1)
						.append(RoleDefinition.F_WORKDEFINITION_ID, 1)
						.append(RoleDefinition.F_WORK_ID, 1));

		// 项目中同一个工作不能出现两个相同的交付物
		ensureUniqureIndex(
				db,
				IModelConstants.C_DELIEVERABLE,
				new BasicDBObject().append(Deliverable.F_WORK_ID, 1).append(
						Deliverable.F_DOCUMENT_ID, 1));

		// 项目模板中前后置关系前置工作定义和后置工作定义id不允许相同
		ensureUniqureIndex(
				db,
				IModelConstants.C_WORK_DEFINITION_CONNECTION,
				new BasicDBObject().append(WorkDefinitionConnection.F_END1_ID,
						1).append(WorkDefinitionConnection.F_END2_ID, 1));

		// 项目中前后置关系前置工作和后置工作id不允许相同
		ensureUniqureIndex(
				db,
				IModelConstants.C_WORK_CONNECTION,
				new BasicDBObject().append(WorkConnection.F_END1_ID, 1).append(
						WorkConnection.F_END2_ID, 1));

		// 不允许用户登陆名相同
		ensureUniqureIndex(db, IModelConstants.C_USER,
				new BasicDBObject().append(User.F_USER_ID, 1));

		// 项目模板中不允许出现重复的预算信息
		ensureUniqureIndex(db, IModelConstants.C_BUDGET_ITEM,
				new BasicDBObject().append(BudgetItem.F_PROJECTTEMPLATE_ID, 1));

		// 项目中不允许出现重复的预算信息
		ensureUniqureIndex(db, IModelConstants.C_PROJECT_BUDGET,
				new BasicDBObject().append(ProjectBudget.F_PROJECT_ID, 1));

		// 文件夹下不允许出现重名的文件夹
		ensureUniqureIndex(db, IModelConstants.C_FOLDER, new BasicDBObject()
				.append(Folder.F_PARENT_ID, 1).append(Folder.F_ROOT_ID, 1)
				.append(Folder.F_DESC, 1));

		// 同一组织中不允许出现重名的项目模板
		ensureUniqureIndex(
				db,
				IModelConstants.C_PROJECT_TEMPLATE,
				new BasicDBObject()
						.append(ProjectTemplate.F_ORGANIZATION_ID, 1).append(
								ProjectTemplate.F_DESC, 1));

		// 组织代码唯一
		// ensureUniqureIndex(db, IModelConstants.C_ORGANIZATION,
		// new BasicDBObject().append(Organization.F_CODE, 1));

		// 成本中心代码唯一
		// ensureUniqureIndex(db, IModelConstants.C_ORGANIZATION,
		// new BasicDBObject().append(Organization.F_COST_CENTER_CODE, 1));

		// 设置公司和工作令号对应表的唯一索引
		ensureUniqureIndex(
				db,
				IModelConstants.C_COMPANY_WORKORDER,
				new BasicDBObject().append(CompanyWorkOrder.F_ORGANIZATION_ID,
						1).append(CompanyWorkOrder.F_WORKORDER, 1));

		// 确保成本中心的每期数据唯一
		ensureUniqureIndex(
				db,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER,
				new BasicDBObject().append(RNDPeriodCost.F_COSTCENTERCODE, 1)
						.append(RNDPeriodCost.F_MONTH, 1)
						.append(RNDPeriodCost.F_YEAR, 1));

		// 创建工作归档字段索引
		ensureIndex(db, IModelConstants.C_WORK,
				new BasicDBObject().append(Work.F_ARCHIVE, 1));

		// 创建项目负责人字段索引
		ensureIndex(db, IModelConstants.C_PROJECT,
				new BasicDBObject().append(Project.F_CHARGER, 1));

		// TODO 增加索引

		/**
		 * 请注意！！！
		 * 
		 * 创建索引时当索引重复或者已经创建时，在某些数据库设置下，将要抛出错误 如果数据库已经保存有问题的数据，也将抛出错误。
		 * 
		 * 如果不忽略这些错误将导致本bundle无法正常启动，并且索引创建不完整
		 * 
		 * 要求使用 ensureUniqureIndex ensureIndex 以上两个方法创建索引以避免该问题
		 * 
		 */
	}

	private void ensureIndex(DB db, String colname, BasicDBObject index) {
		try {
			DBCollection col = db.getCollection(colname);
			col.createIndex(index);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private void ensureUniqureIndex(DB db, String colname, BasicDBObject index) {
		try {
			DBCollection col = db.getCollection(colname);
			col.ensureIndex(index, "unique", true); //$NON-NLS-1$
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}
