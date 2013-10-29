package com.sg.business.finance.rndcost;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class WorkOrderCostViewer extends TableViewer {

	public WorkOrderCostViewer(Composite parent, int style, String[][] columns) {
		super(parent, style);
		
		getTable().setLinesVisible(true);
		getTable().setData(RWT.FIXED_COLUMNS,new Integer(1));
		setContentProvider(ArrayContentProvider.getInstance());
		
		createColumns(columns);
	}
	
	
	private void createColumns(String[][] columns) {
		createLabelColumn();
		for (int i = 0; i < columns.length; i++) {
			createColumn(columns[i][0], columns[i][1]);
		}
	}

	private void createLabelColumn() {
		TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.LEFT);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() );
		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(120);
	}

	private TableColumn createColumn( String accountNumber,
			String accountName) {
		TableViewerColumn viewerColumn = new TableViewerColumn(this,
				SWT.RIGHT);
		viewerColumn.setLabelProvider(new AccountDurationLabelProvider(
				accountNumber));

		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(120);
		String columnTitle;
		if (accountName.contains("-")) {
			columnTitle = accountName.replace('-', '\n');
		} else {
			columnTitle = accountName + "\n ";
		}
		column.setText(columnTitle);
		column.setToolTipText(accountName + "(" + accountNumber + ")\n点输入定位列");
		column.setData("accountNumber", accountNumber);
		column.setData("accountName", accountName);
		return column;
	}

}
