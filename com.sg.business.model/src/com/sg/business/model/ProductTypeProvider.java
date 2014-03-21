package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;

public class ProductTypeProvider extends ProjectProvider {

	private DBCollection projectCol;
	private String userId;
	private String desc;

	public ProductTypeProvider(String desc, String userId) {
		super();
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		set_data(new BasicDBObject());
		this.desc = desc;
		this.userId = userId;
		setValue(F_DESC, desc);

	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	@Override
	public void doInsert(IContext context) throws Exception {
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCursor cur = projectCol
					.find(getQueryCondition(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				ProjectPresentation pres = project.getPresentation();
				if(!pres.isPresentationAvailable()){
					continue;
				}
				
				
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(project
						.getLifecycleStatus())) {
					sum.finished++;
					if (pres.isDelayDefinited()) {
						sum.finished_delay++;
					} else if (pres.isAdvanceDefinited()) {
						sum.finished_advance++;
					} else {
						sum.finished_normal++;
					}
					if (pres.isOverCostDefinited()) {
						sum.finished_cost_over++;
					} else {
						sum.finished_cost_normal++;
					}
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
						.getLifecycleStatus())) {
					sum.processing++;
					if (pres.isDelayEstimated()) {
						sum.processing_delay++;
					} else if (pres.isAdvanceEstimated()) {
						sum.processing_advance++;
					} else {
						sum.processing_normal++;
					}

					if (pres.isOverCostEstimated()) {
						sum.processing_cost_over++;
					} else {
						sum.processing_cost_normal++;
					}
				}

				sum.total_budget_amount += pres.getBudgetValue();
				sum.total_investment_amount += pres.getInvestment();
				sum.total_sales_revenue += pres.getSalesRevenue();
				sum.total_sales_cost += pres.getSalesCost();
				result.add(project);
			}
			sum.total = result.size();

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	public BasicDBObject getQueryCondition(Date start, Date stop) {
		BasicDBObject dbo = super.getQueryCondition(start, stop);
		Object ids = getOrganizationIdCascade(null).toArray();
		dbo.put(Project.F_PRODUCT_TYPE_OPTION, getDesc());
		dbo.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", ids)); //$NON-NLS-1$
		return dbo;
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
		User user = UserToolkit.getUserById(getUserId());
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getProjectSetName() {
		return getDesc() + Messages.get().ProductTypeProvider_1;
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectId> getAllProjectId() {
		BasicDBObject query = new BasicDBObject();
		Object ids = getOrganizationIdCascade(null).toArray();
		query.put(Project.F_PRODUCT_TYPE_OPTION, getDesc());
		query.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", ids));
		return projectCol.distinct(Project.F__ID, query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectId> getSalesAllProjectId() {
		BasicDBObject query = new BasicDBObject();
		Object ids = getOrganizationIdCascade(null).toArray();
		query.put(Project.F_PRODUCT_TYPE_OPTION, getDesc());
		query.put(Project.F_BUSINESS_ORGANIZATION,
				new BasicDBObject().append("$in", ids));
		return projectCol.distinct(Project.F__ID, query);
	}

}
