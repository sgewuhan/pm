package com.tmt.gs.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class GSConfirmProgram extends MasterDetailDataSetFactory {

	public IContext context;
	private User user;

	public GSConfirmProgram(String dbName, String collectionName) {
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
			List<PrimaryObject> result = new ArrayList<PrimaryObject>();
			// 1.��ȡ��ǰ��¼�û��е��Ľ�ɫ
			List<PrimaryObject> roles = user
					.getRoles(IRoleConstance.ROLE_TECHNOLOGY_CHECKER_ID);
			// 2.��ȡ��ɫ������֯
			for (PrimaryObject po : roles) {
				if (po instanceof Role) {
					Role role = (Role) po;
					Organization org = role.getOrganization();
					result.add(org);
				}
			}
			return new DataSet(result);
		}
		return super.getDataSet();
	}

}
