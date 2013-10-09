package com.sg.business.pm2;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.mobnut.db.DBActivator;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.Deliverable;
import com.sg.business.model.DeliverableDefinition;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.ProjectRoleAssignment;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.User;
import com.sg.business.model.WorkConnection;
import com.sg.business.model.WorkDefinitionConnection;

/**
 * The activator class controls the plug-in life cycle
 */
public class PM2Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.pm2"; //$NON-NLS-1$

	// The shared instance
	private static PM2Activator plugin;

	/**
	 * The constructor
	 */
	public PM2Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		context.addBundleListener(new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				if (Portal.PLUGIN_ID
						.equals(event.getBundle().getSymbolicName())) {
					// ��ʼ��
					initialPMSystem();
				}
			}
		});
	}

	protected void initialPMSystem() {
		ensureIndex();
	}

	private void ensureIndex() {
		DB db = DBActivator.getDB(IModelConstants.DB);
		// �˴���ӳ������ڴ���Ψһ����

		// ȫ�����ú��û�����ID���û�ID��Ҫ����Ψһ
		ensureIndex(db, IModelConstants.C_SETTING,
				new BasicDBObject().append("varid", 1).append("userid", 1));

		// ͬһ��֯�½�ɫ��Ų����ظ�
		ensureIndex(
				db,
				IModelConstants.C_ROLE,
				new BasicDBObject().append(Role.F_ORGANIZATION_ID, 1).append(
						Role.F_ROLE_NUMBER, 1));

		// ͬһ��ɫָ���û�ID�����ظ�
		ensureIndex(
				db,
				IModelConstants.C_ROLE_ASSIGNMENT,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1).append(
						RoleAssignment.F_USER_ID, 1));

		// ͬһ���������岻�ܳ���������ͬ�Ľ����ﶨ��
		ensureIndex(
				db,
				IModelConstants.C_DELIEVERABLE_DEFINITION,
				new BasicDBObject().append(
						DeliverableDefinition.F_WORK_DEFINITION_ID, 1).append(
						DeliverableDefinition.F_DOCUMENT_DEFINITION_ID, 1));

		// ��Ŀ�飬ͬһ��ɫ�²��ܳ�����ͬ���û�ID
		ensureIndex(db, IModelConstants.C_PROJECT_ROLE_ASSIGNMENT,
				new BasicDBObject().append(ProjectRoleAssignment.F_ROLE_ID, 1)
						.append(ProjectRoleAssignment.F_USER_ID, 1));

		// ͬһ��Ŀ��ɫ����������ͬ
		ensureIndex(
				db,
				IModelConstants.C_PROJECT_ROLE,
				new BasicDBObject().append(ProjectRole.F_PROJECT_ID, 1)
						.append(ProjectRole.F_ROLE_NUMBER, 1)
						.append(ProjectRole.F_ORGANIZATION_ROLE_ID, 1));

		// ͬһ��Ŀģ���ɫ����������ͬ
		ensureIndex(
				db,
				IModelConstants.C_ROLE_DEFINITION,
				new BasicDBObject()
						.append(RoleDefinition.F_PROJECT_TEMPLATE_ID, 1)
						.append(RoleDefinition.F_ROLE_NUMBER, 1)
						.append(RoleDefinition.F_ORGANIZATION_ROLE_ID, 1)
						.append(RoleDefinition.F_WORKDEFINITION_ID, 1)
						.append(RoleDefinition.F_WORK_ID, 1));

		// ��Ŀ��ͬһ���������ܳ���������ͬ�Ľ�����
		ensureIndex(
				db,
				IModelConstants.C_DELIEVERABLE,
				new BasicDBObject().append(Deliverable.F_WORK_ID, 1).append(
						Deliverable.F_DOCUMENT_ID, 1));

		// ��Ŀģ����ǰ���ù�ϵǰ�ù�������ͺ��ù�������id��������ͬ
		ensureIndex(
				db,
				IModelConstants.C_WORK_DEFINITION_CONNECTION,
				new BasicDBObject().append(WorkDefinitionConnection.F_END1_ID,
						1).append(WorkDefinitionConnection.F_END2_ID, 1));

		// ��Ŀ��ǰ���ù�ϵǰ�ù����ͺ��ù���id��������ͬ
		ensureIndex(
				db,
				IModelConstants.C_WORK_CONNECTION,
				new BasicDBObject().append(WorkConnection.F_END1_ID, 1).append(
						WorkConnection.F_END2_ID, 1));

		// �������û���½����ͬ
		ensureIndex(db, IModelConstants.C_USER,
				new BasicDBObject().append(User.F_USER_ID, 1));

		// ��Ŀģ���в���������ظ���Ԥ����Ϣ
		ensureIndex(db, IModelConstants.C_BUDGET_ITEM,
				new BasicDBObject().append(BudgetItem.F_PROJECTTEMPLATE_ID, 1));

		// ��Ŀ�в���������ظ���Ԥ����Ϣ
		ensureIndex(db, IModelConstants.C_PROJECT_BUDGET,
				new BasicDBObject().append(ProjectBudget.F_PROJECT_ID, 1));

		// �ļ����²���������������ļ���
		ensureIndex(
				db,
				IModelConstants.C_FOLDER,
				new BasicDBObject().append(Folder.F_PARENT_ID, 1)
						.append(Folder.F_ROOT_ID, 1).append(Folder.F_DESC, 1));

		// ͬһ��֯�в����������������Ŀģ��
		ensureIndex(
				db,
				IModelConstants.C_PROJECT_TEMPLATE,
				new BasicDBObject()
						.append(ProjectTemplate.F_ORGANIZATION_ID, 1).append(
								ProjectTemplate.F_DESC, 1));

		// // ͬһ��֯�в�������������Ķ���/ͨ�ù�������
		// ensureIndex(
		// db,
		// IModelConstants.C_WORK_DEFINITION,
		// new BasicDBObject().append(WorkDefinition.F_ORGANIZATION_ID, 1)
		// .append(WorkDefinition.F_DESC, 1)
		// .append(WorkDefinition.F_WORK_TYPE, 1));

		// ͬһ��Ŀ�в�������ֶ��������
		// ensureIndex(
		// db,
		// IModelConstants.C_WORK,
		// new BasicDBObject().append(Work.F_PROJECT_ID, 1)
		// .append(Work.F_ROOT_ID, 1).append(Work.F_PARENT_ID, 1)
		// .append(Work.F_WORK_TYPE, 1));

		// ͬһ��Ŀģ���в�������ֶ������������
		// ensureIndex(
		// db,
		// IModelConstants.C_WORK_DEFINITION,
		// new BasicDBObject()
		// .append(WorkDefinition.F_PROJECT_TEMPLATE_ID, 1)
		// .append(WorkDefinition.F_ROOT_ID, 1)
		// .append(WorkDefinition.F_PARENT_ID, 1)
		// .append(WorkDefinition.F_WORK_TYPE, 1));
	}

	private void ensureIndex(DB db, String colname, BasicDBObject index) {
		try {
			DBCollection col = db.getCollection(colname);
			col.ensureIndex(index, "unique", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static PM2Activator getDefault() {
		return plugin;
	}

}
