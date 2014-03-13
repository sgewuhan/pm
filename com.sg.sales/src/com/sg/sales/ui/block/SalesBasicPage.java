package com.sg.sales.ui.block;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.Utils;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Work;
import com.sg.sales.model.PerformenceUtil;
import com.sg.widgets.block.tab.TabBlockPage;
import com.sg.widgets.part.CurrentAccountContext;

public class SalesBasicPage extends TabBlockPage implements
		ISelectionChangedListener {

	private Label textContent1;

	private Label textContent2;

	private Font font;

	private String userId;

	public SalesBasicPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());

		textContent1 = new Label(parent, SWT.NONE);
		UIFrameworkUtils.enableMarkup(textContent1);

		FormData fd = new FormData();
		textContent1.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.bottom = new FormAttachment(100);

		textContent2 = new Label(parent, SWT.NONE);
		UIFrameworkUtils.enableMarkup(textContent2);

		fd = new FormData();
		textContent2.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(textContent1);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

	}

	private void init() {
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
		userId = new CurrentAccountContext().getConsignerId();
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
		Calendar cal = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);

		/*
		 * 获取当前的月份
		 */
		// String month = MONTHS[cal.get(Calendar.MONTH)];
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				locale);
		String year = ""+cal.get(Calendar.YEAR);

		int m = cal.get(Calendar.MONTH);
		int y = cal.get(Calendar.YEAR);
		String customerQtyMonth = ""+PerformenceUtil.getCustomerQtyMonth(userId,m);

		String customerQtySum = ""+PerformenceUtil.getCustomerQty(userId);;

		double dValue = PerformenceUtil.getSalesCostAmountMonth(userId,m);
		String salesCostAmountMonth = df.format(dValue/10000)+"万元";

		dValue = PerformenceUtil.getSalesCostAmountSum(userId);
		String salesCostAmountSum = df.format(dValue/10000)+"万元";

		long lValue = PerformenceUtil.getFinishedCallMonth(userId,m);
		String finishedCallMonth = ""+lValue;
		
		lValue = PerformenceUtil.getFinishedVisitMonth(userId,m);
		String finishedVisitMonth = ""+lValue;

		/*
		 * 销售线索数量(本月新增)
		 */
		lValue = PerformenceUtil.getLeadsQtyMonth(userId,m);
		String leadsQtyMonth = ""+lValue;

		/*
		 * 销售线索数量(保有)
		 */

		lValue = PerformenceUtil.getLeadsQtySum(userId);
		String leadsQtySum = ""+lValue;

		/*
		 * 销售机会(本月)
		 */
		lValue = PerformenceUtil.getOpportunityQtyMonth(userId,m);
		String opportunityQtyMonth = ""+lValue;

		/*
		 * 累计
		 */
		lValue = PerformenceUtil.getOpportunityQtySum(userId);
		String opportunityQtySum = ""+lValue;

		/*
		 * 签约合同个数（本季度）
		 */
		lValue = PerformenceUtil.getContractQtySeason(userId,m);
		String contractQtySeason = ""+lValue;

		/*
		 * 签约合同金额（本季度）
		 */
		dValue = PerformenceUtil.getContractAmountSeason(userId,m);
		String contractAmountSeason = df.format(dValue/10000)+"万元";

		/*
		 * 签约合同个数（年累计）
		 */
		lValue = PerformenceUtil.getContractQtyYear(userId,y);
		String contractQtySum = ""+lValue;

		/*
		 * 签约合同金额（年累计）
		 */
		dValue = PerformenceUtil.getContractAmountYear(userId,y);
		String contractAmountSum = df.format(dValue/10000)+"万元";

		/*
		 * 回款金额（本季度）
		 */
		dValue = PerformenceUtil.getIncomeAmountSeason(userId,m);
		String incomeAmountSeason = df.format(dValue/10000)+"万元";

		/*
		 * 回款（年累计）
		 */
		dValue = PerformenceUtil.getIncomeAmountYear(userId,y);
		String incomeAmountSum = df.format(dValue/10000)+"万元";

		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("m", m);
		dataMap.put("month", month);
		dataMap.put("year", year);
		dataMap.put("finishedCallMonth", finishedCallMonth);
		dataMap.put("finishedVisitMonth", finishedVisitMonth);
		dataMap.put("salesCostAmountMonth", salesCostAmountMonth);
		dataMap.put("salesCostAmountSum", salesCostAmountSum);
		dataMap.put("customerQtyMonth", customerQtyMonth);
		dataMap.put("customerQtySum", customerQtySum);
		dataMap.put("leadsQtyMonth", leadsQtyMonth);
		dataMap.put("leadsQtySum", leadsQtySum);
		dataMap.put("opportunityQtyMonth", opportunityQtyMonth);
		dataMap.put("opportunityQtySum", opportunityQtySum);
		dataMap.put("contractQtySeason", contractQtySeason);
		dataMap.put("contractQtySum", contractQtySum);
		dataMap.put("contractAmountSeason", contractAmountSeason);
		dataMap.put("contractAmountSum", contractAmountSum);
		dataMap.put("incomeAmountSeason", incomeAmountSeason);
		dataMap.put("incomeAmountSum", incomeAmountSum);
		return dataMap;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		Integer m = (Integer)  ((HashMap<String, Object>) data).get("m");
		String month = (String)  ((HashMap<String, Object>) data).get("month");
		String year = (String)  ((HashMap<String, Object>) data).get("year");
		String finishedCallMonth = (String)  ((HashMap<String, Object>) data).get("finishedCallMonth");
		String finishedVisitMonth = (String)  ((HashMap<String, Object>) data).get("finishedVisitMonth");
		String customerQtyMonth = (String)  ((HashMap<String, Object>) data).get("customerQtyMonth");
		String customerQtySum = (String)  ((HashMap<String, Object>) data).get("customerQtySum");
		String leadsQtyMonth = (String)  ((HashMap<String, Object>) data).get("leadsQtyMonth");
		String leadsQtySum = (String)  ((HashMap<String, Object>) data).get("leadsQtySum");
		String opportunityQtyMonth = (String)  ((HashMap<String, Object>) data).get("opportunityQtyMonth");
		String opportunityQtySum = (String)  ((HashMap<String, Object>) data).get("opportunityQtySum");
		String contractQtySeason = (String)  ((HashMap<String, Object>) data).get("contractQtySeason");
		String contractQtySum = (String)  ((HashMap<String, Object>) data).get("contractQtySum");
		String contractAmountSeason = (String)  ((HashMap<String, Object>) data).get("contractAmountSeason");
		String contractAmountSum = (String)  ((HashMap<String, Object>) data).get("contractAmountSum");
		String incomeAmountSeason = (String)  ((HashMap<String, Object>) data).get("incomeAmountSeason");
		String incomeAmountSum = (String)  ((HashMap<String, Object>) data).get("incomeAmountSum");
		String salesCostAmountMonth = (String)  ((HashMap<String, Object>) data).get("salesCostAmountMonth");
		String salesCostAmountSum = (String)  ((HashMap<String, Object>) data).get("salesCostAmountSum");

		StringBuffer sb = new StringBuffer();
		sb.append(appendColumn(month+"各项销售业绩指标"));

		sb.append(appendLine("新增潜在客户",customerQtyMonth));
		sb.append(appendLine("新增销售线索",leadsQtyMonth));
		sb.append(appendLine("新增销售机会",opportunityQtyMonth));
		sb.append(appendLine(""+((m/3)+1)+"季度合同数","金额",contractQtySeason,contractAmountSeason));
		sb.append(appendLine(""+((m/3)+1)+"季度回款额",incomeAmountSeason));
		sb.append(appendLine("销售费用",salesCostAmountMonth));
		sb.append(appendLine("有效电话工作",finishedCallMonth));
		sb.append(appendLine("拜访及商务活动",finishedVisitMonth));

		sb.append("</div>");
		textContent1.setText(sb.toString());

		sb = new StringBuffer();
		sb.append(appendColumn("累计完成指标"));
		sb.append(appendLine("潜在客户",customerQtySum));
		sb.append(appendLine("销售线索",leadsQtySum));
		sb.append(appendLine("销售机会",opportunityQtySum));
		sb.append(appendLine(year+"年签约","金额",contractQtySum,contractAmountSum));
		sb.append(appendLine(year+"年累计回款",incomeAmountSum));
		sb.append(appendLine(year+"年累计费用",salesCostAmountSum));

		sb.append("</div>");
		textContent2.setText(sb.toString());

	}

	private String appendColumn(String text) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" + "font-family:微软雅黑;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "width:280;" 
				+ "'>");
		sb.append("<div style='margin:0 0 0 8;'>");
		sb.append(text);
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	private String appendLine(String label1, String label2,
			String text1, String text2) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" + "font-size:10pt;" + "color:#909090;"
				+ "margin:8 0 8 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append(label1+": ");
		sb.append("</span><span style='width:60'>");
		sb.append(text1);
		sb.append("</span>");
		sb.append("<span style='width:40'>");
		sb.append(label2+": ");
		sb.append("</span><span>");
		sb.append(text2);
		sb.append("</span>");
		sb.append("</div>");
		return sb.toString();
	}

	private String appendLine(String label, String text) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='" + "font-size:10pt;" + "color:#909090;"
				+ "margin:8 0 8 8;" + "'>");
		sb.append("<span style='width:100'>");
		sb.append(label+": ");
		sb.append("</span><span>");
		sb.append(text);
		sb.append("</span>");
		sb.append("</div>");
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
		UIFrameworkUtils.navigateTo(work, UIFrameworkUtils.NAVIGATE_AUTOSELECT,true);
	}

}
