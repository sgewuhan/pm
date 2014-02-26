package com.sg.business.pm2.home.page;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
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
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.dataset.project.MyProject;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.TabBlockPage;

public class BudgetPage extends TabBlockPage implements
		ISelectionChangedListener {

	private Font font;
	private MyProject ds;
	private Composite finishedContent;
	private Composite wipContent;

	public BudgetPage(Composite parent) {
		super(parent, SWT.NONE);
	}

	@Override
	protected void createContent(Composite parent) {
		init();
		parent.setLayout(new FormLayout());
		Control wipDetail = createWIPSection(parent);
		FormData fd = new FormData();
		wipDetail.setLayoutData(fd);
		fd.top = new FormAttachment(0, 4);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(30);
		Control finishedDetail = createFinishedSection(parent);
		fd = new FormData();
		finishedDetail.setLayoutData(fd);
		fd.top = new FormAttachment(wipDetail, 4);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
	}

	private Control createFinishedSection(Composite parent) {
		finishedContent = new Composite(parent, SWT.NONE);
		finishedContent.setLayout(new FillLayout());
		return finishedContent;
	}

	private Control createWIPSection(Composite parent) {
		wipContent = new Composite(parent, SWT.NONE);
		wipContent.setLayout(new FillLayout());
		return wipContent;
	}

	private void init() {
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
		ds = new MyProject();
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

	private Section createSection(Composite parent,
			List<PrimaryObject> srcInput, int[] rgb, int pageSize) {
		Section section = new Section(parent,
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
		v.getLabelProvider().setKey("singleline.budget");
		v.setPageSize(pageSize);
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

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		List<PrimaryObject> items = ds.getDataSet().getDataItems();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		List<PrimaryObject> items = (List<PrimaryObject>) data;
		setContent(items);
		super.doDisplayData(data);
	}

	private void setContent(List<PrimaryObject> items) {
		Control[] children = wipContent.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		children = finishedContent.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		ArrayList<PrimaryObject> wipItems = new ArrayList<PrimaryObject>();
		ArrayList<PrimaryObject> overCostItems = new ArrayList<PrimaryObject>();

		double cost = 0d;
		double budget = 0d;

		
		double costWIP = 0d;
		double budgetWIP = 0d;
		int overCostWIPCount = 0;
		for (int i = 0; i < items.size(); i++) {
			Project project = (Project) items.get(i);
			String lc = project.getLifecycleStatus();
			if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
				wipItems.add(project);
				// 判断是否超支
				ProjectPresentation pres = project.getPresentation();
				if (pres.isETLDataAvailable() && pres.isOverCostDefinited()) {
					budgetWIP += pres.getBudgetValue();
					costWIP += pres.getDesignatedInvestment();
					overCostWIPCount++;
				}
				
			} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
				// 判断是否超支
				ProjectPresentation pres = project.getPresentation();
				if (pres.isETLDataAvailable() && pres.isOverCostDefinited()) {
					overCostItems.add(project);
					budget += pres.getBudgetValue();
					cost += pres.getDesignatedInvestment();
				}
			}
		}

		if(wipItems.isEmpty()){
			Section section = createSection(wipContent, wipItems,
					Utils.getRGB(Utils.COLOR_BLUE[13]), 4);
			section.setText("您还没有进行中项目");
		}else if(budgetWIP>=costWIP){
			Section section = createSection(wipContent, wipItems,
					Utils.getRGB(Utils.COLOR_BLUE[13]), 4);
			section.setText("进行中项目的支出控制良好");
		}else{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			String overCost = nf.format((costWIP - budgetWIP) / 10000);

			int ratio = new BigDecimal(100 * costWIP / budgetWIP).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();

			Section section = createSection(wipContent, wipItems,
					Utils.getRGB(Utils.COLOR_YELLOW[13]), 4);
			section.setText("进行中已超支的项目数" + overCostWIPCount + "，金额"
					+ overCost + "万元，占比" + ratio + "%");
		}


		if (!overCostItems.isEmpty()) {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			String overCost = nf.format((cost - budget) / 10000);

			int ratio = new BigDecimal(100 * cost / budget).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();

			Section section = createSection(finishedContent, overCostItems,
					Utils.getRGB(Utils.COLOR_RED[13]), 8);
			section.setText("超支完成的项目数" + overCostItems.size() + "，金额"
					+ overCost + "万元，占比" + ratio + "%");
		}else{
			Section section = createSection(finishedContent, overCostItems,
					Utils.getRGB(Utils.COLOR_BLUE[13]), 8);
			section.setText("您没有超支完成的项目");
		}
		layout(false, true);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub

	}
}
