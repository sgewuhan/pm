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
		// ��������
		ensureIndex();
		// syncUser ͬ���û�
		// syncUser();

		// ��ʼ������
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
		setting.put("desc", "���칤��ˢ������(����)"); //$NON-NLS-1$ //$NON-NLS-2$
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

		// �˴���ӳ������ڴ���Ψһ����

		// �ĵ���Ų�����ͬ,��Ϊ�ĵ�����ʱУ�����Ƿ��ظ�
		// ensureUniqureIndex(db, IModelConstants.C_DOCUMENT, new
		// BasicDBObject()
		// .append(Document.F_DOCUMENT_NUMBER, 1));

		// ȫ�����ú��û�����ID���û�ID��Ҫ����Ψһ
		ensureUniqureIndex(db, IModelConstants.C_SETTING, new BasicDBObject()
				.append("varid", 1).append("userid", 1)); //$NON-NLS-1$ //$NON-NLS-2$

		// ͬһ��֯�½�ɫ��Ų����ظ�
		ensureUniqureIndex(db, IModelConstants.C_ROLE, new BasicDBObject()
				.append(Role.F_ORGANIZATION_ID, 1)
				.append(Role.F_ROLE_NUMBER, 1));

		// ͬһ��ɫָ���û�ID�����ظ�
		ensureUniqureIndex(
				db,
				IModelConstants.C_ROLE_ASSIGNMENT,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1).append(
						RoleAssignment.F_USER_ID, 1));

		// ͬһ���������岻�ܳ���������ͬ�Ľ����ﶨ��
		ensureUniqureIndex(
				db,
				IModelConstants.C_DELIEVERABLE_DEFINITION,
				new BasicDBObject().append(
						DeliverableDefinition.F_WORK_DEFINITION_ID, 1).append(
						DeliverableDefinition.F_DOCUMENT_DEFINITION_ID, 1));

		// ��Ŀ�飬ͬһ��ɫ�²��ܳ�����ͬ���û�ID
		ensureUniqureIndex(db, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT,
				new BasicDBObject().append(ProjectRoleAssignment.F_ROLE_ID, 1)
						.append(ProjectRoleAssignment.F_USER_ID, 1));

		// ͬһ��Ŀ��ɫ����������ͬ
		ensureUniqureIndex(
				db,
				IModelConstants.C_PROJECT_ROLE,
				new BasicDBObject().append(ProjectRole.F_PROJECT_ID, 1)
						.append(ProjectRole.F_ROLE_NUMBER, 1)
						.append(ProjectRole.F_ORGANIZATION_ROLE_ID, 1));

		// ͬһ��Ŀģ���ɫ����������ͬ
		ensureUniqureIndex(
				db,
				IModelConstants.C_ROLE_DEFINITION,
				new BasicDBObject()
						.append(RoleDefinition.F_PROJECT_TEMPLATE_ID, 1)
						.append(RoleDefinition.F_ROLE_NUMBER, 1)
						.append(RoleDefinition.F_ORGANIZATION_ROLE_ID, 1)
						.append(RoleDefinition.F_WORKDEFINITION_ID, 1)
						.append(RoleDefinition.F_WORK_ID, 1));

		// ��Ŀ��ͬһ���������ܳ���������ͬ�Ľ�����
		ensureUniqureIndex(
				db,
				IModelConstants.C_DELIEVERABLE,
				new BasicDBObject().append(Deliverable.F_WORK_ID, 1).append(
						Deliverable.F_DOCUMENT_ID, 1));

		// ��Ŀģ����ǰ���ù�ϵǰ�ù�������ͺ��ù�������id��������ͬ
		ensureUniqureIndex(
				db,
				IModelConstants.C_WORK_DEFINITION_CONNECTION,
				new BasicDBObject().append(WorkDefinitionConnection.F_END1_ID,
						1).append(WorkDefinitionConnection.F_END2_ID, 1));

		// ��Ŀ��ǰ���ù�ϵǰ�ù����ͺ��ù���id��������ͬ
		ensureUniqureIndex(
				db,
				IModelConstants.C_WORK_CONNECTION,
				new BasicDBObject().append(WorkConnection.F_END1_ID, 1).append(
						WorkConnection.F_END2_ID, 1));

		// �������û���½����ͬ
		ensureUniqureIndex(db, IModelConstants.C_USER,
				new BasicDBObject().append(User.F_USER_ID, 1));

		// ��Ŀģ���в���������ظ���Ԥ����Ϣ
		ensureUniqureIndex(db, IModelConstants.C_BUDGET_ITEM,
				new BasicDBObject().append(BudgetItem.F_PROJECTTEMPLATE_ID, 1));

		// ��Ŀ�в���������ظ���Ԥ����Ϣ
		ensureUniqureIndex(db, IModelConstants.C_PROJECT_BUDGET,
				new BasicDBObject().append(ProjectBudget.F_PROJECT_ID, 1));

		// �ļ����²���������������ļ���
		ensureUniqureIndex(db, IModelConstants.C_FOLDER, new BasicDBObject()
				.append(Folder.F_PARENT_ID, 1).append(Folder.F_ROOT_ID, 1)
				.append(Folder.F_DESC, 1));

		// ͬһ��֯�в����������������Ŀģ��
		ensureUniqureIndex(
				db,
				IModelConstants.C_PROJECT_TEMPLATE,
				new BasicDBObject()
						.append(ProjectTemplate.F_ORGANIZATION_ID, 1).append(
								ProjectTemplate.F_DESC, 1));

		// ��֯����Ψһ
		// ensureUniqureIndex(db, IModelConstants.C_ORGANIZATION,
		// new BasicDBObject().append(Organization.F_CODE, 1));

		// �ɱ����Ĵ���Ψһ
		// ensureUniqureIndex(db, IModelConstants.C_ORGANIZATION,
		// new BasicDBObject().append(Organization.F_COST_CENTER_CODE, 1));

		// ���ù�˾�͹�����Ŷ�Ӧ���Ψһ����
		ensureUniqureIndex(
				db,
				IModelConstants.C_COMPANY_WORKORDER,
				new BasicDBObject().append(CompanyWorkOrder.F_ORGANIZATION_ID,
						1).append(CompanyWorkOrder.F_WORKORDER, 1));

		// ȷ���ɱ����ĵ�ÿ������Ψһ
		ensureUniqureIndex(
				db,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER,
				new BasicDBObject().append(RNDPeriodCost.F_COSTCENTERCODE, 1)
						.append(RNDPeriodCost.F_MONTH, 1)
						.append(RNDPeriodCost.F_YEAR, 1));

		// ���������鵵�ֶ�����
		ensureIndex(db, IModelConstants.C_WORK,
				new BasicDBObject().append(Work.F_ARCHIVE, 1));

		// ������Ŀ�������ֶ�����
		ensureIndex(db, IModelConstants.C_PROJECT,
				new BasicDBObject().append(Project.F_CHARGER, 1));

		// TODO ��������

		/**
		 * ��ע�⣡����
		 * 
		 * ��������ʱ�������ظ������Ѿ�����ʱ����ĳЩ���ݿ������£���Ҫ�׳����� ������ݿ��Ѿ���������������ݣ�Ҳ���׳�����
		 * 
		 * �����������Щ���󽫵��±�bundle�޷�����������������������������
		 * 
		 * Ҫ��ʹ�� ensureUniqureIndex ensureIndex ���������������������Ա��������
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
