package com.sg.business.visualization.dataset;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;

public abstract class ProjectSetFolder extends PrimaryObject {

	protected User user;

	public void setUser(User user) {
		this.user = user;
	}

	public abstract Object[] getChildren();

	public abstract boolean hasChildren();

	@Override
	final public void doInsert(IContext context) throws Exception {
	}

	@Override
	final public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	final public void doUpdate(IContext context) throws Exception {
	}

	public abstract String getImageURL();

	public abstract String getDescription();
}
