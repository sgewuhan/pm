package com.sg.business.commons.ui.home.perf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.design.ICSSConstants;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.block.tab.TabBlockPage;

public abstract class AbstractListBoardPage extends TabBlockPage {

	public static final int BLOCKWIDTH = 300;
	private ManagementListBoard viewer;
	private int year;
	private int month;
	private List<ObjectId> organizationIdList;
	private static final int limitNumber = 10;

	public AbstractListBoardPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());

		viewer = createListViewer(parent);

		createPageControl(parent);

	}

	private void createPageControl(Composite parent) {
		Button pageRight = new Button(parent, SWT.NONE);
		pageRight.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_RIGHT_24);

		FormData fd = new FormData();
		pageRight.setLayoutData(fd);
		fd.top = new FormAttachment(0, 4);
		fd.right = new FormAttachment(100, -12);
		fd.width = 24;
		fd.height = 24;
		pageRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageRight();
			}
		});

		Button pageLeft = new Button(parent, SWT.NONE);
		pageLeft.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_LEFT_24);

		fd = new FormData();
		pageLeft.setLayoutData(fd);
		fd.top = new FormAttachment(0, 4);
		fd.right = new FormAttachment(pageRight,-4);
		fd.width = 24;
		fd.height = 24;
		pageLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageLeft();
			}
		});
		pageRight.moveAbove(null);
		pageLeft.moveAbove(null);
	}

	protected void pageLeft() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.add(Calendar.MONTH, -1);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		doRefresh();
	}

	protected void pageRight() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.add(Calendar.MONTH, 1);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		doRefresh();
	}

	private ManagementListBoard createListViewer(Composite parent) {
		ManagementListBoard tabItem = new ManagementListBoard(parent) {

			@Override
			protected String getLabel(Object _id) {
				return getListItemLabel(_id);
			}
		};
		tabItem.setTitle(getTitle());
		return tabItem;
	}

	private void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		year = cal.get(Calendar.YEAR);
		// year = 2013;
		month = cal.get(Calendar.MONTH) + 1;
		String consignerId = context.getConsignerId();
		User user = UserToolkit.getUserById(consignerId);
		organizationIdList = user.getManagementOrganizationIdList();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		List<Object[]> input = new ArrayList<Object[]>();
		Object[] topUserList = getTopListByCharger(-1);
		topUserList = removeList(topUserList);
		input.add(topUserList);
		Object[] lastUserList = getTopListByCharger(1);
		lastUserList = removeAndSortList(lastUserList);
		input.add(lastUserList);
		return input;
	}

	protected Object[] removeAndSortList(Object[] removeAndSortList) {
		List<Object> asList = Arrays.asList(removeAndSortList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				Object[] children = (Object[]) object;
				List<Object> asChildrenList = Arrays.asList(children);
				List<Object> tempChildrenList = new ArrayList<Object>();
				tempChildrenList.addAll(asChildrenList);
				tempChildrenList.add(Boolean.FALSE);
				tempList.add(tempChildrenList.toArray(new Object[0]));
			}
		}
		Comparator<? super Object> sorter = new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				if (arg0 == null) {
					return -1;
				}
				if (arg1 == null) {
					return -1;
				}
				Object[] object0 = (Object[]) arg0;
				Object[] object1 = (Object[]) arg1;
				int d0 = (int) object0[2];
				int d1 = (int) object1[2];
				if (d0 > d1) {
					return -1;
				} else if (d0 < d1) {
					return 1;
				} else {
					return 0;
				}
			}

		};
		Collections.sort(tempList, sorter);
		return tempList.toArray(new Object[0]);
	}

	protected Object[] removeList(Object[] removeList) {
		List<Object> asList = Arrays.asList(removeList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				Object[] children = (Object[]) object;
				List<Object> asChildrenList = Arrays.asList(children);
				List<Object> tempChildrenList = new ArrayList<Object>();
				tempChildrenList.addAll(asChildrenList);
				tempChildrenList.add(Boolean.TRUE);
				tempList.add(tempChildrenList.toArray(new Object[0]));
			}
		}
		return tempList.toArray(new Object[0]);
	}

	private Object[] getTopListByCharger(int sortType) {
		String groupField = getGroupField();
		String groupByField = getGroupByField();
		Object[] result = new Object[limitNumber];
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject()
						.append(ProjectETL.F_YEAR, year)
						.append(ProjectETL.F_MONTH, month)
						.append(Project.F_LAUNCH_ORGANIZATION,
								new BasicDBObject().append("$in",
										organizationIdList)));

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

		AggregationOutput aggregationOutput;
		if (hasUnWindField()) {
			aggregationOutput = col.aggregate(
					query,
					new BasicDBObject().append("$unwind", "$"
							+ getUnWindField()), group, sort, limit);
		} else {
			aggregationOutput = col.aggregate(query, group, sort, limit);
		}
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		for (int i = 0; i < result.length; i++) {
			if (iterator.hasNext()) {
				DBObject dbObject = (DBObject) iterator.next();
				Object _id = dbObject.get("_id");
				Double month_sales_profit = (Double) dbObject.get(groupField);
				result[i] = new Object[] { _id, month_sales_profit, i + 1 };
			}
		}
		return result;
	}

	protected abstract Object getUnWindField();

	protected abstract boolean hasUnWindField();

	protected abstract String getGroupByField();

	protected abstract String getGroupField();

	protected abstract String getTitle();

	protected abstract String getListItemLabel(Object _id);

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		if (data instanceof List) {
			viewer.setInput((List<Object[]>) data);
			viewer.setYear(year);
			viewer.setMonth(month);
			viewer.updateLabel(locale);
		}
	}

	public int getContentHeight() {
		return 241;
	}
}
