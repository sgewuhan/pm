package com.sg.sales.ui.block;

import java.util.Calendar;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.business.model.Work;
import com.sg.sales.model.PerformenceUtil;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.block.tab.TabBlockPage;
import com.sg.widgets.part.CurrentAccountContext;

public class IncomePage extends TabBlockPage implements
		ISelectionChangedListener {

	private static final String[] X_AXIS_TEXT = new String[] { "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "10", "11", "12" };

	private Label title;

	private ChartCanvas graphicContent;

	private Font font;

	private String userId;

	private int q;

	private double[] incomeAmountEveryMonth;

	private double incomeAmountSeason;

	private double incomeAmountSum;

	private static final String ORANGE = "#ffbb33";

	public static final int BLOCKSIZE = 300;

	public IncomePage(Composite parent) {
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

		Control graphic = createGraphicBlock(parent);
		fd = new FormData();
		graphic.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

	}

	private void init() {
		userId = new CurrentAccountContext().getConsignerId();
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
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
		 * 回款金额（本季度）
		 */
		incomeAmountSeason = PerformenceUtil.getIncomeAmountSeason(userId,m);

		/*
		 * 回款（年累计）
		 */
		incomeAmountSum = PerformenceUtil.getIncomeAmountYear(userId,y);

		
		incomeAmountEveryMonth = PerformenceUtil.getIncomeAmountEveryMonth(
				userId, y);
		for (int i = 0; i < incomeAmountEveryMonth.length; i++) {
			incomeAmountEveryMonth[i] = Math.floor(incomeAmountEveryMonth[i]/10000);
		}
		return null;
	}

	@Override
	protected void doDisplayData(Object data) {
		title.setText(getTitle());

		setGraphicContent();

	}

	private void setGraphicContent() {
		Chart chart = CommonChart
				.getChart(X_AXIS_TEXT, new String[] { "c" },
						new double[][] { incomeAmountEveryMonth },
						CommonChart.TYPE_BAR, CommonChart.TYPE_SUBTYPE_STACKED,
						true, 10);
		chart.getLegend().setVisible(false);

		graphicContent.setChart(chart);
		try {
			graphicContent.redrawChart(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		sb.append("回款总额");
		sb.append(new Double(Math.floor(incomeAmountSeason / 10000)).longValue() + "万元, ");
		sb.append("全年:");
		sb.append(new Double(Math.floor(incomeAmountSum / 10000)).longValue() + "万元");

		sb.append("</div>");
		sb.append("</span>");
		return sb.toString();
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
		UIFrameworkUtils.navigateTo(work, UIFrameworkUtils.NAVIGATE_AUTOSELECT,false);
	}
}
