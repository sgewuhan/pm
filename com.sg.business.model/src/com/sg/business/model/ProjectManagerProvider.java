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
			int iF_SUMMARY_FINISHED = 0;
			int iF_SUMMARY_FINISHED_DELAY = 0;
			int iF_SUMMARY_FINISHED_NORMAL = 0;
			int iF_SUMMARY_FINISHED_ADVANCED = 0;

			int iF_SUMMARY_PROCESSING = 0;
			int iF_SUMMARY_PROCESSING_DELAY = 0;
			int iF_SUMMARY_PROCESSING_NORMAL = 0;
			int iF_SUMMARY_PROCESSING_ADVANCE = 0;

			int iF_SUMMARY_FINISHED_COSTNORMAL = 0;
			int iF_SUMMARY_FINISHED_COSTOVER = 0;
			int iF_SUMMARY_PROCESSING_COSTNORMA = 0;
			int iF_SUMMARY_PROCESSING_COSTOVER = 0;

			long iF_SUMMARY_TOTAL_BUDGETAMOUNT = 0;
			long iF_SUMMARY_TOTAL_INVESTMENTAMOUNT = 0;

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
					iF_SUMMARY_FINISHED++;
					if (project.isDelay()) {
						iF_SUMMARY_FINISHED_DELAY++;
					} else if (project.isAdvanced()) {
						iF_SUMMARY_FINISHED_ADVANCED++;
					} else {
						iF_SUMMARY_FINISHED_NORMAL++;
					}
					if(project.isOverCost()){
						iF_SUMMARY_FINISHED_COSTOVER++;
					}else{
						iF_SUMMARY_FINISHED_COSTNORMAL++;
					}
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
						.getLifecycleStatus())) {
					iF_SUMMARY_PROCESSING++;
					if (project.maybeDelay()) {
						iF_SUMMARY_PROCESSING_DELAY++;
					} else if (project.maybeAdvanced()) {
						iF_SUMMARY_PROCESSING_ADVANCE++;
					} else {
						iF_SUMMARY_PROCESSING_NORMAL++;
					}
					
					if(project.maybeOverCostNow()){
						iF_SUMMARY_PROCESSING_COSTOVER++;
					}else{
						iF_SUMMARY_PROCESSING_COSTNORMA++;
					}
				}
				
				
				Double budgetValue = project.getBudgetValue();
				iF_SUMMARY_TOTAL_BUDGETAMOUNT += budgetValue == null ? 0
						: budgetValue;
				iF_SUMMARY_TOTAL_INVESTMENTAMOUNT += project
						.getInvestment();
				result.add(project);
			}
			sum.total = result.size();

			sum.finished = iF_SUMMARY_FINISHED;
			sum.finished_delay = iF_SUMMARY_FINISHED_DELAY;
			sum.finished_normal = iF_SUMMARY_FINISHED_NORMAL;
			sum.finished_advance = iF_SUMMARY_FINISHED_ADVANCED;

			sum.processing = iF_SUMMARY_PROCESSING;
			sum.processing_delay = iF_SUMMARY_PROCESSING_DELAY;
			sum.processing_normal = iF_SUMMARY_PROCESSING_NORMAL;
			sum.processing_advance = iF_SUMMARY_PROCESSING_ADVANCE;

			sum.finished_cost_normal = iF_SUMMARY_FINISHED_COSTNORMAL;
			sum.finished_cost_over = iF_SUMMARY_FINISHED_COSTOVER;

			sum.processing_cost_normal = iF_SUMMARY_PROCESSING_COSTNORMA;
			sum.processing_cost_over = iF_SUMMARY_PROCESSING_COSTOVER;

			sum.total_budget_amount = iF_SUMMARY_TOTAL_BUDGETAMOUNT;
			sum.total_investment_amount = iF_SUMMARY_TOTAL_INVESTMENTAMOUNT;


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
