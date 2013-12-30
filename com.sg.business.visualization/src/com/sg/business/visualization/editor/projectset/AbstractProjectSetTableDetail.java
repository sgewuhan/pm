package com.sg.business.visualization.editor.projectset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.sg.business.model.ProjectProvider;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.labelprovider.AbstractProjectLabelProvider;
import com.sg.widgets.Widgets;
import com.sg.widgets.registry.config.ColumnConfigurator;

@SuppressWarnings("restriction")
public abstract class AbstractProjectSetTableDetail extends
		AbstractProjectSetPage {

	private static final int SUMMARY_HEIGHT = 50;
	private Set<ColumnSorter> tableSorters = new HashSet<ColumnSorter>();

	public AbstractProjectSetTableDetail() {

	}

	@Override
	protected Composite createContent(Composite body) {
		Composite bg = new Composite(body,SWT.NONE);
		bg.setLayout(new FormLayout());
		
		Composite content = new Composite(bg, SWT.NONE);
		navi.createPartContent(content);

		FormData fd = new FormData();
		content.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		if (displaySummary()) {
			fd.bottom = new FormAttachment(100, -SUMMARY_HEIGHT);
			Composite summary = createSummary(bg);
			fd = new FormData();
			summary.setLayoutData(fd);
			fd.top = new FormAttachment(content, 1);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
		}

		fd.bottom = new FormAttachment(100);
		return bg;
	}

	private Composite createSummary(Composite body) {
		Composite comp = new Composite(body, SWT.NONE);

		Object adapter = comp.getAdapter(IWidgetGraphicsAdapter.class);
		IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
		int[] percents = new int[] { 0, 5, 15 };
		Display display = comp.getDisplay();
		Color[] gradientColors = new Color[] {
				Widgets.getColor(display, 220, 220, 220),
				Widgets.getColor(display, 240, 240, 240),
				Widgets.getColor(display, 255, 255, 255),
				};
		gfxAdapter.setBackgroundGradient(gradientColors, percents, true);
		
		
		comp.setLayout(new FormLayout());
		Label sep = new Label(comp, SWT.NONE);
		FormData fd = new FormData();
		sep.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.height = 1;
		fd.right = new FormAttachment(100);
		sep.setBackground(Widgets.getColor(comp.getDisplay(), 192, 192, 192));
		
		// 根据column的数量来创建子容器
		Table table = (Table) navi.getViewerControl().getViewer().getControl();
		TableColumn[] columns = table.getColumns();
		SummaryBlock leftSC = null;
		for (int i = 0; i < columns.length; i++) {
			ColumnConfigurator columnConfigurator = (ColumnConfigurator) columns[i].getData("CONFIGURATOR");
			final SummaryBlock sc = createColumnSummary(comp,
					columnConfigurator);
			final TableColumn column = columns[i];
			layoutSummaryBlock(sc, leftSC, column.getWidth());
			final SummaryBlock left = leftSC;
			columns[i].addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					layoutSummaryBlock(sc, left, column.getWidth());
				}
			});
			
			controlColumn(columns[i],columnConfigurator);
			leftSC = sc;
		}

		return comp;
	}

	protected void controlColumn(final TableColumn tableColumn, ColumnConfigurator conf) {
		final List<ColumnSorter> sorters = getColumnSorters(conf);
		if(sorters==null||sorters.isEmpty()){
			return;
		}
		final Menu dropDownMenu = new Menu(tableColumn.getParent().getShell(), SWT.POP_UP);
		for (int i = 0; i < sorters.size(); i++) {
			final ColumnSorter sorter = sorters.get(i);
			if(sorter.isSeperater()){
				new MenuItem(dropDownMenu, SWT.SEPARATOR);	
			}else{
				MenuItem item = new MenuItem(dropDownMenu, SWT.PUSH);	
				sorter.setMenuItem(item);
				sorter.setViewer(navi.getViewer());
				item.setText(sorter.getText());
				item.setImage(BusinessResource.getImage(BusinessResource.IMAGE_24X24_BLANK));
				item.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						sorter.sort();
						removeOtherSorts(sorter);
					}
				});
				tableSorters.add(sorter);
			}
		}
		
		tableColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TableColumn column = (TableColumn) event.getSource();
				
				Table table = column.getParent();
				TableColumn[] cols = table.getColumns();
				int left = 0;
				for (int i = 0; i < cols.length && cols[i]!=column; i++) {
					left += cols[i].getWidth();
				}
				
				Point point = table.toDisplay(event.x+left, event.y+32);
				dropDownMenu.setLocation(point);
				dropDownMenu.setVisible(true);
			}
		});
		
	}

	protected void removeOtherSorts(ColumnSorter sorter) {
		Iterator<ColumnSorter> iter = tableSorters.iterator();
		while(iter.hasNext()){
			ColumnSorter other = iter.next();
			if(other!=sorter){
				other.removeSorter();
			}
		}
	}

	protected List<ColumnSorter> getColumnSorters(ColumnConfigurator conf) {
		return new ArrayList<ColumnSorter>();
	}

	private void layoutSummaryBlock(SummaryBlock sc, SummaryBlock leftSC,
			int width) {
		FormData fd = new FormData();
		sc.setLayoutData(fd);
		fd.top = new FormAttachment(0,1);
		if (leftSC == null) {
			fd.left = new FormAttachment();
		} else {
			fd.left = new FormAttachment(leftSC);
		}
		fd.width = width;
		fd.bottom = new FormAttachment(100);
		sc.getParent().layout();
	}

	protected SummaryBlock createColumnSummary(Composite parent,
			ColumnConfigurator column) {
		String text = getSummary(data,column);
		SummaryBlock sb = new SummaryBlock(parent);
		sb.setText(text);
		return sb;
	}

	protected String getSummary(ProjectProvider data, ColumnConfigurator column) {
		ColumnLabelProvider lp = column.getLabelProvider();
		if(lp instanceof AbstractProjectLabelProvider){
			return ((AbstractProjectLabelProvider) lp).getSummary(data);
		}
		return "";
	}

	protected boolean displaySummary() {
		return false;
	}

	@Override
	protected String getProjectSetPageLabel() {
		String projectSetName = data.getProjectSetName();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:" + FONT_SIZE
				+ "pt'>"); //$NON-NLS-1$
		sb.append(projectSetName + " " + getTitle()); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

	protected abstract String getTitle();

}
