package com.sg.business.commons.ui.block;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.block.tab.TabBlockPage;

public class ListBoardPage extends TabBlockPage {

	public static final int BLOCKWIDTH = 300;
	private ListBoard viewer;
	private int year;
	private int month;
	private Organization org;
	private ObjectId user_id;
	private static final int limitNumber = 10;

	public ListBoardPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());

		viewer = createListViewer(parent);

	}

	private ListBoard createListViewer(Composite parent) {
		ListBoard tabItem = new ListBoard(parent);
		return tabItem;
	}

	private void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		 year = cal.get(Calendar.YEAR);
//		year = 2013;
		month = cal.get(Calendar.MONTH) + 1;
		String consignerId = context.getConsignerId();
		User user = UserToolkit.getUserById(consignerId);
		org = user.getFunctionOrganization();
		user_id = user.get_id();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		List<Object[]> input = new ArrayList<Object[]>();
		List<Object[]> topUserList = getTopListByCharger();
		input.addAll(topUserList);
		Object[] topOrgList = getTopListByPriject();
		input.add(topOrgList);
		return input;
	}

	private List<Object[]> getTopListByCharger() {
		List<Object[]> result = new ArrayList<Object[]>();
		String groupField = ProjectETL.F_MONTH_SALES_PROFIT;
		String groupByField = Project.F_CHARGER;
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year)
						.append(ProjectETL.F_MONTH, month)
						.append(Project.F_FUNCTION_ORGANIZATION, org.get_id()));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + groupByField).append(
						groupField,
						new BasicDBObject().append("$sum", "$" + groupField)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append(groupField, -1));

		BasicDBObject limit = new BasicDBObject();

		limit.put("$limit", limitNumber);

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort,
				limit);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		Object[] userNumbers = new Object[1];
		Object[] results = new Object[limitNumber];
		for (int i = 0; i < results.length; i++) {
			if (iterator.hasNext()) {
				DBObject dbObject = (DBObject) iterator.next();
				Object _id = dbObject.get("_id");
				Double month_sales_profit = (Double) dbObject.get(groupField);
				results[i] = new Object[] { _id, month_sales_profit, i + 1 };
				if (user_id.equals(_id)) {
					userNumbers[0] = i;
				}
			} else {
				results[i] = new Object[] { null, null, i + 1 };
			}
		}
		int userNumber = limitNumber + 1;
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			Object _id = dbObject.get("_id");
			if (user_id.equals(_id)) {
				userNumbers[0] = userNumber;
				break;
			} else {
				userNumber++;
			}
		}

		result.add(userNumbers);
		result.add(results);
		return result;
	}

	private Object[] getTopListByPriject() {
		String groupField = ProjectETL.F_MONTH_SALES_PROFIT;
		String groupByField = ProjectETL.F_PROJECTID;
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year)
						.append(ProjectETL.F_MONTH, month)
						.append(Project.F_FUNCTION_ORGANIZATION, org.get_id()));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + groupByField).append(
						groupField,
						new BasicDBObject().append("$sum", "$" + groupField)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append(groupField, -1));

		BasicDBObject limit = new BasicDBObject();

		limit.put("$limit", limitNumber);

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort,
				limit);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		Object[] results = new Object[limitNumber];
		for (int i = 0; i < results.length; i++) {
			if (iterator.hasNext()) {
				DBObject dbObject = (DBObject) iterator.next();
				Object _id = dbObject.get("_id");
				Double month_sales_profit = (Double) dbObject.get(groupField);
				results[i] = new Object[] { _id, month_sales_profit, i + 1 };
			} else {
				results[i] = new Object[] { null, null, i + 1 };
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		if (data instanceof List) {
			viewer.setInput((List<Object[]>) data);
//			viewer.setYear(year);
			viewer.setMonth(month);
			viewer.setOrganization(org);
			viewer.updateLabel(locale);
		}
	}

	public int getContentHeight() {
		return 241;
	}
}
