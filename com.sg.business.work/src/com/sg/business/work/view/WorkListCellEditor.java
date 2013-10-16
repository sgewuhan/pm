package com.sg.business.work.view;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorksSummary;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.WorksPerformence;

public class WorkListCellEditor extends CellEditor {

	private User user;
	private int dateOfMonth;
	private int year;
	private int month;
//	private Button navi;
	private Shell shell;
	private TableViewer viewer;

	public WorkListCellEditor(Composite parent, User user, int year, int month,
			int dateOfMonth) {
		super(parent);
		this.user = user;
		this.year = year;
		this.month = month;
		this.dateOfMonth = dateOfMonth;
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		ViewerCell cell = (ViewerCell) activationEvent.getSource();
		IWorksSummary ws = user.getAdapter(IWorksSummary.class);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dateOfMonth);
		List<PrimaryObject[]> worklist = ws.getWorkOfWorksSummaryOfDateCode(
				user.getUserid(), cal.getTime());
		
		
		Rectangle cellBounds = cell.getBounds();
		Point location = cell.getControl().toDisplay(cellBounds.x,
				cellBounds.y);
		
		Rectangle displayBounds = cell.getControl().getDisplay().getBounds();
		
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		
//		Point p = cell.getControl().toDisplay(cell.getBounds().x,
//				cell.getBounds().y + cell.getBounds().height);
		
		//能否与控件横向对齐显示
		if(location.x + size.x >displayBounds.width){
			location.x = displayBounds.width - size.x -1;
		}
		
		//能否在控件下方显示
		if(location.y +cellBounds.height+ size.y >displayBounds.height){
			location.y = location.y - size.y;
		}else{
			location.y = location.y + cellBounds.height;
		}
		
		
		open(worklist, location);

		super.activate(activationEvent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite navi = new Composite(parent, SWT.NONE);
//		navi.setData(RWT.CUSTOM_VARIANT, "incell");
//		navi.setBackground(Widgets.getColor(navi.getDisplay(), 255, 255, 255));
//		navi.setImage(BusinessResource.getImage(BusinessResource.IMAGE_NAVI_24));
//		navi.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				deactivate();
//			}
//		});

		Shell parentShell = parent.getShell();
		createShell(parentShell);
		return navi;
	}

	private void createShell(Shell parentShell) {
		shell = new Shell(parentShell, SWT.RESIZE | SWT.BORDER);
		shell.setLayout(new FillLayout());
		viewer = new TableViewer(shell, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("工作");
		col.getColumn().setWidth(200);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				PrimaryObject[] pos = (PrimaryObject[]) element;
				Project project = ((Work) pos[0]).getProject();
				return project == null ? ((Work) pos[0]).getLabel() : (project
						+ " " + ((Work) pos[0]).getLabel());
			}
		});

		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("计划工时");
		col.getColumn().setWidth(60);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				PrimaryObject[] pos = (PrimaryObject[]) element;
				if(pos[1]==null){
					return "";
				}
				Object works = pos[1].getValue(WorksPerformence.F_WORKS);
				if (works instanceof Double) {
					double value = ((Double) works).doubleValue();
					DecimalFormat df = new DecimalFormat("#####");
					return df.format(value);
				}
				return "";
			}
		});
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("实际工时");
		col.getColumn().setWidth(60);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				PrimaryObject[] pos = (PrimaryObject[]) element;
				if(pos[2]==null){
					return "";
				}
				Object works = pos[2].getValue(WorksPerformence.F_WORKS);
				if (works instanceof Double) {
					double value = ((Double) works).doubleValue();
					DecimalFormat df = new DecimalFormat("#####");
					return df.format(value);
				}
				return "";
			}
		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		shell.addShellListener(new ShellListener() {

			@Override
			public void shellDeactivated(ShellEvent e) {
				shell.setVisible(false);
			}

			@Override
			public void shellClosed(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
	}

//	protected void showWorkList() {
//		IWorksSummary ws = user.getAdapter(IWorksSummary.class);
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, year);
//		cal.set(Calendar.MONTH, month);
//		cal.set(Calendar.DATE, dateOfMonth);
//		List<PrimaryObject[]> worklist = ws.getWorkOfWorksSummaryOfDateCode(
//				user.getUserid(), cal.getTime());
//
//		open(worklist, null);
//
//	}

	private void open(List<PrimaryObject[]> worklist, Point p) {
		viewer.setInput(worklist);
		shell.pack();
		shell.setLocation(p);
		shell.open();
	}

	@Override
	protected Object doGetValue() {
		return null;
	}

	@Override
	protected void doSetFocus() {

	}

	@Override
	protected void doSetValue(Object value) {

	}

	@Override
	public void dispose() {
		shell.dispose();
		super.dispose();
	}

	@Override
	protected int getDoubleClickTimeout() {
		return 0;
	}
}
