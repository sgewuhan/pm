package com.sg.business.finance.rndcost;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.widgets.ITableAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

@SuppressWarnings("restriction")
public class RNDCostAdjustmentView extends ViewPart {

	private CostCenterViewer costCenterViewer;
	private WorkOrderCostViewer workOrderViewer;

	public RNDCostAdjustmentView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		costCenterViewer = new CostCenterViewer(parent, SWT.FULL_SELECTION);
		FormData fd = new FormData();
		Table costCenterTable = costCenterViewer.getTable();
		costCenterTable.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		workOrderViewer = new WorkOrderCostViewer(parent, SWT.FULL_SELECTION,
				costCenterViewer.suggestArray);
		Table workOrderTable = workOrderViewer.getTable();
		fd = new FormData();
		workOrderTable.setLayoutData(fd);
		fd.top = new FormAttachment(costCenterTable);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		syncHorizontalScroll(workOrderTable, costCenterTable);
		syncColumnSize(costCenterTable, workOrderTable);

		loadata();
	}

	private void loadata() {
		// 获取成本中心按期间的各科目金额
		CostCenterDuration ccd = new CostCenterDuration();
		costCenterViewer.setContentProvider(ccd);
		Calendar cal = Calendar.getInstance();
		costCenterViewer.setInput(new CostCenterDurationQueryParameter(cal
				.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, null));

	}

	private void syncColumnSize(Table table1, Table table2) {
		final TableColumn[] cols1 = table1.getColumns();
		final TableColumn[] cols2 = table2.getColumns();
		for (int i = 0; i < cols1.length; i++) {
			final int k = i;
			cols1[i].addControlListener(new ControlListener() {

				@Override
				public void controlResized(ControlEvent e) {
					cols2[k].setWidth(cols1[k].getWidth());
				}

				@Override
				public void controlMoved(ControlEvent e) {
				}
			});
		}
	}

	private int scrolling;

	private void syncHorizontalScroll(final Table table1, final Table table2) {
		table1.getHorizontalBar().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int scrolling = ((ScrollBar) event.widget).getSelection();
				if(scrolling == RNDCostAdjustmentView.this.scrolling){
					return;
				}
				RNDCostAdjustmentView.this.scrolling = scrolling;
				scrollingTable(table2, scrolling);
			}
		});

		table2.getHorizontalBar().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				int scrolling = ((ScrollBar) event.widget).getSelection();
				if(scrolling == RNDCostAdjustmentView.this.scrolling){
					return;
				}
				RNDCostAdjustmentView.this.scrolling = scrolling;
				scrollingTable(table1, scrolling);
			}
		});

	}

	protected void scrollingTable(Table table, int table2Scrolling) {
		table.getAdapter(ITableAdapter.class).setLeftOffset(table2Scrolling);
	}

	@Override
	public void setFocus() {
		costCenterViewer.getControl().setFocus();
	}

}
