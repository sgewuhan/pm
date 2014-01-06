package com.sg.business.model;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.Portal;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.fields.value.IFileBase;

public class FolderDistributeFileBase implements IFileBase {

	public FolderDistributeFileBase() {
	}

	@Override
	public String getDB(PrimaryObject doc) {
		String filebase = null;
		//根据当前文档查找filebase
		if (doc != null) {
			filebase = doc.get_filebase_db();
			if (filebase != null) {
				return filebase;
			}
			AccountInfo ca = doc.get_caccount();
			if (ca != null) {
				String userId = ca.getUserId();
				filebase = getFileBaseByUserId(userId);
				if (filebase != null) {
					return filebase;
				}
			}
		}
		//根据当前用户查找filebase
		String userId = new CurrentAccountContext().getConsignerId();
		filebase = getFileBaseByUserId(userId);
		if (filebase != null) {
			return filebase;
		}

		//取根组织的filebase
		Organization org = UserToolkit.getRootOrganization();
		Organization container = org.getContainerOrganization();
		filebase = container.getFileBase();
		if (filebase != null) {
			return filebase;
		}

		//取系统filebase
		return Portal.getBasicDB().getName();
	}

	private String getFileBaseByUserId(String userId) {
		User user = UserToolkit.getUserById(userId);
		Organization org = user.getOrganization();
		if (org == null) {
			org = UserToolkit.getRootOrganization();
		}
		Organization container = org.getContainerOrganization();
		return container.getFileBase();
	}

	@Override
	public String getNamespace(PrimaryObject doc) {
		if (doc != null) {
			String filebase = doc.get_filebase_namespace();
			if (filebase instanceof String) {
				return filebase;
			}
		}
		return null;
	}

}
