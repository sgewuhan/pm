package com.sg.business.vault.view;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.expressions.PropertyTester;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.widgets.part.CurrentAccountContext;

public class EditDocumentPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Document) {
			String loginUserId = new CurrentAccountContext().getUserId();
			Document document = (Document) receiver;
			Folder folder = document.getFolder();
			ObjectId root_id = folder.getRoot_id();
			Organization org = ModelService.createModelObject(
					Organization.class, root_id);
			if (!org.isEmpty()) {
				List<String> userIds = org.getRoleAssignmentUserIds(
						Role.ROLE_VAULT_ADMIN_ID, Organization.ROLE_NOT_SEARCH);
				for (String userId : userIds) {
					if (loginUserId.equals(userId)) {
						return true;
					}
				}
			}

		}
		return false;
	}

}
