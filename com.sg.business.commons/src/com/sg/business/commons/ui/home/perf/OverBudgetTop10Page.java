package com.sg.business.commons.ui.home.perf;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.design.ICSSConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectMonthData;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.etl.IProjectETL;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.tab.PageControledListTabblockPage;

public class OverBudgetTop10Page extends PageControledListTabblockPage {

	private DBCollection col;
	private DBObject basicQuery;
	private User user;
	private Label indcateLabel;
	private Label monthLabel;
	private Calendar now;
	private Comparator<PrimaryObject> comparator;
	private Comparator<PrimaryObject> comparator2;
	private NumberFormat format;
	private DBCollection colETL;
	private long[] monthRates;
	private long[] previousMonthRates;
	private Composite head;
	private Label arrow;

	public OverBudgetTop10Page(Composite parent) {
		super(parent);
	}

	@Override
	protected void init() {
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		colETL = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
		user = UserToolkit.getUserById(context.getUserId());
		now = Calendar.getInstance();
		comparator = new Comparator<PrimaryObject>() {
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
		comparator2 = new Comparator<PrimaryObject>() {
			@Override
			public int compare(PrimaryObject arg0, PrimaryObject arg1) {
				ProjectMonthData pj0 = ((ProjectMonthData) arg0);
				ProjectMonthData pj1 = ((ProjectMonthData) arg1);

				double value0 = pj0
						.getDoubleValue(IProjectETL.F_INVESTMENT_DESIGNATED)
						- pj0.getDoubleValue(IProjectETL.F_BUDGET);
				double value1 = pj1
						.getDoubleValue(IProjectETL.F_INVESTMENT_DESIGNATED)
						- pj1.getDoubleValue(IProjectETL.F_BUDGET);

				return -1 * new Double(value0).compareTo(new Double(value1));
			}
		};
		format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		super.init();
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());
		head = createHead(parent);
		FormData fd = new FormData();
		head.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 68;

		Control control = createListViewer(parent);
		fd = new FormData();
		control.setLayoutData(fd);
		fd.top = new FormAttachment(head);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100, -30);

		Control bar = createPageBar(parent);
		fd = new FormData();
		bar.setLayoutData(fd);
		fd.top = new FormAttachment(control, 2);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100, -2);

	}

	private Composite createHead(Composite parent) {
		Composite head = new Composite(parent, SWT.NONE);
		monthLabel = new Label(head, SWT.NONE);
		HtmlUtil.enableMarkup(monthLabel);
		indcateLabel = new Label(head, SWT.NONE);
		HtmlUtil.enableMarkup(indcateLabel);
		arrow = new Label(head, SWT.NONE);
		Control control = createMonthSwitchLabel(head);
		Label sep = new Label(head, SWT.NONE);
		sep.setBackground(Widgets.getColor(getDisplay(), 0xed, 0xed, 0xed));

		head.setLayout(new FormLayout());

		FormData fd = new FormData();
		indcateLabel.setLayoutData(fd);
		fd.right = new FormAttachment(100,-2);
		fd.top = new FormAttachment();
		fd.width = 160;
		fd.bottom = new FormAttachment(100, -1);

		fd = new FormData();
		arrow.setLayoutData(fd);
		fd.right = new FormAttachment(100,-2);
		fd.width = 30;
		fd.top = new FormAttachment(0, 24);
		
		fd = new FormData();
		monthLabel.setLayoutData(fd);
		fd.left = new FormAttachment(0, 2);
		fd.top = new FormAttachment(0, 2);
		fd.right = new FormAttachment(indcateLabel);
		fd.height = 40;

		fd = new FormData();
		control.setLayoutData(fd);
		fd.left = new FormAttachment(0, 6);
		fd.top = new FormAttachment(monthLabel);
		fd.right = new FormAttachment(indcateLabel);
		fd.height = 24;

		fd = new FormData();
		sep.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);
		fd.height = 1;
		return head;
	}

	private Control createMonthSwitchLabel(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginLeft = 0;
		layout.marginRight = 10;
		layout.spacing = 4;
		layout.marginTop = 4;
		layout.marginBottom = 0;

		bar.setLayout(layout);

		final Button pageBack = new Button(bar, SWT.PUSH);
		pageBack.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_LEFT_16);
		pageBack.setLayoutData(new RowData(16, 16));

		final Button pageNext = new Button(bar, SWT.PUSH);
		pageNext.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_RIGHT_16);
		pageNext.setLayoutData(new RowData(16, 16));

		pageBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				now.add(Calendar.MONTH, -1);
				doRefresh();
			}
		});
		pageNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				now.add(Calendar.MONTH, 1);
				doRefresh();
			}
		});

		return bar;
	}

	@Override
	protected Control createListViewer(Composite parent) {
		Control list = super.createListViewer(parent);
		setLabelKey("performence.budget");
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(50));
		return list;
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
		basicQuery = new BasicDBObject();
		// 获取当前用户作为那些组织的管理者
		List<PrimaryObject> ras = user.getRoles(Role.ROLE_DEPT_MANAGER_ID);
		Set<ObjectId> orgIdSet = new HashSet<ObjectId>();
		for (PrimaryObject po : ras) {
			Role role = (Role) po;
			Organization org = role.getOrganization();
			orgIdSet.addAll(org.getOrganizationStructureOfId());
		}
		basicQuery.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", orgIdSet.toArray()));
		// 设置进行
		basicQuery.put(ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE);
		DBObject result = new BasicDBObject();
		result.putAll(basicQuery);
		return result;
	}

	@Override
	protected void doDisplayData(Object data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div align='center' style='"//$NON-NLS-1$
				+ "float:left;" + "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:22pt;"//$NON-NLS-1$
				+ "margin:4 0 0 4;"//$NON-NLS-1$
				+ "color:#cdcdcd;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		int year = now.get(Calendar.YEAR);
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		if (year == thisYear) {
			sb.append(now.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));
		} else {
			sb.append(String.format("%1$tY/%1$tm", now));
		}
		sb.append("</div>");
		monthLabel.setText(sb.toString());

		sb = new StringBuffer();
		sb.append("<div  style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:9pt;"//$NON-NLS-1$
				+ "margin:4 0 0 0;"//$NON-NLS-1$
				+ "color:#a6a6a6;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append("超支项目数量/总数");
		sb.append("</div>");
		sb.append("<div style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:28pt;"//$NON-NLS-1$
				+ "margin:0 0 0 0;"//$NON-NLS-1$
				+ "color:#d60000;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		if (monthRates[0] != 0) {
			double rateThisMonth = 100d * monthRates[1] / monthRates[0];
			sb.append(format.format(rateThisMonth));
			sb.append("%");
		}
		sb.append("</div>");

		indcateLabel.setText(sb.toString());
		if (monthRates[0] != 0 && previousMonthRates != null
				&& previousMonthRates[0] != 0) {
			sb = new StringBuffer();
			double rateThisMonth = 100d * monthRates[1] / monthRates[0];
			double rateLastMonth = 100d * previousMonthRates[1]
					/ previousMonthRates[0];
			Image image;
			if (rateThisMonth > rateLastMonth) {
				// 上升
				image = BusinessResource.getImage(BusinessResource.IMAGE_UP_48);
			} else {
				image = BusinessResource.getImage(BusinessResource.IMAGE_DOWN_48);

			}
			arrow.setImage(image);
		}
		
		head.layout();
		super.doDisplayData(data);
	}

	@Override
	protected List<PrimaryObject> doGetData() {
		ArrayList<PrimaryObject> result = new ArrayList<PrimaryObject>();
		if (now.get(Calendar.MONTH) == Calendar.getInstance().get(
				Calendar.MONTH)) {// 当月
			monthRates = extractDataFromProject(result);
		} else {
			monthRates = extractDataFromMonth(result, now);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
		cal.add(Calendar.MONTH, -1);
		previousMonthRates = extractDataFromMonth(null, cal);
		return result;
	}

	private long[] extractDataFromMonth(ArrayList<PrimaryObject> result,
			Calendar cal) {
		long[] rateData = new long[2];
		// 查询所有超支的进行中的项目，按超支大小排序
		DBObject query = getBasicQueryCondition();
		query.put(ProjectETL.F_YEAR, cal.get(Calendar.YEAR));
		query.put(ProjectETL.F_MONTH, cal.get(Calendar.MONTH) + 1);

		rateData[0] = colETL.count(query);
		query.put(ProjectETL.F_IS_OVERCOST_DEFINITED, Boolean.TRUE);

		if (result != null) {
			DBCursor cur = colETL.find(query);
			rateData[1] = cur.size();
			while (cur.hasNext()) {
				DBObject dataItem = cur.next();
				ProjectMonthData project = ModelService.createModelObject(
						dataItem, ProjectMonthData.class);
				result.add(project);
			}
			Collections.sort(result, comparator2);
		}else{
			rateData[1] = colETL.count(query);
		}
		return rateData;
	}

	private long[] extractDataFromProject(ArrayList<PrimaryObject> result) {
		long[] rateData = new long[2];
		// 查询所有超支的进行中的项目，按超支大小排序
		DBObject query = getBasicQueryCondition();
		rateData[0] = col.count(query);
		query.put(ProjectETL.F_ETL + "." + ProjectETL.F_IS_OVERCOST_DEFINITED,
				Boolean.TRUE);

		DBCursor cur = col.find(query);
		rateData[1] = cur.size();
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
		Collections.sort(result, comparator);
		return rateData;
	}

	@Override
	protected void select(PrimaryObject po) {
		UIFrameworkUtils.navigateTo(po, UIFrameworkUtils.NAVIGATE_AUTOSELECT,
				false);
	}

}
