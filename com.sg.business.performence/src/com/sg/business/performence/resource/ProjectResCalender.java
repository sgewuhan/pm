package com.sg.business.performence.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectResCalender extends ResourceCalender {

	@Override
	protected IContentProvider getContentProvider() {
		return new ProjectResContentProvider();
	}

	@Override
	protected List<PrimaryObject> getInput() {

		// ��ȡ��ǰ�û����ι����߽�ɫ�Ĳ���
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);

		return getProjectOfOrganizationList(orglist);
	}

	private List<PrimaryObject> getProjectOfOrganizationList(
			List<PrimaryObject> orglist) {
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			input.addAll(getProjectOfOrganization(org));
			List<PrimaryObject> children = org.getChildrenOrganization();
			if (children != null && children.size() > 0) {
				input.addAll(getProjectOfOrganizationList(children));
			}
		}

		return input;
	}

	private Collection<? extends PrimaryObject> getProjectOfOrganization(
			Organization org) {
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		List<PrimaryObject> projectList = org.getRelationByCondition(
				Project.class,
				new BasicDBObject().append(Project.F_FUNCTION_ORGANIZATION,
						org.get_id()));
		for (int j = 0; j < projectList.size(); j++) {
			PrimaryObject project = projectList.get(j);
			if (!input.contains(project)) {
				input.add(project);
			}
		}

		return input;
	}

}