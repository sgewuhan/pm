package com.sg.business.commons.ui.home.perf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.home.basic.ProjectContentBlock2;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.UserPerf;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.block.button.BusinessContentBlock;
import com.sg.widgets.block.button.ButtonBlock;

public class ImportantProjectBlock extends ButtonBlock {

	private static final String PERSPECTIVE = "perspective.project.charger";
	private DBObject condition;
	private DBCollection col;
	private DBObject basicQuery;

	public ImportantProjectBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void init() {
		super.init();
		condition = getCondition();
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected DBCollection getCollection() {
		return DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	protected Class<? extends PrimaryObject> getContentClass() {
		return Project.class;
	}

	private Set<ObjectId> getOrganizations(Organization org) {
		Set<ObjectId> result = new HashSet<ObjectId>();
		result.add(org.get_id());
		List<PrimaryObject> children = org.getChildrenOrganization();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				result.addAll(getOrganizations((Organization) children.get(i)));
			}
		}
		return result;
	}

	private DBObject getBasicQueryCondition() {
		if (basicQuery != null) {
			DBObject result = new BasicDBObject();
			result.putAll(basicQuery);
			return result;
		}
		BasicDBObject query = new BasicDBObject();
		// 获取当前用户作为那些组织的管理者
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> ras = user.getRoles(Role.ROLE_DEPT_MANAGER_ID);
		Set<ObjectId> orgIdSet = new HashSet<ObjectId>();
		for (PrimaryObject po : ras) {
			Role role = (Role) po;
			Organization org = role.getOrganization();
			orgIdSet.addAll(getOrganizations(org));
		}
		query.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", orgIdSet.toArray()));
		// 设置进行中和已完成
		query.put(
				ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
						ILifecycle.STATUS_FINIHED_VALUE,
								ILifecycle.STATUS_WIP_VALUE }));
		basicQuery = query;
		return basicQuery;
	}

	@Override
	protected DBObject[] getResult() {
		Map<String, DBObject[]> results = new HashMap<String, DBObject[]>();

		// 获取当前用户关注的项目
		// 严重超期 天以上 overtime,overtime_cnt,overtime_check
		Object checked = condition.get(IImportantSetting.OVERTIME_CHECKED);
		Object value = condition.get(IImportantSetting.OVERTIME);
		if (value != null && Boolean.TRUE.equals(checked)) {
			try {
				DBObject query = getBasicQueryCondition();
				int iValue = Integer.parseInt(value.toString());
				query.put(Project.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, iValue);
				query.put(Project.F_PLAN_FINISH,
						new BasicDBObject().append("$lt", cal.getTime()));
				DBCursor cursor = col.find(query);
				results.put(IImportantSetting.OVERTIME_CNT, cursor.toArray()
						.toArray(new DBObject[] {}));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 获取当前用户关注的项目
		// 严重超支 万元以上 ,overcost,overcost_cnt,overcost_check
		checked = condition.get(IImportantSetting.OVERCOST_CHECKED);
		value = condition.get(IImportantSetting.OVERCOST);
		if (value != null && Boolean.TRUE.equals(checked)) {
			List<DBObject> r = new ArrayList<DBObject>();
			try {
				double dValue = Double.parseDouble(value.toString());
				DBObject query = getBasicQueryCondition();
				query.put(Project.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE);
				query.put(ProjectETL.F_ETL + "."
						+ ProjectETL.F_IS_OVERCOST_DEFINITED, Boolean.TRUE);
				query.put(ProjectETL.F_ETL + "."
						+ ProjectETL.F_INVESTMENT_DESIGNATED,
						new BasicDBObject().append("$ne", 0));
				DBCursor cursor = col.find(query);
				while (cursor.hasNext()) {
					DBObject ci = cursor.next();
					Project project = ModelService.createModelObject(ci,
							Project.class);
					ProjectPresentation pres = project.getPresentation();
					double bv = pres.getBudgetValue();
					double di = pres.getDesignatedInvestment();
					if (di > (bv + dValue)) {
						r.add(ci);
					}
				}
				results.put(IImportantSetting.OVERCOST_CNT,
						r.toArray(new DBObject[] {}));
			} catch (Exception e) {
			}
		}

		// 严重亏损 万元以上 heavyloss,heavyloss_cnt,heavyloss_check
		checked = condition.get(IImportantSetting.HEAVYLOSS_CHECKED);
		value = condition.get(IImportantSetting.HEAVYLOSS);
		if (value != null && Boolean.TRUE.equals(checked)) {
			List<DBObject> r = new ArrayList<DBObject>();
			try {
				double dValue = Double.parseDouble(value.toString());
				DBObject query = getBasicQueryCondition();
				query.put(Project.F_LIFECYCLE, ILifecycle.STATUS_FINIHED_VALUE);
				query.put(ProjectETL.F_ETL + "." + ProjectETL.F_SALES_COST,
						new BasicDBObject().append("$ne", 0));
				DBCursor cursor = col.find(query);
				while (cursor.hasNext()) {
					DBObject ci = cursor.next();
					Project project = ModelService.createModelObject(ci,
							Project.class);
					ProjectPresentation pres = project.getPresentation();
					double cost = pres.getBudgetValue();
					double revenue = pres.getDesignatedInvestment();
					if (cost > (revenue + dValue)) {
						r.add(ci);
					}
				}
				results.put(IImportantSetting.HEAVYLOSS_CNT,
						r.toArray(new DBObject[] {}));
			} catch (Exception e) {
			}
		}

		// 高利润率% highprofitrate,highprofitrate_cnt,highprofitrate_check
		checked = condition.get(IImportantSetting.HIGHPROFITRATE_CHECKED);
		value = condition.get(IImportantSetting.HIGHPROFITRATE);
		if (value != null && Boolean.TRUE.equals(checked)) {
			List<DBObject> r = new ArrayList<DBObject>();
			try {
				double dValue = Double.parseDouble(value.toString());
				DBObject query = getBasicQueryCondition();
				query.put(Project.F_LIFECYCLE, ILifecycle.STATUS_FINIHED_VALUE);
				query.put(ProjectETL.F_ETL + "." + ProjectETL.F_SALES_REVENUE,
						new BasicDBObject().append("$ne", 0));
				DBCursor cursor = col.find(query);
				while (cursor.hasNext()) {
					DBObject ci = cursor.next();
					Project project = ModelService.createModelObject(ci,
							Project.class);
					ProjectPresentation pres = project.getPresentation();
					double cost = pres.getBudgetValue();
					double revenue = pres.getDesignatedInvestment();
					if (revenue / (revenue + cost) > (dValue / 100)) {
						r.add(ci);
					}
				}
				results.put(IImportantSetting.HIGHPROFITRATE_CNT,
						r.toArray(new DBObject[] {}));
			} catch (Exception e) {
			}
		}

		// 研发预算大 万元以上 highbudget,highbudget_cnt,highbudget_check
		checked = condition.get(IImportantSetting.HIGHBUDGET_CHECKED);
		value = condition.get(IImportantSetting.HIGHBUDGET);
		if (value != null && Boolean.TRUE.equals(checked)) {
			try {
				DBObject query = getBasicQueryCondition();
				double dValue = Double.parseDouble(value.toString());
				query.put(ProjectETL.F_ETL + "." + ProjectETL.F_BUDGET,
						new BasicDBObject().append("$gt", dValue * 10000));
				DBCursor cursor = col.find(query);
				results.put(IImportantSetting.HIGHBUDGET_CNT, cursor.toArray()
						.toArray(new DBObject[] {}));
			} catch (Exception e) {
			}

		}
		// 研发投入大 万元以上 highinvestment,highinvestment_cnt,highinvestment_check
		checked = condition.get(IImportantSetting.HIGHINVESTMENT_CHECKED);
		value = condition.get(IImportantSetting.HIGHINVESTMENT);
		if (value != null && Boolean.TRUE.equals(checked)) {
			try {
				double dValue = Double.parseDouble(value.toString());
				DBObject query = getBasicQueryCondition();
				query.put(ProjectETL.F_ETL + "."
						+ ProjectETL.F_INVESTMENT_DESIGNATED,
						new BasicDBObject().append("$gt", dValue * 10000));
				DBCursor cursor = col.find(query);
				results.put(IImportantSetting.HIGHINVESTMENT_CNT, cursor
						.toArray().toArray(new DBObject[] {}));
			} catch (Exception e) {
			}

		}
		// 研发周期长 月以上longtermdev,longtermdev_cnt,longtermdev_check
		checked = condition.get(IImportantSetting.LONGTERMDEV_CHECKED);
		value = condition.get(IImportantSetting.LONGTERMDEV);
		if (value != null && Boolean.TRUE.equals(checked)) {
			List<DBObject> r = new ArrayList<DBObject>();
			try {
				int iValue = Integer.parseInt(value.toString());
				DBObject query = getBasicQueryCondition();
				query.put(Project.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE);
				DBCursor cursor = col.find(query);
				while (cursor.hasNext()) {
					DBObject ci = cursor.next();
					Project project = ModelService.createModelObject(ci,
							Project.class);
					Date as = project.getActualStart();
					if (as == null) {
						continue;
					}
					Date af = project.getActualFinish();
					if (af == null) {
						af = new Date();
					}
					if ((af.getTime() - as.getTime()) > (iValue * 30 * 24 * 60
							* 60 * 1000)) {
						r.add(ci);
					}
				}
				results.put(IImportantSetting.LONGTERMDEV_CNT,
						r.toArray(new DBObject[] {}));
			} catch (Exception e) {
			}

		}

		Iterator<String> iter = results.keySet().iterator();
		List<DBObject> resultList = new ArrayList<DBObject>();
		while (iter.hasNext()) {
			String key = iter.next();
			Object _cnt = condition.get(key);
			int cnt = 10;
			try {
				cnt = Integer.parseInt(_cnt.toString());
			} catch (Exception e) {
				continue;
			}

			DBObject[] dates = results.get(key);
			addTo(resultList, dates, cnt,key);
		}

		return resultList.toArray(new DBObject[] {});
	}

	private void addTo(List<DBObject> resultList, DBObject[] dates, int cnt, String key) {
		List<DBObject> toBeAdd = new ArrayList<DBObject>();
		for (int i = 0; i < dates.length && toBeAdd.size() <= cnt; i++) {
			boolean has = false;
			for (int j = 0; j < resultList.size(); j++) {
				DBObject resultProject = resultList.get(j);
				if (Util.equals(dates[i].get("_id"),
						resultProject.get("_id"))) {
					Object tags = resultProject.get(Project._TAG);
					if(!(tags instanceof BasicBSONList)){
						tags  = new BasicBSONList();
					}
					((BasicBSONList)tags).add(key);
					resultProject.put(Project._TAG, tags);
					has = true;
					break;
				}
			}
			if (!has) {
				BasicBSONList tags = new BasicBSONList();
				((BasicBSONList)tags).add(key);
				dates[i].put(Project._TAG, tags);
				toBeAdd.add(dates[i]);
			}
		}
		resultList.addAll(toBeAdd);
	}


	@Override
	protected BusinessContentBlock createBlockContent(Composite contentArea,
			PrimaryObject po) {
		return new ProjectContentBlock2(contentArea);
	}

	@Override
	protected void doAdd() {
		final Shell shell = new Shell(getShell(), SWT.ON_TOP);
		shell.setSize(getUnitCountX() * (BLOCKSIZE + 1) - 1, getUnitCountY()
				* (BLOCKSIZE + 1) - 1 + TOPICSIZE);
		shell.setLocation(getParent().toDisplay(1, 1));
		shell.setLayout(new FillLayout());
		final ImportantSettingBlock isb = new ImportantSettingBlock(shell) {
			@Override
			protected void go() {
				shell.dispose();
				super.go();
			}

			@Override
			protected void doSaveCondition(DBObject data) {
				saveCondition(data);
			}
		};
		isb.setCondition(condition);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				shell.dispose();
			}
		});
		shell.open();
	}

	protected void saveCondition(DBObject data) {
		DBCollection conditionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_USER_PREFORMENCE);
		conditionCol.update(new BasicDBObject().append(UserPerf.F_USERID,
				userId), new BasicDBObject().append("$set", new BasicDBObject()
				.append(UserPerf.F_PROJECT_ATTENTION_CONDITION, data)), true,
				false);
		this.condition = data;
		doRefresh();
	}

	private DBObject getCondition() {
		DBCollection conditionCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_USER_PREFORMENCE);
		DBObject userPerfData = conditionCol.findOne(new BasicDBObject()
				.append(UserPerf.F_USERID, userId));
		if (userPerfData != null) {
			Object attData = userPerfData
					.get(UserPerf.F_PROJECT_ATTENTION_CONDITION);
			if (attData instanceof DBObject) {
				return (DBObject) attData;
			}
		}
		return new BasicDBObject();
	}

	@Override
	public int getUnitCountX() {
		return 6;
	}

	@Override
	protected int getLimit() {
		return 7;
	}

	@Override
	protected void fill(DBObject dt[]) {
		int blankCount = getUnitCountX() * getUnitCountY() - 1;
		switch (dt.length) {
		case 11:
			blankCount -= insert(dt, 0, 11, BLOCK_TYPE[0]);
			break;
		case 10:
			blankCount -= insert(dt, 0, 1, BLOCK_TYPE[1]);
			blankCount -= insert(dt, 1, 10, BLOCK_TYPE[0]);
			break;
		case 9:
			blankCount -= insert(dt, 0, 2, BLOCK_TYPE[1]);
			blankCount -= insert(dt, 2, 9, BLOCK_TYPE[0]);
			break;
		case 8:
			blankCount -= insert(dt, 0, 1, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 1, 8, BLOCK_TYPE[0]);
			break;
		case 7:
			blankCount -= insert(dt, 0, 1, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 1, 2, BLOCK_TYPE[1]);
			blankCount -= insert(dt, 2, 7, BLOCK_TYPE[0]);
			break;
		case 6:
			blankCount -= insert(dt, 0, 1, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 1, 3, BLOCK_TYPE[1]);
			blankCount -= insert(dt, 3, 6, BLOCK_TYPE[0]);
			break;
		case 5:
			blankCount -= insert(dt, 0, 2, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 2, 5, BLOCK_TYPE[0]);
			break;
		case 4:
			blankCount -= insert(dt, 0, 2, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 2, 3, BLOCK_TYPE[1]);
			blankCount -= insert(dt, 3, 4, BLOCK_TYPE[0]);
			break;
		case 3:
			blankCount -= insert(dt, 0, 2, BLOCK_TYPE[2]);
			blankCount -= insert(dt, 2, 3, BLOCK_TYPE[1]);
			break;
		case 2:
		case 1:
			blankCount -= insert(dt, 0, dt.length, BLOCK_TYPE[2]);
			break;
		default:
		}
		fillBlank(blankCount);
	}

	private int insert(DBObject[] dt, int start, int end, Point size) {
		int result = 0;
		for (int i = start; i < end; i++) {
			addContentBlock(dt[i], size);
			result += size.x * size.y;
		}
		return result;
	}

	@Override
	protected DBObject getSearchCondition() {
		return null;
	}

}
