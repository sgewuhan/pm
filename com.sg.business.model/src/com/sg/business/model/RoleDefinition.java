package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.nls.Messages;

/**
 * ��ɫ����
 * <p/>
 * ����Ŀģ�����Ŀ�ж���Ľ�ɫ
 * 
 * @author jinxitao
 * 
 */
public class RoleDefinition extends AbstractRoleDefinition implements
		IProjectTemplateRelative {

	/**
	 * ������ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition"; //$NON-NLS-1$

	public static final String F_WORK_ID = "work_id"; //$NON-NLS-1$
	
	/**
	 * ������������_id
	 */
	public static final String F_WORKDEFINITION_ID="workdefinition_id"; //$NON-NLS-1$

	/**
	 * ɾ����ɫ���
	 */
	public List<Object[]> checkRemoveAction(IContext context) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.WBS���õĽ�ɫ
		BasicDBList values = new BasicDBList();
		values.add(new BasicDBObject().append(
				Work.F_ASSIGNMENT_CHARGER_ROLE_ID, getOrganizationRoleId()));
		values.add(new BasicDBObject().append(Work.F_CHARGER_ROLE_ID,
				getOrganizationRoleId()));
		values.add(new BasicDBObject().append(Work.F_PARTICIPATE_ROLE_SET,
				Pattern.compile("^.*" + getOrganizationRoleId() + ".*$", //$NON-NLS-1$ //$NON-NLS-2$
						Pattern.CASE_INSENSITIVE)));
		long countWork = getRelationCountByCondition(
				Work.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						Work.F_PARENT_ID, getProjectTemplateId()));
		if (countWork > 0) {
			message.add(new Object[] { Messages.get().RoleDefinition_0, this, SWT.ICON_WARNING });
		}

		ProjectTemplate projectTemplate = getProjectTemplate();
		IProcessControl pc = (IProcessControl) projectTemplate
				.getAdapter(IProcessControl.class);
		// 2.�ύ����������
		if (pc.isWorkflowActivate(ProjectTemplate.F_WF_COMMIT)) {
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
			DroolsProcessDefinition pd = pc
					.getProcessDefinition(ProjectTemplate.F_WF_COMMIT);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// ����ɫ
				AbstractRoleDefinition role = pc.getProcessActionAssignment(
						ProjectTemplate.F_WF_COMMIT, nap);
				if (getOrganizationRoleId()
						.equals(role.getOrganizationRoleId())) {
					message.add(new Object[] { Messages.get().RoleDefinition_1, this,
							SWT.ICON_WARNING });
					break;
				}

			}
		}

		// 3.�������������

		if (pc.isWorkflowActivate(ProjectTemplate.F_WF_CHANGE)) {
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
			DroolsProcessDefinition pd = pc
					.getProcessDefinition(ProjectTemplate.F_WF_CHANGE);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// ����ɫ
				AbstractRoleDefinition role = pc.getProcessActionAssignment(
						ProjectTemplate.F_WF_CHANGE, nap);
				if (getOrganizationRoleId()
						.equals(role.getOrganizationRoleId())) {
					message.add(new Object[] { "����Ŀģ��ı�������������˸ý�ɫ", this, //$NON-NLS-1$
							SWT.ICON_WARNING });
					break;
				}

			}
		}
		// 4.��������������
		WorkDefinition root = projectTemplate.getWBSRoot();
		message.addAll(checkCascadeRemove(root));
		return message;
	}

	private List<Object[]> checkCascadeRemove(WorkDefinition workDefinition) {
		List<Object[]> message = new ArrayList<Object[]>();
		List<PrimaryObject> childrenWork = workDefinition.getChildrenWork();
		if (childrenWork.size() > 0) {// ������¼��������¼��ļ����
			for (int i = 0; i < childrenWork.size(); i++) {
				WorkDefinition childWork = (WorkDefinition) childrenWork.get(i);
				message.addAll(checkCascadeRemove(childWork));
			}
		} else {
			// 1.ִ������������
			if (workDefinition.isWorkflowActivate(WorkDefinition.F_WF_EXECUTE)) {
				// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
				DroolsProcessDefinition pd = workDefinition
						.getProcessDefinition(WorkDefinition.F_WF_EXECUTE);
				List<NodeAssignment> nalist = pd.getNodesAssignment();
				for (int i = 0; i < nalist.size(); i++) {
					NodeAssignment na = nalist.get(i);
					if (!na.isNeedAssignment()) {
						continue;
					}
					String nap = na.getNodeActorParameter();
					// ����ɫ
					ProjectRole role = workDefinition
							.getProcessActionAssignment(
									WorkDefinition.F_WF_EXECUTE, nap);
					if (getOrganizationRoleId().equals(
							role.getOrganizationRoleId())) {
						message.add(new Object[] { "�ڹ�����ִ�������������˸ý�ɫ", //$NON-NLS-1$
								workDefinition, SWT.ICON_WARNING });
						break;
					}

				}
			}

			// 2.�������������
			if (workDefinition.isWorkflowActivate(WorkDefinition.F_WF_CHANGE)) {
				// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
				DroolsProcessDefinition pd = workDefinition
						.getProcessDefinition(WorkDefinition.F_WF_CHANGE);
				List<NodeAssignment> nalist = pd.getNodesAssignment();
				for (int i = 0; i < nalist.size(); i++) {
					NodeAssignment na = nalist.get(i);
					if (!na.isNeedAssignment()) {
						continue;
					}
					String nap = na.getNodeActorParameter();
					// ����ɫ
					ProjectRole role = workDefinition
							.getProcessActionAssignment(
									WorkDefinition.F_WF_CHANGE, nap);
					if (getOrganizationRoleId().equals(
							role.getOrganizationRoleId())) {
						message.add(new Object[] { "�ڹ����ı�������������˸ý�ɫ", //$NON-NLS-1$
								workDefinition, SWT.ICON_WARNING });
						break;
					}

				}
			}
		}
		return message;
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��ɫ����"; //$NON-NLS-1$
	}

	/**
	 * ��ȡ��ɫ������������Ŀģ��
	 * 
	 * @return ProjectTemplate
	 */
	public ProjectTemplate getProjectTemplate() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
		if (ptId != null) {
			return ModelService.createModelObject(ProjectTemplate.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ��ɫ������������Ŀģ��_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

}
