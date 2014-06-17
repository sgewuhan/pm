package com.sg.business.performence.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;

public class EmployeeWorksNode extends WorksNode {

	public EmployeeWorksNode(WorksNode parent, PrimaryObject data) {
		super(parent, data);
	}

	/**
	 * 显示组织
	 */
	@Override
	public String getAdditionInfomation() {
		User user = (User) getData();
		Organization organization = user.getOrganization();
		if (organization == null) {
			return "";
		}
		String desc = organization.getPath(2);
		return desc;
	}

	/**
	 * 返回user的名称
	 */
	@Override
	public String getLabel() {
		User user = (User) getData();
		return user.getLabel();
	}

	/**
	 * 向用户节点添加工作
	 * 
	 * @param work
	 */
	public void addWork(Work work) {
		if (work.isProjectWork()) {
			// 如果是项目工作...
			Project project = work.getProject();
			// 在chilren中查找该项目，方式参考DataSet中的判断方式
			ProjectWorksNode projectNode = getNodeByProject(project);

			// 如果没有找到
			if (projectNode == null) {
				projectNode = new ProjectWorksNode(this, project);
				addChild(projectNode);
			}
			// 接下来需要构建工作节点
			WorkWorksNode workNode = new WorkWorksNode(projectNode, work);
			projectNode.addChild(workNode);
		} else {
			// 如果是独立工作，检查该独立工作是否需要在项目中计算工时，如果是就不做任何处理
			// 如果不是，需要将该独立工作构造对应的WorkWorksNode添加到children中
			if (Boolean.TRUE.equals(work
					.getValue(Work.F_JOIN_PROJECT_CALCWORKS))) {
				return;
			} else {
				WorkWorksNode workNode = new WorkWorksNode(this, work);
				addChild(workNode);
			}
		}
	}


	private ProjectWorksNode getNodeByProject(Project project) {
		ProjectWorksNode node = new ProjectWorksNode(null, project);
		int index = getChildren().indexOf(node);
		if (index == -1) {
			return null;
		} else {
			return (ProjectWorksNode) getChildren().get(index);
		}

	}

}
