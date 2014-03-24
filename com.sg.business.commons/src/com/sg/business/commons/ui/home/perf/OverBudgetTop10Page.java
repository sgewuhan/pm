package com.sg.business.commons.ui.home.perf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.block.tab.PageControledListTabblockPage;

public class OverBudgetTop10Page extends PageControledListTabblockPage {

	private DBCollection col;
	private DBObject basicQuery;
	private User user;

	public OverBudgetTop10Page(Composite parent) {
		super(parent);
	}

	@Override
	protected void init() {
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		user = UserToolkit.getUserById(context.getUserId());
		super.init();
	}

	@Override
	protected int getPageSize() {
		return 10;
	}

	private DBObject getBasicQueryCondition() {
		if (basicQuery != null) {
			DBObject result = new BasicDBObject();
			result.putAll(basicQuery);
			return result;
		}
		BasicDBObject query = new BasicDBObject();
		// 获取当前用户作为那些组织的管理者
		List<PrimaryObject> ras = user.getRoles(Role.ROLE_DEPT_MANAGER_ID);
		Set<ObjectId> orgIdSet = new HashSet<ObjectId>();
		for (PrimaryObject po : ras) {
			Role role = (Role) po;
			Organization org = role.getOrganization();
			orgIdSet.addAll(org.getOrganizationStructureOfId());
		}
		query.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", orgIdSet.toArray()));
		// 设置进行
		query.put(ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE);
		query.put(ProjectETL.F_ETL + "." + ProjectETL.F_IS_OVERCOST_DEFINITED,
				Boolean.TRUE);
		basicQuery = query;
		return basicQuery;
	}

	@Override
	protected void doDisplayData(Object data) {
		setLabelKey("performence.budget");
		getViewer().getControl().setData(RWT.CUSTOM_ITEM_HEIGHT,
				new Integer(60));
		super.doDisplayData(data);
	}

	@Override
	protected List<PrimaryObject> doGetData() {
		ArrayList<PrimaryObject> result = new ArrayList<PrimaryObject>();
		// 查询所有超支的进行中的项目，按超支大小排序
		DBCursor cur = col.find(getBasicQueryCondition());
		while (cur.hasNext()) {
			DBObject dataItem = cur.next();
			Project project = ModelService.createModelObject(dataItem,
					Project.class);
			ProjectPresentation pres = project.getPresentation();
			if (pres == null) {
				continue;
			}
			result.add(project);
		}

		Comparator<? super PrimaryObject> comp = new Comparator<PrimaryObject>() {

			@Override
			public int compare(PrimaryObject arg0, PrimaryObject arg1) {
				ProjectPresentation pj0 = ((Project) arg0).getPresentation();
				ProjectPresentation pj1 = ((Project) arg1).getPresentation();

				double value0 = pj0.getDesignatedInvestment()
						- pj0.getBudgetValue();
				double value1 = pj1.getDesignatedInvestment()
						- pj1.getBudgetValue();

				return -1 * new Double(value0).compareTo(new Double(value1));
			}
		};
		Collections.sort(result, comp);

		return result;
	}

	@Override
	protected void select(PrimaryObject po) {
		UIFrameworkUtils.navigateTo(po, UIFrameworkUtils.NAVIGATE_AUTOSELECT,
				false);
	}

}
