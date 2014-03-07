package com.sg.sales.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.sales.ISalesRole;
import com.sg.sales.Sales;

public class WorkCost extends TeamControl implements IWorkRelative,
		IDataStatusControl {

	public static final String F_CATAGORY = "catagory";
	public static final String F_FINISHDATE = "finishdate";
	public static final String F_STARTDATE = "startdate";
	public static final String F_STATUS = "coststatus";
	public static final String F_APPLY_WORK_ID = "applywork_id";

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

	@Override
	public AbstractWork getWork() {
		return ModelService.createModelObject(Work.class,
				(ObjectId) getValue(F_WORK_ID));
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

}
