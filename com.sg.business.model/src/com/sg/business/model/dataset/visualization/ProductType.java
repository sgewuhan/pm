package com.sg.business.model.dataset.visualization;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class ProductType extends SingleDBCollectionDataSetFactory {

	private String userId;
	private User user;

	public ProductType() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		user = UserToolkit.getUserById(userId);

	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
		List<String> options = getTypeOptions();
		for (String option : options) {
			ProductTypeProvider projectType = new ProductTypeProvider(option,
					userId);
			dataItems.add(projectType);
		}
		return new DataSet(dataItems);
	}

	// private List<String> getTypeOptions() {
	// List<String> typeList = new ArrayList<String>();
	// DBCollection collection = getCollection();
	// DBCursor cur = collection.find();
	// while (cur.hasNext()) {
	// DBObject dbo = cur.next();
	// ProjectTemplate template = ModelService.createModelObject(dbo,
	// ProjectTemplate.class);
	// Object value = template
	// .getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
	// if (value instanceof List) {
	// @SuppressWarnings("unchecked")
	// List<Object> list = (List<Object>) value;
	// for (Object obj : list) {
	// if (!typeList.contains(obj)) {
	// typeList.add((String) obj);
	// }
	// }
	//
	// }
	//
	// }
	// return typeList;
	// }

	private List<String> getTypeOptions() {
		List<String> typeList = new ArrayList<String>();
		DBCollection col = getCollection();
		DBCursor cur = col.find(new BasicDBObject().append(
				Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", getUerOrgId())).append(
				ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE })));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Project project = ModelService
					.createModelObject(dbo, Project.class);
			List<String> productTypeOptions = project.getProductTypeOptions();
			if (productTypeOptions == null) {
				continue;
			}
			for (String option : productTypeOptions) {
				if (!typeList.contains(option)) {
					typeList.add(option);
				}
			}
		}
		return typeList;
	}

	protected List<ObjectId> getUerOrgId() {
		List<ObjectId> list = new ArrayList<ObjectId>();
		List<PrimaryObject> userOrg = getUserOrg(
				new ArrayList<PrimaryObject>(), getInput());
		for (PrimaryObject po : userOrg) {
			list.add(po.get_id());
		}
		return list;

	}

	protected List<PrimaryObject> getUserOrg(List<PrimaryObject> list,
			List<PrimaryObject> childrenList) {
		list.addAll(childrenList);
		for (PrimaryObject po : childrenList) {
			List<PrimaryObject> childrenOrg = ((Organization) po)
					.getChildrenOrganization();
			getUserOrg(list, childrenOrg);
		}
		return list;
	}

	protected List<PrimaryObject> getInput() {
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
}
