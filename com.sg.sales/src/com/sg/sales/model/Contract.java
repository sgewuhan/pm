package com.sg.sales.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.sales.ISalesRole;

public class Contract extends CompanyRelativeTeamControl implements
		IDataStatusControl {

	public static final String F_SIGNBY = "signby";
	
	public static final String F_EFFECTIVEON = "effectiveon";

	public static final String F_STATUS = "contractstatus";

	public static final String F_AMOUNT = "amount";
	
	public static final String F_INCOME = "iamount";

	@Override
	public void doInsert(IContext context) throws Exception {
		// 合同的签订人需要自动加为所有者
		String signById = (String) getValue(F_SIGNBY);
		if (signById != null) {
			addToPermissionOwnerList(signById);
		}
		super.doInsert(context);
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
	public String getStatusText() {
		return getStringValue(F_STATUS);
	}

	@Override
	public void checkDataStatusForRemove(IContext context) throws Exception {
		if (!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_REMOVE);
		}
	}

	@Override
	public void checkDataStatusForUpdate(IContext context) throws Exception {
//		if (!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))) {
//			throw new Exception(MESSAGE_CANNOT_MODIFY);
//		}
	}

	@Override
	public void checkDataStatusForApply(IContext context) throws Exception {
		if (!CONTRACT_VALUE_NOTVALID.equals(getValue(F_STATUS))) {
			throw new Exception(MESSAGE_CANNOT_APPLY);
		}
	}

	@Override
	protected String[] getRoleDesignatedUserFieldName() {
		return DESIGNATED_FIELDS_BY_ROLE;
	}

	@Override
	protected String getRoleNumberDesignatedUserField(String field) {
		if (F_SALES_SUP.equals(field)) {
			return ISalesRole.SALES_SUPERVISOR_NUMBER;
		}
		return null;
	}

	public void doCalculateAmountSummary() {
		List<PrimaryObject> poitems = getRelationById(F__ID,
				POItem.F_CONTRACT_ID, POItem.class);
		double summary = 0d;
		if (poitems != null && poitems.size() > 0) {
			for (PrimaryObject po : poitems) {
				POItem poi = (POItem) po;
				summary += poi.getSummary();
			}
		}
		DBCollection col = getCollection();
		DBObject newData = col
				.findAndModify(
						new BasicDBObject().append(F__ID, get_id()),
						null,
						null,
						false,
						new BasicDBObject()
								.append("$set", new BasicDBObject().append(F_AMOUNT, new Double(summary))), true, false); //$NON-NLS-1$
		set_data(newData);

	}

	public void doCalculateIncomeSummary() {
		List<PrimaryObject> poitems = getRelationById(F__ID,
				Income.F_CONTRACT_ID, Income.class);
		double summary = 0d;
		if (poitems != null && poitems.size() > 0) {
			for (PrimaryObject po : poitems) {
				Income ic = (Income) po;
				summary += ic.getAmount();
			}
		}
		DBCollection col = getCollection();
		DBObject newData = col
				.findAndModify(
						new BasicDBObject().append(F__ID, get_id()),
						null,
						null,
						false,
						new BasicDBObject()
								.append("$set", new BasicDBObject().append(F_INCOME, new Double(summary))), true, false); //$NON-NLS-1$
		set_data(newData);
	}
}
