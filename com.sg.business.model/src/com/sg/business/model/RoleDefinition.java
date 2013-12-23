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
 * 角色定义
 * <p/>
 * 在项目模板和项目中定义的角色
 * 
 * @author jinxitao
 * 
 */
public class RoleDefinition extends AbstractRoleDefinition implements
		IProjectTemplateRelative {

	/**
	 * 创建角色的编辑器
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition"; //$NON-NLS-1$

	public static final String F_WORK_ID = "work_id"; //$NON-NLS-1$
	
	/**
	 * 独立工作定义_id
	 */
	public static final String F_WORKDEFINITION_ID="workdefinition_id"; //$NON-NLS-1$

	/**
	 * 删除角色检查
	 */
	public List<Object[]> checkRemoveAction(IContext context) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.WBS引用的角色
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
		// 2.提交流程上引用
		if (pc.isWorkflowActivate(ProjectTemplate.F_WF_COMMIT)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc
					.getProcessDefinition(ProjectTemplate.F_WF_COMMIT);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// 检查角色
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

		// 3.变更流程上引用

		if (pc.isWorkflowActivate(ProjectTemplate.F_WF_CHANGE)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc
					.getProcessDefinition(ProjectTemplate.F_WF_CHANGE);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if (!na.isNeedAssignment()) {
					continue;
				}
				String nap = na.getNodeActorParameter();
				// 检查角色
				AbstractRoleDefinition role = pc.getProcessActionAssignment(
						ProjectTemplate.F_WF_CHANGE, nap);
				if (getOrganizationRoleId()
						.equals(role.getOrganizationRoleId())) {
					message.add(new Object[] { "在项目模版的变更流程中引用了该角色", this, //$NON-NLS-1$
							SWT.ICON_WARNING });
					break;
				}

			}
		}
		// 4.工作流程上引用
		WorkDefinition root = projectTemplate.getWBSRoot();
		message.addAll(checkCascadeRemove(root));
		return message;
	}

	private List<Object[]> checkCascadeRemove(WorkDefinition workDefinition) {
		List<Object[]> message = new ArrayList<Object[]>();
		List<PrimaryObject> childrenWork = workDefinition.getChildrenWork();
		if (childrenWork.size() > 0) {// 如果有下级，返回下级的检查结果
			for (int i = 0; i < childrenWork.size(); i++) {
				WorkDefinition childWork = (WorkDefinition) childrenWork.get(i);
				message.addAll(checkCascadeRemove(childWork));
			}
		} else {
			// 1.执行流程上引用
			if (workDefinition.isWorkflowActivate(WorkDefinition.F_WF_EXECUTE)) {
				// 如果流程已经激活，需要判断是否所有的actor都指派
				DroolsProcessDefinition pd = workDefinition
						.getProcessDefinition(WorkDefinition.F_WF_EXECUTE);
				List<NodeAssignment> nalist = pd.getNodesAssignment();
				for (int i = 0; i < nalist.size(); i++) {
					NodeAssignment na = nalist.get(i);
					if (!na.isNeedAssignment()) {
						continue;
					}
					String nap = na.getNodeActorParameter();
					// 检查角色
					ProjectRole role = workDefinition
							.getProcessActionAssignment(
									WorkDefinition.F_WF_EXECUTE, nap);
					if (getOrganizationRoleId().equals(
							role.getOrganizationRoleId())) {
						message.add(new Object[] { "在工作的执行流程中引用了该角色", //$NON-NLS-1$
								workDefinition, SWT.ICON_WARNING });
						break;
					}

				}
			}

			// 2.变更流程上引用
			if (workDefinition.isWorkflowActivate(WorkDefinition.F_WF_CHANGE)) {
				// 如果流程已经激活，需要判断是否所有的actor都指派
				DroolsProcessDefinition pd = workDefinition
						.getProcessDefinition(WorkDefinition.F_WF_CHANGE);
				List<NodeAssignment> nalist = pd.getNodesAssignment();
				for (int i = 0; i < nalist.size(); i++) {
					NodeAssignment na = nalist.get(i);
					if (!na.isNeedAssignment()) {
						continue;
					}
					String nap = na.getNodeActorParameter();
					// 检查角色
					ProjectRole role = workDefinition
							.getProcessActionAssignment(
									WorkDefinition.F_WF_CHANGE, nap);
					if (getOrganizationRoleId().equals(
							role.getOrganizationRoleId())) {
						message.add(new Object[] { "在工作的变更流程中引用了该角色", //$NON-NLS-1$
								workDefinition, SWT.ICON_WARNING });
						break;
					}

				}
			}
		}
		return message;
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "角色定义"; //$NON-NLS-1$
	}

	/**
	 * 获取角色定义所属的项目模板
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
	 * 获取角色定义所属的项目模板_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getProjectTemplateId() {
		return (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
	}

}
