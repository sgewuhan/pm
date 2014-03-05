package com.sg.business.pm2.home.page;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
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
import com.sg.business.pm2.home.widget.ListBoardCTabItem;
import com.sg.widgets.block.TabBlockPage;

public class ListBoardPage extends TabBlockPage {

	public static final int BLOCKWIDTH = 300;
	private ListBoardCTabItem viewer;
	private int year;
	private int month;
	private Organization org;
	private static final int limitNumber = 6;

	public ListBoardPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());

		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setData(RWT.CUSTOM_VARIANT, "toplist"); //$NON-NLS-1$
		tabFolder.setTabHeight(0);
		FormData fd = new FormData();
		tabFolder.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		viewer = createListViewer(tabFolder);

		tabFolder.setSelection(viewer);
	}

	private ListBoardCTabItem createListViewer(CTabFolder tabFolder) {
		ListBoardCTabItem tabItem = new ListBoardCTabItem(tabFolder);
		return tabItem;
	}

	private void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		String consignerId = context.getConsignerId();
		User user = UserToolkit.getUserById(consignerId);
		org = user.getFunctionOrganization();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		List<Object[]> input = new ArrayList<Object[]>();
		Object[] topUserList = getTopList(-1, ProjectETL.F_MONTH_SALES_PROFIT,
				Project.F_CHARGER);
		input.add(topUserList);
		Object[] topOrgList = getTopList(-1, ProjectETL.F_MONTH_SALES_PROFIT,
				ProjectETL.F_PROJECTID);
		input.add(topOrgList);
		return input;
	}

	private Object[] getTopList(int sortType, String groupField,
			String groupByField) {
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
		sort.put("$sort", new BasicDBObject().append(groupField, sortType));

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
		}
	}

	public int getContentHeight() {
		return 241;
	}
}
