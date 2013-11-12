package com.sg.business.pm2.setup;

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
import com.sg.business.model.Organization;
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

public class DBInit implements Runnable {

	public DBInit() {
	}

	@Override
	public void run() {
		// 创建索引
		ensureIndex();
		// syncUser 同步用户
		syncUser();

		// 初始化设置
		initSetting();

	}

	private void initSetting() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SETTING);

		
		BasicDBObject setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_PROCESS_BASE_URL);
		setting.put("desc", "项目流程库地址");
		setting.put("value",
				"http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/Guvnor.jsp?");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_DEFAULT_PROJECT_COMMIT_DURATION);
		setting.put("desc", "项目提交工作默认天数");
		setting.put("value", "5");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_S_TASK_DELAY);
		setting.put("desc", "流程任务延迟标记时间（分钟）");
		setting.put("value", "1");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_S_WORK_RESERVED_REFRESH_INTERVAL);
		setting.put("desc", "待办工作刷新周期(分钟)");
		setting.put("value", "5");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_U_MESSAGE_RESERVED_REFRESH_INTERVAL);
		setting.put("desc", "消息刷新周期(分钟)");
		setting.put("value", "30");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_MAXCONN);
		setting.put("desc", "SAP最大连接数");
		setting.put("value", "20");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_CLIENT);
		setting.put("desc", "SAP 客户端");
		setting.put("value", "700");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_USERID);
		setting.put("desc", "SAP User Id");
		setting.put("value", "ITFSAP");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_PASSWORD);
		setting.put("desc", "SAP User password");
		setting.put("value", "12392008");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_LANGUAGE);
		setting.put("desc", "SAP 语言");
		setting.put("value", "ZH");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_HOST);
		setting.put("desc", "SAP 主机");
		setting.put("value", "172.16.9.74");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}
		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_EAI_SAP_INSTANCENUMBER);
		setting.put("desc", "SAP 实例编号");
		setting.put("value", "00");
		try {
			col.insert(setting);
		} catch (Exception e) {
		}

		setting = new BasicDBObject();
		setting.put("varid", IModelConstants.S_MAJOR_VID_SEQ);
		setting.put("desc", "主版本号序列");
		setting.put("value", "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
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

		// 全局设置和用户设置ID和用户ID需要保持唯一
		ensureUniqureIndex(db, IModelConstants.C_SETTING, new BasicDBObject()
				.append("varid", 1).append("userid", 1));

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
		ensureUniqureIndex(db, IModelConstants.C_ORGANIZATION,
				new BasicDBObject().append(Organization.F_COST_CENTER_CODE, 1));

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
			col.ensureIndex(index, "unique", true);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}
