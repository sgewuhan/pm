package com.sg.business.vault.view;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ISelection;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Folder;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.IPopupMenuProvider;
import com.sg.widgets.viewer.ViewerControl;

public class EditorDocumentForVault implements IPopupMenuProvider {

	@Override
	public String getMenuId(ISelection iSelection, ViewerControl viewerControl) {
		PrimaryObject master = viewerControl.getMaster();
		if (master instanceof Folder) {
			Folder folder = (Folder) master;
			ObjectId root_id = folder.getRoot_id();
			Organization org = ModelService.createModelObject(
					Organization.class, root_id);
			if (!org.isEmpty()) {
				String loginUserId = new CurrentAccountContext().getUserId();
				List<String> userIds = org.getRoleAssignmentUserIds(
						Role.ROLE_VAULT_ADMIN_ID, Organization.ROLE_NOT_SEARCH,
						folder.getProject());
				for (String userId : userIds) {
					if (loginUserId.equals(userId)) {
						return viewerControl.getConfigurator().getPopupMenu();
					}
				}
			}
		}
		return "popup:vault.folder.user1";
	}

	@Override
	public boolean changeMenuWithSelection() {
		return true;
	}

}
