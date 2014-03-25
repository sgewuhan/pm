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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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

public abstract class AbstractProjectTop10Page extends PageControledListTabblockPage {

	public static final int HEAD_HEIGHT = 68;
	private DBCollection col;
	private DBObject basicQuery;
	private User user;
	private Label indcateLabel;
	private Label monthLabel;
	private Calendar now;
	private Comparator<PrimaryObject> comparator;
	private NumberFormat format;
	private DBCollection colETL;
	private long[] monthRates;
	private long[] previousMonthRates;
	private Composite head;
	private Label arrow;
	private Composite hover;

	public AbstractProjectTop10Page(Composite parent) {
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
		comparator = createComparator();

		format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		super.init();
	}

	protected abstract Comparator<PrimaryObject> createComparator() ;

	public void setHoverEnable(boolean enable){
		if(enable){
			hover.moveAbove(null);
		}else{
			hover.moveBelow(null);
		}
		hover.getParent().layout();
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
		fd.height = HEAD_HEIGHT;
		
		hover = new Composite(parent,SWT.NONE);
		hover.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				go();
			}
		});
		hover.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		fd = new FormData();
		hover.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = HEAD_HEIGHT;
		hover.setData(RWT.CUSTOM_VARIANT, "hovermask2");
		hover.moveAbove(null);
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
		fd.left = new FormAttachment(0, 2);
		fd.bottom = new FormAttachment(100, -2);
		fd.right = new FormAttachment(100, -2);
	}

	protected void go() {
		
	}

	private Composite createHead(Composite parent) {
		Composite head = new Composite(parent, SWT.NONE);
		head.setBackground(getHeadBackground());
		monthLabel = new Label(head, SWT.NONE);
		HtmlUtil.enableMarkup(monthLabel);

		Control control = createMonthSwitchLabel(head);

		Composite ind = createIndPanel(head);

		head.setLayout(new FormLayout());
		FormData fd = new FormData();
		monthLabel.setLayoutData(fd);
		fd.left = new FormAttachment(0, 2);
		fd.top = new FormAttachment(0, 2);
		fd.height = 40;
		fd.width = 160;

		fd = new FormData();
		ind.setLayoutData(fd);
		fd.left = new FormAttachment(monthLabel);
		fd.top = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);

		fd = new FormData();
		control.setLayoutData(fd);
		fd.left = new FormAttachment(0, 6);
		fd.top = new FormAttachment(monthLabel);
		fd.height = 24;

		return head;
	}

	protected Color getHeadBackground() {
		return Widgets.getColor(getDisplay(), 0x96, 0xc1, 0xec);
	}

	private Composite createIndPanel(Composite head) {
		Composite panel = new Composite(head, SWT.NONE);
		panel.setBackground(getIndicatorPanelBackgound());
		panel.setLayout(new FormLayout());
		FormData fd;
		indcateLabel = new Label(panel, SWT.NONE);
		HtmlUtil.enableMarkup(indcateLabel);
		Label indcateTitle = new Label(panel, SWT.NONE);
		HtmlUtil.enableMarkup(indcateTitle);
		StringBuffer sb = new StringBuffer();
		sb.append("<div  style='"//$NON-NLS-1$肖
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:13pt;"//$NON-NLS-1$
				+ "margin:2 0 0 12;"//$NON-NLS-1$
				+ "color:#ffffff;"//$NON-NLS-1$
				//				+ "width:100%;"//$NON-NLS-1$
				//				+ "height:24px;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(getIndenticatorLabel());
		sb.append("</div>");
		indcateTitle.setText(sb.toString());
		arrow = new Label(panel, SWT.NONE);

		fd = new FormData();
		indcateTitle.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 24;

		fd = new FormData();
		indcateLabel.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.top = new FormAttachment(indcateTitle);
		fd.bottom = new FormAttachment(100, -1);
		fd.width = 160;

		fd = new FormData();
		arrow.setLayoutData(fd);
		fd.right = new FormAttachment(100, -8);
		fd.width = 30;
		fd.top = new FormAttachment(indcateTitle);
		fd.bottom = new FormAttachment(100, -1);

		return panel;
	}

	protected Color getIndicatorPanelBackgound() {
		return Widgets.getColor(getDisplay(), 0x4b, 0x88, 0xc6);
	}

	protected abstract String getIndenticatorLabel() ;

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
		pageBack.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_LEFT_W_16);
		pageBack.setLayoutData(new RowData(16, 16));

		final Button pageNext = new Button(bar, SWT.PUSH);
		pageNext.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_RIGHT_W_16);
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
		setLabelKey(getLabelProviderKey());
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(50));
		return list;
	}

	protected abstract String getLabelProviderKey() ;

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
				+ "font-size:24pt;"//$NON-NLS-1$
				+ "margin:4 0 0 4;"//$NON-NLS-1$
				+ "color:#ffffff;"//$NON-NLS-1$
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

		if (monthRates[0] != 0) {
			double rateThisMonth = 100d * monthRates[1] / monthRates[0];
			if (rateThisMonth > 10) {
				sb.append("<div style='"//$NON-NLS-1$
						+ "font-family:微软雅黑;"//$NON-NLS-1$
						+ "font-size:28pt;"//$NON-NLS-1$
						+ "margin:0 0 0 0;"//$NON-NLS-1$
						+ "color:#ffffff;"//$NON-NLS-1$
						+ "'>"); //$NON-NLS-1$
				sb.append(format.format(rateThisMonth));
				sb.append("%");
				sb.append("</div>");
			} else {
				sb.append("<div style='"//$NON-NLS-1$
						+ "font-family:微软雅黑;"//$NON-NLS-1$
						+ "font-size:28pt;"//$NON-NLS-1$
						+ "margin:0 0 0 0;"//$NON-NLS-1$
						+ "color:#ffffff;"//$NON-NLS-1$
						+ "'>"); //$NON-NLS-1$
				sb.append(format.format(rateThisMonth));
				sb.append("%");
				sb.append("</div>");
			}
		}

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
				image = BusinessResource
						.getImage(BusinessResource.IMAGE_DOWN_48);

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
		setMonthDataCondition(query);

		if (result != null) {
			DBCursor cur = colETL.find(query);
			rateData[1] = cur.size();
			while (cur.hasNext()) {
				DBObject dataItem = cur.next();
				ProjectMonthData project = ModelService.createModelObject(
						dataItem, ProjectMonthData.class);
				result.add(project);
			}
			Collections.sort(result, comparator);
		} else {
			rateData[1] = colETL.count(query);
		}
		return rateData;
	}

	protected abstract void setMonthDataCondition(DBObject query) ;

	private long[] extractDataFromProject(ArrayList<PrimaryObject> result) {
		long[] rateData = new long[2];
		// 查询所有超支的进行中的项目，按超支大小排序
		DBObject query = getBasicQueryCondition();
		rateData[0] = col.count(query);
		setProjectDataCondition(query);


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

	protected abstract void setProjectDataCondition(DBObject query) ;

	@Override
	protected void select(PrimaryObject po) {
		if (po instanceof Project) {
			UIFrameworkUtils.navigateTo(po,
					UIFrameworkUtils.NAVIGATE_AUTOSELECT, false);
		} else if (po instanceof ProjectMonthData) {
			ProjectMonthData monthData = (ProjectMonthData) po;
			ObjectId projectId = (ObjectId) monthData
					.getValue(IProjectETL.F_PROJECTID);
			Project project = ModelService.createModelObject(Project.class,
					projectId);
			UIFrameworkUtils.navigateTo(project,
					UIFrameworkUtils.NAVIGATE_AUTOSELECT, false);
		}

	}

}
