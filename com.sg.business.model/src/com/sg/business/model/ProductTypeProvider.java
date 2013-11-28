package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
						.getInvestmentValue();
				result.add(project);
			}
			summaryData.total = result.size();

			summaryData.finished = iF_SUMMARY_FINISHED;
			summaryData.finished_delay = iF_SUMMARY_FINISHED_DELAY;
			summaryData.finished_normal = iF_SUMMARY_FINISHED_NORMAL;
			summaryData.finished_advance = iF_SUMMARY_FINISHED_ADVANCED;

			summaryData.processing = iF_SUMMARY_PROCESSING;
			summaryData.processing_delay = iF_SUMMARY_PROCESSING_DELAY;
			summaryData.processing_normal = iF_SUMMARY_PROCESSING_NORMAL;
			summaryData.processing_advance = iF_SUMMARY_PROCESSING_ADVANCE;

			summaryData.finished_cost_normal = iF_SUMMARY_FINISHED_COSTNORMAL;
			summaryData.finished_cost_over = iF_SUMMARY_FINISHED_COSTOVER;

			summaryData.processing_cost_normal = iF_SUMMARY_PROCESSING_COSTNORMA;
			summaryData.processing_cost_over = iF_SUMMARY_PROCESSING_COSTOVER;

			summaryData.total_budget_amount = iF_SUMMARY_TOTAL_BUDGETAMOUNT;
			summaryData.total_investment_amount = iF_SUMMARY_TOTAL_INVESTMENTAMOUNT;


		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	private DBObject getQueryCondtion(Date start, Date stop) {

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_PRODUCT_TYPE_OPTION, getDesc());
		dbo.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", getUerOrgId()));
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
		return getDesc() + "项目集";
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
