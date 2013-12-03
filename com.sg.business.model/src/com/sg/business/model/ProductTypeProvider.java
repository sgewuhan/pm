package com.sg.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.toolkit.UserToolkit;
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
		setValue(F_DESC,desc);

	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCursor cur = projectCol
					.find(getQueryCondtion(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(project
						.getLifecycleStatus())) {
					sum.finished++;
					if (project.isDelay()) {
						sum.finished_delay++;
					} else if (project.isAdvanced()) {
						sum.finished_advance++;
					} else {
						sum.finished_normal++;
					}
					if(project.isOverCost()){
						sum.finished_cost_over++;
					}else{
						sum.finished_cost_normal++;
					}
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
						.getLifecycleStatus())) {
					sum.processing++;
					if (project.maybeDelay()) {
						sum.processing_delay++;
					} else if (project.maybeAdvanced()) {
						sum.processing_advance++;
					} else {
						sum.processing_normal++;
					}
					
					if(project.maybeOverCostNow()){
						sum.processing_cost_over++;
					}else{
						sum.processing_cost_normal++;
					}
				}
				
				Double budgetValue = project.getBudgetValue();
				sum.total_budget_amount += budgetValue == null ? 0
						: budgetValue;
				sum.total_investment_amount += project
						.getInvestment();
				double[] salesSummaryData = project.getSalesSummaryData();
				sum.total_sales_revenue+=salesSummaryData[0];
				sum.total_sales_cost+=salesSummaryData[1];
				result.add(project);
			}
			sum.total = result.size();


		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	private DBObject getQueryCondtion(Date start, Date stop) {

		Object ids = getOrganizationIdCascade(null).toArray();
		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_PRODUCT_TYPE_OPTION, getDesc());
		dbo.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in",ids));
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),
						new BasicDBObject().append(
								"$and",
								new BasicDBObject[] {
										new BasicDBObject().append(
												Project.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lte", start)),
										new BasicDBObject().append(
												Project.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }) });

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
		return getDesc() + "ÏîÄ¿¼¯";
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

	@Override
	public String getDesc() {
		return desc;
	}
	
}
