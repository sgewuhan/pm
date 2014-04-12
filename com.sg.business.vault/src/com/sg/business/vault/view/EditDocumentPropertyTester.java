package com.sg.business.vault.view;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.expressions.PropertyTester;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Container;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.widgets.part.CurrentAccountContext;

public class EditDocumentPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		Organization org = null;
		PrimaryObject po = null;
		if (receiver instanceof Document) {
			Document document = (Document) receiver;
			Folder folder = document.getFolder();
			ObjectId root_id = folder.getRoot_id();
			org = ModelService.createModelObject(
					Organization.class, root_id);
			po = document.getProject();

		} else if (receiver instanceof Container) {
			Container container = (Container) receiver;
			ObjectId root_id = container.get_id();
			org = ModelService.createModelObject(Organization.class,
					root_id, false);
		} else if (receiver instanceof Folder) {
			Folder folder = (Folder) receiver;
			ObjectId root_id = folder.getRoot_id();
			org = ModelService.createModelObject(Organization.class,
					root_id, false);
		}
		String loginUserId = new CurrentAccountContext().getUserId();
		if (org != null && !org.isEmpty()) {
			List<String> userIds = org.getRoleAssignmentUserIds(
					Role.ROLE_VAULT_ADMIN_ID, Organization.ROLE_NOT_SEARCH,
					po);
			for (String userId : userIds) {
				if (loginUserId.equals(userId)) {
					return true;
				}
			}
		}
		return false;
	}

}
