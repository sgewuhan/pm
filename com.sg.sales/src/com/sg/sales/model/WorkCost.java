package com.sg.sales.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.sales.ISalesRole;
import com.sg.sales.Sales;

public class WorkCost extends TeamControl implements IWorkRelative,
		IDataStatusControl, ICompanyRelative, IProjectRelative {

	public static final String F_CATAGORY = "catagory";
	public static final String F_FINISHDATE = "finishdate";
	public static final String F_STARTDATE = "startdate";
	public static final String F_STATUS = "coststatus";
	public static final String F_APPLY_WORK_ID = "applywork_id";
	public static final String F_ACTUAL_AMOUNT = "aamount";

	public static final String F_CHECK_ON = "checkon";
	public static final String F_CHECK_BY = "checkby";
	public static final String F_AUDIT_ON = "auditon";
	public static final String F_AUDIT_BY = "auditby";
	public static final String F_APPROVE_ON = "approveon";
	public static final String F_APPROVE_BY = "approveby";
	public static final String F_CLOSE_ON = "costcloseon";
	public static final String F_CLOSE_BY = "costcloseby";
	public static final String F_SUMMIT_ON = "summiton";
	public static final String F_SUMMIT_BY = "summitby";
	public static final String F_REDO_ON = "redoon";
	public static final String F_REDO_BY = "redoby";
	public static final String F_REJECT_ON = "rejecton";
	public static final String F_REJECT_BY = "rejectby";
	public static final String F_OPPORTUNITY_ID = "opportunity_id";
	public static final String F_CONTRACT_ID = "contract_id";
	public static final String F_DETAIL = "detail";
	public static final String F_LOCSTART = "locstart";
	public static final String F_LOCEND = "locend";

	private static final String MESSAGE_ERR_CATA_EMPTY = "类别不可为空";
	private static final String MESSAGE_ERR_CATA_REQUIRE_RELATIVE = "需要关联的数据";

	private static final String CATA_SALES = "销售费用";
	private static final String CATA_RND = "研发实施费用";
	private static final String CATA_PURCHASE = "采购费用";

	private static final String CATA_DETAIL_SALES_BONUS = "销售奖金";
	private static final String CATA_DETAIL_TRANSPOTATION = "交通费";
	private static final String CATA_DETAIL_PROJECT_BONUS = "项目奖金";

	@Override
	public AbstractWork getWork() {
		ObjectId workId = getWorkId();
		return workId != null ? ModelService.createModelObject(Work.class,
				workId) : null;
	}

	private ObjectId getWorkId() {
		return (ObjectId) getValue(F_WORK_ID);
	}

	public String getCostOwnerId() {
		return (String) getValue(F_OWNER);
	}

	public Date getFinishDate() {
		return (Date) getValue(F_FINISHDATE);
	}

	public Date getStartDate() {
		return (Date) getValue(F_STARTDATE);
	}

	@Override
	public String getStatusText() {
		return getStringValue(F_STATUS);
	}

	@Override
	public boolean canDelete(IContext context) {
		try {
			if (isPersistent()) {
				checkDataStatusForRemove(context);
			}
		} catch (Exception e) {
			return false;
		}
		return super.canDelete(context);
	}

	@Override
	public boolean canEdit(IContext context) {
		try {
			if (isPersistent()) {
				checkDataStatusForUpdate(context);
			}
		} catch (Exception e) {
			return false;
		}
		return super.canEdit(context);
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		if (canDelete(context)) {
			checkDataStatusForRemove(context);
			super.doRemove(context);
		} else {
			throw new Exception(MESSAGE_NOT_PERMISSION);
		}
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		if (canEdit(context)) {
			checkDataStatusForUpdate(context);
			super.doUpdate(context);
		} else {
			throw new Exception(MESSAGE_NOT_PERMISSION);
		}
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		checkCostRelative(context);
		return super.doSave(context);
	}

	/**
	 * 检查费用相关数据的强制要求
	 * 
	 * @param context
	 * @throws Exception
	 */
	private void checkCostRelative(IContext context) throws Exception {
		Company company = getCompany();
		Project project = getProject();
		Contract contract = getContract();
		Opportunity opportunity = getOpportunity();
		Work work = (Work) getWork();

		String catagory = getCatagory();
		if (catagory.isEmpty()) {
			throw new Exception(MESSAGE_ERR_CATA_EMPTY);
		}

		String detail = getDetail();
		// 研发费用
		if (!validRelative(new Object[] { work, project, contract, company },
				new String[] { CATA_RND }, catagory, context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "费用发生的工作，研发所属项目，销售合同和客户");
		}
		// 采购需要
		if (!validRelative(new Object[] { work, contract, company },
				new String[] { CATA_PURCHASE }, catagory, context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "费用发生的工作，销售合同和客户");
		}

		// 销售费用
		if (!validRelative(new Object[] { work, opportunity, company },
				new String[] { CATA_SALES }, catagory, context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "费用发生的工作，销售机会和客户");
		}

		// 销售奖金需要关联到合同
		if (!validRelative(new Object[] { contract, company }, new String[] {
				CATA_DETAIL_PROJECT_BONUS, CATA_DETAIL_SALES_BONUS }, detail,
				context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "销售合同和客户");
		}

		// 项目奖金需要关联到合同
		if (!validRelative(new Object[] { contract, company, project },
				new String[] { CATA_DETAIL_PROJECT_BONUS,
						CATA_DETAIL_SALES_BONUS }, detail, context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "销售合同，客户和项目");
		}
		
		//交通费需要填写出发点和目标地点
		if (!validRelative(new Object[] { getValue(F_LOCSTART), getValue(F_LOCEND)},
				new String[] { 
						CATA_DETAIL_TRANSPOTATION}, detail, context)) {
			throw new Exception(MESSAGE_ERR_CATA_REQUIRE_RELATIVE + ":"
					+ "出发地点和目标地点");
		}

	}

	private boolean validRelative(Object[] objects, String[] catas,
			String catagory, IContext context) {
		if (Utils.inArray(catagory, catas)) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == null) {
					return false;
				}
			}
		}
		return true;
	}

	public String getCatagory() {
		return getText(F_CATAGORY);
	}

	private String getDetail() {
		return getText(F_DETAIL);
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		if (!EXPENSE_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
		if (!EXPENSE_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}
	}

	@Override
	public void checkDataStatusForApply(IContext context) throws Exception {
		if (!EXPENSE_VALUE_EDITING.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_APPLY);
		}
	}

	public static Work createExpenseApplyWork(Object[] workCosts,
			WorkDefinition workd, IContext context) throws Exception {
		// 检查费用状态
		ObjectId[] costIds = new ObjectId[workCosts.length];
		for (int i = 0; i < workCosts.length; i++) {
			((WorkCost) workCosts[i]).checkDataStatusForApply(context);
			costIds[i] = ((WorkCost) workCosts[i]).get_id();
		}

		Calendar cal = Calendar.getInstance();
		Date planstart = cal.getTime();
		cal.add(Calendar.DATE, 10);
		Date planfinish = cal.getTime();

		// 创建报销工作
		Work work = ModelService.createModelObject(Work.class);
		workd.makeStandloneWork(work, context);
		work.setValue(Work.F_CHARGER, context.getAccountInfo().getConsignerId());// 设置负责人为当前用户
		work.setValue(Work.F_WORK_CATAGORY,
				ISalesWork.WORK_CATAGORY_SALES_APPLYEXPENSE);
		work.setValue(Work.F_DESC, "费用支出报销申请");
		work.setValue(Work.F_PLAN_START, planstart);
		work.setValue(Work.F_PLAN_FINISH, planfinish);
		work.setValue(Work.F_EXPENSE_FORBIDDEN, Boolean.TRUE);// 设置本工作为禁止费用
		work.setValue(ISalesWork.F_EXPENSE_SN, String.format("%1$ty%1$tm", new Date()));
		work.doSave(context);

		ObjectId workId = work.get_id();

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_WORKCOST);
		DBObject q = new BasicDBObject().append(F__ID,
				new BasicDBObject().append("$in", costIds));
		DBObject o = new BasicDBObject().append("$set",
				new BasicDBObject().append(F_APPLY_WORK_ID, workId));
		col.update(q, o, false, true);

		return work;
	}

	public static void changeCostStatus(Work work, String operation,
			String actor) {
		ObjectId workId = work.get_id();
		DBObject query = new BasicDBObject().append(F_APPLY_WORK_ID, workId);
		Date date = new Date();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_WORKCOST);
		if ("summit".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_APPROVE_BY, null);
			val.put(F_APPROVE_ON, null);
			val.put(F_AUDIT_BY, null);
			val.put(F_AUDIT_ON, null);
			val.put(F_CHECK_BY, null);
			val.put(F_CHECK_ON, null);
			val.put(F_CLOSE_BY, null);
			val.put(F_CLOSE_ON, null);
			val.put(F_REDO_BY, null);
			val.put(F_REDO_ON, null);
			val.put(F_REJECT_BY, null);
			val.put(F_REJECT_ON, null);
			val.put(F_SUMMIT_BY, actor);
			val.put(F_SUMMIT_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_APPLY);
			// 清除其他记录字段
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("checked".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_CHECK_BY, actor);
			val.put(F_CHECK_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_CHECKED);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("audited".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_AUDIT_BY, actor);
			val.put(F_AUDIT_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_AUDITED);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("editing".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_REDO_BY, actor);
			val.put(F_REDO_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_EDITING);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("approved".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_APPROVE_BY, actor);
			val.put(F_APPROVE_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_APPROVED);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("rejected".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_REJECT_BY, actor);
			val.put(F_REJECT_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_REJECTED);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		} else if ("closed".equals(operation)) { //$NON-NLS-1$
			BasicDBObject val = new BasicDBObject();
			val.put(F_CLOSE_BY, actor);
			val.put(F_CLOSE_ON, date);
			val.put(F_STATUS, EXPENSE_VALUE_TRANSFER);
			col.update(query, new BasicDBObject().append("$set", val), false,
					true);
		}

	}

	@Override
	protected String getPermissionRoleNumber() {
		return ISalesRole.CRM_ADMIN_NUMBER;
	}

	@Override
	protected String[] getVisitableFields() {
		return null;
	}

	@Override
	protected String[] getDuplicateTeamFields() {
		return null;
	}

	@Override
	public ObjectId getCompanyId() {
		return (ObjectId) getValue(F_COMPANY_ID);
	}

	@Override
	public Company getCompany() {
		ObjectId _id = getCompanyId();
		return _id == null ? null : ModelService.createModelObject(
				Company.class, _id);
	}

	@Override
	public Project getProject() {
		ObjectId _id = getProjectId();
		return _id == null ? null : ModelService.createModelObject(
				Project.class, _id);
	}

	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	public Opportunity getOpportunity() {
		ObjectId _id = getOpportunityId();
		return _id == null ? null : ModelService.createModelObject(
				Opportunity.class, _id);
	}

	public ObjectId getOpportunityId() {
		return (ObjectId) getValue(F_OPPORTUNITY_ID);
	}

	public Contract getContract() {
		ObjectId _id = getContractId();
		return _id == null ? null : ModelService.createModelObject(
				Contract.class, _id);
	}

	public ObjectId getContractId() {
		return (ObjectId) getValue(F_CONTRACT_ID);
	}
}
