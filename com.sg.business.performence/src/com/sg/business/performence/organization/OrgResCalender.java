package com.sg.business.performence.organization;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.performence.ui.calendar.ResourceCalender;
import com.sg.business.performence.ui.calendar.WorkListCellEditor;
import com.sg.widgets.part.CurrentAccountContext;

public class OrgResCalender extends ResourceCalender {


	@Override
	protected IContentProvider getContentProvider() {
		return new OrgResContentProvider();
	}

	@Override
	protected List<PrimaryObject> getInput() {

		// 获取当前用户担任管理者角色的部门
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
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
				IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
				return sel.getFirstElement() instanceof User;
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
