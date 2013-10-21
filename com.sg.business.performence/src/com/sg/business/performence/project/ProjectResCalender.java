package com.sg.business.performence.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.performence.ui.calendar.ResourceCalender;
import com.sg.business.performence.ui.calendar.WorkListCellEditor;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectResCalender extends ResourceCalender {

	@Override
	protected IContentProvider getContentProvider() {
		return new ProjectResContentProvider();
	}

	@Override
	protected List<PrimaryObject> getInput() {

		// 获取当前用户担任管理者角色的部门
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

	@Override
	protected EditingSupport getEditingSupport(final int year, final int month,
			final int dateOfMonth) {
		return new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
				return new WorkListCellEditor(viewer.getGrid(), (User) (sel.getFirstElement()),
						year, month, dateOfMonth);
			}

			@Override
			protected boolean canEdit(Object element) {
//				System.out.println(element);
//				Object id = ((PrimaryObject) element).getValue("$projectid");
//				if(id instanceof ObjectId){
//					Project project = ModelService.createModelObject(Project.class, (ObjectId)id);
//					System.out.println(project);
//				}
				IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();

				
				return (sel.getFirstElement()) instanceof User;
			}

			@Override
			protected Object getValue(Object element) {
				return "";
			}

			@Override
			protected void setValue(Object element, Object value) {
			}

		};
	}

}