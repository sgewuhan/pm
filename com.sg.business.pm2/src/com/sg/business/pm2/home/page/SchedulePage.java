package com.sg.business.pm2.home.page;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.internal.widgets.MarkupValidator;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.design.ICSSConstants;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;
import com.sg.widgets.Widgets;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.block.TabBlockPage;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

@SuppressWarnings("restriction")
public class SchedulePage extends TabBlockPage implements ISelectionChangedListener {

	private static final int PAGESIZE = 4;

	private ProcessingNavigatorItemSet itemSet;

	private Label title;

	private Composite textContent;

	private ChartCanvas graphicContent;

	private Font font;

	private static final String BLUE = "#33b5e5";

	private static final String ORANGE = "#ffbb33";

	private static final String RED = "#ff4444";

	public static final int BLOCKSIZE = 300;

	public SchedulePage(Composite parent) {
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

		Control grsphic = createGraphicBlock(parent);
		fd = new FormData();
		grsphic.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(45);
		fd.bottom = new FormAttachment(100);

		Control text = createTextBlock(parent);
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment(grsphic);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
	}

	private void init() {
		itemSet = new ProcessingNavigatorItemSet();
		font = new Font(getDisplay(), "微软雅黑", 16, SWT.NORMAL);
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

	private Control createTitle(Composite parent) {
		title = new Label(parent, SWT.NONE);
		title.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		title.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
		return title;
	}

	private Control createTextBlock(Composite parent) {
		textContent = new Composite(parent, SWT.NONE);
		textContent.setLayout(new FormLayout());
		return textContent;
	}

	private Control createGraphicBlock(Composite parent) {
		graphicContent = new ChartCanvas(parent, SWT.NONE,false);
		return graphicContent;
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	protected Object doGetData() {
		List<PrimaryObject> items = itemSet.getDataSet().getDataItems();
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		List<PrimaryObject> items = (List<PrimaryObject>) data;
		int overSchedule = 0;
		int overScheduleEst = 0;

		for (int i = 0; i < items.size(); i++) {
			Work work = (Work) items.get(i);
			boolean delayFinish = work.isDelayFinish();
			boolean delayFinishEst = work.isDelayFinishEst();
			if (delayFinish) {
				overSchedule++;
			} else if (delayFinishEst) {
				overScheduleEst++;
			}
		}
		int reserved = items.size();
		title.setText(getTitle(reserved, overSchedule, overScheduleEst));
		setTextContent(items);

		setGraphicContent(reserved, overSchedule, overScheduleEst);

	}

	private void setGraphicContent(int reserved, int overSchedule,
			int overScheduleEst) {
		String[] titles = new String[] { "超期", "预警", "正常" };
		double[] values = new double[] { overSchedule, overScheduleEst,
				reserved - overSchedule - overScheduleEst };

		Chart chart = getChart(titles, values);
		graphicContent.setChart(chart);
		try {
			graphicContent.redrawChart(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTextContent(List<PrimaryObject> items) {
		Control[] children = textContent.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		int total = items.size();

		// 显示超期的
		List<PrimaryObject> overScheduleInput = new ArrayList<PrimaryObject>();
		// 显示预警的
		List<PrimaryObject> overScheduleEstInput = new ArrayList<PrimaryObject>();
		// 显示最紧急的
		List<PrimaryObject> emergencyInput = new ArrayList<PrimaryObject>();
		for (int i = 0; i < items.size(); i++) {
			Work work = (Work) items.get(i);
			boolean delayFinish = work.isDelayFinish();
			boolean delayFinishEst = work.isDelayFinishEst();
			if (delayFinish) {
				overScheduleInput.add(work);
			} else if (delayFinishEst) {
				overScheduleEstInput.add(work);
			} else {
				emergencyInput.add(work);
			}
		}

		// 创建超期工作清单
		Control topControl = null;
		int margin = 4;
		FormData fd;
		Section section;
		int count = 3;
		if (!overScheduleInput.isEmpty()) {
			section = createSection(overScheduleInput,Utils.getRGB(Utils.COLOR_RED[6]));
			float rate = 1f * overScheduleInput.size() / total;
			String sectionTitle = new DecimalFormat("#").format(100 * rate)
					+ "%" + "工作逾期未完成";
			section.setText(sectionTitle);

			fd = new FormData();
			section.setLayoutData(fd);
			fd.top = new FormAttachment(0, margin);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100, -margin * 2);
			topControl = section;
			count--;
		}
		// 创建预警工作清单
		if (!overScheduleEstInput.isEmpty()) {
			section = createSection(overScheduleEstInput,Utils.getRGB(Utils.COLOR_YELLOW[6]));
			section.setText("超期预警"+overScheduleEstInput.size()+"件");

			fd = new FormData();
			section.setLayoutData(fd);
			fd.top = new FormAttachment(topControl, margin);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100, -margin * 2);
			topControl = section;
			count--;
		}
		// 工作清单
		if (!emergencyInput.isEmpty() && count > 0) {
			section = createSection(emergencyInput,Utils.getRGB(Utils.COLOR_OLIVER[6]));
			section.setText("需处理的工作"+emergencyInput.size()+"件");

			fd = new FormData();
			section.setLayoutData(fd);
			fd.top = new FormAttachment(topControl, margin);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100, -margin * 2);
			topControl = section;
		}
		textContent.layout();
	}

	private Section createSection(List<PrimaryObject> srcInput,int[] rgb) {
		Section section = new Section(textContent,
				ExpandableComposite.SHORT_TITLE_BAR
						| ExpandableComposite.EXPANDED);
		section.setFont(font);
		section.setForeground(Widgets.getColor(getDisplay(),rgb[0], rgb[1], rgb[2]));
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

		final Label page = new Label(bar,SWT.NONE);
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

	private String getTitle(int reserved, int overSchedule, int overScheduleEst) {
		String color;
		if (overSchedule > 0) {
			color = RED;
		} else if (overScheduleEst > 0) {
			color = ORANGE;
		} else {
			color = BLUE;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='");
		sb.append("width:500px;");
		sb.append("height:36px;" + "margin:1px;");
		sb.append("'>");
		sb.append("<div style='display:block;width:4px; height:36px;  "
				+ "float:left;background:" + color + ";'>");
		sb.append("</div>");
		sb.append("<div style='" + "display:-moz-inline-box; "
				+ "display:inline-block;" + "height:36px;" + "color:#909090;"
				+ "font-family:微软雅黑;font-size:19pt; '>");
		if (reserved > 0) {
			sb.append("您需要处理的工作" + reserved + "件");
			if (overSchedule + overScheduleEst > 0) {
			}
			if (overSchedule > 0) {
				sb.append(" 超期" + overSchedule + "件");
			}
			if (overScheduleEst > 0) {
				sb.append(" 超期预警" + overScheduleEst + "件");
			}
		} else {
			sb.append("您没有需要处理的工作");
		}
		sb.append("</div>");
		sb.append("</span>");
		return sb.toString();
	}

	public Chart getChart(String[] texts, double[] values) {
		// double maxValue = Utils.max(values);
		ChartWithoutAxes chart = ChartWithoutAxesImpl.create();
		chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		chart.getTitle().setVisible(false);
		chart.getLegend().setVisible(false);
		;
		TextDataSet categoryValues = TextDataSetImpl.create(texts);//$NON-NLS-1$ //$NON-NLS-2$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(values);
		// Base Series
		Series series = SeriesImpl.create();
		series.setDataSet(categoryValues);
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		chart.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().getEntries().clear();
		int[] rgb = Utils.getRGB(Utils.COLOR_RED[6]);
		sd.getSeriesPalette().getEntries()
				.add(ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]));
		rgb = Utils.getRGB(Utils.COLOR_YELLOW[6]);
		sd.getSeriesPalette().getEntries()
				.add(ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]));
		rgb = Utils.getRGB(Utils.COLOR_OLIVER[6]);
		sd.getSeriesPalette().getEntries()
				.add(ColorDefinitionImpl.create(rgb[0], rgb[1], rgb[2]));
		sd.getSeries().add(series);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setExplosion(2);
		sePie.setRotation(40);

		sePie.setLabelPosition(Position.INSIDE_LITERAL);// 设置在内部显示数字
		sePie.getLabel().getCaption().getFont().setSize(7f);
		SeriesDefinition sdef = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdef);
		sdef.getSeries().add(sePie);

		return chart;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		if(sel!=null&&!sel.isEmpty()){
			Work work = (Work) sel.getFirstElement();
			select(work);
		}
	}
	
	protected void select(Work work) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PrimaryObjectDetailFormView view = (PrimaryObjectDetailFormView) page.findView("pm2.work.detail");
		IEditorInputFactory inputFactory = work.getAdapter(IEditorInputFactory.class);
		view.setInput(inputFactory.getInput(null));
	}
}
