package com.sg.business.visualization.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.visualization.VisualizationActivator;

public class OrganizationProjectSetFolder extends ProjectSetFolder {

	
	private Object[] children = null;

	@Override
	public Object[] getChildren() {
		if (this.children == null) {
			loadChildren();
		}
		return children;
	}

	@Override
	public boolean hasChildren() {
		if (this.children == null) {
			loadChildren();
		}
		return children.length > 0;
	}
	
	public void loadChildren() {
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
		
		children = input.toArray();
	}
	
	@Override
	public String getLabel() {
		return "项目承担组织";
	}

	@Override
	public String getImageURL() {
		return FileUtil.getImageURL("org_folder_32.png", VisualizationActivator.PLUGIN_ID);
	}

	@Override
	public String getDescription() {
		return "您管理组织的承担的项目集合";
	}


}
