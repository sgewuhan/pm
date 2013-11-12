package com.sg.business.model.dataset.finance;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class FinanceOrganizationDataSet extends
		SingleDBCollectionDataSetFactory {

	public FinanceOrganizationDataSet() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	public DataSet getDataSet() {
		// 查询当前用户在哪些组织（事业部或者法人）担任了财务经理
		String userid = getContext().getAccountInfo().getConsignerId();

		User user = UserToolkit.getUserById(userid);
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_FINANCIAL_MANAGER_ID);

		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);

			String type = org.getOrganizationType();
			if (!Organization.ORG_TYPE_COMPANY.equals(type)
					&& !Organization.ORG_TYPE_BUSINESS_UNIT.equals(type)) {
				continue;
			}

			input.addAll(getCostCenter(org));
		}

		return new DataSet(input);
	}

	private List<PrimaryObject> getCostCenter(Organization org) {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (!Utils.isNullOrEmpty(org.getCostCenterCode())) {
			result.add(org);
		}

		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			result.addAll(getCostCenter((Organization) children.get(i)));
		}

		return result;
	}

	@Override
	public DBObject getQueryCondition() {

		return super.getQueryCondition();
	}

}
