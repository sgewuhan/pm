package com.sg.business.performence.works;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.performence.organization.OrgResCalender;
import com.sg.business.performence.ui.calendar.WorkListCellEditor;

public class WorksRegister extends OrgResCalender {

	protected List<PrimaryObject> getOrganizationRoots(User user) {
		return user.getRoleGrantedInAllOrganization(Role.ROLE_PROJECT_ADMIN_ID);
	}

	@Override
	protected void initdata() {
		disposeGrid();
		groupcount = 1;
		createGrid();
	}

	@Override
	protected EditingSupport getEditingSupport(final int year, final int month,
			final int dateOfMonth) {
		return new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				IStructuredSelection sel = (IStructuredSelection) viewer
						.getSelection();
				return new WorkListCellEditor(viewer.getGrid(),
						(User) (sel.getFirstElement()), year, month,
						dateOfMonth);
			}

			@Override
			protected boolean canEdit(Object element) {
				IStructuredSelection sel = (IStructuredSelection) viewer
						.getSelection();
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
