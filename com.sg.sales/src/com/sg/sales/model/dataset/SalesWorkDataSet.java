package com.sg.sales.model.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class SalesWorkDataSet extends SingleDBCollectionDataSetFactory {

	IContext context;

	public SalesWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		context = new CurrentAccountContext();
		List<String> userlists = new ArrayList<String>();
		AccountInfo accountinfo=context.getAccountInfo();
		if(accountinfo!=null){
			String userId = accountinfo.getConsignerId();
			User user = UserToolkit.getUserById(userId);
			// 获取当前用户所具有的管理者角色
			userlists.add(userId);
			List<PrimaryObject> roles = user.getRoles(Role.ROLE_DEPT_MANAGER_ID);
			for (PrimaryObject po : roles) {
				Role role = (Role) po;
				// 获取管理者角色所在的组织
				Organization organization = role.getOrganization();
				// 获取组织下的所有用户
				List<String> userlist = organization.getMemberIds(true);
				if(userlist != null){
					userlists.addAll(userlist);
				}
				// 判断工作负责人是管理者角色所在组织下的用户以及是独立工作

			}
		}
		setQueryCondition(new BasicDBObject().append(Work.F_CHARGER,
				new BasicDBObject().append("$in", userlists))
				.append(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE));
	}
}
