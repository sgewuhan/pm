package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.widgets.MessageUtil;

public class ProjectManagerProvider extends ProjectProvider {

	private User user;
	private DBCollection projectCol;

	public void setUser(User user) {
		this.user = user;
		setValue(F__ID, user.get_id());
		setValue(F_DESC, user.getUserid()+"/"+user.getUsername());
		projectCol = getCollection(IModelConstants.C_PROJECT);
	}
	
	
	@Override
	public String getDesc() {
		return user.getUsername();
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

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_CHARGER, user.getUserid());
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

	@Override
	public String getProjectSetName() {
		return "负责人项目集";
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

}
