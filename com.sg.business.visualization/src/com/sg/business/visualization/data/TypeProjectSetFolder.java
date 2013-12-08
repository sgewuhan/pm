package com.sg.business.visualization.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.visualization.VisualizationActivator;
import com.sg.business.visualization.data.ProjectSetFolder;

public class TypeProjectSetFolder extends ProjectSetFolder {

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

	private void loadChildren() {
		List<String> result = getTypeOptions();
		if (result == null) {
			children = new Object[0];
		} else {
			children = new Object[result.size()];
			for (int i = 0; i < children.length; i++) {
				children[i] = new ProductTypeProvider(result.get(i), user.getUserid());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getTypeOptions() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,IModelConstants.C_PROJECT);
		Object ids = getOrganizationIdCascade(null).toArray();
		return col.distinct(
				Project.F_PRODUCT_TYPE_OPTION,
				new BasicDBObject().append(Project.F_LAUNCH_ORGANIZATION,
						new BasicDBObject().append("$in", ids)).append(
						ILifecycle.F_LIFECYCLE,
						new BasicDBObject().append("$in", new String[] {
								ILifecycle.STATUS_FINIHED_VALUE,
								ILifecycle.STATUS_WIP_VALUE })));
	}

	private Collection<? extends ObjectId> getOrganizationIdCascade(
			Organization org) {
		Set<ObjectId> result = new HashSet<ObjectId>();

		List<PrimaryObject> orglist;
		if (org != null) {
			result.add(org.get_id());
			orglist = org.getChildrenOrganization();
		} else {
			orglist = getUsersManagedOrganization();
		}

		for (int i = 0; i < orglist.size(); i++) {
			result.addAll(getOrganizationIdCascade((Organization) orglist
					.get(i)));
		}

		return result;
	}

	private List<PrimaryObject> getUsersManagedOrganization() {
		// 获取当前用户担任管理者角色的部门
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
	public String getLabel() {
		return "产品类型分组";
	}

	@Override
	public String getImageURL() {
		return FileUtil.getImageURL("folder_32.png", VisualizationActivator.PLUGIN_ID);
	}

	@Override
	public String getDescription() {
		return "按您管理组织承担项目的产品类型分组";
	}
}
