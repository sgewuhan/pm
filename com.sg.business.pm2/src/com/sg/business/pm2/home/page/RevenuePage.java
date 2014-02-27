package com.sg.business.pm2.home.page;

import java.util.HashMap;

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

import com.sg.business.model.Work;
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

	// private static final String BLUE = "#33b5e5";
	//
	// private static final String ORANGE = "#ffbb33";
	//
	// private static final String RED = "#ff4444";

	public RevenuePage(Composite parent) {
		super(parent, SWT.NONE);
	}

	// 一月新增销售收入5万元，利润3万元
	// 累计实现销售收入300万元，利润100万元
	// 已完成的14个项目中，7个项目实现盈利，占比50%
	// 平均利润率：10%

	// 一月新增销售收入5万元/一月没有新增销收入，利润3万元/亏损10万元
	// 累计实现销售收入300万元，利润100万元/亏损12万元
	// 已完成的14个项目中/没有与已完成的项目，7个项目实现盈利/没有项目实现盈利，占比50%
	// 平均利润率：10%

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());
		
		textContent1 = new Label(parent, SWT.NONE);
		textContent1.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		textContent1.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,Boolean.TRUE);
		FormData fd = new FormData();
		textContent1.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.bottom = new FormAttachment(25);

		textContent2 = new Label(parent, SWT.NONE);
		textContent2.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		textContent2.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,Boolean.TRUE);
		fd = new FormData();
		textContent2.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(textContent1);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(40);

		Label label = new Label(parent, SWT.NONE);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		label.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,Boolean.TRUE);
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" 
				+ "font-family:微软雅黑;" 
				+ "margin:8;" 
				+ "width:100%;"
				+ "'>");
		sb.append("<div style='" 
				+ "font-size:11pt;"
				+ "color:#6a6a6a;" 
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;"
				+ "width:580;"
				+ "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("项目各月销售利润");
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
		//TODO 此处初始化数据集，collection等
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
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
		//TODO 取数，此处获取所有的数据
		/*
		 * 获取当前的月份
		 */
		String month = "一月份";

		/*
		 * 获取当前的月份，当前用户所有项目的销售收入
		 */
		String monthRevenue = "5";

		/*
		 * 获取当前的月份，当前用户所有项目的销售利润
		 */
		String monthProfit = "5";

		/*
		 * 获取当前的月份，当前用户所有项目的销售利润率
		 */
		String monthProfitRate = "10%";

		/*
		 * 获取累计的，当前用户所有项目的销售收入
		 */
		String sumRevenue = "200";

		/*
		 * 获取累计的，当前用户所有项目的销售利润
		 */
		String sumProfit = "23";

		/*
		 * 获取累计的，当前用户所有项目的平均利润率
		 */
		String avgProfitRate = "10%";

		/*
		 * 获取累计的，当前用户的盈利项目个数
		 */
		String sumProfitable = "4";

		/*
		 * 获取累计的，当前用户的盈利项目个数/总项目个数
		 */
		String avgsumProfitableRate = "20%";

		/*
		 * 获取当年每个月的项目销售利润
		 */
		double[] profitEveryMonth = new double[]{10800,12903,29393,23404,38408,48484,10000,12093,29393,23404,38408,48484};

		
		HashMap<String, Object> dataMap = new HashMap<String,Object>();
		dataMap.put("month",month);
		dataMap.put("monthRevenue",monthRevenue);
		dataMap.put("monthProfit",monthProfit);
		dataMap.put("monthProfitRate",monthProfitRate);
		dataMap.put("sumRevenue",sumRevenue);
		dataMap.put("sumProfit",sumProfit);
		dataMap.put("avgProfitRate",avgProfitRate);
		dataMap.put("sumProfitable",sumProfitable);
		dataMap.put("avgsumProfitableRate",avgsumProfitableRate);
		dataMap.put("profitEveryMonth",profitEveryMonth);
		return dataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) data;
		String month = (String) dataMap.get("month");
		String monthRevenue = (String) dataMap.get("monthRevenue");
		String monthProfit = (String) dataMap.get("monthProfit");
		String monthProfitRate = (String) dataMap.get("monthProfitRate");
		String sumRevenue = (String) dataMap.get("sumRevenue");
		String sumProfit = (String) dataMap.get("sumProfit");
		String avgProfitRate = (String) dataMap.get("avgProfitRate");
		String sumProfitable = (String) dataMap.get("sumProfitable");
		String avgsumProfitableRate = (String) dataMap.get("avgsumProfitableRate");
		double[] profitEveryMonth = (double[]) dataMap.get("profitEveryMonth");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" 
				+ "font-family:微软雅黑;" 
				+ "margin:8;" 
				+ "width:100%;"
				+ "'>");
		sb.append("<div style='" 
				+ "font-size:11pt;"
				+ "color:#6a6a6a;" 
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;"
				+ "width:280;"
				+ "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append(month);
		sb.append("项目当月各项经济指标");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:8 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("新增销售收入：");
		sb.append("</span><span>");
		sb.append(monthRevenue);
		sb.append("万元");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:0 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("实现销售利润：");
		sb.append("</span><span>");
		sb.append(monthProfit);
		sb.append("万元");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:0 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("平均利润率：");
		sb.append("</span><span>");
		sb.append(monthProfitRate);
		sb.append("</span>");
		sb.append("</div>");

		sb.append("</div>");
		textContent1.setText(sb.toString());
		
		
		sb = new StringBuffer();
		sb.append("<div style='" 
				+ "font-family:微软雅黑;" 
				+ "margin:8;" 
				+ "width:100%;"
				+ "'>");
		sb.append("<div style='" 
				+ "font-size:11pt;"
				+ "color:#6a6a6a;" 
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;"
				+ "width:280;"
				+ "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("已完成项目累计经济指标");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:8 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("累计销售收入：");
		sb.append("</span><span>");
		sb.append(sumRevenue);
		sb.append("万元");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:0 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("累计销售利润：");
		sb.append("</span><span>");
		sb.append(sumProfit);
		sb.append("万元");
		sb.append("</span>");
		sb.append("</div>");
		
		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:0 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("平均利润率：");
		sb.append("</span><span>");
		sb.append(avgProfitRate);
		sb.append("</span>");
		sb.append("</div>");

		sb.append("<div style='" 
				+ "font-size:9pt;"
				+ "color:#909090;" 
				+ "margin:0 0 0 8;" 
				+ "'>");
		sb.append("<span style='width:100'>");
		sb.append("盈利项目数：");
		sb.append("</span><span style='width:60'>");
		sb.append(sumProfitable);
		sb.append("</span>");
		sb.append("<span style='width:40'>");
		sb.append("占比：");
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
				new double[][] { values }, CommonChart.TYPE_BAR, CommonChart.TYPE_SUBTYPE_STACKED,
				false, 10);
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
