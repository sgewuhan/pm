package com.sg.business.performence.ui.calendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.toolkit.CalendarToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.IRefreshablePart;

public abstract class ResourceCalender extends ViewPart implements
		IRefreshablePart {

	protected int groupcount = 3;
	protected GridTreeViewer viewer;
	private boolean hasLoaded;
	private Calendar calendar;
	private GridColumn labelColumn;

	private Font font;
	private HashMap<GridColumnGroup, Boolean> groupCache = new HashMap<GridColumnGroup, Boolean>();

	@Override
	public void createPartControl(Composite parent) {
		viewer = new GridTreeViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setContentProvider(getContentProvider());
		viewer.setAutoExpandLevel(2);

		createGridViewerEditor();

		calendar = Calendar.getInstance();
		font = new Font(parent.getDisplay(), "Arial", 14, SWT.NORMAL);
		initdata();
	}


	protected void initdata() {
		setDisplay3Month();
	}
	

	protected abstract IContentProvider getContentProvider();

	private void createLabelColumn() {
		if (labelColumn != null) {
			return;
		}
		labelColumn = new GridColumn(viewer.getGrid(), SWT.NONE);
		labelColumn.setWidth(180);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		labelColumn.setAlignment(SWT.LEFT);
		labelColumn.setHeaderFont(font);
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		labelColumn.setText("");

		GridViewerColumn vColumn = new GridViewerColumn(viewer, labelColumn);
		vColumn.setLabelProvider(new PrimaryObjectLabelProvider());

		// »ã×ÜÁÐ
		GridColumn summaryColumn = new GridColumn(viewer.getGrid(), SWT.NONE);
		summaryColumn.setWidth(80);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		summaryColumn.setAlignment(SWT.CENTER);
		summaryColumn.setHeaderFont(font);
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		summaryColumn.setText("P/A");
		summaryColumn.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_SUMMARY_16));
		vColumn = new GridViewerColumn(viewer, summaryColumn);
		vColumn.setLabelProvider(new TotalSummaryLabelProvider());

	}

	private void createMonthGroup(final int year, final int month,
			boolean expand) {
		final GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),
				SWT.TOGGLE);

		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		// String yearName = ""+calendar.get(Calendar.YEAR);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, RWT.getLocale());

		group.setText(monthName);

		if (expand) {
			int days = calendar.getActualMaximum(Calendar.DATE);
			for (int i = 0; i < days; i++) {
				createDayColumn(group, year, month, i + 1);
			}
			groupLoadColumns(group);
		}

		createSummaryColumn(group, year, month);

		group.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(TreeEvent e) {
				if (!isGroupColumnsLoaded(group)) {
					int days = calendar.getActualMaximum(Calendar.DATE);
					for (int i = 0; i < days; i++) {
						createDayColumn(group, year, month, i + 1);
					}
					viewer.refresh(true);
				}
			}

			@Override
			public void treeCollapsed(TreeEvent e) {
			}
		});
		group.setExpanded(expand);

	}

	private void groupLoadColumns(GridColumnGroup group) {
		groupCache.put(group, Boolean.TRUE);
	}

	private boolean isGroupColumnsLoaded(GridColumnGroup group) {
		return Boolean.TRUE.equals(groupCache.get(group));
	}

	private void createSummaryColumn(GridColumnGroup group, int year, int month) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(80);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.CENTER);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		column.setDetail(false);
		column.setSummary(true); // column.setMinimumWidth( 100 );
		// column.setDetail(true);
		column.setText("P/A");
		column.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_SUMMARY_16));

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		MonthSummaryLabelProvider labelProvider = new MonthSummaryLabelProvider(
				year, month);
		vColumn.setLabelProvider(labelProvider);

	}

	private void createDayColumn(GridColumnGroup group, final int year,
			final int month, final int dateOfMonth) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(36);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.RIGHT);
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setMinimumWidth( 100 );
		column.setDetail(true);
		column.setSummary(false);
		column.setText("" + dateOfMonth);

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);

		DateSummaryLabelProvider labelProvider = new DateSummaryLabelProvider(
				year, month, dateOfMonth);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dateOfMonth);

		double hours = CalendarToolkit.getWorkingHours(cal.getTime());
		labelProvider.setWorkingHours(hours);
		vColumn.setLabelProvider(labelProvider);

		vColumn.setEditingSupport(getEditingSupport(year, month, dateOfMonth));
	}

	protected abstract EditingSupport getEditingSupport(int year, int month,
			int dateOfMonth);

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		loadData(true);
	}

	private void loadData(boolean reload) {
		if (!reload && hasLoaded) {
			viewer.refresh(true);
			return;
		}

		List<PrimaryObject> input = getInput();

		viewer.setInput(input);
		hasLoaded = true;

	}

	protected abstract List<PrimaryObject> getInput();

	private void createGridViewerEditor() {
		ColumnViewerEditorActivationStrategy strategy = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				int type = event.eventType;
				int keyCode = event.keyCode;
				return type == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| type == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| (type == ColumnViewerEditorActivationEvent.KEY_PRESSED && keyCode == SWT.CR)
						|| type == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		int feature = ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION;
		GridViewerEditor.create(viewer, strategy, feature);
	}

	protected void disposeGrid() {
		GridColumnGroup[] gps = viewer.getGrid().getColumnGroups();
		for (int i = 0; i < gps.length; i++) {
			gps[i].dispose();
		}
		groupCache.clear();
	}

	protected void createGrid() {
		final Display display = getSite().getShell().getDisplay();
		final ServerPushSession push = new ServerPushSession();
		push.start();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				display.syncExec(new Runnable() {

					@Override
					public void run() {
						createLabelColumn();
						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH);

						if (groupcount == 12) {
							for (int i = 0; i < groupcount; i++) {
								createMonthGroup(year, i, false);
							}
						} else {
							for (int i = month - groupcount + 1; i < month + 1; i++) {
								createMonthGroup(year, i, month == i);
							}

						}

						labelColumn.setText("" + year);
						loadData(false);

						push.stop();
					}

				});

			}

		});
		t.setDaemon(true);
		t.start();
	}

	public void setDisplayNext() {
		calendar.add(Calendar.MONTH, groupcount);
		disposeGrid();
		createGrid();
	}

	public void setDisplayPrevious() {
		calendar.add(Calendar.MONTH, -groupcount);
		disposeGrid();
		createGrid();
	}

	public void setDisplayYear() {
		disposeGrid();
		groupcount = 12;
		createGrid();
	}

	public void setDisplay3Month() {
		disposeGrid();
		groupcount = 3;
		createGrid();
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

	public void expandAll() {
		viewer.expandAll();
	}

	public void collapse() {
		viewer.collapseAll();
	}
}
