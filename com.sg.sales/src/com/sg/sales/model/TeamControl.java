package com.sg.sales.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TeamControl extends PrimaryObject implements ISalesTeam {

	public static final String F_VISITORLIST = "visitor_list";
	public static final String F_ORIGINAL_OWNERLIST = "owner_list";
	public static final String F_OWNER = "owner";
	protected static final String MESSAGE_NOT_PERMISSION = "您没有获得操作的权限";

	@Override
	public boolean canDelete(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(new String[] { F_OWNER,F_ORIGINAL_OWNERLIST }, userId);
	}

	@Override
	public boolean canEdit(IContext context) {
		if(!isPersistent()){
			return true;
		}
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(
				new String[] { F_OWNER, F_ORIGINAL_OWNERLIST, F_CUSTOMER_REP,
						F_SALES_MANAGER, F_SALES_SUP, F_SERVICE_MANAGER },
				userId);
	}

	@Override
	public boolean canRead(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(new String[] { F_OWNER, F_ORIGINAL_OWNERLIST,
				F_CUSTOMER_REP, F_SALES_MANAGER, F_SALES_SUP,
				F_SERVICE_MANAGER, F_VISITORLIST }, userId);
	}

	// 获得我可以访问的客户的条件
	public static DBObject getOwnerCondition(String userId) {
		BasicDBObject cond0 = new BasicDBObject().append(F_OWNER, userId);
		BasicDBObject cond1 = new BasicDBObject()
				.append(F_CUSTOMER_REP, userId);
		BasicDBObject cond2 = new BasicDBObject().append(F_SALES_MANAGER,
				userId);
		BasicDBObject cond3 = new BasicDBObject().append(F_SALES_SUP, userId);
		BasicDBObject cond4 = new BasicDBObject().append(F_SERVICE_MANAGER,
				userId);
		BasicDBObject cond5 = new BasicDBObject().append(F_ORIGINAL_OWNERLIST,
				userId);
		return new BasicDBObject().append("$or", new BasicDBObject[] { cond0,
				cond1, cond2, cond3, cond4, cond5 });
	}

	public static DBObject getVisitableCondition(String userId) {
		BasicDBObject cond0 = new BasicDBObject().append(F_OWNER, userId);
		BasicDBObject cond1 = new BasicDBObject()
				.append(F_CUSTOMER_REP, userId);
		BasicDBObject cond2 = new BasicDBObject().append(F_SALES_MANAGER,
				userId);
		BasicDBObject cond3 = new BasicDBObject().append(F_SALES_SUP, userId);
		BasicDBObject cond4 = new BasicDBObject().append(F_SERVICE_MANAGER,
				userId);
		BasicDBObject cond5 = new BasicDBObject().append(F_VISITORLIST, userId);
		BasicDBObject cond6 = new BasicDBObject().append(F_ORIGINAL_OWNERLIST,
				userId);
		return new BasicDBObject().append("$or", new BasicDBObject[] { cond0,
				cond1, cond2, cond3, cond4, cond5, cond6 });
	}

	public void addToOwnerList(String userId) {
		Object value = getValue(F_ORIGINAL_OWNERLIST);
		if (!(value instanceof List)) {
			value = new BasicDBList();
		}
		List listValue = (List) value;
		if (!listValue.contains(userId)) {
			listValue.add(userId);
			setValue(F_ORIGINAL_OWNERLIST, listValue);
		}
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		String owner = context.getAccountInfo().getUserId();
		if (getValue(F_CUSTOMER_REP) == null) {
			setValue(F_CUSTOMER_REP, owner);
		}
		if(getValue(F_OWNER)==null){
			setValue(F_OWNER, owner);
		}
		super.doInsert(context);
	}

	public void duplicateTeamTo(PrimaryObject to) {
		to.setValue(F_CUSTOMER_REP, getValue(F_CUSTOMER_REP));
		to.setValue(F_SALES_MANAGER, getValue(F_SALES_MANAGER));
		to.setValue(F_SALES_SUP, getValue(F_SALES_SUP));
		to.setValue(F_SERVICE_MANAGER, getValue(F_SERVICE_MANAGER));
	}

	public void checkAndDuplicateTeamFrom(TeamControl from) {
		if (
				getValue(F_CUSTOMER_REP) != null
				|| getValue(F_SALES_MANAGER) != null
				|| getValue(F_SALES_SUP) != null
				|| getValue(F_SERVICE_MANAGER) != null
		){
			return;
		}

		from.duplicateTeamTo(this);
	}

	protected boolean checkUser(String[] fields, String userId) {
		for (int i = 0; i < fields.length; i++) {
			Object value = getValue(fields[i]);
			if (userId.equals(value)) {
				return true;
			} else {
				if ((value instanceof List)
						&& (((List) value).contains(userId))) {
					return true;
				}
			}
		}
		return false;
	}

}
