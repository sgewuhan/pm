package com.sg.business.model;

import java.util.Date;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class ActivateSwitch implements IActivateSwitch {

	private PrimaryObject po;

	public ActivateSwitch(PrimaryObject po) {
		this.po = po;
	}

	@Override
	public void doSwitchActivation(IContext context) throws Exception {
		DBCollection col = po.getCollection();
		Boolean activated = !isActivated();
		setActivated(activated);
		col.update(
				new BasicDBObject().append(PrimaryObject.F__ID, po.get_id()),
				new BasicDBObject().append("$set", new BasicDBObject().append(F_ACTIVATED, activated)));

		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "…Ë÷√º§ªÓ◊¥Ã¨",
				new Date(), po.getLabel(), IModelConstants.DB);
	}

	@Override
	public void setActivated(Boolean activated) {
		po.setValue(F_ACTIVATED, activated);
	}

	@Override
	public boolean isActivated() {
		return Boolean.TRUE.equals(po.getValue(IActivateSwitch.F_ACTIVATED));
	}

}
