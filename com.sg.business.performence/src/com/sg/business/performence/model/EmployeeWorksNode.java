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
	 * ��ʾ��֯
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
	 * ����user������
	 */
	@Override
	public String getLabel() {
		User user = (User) getData();
		return user.getLabel();
	}

	/**
	 * ���û��ڵ���ӹ���
	 * 
	 * @param work
	 */
	public void addWork(Work work) {
		if (work.isProjectWork()) {
			// �������Ŀ����...
			Project project = work.getProject();
			// ��chilren�в��Ҹ���Ŀ����ʽ�ο�DataSet�е��жϷ�ʽ
			ProjectWorksNode projectNode = getNodeByProject(project);

			// ���û���ҵ�
			if (projectNode == null) {
				projectNode = new ProjectWorksNode(this, project);
				addChild(projectNode);
			}
			// ��������Ҫ���������ڵ�
			WorkWorksNode workNode = new WorkWorksNode(projectNode, work);
			projectNode.addChild(workNode);
		} else {
			// ����Ƕ������������ö��������Ƿ���Ҫ����Ŀ�м��㹤ʱ������ǾͲ����κδ���
			// ������ǣ���Ҫ���ö������������Ӧ��WorkWorksNode��ӵ�children��
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
