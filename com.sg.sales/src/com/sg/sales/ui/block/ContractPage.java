package com.sg.sales.ui.block;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.design.ICSSConstants;
import com.mongodb.BasicDBObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.commons.ui.block.PageListViewer;
import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.sales.model.Opportunity;
import com.sg.sales.model.PerformenceUtil;
import com.sg.sales.model.TeamControl;
import com.sg.sales.model.dataset.MyOpportunityDataSet;
import com.sg.widgets.Widgets;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.block.tab.TabBlockPage;
import com.sg.widgets.part.CurrentAccountContext;

public class ContractPage extends TabBlockPage implements
		ISelectionChangedListener {

	private static final String[] X_AXIS_TEXT = new String[] { "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "10", "11", "12" };

	private static final int PAGESIZE = 4;

	private Label title;

	private Composite textContent;

	private ChartCanvas graphicContent;

	private Font font;

	private String userId;

	private long contractQtySeason;

	private double contractAmountSeason;

	private double contractAmountYear;

	private int q;

	private MyOpportunityDataSet opportunityDS;

	private List<PrimaryObject> opportinuties;

	private double[] contractAmountEveryMonth;

	// private static final String BLUE = "#33b5e5";

	private static final String ORANGE = "#ffbb33";

	// private static final String RED = "#ff4444";

	public static final int BLOCKSIZE = 300;

	public ContractPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());
		Control title = createTitle(parent);
		FormData fd = new FormData();
		title.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 40;

		Control text = createTextBlock(parent);
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(50);

		Control graphic = createGraphicBlock(parent);
		fd = new FormData();
		graphic.setLayoutData(fd);
		fd.top = new FormAttachment(text);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

	}

	private void init() {
		userId = new CurrentAccountContext().getConsignerId();
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
		opportunityDS = new MyOpportunityDataSet();
		opportunityDS.setQueryCondition(TeamControl.getVisitableCondition(
				userId).append(
				Opportunity.F_PROGRESS,
				new BasicDBObject().append("$in",
						new String[] { "签单成功", "签单失败" })));
		opportunityDS.setSort(new BasicDBObject().append(Opportunity.F__MDATE,
				-1));
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

	private Control createTitle(Composite parent) {
		title = new Label(parent, SWT.NONE);
		UIFrameworkUtils.enableMarkup(title);
		return title;
	}

	private Control createTextBlock(Composite parent) {
		textContent = new Composite(parent, SWT.NONE);
		textContent.setLayout(new FormLayout());
		return textContent;
	}

	private Control createGraphicBlock(Composite parent) {
		graphicContent = new ChartCanvas(parent, SWT.NONE, false);
		return graphicContent;
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH);
		int y = cal.get(Calendar.YEAR);

		// 季度
		q = m / 4 + 1;
		y = cal.get(Calendar.YEAR);
		/*
		 * 签约合同个数（本季度）
		 */
		contractQtySeason = PerformenceUtil.getContractQtySeason(userId, m);

		/*
		 * 签约合同金额（本季度）
		 */
		contractAmountSeason = PerformenceUtil.getContractAmountSeason(userId,
				m);

		/*
		 * 签约合同个数（年累计）
		 */
		// contractQtyYear = PerformenceUtil.getContractQtyYear(userId, y);

		/*
		 * 丢单
		 */
		// opportunityLost = PerformenceUtil.getOpportunityLostQty(userId);

		/*
		 * 赢单
		 */
		// opportunityGain = PerformenceUtil.getOpportunityGainQty(userId);

		/*
		 * 签约合同金额（年累计）
		 */
		contractAmountYear = PerformenceUtil.getContractAmountYear(userId, y);

		/*
		 * 获得销售机会
		 */
		opportinuties = opportunityDS.getDataSet().getDataItems();

		/*
		 * 获得全年各月的合同金额
		 */

		contractAmountEveryMonth = PerformenceUtil.getContractAmountEveryMonth(
				userId, y);
		for (int i = 0; i < contractAmountEveryMonth.length; i++) {
			contractAmountEveryMonth[i] = Math.floor(contractAmountEveryMonth[i]/10000);
		}
		return null;
	}

	@Override
	protected void doDisplayData(Object data) {
		title.setText(getTitle());
		setTextContent();

		setGraphicContent();

	}

	private void setGraphicContent() {
		Chart chart = CommonChart
				.getChart(X_AXIS_TEXT, new String[] { "c" },
						new double[][] { contractAmountEveryMonth },
						CommonChart.TYPE_BAR, CommonChart.TYPE_SUBTYPE_STACKED,
						true, 1);
		chart.getLegend().setVisible(false);

		graphicContent.setChart(chart);
		try {
			graphicContent.redrawChart(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTextContent() {
		Control[] children = textContent.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		List<PrimaryObject> success = new ArrayList<PrimaryObject>();
		List<PrimaryObject> failure = new ArrayList<PrimaryObject>();
		for (int i = 0; i < opportinuties.size(); i++) {
			Opportunity opp = (Opportunity) opportinuties.get(i);
			if ("签单成功".equals(opp.getProgress())) {
				success.add(opp);
			} else {
				failure.add(opp);
			}
		}

		int margin = 4;
		FormData fd;
		Section sectionLeft = createSection(success,
				Utils.getRGB(Utils.COLOR_BLUE[6]));
		sectionLeft.setText(success.size() == 0 ? "没有签单成功的商机" : ("签单成功的商机"
				+ success.size() + "个"));

		fd = new FormData();
		sectionLeft.setLayoutData(fd);
		fd.top = new FormAttachment(0, margin);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50, -margin);

		Section sectionRight = createSection(failure,
				Utils.getRGB(Utils.COLOR_RED[6]));
		sectionRight.setText(failure.size() == 0 ? "没有签单失败的商机" : ("签单失败的商机"
				+ failure.size() + "个"));

		fd = new FormData();
		sectionRight.setLayoutData(fd);
		fd.top = new FormAttachment(0, margin);
		fd.left = new FormAttachment(sectionLeft, margin);
		fd.right = new FormAttachment(100, -margin);

		textContent.layout();
	}

	private Section createSection(List<PrimaryObject> srcInput, int[] rgb) {
		Section section = new Section(textContent,
				ExpandableComposite.SHORT_TITLE_BAR
						| ExpandableComposite.EXPANDED);
		section.setFont(font);
		section.setForeground(Widgets.getColor(getDisplay(), rgb[0], rgb[1],
				rgb[2]));
		Composite sepbg = new Composite(section, SWT.NONE);
		sepbg.setLayout(new FormLayout());
		Label sep = new Label(sepbg, SWT.NONE);
		FormData fd = new FormData();
		sep.setLayoutData(fd);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 1;
		Display display = sep.getDisplay();
		sep.setBackground(Widgets.getColor(display, 0xe0, 0xe1, 0xe3));
		section.setSeparatorControl(sepbg);

		final PageListViewer v = new PageListViewer(section, SWT.SINGLE);

		v.setPageSize(PAGESIZE);
		v.setPageInput(srcInput);
		v.addSelectionChangedListener(this);
		section.setClient(v.getControl());

		Composite bar = new Composite(section, SWT.NONE);
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
		pageBack.setEnabled(v.canPageBack());

		final Label page = new Label(bar, SWT.NONE);
		page.setText(v.getPageText());

		final Button pageNext = new Button(bar, SWT.PUSH);
		pageNext.setData(RWT.CUSTOM_VARIANT, ICSSConstants.CSS_RIGHT_16);
		pageNext.setLayoutData(new RowData(16, 16));
		pageNext.setEnabled(v.canPageNext());

		pageBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				v.pageBack();
				pageBack.setEnabled(v.canPageBack());
				pageNext.setEnabled(v.canPageNext());
				page.setText(v.getPageText());
			}
		});
		pageNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				v.pageNext();
				pageBack.setEnabled(v.canPageBack());
				pageNext.setEnabled(v.canPageNext());
				page.setText(v.getPageText());
			}
		});

		section.setTextClient(bar);

		return section;
	}

	private String getTitle() {

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='");
		sb.append("width:500px;");
		sb.append("height:36px;" + "margin:1px;");
		sb.append("'>");
		sb.append("<div style='display:block;width:4px; height:36px;  "
				+ "float:left;background:" + ORANGE + ";'>");
		sb.append("</div>");
		sb.append("<div style='" + "display:-moz-inline-box; "
				+ "display:inline-block;" + "height:36px;" + "color:#909090;"
				+ "font-family:微软雅黑;font-size:17pt; '>");

		sb.append(q + "季度");
		sb.append("新签合同");
		sb.append(contractQtySeason);
		sb.append("个,");
		sb.append(new Double(Math.floor(contractAmountSeason / 10000)).longValue() + "万元, ");
		sb.append("全年:");
		sb.append(new Double(Math.floor(contractAmountYear / 10000)).longValue() + "万元");

		sb.append("</div>");
		sb.append("</span>");
		return sb.toString();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Opportunity opp = (Opportunity) sel.getFirstElement();
			select(opp);
		}
	}

	protected void select(PrimaryObject po) {
		UIFrameworkUtils.navigateTo(po, UIFrameworkUtils.NAVIGATE_BY_EDITOR,true);
	}
}
