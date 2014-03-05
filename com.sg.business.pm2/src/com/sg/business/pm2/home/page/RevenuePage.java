package com.sg.business.pm2.home.page;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.DBActivator;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.resource.nls.Messages;
import com.sg.business.visualization.chart.CommonChart;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.block.TabBlockPage;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

@SuppressWarnings("restriction")
public class RevenuePage extends TabBlockPage implements
		ISelectionChangedListener {

	private Label textContent1;

	private Label textContent2;

	private ChartCanvas graphicContent;

	private Font font;

	private String userId;

	private DBCollection projectCol;

	private DBCollection projectMonthDataCol;

	private static String[] MONTHS = new String[] { "һ�·�", "���·�", "���·�", "���·�",
			"���·�", "���·�", "���·�", "���·�", "���·�", "ʮ�·�", "ʮһ�·�", "ʮ���·�" };;

	// private static final String BLUE = "#33b5e5";
	//
	// private static final String ORANGE = "#ffbb33";
	//
	// private static final String RED = "#ff4444";

	public RevenuePage(Composite parent) {
		super(parent, SWT.NONE);
	}

	// һ��������������5��Ԫ������3��Ԫ
	// �ۼ�ʵ����������300��Ԫ������100��Ԫ
	// ����ɵ�14����Ŀ�У�7����Ŀʵ��ӯ����ռ��50%
	// ƽ�������ʣ�10%

	// һ��������������5��Ԫ/һ��û�����������룬����3��Ԫ/����10��Ԫ
	// �ۼ�ʵ����������300��Ԫ������100��Ԫ/����12��Ԫ
	// ����ɵ�14����Ŀ��/û��������ɵ���Ŀ��7����Ŀʵ��ӯ��/û����Ŀʵ��ӯ����ռ��50%
	// ƽ�������ʣ�10%

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());

		textContent1 = new Label(parent, SWT.NONE);
		textContent1.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		textContent1.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);
		FormData fd = new FormData();
		textContent1.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.bottom = new FormAttachment(25);

		textContent2 = new Label(parent, SWT.NONE);
		textContent2.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		textContent2.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);
		fd = new FormData();
		textContent2.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(textContent1);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(40);

		Label label = new Label(parent, SWT.NONE);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		label.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" + "font-family:΢���ź�;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;" + "width:580;" + "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("��Ŀ������������");
		sb.append("</span></div></div>");
		label.setText(sb.toString());
		fd = new FormData();
		label.setLayoutData(fd);
		fd.top = new FormAttachment(textContent1);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		graphicContent = new ChartCanvas(parent, SWT.NONE, false);
		fd = new FormData();
		graphicContent.setLayoutData(fd);
		fd.top = new FormAttachment(label);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

	}

	private void init() {
		// TODO �˴���ʼ�����ݼ���collection��
		font = new Font(getDisplay(), "΢���ź�", 16, SWT.NORMAL);

		userId = context.getConsignerId();

		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);

		projectMonthDataCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		// TODO ȡ�����˴���ȡ���е�����
		Calendar cal = Calendar.getInstance();

		double[] sumSales = getProjectETL();

		int year = cal.get(Calendar.YEAR);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		double[] monthSales = getProjectMonthETL(cal, year, sumSales);

		BigDecimal d = new BigDecimal((sumSales[0]) / 10000d);
		sumSales[0] = d.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		d = new BigDecimal((sumSales[1]) / 10000d);
		sumSales[1] = d.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		d = new BigDecimal((sumSales[2]));
		sumSales[2] = d.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		d = new BigDecimal((sumSales[4]));
		sumSales[4] = d.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		/*
		 * ��ȡ��ǰ���·�
		 */
		String month = MONTHS[cal.get(Calendar.MONTH)];

		/*
		 * ��ȡ��ǰ���·ݣ���ǰ�û�������Ŀ����������
		 */
		String monthRevenue = "" + monthSales[0];

		/*
		 * ��ȡ��ǰ���·ݣ���ǰ�û�������Ŀ����������
		 */
		String monthProfit = "" + monthSales[1];

		/*
		 * ��ȡ��ǰ���·ݣ���ǰ�û�������Ŀ������������
		 */
		String monthProfitRate = "" + monthSales[2] + "%";

		/*
		 * ��ȡ�ۼƵģ���ǰ�û�������Ŀ����������
		 */

		String sumRevenue = "" + sumSales[0];

		/*
		 * ��ȡ�ۼƵģ���ǰ�û�������Ŀ����������
		 */
		String sumProfit = "" + sumSales[1];

		/*
		 * ��ȡ�ۼƵģ���ǰ�û�������Ŀ��ƽ��������
		 */
		String avgProfitRate = "" + sumSales[2] + "%";

		/*
		 * ��ȡ�ۼƵģ���ǰ�û���ӯ����Ŀ����
		 */
		String sumProfitable = "" + sumSales[3];

		/*
		 * ��ȡ�ۼƵģ���ǰ�û���ӯ����Ŀ����/����Ŀ����
		 */
		String avgsumProfitableRate = "" + sumSales[4] + "%";

		/*
		 * ��ȡ����ÿ���µ���Ŀ��������
		 */
		double[] profitEveryMonth = new double[] { monthSales[3],
				monthSales[4], monthSales[5], monthSales[6], monthSales[7],
				monthSales[8], monthSales[9], monthSales[10], monthSales[11],
				monthSales[12], monthSales[13], monthSales[14] };

		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("month", month);
		dataMap.put("monthRevenue", monthRevenue);
		dataMap.put("monthProfit", monthProfit);
		dataMap.put("monthProfitRate", monthProfitRate);
		dataMap.put("sumRevenue", sumRevenue);
		dataMap.put("sumProfit", sumProfit);
		dataMap.put("avgProfitRate", avgProfitRate);
		dataMap.put("sumProfitable", sumProfitable);
		dataMap.put("avgsumProfitableRate", avgsumProfitableRate);
		dataMap.put("profitEveryMonth", profitEveryMonth);
		return dataMap;
	}

	private double[] getProjectMonthETL(Calendar cal, int year,
			double[] sumSales) {
		// year = 2013;
		double[] monthSales = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0 };
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_CHARGER, userId),
						new BasicDBObject().append(Project.F_BUSINESS_CHARGER,
								userId) });
		query.put(ProjectETL.F_YEAR, cal.get(Calendar.YEAR));
		query.put(ProjectETL.F_MONTH, cal.get(Calendar.MONTH) + 1);
		DBCursor projectMonthCursor = projectMonthDataCol.find(query);
		while (projectMonthCursor.hasNext()) {
			DBObject dbo = projectMonthCursor.next();
			double salesRevenue = (double) dbo.get(ProjectETL.F_SALES_REVENUE);
			double salesCost = (double) dbo.get(ProjectETL.F_SALES_COST);
			double salesProfit = salesRevenue - salesCost;
			monthSales[0] = monthSales[0] + salesRevenue;
			monthSales[1] = monthSales[1] + salesProfit;
		}
		monthSales[0] = sumSales[0] - monthSales[0];
		monthSales[1] = sumSales[1] - monthSales[1];
		monthSales[2] = monthSales[1] / monthSales[0] * 100;

		query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year).append(
						"$or",
						new BasicDBObject[] {
								new BasicDBObject().append(Project.F_CHARGER,
										userId),
								new BasicDBObject().append(
										Project.F_BUSINESS_CHARGER, userId) }));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$month").append(
						ProjectETL.F_MONTH_SALES_PROFIT,
						new BasicDBObject().append("$sum", "$"
								+ ProjectETL.F_MONTH_SALES_PROFIT)));

		AggregationOutput aggregationOutput = projectMonthDataCol.aggregate(
				query, group);

		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			int month = (int) dbObject.get("_id");
			monthSales[month + 2] = ((Number) dbObject
					.get(ProjectETL.F_MONTH_SALES_PROFIT)).doubleValue();
		}
		for (int i = 0; i < monthSales.length; i++) {
			if (monthSales[i] != 0d && i != 2) {
				BigDecimal d = new BigDecimal((monthSales[i]) / 10000d);
				monthSales[i] = d.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			} else if (i == 2) {
				BigDecimal d = new BigDecimal(monthSales[i]);
				monthSales[i] = d.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
		}

		return monthSales;
	}

	private double[] getProjectETL() {
		double[] sumSales = new double[] { 0, 0, 0, 0, 0 };
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_CHARGER, userId),
						new BasicDBObject().append(Project.F_BUSINESS_CHARGER,
								userId) });
		DBCursor projectCursor = projectCol.find(query);
		sumSales[4] = projectCursor.size();
		while (projectCursor.hasNext()) {
			DBObject dbo = projectCursor.next();
			Object etl = dbo.get(ProjectETL.F_ETL);
			if (etl != null && etl instanceof DBObject) {
				double salesRevenue = (double) ((DBObject) etl)
						.get(ProjectETL.F_SALES_REVENUE);
				double salesCost = (double) ((DBObject) etl)
						.get(ProjectETL.F_SALES_COST);
				double salesProfit = salesRevenue - salesCost;
				sumSales[0] = sumSales[0] + salesRevenue;
				sumSales[1] = sumSales[1] + salesProfit;
				if (salesProfit > 0) {
					sumSales[3] = sumSales[3] + 1;
				}
			}

		}

		sumSales[2] = sumSales[1] / sumSales[0] * 100;
		sumSales[4] = sumSales[3] / sumSales[4] * 100;

		return sumSales;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		String month = "";
		String monthRevenue = "";
		String monthProfit = "";
		String monthProfitRate = "";
		String sumRevenue = "";
		String sumProfit = "";
		String avgProfitRate = "";
		String sumProfitable = "";
		String avgsumProfitableRate = "";
		double[] profitEveryMonth = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0 };

		HashMap<String, Object> dataMap = (HashMap<String, Object>) data;
		if (dataMap != null) {
			month = (String) dataMap.get("month");
			monthRevenue = (String) dataMap.get("monthRevenue");
			monthProfit = (String) dataMap.get("monthProfit");
			monthProfitRate = (String) dataMap.get("monthProfitRate");
			sumRevenue = (String) dataMap.get("sumRevenue");
			sumProfit = (String) dataMap.get("sumProfit");
			avgProfitRate = (String) dataMap.get("avgProfitRate");
			sumProfitable = (String) dataMap.get("sumProfitable");
			avgsumProfitableRate = (String) dataMap.get("avgsumProfitableRate");
			profitEveryMonth = (double[]) dataMap.get("profitEveryMonth");

		}

		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" + "font-family:΢���ź�;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;" + "width:280;" + "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append(month);
		sb.append("��Ŀ���¸����ָ��");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:8 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("�����������룺");
		sb.append("</span><span>");
		sb.append(monthRevenue);
		sb.append("��Ԫ");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:0 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("ʵ����������");
		sb.append("</span><span>");
		sb.append(monthProfit);
		sb.append("��Ԫ");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:0 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("ƽ�������ʣ�");
		sb.append("</span><span>");
		sb.append(monthProfitRate);
		sb.append("</span>");
		sb.append("</div>");

		sb.append("</div>");
		textContent1.setText(sb.toString());

		sb = new StringBuffer();
		sb.append("<div style='" + "font-family:΢���ź�;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;" + "width:280;" + "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("�������Ŀ�ۼƾ���ָ��");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:8 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("�ۼ��������룺");
		sb.append("</span><span>");
		sb.append(sumRevenue);
		sb.append("��Ԫ");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:0 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("�ۼ���������");
		sb.append("</span><span>");
		sb.append(sumProfit);
		sb.append("��Ԫ");
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:0 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("ƽ�������ʣ�");
		sb.append("</span><span>");
		sb.append(avgProfitRate);
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" + "font-size:9pt;" + "color:#909090;"
				+ "margin:0 0 0 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append("ӯ����Ŀ����");
		sb.append("</span><span style='width:60'>");
		sb.append(sumProfitable);
		sb.append("</span>");
		sb.append("<span style='width:40'>");
		sb.append("ռ�ȣ�");
		sb.append("</span><span>");
		sb.append(avgsumProfitableRate);
		sb.append("</span>");
		sb.append("</div>");

		sb.append("</div>");
		textContent2.setText(sb.toString());

		setGraphicContent(profitEveryMonth);

	}

	private void setGraphicContent(double[] values) {
		Messages messages = Messages.get(locale);
		String[] lsText = { messages.ProfitVolumnView_0 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		Chart chart = CommonChart.getChart(xAxisText, lsText,
				new double[][] { values }, CommonChart.TYPE_BAR,
				CommonChart.TYPE_SUBTYPE_STACKED, false, 10);
		chart.getLegend().setVisible(false);
		graphicContent.setChart(chart);
		try {
			graphicContent.redrawChart(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Work work = (Work) sel.getFirstElement();
			select(work);
		}
	}

	protected void select(Work work) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		PrimaryObjectDetailFormView view = (PrimaryObjectDetailFormView) page
				.findView("pm2.work.detail");
		IEditorInputFactory inputFactory = work
				.getAdapter(IEditorInputFactory.class);
		view.setInput(inputFactory.getInput(null));
	}

}
