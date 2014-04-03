package com.tmt.gs.dataset;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class ConfirmProgramOfGS extends MasterDetailDataSetFactory {

	public IContext context;
	private User user;

	public ConfirmProgramOfGS() {
		super(IModelConstants.DB, IModelConstants.C_USER);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	public DataSet getDataSet() {
		if (master != null) {
			// 1.获取当前登录用户承担的角色
			List<PrimaryObject> orglist = user.getRoleGrantedInAllOrganization(IRoleConstance.ROLE_TECHNOLOGY_CHECKER_ID);
			// 2.获取角色所在组织
			return new DataSet(orglist);
		}
		return super.getDataSet();
	}

}
