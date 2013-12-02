package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.IPrimaryObjectEventListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.widgets.MessageUtil;

public class UserProjectPerf extends ProjectProvider {

	public static final String F_PROJECT_ID = "project_id";

	public static final String F_USERID = "userid";

	public static final String EDITOR_SETTING = "editor.visualization.addprojectset";

	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCollection projectCol = getCollection(IModelConstants.C_PROJECT);
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
					if (project.isOverCost()) {
						sum.finished_cost_over++;
					} else {
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

					if (project.maybeOverCostNow()) {
						sum.processing_cost_over++;
					} else {
						sum.processing_cost_normal++;
					}
				}

				Double budgetValue = project.getBudgetValue();
				sum.total_budget_amount += budgetValue == null ? 0
						: budgetValue;
				sum.total_investment_amount += project.getInvestment();
				double[] salesSummaryData = project.getSalesSummaryData();
				sum.total_sales_revenue += salesSummaryData[0];
				sum.total_sales_cost += salesSummaryData[1];
				result.add(project);
			}
			sum.total = result.size();

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	private DBObject getQueryCondtion(Date start, Date stop) {

		List<?> projectidlist = getProjectIdList();

		DBObject dbo = new BasicDBObject();
		dbo.put(F__ID, new BasicDBObject().append("$in", projectidlist));
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

	/**
	 * 获取本项目组合的项目
	 * 
	 * @return
	 */
	private List<?> getProjectIdList() {
		return (List<?>) getValue(F_PROJECT_ID);
	}

	@Override
	public String getProjectSetName() {
		return getDesc();
	}

	@Override
	public String getTypeName() {
		return "自定义项目组合";
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		if (!isPersistent()) {
			doInsert(context);
			fireEvent(IPrimaryObjectEventListener.INSERTED);
		} else {
			doUpdate(context);
			fireEvent(IPrimaryObjectEventListener.UPDATED);
		}
		return true;
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		DBObject data = get_data();
		ObjectId oid = get_id();
		if (oid == null) {
			oid = new ObjectId();
		}
		data.put(F__ID, oid);
		DBCollection col = getCollection();
		col.insert(data);
	}
}
